package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.connection.ServerConnectionState;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.types.ObjectId;

public abstract class AbstractMultiServerCluster extends BaseCluster {
   private static final Logger LOGGER = Loggers.getLogger("cluster");
   private ClusterType clusterType;
   private String replicaSetName;
   private ObjectId maxElectionId;
   private Integer maxSetVersion;
   private final ConcurrentMap<ServerAddress, AbstractMultiServerCluster.ServerTuple> addressToServerTupleMap = new ConcurrentHashMap();

   AbstractMultiServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
      super(clusterId, settings, serverFactory);
      Assertions.isTrue("connection mode is multiple", settings.getMode() == ClusterConnectionMode.MULTIPLE);
      this.clusterType = settings.getRequiredClusterType();
      this.replicaSetName = settings.getRequiredReplicaSetName();
   }

   ClusterType getClusterType() {
      return this.clusterType;
   }

   @Nullable
   MongoException getSrvResolutionException() {
      return null;
   }

   protected void initialize(Collection<ServerAddress> serverAddresses) {
      ClusterDescription currentDescription = this.getCurrentDescription();
      this.withLock(() -> {
         Iterator var3 = serverAddresses.iterator();

         while(var3.hasNext()) {
            ServerAddress serverAddress = (ServerAddress)var3.next();
            this.addServer(serverAddress);
         }

         ClusterDescription newDescription = this.updateDescription();
         this.fireChangeEvent(newDescription, currentDescription);
      });
   }

   protected void connect() {
      Iterator var1 = this.addressToServerTupleMap.values().iterator();

      while(var1.hasNext()) {
         AbstractMultiServerCluster.ServerTuple cur = (AbstractMultiServerCluster.ServerTuple)var1.next();
         cur.server.connect();
      }

   }

   public void close() {
      this.withLock(() -> {
         if (!this.isClosed()) {
            Iterator var1 = this.addressToServerTupleMap.values().iterator();

            while(var1.hasNext()) {
               AbstractMultiServerCluster.ServerTuple serverTuple = (AbstractMultiServerCluster.ServerTuple)var1.next();
               serverTuple.server.close();
            }
         }

         super.close();
      });
   }

   public ClusterableServer getServer(ServerAddress serverAddress) {
      Assertions.isTrue("is open", !this.isClosed());
      AbstractMultiServerCluster.ServerTuple serverTuple = (AbstractMultiServerCluster.ServerTuple)this.addressToServerTupleMap.get(serverAddress);
      return serverTuple == null ? null : serverTuple.server;
   }

   void onChange(Collection<ServerAddress> newHosts) {
      this.withLock(() -> {
         if (!this.isClosed()) {
            Iterator iterator = newHosts.iterator();

            while(iterator.hasNext()) {
               ServerAddress cur = (ServerAddress)iterator.next();
               this.addServer(cur);
            }

            iterator = this.addressToServerTupleMap.values().iterator();

            while(iterator.hasNext()) {
               AbstractMultiServerCluster.ServerTuple curx = (AbstractMultiServerCluster.ServerTuple)iterator.next();
               if (!newHosts.contains(curx.description.getAddress())) {
                  if (LOGGER.isInfoEnabled()) {
                     LOGGER.info(String.format("Removing %s from client view of cluster.", curx.description.getAddress()));
                  }

                  iterator.remove();
                  curx.server.close();
               }
            }

            ClusterDescription oldClusterDescription = this.getCurrentDescription();
            ClusterDescription newClusterDescription = this.updateDescription();
            this.fireChangeEvent(newClusterDescription, oldClusterDescription);
         }
      });
   }

   public void onChange(ServerDescriptionChangedEvent event) {
      this.withLock(() -> {
         if (!this.isClosed()) {
            ServerDescription newDescription = event.getNewDescription();
            if (LOGGER.isTraceEnabled()) {
               LOGGER.trace(String.format("Handling description changed event for server %s with description %s", newDescription.getAddress(), newDescription));
            }

            AbstractMultiServerCluster.ServerTuple serverTuple = (AbstractMultiServerCluster.ServerTuple)this.addressToServerTupleMap.get(newDescription.getAddress());
            if (serverTuple == null) {
               if (LOGGER.isTraceEnabled()) {
                  LOGGER.trace(String.format("Ignoring description changed event for removed server %s", newDescription.getAddress()));
               }

            } else {
               boolean shouldUpdateDescription = true;
               if (newDescription.isOk()) {
                  if (this.clusterType == ClusterType.UNKNOWN && newDescription.getType() != ServerType.REPLICA_SET_GHOST) {
                     this.clusterType = newDescription.getClusterType();
                     if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(String.format("Discovered cluster type of %s", this.clusterType));
                     }
                  }

                  switch(this.clusterType) {
                  case REPLICA_SET:
                     shouldUpdateDescription = this.handleReplicaSetMemberChanged(newDescription);
                     break;
                  case SHARDED:
                     shouldUpdateDescription = this.handleShardRouterChanged(newDescription);
                     break;
                  case STANDALONE:
                     shouldUpdateDescription = this.handleStandAloneChanged(newDescription);
                  }
               }

               ClusterDescription oldClusterDescription = null;
               ClusterDescription newClusterDescription = null;
               if (shouldUpdateDescription) {
                  serverTuple.description = newDescription;
                  oldClusterDescription = this.getCurrentDescription();
                  newClusterDescription = this.updateDescription();
               }

               if (shouldUpdateDescription) {
                  this.fireChangeEvent(newClusterDescription, oldClusterDescription);
               }

            }
         }
      });
   }

   private boolean handleReplicaSetMemberChanged(ServerDescription newDescription) {
      if (!newDescription.isReplicaSetMember()) {
         LOGGER.error(String.format("Expecting replica set member, but found a %s.  Removing %s from client view of cluster.", newDescription.getType(), newDescription.getAddress()));
         this.removeServer(newDescription.getAddress());
         return true;
      } else if (newDescription.getType() == ServerType.REPLICA_SET_GHOST) {
         LOGGER.info(String.format("Server %s does not appear to be a member of an initiated replica set.", newDescription.getAddress()));
         return true;
      } else {
         if (this.replicaSetName == null) {
            this.replicaSetName = newDescription.getSetName();
         }

         if (!this.replicaSetName.equals(newDescription.getSetName())) {
            LOGGER.error(String.format("Expecting replica set member from set '%s', but found one from set '%s'.  Removing %s from client view of cluster.", this.replicaSetName, newDescription.getSetName(), newDescription.getAddress()));
            this.removeServer(newDescription.getAddress());
            return true;
         } else {
            this.ensureServers(newDescription);
            if (newDescription.getCanonicalAddress() != null && !newDescription.getAddress().equals(new ServerAddress(newDescription.getCanonicalAddress())) && !newDescription.isPrimary()) {
               LOGGER.info(String.format("Canonical address %s does not match server address.  Removing %s from client view of cluster", newDescription.getCanonicalAddress(), newDescription.getAddress()));
               this.removeServer(newDescription.getAddress());
               return true;
            } else if (!newDescription.isPrimary()) {
               return true;
            } else if (this.isStalePrimary(newDescription)) {
               this.invalidatePotentialPrimary(newDescription);
               return false;
            } else {
               this.maxElectionId = (ObjectId)nullSafeMax(newDescription.getElectionId(), this.maxElectionId);
               this.maxSetVersion = (Integer)nullSafeMax(newDescription.getSetVersion(), this.maxSetVersion);
               this.invalidateOldPrimaries(newDescription.getAddress());
               if (this.isNotAlreadyPrimary(newDescription.getAddress())) {
                  LOGGER.info(String.format("Discovered replica set primary %s with max election id %s and max set version %d", newDescription.getAddress(), newDescription.getElectionId(), newDescription.getSetVersion()));
               }

               return true;
            }
         }
      }
   }

   private boolean isStalePrimary(ServerDescription description) {
      ObjectId electionId = description.getElectionId();
      Integer setVersion = description.getSetVersion();
      if (description.getMaxWireVersion() >= 17) {
         return nullSafeCompareTo(electionId, this.maxElectionId) < 0 || nullSafeCompareTo(electionId, this.maxElectionId) == 0 && nullSafeCompareTo(setVersion, this.maxSetVersion) < 0;
      } else {
         return setVersion != null && electionId != null && (nullSafeCompareTo(setVersion, this.maxSetVersion) < 0 || nullSafeCompareTo(setVersion, this.maxSetVersion) == 0 && nullSafeCompareTo(electionId, this.maxElectionId) < 0);
      }
   }

   private void invalidatePotentialPrimary(ServerDescription newDescription) {
      LOGGER.info(String.format("Invalidating potential primary %s whose (set version, election id) tuple of (%d, %s) is less than one already seen of (%d, %s)", newDescription.getAddress(), newDescription.getSetVersion(), newDescription.getElectionId(), this.maxSetVersion, this.maxElectionId));
      ((AbstractMultiServerCluster.ServerTuple)this.addressToServerTupleMap.get(newDescription.getAddress())).server.resetToConnecting();
   }

   private static <T extends Comparable<T>> int nullSafeCompareTo(@Nullable T first, @Nullable T second) {
      if (first == null) {
         return second == null ? 0 : -1;
      } else {
         return second == null ? 1 : first.compareTo(second);
      }
   }

   @Nullable
   private static <T extends Comparable<T>> T nullSafeMax(@Nullable T first, @Nullable T second) {
      if (first == null) {
         return second;
      } else if (second == null) {
         return first;
      } else {
         return first.compareTo(second) >= 0 ? first : second;
      }
   }

   private boolean isNotAlreadyPrimary(ServerAddress address) {
      AbstractMultiServerCluster.ServerTuple serverTuple = (AbstractMultiServerCluster.ServerTuple)this.addressToServerTupleMap.get(address);
      return serverTuple == null || !serverTuple.description.isPrimary();
   }

   private boolean handleShardRouterChanged(ServerDescription newDescription) {
      if (!newDescription.isShardRouter()) {
         LOGGER.error(String.format("Expecting a %s, but found a %s.  Removing %s from client view of cluster.", ServerType.SHARD_ROUTER, newDescription.getType(), newDescription.getAddress()));
         this.removeServer(newDescription.getAddress());
      }

      return true;
   }

   private boolean handleStandAloneChanged(ServerDescription newDescription) {
      if (this.getSettings().getHosts().size() > 1) {
         LOGGER.error(String.format("Expecting a single %s, but found more than one.  Removing %s from client view of cluster.", ServerType.STANDALONE, newDescription.getAddress()));
         this.clusterType = ClusterType.UNKNOWN;
         this.removeServer(newDescription.getAddress());
      }

      return true;
   }

   private void addServer(ServerAddress serverAddress) {
      if (!this.addressToServerTupleMap.containsKey(serverAddress)) {
         if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Adding discovered server %s to client view of cluster", serverAddress));
         }

         ClusterableServer server = this.createServer(serverAddress);
         this.addressToServerTupleMap.put(serverAddress, new AbstractMultiServerCluster.ServerTuple(server, this.getConnectingServerDescription(serverAddress)));
      }

   }

   private void removeServer(ServerAddress serverAddress) {
      AbstractMultiServerCluster.ServerTuple removed = (AbstractMultiServerCluster.ServerTuple)this.addressToServerTupleMap.remove(serverAddress);
      if (removed != null) {
         removed.server.close();
      }

   }

   private void invalidateOldPrimaries(ServerAddress newPrimary) {
      Iterator var2 = this.addressToServerTupleMap.values().iterator();

      while(var2.hasNext()) {
         AbstractMultiServerCluster.ServerTuple serverTuple = (AbstractMultiServerCluster.ServerTuple)var2.next();
         if (!serverTuple.description.getAddress().equals(newPrimary) && serverTuple.description.isPrimary()) {
            if (LOGGER.isInfoEnabled()) {
               LOGGER.info(String.format("Rediscovering type of existing primary %s", serverTuple.description.getAddress()));
            }

            serverTuple.server.invalidate();
         }
      }

   }

   private ServerDescription getConnectingServerDescription(ServerAddress serverAddress) {
      return ServerDescription.builder().state(ServerConnectionState.CONNECTING).address(serverAddress).build();
   }

   private ClusterDescription updateDescription() {
      ClusterDescription newDescription = new ClusterDescription(ClusterConnectionMode.MULTIPLE, this.clusterType, this.getSrvResolutionException(), this.getNewServerDescriptionList(), this.getSettings(), this.getServerFactory().getSettings());
      this.updateDescription(newDescription);
      return newDescription;
   }

   private List<ServerDescription> getNewServerDescriptionList() {
      List<ServerDescription> serverDescriptions = new ArrayList();
      Iterator var2 = this.addressToServerTupleMap.values().iterator();

      while(var2.hasNext()) {
         AbstractMultiServerCluster.ServerTuple cur = (AbstractMultiServerCluster.ServerTuple)var2.next();
         serverDescriptions.add(cur.description);
      }

      return serverDescriptions;
   }

   private void ensureServers(ServerDescription description) {
      if (description.isPrimary() || !this.hasPrimary()) {
         this.addNewHosts(description.getHosts());
         this.addNewHosts(description.getPassives());
         this.addNewHosts(description.getArbiters());
      }

      if (description.isPrimary()) {
         this.removeExtraHosts(description);
      }

   }

   private boolean hasPrimary() {
      Iterator var1 = this.addressToServerTupleMap.values().iterator();

      AbstractMultiServerCluster.ServerTuple serverTuple;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         serverTuple = (AbstractMultiServerCluster.ServerTuple)var1.next();
      } while(!serverTuple.description.isPrimary());

      return true;
   }

   private void addNewHosts(Set<String> hosts) {
      Iterator var2 = hosts.iterator();

      while(var2.hasNext()) {
         String cur = (String)var2.next();
         this.addServer(new ServerAddress(cur));
      }

   }

   private void removeExtraHosts(ServerDescription serverDescription) {
      Set<ServerAddress> allServerAddresses = this.getAllServerAddresses(serverDescription);
      Iterator iterator = this.addressToServerTupleMap.values().iterator();

      while(iterator.hasNext()) {
         AbstractMultiServerCluster.ServerTuple cur = (AbstractMultiServerCluster.ServerTuple)iterator.next();
         if (!allServerAddresses.contains(cur.description.getAddress())) {
            if (LOGGER.isInfoEnabled()) {
               LOGGER.info(String.format("Server %s is no longer a member of the replica set.  Removing from client view of cluster.", cur.description.getAddress()));
            }

            iterator.remove();
            cur.server.close();
         }
      }

   }

   private Set<ServerAddress> getAllServerAddresses(ServerDescription serverDescription) {
      Set<ServerAddress> retVal = new HashSet();
      this.addHostsToSet(serverDescription.getHosts(), retVal);
      this.addHostsToSet(serverDescription.getPassives(), retVal);
      this.addHostsToSet(serverDescription.getArbiters(), retVal);
      return retVal;
   }

   private void addHostsToSet(Set<String> hosts, Set<ServerAddress> retVal) {
      Iterator var3 = hosts.iterator();

      while(var3.hasNext()) {
         String host = (String)var3.next();
         retVal.add(new ServerAddress(host));
      }

   }

   private static final class ServerTuple {
      private final ClusterableServer server;
      private ServerDescription description;

      private ServerTuple(ClusterableServer server, ServerDescription description) {
         this.server = server;
         this.description = description;
      }

      // $FF: synthetic method
      ServerTuple(ClusterableServer x0, ServerDescription x1, Object x2) {
         this(x0, x1);
      }
   }
}
