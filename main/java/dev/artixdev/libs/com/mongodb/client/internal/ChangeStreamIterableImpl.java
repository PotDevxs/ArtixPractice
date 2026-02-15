package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ChangeStreamIterable;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.MongoChangeStreamCursor;
import dev.artixdev.libs.com.mongodb.client.MongoCursor;
import dev.artixdev.libs.com.mongodb.client.MongoIterable;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.changestream.ChangeStreamDocument;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocument;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import dev.artixdev.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.RawBsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class ChangeStreamIterableImpl<TResult> extends MongoIterableImpl<ChangeStreamDocument<TResult>> implements ChangeStreamIterable<TResult> {
   private final CodecRegistry codecRegistry;
   private final List<? extends Bson> pipeline;
   private final Codec<ChangeStreamDocument<TResult>> codec;
   private final ChangeStreamLevel changeStreamLevel;
   private final SyncOperations<TResult> operations;
   private FullDocument fullDocument;
   private FullDocumentBeforeChange fullDocumentBeforeChange;
   private BsonDocument resumeToken;
   private BsonDocument startAfter;
   private long maxAwaitTimeMS;
   private Collation collation;
   private BsonTimestamp startAtOperationTime;
   private BsonValue comment;
   private boolean showExpandedEvents;

   public ChangeStreamIterableImpl(@Nullable ClientSession clientSession, String databaseName, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
      this(clientSession, new MongoNamespace(databaseName, "ignored"), codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
   }

   public ChangeStreamIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
      super(clientSession, executor, readConcern, readPreference, retryReads);
      this.fullDocument = FullDocument.DEFAULT;
      this.fullDocumentBeforeChange = FullDocumentBeforeChange.DEFAULT;
      this.codecRegistry = (CodecRegistry)Assertions.notNull("codecRegistry", codecRegistry);
      this.pipeline = (List)Assertions.notNull("pipeline", pipeline);
      this.codec = ChangeStreamDocument.createCodec((Class)Assertions.notNull("resultClass", resultClass), codecRegistry);
      this.changeStreamLevel = (ChangeStreamLevel)Assertions.notNull("changeStreamLevel", changeStreamLevel);
      this.operations = new SyncOperations(namespace, resultClass, readPreference, codecRegistry, retryReads);
   }

   public ChangeStreamIterable<TResult> fullDocument(FullDocument fullDocument) {
      this.fullDocument = (FullDocument)Assertions.notNull("fullDocument", fullDocument);
      return this;
   }

   public ChangeStreamIterable<TResult> fullDocumentBeforeChange(FullDocumentBeforeChange fullDocumentBeforeChange) {
      this.fullDocumentBeforeChange = (FullDocumentBeforeChange)Assertions.notNull("fullDocumentBeforeChange", fullDocumentBeforeChange);
      return this;
   }

   public ChangeStreamIterable<TResult> resumeAfter(BsonDocument resumeAfter) {
      this.resumeToken = (BsonDocument)Assertions.notNull("resumeAfter", resumeAfter);
      return this;
   }

   public ChangeStreamIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public ChangeStreamIterable<TResult> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
      return this;
   }

   public ChangeStreamIterable<TResult> collation(@Nullable Collation collation) {
      this.collation = (Collation)Assertions.notNull("collation", collation);
      return this;
   }

   public <TDocument> MongoIterable<TDocument> withDocumentClass(final Class<TDocument> clazz) {
      return new MongoIterableImpl<TDocument>(this.getClientSession(), this.getExecutor(), this.getReadConcern(), this.getReadPreference(), this.getRetryReads()) {
         public MongoCursor<TDocument> iterator() {
            return this.cursor();
         }

         public MongoChangeStreamCursor<TDocument> cursor() {
            return new MongoChangeStreamCursorImpl(ChangeStreamIterableImpl.this.execute(), ChangeStreamIterableImpl.this.codecRegistry.get(clazz), ChangeStreamIterableImpl.this.initialResumeToken());
         }

         public ReadOperation<BatchCursor<TDocument>> asReadOperation() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public ChangeStreamIterable<TResult> startAtOperationTime(BsonTimestamp startAtOperationTime) {
      this.startAtOperationTime = (BsonTimestamp)Assertions.notNull("startAtOperationTime", startAtOperationTime);
      return this;
   }

   public ChangeStreamIterableImpl<TResult> startAfter(BsonDocument startAfter) {
      this.startAfter = (BsonDocument)Assertions.notNull("startAfter", startAfter);
      return this;
   }

   public ChangeStreamIterable<TResult> comment(@Nullable String comment) {
      this.comment = comment == null ? null : new BsonString(comment);
      return this;
   }

   public ChangeStreamIterable<TResult> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public ChangeStreamIterable<TResult> showExpandedEvents(boolean showExpandedEvents) {
      this.showExpandedEvents = showExpandedEvents;
      return this;
   }

   public MongoCursor<ChangeStreamDocument<TResult>> iterator() {
      return this.cursor();
   }

   public MongoChangeStreamCursor<ChangeStreamDocument<TResult>> cursor() {
      return new MongoChangeStreamCursorImpl(this.execute(), this.codec, this.initialResumeToken());
   }

   @Nullable
   public ChangeStreamDocument<TResult> first() {
      MongoChangeStreamCursor cursor = this.cursor();

      ChangeStreamDocument var2;
      label43: {
         try {
            if (!cursor.hasNext()) {
               var2 = null;
               break label43;
            }

            var2 = (ChangeStreamDocument)cursor.next();
         } catch (Throwable e) {
            if (cursor != null) {
               try {
                  cursor.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (cursor != null) {
            cursor.close();
         }

         return var2;
      }

      if (cursor != null) {
         cursor.close();
      }

      return var2;
   }

   public ReadOperation<BatchCursor<ChangeStreamDocument<TResult>>> asReadOperation() {
      throw new UnsupportedOperationException();
   }

   private ReadOperation<BatchCursor<RawBsonDocument>> createChangeStreamOperation() {
      return this.operations.changeStream(this.fullDocument, this.fullDocumentBeforeChange, this.pipeline, new RawBsonDocumentCodec(), this.changeStreamLevel, this.getBatchSize(), this.collation, this.comment, this.maxAwaitTimeMS, this.resumeToken, this.startAtOperationTime, this.startAfter, this.showExpandedEvents);
   }

   private BatchCursor<RawBsonDocument> execute() {
      return (BatchCursor)this.getExecutor().execute(this.createChangeStreamOperation(), this.getReadPreference(), this.getReadConcern(), this.getClientSession());
   }

   private BsonDocument initialResumeToken() {
      return this.startAfter != null ? this.startAfter : this.resumeToken;
   }
}
