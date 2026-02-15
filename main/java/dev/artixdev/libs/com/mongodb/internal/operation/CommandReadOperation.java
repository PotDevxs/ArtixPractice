package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class CommandReadOperation<T> implements AsyncReadOperation<T>, ReadOperation<T> {
   private final String databaseName;
   private final BsonDocument command;
   private final Decoder<T> decoder;

   public CommandReadOperation(String databaseName, BsonDocument command, Decoder<T> decoder) {
      this.databaseName = (String)Assertions.notNull("databaseName", databaseName);
      this.command = (BsonDocument)Assertions.notNull("command", command);
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
   }

   public T execute(ReadBinding binding) {
      return SyncOperationHelper.executeRetryableRead(binding, this.databaseName, this.getCommandCreator(), this.decoder, (result, source, connection) -> {
         return result;
      }, false);
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<T> callback) {
      AsyncOperationHelper.executeRetryableReadAsync(binding, this.databaseName, this.getCommandCreator(), this.decoder, (result, source, connection) -> {
         return result;
      }, false, callback);
   }

   private CommandOperationHelper.CommandCreator getCommandCreator() {
      return (serverDescription, connectionDescription) -> {
         return this.command;
      };
   }
}
