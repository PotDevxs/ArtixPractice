package dev.artixdev.libs.com.mongodb.bulk;

import java.util.Objects;
import dev.artixdev.libs.org.bson.BsonValue;

public class BulkWriteInsert {
   private final int index;
   private final BsonValue id;

   public BulkWriteInsert(int index, BsonValue id) {
      this.index = index;
      this.id = id;
   }

   public int getIndex() {
      return this.index;
   }

   public BsonValue getId() {
      return this.id;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BulkWriteInsert that = (BulkWriteInsert)o;
         return this.index == that.index && Objects.equals(this.id, that.id);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.index, this.id});
   }

   public String toString() {
      return "BulkWriteInsert{index=" + this.index + ", id=" + this.id + '}';
   }
}
