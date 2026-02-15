package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class CreateViewOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private final String databaseName;
   private final String viewName;
   private final String viewOn;
   private final List<BsonDocument> pipeline;
   private final WriteConcern writeConcern;
   private Collation collation;

   public CreateViewOperation(String databaseName, String viewName, String viewOn, List<BsonDocument> pipeline, WriteConcern writeConcern) {
      this.databaseName = (String)Assertions.notNull("databaseName", databaseName);
      this.viewName = (String)Assertions.notNull("viewName", viewName);
      this.viewOn = (String)Assertions.notNull("viewOn", viewOn);
      this.pipeline = (List)Assertions.notNull("pipeline", pipeline);
      this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
   }

   public String getDatabaseName() {
      return this.databaseName;
   }

   public String getViewName() {
      return this.viewName;
   }

   public String getViewOn() {
      return this.viewOn;
   }

   public List<BsonDocument> getPipeline() {
      return this.pipeline;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public CreateViewOperation collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public Void execute(WriteBinding binding) {
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         SyncOperationHelper.executeCommand(binding, this.databaseName, this.getCommand(), (Decoder)(new BsonDocumentCodec()), SyncOperationHelper.writeConcernErrorTransformer());
         return null;
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
         SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            SingleResultCallback<Void> wrappedCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);
            AsyncOperationHelper.executeCommandAsync(binding, this.databaseName, this.getCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), wrappedCallback);
         }

      });
   }

   private BsonDocument getCommand() {
      BsonDocument commandDocument = (new BsonDocument("create", new BsonString(this.viewName))).append("viewOn", new BsonString(this.viewOn)).append("pipeline", new BsonArray(this.pipeline));
      if (this.collation != null) {
         commandDocument.put((String)"collation", (BsonValue)this.collation.asDocument());
      }

      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
      return commandDocument;
   }
}
