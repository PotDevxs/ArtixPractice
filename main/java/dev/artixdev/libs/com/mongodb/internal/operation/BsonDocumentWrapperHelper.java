package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;

final class BsonDocumentWrapperHelper {
   static <T> List<T> toList(BsonDocument result, String fieldContainingWrappedArray) {
      return ((BsonArrayWrapper)result.getArray(fieldContainingWrappedArray)).getWrappedArray();
   }

   static <T> T toDocument(BsonDocument document) {
      return document == null ? null : (T) ((BsonDocumentWrapper)document).getWrappedDocument();
   }

   private BsonDocumentWrapperHelper() {
   }
}
