package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;

/** @deprecated */
@Deprecated
public final class ConnectionAddedEvent {
   private final ConnectionId connectionId;

   public ConnectionAddedEvent(ConnectionId connectionId) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public String toString() {
      return "ConnectionAddedEvent{connectionId=" + this.connectionId + '}';
   }
}
