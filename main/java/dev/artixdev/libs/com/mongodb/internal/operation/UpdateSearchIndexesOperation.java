package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

final class UpdateSearchIndexesOperation extends AbstractWriteSearchIndexOperation {
   private static final String COMMAND_NAME = "updateSearchIndex";
   private final SearchIndexRequest request;

   UpdateSearchIndexesOperation(MongoNamespace namespace, SearchIndexRequest request, WriteConcern writeConcern) {
      super(namespace, writeConcern);
      this.request = request;
   }

   BsonDocument buildCommand() {
      BsonDocument command = (new BsonDocument("updateSearchIndex", new BsonString(this.getNamespace().getCollectionName()))).append("name", new BsonString(this.request.getIndexName())).append("definition", this.request.getDefinition());
      WriteConcernHelper.appendWriteConcernToCommand(this.getWriteConcern(), command);
      return command;
   }
}
