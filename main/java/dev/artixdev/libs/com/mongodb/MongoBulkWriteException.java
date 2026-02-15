package dev.artixdev.libs.com.mongodb;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteError;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
import dev.artixdev.libs.com.mongodb.bulk.WriteConcernError;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class MongoBulkWriteException extends MongoServerException {
   private static final long serialVersionUID = -4345399805987210275L;
   private final BulkWriteResult writeResult;
   private final List<BulkWriteError> errors;
   private final ServerAddress serverAddress;
   private final WriteConcernError writeConcernError;

   /** @deprecated */
   @Deprecated
   public MongoBulkWriteException(BulkWriteResult writeResult, List<BulkWriteError> writeErrors, @Nullable WriteConcernError writeConcernError, ServerAddress serverAddress) {
      this(writeResult, writeErrors, writeConcernError, serverAddress, Collections.emptySet());
   }

   public MongoBulkWriteException(BulkWriteResult writeResult, List<BulkWriteError> writeErrors, @Nullable WriteConcernError writeConcernError, ServerAddress serverAddress, Set<String> errorLabels) {
      super("Bulk write operation error on server " + serverAddress + ". " + (writeErrors.isEmpty() ? "" : "Write errors: " + writeErrors + ". ") + (writeConcernError == null ? "" : "Write concern error: " + writeConcernError + ". "), serverAddress);
      this.writeResult = writeResult;
      this.errors = writeErrors;
      this.writeConcernError = writeConcernError;
      this.serverAddress = serverAddress;
      this.addLabels(errorLabels);
      if (writeConcernError != null) {
         this.addLabels(writeConcernError.getErrorLabels());
      }

   }

   public BulkWriteResult getWriteResult() {
      return this.writeResult;
   }

   public List<BulkWriteError> getWriteErrors() {
      return this.errors;
   }

   @Nullable
   public WriteConcernError getWriteConcernError() {
      return this.writeConcernError;
   }

   public ServerAddress getServerAddress() {
      return this.serverAddress;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MongoBulkWriteException that = (MongoBulkWriteException)o;
         if (!this.errors.equals(that.errors)) {
            return false;
         } else if (!this.serverAddress.equals(that.serverAddress)) {
            return false;
         } else if (!Objects.equals(this.writeConcernError, that.writeConcernError)) {
            return false;
         } else {
            return this.writeResult.equals(that.writeResult);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.writeResult.hashCode();
      result = 31 * result + this.errors.hashCode();
      result = 31 * result + this.serverAddress.hashCode();
      result = 31 * result + (this.writeConcernError != null ? this.writeConcernError.hashCode() : 0);
      return result;
   }
}
