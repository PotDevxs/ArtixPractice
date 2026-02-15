package dev.artixdev.libs.org.bson;

public final class BsonNull extends BsonValue {
   public static final BsonNull VALUE = new BsonNull();

   public BsonType getBsonType() {
      return BsonType.NULL;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && this.getClass() == o.getClass();
      }
   }

   public int hashCode() {
      return 0;
   }

   public String toString() {
      return "BsonNull";
   }
}
