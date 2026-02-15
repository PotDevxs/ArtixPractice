package dev.artixdev.libs.org.bson;

import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.types.Decimal128;

public final class BsonDecimal128 extends BsonNumber {
   private final Decimal128 value;

   public BsonDecimal128(Decimal128 value) {
      Assertions.notNull("value", value);
      this.value = value;
   }

   public BsonType getBsonType() {
      return BsonType.DECIMAL128;
   }

   public Decimal128 getValue() {
      return this.value;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonDecimal128 that = (BsonDecimal128)o;
         return this.value.equals(that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public String toString() {
      return "BsonDecimal128{value=" + this.value + '}';
   }

   public int intValue() {
      return this.value.bigDecimalValue().intValue();
   }

   public long longValue() {
      return this.value.bigDecimalValue().longValue();
   }

   public double doubleValue() {
      return this.value.bigDecimalValue().doubleValue();
   }

   public Decimal128 decimal128Value() {
      return this.value;
   }
}
