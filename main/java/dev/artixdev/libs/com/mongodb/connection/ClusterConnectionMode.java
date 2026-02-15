package dev.artixdev.libs.com.mongodb.connection;

public enum ClusterConnectionMode {
   SINGLE,
   MULTIPLE,
   LOAD_BALANCED;

   // $FF: synthetic method
   private static ClusterConnectionMode[] $values() {
      return new ClusterConnectionMode[]{SINGLE, MULTIPLE, LOAD_BALANCED};
   }
}
