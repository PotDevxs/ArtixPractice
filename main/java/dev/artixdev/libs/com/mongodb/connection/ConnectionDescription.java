package dev.artixdev.libs.com.mongodb.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.types.ObjectId;

@Immutable
public class ConnectionDescription {
   @Nullable
   private final ObjectId serviceId;
   private final ConnectionId connectionId;
   private final int maxWireVersion;
   private final ServerType serverType;
   private final int maxBatchCount;
   private final int maxDocumentSize;
   private final int maxMessageSize;
   private final List<String> compressors;
   private final BsonArray saslSupportedMechanisms;
   private final Integer logicalSessionTimeoutMinutes;
   private static final int DEFAULT_MAX_MESSAGE_SIZE = 33554432;
   private static final int DEFAULT_MAX_WRITE_BATCH_SIZE = 512;

   public ConnectionDescription(ServerId serverId) {
      this(new ConnectionId(serverId), 0, ServerType.UNKNOWN, 512, ServerDescription.getDefaultMaxDocumentSize(), 33554432, Collections.emptyList());
   }

   public ConnectionDescription(ConnectionId connectionId, int maxWireVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors) {
      this(connectionId, maxWireVersion, serverType, maxBatchCount, maxDocumentSize, maxMessageSize, compressors, (BsonArray)null);
   }

   public ConnectionDescription(ConnectionId connectionId, int maxWireVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors, @Nullable BsonArray saslSupportedMechanisms) {
      this((ObjectId)null, connectionId, maxWireVersion, serverType, maxBatchCount, maxDocumentSize, maxMessageSize, compressors, saslSupportedMechanisms);
   }

   public ConnectionDescription(ConnectionId connectionId, int maxWireVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors, @Nullable BsonArray saslSupportedMechanisms, @Nullable Integer logicalSessionTimeoutMinutes) {
      this((ObjectId)null, connectionId, maxWireVersion, serverType, maxBatchCount, maxDocumentSize, maxMessageSize, compressors, saslSupportedMechanisms, logicalSessionTimeoutMinutes);
   }

   public ConnectionDescription(@Nullable ObjectId serviceId, ConnectionId connectionId, int maxWireVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors, @Nullable BsonArray saslSupportedMechanisms) {
      this(serviceId, connectionId, maxWireVersion, serverType, maxBatchCount, maxDocumentSize, maxMessageSize, compressors, saslSupportedMechanisms, (Integer)null);
   }

   private ConnectionDescription(@Nullable ObjectId serviceId, ConnectionId connectionId, int maxWireVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors, @Nullable BsonArray saslSupportedMechanisms, @Nullable Integer logicalSessionTimeoutMinutes) {
      this.serviceId = serviceId;
      this.connectionId = connectionId;
      this.serverType = serverType;
      this.maxBatchCount = maxBatchCount;
      this.maxDocumentSize = maxDocumentSize;
      this.maxMessageSize = maxMessageSize;
      this.maxWireVersion = maxWireVersion;
      this.compressors = (List)Assertions.notNull("compressors", Collections.unmodifiableList(new ArrayList(compressors)));
      this.saslSupportedMechanisms = saslSupportedMechanisms;
      this.logicalSessionTimeoutMinutes = logicalSessionTimeoutMinutes;
   }

   public ConnectionDescription withConnectionId(ConnectionId connectionId) {
      Assertions.notNull("connectionId", connectionId);
      return new ConnectionDescription(this.serviceId, connectionId, this.maxWireVersion, this.serverType, this.maxBatchCount, this.maxDocumentSize, this.maxMessageSize, this.compressors, this.saslSupportedMechanisms, this.logicalSessionTimeoutMinutes);
   }

   public ConnectionDescription withServiceId(ObjectId serviceId) {
      Assertions.notNull("serviceId", serviceId);
      return new ConnectionDescription(serviceId, this.connectionId, this.maxWireVersion, this.serverType, this.maxBatchCount, this.maxDocumentSize, this.maxMessageSize, this.compressors, this.saslSupportedMechanisms, this.logicalSessionTimeoutMinutes);
   }

   public ServerAddress getServerAddress() {
      return this.connectionId.getServerId().getAddress();
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   @Nullable
   public ObjectId getServiceId() {
      return this.serviceId;
   }

   public int getMaxWireVersion() {
      return this.maxWireVersion;
   }

   public ServerType getServerType() {
      return this.serverType;
   }

   public int getMaxBatchCount() {
      return this.maxBatchCount;
   }

   public int getMaxDocumentSize() {
      return this.maxDocumentSize;
   }

   public int getMaxMessageSize() {
      return this.maxMessageSize;
   }

   public List<String> getCompressors() {
      return this.compressors;
   }

   @Nullable
   public BsonArray getSaslSupportedMechanisms() {
      return this.saslSupportedMechanisms;
   }

   @Nullable
   public Integer getLogicalSessionTimeoutMinutes() {
      return this.logicalSessionTimeoutMinutes;
   }

   public static int getDefaultMaxMessageSize() {
      return 33554432;
   }

   public static int getDefaultMaxWriteBatchSize() {
      return 512;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ConnectionDescription that = (ConnectionDescription)o;
         if (this.maxWireVersion != that.maxWireVersion) {
            return false;
         } else if (this.maxBatchCount != that.maxBatchCount) {
            return false;
         } else if (this.maxDocumentSize != that.maxDocumentSize) {
            return false;
         } else if (this.maxMessageSize != that.maxMessageSize) {
            return false;
         } else if (!Objects.equals(this.serviceId, that.serviceId)) {
            return false;
         } else if (!this.connectionId.equals(that.connectionId)) {
            return false;
         } else if (this.serverType != that.serverType) {
            return false;
         } else if (!this.compressors.equals(that.compressors)) {
            return false;
         } else {
            return !Objects.equals(this.logicalSessionTimeoutMinutes, that.logicalSessionTimeoutMinutes) ? false : Objects.equals(this.saslSupportedMechanisms, that.saslSupportedMechanisms);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.connectionId.hashCode();
      result = 31 * result + this.maxWireVersion;
      result = 31 * result + this.serverType.hashCode();
      result = 31 * result + this.maxBatchCount;
      result = 31 * result + this.maxDocumentSize;
      result = 31 * result + this.maxMessageSize;
      result = 31 * result + this.compressors.hashCode();
      result = 31 * result + (this.serviceId != null ? this.serviceId.hashCode() : 0);
      result = 31 * result + (this.saslSupportedMechanisms != null ? this.saslSupportedMechanisms.hashCode() : 0);
      result = 31 * result + (this.logicalSessionTimeoutMinutes != null ? this.logicalSessionTimeoutMinutes.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "ConnectionDescription{connectionId=" + this.connectionId + ", maxWireVersion=" + this.maxWireVersion + ", serverType=" + this.serverType + ", maxBatchCount=" + this.maxBatchCount + ", maxDocumentSize=" + this.maxDocumentSize + ", maxMessageSize=" + this.maxMessageSize + ", compressors=" + this.compressors + ", logicialSessionTimeoutMinutes=" + this.logicalSessionTimeoutMinutes + ", serviceId=" + this.serviceId + '}';
   }
}
