package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;

public abstract class TransactionOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private final WriteConcern writeConcern;

   TransactionOperation(WriteConcern writeConcern) {
      this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public Void execute(WriteBinding binding) {
      Assertions.isTrue("in transaction", binding.getSessionContext().hasActiveTransaction());
      return (Void)SyncOperationHelper.executeRetryableWrite(binding, "admin", (ReadPreference)null, new NoOpFieldNameValidator(), new BsonDocumentCodec(), this.getCommandCreator(), SyncOperationHelper.writeConcernErrorTransformer(), this.getRetryCommandModifier());
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      Assertions.isTrue("in transaction", binding.getSessionContext().hasActiveTransaction());
      AsyncOperationHelper.executeRetryableWriteAsync(binding, "admin", (ReadPreference)null, new NoOpFieldNameValidator(), new BsonDocumentCodec(), this.getCommandCreator(), AsyncOperationHelper.writeConcernErrorTransformerAsync(), this.getRetryCommandModifier(), ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
   }

   CommandOperationHelper.CommandCreator getCommandCreator() {
      return (serverDescription, connectionDescription) -> {
         BsonDocument command = new BsonDocument(this.getCommandName(), new BsonInt32(1));
         if (!this.writeConcern.isServerDefault()) {
            command.put((String)"writeConcern", (BsonValue)this.writeConcern.asDocument());
         }

         return command;
      };
   }

   protected abstract String getCommandName();

   protected abstract Function<BsonDocument, BsonDocument> getRetryCommandModifier();
}
