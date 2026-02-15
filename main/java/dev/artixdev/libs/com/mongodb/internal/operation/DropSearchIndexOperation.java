package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

final class DropSearchIndexOperation extends AbstractWriteSearchIndexOperation {
   private static final String COMMAND_NAME = "dropSearchIndex";
   private final String indexName;

   DropSearchIndexOperation(MongoNamespace namespace, String indexName, WriteConcern writeConcern) {
      super(namespace, writeConcern);
      this.indexName = indexName;
   }

   <E extends Throwable> void swallowOrThrow(@Nullable E mongoExecutionException) throws E {
      if (mongoExecutionException != null && !CommandOperationHelper.isNamespaceError(mongoExecutionException)) {
         throw mongoExecutionException;
      }
   }

   BsonDocument buildCommand() {
      BsonDocument command = (new BsonDocument("dropSearchIndex", new BsonString(this.getNamespace().getCollectionName()))).append("name", new BsonString(this.indexName));
      WriteConcernHelper.appendWriteConcernToCommand(this.getWriteConcern(), command);
      return command;
   }
}
