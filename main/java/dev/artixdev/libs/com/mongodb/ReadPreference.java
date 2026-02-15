package dev.artixdev.libs.com.mongodb;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

@Immutable
public abstract class ReadPreference {
   private static final ReadPreference PRIMARY = new ReadPreference.PrimaryReadPreference();
   private static final ReadPreference SECONDARY = new TaggableReadPreference.SecondaryReadPreference();
   private static final ReadPreference SECONDARY_PREFERRED = new TaggableReadPreference.SecondaryPreferredReadPreference();
   private static final ReadPreference PRIMARY_PREFERRED = new TaggableReadPreference.PrimaryPreferredReadPreference();
   private static final ReadPreference NEAREST = new TaggableReadPreference.NearestReadPreference();

   ReadPreference() {
   }

   public abstract ReadPreference withTagSet(TagSet var1);

   public abstract ReadPreference withTagSetList(List<TagSet> var1);

   public abstract ReadPreference withMaxStalenessMS(Long var1, TimeUnit var2);

   public abstract ReadPreference withHedgeOptions(ReadPreferenceHedgeOptions var1);

   /** @deprecated */
   @Deprecated
   public abstract boolean isSlaveOk();

   public abstract boolean isSecondaryOk();

   public abstract String getName();

   public abstract BsonDocument toDocument();

   public final List<ServerDescription> choose(ClusterDescription clusterDescription) {
      switch(clusterDescription.getType()) {
      case REPLICA_SET:
         return this.chooseForReplicaSet(clusterDescription);
      case SHARDED:
      case STANDALONE:
         return this.chooseForNonReplicaSet(clusterDescription);
      case LOAD_BALANCED:
         return clusterDescription.getServerDescriptions();
      case UNKNOWN:
         return Collections.emptyList();
      default:
         throw new UnsupportedOperationException("Unsupported cluster type: " + clusterDescription.getType());
      }
   }

   protected abstract List<ServerDescription> chooseForNonReplicaSet(ClusterDescription var1);

   protected abstract List<ServerDescription> chooseForReplicaSet(ClusterDescription var1);

   public static ReadPreference primary() {
      return PRIMARY;
   }

   public static ReadPreference primaryPreferred() {
      return PRIMARY_PREFERRED;
   }

   public static ReadPreference secondary() {
      return SECONDARY;
   }

   public static ReadPreference secondaryPreferred() {
      return SECONDARY_PREFERRED;
   }

   public static ReadPreference nearest() {
      return NEAREST;
   }

   public static ReadPreference primaryPreferred(long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.PrimaryPreferredReadPreference(Collections.emptyList(), maxStaleness, timeUnit);
   }

   public static ReadPreference secondary(long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.SecondaryReadPreference(Collections.emptyList(), maxStaleness, timeUnit);
   }

   public static ReadPreference secondaryPreferred(long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.SecondaryPreferredReadPreference(Collections.emptyList(), maxStaleness, timeUnit);
   }

   public static ReadPreference nearest(long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.NearestReadPreference(Collections.emptyList(), maxStaleness, timeUnit);
   }

   public static TaggableReadPreference primaryPreferred(TagSet tagSet) {
      return new TaggableReadPreference.PrimaryPreferredReadPreference(Collections.singletonList(tagSet), (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference secondary(TagSet tagSet) {
      return new TaggableReadPreference.SecondaryReadPreference(Collections.singletonList(tagSet), (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference secondaryPreferred(TagSet tagSet) {
      return new TaggableReadPreference.SecondaryPreferredReadPreference(Collections.singletonList(tagSet), (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference nearest(TagSet tagSet) {
      return new TaggableReadPreference.NearestReadPreference(Collections.singletonList(tagSet), (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference primaryPreferred(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.PrimaryPreferredReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
   }

   public static TaggableReadPreference secondary(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.SecondaryReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
   }

   public static TaggableReadPreference secondaryPreferred(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.SecondaryPreferredReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
   }

   public static TaggableReadPreference nearest(TagSet tagSet, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.NearestReadPreference(Collections.singletonList(tagSet), maxStaleness, timeUnit);
   }

   public static TaggableReadPreference primaryPreferred(List<TagSet> tagSetList) {
      return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList, (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference secondary(List<TagSet> tagSetList) {
      return new TaggableReadPreference.SecondaryReadPreference(tagSetList, (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference secondaryPreferred(List<TagSet> tagSetList) {
      return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList, (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference nearest(List<TagSet> tagSetList) {
      return new TaggableReadPreference.NearestReadPreference(tagSetList, (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference primaryPreferred(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
   }

   public static TaggableReadPreference secondary(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.SecondaryReadPreference(tagSetList, maxStaleness, timeUnit);
   }

   public static TaggableReadPreference secondaryPreferred(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
   }

   public static TaggableReadPreference nearest(List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
      return new TaggableReadPreference.NearestReadPreference(tagSetList, maxStaleness, timeUnit);
   }

   public static ReadPreference valueOf(String name) {
      Assertions.notNull("name", name);
      String nameToCheck = name.toLowerCase();
      if (nameToCheck.equals(PRIMARY.getName().toLowerCase())) {
         return PRIMARY;
      } else if (nameToCheck.equals(SECONDARY.getName().toLowerCase())) {
         return SECONDARY;
      } else if (nameToCheck.equals(SECONDARY_PREFERRED.getName().toLowerCase())) {
         return SECONDARY_PREFERRED;
      } else if (nameToCheck.equals(PRIMARY_PREFERRED.getName().toLowerCase())) {
         return PRIMARY_PREFERRED;
      } else if (nameToCheck.equals(NEAREST.getName().toLowerCase())) {
         return NEAREST;
      } else {
         throw new IllegalArgumentException("No match for read preference of " + name);
      }
   }

   public static TaggableReadPreference valueOf(String name, List<TagSet> tagSetList) {
      return valueOf(name, tagSetList, (Long)null, TimeUnit.MILLISECONDS);
   }

   public static TaggableReadPreference valueOf(String name, List<TagSet> tagSetList, long maxStaleness, TimeUnit timeUnit) {
      return valueOf(name, tagSetList, maxStaleness, timeUnit);
   }

   private static TaggableReadPreference valueOf(String name, List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
      Assertions.notNull("name", name);
      Assertions.notNull("tagSetList", tagSetList);
      Assertions.notNull("timeUnit", timeUnit);
      String nameToCheck = name.toLowerCase();
      if (nameToCheck.equals(PRIMARY.getName().toLowerCase())) {
         throw new IllegalArgumentException("Primary read preference can not also specify tag sets, max staleness or hedge");
      } else if (nameToCheck.equals(SECONDARY.getName().toLowerCase())) {
         return new TaggableReadPreference.SecondaryReadPreference(tagSetList, maxStaleness, timeUnit);
      } else if (nameToCheck.equals(SECONDARY_PREFERRED.getName().toLowerCase())) {
         return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
      } else if (nameToCheck.equals(PRIMARY_PREFERRED.getName().toLowerCase())) {
         return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList, maxStaleness, timeUnit);
      } else if (nameToCheck.equals(NEAREST.getName().toLowerCase())) {
         return new TaggableReadPreference.NearestReadPreference(tagSetList, maxStaleness, timeUnit);
      } else {
         throw new IllegalArgumentException("No match for read preference of " + name);
      }
   }

   private static final class PrimaryReadPreference extends ReadPreference {
      private PrimaryReadPreference() {
      }

      public ReadPreference withTagSet(TagSet tagSet) {
         throw new UnsupportedOperationException("Primary read preference can not also specify tag sets");
      }

      public TaggableReadPreference withTagSetList(List<TagSet> tagSet) {
         throw new UnsupportedOperationException("Primary read preference can not also specify tag sets");
      }

      public TaggableReadPreference withMaxStalenessMS(Long maxStalenessMS, TimeUnit timeUnit) {
         throw new UnsupportedOperationException("Primary read preference can not also specify max staleness");
      }

      public TaggableReadPreference withHedgeOptions(ReadPreferenceHedgeOptions hedgeOptions) {
         throw new UnsupportedOperationException("Primary read preference can not also specify hedge");
      }

      public boolean isSlaveOk() {
         return false;
      }

      public boolean isSecondaryOk() {
         return false;
      }

      public String toString() {
         return this.getName();
      }

      public boolean equals(Object o) {
         return o != null && this.getClass() == o.getClass();
      }

      public int hashCode() {
         return this.getName().hashCode();
      }

      public BsonDocument toDocument() {
         return new BsonDocument("mode", new BsonString(this.getName()));
      }

      protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
         return ClusterDescriptionHelper.getPrimaries(clusterDescription);
      }

      protected List<ServerDescription> chooseForNonReplicaSet(ClusterDescription clusterDescription) {
         return ClusterDescriptionHelper.getAny(clusterDescription);
      }

      public String getName() {
         return "primary";
      }

      // $FF: synthetic method
      PrimaryReadPreference(Object x0) {
         this();
      }
   }
}
