package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerId;

public final class ServerClosedEvent {
   private final ServerId serverId;

   public ServerClosedEvent(ServerId serverId) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public String toString() {
      return "ServerClosedEvent{serverId=" + this.serverId + '}';
   }
}
