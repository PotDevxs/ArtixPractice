package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class CommandStartedEvent extends CommandEvent {
   private final BsonDocument command;

   public CommandStartedEvent(@Nullable RequestContext requestContext, long operationId, int requestId, ConnectionDescription connectionDescription, String databaseName, String commandName, BsonDocument command) {
      super(requestContext, operationId, requestId, connectionDescription, databaseName, commandName);
      this.command = command;
   }

   /** @deprecated */
   @Deprecated
   public CommandStartedEvent(@Nullable RequestContext requestContext, int requestId, ConnectionDescription connectionDescription, String databaseName, String commandName, BsonDocument command) {
      this(requestContext, -1L, requestId, connectionDescription, databaseName, commandName, command);
   }

   /** @deprecated */
   @Deprecated
   public CommandStartedEvent(int requestId, ConnectionDescription connectionDescription, String databaseName, String commandName, BsonDocument command) {
      this((RequestContext)null, requestId, connectionDescription, databaseName, commandName, command);
   }

   public BsonDocument getCommand() {
      return this.command;
   }
}
