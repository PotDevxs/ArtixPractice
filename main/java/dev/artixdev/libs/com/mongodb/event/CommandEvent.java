package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public abstract class CommandEvent {
   @Nullable
   private final RequestContext requestContext;
   private final int requestId;
   private final ConnectionDescription connectionDescription;
   private final String commandName;
   private final String databaseName;
   private final long operationId;

   public CommandEvent(@Nullable RequestContext requestContext, long operationId, int requestId, ConnectionDescription connectionDescription, String databaseName, String commandName) {
      this.requestContext = requestContext;
      this.requestId = requestId;
      this.connectionDescription = connectionDescription;
      this.commandName = commandName;
      this.databaseName = databaseName;
      this.operationId = operationId;
   }

   /** @deprecated */
   @Deprecated
   public CommandEvent(@Nullable RequestContext requestContext, long operationId, int requestId, ConnectionDescription connectionDescription, String commandName) {
      this(requestContext, -1L, requestId, connectionDescription, "", commandName);
   }

   /** @deprecated */
   @Deprecated
   public CommandEvent(@Nullable RequestContext requestContext, int requestId, ConnectionDescription connectionDescription, String commandName) {
      this(requestContext, -1L, requestId, connectionDescription, "", commandName);
   }

   public CommandEvent(int requestId, ConnectionDescription connectionDescription, String commandName) {
      this((RequestContext)null, requestId, connectionDescription, commandName);
   }

   public long getOperationId() {
      return this.operationId;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public ConnectionDescription getConnectionDescription() {
      return this.connectionDescription;
   }

   public String getCommandName() {
      return this.commandName;
   }

   public String getDatabaseName() {
      return this.databaseName;
   }

   @Nullable
   public RequestContext getRequestContext() {
      return this.requestContext;
   }
}
