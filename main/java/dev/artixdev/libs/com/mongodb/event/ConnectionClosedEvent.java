package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;

public final class ConnectionClosedEvent {
   private final ConnectionId connectionId;
   private final ConnectionClosedEvent.Reason reason;

   public ConnectionClosedEvent(ConnectionId connectionId, ConnectionClosedEvent.Reason reason) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
      this.reason = (ConnectionClosedEvent.Reason)Assertions.notNull("reason", reason);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public ConnectionClosedEvent.Reason getReason() {
      return this.reason;
   }

   public String toString() {
      return "ConnectionClosedEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + ", reason=" + this.reason + '}';
   }

   public static enum Reason {
      STALE,
      IDLE,
      ERROR,
      POOL_CLOSED;

      // $FF: synthetic method
      private static ConnectionClosedEvent.Reason[] $values() {
         return new ConnectionClosedEvent.Reason[]{STALE, IDLE, ERROR, POOL_CLOSED};
      }
   }
}
