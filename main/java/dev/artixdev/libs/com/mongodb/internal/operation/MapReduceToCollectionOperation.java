package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonJavaScript;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;

public class MapReduceToCollectionOperation implements AsyncWriteOperation<MapReduceStatistics>, WriteOperation<MapReduceStatistics> {
   private final MongoNamespace namespace;
   private final BsonJavaScript mapFunction;
   private final BsonJavaScript reduceFunction;
   private final String collectionName;
   private final WriteConcern writeConcern;
   private BsonJavaScript finalizeFunction;
   private BsonDocument scope;
   private BsonDocument filter;
   private BsonDocument sort;
   private int limit;
   private boolean jsMode;
   private boolean verbose;
   private long maxTimeMS;
   private String action;
   private String databaseName;
   private boolean sharded;
   private boolean nonAtomic;
   private Boolean bypassDocumentValidation;
   private Collation collation;
   private static final List<String> VALID_ACTIONS = Arrays.asList("replace", "merge", "reduce");

   public MapReduceToCollectionOperation(MongoNamespace namespace, BsonJavaScript mapFunction, BsonJavaScript reduceFunction, String collectionName) {
      this(namespace, mapFunction, reduceFunction, collectionName, (WriteConcern)null);
   }

   public MapReduceToCollectionOperation(MongoNamespace namespace, BsonJavaScript mapFunction, BsonJavaScript reduceFunction, @Nullable String collectionName, @Nullable WriteConcern writeConcern) {
      this.action = "replace";
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.mapFunction = (BsonJavaScript)Assertions.notNull("mapFunction", mapFunction);
      this.reduceFunction = (BsonJavaScript)Assertions.notNull("reduceFunction", reduceFunction);
      this.collectionName = (String)Assertions.notNull("collectionName", collectionName);
      this.writeConcern = writeConcern;
   }

   public MongoNamespace getNamespace() {
      return this.namespace;
   }

   public BsonJavaScript getMapFunction() {
      return this.mapFunction;
   }

   public BsonJavaScript getReduceFunction() {
      return this.reduceFunction;
   }

   public String getCollectionName() {
      return this.collectionName;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public BsonJavaScript getFinalizeFunction() {
      return this.finalizeFunction;
   }

   public MapReduceToCollectionOperation finalizeFunction(BsonJavaScript finalizeFunction) {
      this.finalizeFunction = finalizeFunction;
      return this;
   }

   public BsonDocument getScope() {
      return this.scope;
   }

   public MapReduceToCollectionOperation scope(@Nullable BsonDocument scope) {
      this.scope = scope;
      return this;
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public MapReduceToCollectionOperation filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public BsonDocument getSort() {
      return this.sort;
   }

   public MapReduceToCollectionOperation sort(@Nullable BsonDocument sort) {
      this.sort = sort;
      return this;
   }

   public int getLimit() {
      return this.limit;
   }

   public MapReduceToCollectionOperation limit(int limit) {
      this.limit = limit;
      return this;
   }

   public boolean isJsMode() {
      return this.jsMode;
   }

   public MapReduceToCollectionOperation jsMode(boolean jsMode) {
      this.jsMode = jsMode;
      return this;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public MapReduceToCollectionOperation verbose(boolean verbose) {
      this.verbose = verbose;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public MapReduceToCollectionOperation maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public String getAction() {
      return this.action;
   }

   public MapReduceToCollectionOperation action(String action) {
      Assertions.notNull("action", action);
      Assertions.isTrue("action must be one of: \"replace\", \"merge\", \"reduce\"", VALID_ACTIONS.contains(action));
      this.action = action;
      return this;
   }

   @Nullable
   public String getDatabaseName() {
      return this.databaseName;
   }

   public MapReduceToCollectionOperation databaseName(@Nullable String databaseName) {
      this.databaseName = databaseName;
      return this;
   }

   public boolean isSharded() {
      return this.sharded;
   }

   public MapReduceToCollectionOperation sharded(boolean sharded) {
      this.sharded = sharded;
      return this;
   }

   public boolean isNonAtomic() {
      return this.nonAtomic;
   }

   public MapReduceToCollectionOperation nonAtomic(boolean nonAtomic) {
      this.nonAtomic = nonAtomic;
      return this;
   }

   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public MapReduceToCollectionOperation bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public MapReduceToCollectionOperation collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public MapReduceStatistics execute(WriteBinding binding) {
      return (MapReduceStatistics)SyncOperationHelper.withConnection(binding, (connection) -> {
         return (MapReduceStatistics)Assertions.assertNotNull((MapReduceStatistics)SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, this.transformer()));
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<MapReduceStatistics> callback) {
      AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
         SingleResultCallback<MapReduceStatistics> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, this.transformerAsync(), AsyncOperationHelper.releasingCallback(errHandlingCallback, connection));
         }

      });
   }

   public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
      return this.createExplainableOperation(explainVerbosity);
   }

   public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(ExplainVerbosity explainVerbosity) {
      return this.createExplainableOperation(explainVerbosity);
   }

   private CommandReadOperation<BsonDocument> createExplainableOperation(ExplainVerbosity explainVerbosity) {
      return new CommandReadOperation(this.namespace.getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand((ConnectionDescription)null), explainVerbosity), new BsonDocumentCodec());
   }

   private SyncOperationHelper.CommandWriteTransformer<BsonDocument, MapReduceStatistics> transformer() {
      return (result, connection) -> {
         WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
         return MapReduceHelper.createStatistics(result);
      };
   }

   private AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, MapReduceStatistics> transformerAsync() {
      return (result, connection) -> {
         WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
         return MapReduceHelper.createStatistics(result);
      };
   }

   private BsonDocument getCommand(@Nullable ConnectionDescription description) {
      BsonDocument outputDocument = new BsonDocument(this.getAction(), new BsonString(this.getCollectionName()));
      if (description != null && !ServerVersionHelper.serverIsAtLeastVersionFourDotFour(description)) {
         DocumentHelper.putIfTrue(outputDocument, "sharded", this.isSharded());
         DocumentHelper.putIfTrue(outputDocument, "nonAtomic", this.isNonAtomic());
      }

      if (this.getDatabaseName() != null) {
         outputDocument.put((String)"db", (BsonValue)(new BsonString(this.getDatabaseName())));
      }

      BsonDocument commandDocument = (new BsonDocument("mapreduce", new BsonString(this.namespace.getCollectionName()))).append("map", this.getMapFunction()).append("reduce", this.getReduceFunction()).append("out", outputDocument);
      DocumentHelper.putIfNotNull(commandDocument, "query", (BsonValue)this.getFilter());
      DocumentHelper.putIfNotNull(commandDocument, "sort", (BsonValue)this.getSort());
      DocumentHelper.putIfNotNull(commandDocument, "finalize", (BsonValue)this.getFinalizeFunction());
      DocumentHelper.putIfNotNull(commandDocument, "scope", (BsonValue)this.getScope());
      DocumentHelper.putIfTrue(commandDocument, "verbose", this.isVerbose());
      DocumentHelper.putIfNotZero(commandDocument, "limit", this.getLimit());
      DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
      DocumentHelper.putIfTrue(commandDocument, "jsMode", this.isJsMode());
      if (this.bypassDocumentValidation != null && description != null) {
         commandDocument.put((String)"bypassDocumentValidation", (BsonValue)BsonBoolean.valueOf(this.bypassDocumentValidation));
      }

      if (description != null) {
         WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
      }

      if (this.collation != null) {
         commandDocument.put((String)"collation", (BsonValue)this.collation.asDocument());
      }

      return commandDocument;
   }
}
