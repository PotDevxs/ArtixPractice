package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.connection.ServerDescription;

public class ServerTuple {
   private final Server server;
   private final ServerDescription serverDescription;

   public ServerTuple(Server server, ServerDescription serverDescription) {
      this.server = server;
      this.serverDescription = serverDescription;
   }

   public Server getServer() {
      return this.server;
   }

   public ServerDescription getServerDescription() {
      return this.serverDescription;
   }
}
