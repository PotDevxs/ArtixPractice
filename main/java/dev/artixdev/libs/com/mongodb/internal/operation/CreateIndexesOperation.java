package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.CreateIndexCommitQuorum;
import dev.artixdev.libs.com.mongodb.DuplicateKeyException;
import dev.artixdev.libs.com.mongodb.ErrorCategory;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.WriteConcernResult;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.bulk.IndexRequest;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDouble;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public class CreateIndexesOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private final MongoNamespace namespace;
   private final List<IndexRequest> requests;
   private final WriteConcern writeConcern;
   private long maxTimeMS;
   private CreateIndexCommitQuorum commitQuorum;

   public CreateIndexesOperation(MongoNamespace namespace, List<IndexRequest> requests) {
      this(namespace, requests, (WriteConcern)null);
   }

   public CreateIndexesOperation(MongoNamespace namespace, List<IndexRequest> requests, @Nullable WriteConcern writeConcern) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.requests = (List)Assertions.notNull("indexRequests", requests);
      this.writeConcern = writeConcern;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public List<IndexRequest> getRequests() {
      return this.requests;
   }

   public List<String> getIndexNames() {
      List<String> indexNames = new ArrayList(this.requests.size());
      Iterator<IndexRequest> iterator = this.requests.iterator();

      while(iterator.hasNext()) {
         IndexRequest request = iterator.next();
         if (request.getName() != null) {
            indexNames.add(request.getName());
         } else {
            indexNames.add(IndexHelper.generateIndexName(request.getKeys()));
         }
      }

      return indexNames;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public CreateIndexesOperation maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public CreateIndexCommitQuorum getCommitQuorum() {
      return this.commitQuorum;
   }

   public CreateIndexesOperation commitQuorum(@Nullable CreateIndexCommitQuorum commitQuorum) {
      this.commitQuorum = commitQuorum;
      return this;
   }

   public Void execute(WriteBinding binding) {
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         try {
            SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, SyncOperationHelper.writeConcernErrorTransformer());
            return null;
         } catch (MongoCommandException e) {
            throw this.checkForDuplicateKeyError(e);
         }
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
         SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            SingleResultCallback wrappedCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);

            try {
               AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), (result, t12) -> {
                  wrappedCallback.onResult(null, this.translateException(t12));
               });
            } catch (Throwable throwable) {
               wrappedCallback.onResult(null, throwable);
            }
         }

      });
   }

   private BsonDocument getIndex(IndexRequest request) {
      BsonDocument index = new BsonDocument();
      index.append("key", request.getKeys());
      index.append("name", new BsonString(request.getName() != null ? request.getName() : IndexHelper.generateIndexName(request.getKeys())));
      if (request.isBackground()) {
         index.append("background", BsonBoolean.TRUE);
      }

      if (request.isUnique()) {
         index.append("unique", BsonBoolean.TRUE);
      }

      if (request.isSparse()) {
         index.append("sparse", BsonBoolean.TRUE);
      }

      if (request.getExpireAfter(TimeUnit.SECONDS) != null) {
         index.append("expireAfterSeconds", new BsonInt64((Long)Assertions.assertNotNull(request.getExpireAfter(TimeUnit.SECONDS))));
      }

      if (request.getVersion() != null) {
         index.append("v", new BsonInt32((Integer)Assertions.assertNotNull(request.getVersion())));
      }

      if (request.getWeights() != null) {
         index.append("weights", (BsonValue)Assertions.assertNotNull(request.getWeights()));
      }

      if (request.getDefaultLanguage() != null) {
         index.append("default_language", new BsonString((String)Assertions.assertNotNull(request.getDefaultLanguage())));
      }

      if (request.getLanguageOverride() != null) {
         index.append("language_override", new BsonString((String)Assertions.assertNotNull(request.getLanguageOverride())));
      }

      if (request.getTextVersion() != null) {
         index.append("textIndexVersion", new BsonInt32((Integer)Assertions.assertNotNull(request.getTextVersion())));
      }

      if (request.getSphereVersion() != null) {
         index.append("2dsphereIndexVersion", new BsonInt32((Integer)Assertions.assertNotNull(request.getSphereVersion())));
      }

      if (request.getBits() != null) {
         index.append("bits", new BsonInt32((Integer)Assertions.assertNotNull(request.getBits())));
      }

      if (request.getMin() != null) {
         index.append("min", new BsonDouble((Double)Assertions.assertNotNull(request.getMin())));
      }

      if (request.getMax() != null) {
         index.append("max", new BsonDouble((Double)Assertions.assertNotNull(request.getMax())));
      }

      if (request.getBucketSize() != null) {
         index.append("bucketSize", new BsonDouble((Double)Assertions.assertNotNull(request.getBucketSize())));
      }

      if (request.getDropDups()) {
         index.append("dropDups", BsonBoolean.TRUE);
      }

      if (request.getStorageEngine() != null) {
         index.append("storageEngine", (BsonValue)Assertions.assertNotNull(request.getStorageEngine()));
      }

      if (request.getPartialFilterExpression() != null) {
         index.append("partialFilterExpression", (BsonValue)Assertions.assertNotNull(request.getPartialFilterExpression()));
      }

      if (request.getCollation() != null) {
         index.append("collation", (BsonValue)Assertions.assertNotNull(request.getCollation().asDocument()));
      }

      if (request.getWildcardProjection() != null) {
         index.append("wildcardProjection", (BsonValue)Assertions.assertNotNull(request.getWildcardProjection()));
      }

      if (request.isHidden()) {
         index.append("hidden", BsonBoolean.TRUE);
      }

      return index;
   }

   private BsonDocument getCommand(ConnectionDescription description) {
      BsonDocument command = new BsonDocument("createIndexes", new BsonString(this.namespace.getCollectionName()));
      List<BsonDocument> values = new ArrayList();
      Iterator<IndexRequest> iterator = this.requests.iterator();

      while(iterator.hasNext()) {
         IndexRequest request = iterator.next();
         values.add(this.getIndex(request));
      }

      command.put((String)"indexes", (BsonValue)(new BsonArray(values)));
      DocumentHelper.putIfNotZero(command, "maxTimeMS", this.maxTimeMS);
      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, command);
      if (this.commitQuorum != null) {
         if (!ServerVersionHelper.serverIsAtLeastVersionFourDotFour(description)) {
            throw new MongoClientException("Specifying a value for the create index commit quorum option requires a minimum MongoDB version of 4.4");
         }

         command.put("commitQuorum", this.commitQuorum.toBsonValue());
      }

      return command;
   }

   @Nullable
   private MongoException translateException(@Nullable Throwable t) {
      return t instanceof MongoCommandException ? this.checkForDuplicateKeyError((MongoCommandException)t) : MongoException.fromThrowable(t);
   }

   private MongoException checkForDuplicateKeyError(MongoCommandException e) {
      return (MongoException)(ErrorCategory.fromErrorCode(e.getCode()) == ErrorCategory.DUPLICATE_KEY ? new DuplicateKeyException(e.getResponse(), e.getServerAddress(), WriteConcernResult.acknowledged(0, false, (BsonValue)null)) : e);
   }
}
