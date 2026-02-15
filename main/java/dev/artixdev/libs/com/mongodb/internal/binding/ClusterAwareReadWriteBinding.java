package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.internal.connection.Cluster;

public interface ClusterAwareReadWriteBinding extends ReadWriteBinding {
   Cluster getCluster();

   ConnectionSource getConnectionSource(ServerAddress var1);
}
