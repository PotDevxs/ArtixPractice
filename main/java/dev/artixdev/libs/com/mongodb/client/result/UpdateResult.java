package dev.artixdev.libs.com.mongodb.client.result;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;

public abstract class UpdateResult {
   public abstract boolean wasAcknowledged();

   public abstract long getMatchedCount();

   public abstract long getModifiedCount();

   @Nullable
   public abstract BsonValue getUpsertedId();

   public static UpdateResult acknowledged(long matchedCount, @Nullable Long modifiedCount, @Nullable BsonValue upsertedId) {
      return new UpdateResult.AcknowledgedUpdateResult(matchedCount, modifiedCount, upsertedId);
   }

   public static UpdateResult unacknowledged() {
      return new UpdateResult.UnacknowledgedUpdateResult();
   }

   private static class AcknowledgedUpdateResult extends UpdateResult {
      private final long matchedCount;
      private final Long modifiedCount;
      private final BsonValue upsertedId;

      AcknowledgedUpdateResult(long matchedCount, Long modifiedCount, @Nullable BsonValue upsertedId) {
         this.matchedCount = matchedCount;
         this.modifiedCount = modifiedCount;
         this.upsertedId = upsertedId;
      }

      public boolean wasAcknowledged() {
         return true;
      }

      public long getMatchedCount() {
         return this.matchedCount;
      }

      public long getModifiedCount() {
         return this.modifiedCount;
      }

      @Nullable
      public BsonValue getUpsertedId() {
         return this.upsertedId;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            UpdateResult.AcknowledgedUpdateResult that = (UpdateResult.AcknowledgedUpdateResult)o;
            if (this.matchedCount != that.matchedCount) {
               return false;
            } else if (!Objects.equals(this.modifiedCount, that.modifiedCount)) {
               return false;
            } else {
               return Objects.equals(this.upsertedId, that.upsertedId);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = (int)(this.matchedCount ^ this.matchedCount >>> 32);
         result = 31 * result + (this.modifiedCount != null ? this.modifiedCount.hashCode() : 0);
         result = 31 * result + (this.upsertedId != null ? this.upsertedId.hashCode() : 0);
         return result;
      }

      public String toString() {
         return "AcknowledgedUpdateResult{matchedCount=" + this.matchedCount + ", modifiedCount=" + this.modifiedCount + ", upsertedId=" + this.upsertedId + '}';
      }
   }

   private static class UnacknowledgedUpdateResult extends UpdateResult {
      private UnacknowledgedUpdateResult() {
      }

      public boolean wasAcknowledged() {
         return false;
      }

      public long getMatchedCount() {
         throw this.getUnacknowledgedWriteException();
      }

      public long getModifiedCount() {
         throw this.getUnacknowledgedWriteException();
      }

      @Nullable
      public BsonValue getUpsertedId() {
         throw this.getUnacknowledgedWriteException();
      }

      private UnsupportedOperationException getUnacknowledgedWriteException() {
         return new UnsupportedOperationException("Cannot get information about an unacknowledged update");
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
         return "UnacknowledgedUpdateResult{}";
      }

      // $FF: synthetic method
      UnacknowledgedUpdateResult(Object x0) {
         this();
      }
   }
}
