package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class Tag {
   private final String name;
   private final String value;

   public Tag(String name, String value) {
      this.name = (String)Assertions.notNull("name", name);
      this.value = (String)Assertions.notNull("value", value);
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Tag that = (Tag)o;
         if (!this.name.equals(that.name)) {
            return false;
         } else {
            return this.value.equals(that.value);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.name.hashCode();
      result = 31 * result + this.value.hashCode();
      return result;
   }

   public String toString() {
      return "Tag{name='" + this.name + '\'' + ", value='" + this.value + '\'' + '}';
   }
}
