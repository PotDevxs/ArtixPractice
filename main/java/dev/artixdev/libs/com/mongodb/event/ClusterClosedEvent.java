package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;

public final class ClusterClosedEvent {
   private final ClusterId clusterId;

   public ClusterClosedEvent(ClusterId clusterId) {
      this.clusterId = (ClusterId)Assertions.notNull("clusterId", clusterId);
   }

   public ClusterId getClusterId() {
      return this.clusterId;
   }

   public String toString() {
      return "ClusterClosedEvent{clusterId=" + this.clusterId + '}';
   }
}
