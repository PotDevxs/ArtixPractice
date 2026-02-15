package dev.artixdev.libs.com.mongodb.client.result;

public abstract class DeleteResult {
   public abstract boolean wasAcknowledged();

   public abstract long getDeletedCount();

   public static DeleteResult acknowledged(long deletedCount) {
      return new DeleteResult.AcknowledgedDeleteResult(deletedCount);
   }

   public static DeleteResult unacknowledged() {
      return new DeleteResult.UnacknowledgedDeleteResult();
   }

   private static class AcknowledgedDeleteResult extends DeleteResult {
      private final long deletedCount;

      AcknowledgedDeleteResult(long deletedCount) {
         this.deletedCount = deletedCount;
      }

      public boolean wasAcknowledged() {
         return true;
      }

      public long getDeletedCount() {
         return this.deletedCount;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            DeleteResult.AcknowledgedDeleteResult that = (DeleteResult.AcknowledgedDeleteResult)o;
            return this.deletedCount == that.deletedCount;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return (int)(this.deletedCount ^ this.deletedCount >>> 32);
      }

      public String toString() {
         return "AcknowledgedDeleteResult{deletedCount=" + this.deletedCount + '}';
      }
   }

   private static class UnacknowledgedDeleteResult extends DeleteResult {
      private UnacknowledgedDeleteResult() {
      }

      public boolean wasAcknowledged() {
         return false;
      }

      public long getDeletedCount() {
         throw new UnsupportedOperationException("Cannot get information about an unacknowledged delete");
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
         return "UnacknowledgedDeleteResult{}";
      }

      // $FF: synthetic method
      UnacknowledgedDeleteResult(Object x0) {
         this();
      }
   }
}
