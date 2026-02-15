package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

abstract class AbstractWriteSearchIndexOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private final MongoNamespace namespace;
   private final WriteConcern writeConcern;

   AbstractWriteSearchIndexOperation(MongoNamespace mongoNamespace, WriteConcern writeConcern) {
      this.namespace = mongoNamespace;
      this.writeConcern = writeConcern;
   }

   public Void execute(WriteBinding binding) {
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         try {
            SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.buildCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer());
         } catch (MongoCommandException e) {
            this.swallowOrThrow(e);
         }

         return null;
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      Objects.requireNonNull(binding);
      AsyncOperationHelper.withAsyncSourceAndConnection(binding::getWriteConnectionSource, false, callback, (connectionSource, connection, cb) -> {
         AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.buildCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), (result, commandExecutionError) -> {
            try {
               this.swallowOrThrow(commandExecutionError);
               callback.onResult(result, (Throwable)null);
            } catch (Throwable e) {
               callback.onResult(null, e);
            }

         });
      });
   }

   <E extends Throwable> void swallowOrThrow(@Nullable E mongoExecutionException) throws E {
      if (mongoExecutionException != null) {
         throw mongoExecutionException;
      }
   }

   abstract BsonDocument buildCommand();

   MongoNamespace getNamespace() {
      return this.namespace;
   }

   WriteConcern getWriteConcern() {
      return this.writeConcern;
   }
}
