package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;

public final class ServerDescriptionChangedEvent {
   private final ServerId serverId;
   private final ServerDescription newDescription;
   private final ServerDescription previousDescription;

   public ServerDescriptionChangedEvent(ServerId serverId, ServerDescription newDescription, ServerDescription previousDescription) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.newDescription = (ServerDescription)Assertions.notNull("newDescription", newDescription);
      this.previousDescription = (ServerDescription)Assertions.notNull("previousDescription", previousDescription);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public ServerDescription getNewDescription() {
      return this.newDescription;
   }

   public ServerDescription getPreviousDescription() {
      return this.previousDescription;
   }

   public String toString() {
      return "ServerDescriptionChangedEvent{serverId=" + this.serverId + ", newDescription=" + this.newDescription + ", previousDescription=" + this.previousDescription + '}';
   }
}
