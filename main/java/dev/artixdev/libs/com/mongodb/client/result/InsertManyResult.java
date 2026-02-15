package dev.artixdev.libs.com.mongodb.client.result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import dev.artixdev.libs.org.bson.BsonValue;

public abstract class InsertManyResult {
   public abstract boolean wasAcknowledged();

   public abstract Map<Integer, BsonValue> getInsertedIds();

   public static InsertManyResult acknowledged(Map<Integer, BsonValue> insertedIds) {
      return new InsertManyResult.AcknowledgedInsertManyResult(insertedIds);
   }

   public static InsertManyResult unacknowledged() {
      return new InsertManyResult.UnacknowledgedInsertManyResult();
   }

   private static class AcknowledgedInsertManyResult extends InsertManyResult {
      private final Map<Integer, BsonValue> insertedIds;

      AcknowledgedInsertManyResult(Map<Integer, BsonValue> insertedIds) {
         this.insertedIds = Collections.unmodifiableMap(new HashMap(insertedIds));
      }

      public boolean wasAcknowledged() {
         return true;
      }

      public Map<Integer, BsonValue> getInsertedIds() {
         return this.insertedIds;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            InsertManyResult.AcknowledgedInsertManyResult that = (InsertManyResult.AcknowledgedInsertManyResult)o;
            return Objects.equals(this.insertedIds, that.insertedIds);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.insertedIds});
      }

      public String toString() {
         return "AcknowledgedInsertManyResult{insertedIds=" + this.insertedIds + '}';
      }
   }

   private static class UnacknowledgedInsertManyResult extends InsertManyResult {
      private UnacknowledgedInsertManyResult() {
      }

      public boolean wasAcknowledged() {
         return false;
      }

      public Map<Integer, BsonValue> getInsertedIds() {
         throw new UnsupportedOperationException("Cannot get information about an unacknowledged insert many");
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
         return "UnacknowledgedInsertManyResult{}";
      }

      // $FF: synthetic method
      UnacknowledgedInsertManyResult(Object x0) {
         this();
      }
   }
}
