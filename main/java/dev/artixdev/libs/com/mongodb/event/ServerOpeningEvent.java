package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerId;

public final class ServerOpeningEvent {
   private final ServerId serverId;

   public ServerOpeningEvent(ServerId serverId) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public String toString() {
      return "ServerOpeningEvent{serverId=" + this.serverId + '}';
   }
}
