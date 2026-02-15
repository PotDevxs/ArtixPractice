package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;

public interface ClusterableServerFactory {
   ClusterableServer create(Cluster var1, ServerAddress var2);

   ServerSettings getSettings();
}
