package dev.artixdev.libs.com.mongodb.connection;

import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class ServerId {
   private final ClusterId clusterId;
   private final ServerAddress address;

   public ServerId(ClusterId clusterId, ServerAddress address) {
      this.clusterId = (ClusterId)Assertions.notNull("clusterId", clusterId);
      this.address = (ServerAddress)Assertions.notNull("address", address);
   }

   public ClusterId getClusterId() {
      return this.clusterId;
   }

   public ServerAddress getAddress() {
      return this.address;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ServerId serverId = (ServerId)o;
         if (!this.address.equals(serverId.address)) {
            return false;
         } else {
            return this.clusterId.equals(serverId.clusterId);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.clusterId.hashCode();
      result = 31 * result + this.address.hashCode();
      return result;
   }

   public String toString() {
      return "ServerId{clusterId=" + this.clusterId + ", address=" + this.address + '}';
   }
}
