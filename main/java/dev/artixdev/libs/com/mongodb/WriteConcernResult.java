package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;

public abstract class WriteConcernResult {
   public abstract boolean wasAcknowledged();

   public abstract int getCount();

   public abstract boolean isUpdateOfExisting();

   @Nullable
   public abstract BsonValue getUpsertedId();

   public static WriteConcernResult acknowledged(final int count, final boolean isUpdateOfExisting, @Nullable final BsonValue upsertedId) {
      return new WriteConcernResult() {
         public boolean wasAcknowledged() {
            return true;
         }

         public int getCount() {
            return count;
         }

         public boolean isUpdateOfExisting() {
            return isUpdateOfExisting;
         }

         @Nullable
         public BsonValue getUpsertedId() {
            return upsertedId;
         }

         public boolean equals(Object o) {
            if (this == o) {
               return true;
            } else if (o != null && this.getClass() == o.getClass()) {
               WriteConcernResult that = (WriteConcernResult)o;
               if (!that.wasAcknowledged()) {
                  return false;
               } else if (count != that.getCount()) {
                  return false;
               } else if (isUpdateOfExisting != that.isUpdateOfExisting()) {
                  return false;
               } else {
                  if (upsertedId != null) {
                     if (!upsertedId.equals(that.getUpsertedId())) {
                        return false;
                     }
                  } else if (that.getUpsertedId() != null) {
                     return false;
                  }

                  return true;
               }
            } else {
               return false;
            }
         }

         public int hashCode() {
            int result = count;
            result = 31 * result + (isUpdateOfExisting ? 1 : 0);
            result = 31 * result + (upsertedId != null ? upsertedId.hashCode() : 0);
            return result;
         }

         public String toString() {
            return "AcknowledgedWriteResult{count=" + count + ", isUpdateOfExisting=" + isUpdateOfExisting + ", upsertedId=" + upsertedId + '}';
         }
      };
   }

   public static WriteConcernResult unacknowledged() {
      return new WriteConcernResult() {
         public boolean wasAcknowledged() {
            return false;
         }

         public int getCount() {
            throw this.getUnacknowledgedWriteException();
         }

         public boolean isUpdateOfExisting() {
            throw this.getUnacknowledgedWriteException();
         }

         public BsonValue getUpsertedId() {
            throw this.getUnacknowledgedWriteException();
         }

         public boolean equals(Object o) {
            if (this == o) {
               return true;
            } else if (o != null && this.getClass() == o.getClass()) {
               WriteConcernResult that = (WriteConcernResult)o;
               return !that.wasAcknowledged();
            } else {
               return false;
            }
         }

         public int hashCode() {
            return 1;
         }

         public String toString() {
            return "UnacknowledgedWriteResult{}";
         }

         private UnsupportedOperationException getUnacknowledgedWriteException() {
            return new UnsupportedOperationException("Cannot get information about an unacknowledged write");
         }
      };
   }
}
