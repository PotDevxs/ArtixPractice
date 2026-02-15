package dev.artixdev.libs.com.mongodb.connection;

public enum ClusterType {
   STANDALONE,
   REPLICA_SET,
   SHARDED,
   LOAD_BALANCED,
   UNKNOWN;

   // $FF: synthetic method
   private static ClusterType[] $values() {
      return new ClusterType[]{STANDALONE, REPLICA_SET, SHARDED, LOAD_BALANCED, UNKNOWN};
   }
}
