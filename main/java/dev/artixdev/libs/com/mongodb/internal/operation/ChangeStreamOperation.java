package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocument;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncAggregateResponseBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.RawBsonDocumentCodec;

public class ChangeStreamOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>> {
   private static final RawBsonDocumentCodec RAW_BSON_DOCUMENT_CODEC = new RawBsonDocumentCodec();
   private final AggregateOperationImpl<RawBsonDocument> wrapped;
   private final FullDocument fullDocument;
   private final FullDocumentBeforeChange fullDocumentBeforeChange;
   private final Decoder<T> decoder;
   private final ChangeStreamLevel changeStreamLevel;
   private BsonDocument resumeAfter;
   private BsonDocument startAfter;
   private BsonTimestamp startAtOperationTime;
   private boolean showExpandedEvents;

   public ChangeStreamOperation(MongoNamespace namespace, FullDocument fullDocument, FullDocumentBeforeChange fullDocumentBeforeChange, List<BsonDocument> pipeline, Decoder<T> decoder) {
      this(namespace, fullDocument, fullDocumentBeforeChange, pipeline, decoder, ChangeStreamLevel.COLLECTION);
   }

   public ChangeStreamOperation(MongoNamespace namespace, FullDocument fullDocument, FullDocumentBeforeChange fullDocumentBeforeChange, List<BsonDocument> pipeline, Decoder<T> decoder, ChangeStreamLevel changeStreamLevel) {
      this.wrapped = new AggregateOperationImpl(namespace, pipeline, RAW_BSON_DOCUMENT_CODEC, this.getAggregateTarget(), this.getPipelineCreator());
      this.fullDocument = (FullDocument)Assertions.notNull("fullDocument", fullDocument);
      this.fullDocumentBeforeChange = (FullDocumentBeforeChange)Assertions.notNull("fullDocumentBeforeChange", fullDocumentBeforeChange);
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
      this.changeStreamLevel = (ChangeStreamLevel)Assertions.notNull("changeStreamLevel", changeStreamLevel);
   }

   public MongoNamespace getNamespace() {
      return this.wrapped.getNamespace();
   }

   public Decoder<T> getDecoder() {
      return this.decoder;
   }

   public FullDocument getFullDocument() {
      return this.fullDocument;
   }

   public BsonDocument getResumeAfter() {
      return this.resumeAfter;
   }

   public ChangeStreamOperation<T> resumeAfter(BsonDocument resumeAfter) {
      this.resumeAfter = resumeAfter;
      return this;
   }

   public BsonDocument getStartAfter() {
      return this.startAfter;
   }

   public ChangeStreamOperation<T> startAfter(BsonDocument startAfter) {
      this.startAfter = startAfter;
      return this;
   }

   public List<BsonDocument> getPipeline() {
      return this.wrapped.getPipeline();
   }

   public Integer getBatchSize() {
      return this.wrapped.getBatchSize();
   }

   public ChangeStreamOperation<T> batchSize(@Nullable Integer batchSize) {
      this.wrapped.batchSize(batchSize);
      return this;
   }

   public long getMaxAwaitTime(TimeUnit timeUnit) {
      return this.wrapped.getMaxAwaitTime(timeUnit);
   }

   public ChangeStreamOperation<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
      this.wrapped.maxAwaitTime(maxAwaitTime, timeUnit);
      return this;
   }

   public Collation getCollation() {
      return this.wrapped.getCollation();
   }

   public ChangeStreamOperation<T> collation(Collation collation) {
      this.wrapped.collation(collation);
      return this;
   }

   public ChangeStreamOperation<T> startAtOperationTime(BsonTimestamp startAtOperationTime) {
      this.startAtOperationTime = startAtOperationTime;
      return this;
   }

   public BsonTimestamp getStartAtOperationTime() {
      return this.startAtOperationTime;
   }

   public ChangeStreamOperation<T> retryReads(boolean retryReads) {
      this.wrapped.retryReads(retryReads);
      return this;
   }

   public boolean getRetryReads() {
      return this.wrapped.getRetryReads();
   }

   @Nullable
   public BsonValue getComment() {
      return this.wrapped.getComment();
   }

   public ChangeStreamOperation<T> comment(BsonValue comment) {
      this.wrapped.comment(comment);
      return this;
   }

   public boolean getShowExpandedEvents() {
      return this.showExpandedEvents;
   }

   public ChangeStreamOperation<T> showExpandedEvents(boolean showExpandedEvents) {
      this.showExpandedEvents = showExpandedEvents;
      return this;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      return (BatchCursor)SyncOperationHelper.withReadConnectionSource(binding, (source) -> {
         AggregateResponseBatchCursor<RawBsonDocument> cursor = (AggregateResponseBatchCursor)this.wrapped.execute(binding);
         return new ChangeStreamBatchCursor(this, cursor, binding, this.setChangeStreamOptions(cursor.getPostBatchResumeToken(), cursor.getOperationTime(), cursor.getMaxWireVersion(), cursor.isFirstBatchEmpty()), cursor.getMaxWireVersion());
      });
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      this.wrapped.executeAsync(binding, (result, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            AsyncAggregateResponseBatchCursor<RawBsonDocument> cursor = (AsyncAggregateResponseBatchCursor)result;
            AsyncOperationHelper.withAsyncReadConnectionSource(binding, (source, t1) -> {
               if (t1 != null) {
                  callback.onResult(null, t1);
               } else {
                  callback.onResult(new AsyncChangeStreamBatchCursor(this, cursor, binding, this.setChangeStreamOptions(cursor.getPostBatchResumeToken(), cursor.getOperationTime(), cursor.getMaxWireVersion(), cursor.isFirstBatchEmpty()), cursor.getMaxWireVersion()), (Throwable)null);
               }

               source.release();
            });
         }

      });
   }

   @Nullable
   private BsonDocument setChangeStreamOptions(@Nullable BsonDocument postBatchResumeToken, BsonTimestamp operationTime, int maxWireVersion, boolean firstBatchEmpty) {
      BsonDocument resumeToken = null;
      if (this.startAfter != null) {
         resumeToken = this.startAfter;
      } else if (this.resumeAfter != null) {
         resumeToken = this.resumeAfter;
      } else if (this.startAtOperationTime == null && postBatchResumeToken == null && firstBatchEmpty && maxWireVersion >= 7) {
         this.startAtOperationTime = operationTime;
      }

      return resumeToken;
   }

   public void setChangeStreamOptionsForResume(@Nullable BsonDocument resumeToken, int maxWireVersion) {
      this.startAfter = null;
      if (resumeToken != null) {
         this.startAtOperationTime = null;
         this.resumeAfter = resumeToken;
      } else if (this.startAtOperationTime != null && maxWireVersion >= 7) {
         this.resumeAfter = null;
      } else {
         this.resumeAfter = null;
         this.startAtOperationTime = null;
      }

   }

   private AggregateOperationImpl.AggregateTarget getAggregateTarget() {
      return () -> {
         return (BsonValue)(this.changeStreamLevel == ChangeStreamLevel.COLLECTION ? new BsonString(this.getNamespace().getCollectionName()) : new BsonInt32(1));
      };
   }

   private AggregateOperationImpl.PipelineCreator getPipelineCreator() {
      return () -> {
         List<BsonDocument> changeStreamPipeline = new ArrayList();
         BsonDocument changeStream = new BsonDocument();
         if (this.fullDocument != FullDocument.DEFAULT) {
            changeStream.append("fullDocument", new BsonString(this.fullDocument.getValue()));
         }

         if (this.fullDocumentBeforeChange != FullDocumentBeforeChange.DEFAULT) {
            changeStream.append("fullDocumentBeforeChange", new BsonString(this.fullDocumentBeforeChange.getValue()));
         }

         if (this.changeStreamLevel == ChangeStreamLevel.CLIENT) {
            changeStream.append("allChangesForCluster", BsonBoolean.TRUE);
         }

         if (this.showExpandedEvents) {
            changeStream.append("showExpandedEvents", BsonBoolean.TRUE);
         }

         if (this.resumeAfter != null) {
            changeStream.append("resumeAfter", this.resumeAfter);
         }

         if (this.startAfter != null) {
            changeStream.append("startAfter", this.startAfter);
         }

         if (this.startAtOperationTime != null) {
            changeStream.append("startAtOperationTime", this.startAtOperationTime);
         }

         changeStreamPipeline.add(new BsonDocument("$changeStream", changeStream));
         changeStreamPipeline.addAll(this.getPipeline());
         return new BsonArray(changeStreamPipeline);
      };
   }
}
