package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;

public final class ConnectionCreatedEvent {
   private final ConnectionId connectionId;

   public ConnectionCreatedEvent(ConnectionId connectionId) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public String toString() {
      return "ConnectionCreatedEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + '}';
   }
}
