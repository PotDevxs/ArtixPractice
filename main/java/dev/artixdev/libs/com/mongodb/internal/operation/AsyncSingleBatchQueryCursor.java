package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;

class AsyncSingleBatchQueryCursor<T> implements AsyncBatchCursor<T> {
   private volatile QueryResult<T> firstBatch;
   private volatile boolean closed;

   AsyncSingleBatchQueryCursor(QueryResult<T> firstBatch) {
      this.firstBatch = firstBatch;
      Assertions.isTrue("Empty Cursor", firstBatch.getCursor() == null);
   }

   public void close() {
      this.closed = true;
   }

   public void next(SingleResultCallback<List<T>> callback) {
      if (this.closed) {
         callback.onResult(null, new MongoException("next() called after the cursor was closed."));
      } else if (this.firstBatch != null && !this.firstBatch.getResults().isEmpty()) {
         List<T> results = this.firstBatch.getResults();
         this.firstBatch = null;
         callback.onResult(results, (Throwable)null);
      } else {
         this.closed = true;
         callback.onResult(null, (Throwable)null);
      }

   }

   public void setBatchSize(int batchSize) {
   }

   public int getBatchSize() {
      return 0;
   }

   public boolean isClosed() {
      return this.closed;
   }
}
