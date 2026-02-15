package dev.artixdev.libs.com.mongodb.event;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class CommandFailedEvent extends CommandEvent {
   private final long elapsedTimeNanos;
   private final Throwable throwable;

   public CommandFailedEvent(@Nullable RequestContext requestContext, long operationId, int requestId, ConnectionDescription connectionDescription, String databaseName, String commandName, long elapsedTimeNanos, Throwable throwable) {
      super(requestContext, operationId, requestId, connectionDescription, databaseName, commandName);
      Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
      this.throwable = throwable;
   }

   /** @deprecated */
   @Deprecated
   public CommandFailedEvent(@Nullable RequestContext requestContext, long operationId, int requestId, ConnectionDescription connectionDescription, String commandName, long elapsedTimeNanos, Throwable throwable) {
      super(requestContext, operationId, requestId, connectionDescription, commandName);
      Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
      this.throwable = throwable;
   }

   /** @deprecated */
   @Deprecated
   public CommandFailedEvent(@Nullable RequestContext requestContext, int requestId, ConnectionDescription connectionDescription, String commandName, long elapsedTimeNanos, Throwable throwable) {
      this(requestContext, -1L, requestId, connectionDescription, commandName, elapsedTimeNanos, throwable);
   }

   /** @deprecated */
   @Deprecated
   public CommandFailedEvent(int requestId, ConnectionDescription connectionDescription, String commandName, long elapsedTimeNanos, Throwable throwable) {
      this((RequestContext)null, requestId, connectionDescription, commandName, elapsedTimeNanos, throwable);
   }

   public long getElapsedTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
   }

   public Throwable getThrowable() {
      return this.throwable;
   }
}
