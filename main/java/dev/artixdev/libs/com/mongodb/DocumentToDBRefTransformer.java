package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.Transformer;

public final class DocumentToDBRefTransformer implements Transformer {
   public Object transform(Object value) {
      if (value instanceof Document) {
         Document document = (Document)value;
         if (document.containsKey("$id") && document.containsKey("$ref")) {
            return new DBRef((String)document.get("$db"), (String)document.get("$ref"), document.get("$id"));
         }
      }

      return value;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && this.getClass() == o.getClass();
      }
   }

   public int hashCode() {
      return 0;
   }
}
