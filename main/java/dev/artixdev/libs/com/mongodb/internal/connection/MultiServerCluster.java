package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;

public final class MultiServerCluster extends AbstractMultiServerCluster {
   public MultiServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
      super(clusterId, settings, serverFactory);
      Assertions.isTrue("srvHost is null", settings.getSrvHost() == null);
      this.initialize(settings.getHosts());
   }
}
