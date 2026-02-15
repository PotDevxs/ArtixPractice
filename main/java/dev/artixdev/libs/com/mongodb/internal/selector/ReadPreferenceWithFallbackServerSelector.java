package dev.artixdev.libs.com.mongodb.internal.selector;

import java.util.List;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerConnectionState;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

public class ReadPreferenceWithFallbackServerSelector implements ServerSelector {
   private final ReadPreference preferredReadPreference;
   private final int minWireVersion;
   private final ReadPreference fallbackReadPreference;
   private ReadPreference appliedReadPreference;

   public ReadPreferenceWithFallbackServerSelector(ReadPreference preferredReadPreference, int minWireVersion, ReadPreference fallbackReadPreference) {
      this.preferredReadPreference = preferredReadPreference;
      this.minWireVersion = minWireVersion;
      this.fallbackReadPreference = fallbackReadPreference;
   }

   public List<ServerDescription> select(ClusterDescription clusterDescription) {
      if (this.clusterContainsOlderServers(clusterDescription)) {
         this.appliedReadPreference = this.fallbackReadPreference;
         return (new ReadPreferenceServerSelector(this.fallbackReadPreference)).select(clusterDescription);
      } else {
         this.appliedReadPreference = this.preferredReadPreference;
         return (new ReadPreferenceServerSelector(this.preferredReadPreference)).select(clusterDescription);
      }
   }

   public ReadPreference getAppliedReadPreference() {
      return this.appliedReadPreference;
   }

   private boolean clusterContainsOlderServers(ClusterDescription clusterDescription) {
      return clusterDescription.getServerDescriptions().stream().filter((serverDescription) -> {
         return serverDescription.getState() == ServerConnectionState.CONNECTED;
      }).anyMatch((serverDescription) -> {
         return serverDescription.getMaxWireVersion() < this.minWireVersion;
      });
   }

   public String toString() {
      return "ReadPreferenceWithFallbackServerSelector{preferredReadPreference=" + this.preferredReadPreference + ", fallbackReadPreference=" + this.fallbackReadPreference + ", minWireVersionForPreferred=" + this.minWireVersion + '}';
   }
}
