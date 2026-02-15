package dev.artixdev.libs.com.mongodb.internal.selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

public class LatencyMinimizingServerSelector implements ServerSelector {
   private final long acceptableLatencyDifferenceNanos;

   public LatencyMinimizingServerSelector(long acceptableLatencyDifference, TimeUnit timeUnit) {
      this.acceptableLatencyDifferenceNanos = TimeUnit.NANOSECONDS.convert(acceptableLatencyDifference, timeUnit);
   }

   public long getAcceptableLatencyDifference(TimeUnit timeUnit) {
      return timeUnit.convert(this.acceptableLatencyDifferenceNanos, TimeUnit.NANOSECONDS);
   }

   public List<ServerDescription> select(ClusterDescription clusterDescription) {
      return clusterDescription.getConnectionMode() != ClusterConnectionMode.MULTIPLE ? ClusterDescriptionHelper.getAny(clusterDescription) : this.getServersWithAcceptableLatencyDifference(ClusterDescriptionHelper.getAny(clusterDescription), this.getFastestRoundTripTimeNanos(clusterDescription.getServerDescriptions()));
   }

   public String toString() {
      return "LatencyMinimizingServerSelector{acceptableLatencyDifference=" + TimeUnit.MILLISECONDS.convert(this.acceptableLatencyDifferenceNanos, TimeUnit.NANOSECONDS) + " ms" + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         LatencyMinimizingServerSelector that = (LatencyMinimizingServerSelector)o;
         return this.acceptableLatencyDifferenceNanos == that.acceptableLatencyDifferenceNanos;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)(this.acceptableLatencyDifferenceNanos ^ this.acceptableLatencyDifferenceNanos >>> 32);
   }

   private long getFastestRoundTripTimeNanos(List<ServerDescription> members) {
      long fastestRoundTripTime = Long.MAX_VALUE;
      Iterator var4 = members.iterator();

      while(var4.hasNext()) {
         ServerDescription cur = (ServerDescription)var4.next();
         if (cur.isOk() && cur.getRoundTripTimeNanos() < fastestRoundTripTime) {
            fastestRoundTripTime = cur.getRoundTripTimeNanos();
         }
      }

      return fastestRoundTripTime;
   }

   private List<ServerDescription> getServersWithAcceptableLatencyDifference(List<ServerDescription> servers, long bestPingTime) {
      List<ServerDescription> goodSecondaries = new ArrayList(servers.size());
      Iterator var5 = servers.iterator();

      while(var5.hasNext()) {
         ServerDescription cur = (ServerDescription)var5.next();
         if (cur.isOk() && cur.getRoundTripTimeNanos() - this.acceptableLatencyDifferenceNanos <= bestPingTime) {
            goodSecondaries.add(cur);
         }
      }

      return goodSecondaries;
   }
}
