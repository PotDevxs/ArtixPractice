package dev.artixdev.libs.com.mongodb.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.ClusterListener;
import dev.artixdev.libs.com.mongodb.internal.connection.ServerAddressHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

@Immutable
public final class ClusterSettings {
   private final String srvHost;
   private final Integer srvMaxHosts;
   private final String srvServiceName;
   private final List<ServerAddress> hosts;
   private final ClusterConnectionMode mode;
   private final ClusterType requiredClusterType;
   private final String requiredReplicaSetName;
   private final ServerSelector serverSelector;
   private final long localThresholdMS;
   private final long serverSelectionTimeoutMS;
   private final List<ClusterListener> clusterListeners;

   public static ClusterSettings.Builder builder() {
      return new ClusterSettings.Builder();
   }

   public static ClusterSettings.Builder builder(ClusterSettings clusterSettings) {
      return builder().applySettings(clusterSettings);
   }

   @Nullable
   public String getSrvHost() {
      return this.srvHost;
   }

   @Nullable
   public Integer getSrvMaxHosts() {
      return this.srvMaxHosts;
   }

   public String getSrvServiceName() {
      return this.srvServiceName;
   }

   public List<ServerAddress> getHosts() {
      return this.hosts;
   }

   public ClusterConnectionMode getMode() {
      return this.mode;
   }

   public ClusterType getRequiredClusterType() {
      return this.requiredClusterType;
   }

   @Nullable
   public String getRequiredReplicaSetName() {
      return this.requiredReplicaSetName;
   }

   @Nullable
   public ServerSelector getServerSelector() {
      return this.serverSelector;
   }

   public long getServerSelectionTimeout(TimeUnit timeUnit) {
      return timeUnit.convert(this.serverSelectionTimeoutMS, TimeUnit.MILLISECONDS);
   }

   public long getLocalThreshold(TimeUnit timeUnit) {
      return timeUnit.convert(this.localThresholdMS, TimeUnit.MILLISECONDS);
   }

   public List<ClusterListener> getClusterListeners() {
      return this.clusterListeners;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ClusterSettings that = (ClusterSettings)o;
         return this.localThresholdMS == that.localThresholdMS && this.serverSelectionTimeoutMS == that.serverSelectionTimeoutMS && Objects.equals(this.srvHost, that.srvHost) && Objects.equals(this.srvMaxHosts, that.srvMaxHosts) && this.srvServiceName.equals(that.srvServiceName) && this.hosts.equals(that.hosts) && this.mode == that.mode && this.requiredClusterType == that.requiredClusterType && Objects.equals(this.requiredReplicaSetName, that.requiredReplicaSetName) && Objects.equals(this.serverSelector, that.serverSelector) && this.clusterListeners.equals(that.clusterListeners);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.srvHost, this.srvMaxHosts, this.srvServiceName, this.hosts, this.mode, this.requiredClusterType, this.requiredReplicaSetName, this.serverSelector, this.localThresholdMS, this.serverSelectionTimeoutMS, this.clusterListeners});
   }

   public String toString() {
      return "{" + (this.hosts.isEmpty() ? "" : "hosts=" + this.hosts) + (this.srvHost == null ? "" : ", srvHost=" + this.srvHost) + (this.srvServiceName == null ? "" : ", srvServiceName=" + this.srvServiceName) + (this.srvMaxHosts == null ? "" : ", srvMaxHosts=" + this.srvMaxHosts) + ", mode=" + this.mode + ", requiredClusterType=" + this.requiredClusterType + ", requiredReplicaSetName='" + this.requiredReplicaSetName + '\'' + ", serverSelector='" + this.serverSelector + '\'' + ", clusterListeners='" + this.clusterListeners + '\'' + ", serverSelectionTimeout='" + this.serverSelectionTimeoutMS + " ms" + '\'' + ", localThreshold='" + this.localThresholdMS + " ms" + '\'' + '}';
   }

   public String getShortDescription() {
      return "{" + (this.hosts.isEmpty() ? "" : "hosts=" + this.hosts) + (this.srvHost == null ? "" : ", srvHost=" + this.srvHost) + ", mode=" + this.mode + ", requiredClusterType=" + this.requiredClusterType + ", serverSelectionTimeout='" + this.serverSelectionTimeoutMS + " ms" + '\'' + (this.requiredReplicaSetName == null ? "" : ", requiredReplicaSetName='" + this.requiredReplicaSetName + '\'') + '}';
   }

   private ClusterSettings(ClusterSettings.Builder builder) {
      if (builder.srvHost != null) {
         if (builder.srvHost.contains(":")) {
            throw new IllegalArgumentException("The srvHost can not contain a host name that specifies a port");
         }

         if (builder.srvHost.split("\\.").length < 3) {
            throw new IllegalArgumentException(String.format("An SRV host name '%s' was provided that does not contain at least three parts. It must contain a hostname, domain name and a top level domain.", builder.srvHost));
         }
      }

      if (builder.hosts.size() > 1 && builder.requiredClusterType == ClusterType.STANDALONE) {
         throw new IllegalArgumentException("Multiple hosts cannot be specified when using ClusterType.STANDALONE.");
      } else {
         if (builder.requiredReplicaSetName != null) {
            if (builder.requiredClusterType == ClusterType.UNKNOWN) {
               builder.requiredClusterType = ClusterType.REPLICA_SET;
            } else if (builder.requiredClusterType != ClusterType.REPLICA_SET) {
               throw new IllegalArgumentException("When specifying a replica set name, only ClusterType.UNKNOWN and ClusterType.REPLICA_SET are valid.");
            }
         }

         if (builder.mode == ClusterConnectionMode.LOAD_BALANCED && builder.srvHost == null && builder.hosts.size() != 1) {
            throw new IllegalArgumentException("Multiple hosts cannot be specified when in load balancing mode");
         } else {
            this.srvHost = builder.srvHost;
            this.srvMaxHosts = builder.srvMaxHosts;
            this.srvServiceName = builder.srvServiceName;
            this.hosts = builder.hosts;
            if (this.srvHost != null) {
               if (builder.mode == ClusterConnectionMode.SINGLE) {
                  throw new IllegalArgumentException("An SRV host name was provided but the connection mode is not MULTIPLE");
               }

               this.mode = builder.mode != null ? builder.mode : ClusterConnectionMode.MULTIPLE;
            } else {
               if (builder.mode == ClusterConnectionMode.SINGLE && builder.hosts.size() > 1) {
                  throw new IllegalArgumentException("Can not directly connect to more than one server");
               }

               this.mode = builder.mode != null ? builder.mode : (this.hosts.size() == 1 ? ClusterConnectionMode.SINGLE : ClusterConnectionMode.MULTIPLE);
            }

            this.requiredReplicaSetName = builder.requiredReplicaSetName;
            this.requiredClusterType = builder.requiredClusterType;
            this.localThresholdMS = builder.localThresholdMS;
            this.serverSelector = builder.serverSelector;
            this.serverSelectionTimeoutMS = builder.serverSelectionTimeoutMS;
            this.clusterListeners = Collections.unmodifiableList(builder.clusterListeners);
         }
      }
   }

   // $FF: synthetic method
   ClusterSettings(ClusterSettings.Builder x0, Object x1) {
      this(x0);
   }

   @NotThreadSafe
   public static final class Builder {
      private static final List<ServerAddress> DEFAULT_HOSTS = Collections.singletonList(new ServerAddress());
      private String srvHost;
      private Integer srvMaxHosts;
      private String srvServiceName;
      private List<ServerAddress> hosts;
      private ClusterConnectionMode mode;
      private ClusterType requiredClusterType;
      private String requiredReplicaSetName;
      private ServerSelector serverSelector;
      private long serverSelectionTimeoutMS;
      private long localThresholdMS;
      private List<ClusterListener> clusterListeners;

      private Builder() {
         this.srvServiceName = "mongodb";
         this.hosts = DEFAULT_HOSTS;
         this.requiredClusterType = ClusterType.UNKNOWN;
         this.serverSelectionTimeoutMS = TimeUnit.MILLISECONDS.convert(30L, TimeUnit.SECONDS);
         this.localThresholdMS = TimeUnit.MILLISECONDS.convert(15L, TimeUnit.MILLISECONDS);
         this.clusterListeners = new ArrayList();
      }

      public ClusterSettings.Builder applySettings(ClusterSettings clusterSettings) {
         Assertions.notNull("clusterSettings", clusterSettings);
         this.srvHost = clusterSettings.srvHost;
         this.srvServiceName = clusterSettings.srvServiceName;
         this.srvMaxHosts = clusterSettings.srvMaxHosts;
         this.hosts = clusterSettings.hosts;
         this.mode = clusterSettings.mode;
         this.requiredReplicaSetName = clusterSettings.requiredReplicaSetName;
         this.requiredClusterType = clusterSettings.requiredClusterType;
         this.localThresholdMS = clusterSettings.localThresholdMS;
         this.serverSelectionTimeoutMS = clusterSettings.serverSelectionTimeoutMS;
         this.clusterListeners = new ArrayList(clusterSettings.clusterListeners);
         this.serverSelector = clusterSettings.serverSelector;
         return this;
      }

      public ClusterSettings.Builder srvHost(String srvHost) {
         if (this.hosts != DEFAULT_HOSTS) {
            throw new IllegalArgumentException("Can not set both hosts and srvHost");
         } else {
            this.srvHost = srvHost;
            return this;
         }
      }

      public ClusterSettings.Builder srvMaxHosts(Integer srvMaxHosts) {
         this.srvMaxHosts = srvMaxHosts;
         return this;
      }

      public ClusterSettings.Builder srvServiceName(String srvServiceName) {
         this.srvServiceName = (String)Assertions.notNull("srvServiceName", srvServiceName);
         return this;
      }

      public ClusterSettings.Builder hosts(List<ServerAddress> hosts) {
         Assertions.notNull("hosts", hosts);
         if (hosts.isEmpty()) {
            throw new IllegalArgumentException("hosts list may not be empty");
         } else if (this.srvHost != null) {
            throw new IllegalArgumentException("srvHost must be null");
         } else {
            Set<ServerAddress> hostsSet = new LinkedHashSet(hosts.size());
            Iterator var3 = hosts.iterator();

            while(var3.hasNext()) {
               ServerAddress serverAddress = (ServerAddress)var3.next();
               Assertions.notNull("serverAddress", serverAddress);
               hostsSet.add(ServerAddressHelper.createServerAddress(serverAddress.getHost(), serverAddress.getPort()));
            }

            this.hosts = Collections.unmodifiableList(new ArrayList(hostsSet));
            return this;
         }
      }

      public ClusterSettings.Builder mode(ClusterConnectionMode mode) {
         this.mode = (ClusterConnectionMode)Assertions.notNull("mode", mode);
         return this;
      }

      public ClusterSettings.Builder requiredReplicaSetName(@Nullable String requiredReplicaSetName) {
         this.requiredReplicaSetName = requiredReplicaSetName;
         return this;
      }

      public ClusterSettings.Builder requiredClusterType(ClusterType requiredClusterType) {
         this.requiredClusterType = (ClusterType)Assertions.notNull("requiredClusterType", requiredClusterType);
         return this;
      }

      public ClusterSettings.Builder localThreshold(long localThreshold, TimeUnit timeUnit) {
         Assertions.isTrueArgument("localThreshold must be >= 0", localThreshold >= 0L);
         this.localThresholdMS = TimeUnit.MILLISECONDS.convert(localThreshold, timeUnit);
         return this;
      }

      public ClusterSettings.Builder serverSelector(ServerSelector serverSelector) {
         this.serverSelector = serverSelector;
         return this;
      }

      public ClusterSettings.Builder serverSelectionTimeout(long serverSelectionTimeout, TimeUnit timeUnit) {
         this.serverSelectionTimeoutMS = TimeUnit.MILLISECONDS.convert(serverSelectionTimeout, timeUnit);
         return this;
      }

      public ClusterSettings.Builder addClusterListener(ClusterListener clusterListener) {
         Assertions.notNull("clusterListener", clusterListener);
         this.clusterListeners.add(clusterListener);
         return this;
      }

      public ClusterSettings.Builder clusterListenerList(List<ClusterListener> clusterListeners) {
         Assertions.notNull("clusterListeners", clusterListeners);
         this.clusterListeners = new ArrayList(clusterListeners);
         return this;
      }

      public ClusterSettings.Builder applyConnectionString(ConnectionString connectionString) {
         Boolean directConnection = connectionString.isDirectConnection();
         Boolean loadBalanced = connectionString.isLoadBalanced();
         Integer serverSelectionTimeout;
         if (loadBalanced != null && loadBalanced) {
            this.mode(ClusterConnectionMode.LOAD_BALANCED);
            if (connectionString.isSrvProtocol()) {
               this.srvHost((String)connectionString.getHosts().get(0));
            } else {
               this.hosts(Collections.singletonList(ServerAddressHelper.createServerAddress((String)connectionString.getHosts().get(0))));
            }
         } else if (connectionString.isSrvProtocol()) {
            this.mode(ClusterConnectionMode.MULTIPLE);
            this.srvHost((String)connectionString.getHosts().get(0));
            serverSelectionTimeout = connectionString.getSrvMaxHosts();
            if (serverSelectionTimeout != null) {
               this.srvMaxHosts(serverSelectionTimeout);
            }

            String srvServiceName = connectionString.getSrvServiceName();
            if (srvServiceName != null) {
               this.srvServiceName(srvServiceName);
            }
         } else if ((directConnection == null || !directConnection) && (directConnection != null || connectionString.getHosts().size() != 1 || connectionString.getRequiredReplicaSetName() != null)) {
            List<ServerAddress> seedList = (List)connectionString.getHosts().stream().map(ServerAddressHelper::createServerAddress).collect(Collectors.toList());
            this.mode(ClusterConnectionMode.MULTIPLE).hosts(seedList);
         } else {
            this.mode(ClusterConnectionMode.SINGLE).hosts(Collections.singletonList(ServerAddressHelper.createServerAddress((String)connectionString.getHosts().get(0))));
         }

         this.requiredReplicaSetName(connectionString.getRequiredReplicaSetName());
         serverSelectionTimeout = connectionString.getServerSelectionTimeout();
         if (serverSelectionTimeout != null) {
            this.serverSelectionTimeout((long)serverSelectionTimeout, TimeUnit.MILLISECONDS);
         }

         Integer localThreshold = connectionString.getLocalThreshold();
         if (localThreshold != null) {
            this.localThreshold((long)localThreshold, TimeUnit.MILLISECONDS);
         }

         return this;
      }

      public ClusterSettings build() {
         return new ClusterSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
