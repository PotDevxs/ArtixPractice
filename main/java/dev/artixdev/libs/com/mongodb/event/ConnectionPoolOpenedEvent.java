package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerId;

/** @deprecated */
@Deprecated
public final class ConnectionPoolOpenedEvent {
   private final ServerId serverId;
   private final ConnectionPoolSettings settings;

   public ConnectionPoolOpenedEvent(ServerId serverId, ConnectionPoolSettings settings) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.settings = (ConnectionPoolSettings)Assertions.notNull("settings", settings);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public ConnectionPoolSettings getSettings() {
      return this.settings;
   }

   public String toString() {
      return "ConnectionPoolOpenedEvent{serverId=" + this.serverId + " settings=" + this.settings + '}';
   }
}
