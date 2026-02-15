package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerId;

public final class ConnectionCheckOutStartedEvent {
   private final ServerId serverId;
   private final long operationId;

   public ConnectionCheckOutStartedEvent(ServerId serverId, long operationId) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.operationId = operationId;
   }

   /** @deprecated */
   @Deprecated
   public ConnectionCheckOutStartedEvent(ServerId serverId) {
      this(serverId, -1L);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public long getOperationId() {
      return this.operationId;
   }

   public String toString() {
      return "ConnectionCheckOutStartedEvent{server=" + this.serverId.getAddress() + ", clusterId=" + this.serverId.getClusterId() + ", operationId=" + this.operationId + '}';
   }
}
