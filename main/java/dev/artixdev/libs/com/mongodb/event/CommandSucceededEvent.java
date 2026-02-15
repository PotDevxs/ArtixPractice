package dev.artixdev.libs.com.mongodb.event;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class CommandSucceededEvent extends CommandEvent {
   private final BsonDocument response;
   private final long elapsedTimeNanos;

   public CommandSucceededEvent(@Nullable RequestContext requestContext, long operationId, int requestId, ConnectionDescription connectionDescription, String databaseName, String commandName, BsonDocument response, long elapsedTimeNanos) {
      super(requestContext, operationId, requestId, connectionDescription, databaseName, commandName);
      this.response = response;
      Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
   }

   /** @deprecated */
   @Deprecated
   public CommandSucceededEvent(@Nullable RequestContext requestContext, long operationId, int requestId, ConnectionDescription connectionDescription, String commandName, BsonDocument response, long elapsedTimeNanos) {
      super(requestContext, operationId, requestId, connectionDescription, commandName);
      this.response = response;
      Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
   }

   /** @deprecated */
   @Deprecated
   public CommandSucceededEvent(@Nullable RequestContext requestContext, int requestId, ConnectionDescription connectionDescription, String commandName, BsonDocument response, long elapsedTimeNanos) {
      this(requestContext, -1L, requestId, connectionDescription, commandName, response, elapsedTimeNanos);
   }

   /** @deprecated */
   @Deprecated
   public CommandSucceededEvent(int requestId, ConnectionDescription connectionDescription, String commandName, BsonDocument response, long elapsedTimeNanos) {
      this((RequestContext)null, requestId, connectionDescription, commandName, response, elapsedTimeNanos);
   }

   public long getElapsedTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
   }

   public BsonDocument getResponse() {
      return this.response;
   }
}
