package dev.artixdev.libs.com.mongodb.internal.selector;

import java.util.List;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

public class ReadPreferenceServerSelector implements ServerSelector {
   private final ReadPreference readPreference;

   public ReadPreferenceServerSelector(ReadPreference readPreference) {
      this.readPreference = (ReadPreference)Assertions.notNull("readPreference", readPreference);
   }

   public ReadPreference getReadPreference() {
      return this.readPreference;
   }

   public List<ServerDescription> select(ClusterDescription clusterDescription) {
      return clusterDescription.getConnectionMode() == ClusterConnectionMode.SINGLE ? ClusterDescriptionHelper.getAny(clusterDescription) : this.readPreference.choose(clusterDescription);
   }

   public String toString() {
      return "ReadPreferenceServerSelector{readPreference=" + this.readPreference + '}';
   }
}
