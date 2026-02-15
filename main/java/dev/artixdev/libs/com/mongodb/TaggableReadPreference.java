package dev.artixdev.libs.com.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

@Immutable
public abstract class TaggableReadPreference extends ReadPreference {
   private static final int SMALLEST_MAX_STALENESS_MS = 90000;
   private static final int IDLE_WRITE_PERIOD_MS = 10000;
   private final List<TagSet> tagSetList = new ArrayList();
   private final Long maxStalenessMS;
   private final ReadPreferenceHedgeOptions hedgeOptions;

   TaggableReadPreference() {
      this.maxStalenessMS = null;
      this.hedgeOptions = null;
   }

   TaggableReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit, @Nullable ReadPreferenceHedgeOptions hedgeOptions) {
      Assertions.notNull("tagSetList", tagSetList);
      Assertions.isTrueArgument("maxStaleness is null or >= 0", maxStaleness == null || maxStaleness >= 0L);
      this.maxStalenessMS = maxStaleness == null ? null : TimeUnit.MILLISECONDS.convert(maxStaleness, timeUnit);
      this.tagSetList.addAll(tagSetList);
      this.hedgeOptions = hedgeOptions;
   }

   public abstract TaggableReadPreference withTagSet(TagSet var1);

   public abstract TaggableReadPreference withTagSetList(List<TagSet> var1);

   public abstract TaggableReadPreference withMaxStalenessMS(Long var1, TimeUnit var2);

   public abstract TaggableReadPreference withHedgeOptions(ReadPreferenceHedgeOptions var1);

   /** @deprecated */
   @Deprecated
   public boolean isSlaveOk() {
      return true;
   }

   public boolean isSecondaryOk() {
      return true;
   }

   public BsonDocument toDocument() {
      BsonDocument readPrefObject = new BsonDocument("mode", new BsonString(this.getName()));
      if (!this.tagSetList.isEmpty()) {
         readPrefObject.put((String)"tags", (BsonValue)this.tagsListToBsonArray());
      }

      if (this.maxStalenessMS != null) {
         readPrefObject.put((String)"maxStalenessSeconds", (BsonValue)(new BsonInt64(TimeUnit.MILLISECONDS.toSeconds(this.maxStalenessMS))));
      }

      if (this.hedgeOptions != null) {
         readPrefObject.put((String)"hedge", (BsonValue)this.hedgeOptions.toBsonDocument());
      }

      return readPrefObject;
   }

   public List<TagSet> getTagSetList() {
      return Collections.unmodifiableList(this.tagSetList);
   }

   @Nullable
   public Long getMaxStaleness(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return this.maxStalenessMS == null ? null : timeUnit.convert(this.maxStalenessMS, TimeUnit.MILLISECONDS);
   }

   @Nullable
   public ReadPreferenceHedgeOptions getHedgeOptions() {
      return this.hedgeOptions;
   }

   public String toString() {
      return "ReadPreference{name=" + this.getName() + (this.tagSetList.isEmpty() ? "" : ", tagSetList=" + this.tagSetList) + (this.maxStalenessMS == null ? "" : ", maxStalenessMS=" + this.maxStalenessMS) + ", hedgeOptions=" + this.hedgeOptions + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TaggableReadPreference that = (TaggableReadPreference)o;
         if (!Objects.equals(this.maxStalenessMS, that.maxStalenessMS)) {
            return false;
         } else if (!this.tagSetList.equals(that.tagSetList)) {
            return false;
         } else {
            return Objects.equals(this.hedgeOptions, that.hedgeOptions);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.tagSetList.hashCode();
      result = 31 * result + this.getName().hashCode();
      result = 31 * result + (this.maxStalenessMS != null ? this.maxStalenessMS.hashCode() : 0);
      result = 31 * result + (this.hedgeOptions != null ? this.hedgeOptions.hashCode() : 0);
      return result;
   }

   protected List<ServerDescription> chooseForNonReplicaSet(ClusterDescription clusterDescription) {
      return this.selectFreshServers(clusterDescription, ClusterDescriptionHelper.getAny(clusterDescription));
   }

   static ClusterDescription copyClusterDescription(ClusterDescription clusterDescription, List<ServerDescription> selectedServers) {
      return new ClusterDescription(clusterDescription.getConnectionMode(), clusterDescription.getType(), selectedServers, clusterDescription.getClusterSettings(), clusterDescription.getServerSettings());
   }

   List<ServerDescription> selectFreshServers(ClusterDescription clusterDescription, List<ServerDescription> servers) {
      Long maxStaleness = this.getMaxStaleness(TimeUnit.MILLISECONDS);
      if (maxStaleness == null) {
         return servers;
      } else if (clusterDescription.getServerSettings() == null) {
         throw new MongoConfigurationException("heartbeat frequency must be provided in cluster description");
      } else if (!this.serversAreAllThreeDotFour(clusterDescription)) {
         throw new MongoConfigurationException("Servers must all be at least version 3.4 when max staleness is configured");
      } else if (clusterDescription.getType() != ClusterType.REPLICA_SET) {
         return servers;
      } else {
         long heartbeatFrequencyMS = clusterDescription.getServerSettings().getHeartbeatFrequency(TimeUnit.MILLISECONDS);
         if (maxStaleness < Math.max(90000L, heartbeatFrequencyMS + 10000L)) {
            if (90000L > heartbeatFrequencyMS + 10000L) {
               throw new MongoConfigurationException(String.format("Max staleness (%d sec) must be at least 90 seconds", this.getMaxStaleness(TimeUnit.SECONDS)));
            } else {
               throw new MongoConfigurationException(String.format("Max staleness (%d ms) must be at least the heartbeat period (%d ms) plus the idle write period (%d ms)", maxStaleness, heartbeatFrequencyMS, 10000));
            }
         } else {
            List<ServerDescription> freshServers = new ArrayList(servers.size());
            ServerDescription primary = this.findPrimary(clusterDescription);
            if (primary != null) {
               Iterator var8 = servers.iterator();

               while(var8.hasNext()) {
                  ServerDescription cur = (ServerDescription)var8.next();
                  if (cur.isPrimary()) {
                     freshServers.add(cur);
                  } else if (this.getStalenessOfSecondaryRelativeToPrimary(primary, cur, heartbeatFrequencyMS) <= maxStaleness) {
                     freshServers.add(cur);
                  }
               }
            } else {
               ServerDescription mostUpToDateSecondary = this.findMostUpToDateSecondary(clusterDescription);
               if (mostUpToDateSecondary != null) {
                  Iterator var12 = servers.iterator();

                  while(var12.hasNext()) {
                     ServerDescription cur = (ServerDescription)var12.next();
                     if (this.getLastWriteDateNonNull(mostUpToDateSecondary).getTime() - this.getLastWriteDateNonNull(cur).getTime() + heartbeatFrequencyMS <= maxStaleness) {
                        freshServers.add(cur);
                     }
                  }
               }
            }

            return freshServers;
         }
      }
   }

   private long getStalenessOfSecondaryRelativeToPrimary(ServerDescription primary, ServerDescription serverDescription, long heartbeatFrequencyMS) {
      return this.getLastWriteDateNonNull(primary).getTime() + (serverDescription.getLastUpdateTime(TimeUnit.MILLISECONDS) - primary.getLastUpdateTime(TimeUnit.MILLISECONDS)) - this.getLastWriteDateNonNull(serverDescription).getTime() + heartbeatFrequencyMS;
   }

   @Nullable
   private ServerDescription findPrimary(ClusterDescription clusterDescription) {
      Iterator var2 = clusterDescription.getServerDescriptions().iterator();

      ServerDescription cur;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         cur = (ServerDescription)var2.next();
      } while(!cur.isPrimary());

      return cur;
   }

   private ServerDescription findMostUpToDateSecondary(ClusterDescription clusterDescription) {
      ServerDescription mostUpdateToDateSecondary = null;
      Iterator var3 = clusterDescription.getServerDescriptions().iterator();

      while(true) {
         ServerDescription cur;
         do {
            do {
               if (!var3.hasNext()) {
                  return mostUpdateToDateSecondary;
               }

               cur = (ServerDescription)var3.next();
            } while(!cur.isSecondary());
         } while(mostUpdateToDateSecondary != null && this.getLastWriteDateNonNull(cur).getTime() <= this.getLastWriteDateNonNull(mostUpdateToDateSecondary).getTime());

         mostUpdateToDateSecondary = cur;
      }
   }

   private Date getLastWriteDateNonNull(ServerDescription serverDescription) {
      Date lastWriteDate = serverDescription.getLastWriteDate();
      if (lastWriteDate == null) {
         throw new MongoClientException("lastWriteDate should not be null in " + serverDescription);
      } else {
         return lastWriteDate;
      }
   }

   private boolean serversAreAllThreeDotFour(ClusterDescription clusterDescription) {
      Iterator var2 = clusterDescription.getServerDescriptions().iterator();

      ServerDescription cur;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         cur = (ServerDescription)var2.next();
      } while(!cur.isOk() || cur.getMaxWireVersion() >= 5);

      return false;
   }

   private BsonArray tagsListToBsonArray() {
      BsonArray bsonArray = new BsonArray(this.tagSetList.size());
      Iterator var2 = this.tagSetList.iterator();

      while(var2.hasNext()) {
         TagSet tagSet = (TagSet)var2.next();
         bsonArray.add((BsonValue)this.toDocument(tagSet));
      }

      return bsonArray;
   }

   private BsonDocument toDocument(TagSet tagSet) {
      BsonDocument document = new BsonDocument();
      Iterator var3 = tagSet.iterator();

      while(var3.hasNext()) {
         Tag tag = (Tag)var3.next();
         document.put((String)tag.getName(), (BsonValue)(new BsonString(tag.getValue())));
      }

      return document;
   }

   static class PrimaryPreferredReadPreference extends TaggableReadPreference.SecondaryReadPreference {
      PrimaryPreferredReadPreference() {
      }

      PrimaryPreferredReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
         this(tagSetList, maxStaleness, timeUnit, (ReadPreferenceHedgeOptions)null);
      }

      PrimaryPreferredReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit, @Nullable ReadPreferenceHedgeOptions hedgeOptions) {
         super(tagSetList, maxStaleness, timeUnit, hedgeOptions);
      }

      public TaggableReadPreference withTagSet(TagSet tagSet) {
         return this.withTagSetList(Collections.singletonList(tagSet));
      }

      public TaggableReadPreference withTagSetList(List<TagSet> tagSetList) {
         Assertions.notNull("tagSetList", tagSetList);
         return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList, this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, this.getHedgeOptions());
      }

      public TaggableReadPreference withMaxStalenessMS(@Nullable Long maxStaleness, TimeUnit timeUnit) {
         Assertions.isTrueArgument("maxStaleness is null or >= 0", maxStaleness == null || maxStaleness >= 0L);
         return new TaggableReadPreference.PrimaryPreferredReadPreference(this.getTagSetList(), maxStaleness, timeUnit, this.getHedgeOptions());
      }

      public TaggableReadPreference withHedgeOptions(ReadPreferenceHedgeOptions hedgeOptions) {
         return new TaggableReadPreference.PrimaryPreferredReadPreference(this.getTagSetList(), this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, hedgeOptions);
      }

      public String getName() {
         return "primaryPreferred";
      }

      protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
         List<ServerDescription> selectedServers = this.selectFreshServers(clusterDescription, ClusterDescriptionHelper.getPrimaries(clusterDescription));
         if (selectedServers.isEmpty()) {
            selectedServers = super.chooseForReplicaSet(clusterDescription);
         }

         return selectedServers;
      }
   }

   static class NearestReadPreference extends TaggableReadPreference {
      NearestReadPreference() {
      }

      NearestReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
         this(tagSetList, maxStaleness, timeUnit, (ReadPreferenceHedgeOptions)null);
      }

      NearestReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit, @Nullable ReadPreferenceHedgeOptions hedgeOptions) {
         super(tagSetList, maxStaleness, timeUnit, hedgeOptions);
      }

      public TaggableReadPreference withTagSet(TagSet tagSet) {
         return this.withTagSetList(Collections.singletonList(tagSet));
      }

      public TaggableReadPreference withTagSetList(List<TagSet> tagSetList) {
         Assertions.notNull("tagSetList", tagSetList);
         return new TaggableReadPreference.NearestReadPreference(tagSetList, this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, this.getHedgeOptions());
      }

      public TaggableReadPreference withMaxStalenessMS(@Nullable Long maxStaleness, TimeUnit timeUnit) {
         Assertions.isTrueArgument("maxStaleness is null or >= 0", maxStaleness == null || maxStaleness >= 0L);
         return new TaggableReadPreference.NearestReadPreference(this.getTagSetList(), maxStaleness, timeUnit, this.getHedgeOptions());
      }

      public TaggableReadPreference withHedgeOptions(ReadPreferenceHedgeOptions hedgeOptions) {
         return new TaggableReadPreference.NearestReadPreference(this.getTagSetList(), this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, hedgeOptions);
      }

      public String getName() {
         return "nearest";
      }

      public List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
         List<ServerDescription> selectedServers = this.selectFreshServers(clusterDescription, ClusterDescriptionHelper.getAnyPrimaryOrSecondary(clusterDescription));
         if (!this.getTagSetList().isEmpty()) {
            ClusterDescription nonStaleClusterDescription = copyClusterDescription(clusterDescription, selectedServers);
            selectedServers = Collections.emptyList();
            Iterator var4 = this.getTagSetList().iterator();

            while(var4.hasNext()) {
               TagSet tagSet = (TagSet)var4.next();
               List<ServerDescription> servers = ClusterDescriptionHelper.getAnyPrimaryOrSecondary(nonStaleClusterDescription, tagSet);
               if (!servers.isEmpty()) {
                  selectedServers = servers;
                  break;
               }
            }
         }

         return selectedServers;
      }
   }

   static class SecondaryPreferredReadPreference extends TaggableReadPreference.SecondaryReadPreference {
      SecondaryPreferredReadPreference() {
      }

      SecondaryPreferredReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
         this(tagSetList, maxStaleness, timeUnit, (ReadPreferenceHedgeOptions)null);
      }

      SecondaryPreferredReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit, @Nullable ReadPreferenceHedgeOptions hedgeOptions) {
         super(tagSetList, maxStaleness, timeUnit, hedgeOptions);
      }

      public TaggableReadPreference withTagSet(TagSet tagSet) {
         return this.withTagSetList(Collections.singletonList(tagSet));
      }

      public TaggableReadPreference withTagSetList(List<TagSet> tagSetList) {
         Assertions.notNull("tagSetList", tagSetList);
         return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList, this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, this.getHedgeOptions());
      }

      public TaggableReadPreference withMaxStalenessMS(@Nullable Long maxStaleness, TimeUnit timeUnit) {
         Assertions.isTrueArgument("maxStaleness is null or >= 0", maxStaleness == null || maxStaleness >= 0L);
         return new TaggableReadPreference.SecondaryPreferredReadPreference(this.getTagSetList(), maxStaleness, timeUnit, this.getHedgeOptions());
      }

      public TaggableReadPreference withHedgeOptions(ReadPreferenceHedgeOptions hedgeOptions) {
         return new TaggableReadPreference.SecondaryPreferredReadPreference(this.getTagSetList(), this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, hedgeOptions);
      }

      public String getName() {
         return "secondaryPreferred";
      }

      protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
         List<ServerDescription> selectedServers = super.chooseForReplicaSet(clusterDescription);
         if (selectedServers.isEmpty()) {
            selectedServers = ClusterDescriptionHelper.getPrimaries(clusterDescription);
         }

         return selectedServers;
      }
   }

   static class SecondaryReadPreference extends TaggableReadPreference {
      SecondaryReadPreference() {
      }

      SecondaryReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
         this(tagSetList, maxStaleness, timeUnit, (ReadPreferenceHedgeOptions)null);
      }

      SecondaryReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit, @Nullable ReadPreferenceHedgeOptions hedgeOptions) {
         super(tagSetList, maxStaleness, timeUnit, hedgeOptions);
      }

      public TaggableReadPreference withTagSet(TagSet tagSet) {
         return this.withTagSetList(Collections.singletonList(tagSet));
      }

      public TaggableReadPreference withTagSetList(List<TagSet> tagSetList) {
         Assertions.notNull("tagSetList", tagSetList);
         return new TaggableReadPreference.SecondaryReadPreference(tagSetList, this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, this.getHedgeOptions());
      }

      public TaggableReadPreference withMaxStalenessMS(@Nullable Long maxStaleness, TimeUnit timeUnit) {
         Assertions.isTrueArgument("maxStaleness is null or >= 0", maxStaleness == null || maxStaleness >= 0L);
         return new TaggableReadPreference.SecondaryReadPreference(this.getTagSetList(), maxStaleness, timeUnit, this.getHedgeOptions());
      }

      public TaggableReadPreference withHedgeOptions(ReadPreferenceHedgeOptions hedgeOptions) {
         return new TaggableReadPreference.SecondaryReadPreference(this.getTagSetList(), this.getMaxStaleness(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, hedgeOptions);
      }

      public String getName() {
         return "secondary";
      }

      protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
         List<ServerDescription> selectedServers = this.selectFreshServers(clusterDescription, ClusterDescriptionHelper.getSecondaries(clusterDescription));
         if (!this.getTagSetList().isEmpty()) {
            ClusterDescription nonStaleClusterDescription = copyClusterDescription(clusterDescription, selectedServers);
            selectedServers = Collections.emptyList();
            Iterator var4 = this.getTagSetList().iterator();

            while(var4.hasNext()) {
               TagSet tagSet = (TagSet)var4.next();
               List<ServerDescription> servers = ClusterDescriptionHelper.getSecondaries(nonStaleClusterDescription, tagSet);
               if (!servers.isEmpty()) {
                  selectedServers = servers;
                  break;
               }
            }
         }

         return selectedServers;
      }
   }
}
