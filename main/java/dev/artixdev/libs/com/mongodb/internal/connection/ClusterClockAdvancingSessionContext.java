package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

public final class ClusterClockAdvancingSessionContext implements SessionContext {
   private final SessionContext wrapped;
   private final ClusterClock clusterClock;

   public ClusterClockAdvancingSessionContext(SessionContext wrapped, ClusterClock clusterClock) {
      this.wrapped = wrapped;
      this.clusterClock = clusterClock;
   }

   public boolean hasSession() {
      return this.wrapped.hasSession();
   }

   public boolean isImplicitSession() {
      return this.wrapped.isImplicitSession();
   }

   public BsonDocument getSessionId() {
      return this.wrapped.getSessionId();
   }

   public boolean isCausallyConsistent() {
      return this.wrapped.isCausallyConsistent();
   }

   public long getTransactionNumber() {
      return this.wrapped.getTransactionNumber();
   }

   public long advanceTransactionNumber() {
      return this.wrapped.advanceTransactionNumber();
   }

   public boolean notifyMessageSent() {
      return this.wrapped.notifyMessageSent();
   }

   public BsonTimestamp getOperationTime() {
      return this.wrapped.getOperationTime();
   }

   public void advanceOperationTime(@Nullable BsonTimestamp operationTime) {
      this.wrapped.advanceOperationTime(operationTime);
   }

   public BsonDocument getClusterTime() {
      return this.clusterClock.greaterOf(this.wrapped.getClusterTime());
   }

   public void advanceClusterTime(@Nullable BsonDocument clusterTime) {
      this.wrapped.advanceClusterTime(clusterTime);
      this.clusterClock.advance(clusterTime);
   }

   public boolean isSnapshot() {
      return this.wrapped.isSnapshot();
   }

   public void setSnapshotTimestamp(@Nullable BsonTimestamp snapshotTimestamp) {
      this.wrapped.setSnapshotTimestamp(snapshotTimestamp);
   }

   @Nullable
   public BsonTimestamp getSnapshotTimestamp() {
      return this.wrapped.getSnapshotTimestamp();
   }

   public boolean hasActiveTransaction() {
      return this.wrapped.hasActiveTransaction();
   }

   public ReadConcern getReadConcern() {
      return this.wrapped.getReadConcern();
   }

   public void setRecoveryToken(BsonDocument recoveryToken) {
      this.wrapped.setRecoveryToken(recoveryToken);
   }

   public void clearTransactionContext() {
      this.wrapped.clearTransactionContext();
   }

   public void markSessionDirty() {
      this.wrapped.markSessionDirty();
   }

   public boolean isSessionMarkedDirty() {
      return this.wrapped.isSessionMarkedDirty();
   }
}
