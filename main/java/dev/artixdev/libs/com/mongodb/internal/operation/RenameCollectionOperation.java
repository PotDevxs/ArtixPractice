package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.AsyncConnection;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

public class RenameCollectionOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private final MongoNamespace originalNamespace;
   private final MongoNamespace newNamespace;
   private final WriteConcern writeConcern;
   private boolean dropTarget;

   public RenameCollectionOperation(MongoNamespace originalNamespace, MongoNamespace newNamespace) {
      this(originalNamespace, newNamespace, (WriteConcern)null);
   }

   public RenameCollectionOperation(MongoNamespace originalNamespace, MongoNamespace newNamespace, @Nullable WriteConcern writeConcern) {
      this.originalNamespace = (MongoNamespace)Assertions.notNull("originalNamespace", originalNamespace);
      this.newNamespace = (MongoNamespace)Assertions.notNull("newNamespace", newNamespace);
      this.writeConcern = writeConcern;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public boolean isDropTarget() {
      return this.dropTarget;
   }

   public RenameCollectionOperation dropTarget(boolean dropTarget) {
      this.dropTarget = dropTarget;
      return this;
   }

   public Void execute(WriteBinding binding) {
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         return (Void)SyncOperationHelper.executeCommand(binding, "admin", this.getCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer());
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
         SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            AsyncOperationHelper.executeCommandAsync(binding, "admin", this.getCommand(), (AsyncConnection)Assertions.assertNotNull(connection), AsyncOperationHelper.writeConcernErrorTransformerAsync(), AsyncOperationHelper.releasingCallback(errHandlingCallback, connection));
         }

      });
   }

   private BsonDocument getCommand() {
      BsonDocument commandDocument = (new BsonDocument("renameCollection", new BsonString(this.originalNamespace.getFullName()))).append("to", new BsonString(this.newNamespace.getFullName())).append("dropTarget", BsonBoolean.valueOf(this.dropTarget));
      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
      return commandDocument;
   }
}
