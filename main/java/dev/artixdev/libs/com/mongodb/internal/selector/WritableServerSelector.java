package dev.artixdev.libs.com.mongodb.internal.selector;

import java.util.List;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

public final class WritableServerSelector implements ServerSelector {
   public List<ServerDescription> select(ClusterDescription clusterDescription) {
      return clusterDescription.getConnectionMode() != ClusterConnectionMode.SINGLE && clusterDescription.getConnectionMode() != ClusterConnectionMode.LOAD_BALANCED ? ClusterDescriptionHelper.getPrimaries(clusterDescription) : ClusterDescriptionHelper.getAny(clusterDescription);
   }

   public String toString() {
      return "WritableServerSelector";
   }
}
