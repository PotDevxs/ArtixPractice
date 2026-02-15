package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Iterator;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

final class EventHelper {
   static boolean wouldDescriptionsGenerateEquivalentEvents(ClusterDescription current, ClusterDescription previous) {
      if (!exceptionsEquals(current.getSrvResolutionException(), previous.getSrvResolutionException())) {
         return false;
      } else if (current.getServerDescriptions().size() != previous.getServerDescriptions().size()) {
         return false;
      } else {
         Iterator var2 = current.getServerDescriptions().iterator();

         ServerDescription curNew;
         ServerDescription matchingPrev;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            curNew = (ServerDescription)var2.next();
            matchingPrev = null;
            Iterator var5 = previous.getServerDescriptions().iterator();

            while(var5.hasNext()) {
               ServerDescription curPrev = (ServerDescription)var5.next();
               if (curNew.getAddress().equals(curPrev.getAddress())) {
                  matchingPrev = curPrev;
                  break;
               }
            }
         } while(wouldDescriptionsGenerateEquivalentEvents(curNew, matchingPrev));

         return false;
      }
   }

   static boolean wouldDescriptionsGenerateEquivalentEvents(@Nullable ServerDescription current, @Nullable ServerDescription previous) {
      if (current == previous) {
         return true;
      } else if (previous != null && current != null) {
         if (current.isOk() != previous.isOk()) {
            return false;
         } else if (current.getState() != previous.getState()) {
            return false;
         } else if (current.getType() != previous.getType()) {
            return false;
         } else if (current.getMinWireVersion() != previous.getMinWireVersion()) {
            return false;
         } else if (current.getMaxWireVersion() != previous.getMaxWireVersion()) {
            return false;
         } else if (!Objects.equals(current.getCanonicalAddress(), previous.getCanonicalAddress())) {
            return false;
         } else if (!current.getHosts().equals(previous.getHosts())) {
            return false;
         } else if (!current.getPassives().equals(previous.getPassives())) {
            return false;
         } else if (!current.getArbiters().equals(previous.getArbiters())) {
            return false;
         } else if (!current.getTagSet().equals(previous.getTagSet())) {
            return false;
         } else if (!Objects.equals(current.getSetName(), previous.getSetName())) {
            return false;
         } else if (!Objects.equals(current.getSetVersion(), previous.getSetVersion())) {
            return false;
         } else if (!Objects.equals(current.getElectionId(), previous.getElectionId())) {
            return false;
         } else if (!Objects.equals(current.getPrimary(), previous.getPrimary())) {
            return false;
         } else if (!Objects.equals(current.getLogicalSessionTimeoutMinutes(), previous.getLogicalSessionTimeoutMinutes())) {
            return false;
         } else if (!Objects.equals(current.getTopologyVersion(), previous.getTopologyVersion())) {
            return false;
         } else {
            return exceptionsEquals(current.getException(), previous.getException());
         }
      } else {
         return false;
      }
   }

   private static boolean exceptionsEquals(@Nullable Throwable current, @Nullable Throwable previous) {
      if (current != null && previous != null) {
         if (!Objects.equals(current.getClass(), previous.getClass())) {
            return false;
         } else {
            return Objects.equals(current.getMessage(), previous.getMessage());
         }
      } else {
         return current == previous;
      }
   }

   private EventHelper() {
   }
}
