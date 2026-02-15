package dev.artixdev.libs.org.bson;

import java.util.Arrays;
import dev.artixdev.libs.org.bson.assertions.Assertions;

public final class BsonRegularExpression extends BsonValue {
   private final String pattern;
   private final String options;

   public BsonRegularExpression(String pattern, String options) {
      this.pattern = (String)Assertions.notNull("pattern", pattern);
      this.options = options == null ? "" : this.sortOptionCharacters(options);
   }

   public BsonRegularExpression(String pattern) {
      this(pattern, (String)null);
   }

   public BsonType getBsonType() {
      return BsonType.REGULAR_EXPRESSION;
   }

   public String getPattern() {
      return this.pattern;
   }

   public String getOptions() {
      return this.options;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonRegularExpression that = (BsonRegularExpression)o;
         if (!this.options.equals(that.options)) {
            return false;
         } else {
            return this.pattern.equals(that.pattern);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.pattern.hashCode();
      result = 31 * result + this.options.hashCode();
      return result;
   }

   public String toString() {
      return "BsonRegularExpression{pattern='" + this.pattern + '\'' + ", options='" + this.options + '\'' + '}';
   }

   private String sortOptionCharacters(String options) {
      char[] chars = options.toCharArray();
      Arrays.sort(chars);
      return new String(chars);
   }
}
