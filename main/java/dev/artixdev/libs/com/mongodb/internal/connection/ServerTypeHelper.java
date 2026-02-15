package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.connection.ServerType;

final class ServerTypeHelper {
   static boolean isDataBearing(ServerType serverType) {
      switch(serverType) {
      case STANDALONE:
      case REPLICA_SET_PRIMARY:
      case REPLICA_SET_SECONDARY:
      case SHARD_ROUTER:
      case LOAD_BALANCER:
         return true;
      case REPLICA_SET_ARBITER:
      case REPLICA_SET_OTHER:
      case REPLICA_SET_GHOST:
      case UNKNOWN:
         return false;
      default:
         throw new AssertionError();
      }
   }

   private ServerTypeHelper() {
      throw new AssertionError();
   }
}
