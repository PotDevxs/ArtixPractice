package dev.artixdev.libs.org.bson;

import java.math.BigDecimal;
import dev.artixdev.libs.org.bson.types.Decimal128;

public class BsonDouble extends BsonNumber implements Comparable<BsonDouble> {
   private final double value;

   public BsonDouble(double value) {
      this.value = value;
   }

   public int compareTo(BsonDouble o) {
      return Double.compare(this.value, o.value);
   }

   public BsonType getBsonType() {
      return BsonType.DOUBLE;
   }

   public double getValue() {
      return this.value;
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public Decimal128 decimal128Value() {
      if (Double.isNaN(this.value)) {
         return Decimal128.NaN;
      } else if (Double.isInfinite(this.value)) {
         return this.value > 0.0D ? Decimal128.POSITIVE_INFINITY : Decimal128.NEGATIVE_INFINITY;
      } else {
         return new Decimal128(new BigDecimal(this.value));
      }
   }

   public double doubleValue() {
      return this.value;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonDouble that = (BsonDouble)o;
         return Double.compare(that.value, this.value) == 0;
      } else {
         return false;
      }
   }

   public int hashCode() {
      long temp = Double.doubleToLongBits(this.value);
      return (int)(temp ^ temp >>> 32);
   }

   public String toString() {
      return "BsonDouble{value=" + this.value + '}';
   }
}
