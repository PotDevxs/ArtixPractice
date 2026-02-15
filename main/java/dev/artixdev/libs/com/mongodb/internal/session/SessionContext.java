package dev.artixdev.libs.com.mongodb.internal.session;

import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

public interface SessionContext {
   boolean hasSession();

   boolean isImplicitSession();

   BsonDocument getSessionId();

   boolean isCausallyConsistent();

   long getTransactionNumber();

   long advanceTransactionNumber();

   boolean notifyMessageSent();

   @Nullable
   BsonTimestamp getOperationTime();

   void advanceOperationTime(@Nullable BsonTimestamp var1);

   @Nullable
   BsonDocument getClusterTime();

   void advanceClusterTime(@Nullable BsonDocument var1);

   boolean isSnapshot();

   void setSnapshotTimestamp(@Nullable BsonTimestamp var1);

   @Nullable
   BsonTimestamp getSnapshotTimestamp();

   boolean hasActiveTransaction();

   ReadConcern getReadConcern();

   void setRecoveryToken(BsonDocument var1);

   void clearTransactionContext();

   void markSessionDirty();

   boolean isSessionMarkedDirty();
}
