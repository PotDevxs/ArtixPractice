package dev.artixdev.libs.com.mongodb.bulk;

import java.util.Collections;
import java.util.Set;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.org.bson.BsonDocument;

public class WriteConcernError {
   private final int code;
   private final String codeName;
   private final String message;
   private final BsonDocument details;
   private final Set<String> errorLabels;

   public WriteConcernError(int code, String codeName, String message, BsonDocument details) {
      this(code, codeName, message, details, Collections.emptySet());
   }

   /** @deprecated */
   @Deprecated
   public WriteConcernError(int code, String codeName, String message, BsonDocument details, Set<String> errorLabels) {
      this.code = code;
      this.codeName = (String)Assertions.notNull("codeName", codeName);
      this.message = (String)Assertions.notNull("message", message);
      this.details = (BsonDocument)Assertions.notNull("details", details);
      this.errorLabels = (Set)Assertions.notNull("errorLabels", errorLabels);
   }

   public int getCode() {
      return this.code;
   }

   public String getCodeName() {
      return this.codeName;
   }

   public String getMessage() {
      return this.message;
   }

   public BsonDocument getDetails() {
      return this.details;
   }

   /** @deprecated */
   @Deprecated
   public void addLabel(String errorLabel) {
      Assertions.notNull("errorLabel", errorLabel);
      this.errorLabels.add(errorLabel);
   }

   /** @deprecated */
   @NonNull
   @Deprecated
   public Set<String> getErrorLabels() {
      return Collections.unmodifiableSet(this.errorLabels);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         WriteConcernError that = (WriteConcernError)o;
         if (this.code != that.code) {
            return false;
         } else if (!this.codeName.equals(that.codeName)) {
            return false;
         } else if (!this.details.equals(that.details)) {
            return false;
         } else if (!this.message.equals(that.message)) {
            return false;
         } else {
            return this.errorLabels.equals(that.errorLabels);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.code;
      result = 31 * result + this.codeName.hashCode();
      result = 31 * result + this.message.hashCode();
      result = 31 * result + this.details.hashCode();
      result = 31 * result + this.errorLabels.hashCode();
      return result;
   }

   public String toString() {
      return "WriteConcernError{code=" + this.code + ", codeName='" + this.codeName + '\'' + ", message='" + this.message + '\'' + ", details=" + this.details + ", errorLabels=" + this.errorLabels + '}';
   }
}
