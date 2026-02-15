package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.connection.ConnectionId;
import dev.artixdev.libs.org.bson.assertions.Assertions;

/** @deprecated */
@Deprecated
public final class ConnectionRemovedEvent {
   private final ConnectionId connectionId;
   private final ConnectionRemovedEvent.Reason reason;

   public ConnectionRemovedEvent(ConnectionId connectionId, ConnectionRemovedEvent.Reason reason) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
      this.reason = (ConnectionRemovedEvent.Reason)Assertions.notNull("reason", reason);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public ConnectionRemovedEvent.Reason getReason() {
      return this.reason;
   }

   public String toString() {
      return "ConnectionRemovedEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + ", reason=" + this.reason + '}';
   }

   public static enum Reason {
      UNKNOWN,
      STALE,
      MAX_IDLE_TIME_EXCEEDED,
      MAX_LIFE_TIME_EXCEEDED,
      ERROR,
      POOL_CLOSED;

      // $FF: synthetic method
      private static ConnectionRemovedEvent.Reason[] $values() {
         return new ConnectionRemovedEvent.Reason[]{UNKNOWN, STALE, MAX_IDLE_TIME_EXCEEDED, MAX_LIFE_TIME_EXCEEDED, ERROR, POOL_CLOSED};
      }
   }
}
