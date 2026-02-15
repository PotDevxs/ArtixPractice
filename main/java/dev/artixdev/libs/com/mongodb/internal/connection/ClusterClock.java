package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

public class ClusterClock {
   private static final String CLUSTER_TIME_KEY = "clusterTime";
   private final ReentrantLock lock = new ReentrantLock();
   private BsonDocument clusterTime;

   public BsonDocument getCurrent() {
      return (BsonDocument)Locks.withInterruptibleLock(this.lock, (Supplier)(() -> {
         return this.clusterTime;
      }));
   }

   public BsonTimestamp getClusterTime() {
      return (BsonTimestamp)Locks.withInterruptibleLock(this.lock, (Supplier)(() -> {
         return this.clusterTime != null ? this.clusterTime.getTimestamp("clusterTime") : null;
      }));
   }

   public void advance(@Nullable BsonDocument other) {
      Locks.withInterruptibleLock(this.lock, (Supplier)(() -> {
         return this.clusterTime = this.greaterOf(other);
      }));
   }

   public BsonDocument greaterOf(@Nullable BsonDocument other) {
      return (BsonDocument)Locks.withInterruptibleLock(this.lock, (Supplier)(() -> {
         if (other == null) {
            return this.clusterTime;
         } else if (this.clusterTime == null) {
            return other;
         } else {
            return other.getTimestamp("clusterTime").compareTo(this.clusterTime.getTimestamp("clusterTime")) > 0 ? other : this.clusterTime;
         }
      }));
   }
}
