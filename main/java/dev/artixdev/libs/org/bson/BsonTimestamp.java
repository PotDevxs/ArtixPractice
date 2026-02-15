package dev.artixdev.libs.org.bson;

public final class BsonTimestamp extends BsonValue implements Comparable<BsonTimestamp> {
   private final long value;

   public BsonTimestamp() {
      this.value = 0L;
   }

   public BsonTimestamp(long value) {
      this.value = value;
   }

   public BsonTimestamp(int seconds, int increment) {
      this.value = (long)seconds << 32 | (long)increment & 4294967295L;
   }

   public BsonType getBsonType() {
      return BsonType.TIMESTAMP;
   }

   public long getValue() {
      return this.value;
   }

   public int getTime() {
      return (int)(this.value >> 32);
   }

   public int getInc() {
      return (int)this.value;
   }

   public String toString() {
      return "Timestamp{value=" + this.getValue() + ", seconds=" + this.getTime() + ", inc=" + this.getInc() + '}';
   }

   public int compareTo(BsonTimestamp ts) {
      return Long.compareUnsigned(this.value, ts.value);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonTimestamp timestamp = (BsonTimestamp)o;
         return this.value == timestamp.value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)(this.value ^ this.value >>> 32);
   }
}
