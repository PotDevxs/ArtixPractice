package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.ReadConcernLevel;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

public final class ReadConcernHelper {
   public static BsonDocument getReadConcernDocument(SessionContext sessionContext, int maxWireVersion) {
      Assertions.notNull("sessionContext", sessionContext);
      BsonDocument readConcernDocument = new BsonDocument();
      ReadConcernLevel level = sessionContext.getReadConcern().getLevel();
      if (level != null) {
         readConcernDocument.append("level", new BsonString(level.getValue()));
      }

      Assertions.assertFalse(sessionContext.isSnapshot() && sessionContext.isCausallyConsistent());
      if (sessionContext.isSnapshot() && maxWireVersion < 12) {
         throw new MongoClientException("Snapshot reads require MongoDB 5.0 or later");
      } else {
         if (shouldAddAfterClusterTime(sessionContext)) {
            readConcernDocument.append("afterClusterTime", sessionContext.getOperationTime());
         } else if (shouldAddAtClusterTime(sessionContext)) {
            readConcernDocument.append("atClusterTime", sessionContext.getSnapshotTimestamp());
         }

         return readConcernDocument;
      }
   }

   private static boolean shouldAddAtClusterTime(SessionContext sessionContext) {
      return sessionContext.isSnapshot() && sessionContext.getSnapshotTimestamp() != null;
   }

   private static boolean shouldAddAfterClusterTime(SessionContext sessionContext) {
      return sessionContext.isCausallyConsistent() && sessionContext.getOperationTime() != null;
   }

   private ReadConcernHelper() {
   }
}
