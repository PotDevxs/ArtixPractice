package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Collection;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;

interface DnsSrvRecordInitializer {
   void initialize(Collection<ServerAddress> var1);

   void initialize(MongoException var1);

   ClusterType getClusterType();
}
