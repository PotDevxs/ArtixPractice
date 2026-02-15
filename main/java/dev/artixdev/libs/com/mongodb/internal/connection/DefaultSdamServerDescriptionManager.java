package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerListener;

@ThreadSafe
final class DefaultSdamServerDescriptionManager implements SdamServerDescriptionManager {
   private final Cluster cluster;
   private final ServerId serverId;
   private final ServerListener serverListener;
   private final ServerMonitor serverMonitor;
   private final ConnectionPool connectionPool;
   private final ClusterConnectionMode connectionMode;
   private volatile ServerDescription description;

   DefaultSdamServerDescriptionManager(Cluster cluster, ServerId serverId, ServerListener serverListener, ServerMonitor serverMonitor, ConnectionPool connectionPool, ClusterConnectionMode connectionMode) {
      this.cluster = cluster;
      this.serverId = (ServerId)Assertions.assertNotNull(serverId);
      this.serverListener = (ServerListener)Assertions.assertNotNull(serverListener);
      this.serverMonitor = (ServerMonitor)Assertions.assertNotNull(serverMonitor);
      this.connectionPool = (ConnectionPool)Assertions.assertNotNull(connectionPool);
      this.connectionMode = (ClusterConnectionMode)Assertions.assertNotNull(connectionMode);
      this.description = ServerDescriptionHelper.unknownConnectingServerDescription(serverId, (Throwable)null);
   }

   public void update(ServerDescription candidateDescription) {
      this.cluster.withLock(() -> {
         if (!TopologyVersionHelper.newer(this.description.getTopologyVersion(), candidateDescription.getTopologyVersion())) {
            ServerType newServerType = candidateDescription.getType();
            boolean markedPoolReady = false;
            if (ServerTypeHelper.isDataBearing(newServerType) || newServerType != ServerType.UNKNOWN && this.connectionMode == ClusterConnectionMode.SINGLE) {
               this.connectionPool.ready();
               markedPoolReady = true;
            }

            this.updateDescription(candidateDescription);
            Throwable candidateDescriptionException = candidateDescription.getException();
            if (candidateDescriptionException != null) {
               Assertions.assertTrue(newServerType == ServerType.UNKNOWN);
               Assertions.assertFalse(markedPoolReady);
               this.connectionPool.invalidate(candidateDescriptionException);
            }

         }
      });
   }

   public void handleExceptionBeforeHandshake(SdamServerDescriptionManager.SdamIssue sdamIssue) {
      this.cluster.withLock(() -> {
         this.handleException(sdamIssue, true);
      });
   }

   public void handleExceptionAfterHandshake(SdamServerDescriptionManager.SdamIssue sdamIssue) {
      this.cluster.withLock(() -> {
         this.handleException(sdamIssue, false);
      });
   }

   public SdamServerDescriptionManager.SdamIssue.Context context() {
      return new SdamServerDescriptionManager.SdamIssue.Context(this.serverId, this.connectionPool.getGeneration(), this.description.getMaxWireVersion());
   }

   public SdamServerDescriptionManager.SdamIssue.Context context(InternalConnection connection) {
      return new SdamServerDescriptionManager.SdamIssue.Context(this.serverId, connection.getGeneration(), connection.getDescription().getMaxWireVersion());
   }

   private void updateDescription(ServerDescription newDescription) {
      ServerDescription previousDescription = this.description;
      this.description = newDescription;
      ServerDescriptionChangedEvent serverDescriptionChangedEvent = new ServerDescriptionChangedEvent(this.serverId, newDescription, previousDescription);
      if (!EventHelper.wouldDescriptionsGenerateEquivalentEvents(newDescription, previousDescription)) {
         this.serverListener.serverDescriptionChanged(serverDescriptionChangedEvent);
      }

      this.cluster.onChange(serverDescriptionChangedEvent);
   }

   private void handleException(SdamServerDescriptionManager.SdamIssue sdamIssue, boolean beforeHandshake) {
      if (!sdamIssue.stale(this.connectionPool, this.description)) {
         if (sdamIssue.relatedToStateChange()) {
            this.updateDescription(sdamIssue.serverDescription());
            if (sdamIssue.serverIsLessThanVersionFourDotTwo() || sdamIssue.relatedToShutdown()) {
               this.connectionPool.invalidate(sdamIssue.exception().orElse(null));
            }

            this.serverMonitor.connect();
         } else if (!sdamIssue.relatedToNetworkNotTimeout() && (!beforeHandshake || !sdamIssue.relatedToNetworkTimeout() && !sdamIssue.relatedToAuth())) {
            if (sdamIssue.relatedToWriteConcern() || !sdamIssue.specific()) {
               this.updateDescription(sdamIssue.serverDescription());
               if (sdamIssue.serverIsLessThanVersionFourDotTwo()) {
                  this.connectionPool.invalidate(sdamIssue.exception().orElse(null));
               }

               this.serverMonitor.connect();
            }
         } else {
            this.updateDescription(sdamIssue.serverDescription());
            this.connectionPool.invalidate(sdamIssue.exception().orElse(null));
            this.serverMonitor.cancelCurrentCheck();
         }

      }
   }
}
