package dev.artixdev.libs.com.mongodb.internal.selector;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

public class ServerAddressSelector implements ServerSelector {
   private final ServerAddress serverAddress;

   public ServerAddressSelector(ServerAddress serverAddress) {
      this.serverAddress = (ServerAddress)Assertions.notNull("serverAddress", serverAddress);
   }

   public ServerAddress getServerAddress() {
      return this.serverAddress;
   }

   public List<ServerDescription> select(ClusterDescription clusterDescription) {
      ServerDescription serverDescription = ClusterDescriptionHelper.getByServerAddress(clusterDescription, this.serverAddress);
      return serverDescription != null ? Collections.singletonList(serverDescription) : Collections.emptyList();
   }

   public String toString() {
      return "ServerAddressSelector{serverAddress=" + this.serverAddress + '}';
   }
}
