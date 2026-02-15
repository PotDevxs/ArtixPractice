package dev.artixdev.libs.com.mongodb.client.model.changestream;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonCreator;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonProperty;

public final class TruncatedArray {
   private final String field;
   private final int newSize;

   @BsonCreator
   public TruncatedArray(@BsonProperty("field") String field, @BsonProperty("newSize") int newSize) {
      this.field = (String)Assertions.notNull("field", field);
      Assertions.isTrueArgument("newSize >= 0", newSize >= 0);
      this.newSize = newSize;
   }

   public String getField() {
      return this.field;
   }

   public int getNewSize() {
      return this.newSize;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TruncatedArray that = (TruncatedArray)o;
         return this.newSize == that.newSize && this.field.equals(that.field);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.field, this.newSize});
   }

   public String toString() {
      return "TruncatedArray{field=" + this.field + ", newSize=" + this.newSize + '}';
   }
}
