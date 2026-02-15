package dev.artixdev.libs.org.bson;

public final class BsonMaxKey extends BsonValue {
   public BsonType getBsonType() {
      return BsonType.MAX_KEY;
   }

   public boolean equals(Object o) {
      return o instanceof BsonMaxKey;
   }

   public int hashCode() {
      return 0;
   }

   public String toString() {
      return "BsonMaxKey";
   }
}
