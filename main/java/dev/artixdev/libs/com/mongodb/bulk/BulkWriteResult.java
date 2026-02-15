package dev.artixdev.libs.com.mongodb.bulk;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequest;

public abstract class BulkWriteResult {
   public abstract boolean wasAcknowledged();

   public abstract int getInsertedCount();

   public abstract int getMatchedCount();

   public abstract int getDeletedCount();

   public abstract int getModifiedCount();

   public abstract List<BulkWriteInsert> getInserts();

   public abstract List<BulkWriteUpsert> getUpserts();

   /** @deprecated */
   @Deprecated
   public static BulkWriteResult acknowledged(WriteRequest.Type type, int count, List<BulkWriteUpsert> upserts) {
      return acknowledged(type, count, 0, upserts, Collections.emptyList());
   }

   /** @deprecated */
   @Deprecated
   public static BulkWriteResult acknowledged(WriteRequest.Type type, int count, Integer modifiedCount, List<BulkWriteUpsert> upserts) {
      return acknowledged(type, count, modifiedCount, upserts, Collections.emptyList());
   }

   public static BulkWriteResult acknowledged(WriteRequest.Type type, int count, Integer modifiedCount, List<BulkWriteUpsert> upserts, List<BulkWriteInsert> inserts) {
      return acknowledged(type == WriteRequest.Type.INSERT ? count : 0, type != WriteRequest.Type.UPDATE && type != WriteRequest.Type.REPLACE ? 0 : count, type == WriteRequest.Type.DELETE ? count : 0, modifiedCount, upserts, inserts);
   }

   /** @deprecated */
   @Deprecated
   public static BulkWriteResult acknowledged(int insertedCount, int matchedCount, int removedCount, Integer modifiedCount, List<BulkWriteUpsert> upserts) {
      return acknowledged(insertedCount, matchedCount, removedCount, modifiedCount, upserts, Collections.emptyList());
   }

   public static BulkWriteResult acknowledged(final int insertedCount, final int matchedCount, final int removedCount, final Integer modifiedCount, final List<BulkWriteUpsert> upserts, final List<BulkWriteInsert> inserts) {
      return new BulkWriteResult() {
         public boolean wasAcknowledged() {
            return true;
         }

         public int getInsertedCount() {
            return insertedCount;
         }

         public int getMatchedCount() {
            return matchedCount;
         }

         public int getDeletedCount() {
            return removedCount;
         }

         public int getModifiedCount() {
            return (Integer)Assertions.assertNotNull(modifiedCount);
         }

         public List<BulkWriteInsert> getInserts() {
            return Collections.unmodifiableList(inserts);
         }

         public List<BulkWriteUpsert> getUpserts() {
            return Collections.unmodifiableList(upserts);
         }

         public boolean equals(Object o) {
            if (this == o) {
               return true;
            } else if (o != null && this.getClass() == o.getClass()) {
               BulkWriteResult that = (BulkWriteResult)o;
               if (!that.wasAcknowledged()) {
                  return false;
               } else if (insertedCount != that.getInsertedCount()) {
                  return false;
               } else if (!modifiedCount.equals(that.getModifiedCount())) {
                  return false;
               } else if (removedCount != that.getDeletedCount()) {
                  return false;
               } else if (matchedCount != that.getMatchedCount()) {
                  return false;
               } else if (!upserts.equals(that.getUpserts())) {
                  return false;
               } else {
                  return inserts.equals(that.getInserts());
               }
            } else {
               return false;
            }
         }

         public int hashCode() {
            int result = upserts.hashCode();
            result = 31 * result + inserts.hashCode();
            result = 31 * result + insertedCount;
            result = 31 * result + matchedCount;
            result = 31 * result + removedCount;
            result = 31 * result + modifiedCount.hashCode();
            return result;
         }

         public String toString() {
            return "AcknowledgedBulkWriteResult{insertedCount=" + insertedCount + ", matchedCount=" + matchedCount + ", removedCount=" + removedCount + ", modifiedCount=" + modifiedCount + ", upserts=" + upserts + ", inserts=" + inserts + '}';
         }
      };
   }

   public static BulkWriteResult unacknowledged() {
      return new BulkWriteResult() {
         public boolean wasAcknowledged() {
            return false;
         }

         public int getInsertedCount() {
            throw this.getUnacknowledgedWriteException();
         }

         public int getMatchedCount() {
            throw this.getUnacknowledgedWriteException();
         }

         public int getDeletedCount() {
            throw this.getUnacknowledgedWriteException();
         }

         public int getModifiedCount() {
            throw this.getUnacknowledgedWriteException();
         }

         public List<BulkWriteInsert> getInserts() {
            throw this.getUnacknowledgedWriteException();
         }

         public List<BulkWriteUpsert> getUpserts() {
            throw this.getUnacknowledgedWriteException();
         }

         public boolean equals(Object o) {
            if (this == o) {
               return true;
            } else if (o != null && this.getClass() == o.getClass()) {
               BulkWriteResult that = (BulkWriteResult)o;
               return !that.wasAcknowledged();
            } else {
               return false;
            }
         }

         public int hashCode() {
            return 0;
         }

         public String toString() {
            return "UnacknowledgedBulkWriteResult{}";
         }

         private UnsupportedOperationException getUnacknowledgedWriteException() {
            return new UnsupportedOperationException("Cannot get information about an unacknowledged write");
         }
      };
   }
}
