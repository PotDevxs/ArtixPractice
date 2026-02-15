package dev.artixdev.libs.com.mongodb.event;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;

public final class ConnectionReadyEvent {
   private final ConnectionId connectionId;
   private final long elapsedTimeNanos;

   public ConnectionReadyEvent(ConnectionId connectionId, long elapsedTimeNanos) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
      Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
   }

   /** @deprecated */
   @Deprecated
   public ConnectionReadyEvent(ConnectionId connectionId) {
      this(connectionId, 0L);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public long getElapsedTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
   }

   public String toString() {
      return "ConnectionReadyEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + ", elapsedTimeNanos=" + this.elapsedTimeNanos + '}';
   }
}
