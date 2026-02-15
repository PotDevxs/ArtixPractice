package dev.artixdev.libs.com.mongodb.internal.session;

import java.util.concurrent.atomic.AtomicBoolean;
import dev.artixdev.libs.com.mongodb.ClientSessionOptions;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.binding.ReferenceCounted;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.session.ClientSession;
import dev.artixdev.libs.com.mongodb.session.ServerSession;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

public class BaseClientSessionImpl implements ClientSession {
   private static final String CLUSTER_TIME_KEY = "clusterTime";
   private final ServerSessionPool serverSessionPool;
   private ServerSession serverSession;
   private final Object originator;
   private final ClientSessionOptions options;
   private final AtomicBoolean closed = new AtomicBoolean(false);
   private BsonDocument clusterTime;
   private BsonTimestamp operationTime;
   private BsonTimestamp snapshotTimestamp;
   private ServerAddress pinnedServerAddress;
   private BsonDocument recoveryToken;
   private ReferenceCounted transactionContext;

   public BaseClientSessionImpl(ServerSessionPool serverSessionPool, Object originator, ClientSessionOptions options) {
      this.serverSessionPool = serverSessionPool;
      this.originator = originator;
      this.options = options;
      this.pinnedServerAddress = null;
   }

   @Nullable
   public ServerAddress getPinnedServerAddress() {
      return this.pinnedServerAddress;
   }

   public Object getTransactionContext() {
      return this.transactionContext;
   }

   public void setTransactionContext(ServerAddress address, Object transactionContext) {
      Assertions.assertTrue(transactionContext instanceof ReferenceCounted);
      this.pinnedServerAddress = address;
      this.transactionContext = (ReferenceCounted)transactionContext;
      this.transactionContext.retain();
   }

   public void clearTransactionContext() {
      this.pinnedServerAddress = null;
      if (this.transactionContext != null) {
         this.transactionContext.release();
         this.transactionContext = null;
      }

   }

   public BsonDocument getRecoveryToken() {
      return this.recoveryToken;
   }

   public void setRecoveryToken(BsonDocument recoveryToken) {
      this.recoveryToken = recoveryToken;
   }

   public ClientSessionOptions getOptions() {
      return this.options;
   }

   public boolean isCausallyConsistent() {
      Boolean causallyConsistent = this.options.isCausallyConsistent();
      return causallyConsistent == null || causallyConsistent;
   }

   public Object getOriginator() {
      return this.originator;
   }

   public BsonDocument getClusterTime() {
      return this.clusterTime;
   }

   public BsonTimestamp getOperationTime() {
      return this.operationTime;
   }

   public ServerSession getServerSession() {
      Assertions.isTrue("open", !this.closed.get());
      if (this.serverSession == null) {
         this.serverSession = this.serverSessionPool.get();
      }

      return this.serverSession;
   }

   public void advanceOperationTime(@Nullable BsonTimestamp newOperationTime) {
      Assertions.isTrue("open", !this.closed.get());
      this.operationTime = this.greaterOf(newOperationTime);
   }

   public void advanceClusterTime(@Nullable BsonDocument newClusterTime) {
      Assertions.isTrue("open", !this.closed.get());
      this.clusterTime = this.greaterOf(newClusterTime);
   }

   public void setSnapshotTimestamp(@Nullable BsonTimestamp snapshotTimestamp) {
      Assertions.isTrue("open", !this.closed.get());
      if (snapshotTimestamp != null) {
         if (this.snapshotTimestamp != null && !snapshotTimestamp.equals(this.snapshotTimestamp)) {
            throw new MongoClientException("Snapshot timestamps should not change during the lifetime of the session.  Current timestamp is " + this.snapshotTimestamp + ", and attempting to set it to " + snapshotTimestamp);
         }

         this.snapshotTimestamp = snapshotTimestamp;
      }

   }

   @Nullable
   public BsonTimestamp getSnapshotTimestamp() {
      Assertions.isTrue("open", !this.closed.get());
      return this.snapshotTimestamp;
   }

   private BsonDocument greaterOf(@Nullable BsonDocument newClusterTime) {
      if (newClusterTime == null) {
         return this.clusterTime;
      } else if (this.clusterTime == null) {
         return newClusterTime;
      } else {
         return newClusterTime.getTimestamp("clusterTime").compareTo(this.clusterTime.getTimestamp("clusterTime")) > 0 ? newClusterTime : this.clusterTime;
      }
   }

   private BsonTimestamp greaterOf(@Nullable BsonTimestamp newOperationTime) {
      if (newOperationTime == null) {
         return this.operationTime;
      } else if (this.operationTime == null) {
         return newOperationTime;
      } else {
         return newOperationTime.compareTo(this.operationTime) > 0 ? newOperationTime : this.operationTime;
      }
   }

   public void close() {
      if (this.closed.compareAndSet(false, true)) {
         if (this.serverSession != null) {
            this.serverSessionPool.release(this.serverSession);
         }

         this.clearTransactionContext();
      }

   }
}
