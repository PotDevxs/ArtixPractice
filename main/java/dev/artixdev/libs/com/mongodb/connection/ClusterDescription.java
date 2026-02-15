package dev.artixdev.libs.com.mongodb.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import dev.artixdev.libs.com.mongodb.internal.selector.ReadPreferenceServerSelector;
import dev.artixdev.libs.com.mongodb.internal.selector.WritableServerSelector;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public class ClusterDescription {
   private final ClusterConnectionMode connectionMode;
   private final ClusterType type;
   private final List<ServerDescription> serverDescriptions;
   private final ClusterSettings clusterSettings;
   private final ServerSettings serverSettings;
   private final MongoException srvResolutionException;
   private final Integer logicalSessionTimeoutMinutes;

   public ClusterDescription(ClusterConnectionMode connectionMode, ClusterType type, List<ServerDescription> serverDescriptions) {
      this(connectionMode, type, serverDescriptions, (ClusterSettings)null, (ServerSettings)null);
   }

   public ClusterDescription(ClusterConnectionMode connectionMode, ClusterType type, List<ServerDescription> serverDescriptions, @Nullable ClusterSettings clusterSettings, @Nullable ServerSettings serverSettings) {
      this(connectionMode, type, (MongoException)null, serverDescriptions, clusterSettings, serverSettings);
   }

   public ClusterDescription(ClusterConnectionMode connectionMode, ClusterType type, @Nullable MongoException srvResolutionException, List<ServerDescription> serverDescriptions, @Nullable ClusterSettings clusterSettings, @Nullable ServerSettings serverSettings) {
      Assertions.notNull("all", serverDescriptions);
      this.connectionMode = (ClusterConnectionMode)Assertions.notNull("connectionMode", connectionMode);
      this.type = (ClusterType)Assertions.notNull("type", type);
      this.srvResolutionException = srvResolutionException;
      this.serverDescriptions = new ArrayList(serverDescriptions);
      this.clusterSettings = clusterSettings;
      this.serverSettings = serverSettings;
      this.logicalSessionTimeoutMinutes = this.calculateLogicalSessionTimeoutMinutes();
   }

   public ClusterSettings getClusterSettings() {
      return this.clusterSettings;
   }

   public ServerSettings getServerSettings() {
      return this.serverSettings;
   }

   public boolean isCompatibleWithDriver() {
      Iterator var1 = this.serverDescriptions.iterator();

      ServerDescription cur;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         cur = (ServerDescription)var1.next();
      } while(cur.isCompatibleWithDriver());

      return false;
   }

   @Nullable
   public ServerDescription findServerIncompatiblyOlderThanDriver() {
      Iterator var1 = this.serverDescriptions.iterator();

      ServerDescription cur;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         cur = (ServerDescription)var1.next();
      } while(!cur.isIncompatiblyOlderThanDriver());

      return cur;
   }

   @Nullable
   public ServerDescription findServerIncompatiblyNewerThanDriver() {
      Iterator var1 = this.serverDescriptions.iterator();

      ServerDescription cur;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         cur = (ServerDescription)var1.next();
      } while(!cur.isIncompatiblyNewerThanDriver());

      return cur;
   }

   public boolean hasReadableServer(ReadPreference readPreference) {
      Assertions.notNull("readPreference", readPreference);
      return !(new ReadPreferenceServerSelector(readPreference)).select(this).isEmpty();
   }

   public boolean hasWritableServer() {
      return !(new WritableServerSelector()).select(this).isEmpty();
   }

   public ClusterConnectionMode getConnectionMode() {
      return this.connectionMode;
   }

   public ClusterType getType() {
      return this.type;
   }

   @Nullable
   public MongoException getSrvResolutionException() {
      return this.srvResolutionException;
   }

   public List<ServerDescription> getServerDescriptions() {
      return Collections.unmodifiableList(this.serverDescriptions);
   }

   @Nullable
   public Integer getLogicalSessionTimeoutMinutes() {
      return this.logicalSessionTimeoutMinutes;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ClusterDescription that = (ClusterDescription)o;
         if (this.connectionMode != that.connectionMode) {
            return false;
         } else if (this.type != that.type) {
            return false;
         } else if (this.serverDescriptions.size() != that.serverDescriptions.size()) {
            return false;
         } else if (!this.serverDescriptions.containsAll(that.serverDescriptions)) {
            return false;
         } else {
            Class<?> thisExceptionClass = this.srvResolutionException != null ? this.srvResolutionException.getClass() : null;
            Class<?> thatExceptionClass = that.srvResolutionException != null ? that.srvResolutionException.getClass() : null;
            if (!Objects.equals(thisExceptionClass, thatExceptionClass)) {
               return false;
            } else {
               String thisExceptionMessage = this.srvResolutionException != null ? this.srvResolutionException.getMessage() : null;
               String thatExceptionMessage = that.srvResolutionException != null ? that.srvResolutionException.getMessage() : null;
               return Objects.equals(thisExceptionMessage, thatExceptionMessage);
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.connectionMode.hashCode();
      result = 31 * result + this.type.hashCode();
      result = 31 * result + (this.srvResolutionException == null ? 0 : this.srvResolutionException.hashCode());
      result = 31 * result + this.serverDescriptions.hashCode();
      return result;
   }

   public String toString() {
      return "ClusterDescription{type=" + this.getType() + (this.srvResolutionException == null ? "" : ", srvResolutionException=" + this.srvResolutionException) + ", connectionMode=" + this.connectionMode + ", serverDescriptions=" + this.serverDescriptions + '}';
   }

   public String getShortDescription() {
      StringBuilder serverDescriptions = new StringBuilder();
      String delimiter = "";

      for(Iterator var3 = this.serverDescriptions.iterator(); var3.hasNext(); delimiter = ", ") {
         ServerDescription cur = (ServerDescription)var3.next();
         serverDescriptions.append(delimiter).append(cur.getShortDescription());
      }

      return this.srvResolutionException == null ? String.format("{type=%s, servers=[%s]", this.type, serverDescriptions) : String.format("{type=%s, srvResolutionException=%s, servers=[%s]", this.type, this.srvResolutionException, serverDescriptions);
   }

   @Nullable
   private Integer calculateLogicalSessionTimeoutMinutes() {
      Integer retVal = null;
      Iterator var2 = ClusterDescriptionHelper.getServersByPredicate(this, (serverDescription) -> {
         return serverDescription.isPrimary() || serverDescription.isSecondary();
      }).iterator();

      while(var2.hasNext()) {
         ServerDescription cur = (ServerDescription)var2.next();
         Integer logicalSessionTimeoutMinutes = cur.getLogicalSessionTimeoutMinutes();
         if (logicalSessionTimeoutMinutes == null) {
            return null;
         }

         if (retVal == null) {
            retVal = logicalSessionTimeoutMinutes;
         } else {
            retVal = Math.min(retVal, logicalSessionTimeoutMinutes);
         }
      }

      return retVal;
   }
}
