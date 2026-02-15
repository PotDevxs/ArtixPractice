package dev.artixdev.libs.org.bson;

public class BsonElement {
   private final String name;
   private final BsonValue value;

   public BsonElement(String name, BsonValue value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public BsonValue getValue() {
      return this.value;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonElement that = (BsonElement)o;
         if (this.getName() != null) {
            if (!this.getName().equals(that.getName())) {
               return false;
            }
         } else if (that.getName() != null) {
            return false;
         }

         if (this.getValue() != null) {
            if (this.getValue().equals(that.getValue())) {
               return true;
            }
         } else if (that.getValue() == null) {
            return true;
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.getName() != null ? this.getName().hashCode() : 0;
      result = 31 * result + (this.getValue() != null ? this.getValue().hashCode() : 0);
      return result;
   }
}
