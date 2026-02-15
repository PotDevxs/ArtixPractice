package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.MongoChangeStreamException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.Decoder;

final class ChangeStreamBatchCursor<T> implements AggregateResponseBatchCursor<T> {
   private final ReadBinding binding;
   private final ChangeStreamOperation<T> changeStreamOperation;
   private final int maxWireVersion;
   private AggregateResponseBatchCursor<RawBsonDocument> wrapped;
   private BsonDocument resumeToken;
   private final AtomicBoolean closed;

   ChangeStreamBatchCursor(ChangeStreamOperation<T> changeStreamOperation, AggregateResponseBatchCursor<RawBsonDocument> wrapped, ReadBinding binding, @Nullable BsonDocument resumeToken, int maxWireVersion) {
      this.changeStreamOperation = changeStreamOperation;
      this.binding = binding.retain();
      this.wrapped = wrapped;
      this.resumeToken = resumeToken;
      this.maxWireVersion = maxWireVersion;
      this.closed = new AtomicBoolean();
   }

   AggregateResponseBatchCursor<RawBsonDocument> getWrapped() {
      return this.wrapped;
   }

   public boolean hasNext() {
      return (Boolean)this.resumeableOperation((queryBatchCursor) -> {
         Boolean var2;
         try {
            var2 = queryBatchCursor.hasNext();
         } finally {
            this.cachePostBatchResumeToken(queryBatchCursor);
         }

         return var2;
      });
   }

   public List<T> next() {
      return (List)this.resumeableOperation((queryBatchCursor) -> {
         List var2;
         try {
            var2 = convertAndProduceLastId(queryBatchCursor.next(), this.changeStreamOperation.getDecoder(), (lastId) -> {
               this.resumeToken = lastId;
            });
         } finally {
            this.cachePostBatchResumeToken(queryBatchCursor);
         }

         return var2;
      });
   }

   public int available() {
      return this.wrapped.available();
   }

   public List<T> tryNext() {
      return (List)this.resumeableOperation((queryBatchCursor) -> {
         List var2;
         try {
            var2 = convertAndProduceLastId(queryBatchCursor.tryNext(), this.changeStreamOperation.getDecoder(), (lastId) -> {
               this.resumeToken = lastId;
            });
         } finally {
            this.cachePostBatchResumeToken(queryBatchCursor);
         }

         return var2;
      });
   }

   public void close() {
      if (!this.closed.getAndSet(true)) {
         this.wrapped.close();
         this.binding.release();
      }

   }

   public void setBatchSize(int batchSize) {
      this.wrapped.setBatchSize(batchSize);
   }

   public int getBatchSize() {
      return this.wrapped.getBatchSize();
   }

   public ServerCursor getServerCursor() {
      return this.wrapped.getServerCursor();
   }

   public ServerAddress getServerAddress() {
      return this.wrapped.getServerAddress();
   }

   public void remove() {
      throw new UnsupportedOperationException("Not implemented!");
   }

   public BsonDocument getPostBatchResumeToken() {
      return this.wrapped.getPostBatchResumeToken();
   }

   public BsonTimestamp getOperationTime() {
      return this.changeStreamOperation.getStartAtOperationTime();
   }

   public boolean isFirstBatchEmpty() {
      return this.wrapped.isFirstBatchEmpty();
   }

   public int getMaxWireVersion() {
      return this.maxWireVersion;
   }

   private void cachePostBatchResumeToken(AggregateResponseBatchCursor<RawBsonDocument> queryBatchCursor) {
      if (queryBatchCursor.getPostBatchResumeToken() != null) {
         this.resumeToken = queryBatchCursor.getPostBatchResumeToken();
      }

   }

   @Nullable
   static <T> List<T> convertAndProduceLastId(@Nullable List<RawBsonDocument> rawDocuments, Decoder<T> decoder, Consumer<BsonDocument> lastIdConsumer) {
      List<T> results = null;
      if (rawDocuments != null) {
         results = new ArrayList();
         Iterator var4 = rawDocuments.iterator();

         while(var4.hasNext()) {
            RawBsonDocument rawDocument = (RawBsonDocument)var4.next();
            if (!rawDocument.containsKey("_id")) {
               throw new MongoChangeStreamException("Cannot provide resume functionality when the resume token is missing.");
            }

            results.add(rawDocument.decode(decoder));
         }

         lastIdConsumer.accept(((RawBsonDocument)rawDocuments.get(rawDocuments.size() - 1)).getDocument("_id"));
      }

      return results;
   }

   <R> R resumeableOperation(Function<AggregateResponseBatchCursor<RawBsonDocument>, R> function) {
      while(true) {
         try {
            return function.apply(this.wrapped);
         } catch (Throwable e) {
            if (!ChangeStreamBatchCursorHelper.isResumableError(e, this.maxWireVersion)) {
               throw MongoException.fromThrowableNonNull(e);
            }

            this.wrapped.close();
            SyncOperationHelper.withReadConnectionSource(this.binding, (source) -> {
               this.changeStreamOperation.setChangeStreamOptionsForResume(this.resumeToken, source.getServerDescription().getMaxWireVersion());
               return null;
            });
            this.wrapped = ((ChangeStreamBatchCursor)this.changeStreamOperation.execute(this.binding)).getWrapped();
            this.binding.release();
         }
      }
   }
}
