package dev.artixdev.libs.com.mongodb.event;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;

public final class ConnectionCheckedOutEvent {
   private final ConnectionId connectionId;
   private final long operationId;
   private final long elapsedTimeNanos;

   public ConnectionCheckedOutEvent(ConnectionId connectionId, long operationId, long elapsedTimeNanos) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
      this.operationId = operationId;
      Assertions.isTrueArgument("waited time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
   }

   /** @deprecated */
   @Deprecated
   public ConnectionCheckedOutEvent(ConnectionId connectionId, long operationId) {
      this(connectionId, operationId, 0L);
   }

   /** @deprecated */
   @Deprecated
   public ConnectionCheckedOutEvent(ConnectionId connectionId) {
      this(connectionId, -1L);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public long getOperationId() {
      return this.operationId;
   }

   public long getElapsedTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
   }

   public String toString() {
      return "ConnectionCheckedOutEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + ", operationId=" + this.operationId + ", elapsedTimeNanos=" + this.elapsedTimeNanos + '}';
   }
}
