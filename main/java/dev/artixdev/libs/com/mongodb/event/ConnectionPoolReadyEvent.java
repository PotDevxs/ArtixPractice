package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerId;

public final class ConnectionPoolReadyEvent {
   private final ServerId serverId;

   public ConnectionPoolReadyEvent(ServerId serverId) {
      this.serverId = (ServerId)Assertions.assertNotNull(serverId);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public String toString() {
      return "ConnectionPoolReadyEvent{serverId=" + this.serverId + '}';
   }
}
