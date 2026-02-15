package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public class DropIndexOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private final MongoNamespace namespace;
   private final String indexName;
   private final BsonDocument indexKeys;
   private final WriteConcern writeConcern;
   private long maxTimeMS;

   public DropIndexOperation(MongoNamespace namespace, String indexName) {
      this(namespace, (String)indexName, (WriteConcern)null);
   }

   public DropIndexOperation(MongoNamespace namespace, BsonDocument keys) {
      this(namespace, (BsonDocument)keys, (WriteConcern)null);
   }

   public DropIndexOperation(MongoNamespace namespace, String indexName, @Nullable WriteConcern writeConcern) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.indexName = (String)Assertions.notNull("indexName", indexName);
      this.indexKeys = null;
      this.writeConcern = writeConcern;
   }

   public DropIndexOperation(MongoNamespace namespace, BsonDocument indexKeys, @Nullable WriteConcern writeConcern) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.indexKeys = (BsonDocument)Assertions.notNull("indexKeys", indexKeys);
      this.indexName = null;
      this.writeConcern = writeConcern;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public DropIndexOperation maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public Void execute(WriteBinding binding) {
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         try {
            SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer());
         } catch (MongoCommandException e) {
            CommandOperationHelper.rethrowIfNotNamespaceError(e);
         }

         return null;
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
         SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            SingleResultCallback<Void> releasingCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);
            AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), (result, t1) -> {
               if (t1 != null && !CommandOperationHelper.isNamespaceError(t1)) {
                  releasingCallback.onResult(null, t1);
               } else {
                  releasingCallback.onResult(result, (Throwable)null);
               }

            });
         }

      });
   }

   private BsonDocument getCommand() {
      BsonDocument command = new BsonDocument("dropIndexes", new BsonString(this.namespace.getCollectionName()));
      if (this.indexName != null) {
         command.put((String)"index", (BsonValue)(new BsonString(this.indexName)));
      } else {
         command.put((String)"index", (BsonValue)this.indexKeys);
      }

      DocumentHelper.putIfNotZero(command, "maxTimeMS", this.maxTimeMS);
      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, command);
      return command;
   }
}
