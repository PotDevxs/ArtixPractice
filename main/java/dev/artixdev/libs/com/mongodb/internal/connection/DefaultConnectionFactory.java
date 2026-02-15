package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;

class DefaultConnectionFactory implements ConnectionFactory {
   public Connection create(InternalConnection internalConnection, ProtocolExecutor executor, ClusterConnectionMode clusterConnectionMode) {
      return new DefaultServerConnection(internalConnection, executor, clusterConnectionMode);
   }

   public AsyncConnection createAsync(InternalConnection internalConnection, ProtocolExecutor executor, ClusterConnectionMode clusterConnectionMode) {
      return new DefaultServerConnection(internalConnection, executor, clusterConnectionMode);
   }
}
