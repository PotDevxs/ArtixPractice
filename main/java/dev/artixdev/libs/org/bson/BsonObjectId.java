package dev.artixdev.libs.org.bson;

import dev.artixdev.libs.org.bson.types.ObjectId;

public class BsonObjectId extends BsonValue implements Comparable<BsonObjectId> {
   private final ObjectId value;

   public BsonObjectId() {
      this(new ObjectId());
   }

   public BsonObjectId(ObjectId value) {
      if (value == null) {
         throw new IllegalArgumentException("value may not be null");
      } else {
         this.value = value;
      }
   }

   public ObjectId getValue() {
      return this.value;
   }

   public BsonType getBsonType() {
      return BsonType.OBJECT_ID;
   }

   public int compareTo(BsonObjectId o) {
      return this.value.compareTo(o.value);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonObjectId that = (BsonObjectId)o;
         return this.value.equals(that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public String toString() {
      return "BsonObjectId{value=" + this.value.toHexString() + '}';
   }
}
