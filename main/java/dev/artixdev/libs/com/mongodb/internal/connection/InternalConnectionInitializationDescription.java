package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;

@Immutable
public class InternalConnectionInitializationDescription {
   private final ConnectionDescription connectionDescription;
   private final ServerDescription serverDescription;

   public InternalConnectionInitializationDescription(ConnectionDescription connectionDescription, ServerDescription serverDescription) {
      this.connectionDescription = connectionDescription;
      this.serverDescription = serverDescription;
   }

   public ConnectionDescription getConnectionDescription() {
      return this.connectionDescription;
   }

   public ServerDescription getServerDescription() {
      return this.serverDescription;
   }

   public InternalConnectionInitializationDescription withConnectionDescription(ConnectionDescription connectionDescription) {
      return new InternalConnectionInitializationDescription(connectionDescription, this.serverDescription);
   }
}
