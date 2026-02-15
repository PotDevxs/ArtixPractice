package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

final class SearchIndexRequest {
   private final BsonDocument definition;
   @Nullable
   private final String indexName;

   SearchIndexRequest(BsonDocument definition, @Nullable String indexName) {
      Assertions.assertNotNull(definition);
      this.definition = definition;
      this.indexName = indexName;
   }

   public BsonDocument getDefinition() {
      return this.definition;
   }

   @Nullable
   public String getIndexName() {
      return this.indexName;
   }
}
