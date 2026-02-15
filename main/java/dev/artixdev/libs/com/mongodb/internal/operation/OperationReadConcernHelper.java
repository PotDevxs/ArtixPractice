package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.connection.ReadConcernHelper;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.org.bson.BsonDocument;

final class OperationReadConcernHelper {
   static void appendReadConcernToCommand(SessionContext sessionContext, int maxWireVersion, BsonDocument commandDocument) {
      Assertions.notNull("commandDocument", commandDocument);
      Assertions.notNull("sessionContext", sessionContext);
      if (!sessionContext.hasActiveTransaction()) {
         if (!sessionContext.isSnapshot()) {
            BsonDocument readConcernDocument = ReadConcernHelper.getReadConcernDocument(sessionContext, maxWireVersion);
            if (!readConcernDocument.isEmpty()) {
               commandDocument.append("readConcern", readConcernDocument);
            }

         }
      }
   }

   private OperationReadConcernHelper() {
   }
}
