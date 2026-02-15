package dev.artixdev.libs.com.mongodb.event;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;

public final class ServerHeartbeatFailedEvent {
   private final ConnectionId connectionId;
   private final long elapsedTimeNanos;
   private final boolean awaited;
   private final Throwable throwable;

   /** @deprecated */
   @Deprecated
   public ServerHeartbeatFailedEvent(ConnectionId connectionId, long elapsedTimeNanos, Throwable throwable) {
      this(connectionId, elapsedTimeNanos, false, throwable);
   }

   public ServerHeartbeatFailedEvent(ConnectionId connectionId, long elapsedTimeNanos, boolean awaited, Throwable throwable) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
      this.awaited = awaited;
      Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
      this.throwable = (Throwable)Assertions.notNull("throwable", throwable);
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public long getElapsedTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
   }

   public boolean isAwaited() {
      return this.awaited;
   }

   public Throwable getThrowable() {
      return this.throwable;
   }

   public String toString() {
      return "ServerHeartbeatFailedEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + ", elapsedTimeNanos=" + this.elapsedTimeNanos + ", awaited=" + this.awaited + ", throwable=" + this.throwable + "} " + super.toString();
   }
}
