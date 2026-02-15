package dev.artixdev.libs.com.mongodb.connection;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.types.ObjectId;

public final class ClusterId {
   private final String value;
   private final String description;

   public ClusterId() {
      this((String)null);
   }

   public ClusterId(@Nullable String description) {
      this.value = (new ObjectId()).toHexString();
      this.description = description;
   }

   ClusterId(String value, String description) {
      this.value = (String)Assertions.notNull("value", value);
      this.description = description;
   }

   public String getValue() {
      return this.value;
   }

   @Nullable
   public String getDescription() {
      return this.description;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ClusterId clusterId = (ClusterId)o;
         if (!this.value.equals(clusterId.value)) {
            return false;
         } else {
            return Objects.equals(this.description, clusterId.description);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.value.hashCode();
      result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "ClusterId{value='" + this.value + '\'' + ", description='" + this.description + '\'' + '}';
   }
}
