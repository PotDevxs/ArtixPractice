package dev.artixdev.libs.org.bson;

public final class BsonUndefined extends BsonValue {
   public BsonType getBsonType() {
      return BsonType.UNDEFINED;
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
}
