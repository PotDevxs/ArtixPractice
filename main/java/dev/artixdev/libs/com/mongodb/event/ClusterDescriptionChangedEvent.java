package dev.artixdev.libs.com.mongodb.event;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;

public final class ClusterDescriptionChangedEvent {
   private final ClusterId clusterId;
   private final ClusterDescription newDescription;
   private final ClusterDescription previousDescription;

   public ClusterDescriptionChangedEvent(ClusterId clusterId, ClusterDescription newDescription, ClusterDescription previousDescription) {
      this.clusterId = (ClusterId)Assertions.notNull("clusterId", clusterId);
      this.newDescription = (ClusterDescription)Assertions.notNull("newDescription", newDescription);
      this.previousDescription = (ClusterDescription)Assertions.notNull("previousDescription", previousDescription);
   }

   public ClusterId getClusterId() {
      return this.clusterId;
   }

   public ClusterDescription getNewDescription() {
      return this.newDescription;
   }

   public ClusterDescription getPreviousDescription() {
      return this.previousDescription;
   }

   public String toString() {
      return "ClusterDescriptionChangedEvent{clusterId=" + this.clusterId + ", newDescription=" + this.newDescription + ", previousDescription=" + this.previousDescription + '}';
   }
}
