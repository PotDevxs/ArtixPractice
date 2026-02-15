package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;

final class CursorHelper {
   static int getNumberToReturn(int limit, int batchSize, int numReturnedSoFar) {
      int numberToReturn;
      if (Math.abs(limit) != 0) {
         numberToReturn = Math.abs(limit) - numReturnedSoFar;
         if (batchSize != 0 && numberToReturn > Math.abs(batchSize)) {
            numberToReturn = batchSize;
         }
      } else {
         numberToReturn = batchSize;
      }

      return numberToReturn;
   }

   static BsonDocument getCursorDocumentFromBatchSize(@Nullable Integer batchSize) {
      return batchSize == null ? new BsonDocument() : new BsonDocument("batchSize", new BsonInt32(batchSize));
   }

   private CursorHelper() {
   }
}
