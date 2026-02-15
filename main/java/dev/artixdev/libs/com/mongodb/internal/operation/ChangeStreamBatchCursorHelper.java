package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoChangeStreamException;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoCursorNotFoundException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.MongoNotPrimaryException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;

final class ChangeStreamBatchCursorHelper {
   static final List<Integer> RETRYABLE_SERVER_ERROR_CODES = Arrays.asList(6, 7, 63, 89, 91, 133, 150, 189, 234, 262, 9001, 10107, 11600, 11602, 13388, 13435, 13436);
   static final String RESUMABLE_CHANGE_STREAM_ERROR_LABEL = "ResumableChangeStreamError";

   static boolean isResumableError(Throwable t, int maxWireVersion) {
      if (t instanceof MongoException && !(t instanceof MongoChangeStreamException) && !(t instanceof MongoInterruptedException)) {
         if (!(t instanceof MongoNotPrimaryException) && !(t instanceof MongoCursorNotFoundException) && !(t instanceof MongoSocketException | t instanceof MongoClientException)) {
            return maxWireVersion >= 9 ? ((MongoException)t).getErrorLabels().contains("ResumableChangeStreamError") : RETRYABLE_SERVER_ERROR_CODES.contains(((MongoException)t).getCode());
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private ChangeStreamBatchCursorHelper() {
   }
}
