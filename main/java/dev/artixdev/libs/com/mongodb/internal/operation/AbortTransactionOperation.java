package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

public class AbortTransactionOperation extends TransactionOperation {
   private BsonDocument recoveryToken;

   public AbortTransactionOperation(WriteConcern writeConcern) {
      super(writeConcern);
   }

   public AbortTransactionOperation recoveryToken(@Nullable BsonDocument recoveryToken) {
      this.recoveryToken = recoveryToken;
      return this;
   }

   protected String getCommandName() {
      return "abortTransaction";
   }

   CommandOperationHelper.CommandCreator getCommandCreator() {
      CommandOperationHelper.CommandCreator creator = super.getCommandCreator();
      return this.recoveryToken != null ? (serverDescription, connectionDescription) -> {
         return creator.create(serverDescription, connectionDescription).append("recoveryToken", this.recoveryToken);
      } : creator;
   }

   protected Function<BsonDocument, BsonDocument> getRetryCommandModifier() {
      return (cmd) -> {
         return cmd;
      };
   }
}
