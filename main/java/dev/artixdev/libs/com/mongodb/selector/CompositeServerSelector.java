package dev.artixdev.libs.com.mongodb.selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;

public final class CompositeServerSelector implements ServerSelector {
   private final List<ServerSelector> serverSelectors;

   public CompositeServerSelector(List<? extends ServerSelector> serverSelectors) {
      Assertions.notNull("serverSelectors", serverSelectors);
      if (serverSelectors.isEmpty()) {
         throw new IllegalArgumentException("Server selectors can not be an empty list");
      } else {
         ArrayList<ServerSelector> mergedServerSelectors = new ArrayList();
         Iterator var3 = serverSelectors.iterator();

         while(var3.hasNext()) {
            ServerSelector cur = (ServerSelector)var3.next();
            if (cur == null) {
               throw new IllegalArgumentException("Can not have a null server selector in the list of composed selectors");
            }

            if (cur instanceof CompositeServerSelector) {
               mergedServerSelectors.addAll(((CompositeServerSelector)cur).serverSelectors);
            } else {
               mergedServerSelectors.add(cur);
            }
         }

         this.serverSelectors = Collections.unmodifiableList(mergedServerSelectors);
      }
   }

   public List<ServerSelector> getServerSelectors() {
      return this.serverSelectors;
   }

   public List<ServerDescription> select(ClusterDescription clusterDescription) {
      ClusterDescription curClusterDescription = clusterDescription;
      List<ServerDescription> choices = null;

      for(Iterator var4 = this.serverSelectors.iterator(); var4.hasNext(); curClusterDescription = new ClusterDescription(clusterDescription.getConnectionMode(), clusterDescription.getType(), choices, clusterDescription.getClusterSettings(), clusterDescription.getServerSettings())) {
         ServerSelector cur = (ServerSelector)var4.next();
         choices = cur.select(curClusterDescription);
      }

      return (List)Assertions.assertNotNull(choices);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CompositeServerSelector that = (CompositeServerSelector)o;
         return this.serverSelectors.size() != that.serverSelectors.size() ? false : this.serverSelectors.equals(that.serverSelectors);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.serverSelectors != null ? this.serverSelectors.hashCode() : 0;
   }

   public String toString() {
      return "CompositeServerSelector{serverSelectors=" + this.serverSelectors + '}';
   }
}
