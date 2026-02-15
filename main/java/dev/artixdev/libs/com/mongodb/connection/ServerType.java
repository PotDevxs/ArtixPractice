package dev.artixdev.libs.com.mongodb.connection;

public enum ServerType {
   STANDALONE {
      public ClusterType getClusterType() {
         return ClusterType.STANDALONE;
      }
   },
   REPLICA_SET_PRIMARY {
      public ClusterType getClusterType() {
         return ClusterType.REPLICA_SET;
      }
   },
   REPLICA_SET_SECONDARY {
      public ClusterType getClusterType() {
         return ClusterType.REPLICA_SET;
      }
   },
   REPLICA_SET_ARBITER {
      public ClusterType getClusterType() {
         return ClusterType.REPLICA_SET;
      }
   },
   REPLICA_SET_OTHER {
      public ClusterType getClusterType() {
         return ClusterType.REPLICA_SET;
      }
   },
   REPLICA_SET_GHOST {
      public ClusterType getClusterType() {
         return ClusterType.REPLICA_SET;
      }
   },
   SHARD_ROUTER {
      public ClusterType getClusterType() {
         return ClusterType.SHARDED;
      }
   },
   LOAD_BALANCER {
      public ClusterType getClusterType() {
         return ClusterType.LOAD_BALANCED;
      }
   },
   UNKNOWN {
      public ClusterType getClusterType() {
         return ClusterType.UNKNOWN;
      }
   };

   private ServerType() {
   }

   public abstract ClusterType getClusterType();

   // $FF: synthetic method
   private static ServerType[] $values() {
      return new ServerType[]{STANDALONE, REPLICA_SET_PRIMARY, REPLICA_SET_SECONDARY, REPLICA_SET_ARBITER, REPLICA_SET_OTHER, REPLICA_SET_GHOST, SHARD_ROUTER, LOAD_BALANCER, UNKNOWN};
   }

   // $FF: synthetic method
   ServerType(Object x2) {
      this();
   }
}
