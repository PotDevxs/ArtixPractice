package dev.artixdev.libs.org.bson;

import dev.artixdev.libs.org.bson.types.Decimal128;

public final class BsonInt32 extends BsonNumber implements Comparable<BsonInt32> {
   private final int value;

   public BsonInt32(int value) {
      this.value = value;
   }

   public int compareTo(BsonInt32 o) {
      return this.value < o.value ? -1 : (this.value == o.value ? 0 : 1);
   }

   public BsonType getBsonType() {
      return BsonType.INT32;
   }

   public int getValue() {
      return this.value;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public Decimal128 decimal128Value() {
      return new Decimal128((long)this.value);
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonInt32 bsonInt32 = (BsonInt32)o;
         return this.value == bsonInt32.value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value;
   }

   public String toString() {
      return "BsonInt32{value=" + this.value + '}';
   }
}
