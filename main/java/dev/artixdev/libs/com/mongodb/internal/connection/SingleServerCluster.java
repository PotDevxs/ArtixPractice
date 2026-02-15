package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import dev.artixdev.libs.com.mongodb.MongoConfigurationException;
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

public final class SingleServerCluster extends BaseCluster {
   private static final Logger LOGGER = Loggers.getLogger("cluster");
   private final AtomicReference<ClusterableServer> server;

   public SingleServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
      super(clusterId, settings, serverFactory);
      Assertions.isTrue("one server in a direct cluster", settings.getHosts().size() == 1);
      Assertions.isTrue("connection mode is single", settings.getMode() == ClusterConnectionMode.SINGLE);
      this.server = new AtomicReference();
      this.withLock(() -> {
         this.server.set(this.createServer((ServerAddress)settings.getHosts().get(0)));
         this.publishDescription(ServerDescription.builder().state(ServerConnectionState.CONNECTING).address((ServerAddress)settings.getHosts().get(0)).build());
      });
   }

   protected void connect() {
      ((ClusterableServer)Assertions.assertNotNull((ClusterableServer)this.server.get())).connect();
   }

   public ClusterableServer getServer(ServerAddress serverAddress) {
      Assertions.isTrue("open", !this.isClosed());
      return (ClusterableServer)Assertions.assertNotNull((ClusterableServer)this.server.get());
   }

   public void close() {
      if (!this.isClosed()) {
         ((ClusterableServer)Assertions.assertNotNull((ClusterableServer)this.server.get())).close();
         super.close();
      }

   }

   public void onChange(ServerDescriptionChangedEvent event) {
      this.withLock(() -> {
         ServerDescription newDescription = event.getNewDescription();
         if (newDescription.isOk()) {
            if (this.getSettings().getRequiredClusterType() != ClusterType.UNKNOWN && this.getSettings().getRequiredClusterType() != newDescription.getClusterType()) {
               newDescription = null;
            } else if (this.getSettings().getRequiredClusterType() == ClusterType.REPLICA_SET && this.getSettings().getRequiredReplicaSetName() != null && !this.getSettings().getRequiredReplicaSetName().equals(newDescription.getSetName())) {
               newDescription = ServerDescription.builder(newDescription).exception(new MongoConfigurationException(String.format("Replica set name '%s' does not match required replica set name of '%s'", newDescription.getSetName(), this.getSettings().getRequiredReplicaSetName()))).type(ServerType.UNKNOWN).setName((String)null).ok(false).build();
               this.publishDescription(ClusterType.UNKNOWN, newDescription);
               return;
            }
         }

         this.publishDescription(newDescription);
      });
   }

   private void publishDescription(ServerDescription serverDescription) {
      ClusterType clusterType = this.getSettings().getRequiredClusterType();
      if (clusterType == ClusterType.UNKNOWN && serverDescription != null) {
         clusterType = serverDescription.getClusterType();
      }

      this.publishDescription(clusterType, serverDescription);
   }

   private void publishDescription(ClusterType clusterType, ServerDescription serverDescription) {
      ClusterDescription currentDescription = this.getCurrentDescription();
      ClusterDescription description = new ClusterDescription(ClusterConnectionMode.SINGLE, clusterType, serverDescription == null ? Collections.emptyList() : Collections.singletonList(serverDescription), this.getSettings(), this.getServerFactory().getSettings());
      this.updateDescription(description);
      this.fireChangeEvent(description, currentDescription);
   }
}
