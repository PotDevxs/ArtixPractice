package dev.artixdev.libs.com.mongodb.session;

import java.io.Closeable;
import dev.artixdev.libs.com.mongodb.ClientSessionOptions;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

@NotThreadSafe
public interface ClientSession extends Closeable {
   @Nullable
   ServerAddress getPinnedServerAddress();

   @Nullable
   Object getTransactionContext();

   void setTransactionContext(ServerAddress var1, Object var2);

   void clearTransactionContext();

   @Nullable
   BsonDocument getRecoveryToken();

   void setRecoveryToken(BsonDocument var1);

   ClientSessionOptions getOptions();

   boolean isCausallyConsistent();

   Object getOriginator();

   ServerSession getServerSession();

   BsonTimestamp getOperationTime();

   void advanceOperationTime(@Nullable BsonTimestamp var1);

   void advanceClusterTime(@Nullable BsonDocument var1);

   void setSnapshotTimestamp(@Nullable BsonTimestamp var1);

   @Nullable
   BsonTimestamp getSnapshotTimestamp();

   BsonDocument getClusterTime();

   void close();
}
