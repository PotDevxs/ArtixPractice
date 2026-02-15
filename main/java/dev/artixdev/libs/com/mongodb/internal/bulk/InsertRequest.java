package dev.artixdev.libs.com.mongodb.internal.bulk;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class InsertRequest extends WriteRequest {
   private final BsonDocument document;

   public InsertRequest(BsonDocument document) {
      this.document = (BsonDocument)Assertions.notNull("document", document);
   }

   public BsonDocument getDocument() {
      return this.document;
   }

   public WriteRequest.Type getType() {
      return WriteRequest.Type.INSERT;
   }
}
