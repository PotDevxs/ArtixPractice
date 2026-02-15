package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.List;
import java.util.NoSuchElementException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.client.MongoChangeStreamCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.AggregateResponseBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class MongoChangeStreamCursorImpl<T> implements MongoChangeStreamCursor<T> {
   private final AggregateResponseBatchCursor<RawBsonDocument> batchCursor;
   private final Decoder<T> decoder;
   private List<RawBsonDocument> curBatch;
   private int curPos;
   private BsonDocument resumeToken;

   public MongoChangeStreamCursorImpl(BatchCursor<RawBsonDocument> batchCursor, Decoder<T> decoder, @Nullable BsonDocument initialResumeToken) {
      this.batchCursor = (AggregateResponseBatchCursor)batchCursor;
      this.decoder = decoder;
      this.resumeToken = initialResumeToken;
   }

   public void remove() {
      throw new UnsupportedOperationException("Cursors do not support removal");
   }

   public void close() {
      this.batchCursor.close();
   }

   public boolean hasNext() {
      return this.curBatch != null || this.batchCursor.hasNext();
   }

   public T next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         if (this.curBatch == null) {
            this.curBatch = this.batchCursor.next();
         }

         return this.getNextInBatch();
      }
   }

   public int available() {
      int available = this.batchCursor.available();
      if (this.curBatch != null) {
         available += this.curBatch.size() - this.curPos;
      }

      return available;
   }

   @Nullable
   public T tryNext() {
      if (this.curBatch == null) {
         this.curBatch = this.batchCursor.tryNext();
      }

      if (this.curBatch == null && this.batchCursor.getPostBatchResumeToken() != null) {
         this.resumeToken = this.batchCursor.getPostBatchResumeToken();
      }

      return this.curBatch == null ? null : this.getNextInBatch();
   }

   @Nullable
   public ServerCursor getServerCursor() {
      return this.batchCursor.getServerCursor();
   }

   public ServerAddress getServerAddress() {
      return this.batchCursor.getServerAddress();
   }

   private T getNextInBatch() {
      RawBsonDocument nextInBatch = (RawBsonDocument)this.curBatch.get(this.curPos);
      this.resumeToken = nextInBatch.getDocument("_id");
      if (this.curPos < this.curBatch.size() - 1) {
         ++this.curPos;
      } else {
         this.curBatch = null;
         this.curPos = 0;
         if (this.batchCursor.getPostBatchResumeToken() != null) {
            this.resumeToken = this.batchCursor.getPostBatchResumeToken();
         }
      }

      return nextInBatch.decode(this.decoder);
   }

   @Nullable
   public BsonDocument getResumeToken() {
      return this.resumeToken;
   }
}
