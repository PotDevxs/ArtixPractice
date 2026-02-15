package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncAggregateResponseBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.RawBsonDocument;

final class AsyncChangeStreamBatchCursor<T> implements AsyncAggregateResponseBatchCursor<T> {
   private final AsyncReadBinding binding;
   private final ChangeStreamOperation<T> changeStreamOperation;
   private final int maxWireVersion;
   private volatile BsonDocument resumeToken;
   private final AtomicReference<AsyncAggregateResponseBatchCursor<RawBsonDocument>> wrapped;
   private final AtomicBoolean isClosed;

   AsyncChangeStreamBatchCursor(ChangeStreamOperation<T> changeStreamOperation, AsyncAggregateResponseBatchCursor<RawBsonDocument> wrapped, AsyncReadBinding binding, @Nullable BsonDocument resumeToken, int maxWireVersion) {
      this.changeStreamOperation = changeStreamOperation;
      this.wrapped = new AtomicReference((AsyncAggregateResponseBatchCursor)Assertions.assertNotNull(wrapped));
      this.binding = binding;
      binding.retain();
      this.resumeToken = resumeToken;
      this.maxWireVersion = maxWireVersion;
      this.isClosed = new AtomicBoolean();
   }

   @NonNull
   AsyncAggregateResponseBatchCursor<RawBsonDocument> getWrapped() {
      return (AsyncAggregateResponseBatchCursor)Assertions.assertNotNull((AsyncAggregateResponseBatchCursor)this.wrapped.get());
   }

   public void next(SingleResultCallback<List<T>> callback) {
      this.resumeableOperation((cursor, callback1) -> {
         cursor.next(callback1);
      }, callback, false);
   }

   public void close() {
      if (this.isClosed.compareAndSet(false, true)) {
         try {
            this.nullifyAndCloseWrapped();
         } finally {
            this.binding.release();
         }
      }

   }

   public void setBatchSize(int batchSize) {
      this.getWrapped().setBatchSize(batchSize);
   }

   public int getBatchSize() {
      return this.getWrapped().getBatchSize();
   }

   public boolean isClosed() {
      if (this.isClosed.get()) {
         return true;
      } else if (this.wrappedClosedItself()) {
         this.close();
         return true;
      } else {
         return false;
      }
   }

   private boolean wrappedClosedItself() {
      AsyncAggregateResponseBatchCursor<RawBsonDocument> observedWrapped = (AsyncAggregateResponseBatchCursor)this.wrapped.get();
      return observedWrapped != null && observedWrapped.isClosed();
   }

   private void nullifyAndCloseWrapped() {
      AsyncAggregateResponseBatchCursor<RawBsonDocument> observedWrapped = this.wrapped.getAndSet(null);
      if (observedWrapped != null) {
         observedWrapped.close();
      }

   }

   private void setWrappedOrCloseIt(AsyncAggregateResponseBatchCursor<RawBsonDocument> newValue) {
      if (this.isClosed()) {
         Assertions.assertNull((AsyncAggregateResponseBatchCursor)this.wrapped.get());
         newValue.close();
      } else {
         Assertions.assertNull((AsyncAggregateResponseBatchCursor)this.wrapped.getAndSet(newValue));
         if (this.isClosed()) {
            this.nullifyAndCloseWrapped();
         }
      }

   }

   public BsonDocument getPostBatchResumeToken() {
      return this.getWrapped().getPostBatchResumeToken();
   }

   public BsonTimestamp getOperationTime() {
      return this.changeStreamOperation.getStartAtOperationTime();
   }

   public boolean isFirstBatchEmpty() {
      return this.getWrapped().isFirstBatchEmpty();
   }

   public int getMaxWireVersion() {
      return this.maxWireVersion;
   }

   private void cachePostBatchResumeToken(AsyncAggregateResponseBatchCursor<RawBsonDocument> queryBatchCursor) {
      BsonDocument resumeToken = queryBatchCursor.getPostBatchResumeToken();
      if (resumeToken != null) {
         this.resumeToken = resumeToken;
      }

   }

   private void resumeableOperation(AsyncChangeStreamBatchCursor.AsyncBlock asyncBlock, SingleResultCallback<List<T>> callback, boolean tryNext) {
      SingleResultCallback<List<T>> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
      if (this.isClosed()) {
         errHandlingCallback.onResult(null, new MongoException(String.format("%s called after the cursor was closed.", tryNext ? "tryNext()" : "next()")));
      } else {
         AsyncAggregateResponseBatchCursor<RawBsonDocument> wrappedCursor = this.getWrapped();
         asyncBlock.apply(wrappedCursor, (result, t) -> {
            if (t == null) {
               try {
                  List convertedResults;
                  try {
                     convertedResults = ChangeStreamBatchCursor.convertAndProduceLastId(result, this.changeStreamOperation.getDecoder(), (lastId) -> {
                        this.resumeToken = lastId;
                     });
                  } finally {
                     this.cachePostBatchResumeToken(wrappedCursor);
                  }

                  errHandlingCallback.onResult(convertedResults, (Throwable)null);
               } catch (Exception exception) {
                  errHandlingCallback.onResult(null, exception);
               }
            } else {
               this.cachePostBatchResumeToken(wrappedCursor);
               if (ChangeStreamBatchCursorHelper.isResumableError(t, this.maxWireVersion)) {
                  this.nullifyAndCloseWrapped();
                  this.retryOperation(asyncBlock, errHandlingCallback, tryNext);
               } else {
                  errHandlingCallback.onResult(null, t);
               }
            }

         });
      }
   }

   private void retryOperation(AsyncChangeStreamBatchCursor.AsyncBlock asyncBlock, SingleResultCallback<List<T>> callback, boolean tryNext) {
      AsyncOperationHelper.withAsyncReadConnectionSource(this.binding, (source, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            this.changeStreamOperation.setChangeStreamOptionsForResume(this.resumeToken, source.getServerDescription().getMaxWireVersion());
            source.release();
            this.changeStreamOperation.executeAsync(this.binding, (result, t1) -> {
               if (t1 != null) {
                  callback.onResult(null, t1);
               } else {
                  try {
                     this.setWrappedOrCloseIt(((AsyncChangeStreamBatchCursor)result).getWrapped());
                  } finally {
                     try {
                        this.binding.release();
                     } finally {
                        this.resumeableOperation(asyncBlock, callback, tryNext);
                     }
                  }
               }

            });
         }

      });
   }

   private interface AsyncBlock {
      void apply(AsyncAggregateResponseBatchCursor<RawBsonDocument> var1, SingleResultCallback<List<RawBsonDocument>> var2);
   }
}
