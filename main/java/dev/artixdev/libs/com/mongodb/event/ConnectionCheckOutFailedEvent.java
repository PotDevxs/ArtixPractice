package dev.artixdev.libs.com.mongodb.event;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerId;

public final class ConnectionCheckOutFailedEvent {
   private final ServerId serverId;
   private final long operationId;
   private final ConnectionCheckOutFailedEvent.Reason reason;
   private final long elapsedTimeNanos;

   public ConnectionCheckOutFailedEvent(ServerId serverId, long operationId, ConnectionCheckOutFailedEvent.Reason reason, long elapsedTimeNanos) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.operationId = operationId;
      this.reason = (ConnectionCheckOutFailedEvent.Reason)Assertions.notNull("reason", reason);
      Assertions.isTrueArgument("waited time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
   }

   /** @deprecated */
   @Deprecated
   public ConnectionCheckOutFailedEvent(ServerId serverId, long operationId, ConnectionCheckOutFailedEvent.Reason reason) {
      this(serverId, operationId, reason, 0L);
   }

   /** @deprecated */
   @Deprecated
   public ConnectionCheckOutFailedEvent(ServerId serverId, ConnectionCheckOutFailedEvent.Reason reason) {
      this(serverId, -1L, reason);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public long getOperationId() {
      return this.operationId;
   }

   public ConnectionCheckOutFailedEvent.Reason getReason() {
      return this.reason;
   }

   public long getElapsedTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
   }

   public String toString() {
      return "ConnectionCheckOutFailedEvent{server=" + this.serverId.getAddress() + ", clusterId=" + this.serverId.getClusterId() + ", operationId=" + this.operationId + ", reason=" + this.reason + ", elapsedTimeNanos=" + this.elapsedTimeNanos + '}';
   }

   public static enum Reason {
      POOL_CLOSED,
      TIMEOUT,
      CONNECTION_ERROR,
      UNKNOWN;

      // $FF: synthetic method
      private static ConnectionCheckOutFailedEvent.Reason[] $values() {
         return new ConnectionCheckOutFailedEvent.Reason[]{POOL_CLOSED, TIMEOUT, CONNECTION_ERROR, UNKNOWN};
      }
   }
}
