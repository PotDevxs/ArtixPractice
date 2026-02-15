package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Optional;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.connection.TopologyVersion;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

final class TopologyVersionHelper {
   static boolean newer(@Nullable TopologyVersion current, @Nullable TopologyVersion candidate) {
      return compare(current, candidate) > 0;
   }

   static boolean newerOrEqual(@Nullable TopologyVersion current, @Nullable TopologyVersion candidate) {
      return compare(current, candidate) >= 0;
   }

   static Optional<TopologyVersion> topologyVersion(@Nullable Throwable t) {
      TopologyVersion result = null;
      if (t instanceof MongoCommandException) {
         BsonDocument rawTopologyVersion = ((MongoCommandException)t).getResponse().getDocument("topologyVersion", (BsonDocument)null);
         if (rawTopologyVersion != null) {
            result = new TopologyVersion(rawTopologyVersion);
         }
      }

      return Optional.ofNullable(result);
   }

   private static int compare(@Nullable TopologyVersion o1, @Nullable TopologyVersion o2) {
      if (o1 != null && o2 != null) {
         return o1.getProcessId().equals(o2.getProcessId()) ? Long.compare(o1.getCounter(), o2.getCounter()) : -1;
      } else {
         return -1;
      }
   }

   private TopologyVersionHelper() {
      throw new AssertionError();
   }
}
