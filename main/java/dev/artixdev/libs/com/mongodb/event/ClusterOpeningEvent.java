package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;

public final class ClusterOpeningEvent {
   private final ClusterId clusterId;

   public ClusterOpeningEvent(ClusterId clusterId) {
      this.clusterId = (ClusterId)Assertions.notNull("clusterId", clusterId);
   }

   public ClusterId getClusterId() {
      return this.clusterId;
   }

   public String toString() {
      return "ClusterOpeningEvent{clusterId=" + this.clusterId + '}';
   }
}
