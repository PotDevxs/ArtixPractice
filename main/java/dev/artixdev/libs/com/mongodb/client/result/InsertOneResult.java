package dev.artixdev.libs.com.mongodb.client.result;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;

public abstract class InsertOneResult {
   public abstract boolean wasAcknowledged();

   @Nullable
   public abstract BsonValue getInsertedId();

   public static InsertOneResult acknowledged(@Nullable BsonValue insertId) {
      return new InsertOneResult.AcknowledgedInsertOneResult(insertId);
   }

   public static InsertOneResult unacknowledged() {
      return new InsertOneResult.UnacknowledgedInsertOneResult();
   }

   private static class AcknowledgedInsertOneResult extends InsertOneResult {
      private final BsonValue insertedId;

      AcknowledgedInsertOneResult(@Nullable BsonValue insertId) {
         this.insertedId = insertId;
      }

      public boolean wasAcknowledged() {
         return true;
      }

      @Nullable
      public BsonValue getInsertedId() {
         return this.insertedId;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            InsertOneResult.AcknowledgedInsertOneResult that = (InsertOneResult.AcknowledgedInsertOneResult)o;
            return Objects.equals(this.insertedId, that.insertedId);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.insertedId});
      }

      public String toString() {
         return "AcknowledgedInsertOneResult{insertedId=" + this.insertedId + '}';
      }
   }

   private static class UnacknowledgedInsertOneResult extends InsertOneResult {
      private UnacknowledgedInsertOneResult() {
      }

      public boolean wasAcknowledged() {
         return false;
      }

      @Nullable
      public BsonValue getInsertedId() {
         throw new UnsupportedOperationException("Cannot get information about an unacknowledged insert");
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
         return "UnacknowledgedInsertOneResult{}";
      }

      // $FF: synthetic method
      UnacknowledgedInsertOneResult(Object x0) {
         this();
      }
   }
}
