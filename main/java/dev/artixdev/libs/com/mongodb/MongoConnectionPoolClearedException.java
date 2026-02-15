package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class MongoConnectionPoolClearedException extends MongoClientException {
   private static final long serialVersionUID = 1L;

   public MongoConnectionPoolClearedException(ServerId connectionPoolServerId, @Nullable Throwable cause) {
      super("Connection pool for " + Assertions.assertNotNull(connectionPoolServerId) + " is paused" + (cause == null ? "" : " because another operation failed"), cause);
   }
}
