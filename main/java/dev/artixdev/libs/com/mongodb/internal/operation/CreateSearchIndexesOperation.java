package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

final class CreateSearchIndexesOperation extends AbstractWriteSearchIndexOperation {
   private static final String COMMAND_NAME = "createSearchIndexes";
   private final List<SearchIndexRequest> indexRequests;

   CreateSearchIndexesOperation(MongoNamespace namespace, List<SearchIndexRequest> indexRequests, WriteConcern writeConcern) {
      super(namespace, writeConcern);
      this.indexRequests = (List)Assertions.assertNotNull(indexRequests);
   }

   private static BsonArray convert(List<SearchIndexRequest> requests) {
      return (BsonArray)requests.stream().map(CreateSearchIndexesOperation::convert).collect(Collectors.toCollection(BsonArray::new));
   }

   private static BsonDocument convert(SearchIndexRequest request) {
      BsonDocument bsonIndexRequest = new BsonDocument();
      String searchIndexName = request.getIndexName();
      if (searchIndexName != null) {
         bsonIndexRequest.append("name", new BsonString(searchIndexName));
      }

      bsonIndexRequest.append("definition", request.getDefinition());
      return bsonIndexRequest;
   }

   BsonDocument buildCommand() {
      BsonDocument command = (new BsonDocument("createSearchIndexes", new BsonString(this.getNamespace().getCollectionName()))).append("indexes", convert(this.indexRequests));
      WriteConcernHelper.appendWriteConcernToCommand(this.getWriteConcern(), command);
      return command;
   }
}
