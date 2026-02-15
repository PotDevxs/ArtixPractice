package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class DistinctOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>> {
   private static final String VALUES = "values";
   private final MongoNamespace namespace;
   private final String fieldName;
   private final Decoder<T> decoder;
   private boolean retryReads;
   private BsonDocument filter;
   private long maxTimeMS;
   private Collation collation;
   private BsonValue comment;

   public DistinctOperation(MongoNamespace namespace, String fieldName, Decoder<T> decoder) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.fieldName = (String)Assertions.notNull("fieldName", fieldName);
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public DistinctOperation<T> filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public DistinctOperation<T> retryReads(boolean retryReads) {
      this.retryReads = retryReads;
      return this;
   }

   public boolean getRetryReads() {
      return this.retryReads;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public DistinctOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public DistinctOperation<T> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public BsonValue getComment() {
      return this.comment;
   }

   public DistinctOperation<T> comment(BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      return (BatchCursor)SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), this.createCommandDecoder(), this.transformer(), this.retryReads);
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), this.createCommandDecoder(), this.asyncTransformer(), this.retryReads, ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
   }

   private Codec<BsonDocument> createCommandDecoder() {
      return CommandResultDocumentCodec.create(this.decoder, "values");
   }

   private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
      return new QueryResult(this.namespace, BsonDocumentWrapperHelper.toList(result, "values"), 0L, description.getServerAddress());
   }

   private SyncOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> transformer() {
      return (result, source, connection) -> {
         QueryResult<T> queryResult = this.createQueryResult(result, connection.getDescription());
         return new QueryBatchCursor(queryResult, 0, 0, this.decoder, this.comment, source);
      };
   }

   private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
      return (result, source, connection) -> {
         QueryResult<T> queryResult = this.createQueryResult(result, connection.getDescription());
         return new AsyncSingleBatchQueryCursor(queryResult);
      };
   }

   private CommandOperationHelper.CommandCreator getCommandCreator(SessionContext sessionContext) {
      return (serverDescription, connectionDescription) -> {
         return this.getCommand(sessionContext, connectionDescription);
      };
   }

   private BsonDocument getCommand(SessionContext sessionContext, ConnectionDescription connectionDescription) {
      BsonDocument commandDocument = new BsonDocument("distinct", new BsonString(this.namespace.getCollectionName()));
      OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, connectionDescription.getMaxWireVersion(), commandDocument);
      commandDocument.put((String)"key", (BsonValue)(new BsonString(this.fieldName)));
      DocumentHelper.putIfNotNull(commandDocument, "query", (BsonValue)this.filter);
      DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.maxTimeMS);
      if (this.collation != null) {
         commandDocument.put((String)"collation", (BsonValue)this.collation.asDocument());
      }

      DocumentHelper.putIfNotNull(commandDocument, "comment", this.comment);
      return commandDocument;
   }
}
