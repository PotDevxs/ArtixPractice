package dev.artixdev.libs.com.mongodb.bulk;

import dev.artixdev.libs.org.bson.BsonValue;

public class BulkWriteUpsert {
   private final int index;
   private final BsonValue id;

   public BulkWriteUpsert(int index, BsonValue id) {
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
         BulkWriteUpsert that = (BulkWriteUpsert)o;
         if (this.index != that.index) {
            return false;
         } else {
            return this.id.equals(that.id);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.index;
      result = 31 * result + this.id.hashCode();
      return result;
   }

   public String toString() {
      return "BulkWriteUpsert{index=" + this.index + ", id=" + this.id + '}';
   }
}
