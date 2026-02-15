package dev.artixdev.libs.com.mongodb.internal.session;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.session.ClientSession;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

public abstract class ClientSessionContext implements SessionContext {
   private final ClientSession clientSession;

   public ClientSessionContext(ClientSession clientSession) {
      this.clientSession = (ClientSession)Assertions.notNull("clientSession", clientSession);
   }

   public ClientSession getClientSession() {
      return this.clientSession;
   }

   public boolean hasSession() {
      return true;
   }

   public BsonDocument getSessionId() {
      return this.clientSession.getServerSession().getIdentifier();
   }

   public boolean isCausallyConsistent() {
      return this.clientSession.isCausallyConsistent();
   }

   public long getTransactionNumber() {
      return this.clientSession.getServerSession().getTransactionNumber();
   }

   public long advanceTransactionNumber() {
      return this.clientSession.getServerSession().advanceTransactionNumber();
   }

   public BsonTimestamp getOperationTime() {
      return this.clientSession.getOperationTime();
   }

   public void advanceOperationTime(@Nullable BsonTimestamp operationTime) {
      this.clientSession.advanceOperationTime(operationTime);
   }

   public BsonDocument getClusterTime() {
      return this.clientSession.getClusterTime();
   }

   public void advanceClusterTime(@Nullable BsonDocument clusterTime) {
      this.clientSession.advanceClusterTime(clusterTime);
   }

   public boolean isSnapshot() {
      Boolean snapshot = this.clientSession.getOptions().isSnapshot();
      return snapshot != null && snapshot;
   }

   public void setSnapshotTimestamp(@Nullable BsonTimestamp snapshotTimestamp) {
      this.clientSession.setSnapshotTimestamp(snapshotTimestamp);
   }

   @Nullable
   public BsonTimestamp getSnapshotTimestamp() {
      return this.clientSession.getSnapshotTimestamp();
   }

   public void setRecoveryToken(BsonDocument recoveryToken) {
      this.clientSession.setRecoveryToken(recoveryToken);
   }

   public void clearTransactionContext() {
      this.clientSession.clearTransactionContext();
   }

   public void markSessionDirty() {
      this.clientSession.getServerSession().markDirty();
   }

   public boolean isSessionMarkedDirty() {
      return this.clientSession.getServerSession().isMarkedDirty();
   }
}
