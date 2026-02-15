package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

public class NoOpSessionContext implements SessionContext {
   public static final NoOpSessionContext INSTANCE = new NoOpSessionContext();

   public boolean hasSession() {
      return false;
   }

   public boolean isImplicitSession() {
      throw new UnsupportedOperationException();
   }

   public BsonDocument getSessionId() {
      throw new UnsupportedOperationException();
   }

   public boolean isCausallyConsistent() {
      return false;
   }

   public long getTransactionNumber() {
      throw new UnsupportedOperationException();
   }

   public long advanceTransactionNumber() {
      throw new UnsupportedOperationException();
   }

   public boolean notifyMessageSent() {
      return false;
   }

   @Nullable
   public BsonTimestamp getOperationTime() {
      return null;
   }

   public void advanceOperationTime(@Nullable BsonTimestamp operationTime) {
   }

   @Nullable
   public BsonDocument getClusterTime() {
      return null;
   }

   public void advanceClusterTime(@Nullable BsonDocument clusterTime) {
   }

   public boolean isSnapshot() {
      return false;
   }

   public void setSnapshotTimestamp(@Nullable BsonTimestamp snapshotTimestamp) {
   }

   @Nullable
   public BsonTimestamp getSnapshotTimestamp() {
      return null;
   }

   public boolean hasActiveTransaction() {
      return false;
   }

   public ReadConcern getReadConcern() {
      return ReadConcern.DEFAULT;
   }

   public void setRecoveryToken(BsonDocument recoveryToken) {
      throw new UnsupportedOperationException();
   }

   public void clearTransactionContext() {
      throw new UnsupportedOperationException();
   }

   public void markSessionDirty() {
      throw new UnsupportedOperationException();
   }

   public boolean isSessionMarkedDirty() {
      return false;
   }
}
