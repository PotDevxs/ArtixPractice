package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.CursorType;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.FindIterable;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.client.model.FindOptions;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ExplainableReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

class FindIterableImpl<TDocument, TResult> extends MongoIterableImpl<TResult> implements FindIterable<TResult> {
   private final SyncOperations<TDocument> operations;
   private final Class<TResult> resultClass;
   private final FindOptions findOptions;
   private final CodecRegistry codecRegistry;
   private Bson filter;

   FindIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, Bson filter) {
      this(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, filter, true);
   }

   FindIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, Bson filter, boolean retryReads) {
      super(clientSession, executor, readConcern, readPreference, retryReads);
      this.operations = new SyncOperations(namespace, documentClass, readPreference, codecRegistry, retryReads);
      this.resultClass = (Class)Assertions.notNull("resultClass", resultClass);
      this.filter = (Bson)Assertions.notNull("filter", filter);
      this.findOptions = new FindOptions();
      this.codecRegistry = codecRegistry;
   }

   public FindIterable<TResult> filter(@Nullable Bson filter) {
      this.filter = filter;
      return this;
   }

   public FindIterable<TResult> limit(int limit) {
      this.findOptions.limit(limit);
      return this;
   }

   public FindIterable<TResult> skip(int skip) {
      this.findOptions.skip(skip);
      return this;
   }

   public FindIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.findOptions.maxTime(maxTime, timeUnit);
      return this;
   }

   public FindIterable<TResult> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.findOptions.maxAwaitTime(maxAwaitTime, timeUnit);
      return this;
   }

   public FindIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      this.findOptions.batchSize(batchSize);
      return this;
   }

   public FindIterable<TResult> collation(@Nullable Collation collation) {
      this.findOptions.collation(collation);
      return this;
   }

   public FindIterable<TResult> projection(@Nullable Bson projection) {
      this.findOptions.projection(projection);
      return this;
   }

   public FindIterable<TResult> sort(@Nullable Bson sort) {
      this.findOptions.sort(sort);
      return this;
   }

   public FindIterable<TResult> noCursorTimeout(boolean noCursorTimeout) {
      this.findOptions.noCursorTimeout(noCursorTimeout);
      return this;
   }

   /** @deprecated */
   @Deprecated
   public FindIterable<TResult> oplogReplay(boolean oplogReplay) {
      this.findOptions.oplogReplay(oplogReplay);
      return this;
   }

   public FindIterable<TResult> partial(boolean partial) {
      this.findOptions.partial(partial);
      return this;
   }

   public FindIterable<TResult> cursorType(CursorType cursorType) {
      this.findOptions.cursorType(cursorType);
      return this;
   }

   public FindIterable<TResult> comment(@Nullable String comment) {
      this.findOptions.comment(comment);
      return this;
   }

   public FindIterable<TResult> comment(@Nullable BsonValue comment) {
      this.findOptions.comment(comment);
      return this;
   }

   public FindIterable<TResult> hint(@Nullable Bson hint) {
      this.findOptions.hint(hint);
      return this;
   }

   public FindIterable<TResult> hintString(@Nullable String hint) {
      this.findOptions.hintString(hint);
      return this;
   }

   public FindIterable<TResult> let(@Nullable Bson variables) {
      this.findOptions.let(variables);
      return this;
   }

   public FindIterable<TResult> max(@Nullable Bson max) {
      this.findOptions.max(max);
      return this;
   }

   public FindIterable<TResult> min(@Nullable Bson min) {
      this.findOptions.min(min);
      return this;
   }

   public FindIterable<TResult> returnKey(boolean returnKey) {
      this.findOptions.returnKey(returnKey);
      return this;
   }

   public FindIterable<TResult> showRecordId(boolean showRecordId) {
      this.findOptions.showRecordId(showRecordId);
      return this;
   }

   public FindIterable<TResult> allowDiskUse(@Nullable Boolean allowDiskUse) {
      this.findOptions.allowDiskUse(allowDiskUse);
      return this;
   }

   @Nullable
   public TResult first() {
      BatchCursor batchCursor = (BatchCursor)this.getExecutor().execute(this.operations.findFirst(this.filter, this.resultClass, this.findOptions), this.getReadPreference(), this.getReadConcern(), this.getClientSession());

      TResult var2;
      try {
         var2 = batchCursor.hasNext() ? (TResult) batchCursor.next().iterator().next() : null;
      } catch (Throwable e) {
         if (batchCursor != null) {
            try {
               batchCursor.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }
         }

         throw e;
      }

      if (batchCursor != null) {
         batchCursor.close();
      }

      return var2;
   }

   public Document explain() {
      return (Document)this.executeExplain(Document.class, (ExplainVerbosity)null);
   }

   public Document explain(ExplainVerbosity verbosity) {
      return (Document)this.executeExplain(Document.class, (ExplainVerbosity)Assertions.notNull("verbosity", verbosity));
   }

   public <E> E explain(Class<E> explainDocumentClass) {
      return this.executeExplain(explainDocumentClass, (ExplainVerbosity)null);
   }

   public <E> E explain(Class<E> explainResultClass, ExplainVerbosity verbosity) {
      return this.executeExplain(explainResultClass, (ExplainVerbosity)Assertions.notNull("verbosity", verbosity));
   }

   private <E> E executeExplain(Class<E> explainResultClass, @Nullable ExplainVerbosity verbosity) {
      Assertions.notNull("explainDocumentClass", explainResultClass);
      return this.getExecutor().execute(this.asReadOperation().asExplainableOperation(verbosity, this.codecRegistry.get(explainResultClass)), this.getReadPreference(), this.getReadConcern(), this.getClientSession());
   }

   public ExplainableReadOperation<BatchCursor<TResult>> asReadOperation() {
      return this.operations.find(this.filter, this.resultClass, this.findOptions);
   }
}
