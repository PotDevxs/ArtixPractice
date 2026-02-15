package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonDocument;

public class WriteError {
   private final int code;
   private final String message;
   private final BsonDocument details;

   public WriteError(int code, String message, BsonDocument details) {
      this.code = code;
      this.message = (String)Assertions.notNull("message", message);
      this.details = (BsonDocument)Assertions.notNull("details", details);
   }

   public WriteError(WriteError writeError) {
      this.code = writeError.code;
      this.message = writeError.message;
      this.details = writeError.details;
   }

   public ErrorCategory getCategory() {
      return ErrorCategory.fromErrorCode(this.code);
   }

   public int getCode() {
      return this.code;
   }

   public String getMessage() {
      return this.message;
   }

   public BsonDocument getDetails() {
      return this.details;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         WriteError that = (WriteError)o;
         if (this.code != that.code) {
            return false;
         } else if (!this.details.equals(that.details)) {
            return false;
         } else {
            return this.message.equals(that.message);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.code;
      result = 31 * result + this.message.hashCode();
      result = 31 * result + this.details.hashCode();
      return result;
   }

   public String toString() {
      return "WriteError{code=" + this.code + ", message='" + this.message + '\'' + ", details=" + this.details + '}';
   }
}
