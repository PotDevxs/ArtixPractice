package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;

public class DropDatabaseOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private final String databaseName;
   private final WriteConcern writeConcern;

   public DropDatabaseOperation(String databaseName) {
      this(databaseName, (WriteConcern)null);
   }

   public DropDatabaseOperation(String databaseName, @Nullable WriteConcern writeConcern) {
      this.databaseName = (String)Assertions.notNull("databaseName", databaseName);
      this.writeConcern = writeConcern;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public Void execute(WriteBinding binding) {
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         SyncOperationHelper.executeCommand(binding, this.databaseName, this.getCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer());
         return null;
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
         SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            AsyncOperationHelper.executeCommandAsync(binding, this.databaseName, this.getCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), AsyncOperationHelper.releasingCallback(errHandlingCallback, connection));
         }

      });
   }

   private BsonDocument getCommand() {
      BsonDocument commandDocument = new BsonDocument("dropDatabase", new BsonInt32(1));
      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
      return commandDocument;
   }
}
