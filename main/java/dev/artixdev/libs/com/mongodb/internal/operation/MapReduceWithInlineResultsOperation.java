package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.NoOpSessionContext;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonJavaScript;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class MapReduceWithInlineResultsOperation<T> implements AsyncReadOperation<MapReduceAsyncBatchCursor<T>>, ReadOperation<MapReduceBatchCursor<T>> {
   private final MongoNamespace namespace;
   private final BsonJavaScript mapFunction;
   private final BsonJavaScript reduceFunction;
   private final Decoder<T> decoder;
   private BsonJavaScript finalizeFunction;
   private BsonDocument scope;
   private BsonDocument filter;
   private BsonDocument sort;
   private int limit;
   private boolean jsMode;
   private boolean verbose;
   private long maxTimeMS;
   private Collation collation;

   public MapReduceWithInlineResultsOperation(MongoNamespace namespace, BsonJavaScript mapFunction, BsonJavaScript reduceFunction, Decoder<T> decoder) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.mapFunction = (BsonJavaScript)Assertions.notNull("mapFunction", mapFunction);
      this.reduceFunction = (BsonJavaScript)Assertions.notNull("reduceFunction", reduceFunction);
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
   }

   public MongoNamespace getNamespace() {
      return this.namespace;
   }

   public Decoder<T> getDecoder() {
      return this.decoder;
   }

   public BsonJavaScript getMapFunction() {
      return this.mapFunction;
   }

   public BsonJavaScript getReduceFunction() {
      return this.reduceFunction;
   }

   public BsonJavaScript getFinalizeFunction() {
      return this.finalizeFunction;
   }

   public MapReduceWithInlineResultsOperation<T> finalizeFunction(BsonJavaScript finalizeFunction) {
      this.finalizeFunction = finalizeFunction;
      return this;
   }

   public BsonDocument getScope() {
      return this.scope;
   }

   public MapReduceWithInlineResultsOperation<T> scope(@Nullable BsonDocument scope) {
      this.scope = scope;
      return this;
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public MapReduceWithInlineResultsOperation<T> filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public BsonDocument getSort() {
      return this.sort;
   }

   public MapReduceWithInlineResultsOperation<T> sort(@Nullable BsonDocument sort) {
      this.sort = sort;
      return this;
   }

   public int getLimit() {
      return this.limit;
   }

   public MapReduceWithInlineResultsOperation<T> limit(int limit) {
      this.limit = limit;
      return this;
   }

   public boolean isJsMode() {
      return this.jsMode;
   }

   public MapReduceWithInlineResultsOperation<T> jsMode(boolean jsMode) {
      this.jsMode = jsMode;
      return this;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public MapReduceWithInlineResultsOperation<T> verbose(boolean verbose) {
      this.verbose = verbose;
      return this;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public MapReduceWithInlineResultsOperation<T> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public MapReduceWithInlineResultsOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public MapReduceBatchCursor<T> execute(ReadBinding binding) {
      return (MapReduceBatchCursor)SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "results"), this.transformer(), false);
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<MapReduceAsyncBatchCursor<T>> callback) {
      SingleResultCallback<MapReduceAsyncBatchCursor<T>> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
      AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "results"), this.asyncTransformer(), false, errHandlingCallback);
   }

   public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
      return this.createExplainableOperation(explainVerbosity);
   }

   public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(ExplainVerbosity explainVerbosity) {
      return this.createExplainableOperation(explainVerbosity);
   }

   private CommandReadOperation<BsonDocument> createExplainableOperation(ExplainVerbosity explainVerbosity) {
      return new CommandReadOperation(this.namespace.getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(NoOpSessionContext.INSTANCE, 0), explainVerbosity), new BsonDocumentCodec());
   }

   private SyncOperationHelper.CommandReadTransformer<BsonDocument, MapReduceBatchCursor<T>> transformer() {
      return (result, source, connection) -> {
         return new MapReduceInlineResultsCursor(this.createQueryResult(result, connection.getDescription()), this.decoder, source, MapReduceHelper.createStatistics(result));
      };
   }

   private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, MapReduceAsyncBatchCursor<T>> asyncTransformer() {
      return (result, source, connection) -> {
         return new MapReduceInlineResultsAsyncCursor(this.createQueryResult(result, connection.getDescription()), MapReduceHelper.createStatistics(result));
      };
   }

   private CommandOperationHelper.CommandCreator getCommandCreator(SessionContext sessionContext) {
      return (serverDescription, connectionDescription) -> {
         return this.getCommand(sessionContext, connectionDescription.getMaxWireVersion());
      };
   }

   private BsonDocument getCommand(SessionContext sessionContext, int maxWireVersion) {
      BsonDocument commandDocument = (new BsonDocument("mapreduce", new BsonString(this.namespace.getCollectionName()))).append("map", this.getMapFunction()).append("reduce", this.getReduceFunction()).append("out", new BsonDocument("inline", new BsonInt32(1)));
      DocumentHelper.putIfNotNull(commandDocument, "query", (BsonValue)this.getFilter());
      DocumentHelper.putIfNotNull(commandDocument, "sort", (BsonValue)this.getSort());
      DocumentHelper.putIfNotNull(commandDocument, "finalize", (BsonValue)this.getFinalizeFunction());
      DocumentHelper.putIfNotNull(commandDocument, "scope", (BsonValue)this.getScope());
      DocumentHelper.putIfTrue(commandDocument, "verbose", this.isVerbose());
      OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, maxWireVersion, commandDocument);
      DocumentHelper.putIfNotZero(commandDocument, "limit", this.getLimit());
      DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
      DocumentHelper.putIfTrue(commandDocument, "jsMode", this.isJsMode());
      if (this.collation != null) {
         commandDocument.put((String)"collation", (BsonValue)this.collation.asDocument());
      }

      return commandDocument;
   }

   private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
      return new QueryResult(this.namespace, BsonDocumentWrapperHelper.toList(result, "results"), 0L, description.getServerAddress());
   }
}
