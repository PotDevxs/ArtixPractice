package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.TagSet;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class ClusterDescriptionHelper {
   public static Set<ServerDescription> getAll(ClusterDescription clusterDescription) {
      Set<ServerDescription> serverDescriptionSet = new TreeSet<>(Comparator.comparing((ServerDescription o) -> {
         return o.getAddress().getHost();
      }).thenComparingInt((o) -> {
         return o.getAddress().getPort();
      }));
      serverDescriptionSet.addAll(clusterDescription.getServerDescriptions());
      return Collections.unmodifiableSet(serverDescriptionSet);
   }

   @Nullable
   public static ServerDescription getByServerAddress(ClusterDescription clusterDescription, ServerAddress serverAddress) {
      Iterator<ServerDescription> iterator = clusterDescription.getServerDescriptions().iterator();

      ServerDescription cur;
      do {
         if (!iterator.hasNext()) {
            return null;
         }

         cur = iterator.next();
      } while(!cur.isOk() || !cur.getAddress().equals(serverAddress));

      return cur;
   }

   public static List<ServerDescription> getPrimaries(ClusterDescription clusterDescription) {
      return getServersByPredicate(clusterDescription, ServerDescription::isPrimary);
   }

   public static List<ServerDescription> getSecondaries(ClusterDescription clusterDescription) {
      return getServersByPredicate(clusterDescription, ServerDescription::isSecondary);
   }

   public static List<ServerDescription> getSecondaries(ClusterDescription clusterDescription, TagSet tagSet) {
      return getServersByPredicate(clusterDescription, (serverDescription) -> {
         return serverDescription.isSecondary() && serverDescription.hasTags(tagSet);
      });
   }

   public static List<ServerDescription> getAny(ClusterDescription clusterDescription) {
      return getServersByPredicate(clusterDescription, ServerDescription::isOk);
   }

   public static List<ServerDescription> getAnyPrimaryOrSecondary(ClusterDescription clusterDescription) {
      return getServersByPredicate(clusterDescription, (serverDescription) -> {
         return serverDescription.isPrimary() || serverDescription.isSecondary();
      });
   }

   public static List<ServerDescription> getAnyPrimaryOrSecondary(ClusterDescription clusterDescription, TagSet tagSet) {
      return getServersByPredicate(clusterDescription, (serverDescription) -> {
         return (serverDescription.isPrimary() || serverDescription.isSecondary()) && serverDescription.hasTags(tagSet);
      });
   }

   public static List<ServerDescription> getServersByPredicate(ClusterDescription clusterDescription, ClusterDescriptionHelper.Predicate predicate) {
      List<ServerDescription> membersByTag = new ArrayList();
      Iterator<ServerDescription> iterator = clusterDescription.getServerDescriptions().iterator();

      while(iterator.hasNext()) {
         ServerDescription cur = iterator.next();
         if (predicate.apply(cur)) {
            membersByTag.add(cur);
         }
      }

      return membersByTag;
   }

   private ClusterDescriptionHelper() {
   }

   public interface Predicate {
      boolean apply(ServerDescription var1);
   }
}
