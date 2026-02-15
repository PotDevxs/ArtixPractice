package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.types.ObjectId;

public final class ConnectionPoolClearedEvent {
   private final ServerId serverId;
   @Nullable
   private final ObjectId serviceId;

   public ConnectionPoolClearedEvent(ServerId serverId) {
      this(serverId, (ObjectId)null);
   }

   public ConnectionPoolClearedEvent(ServerId serverId, @Nullable ObjectId serviceId) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.serviceId = serviceId;
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   @Nullable
   public ObjectId getServiceId() {
      return this.serviceId;
   }

   public String toString() {
      return "ConnectionPoolClearedEvent{serverId=" + this.serverId + ", serviceId=" + this.serviceId + '}';
   }
}
