package dev.artixdev.libs.com.mongodb.bulk;

import dev.artixdev.libs.com.mongodb.WriteError;
import dev.artixdev.libs.org.bson.BsonDocument;

public class BulkWriteError extends WriteError {
   private final int index;

   public BulkWriteError(int code, String message, BsonDocument details, int index) {
      super(code, message, details);
      this.index = index;
   }

   public int getIndex() {
      return this.index;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BulkWriteError that = (BulkWriteError)o;
         return this.index != that.index ? false : super.equals(that);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.index;
      return result;
   }

   public String toString() {
      return "BulkWriteError{index=" + this.index + ", code=" + this.getCode() + ", message='" + this.getMessage() + '\'' + ", details=" + this.getDetails() + '}';
   }
}
