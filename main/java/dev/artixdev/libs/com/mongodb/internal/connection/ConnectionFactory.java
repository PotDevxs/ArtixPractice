package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;

interface ConnectionFactory {
   Connection create(InternalConnection var1, ProtocolExecutor var2, ClusterConnectionMode var3);

   AsyncConnection createAsync(InternalConnection var1, ProtocolExecutor var2, ClusterConnectionMode var3);
}
