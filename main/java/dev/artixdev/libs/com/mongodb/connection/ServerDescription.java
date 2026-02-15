package dev.artixdev.libs.com.mongodb.connection;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.TagSet;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.connection.DecimalFormatHelper;
import dev.artixdev.libs.com.mongodb.internal.connection.Time;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.types.ObjectId;

@Immutable
public class ServerDescription {
   public static final String MIN_DRIVER_SERVER_VERSION = "3.6";
   public static final int MIN_DRIVER_WIRE_VERSION = 6;
   public static final int MAX_DRIVER_WIRE_VERSION = 21;
   private static final int DEFAULT_MAX_DOCUMENT_SIZE = 16777216;
   private final ServerAddress address;
   private final ServerType type;
   private final String canonicalAddress;
   private final Set<String> hosts;
   private final Set<String> passives;
   private final Set<String> arbiters;
   private final String primary;
   private final int maxDocumentSize;
   private final TagSet tagSet;
   private final String setName;
   private final long roundTripTimeNanos;
   private final boolean ok;
   private final ServerConnectionState state;
   private final int minWireVersion;
   private final int maxWireVersion;
   private final ObjectId electionId;
   private final Integer setVersion;
   private final TopologyVersion topologyVersion;
   private final Date lastWriteDate;
   private final long lastUpdateTimeNanos;
   private final Integer logicalSessionTimeoutMinutes;
   private final Throwable exception;
   private final boolean helloOk;

   public static ServerDescription.Builder builder() {
      return new ServerDescription.Builder();
   }

   public static ServerDescription.Builder builder(ServerDescription serverDescription) {
      return new ServerDescription.Builder(serverDescription);
   }

   @Nullable
   public String getCanonicalAddress() {
      return this.canonicalAddress;
   }

   @Nullable
   public Integer getLogicalSessionTimeoutMinutes() {
      return this.logicalSessionTimeoutMinutes;
   }

   public boolean isHelloOk() {
      return this.helloOk;
   }

   public boolean isCompatibleWithDriver() {
      if (this.type == ServerType.LOAD_BALANCER) {
         return true;
      } else if (this.isIncompatiblyOlderThanDriver()) {
         return false;
      } else {
         return !this.isIncompatiblyNewerThanDriver();
      }
   }

   public boolean isIncompatiblyNewerThanDriver() {
      return this.ok && this.type != ServerType.LOAD_BALANCER && this.minWireVersion > 21;
   }

   public boolean isIncompatiblyOlderThanDriver() {
      return this.ok && this.type != ServerType.LOAD_BALANCER && this.maxWireVersion < 6;
   }

   public static int getDefaultMaxDocumentSize() {
      return 16777216;
   }

   public static int getDefaultMinWireVersion() {
      return 0;
   }

   public static int getDefaultMaxWireVersion() {
      return 0;
   }

   public ServerAddress getAddress() {
      return this.address;
   }

   public boolean isReplicaSetMember() {
      return this.type.getClusterType() == ClusterType.REPLICA_SET;
   }

   public boolean isShardRouter() {
      return this.type == ServerType.SHARD_ROUTER;
   }

   public boolean isStandAlone() {
      return this.type == ServerType.STANDALONE;
   }

   public boolean isPrimary() {
      return this.ok && (this.type == ServerType.REPLICA_SET_PRIMARY || this.type == ServerType.SHARD_ROUTER || this.type == ServerType.STANDALONE || this.type == ServerType.LOAD_BALANCER);
   }

   public boolean isSecondary() {
      return this.ok && (this.type == ServerType.REPLICA_SET_SECONDARY || this.type == ServerType.SHARD_ROUTER || this.type == ServerType.STANDALONE || this.type == ServerType.LOAD_BALANCER);
   }

   public Set<String> getHosts() {
      return this.hosts;
   }

   public Set<String> getPassives() {
      return this.passives;
   }

   public Set<String> getArbiters() {
      return this.arbiters;
   }

   @Nullable
   public String getPrimary() {
      return this.primary;
   }

   public int getMaxDocumentSize() {
      return this.maxDocumentSize;
   }

   public TagSet getTagSet() {
      return this.tagSet;
   }

   public int getMinWireVersion() {
      return this.minWireVersion;
   }

   public int getMaxWireVersion() {
      return this.maxWireVersion;
   }

   @Nullable
   public ObjectId getElectionId() {
      return this.electionId;
   }

   @Nullable
   public Integer getSetVersion() {
      return this.setVersion;
   }

   @Nullable
   public TopologyVersion getTopologyVersion() {
      return this.topologyVersion;
   }

   @Nullable
   public Date getLastWriteDate() {
      return this.lastWriteDate;
   }

   public long getLastUpdateTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.lastUpdateTimeNanos, TimeUnit.NANOSECONDS);
   }

   public boolean hasTags(TagSet desiredTags) {
      if (!this.ok) {
         return false;
      } else {
         return this.type != ServerType.STANDALONE && this.type != ServerType.SHARD_ROUTER ? this.tagSet.containsAll(desiredTags) : true;
      }
   }

   @Nullable
   public String getSetName() {
      return this.setName;
   }

   public boolean isOk() {
      return this.ok;
   }

   public ServerConnectionState getState() {
      return this.state;
   }

   public ServerType getType() {
      return this.type;
   }

   public ClusterType getClusterType() {
      return this.type.getClusterType();
   }

   public long getRoundTripTimeNanos() {
      return this.roundTripTimeNanos;
   }

   @Nullable
   public Throwable getException() {
      return this.exception;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ServerDescription that = (ServerDescription)o;
         if (this.maxDocumentSize != that.maxDocumentSize) {
            return false;
         } else if (this.ok != that.ok) {
            return false;
         } else if (!this.address.equals(that.address)) {
            return false;
         } else if (!this.arbiters.equals(that.arbiters)) {
            return false;
         } else if (!Objects.equals(this.canonicalAddress, that.canonicalAddress)) {
            return false;
         } else if (!this.hosts.equals(that.hosts)) {
            return false;
         } else if (!this.passives.equals(that.passives)) {
            return false;
         } else if (!Objects.equals(this.primary, that.primary)) {
            return false;
         } else if (!Objects.equals(this.setName, that.setName)) {
            return false;
         } else if (this.state != that.state) {
            return false;
         } else if (!this.tagSet.equals(that.tagSet)) {
            return false;
         } else if (this.type != that.type) {
            return false;
         } else if (this.minWireVersion != that.minWireVersion) {
            return false;
         } else if (this.maxWireVersion != that.maxWireVersion) {
            return false;
         } else if (!Objects.equals(this.electionId, that.electionId)) {
            return false;
         } else if (!Objects.equals(this.setVersion, that.setVersion)) {
            return false;
         } else if (!Objects.equals(this.topologyVersion, that.topologyVersion)) {
            return false;
         } else if (!Objects.equals(this.lastWriteDate, that.lastWriteDate)) {
            return false;
         } else if (this.lastUpdateTimeNanos != that.lastUpdateTimeNanos) {
            return false;
         } else if (!Objects.equals(this.logicalSessionTimeoutMinutes, that.logicalSessionTimeoutMinutes)) {
            return false;
         } else if (this.helloOk != that.helloOk) {
            return false;
         } else {
            Class<?> thisExceptionClass = this.exception != null ? this.exception.getClass() : null;
            Class<?> thatExceptionClass = that.exception != null ? that.exception.getClass() : null;
            if (!Objects.equals(thisExceptionClass, thatExceptionClass)) {
               return false;
            } else {
               String thisExceptionMessage = this.exception != null ? this.exception.getMessage() : null;
               String thatExceptionMessage = that.exception != null ? that.exception.getMessage() : null;
               return Objects.equals(thisExceptionMessage, thatExceptionMessage);
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.address.hashCode();
      result = 31 * result + this.type.hashCode();
      result = 31 * result + (this.canonicalAddress != null ? this.canonicalAddress.hashCode() : 0);
      result = 31 * result + this.hosts.hashCode();
      result = 31 * result + this.passives.hashCode();
      result = 31 * result + this.arbiters.hashCode();
      result = 31 * result + (this.primary != null ? this.primary.hashCode() : 0);
      result = 31 * result + this.maxDocumentSize;
      result = 31 * result + this.tagSet.hashCode();
      result = 31 * result + (this.setName != null ? this.setName.hashCode() : 0);
      result = 31 * result + (this.electionId != null ? this.electionId.hashCode() : 0);
      result = 31 * result + (this.setVersion != null ? this.setVersion.hashCode() : 0);
      result = 31 * result + (this.topologyVersion != null ? this.topologyVersion.hashCode() : 0);
      result = 31 * result + (this.lastWriteDate != null ? this.lastWriteDate.hashCode() : 0);
      result = 31 * result + (int)(this.lastUpdateTimeNanos ^ this.lastUpdateTimeNanos >>> 32);
      result = 31 * result + (this.ok ? 1 : 0);
      result = 31 * result + this.state.hashCode();
      result = 31 * result + this.minWireVersion;
      result = 31 * result + this.maxWireVersion;
      result = 31 * result + (this.logicalSessionTimeoutMinutes != null ? this.logicalSessionTimeoutMinutes.hashCode() : 0);
      result = 31 * result + (this.helloOk ? 1 : 0);
      result = 31 * result + (this.exception == null ? 0 : this.exception.getClass().hashCode());
      result = 31 * result + (this.exception == null ? 0 : this.exception.getMessage().hashCode());
      return result;
   }

   public String toString() {
      return "ServerDescription{address=" + this.address + ", type=" + this.type + ", state=" + this.state + (this.state == ServerConnectionState.CONNECTED ? ", ok=" + this.ok + ", minWireVersion=" + this.minWireVersion + ", maxWireVersion=" + this.maxWireVersion + ", maxDocumentSize=" + this.maxDocumentSize + ", logicalSessionTimeoutMinutes=" + this.logicalSessionTimeoutMinutes + ", roundTripTimeNanos=" + this.roundTripTimeNanos : "") + (this.isReplicaSetMember() ? ", setName='" + this.setName + '\'' + ", canonicalAddress=" + this.canonicalAddress + ", hosts=" + this.hosts + ", passives=" + this.passives + ", arbiters=" + this.arbiters + ", primary='" + this.primary + '\'' + ", tagSet=" + this.tagSet + ", electionId=" + this.electionId + ", setVersion=" + this.setVersion + ", topologyVersion=" + this.topologyVersion + ", lastWriteDate=" + this.lastWriteDate + ", lastUpdateTimeNanos=" + this.lastUpdateTimeNanos : "") + (this.exception == null ? "" : ", exception=" + this.translateExceptionToString()) + '}';
   }

   public String getShortDescription() {
      return "{address=" + this.address + ", type=" + this.type + (!this.tagSet.iterator().hasNext() ? "" : ", " + this.tagSet) + (this.state == ServerConnectionState.CONNECTED ? ", roundTripTime=" + this.getRoundTripFormattedInMilliseconds() + " ms" : "") + ", state=" + this.state + (this.exception == null ? "" : ", exception=" + this.translateExceptionToString()) + '}';
   }

   private String translateExceptionToString() {
      StringBuilder builder = new StringBuilder();
      builder.append("{");
      builder.append(this.exception);
      builder.append("}");

      for(Throwable cur = this.exception.getCause(); cur != null; cur = cur.getCause()) {
         builder.append(", caused by ");
         builder.append("{");
         builder.append(cur);
         builder.append("}");
      }

      return builder.toString();
   }

   private String getRoundTripFormattedInMilliseconds() {
      return DecimalFormatHelper.format("#0.0", (double)this.roundTripTimeNanos / 1000.0D / 1000.0D);
   }

   ServerDescription(ServerDescription.Builder builder) {
      this.address = (ServerAddress)Assertions.notNull("address", builder.address);
      this.type = (ServerType)Assertions.notNull("type", builder.type);
      this.state = (ServerConnectionState)Assertions.notNull("state", builder.state);
      this.canonicalAddress = builder.canonicalAddress;
      this.hosts = builder.hosts;
      this.passives = builder.passives;
      this.arbiters = builder.arbiters;
      this.primary = builder.primary;
      this.maxDocumentSize = builder.maxDocumentSize;
      this.tagSet = builder.tagSet;
      this.setName = builder.setName;
      this.roundTripTimeNanos = builder.roundTripTimeNanos;
      this.ok = builder.ok;
      this.minWireVersion = builder.minWireVersion;
      this.maxWireVersion = builder.maxWireVersion;
      this.electionId = builder.electionId;
      this.setVersion = builder.setVersion;
      this.topologyVersion = builder.topologyVersion;
      this.lastWriteDate = builder.lastWriteDate;
      this.lastUpdateTimeNanos = builder.lastUpdateTimeNanos;
      this.logicalSessionTimeoutMinutes = builder.logicalSessionTimeoutMinutes;
      this.helloOk = builder.helloOk;
      this.exception = builder.exception;
   }

   @NotThreadSafe
   public static class Builder {
      private ServerAddress address;
      private ServerType type;
      private String canonicalAddress;
      private Set<String> hosts;
      private Set<String> passives;
      private Set<String> arbiters;
      private String primary;
      private int maxDocumentSize;
      private TagSet tagSet;
      private String setName;
      private long roundTripTimeNanos;
      private boolean ok;
      private ServerConnectionState state;
      private int minWireVersion;
      private int maxWireVersion;
      private ObjectId electionId;
      private Integer setVersion;
      private TopologyVersion topologyVersion;
      private Date lastWriteDate;
      private long lastUpdateTimeNanos;
      private Integer logicalSessionTimeoutMinutes;
      private boolean helloOk;
      private Throwable exception;

      Builder() {
         this.type = ServerType.UNKNOWN;
         this.hosts = Collections.emptySet();
         this.passives = Collections.emptySet();
         this.arbiters = Collections.emptySet();
         this.maxDocumentSize = 16777216;
         this.tagSet = new TagSet();
         this.minWireVersion = 0;
         this.maxWireVersion = 0;
         this.lastUpdateTimeNanos = Time.nanoTime();
      }

      Builder(ServerDescription serverDescription) {
         this.type = ServerType.UNKNOWN;
         this.hosts = Collections.emptySet();
         this.passives = Collections.emptySet();
         this.arbiters = Collections.emptySet();
         this.maxDocumentSize = 16777216;
         this.tagSet = new TagSet();
         this.minWireVersion = 0;
         this.maxWireVersion = 0;
         this.lastUpdateTimeNanos = Time.nanoTime();
         this.address = serverDescription.address;
         this.type = serverDescription.type;
         this.canonicalAddress = serverDescription.canonicalAddress;
         this.hosts = serverDescription.hosts;
         this.passives = serverDescription.passives;
         this.arbiters = serverDescription.arbiters;
         this.primary = serverDescription.primary;
         this.maxDocumentSize = serverDescription.maxDocumentSize;
         this.tagSet = serverDescription.tagSet;
         this.setName = serverDescription.setName;
         this.roundTripTimeNanos = serverDescription.roundTripTimeNanos;
         this.ok = serverDescription.ok;
         this.state = serverDescription.state;
         this.minWireVersion = serverDescription.minWireVersion;
         this.maxWireVersion = serverDescription.maxWireVersion;
         this.electionId = serverDescription.electionId;
         this.setVersion = serverDescription.setVersion;
         this.topologyVersion = serverDescription.topologyVersion;
         this.lastWriteDate = serverDescription.lastWriteDate;
         this.lastUpdateTimeNanos = serverDescription.lastUpdateTimeNanos;
         this.logicalSessionTimeoutMinutes = serverDescription.logicalSessionTimeoutMinutes;
         this.exception = serverDescription.exception;
      }

      public ServerDescription.Builder address(ServerAddress address) {
         this.address = address;
         return this;
      }

      public ServerDescription.Builder canonicalAddress(@Nullable String canonicalAddress) {
         this.canonicalAddress = canonicalAddress;
         return this;
      }

      public ServerDescription.Builder type(ServerType type) {
         this.type = (ServerType)Assertions.notNull("type", type);
         return this;
      }

      public ServerDescription.Builder hosts(@Nullable Set<String> hosts) {
         this.hosts = hosts == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet(hosts));
         return this;
      }

      public ServerDescription.Builder passives(@Nullable Set<String> passives) {
         this.passives = passives == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet(passives));
         return this;
      }

      public ServerDescription.Builder arbiters(@Nullable Set<String> arbiters) {
         this.arbiters = arbiters == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet(arbiters));
         return this;
      }

      public ServerDescription.Builder primary(@Nullable String primary) {
         this.primary = primary;
         return this;
      }

      public ServerDescription.Builder maxDocumentSize(int maxDocumentSize) {
         this.maxDocumentSize = maxDocumentSize;
         return this;
      }

      public ServerDescription.Builder tagSet(@Nullable TagSet tagSet) {
         this.tagSet = tagSet == null ? new TagSet() : tagSet;
         return this;
      }

      public ServerDescription.Builder roundTripTime(long roundTripTime, TimeUnit timeUnit) {
         this.roundTripTimeNanos = timeUnit.toNanos(roundTripTime);
         return this;
      }

      public ServerDescription.Builder setName(@Nullable String setName) {
         this.setName = setName;
         return this;
      }

      public ServerDescription.Builder ok(boolean ok) {
         this.ok = ok;
         return this;
      }

      public ServerDescription.Builder state(ServerConnectionState state) {
         this.state = state;
         return this;
      }

      public ServerDescription.Builder minWireVersion(int minWireVersion) {
         this.minWireVersion = minWireVersion;
         return this;
      }

      public ServerDescription.Builder maxWireVersion(int maxWireVersion) {
         this.maxWireVersion = maxWireVersion;
         return this;
      }

      public ServerDescription.Builder electionId(@Nullable ObjectId electionId) {
         this.electionId = electionId;
         return this;
      }

      public ServerDescription.Builder setVersion(@Nullable Integer setVersion) {
         this.setVersion = setVersion;
         return this;
      }

      public ServerDescription.Builder topologyVersion(@Nullable TopologyVersion topologyVersion) {
         this.topologyVersion = topologyVersion;
         return this;
      }

      public ServerDescription.Builder lastWriteDate(@Nullable Date lastWriteDate) {
         this.lastWriteDate = lastWriteDate;
         return this;
      }

      public ServerDescription.Builder lastUpdateTimeNanos(long lastUpdateTimeNanos) {
         this.lastUpdateTimeNanos = lastUpdateTimeNanos;
         return this;
      }

      public ServerDescription.Builder logicalSessionTimeoutMinutes(@Nullable Integer logicalSessionTimeoutMinutes) {
         this.logicalSessionTimeoutMinutes = logicalSessionTimeoutMinutes;
         return this;
      }

      public ServerDescription.Builder helloOk(boolean helloOk) {
         this.helloOk = helloOk;
         return this;
      }

      public ServerDescription.Builder exception(Throwable exception) {
         this.exception = exception;
         return this;
      }

      public ServerDescription build() {
         return new ServerDescription(this);
      }
   }
}
