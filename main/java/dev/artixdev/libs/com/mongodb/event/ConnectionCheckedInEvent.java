package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;

public final class ConnectionCheckedInEvent {
   private final ConnectionId connectionId;
   private final long operationId;

   public ConnectionCheckedInEvent(ConnectionId connectionId, long operationId) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
      this.operationId = operationId;
   }

   /** @deprecated */
   @Deprecated
   public ConnectionCheckedInEvent(ConnectionId connectionId) {
      this(connectionId, -1L);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public long getOperationId() {
      return this.operationId;
   }

   public String toString() {
      return "ConnectionCheckedInEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + ", operationId=" + this.operationId + '}';
   }
}
