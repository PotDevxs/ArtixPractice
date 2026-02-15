package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoCursorNotFoundException;
import dev.artixdev.libs.com.mongodb.MongoQueryException;
import dev.artixdev.libs.com.mongodb.ServerCursor;

final class QueryHelper {
   static MongoQueryException translateCommandException(MongoCommandException commandException, ServerCursor cursor) {
      return (MongoQueryException)(commandException.getErrorCode() == 43 ? new MongoCursorNotFoundException(cursor.getId(), commandException.getResponse(), cursor.getAddress()) : new MongoQueryException(commandException.getResponse(), commandException.getServerAddress()));
   }

   private QueryHelper() {
   }
}
