package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.Tag;
import dev.artixdev.libs.com.mongodb.TagSet;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;
import dev.artixdev.libs.com.mongodb.connection.ServerConnectionState;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.connection.TopologyVersion;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.types.ObjectId;

public final class DescriptionHelper {
   static ConnectionDescription createConnectionDescription(ClusterConnectionMode clusterConnectionMode, ConnectionId connectionId, BsonDocument helloResult) {
      ConnectionDescription connectionDescription = new ConnectionDescription(connectionId, getMaxWireVersion(helloResult), getServerType(helloResult), getMaxWriteBatchSize(helloResult), getMaxBsonObjectSize(helloResult), getMaxMessageSizeBytes(helloResult), getCompressors(helloResult), helloResult.getArray("saslSupportedMechs", (BsonArray)null), getLogicalSessionTimeoutMinutes(helloResult));
      if (helloResult.containsKey("connectionId")) {
         ConnectionId newConnectionId = connectionDescription.getConnectionId().withServerValue(helloResult.getNumber("connectionId").intValue());
         connectionDescription = connectionDescription.withConnectionId(newConnectionId);
      }

      if (clusterConnectionMode == ClusterConnectionMode.LOAD_BALANCED) {
         ObjectId serviceId = getServiceId(helloResult);
         if (serviceId == null) {
            throw new MongoClientException("Driver attempted to initialize in load balancing mode, but the server does not support this mode");
         }

         connectionDescription = connectionDescription.withServiceId(serviceId);
      }

      return connectionDescription;
   }

   public static ServerDescription createServerDescription(ServerAddress serverAddress, BsonDocument helloResult, long roundTripTime) {
      return ServerDescription.builder().state(ServerConnectionState.CONNECTED).address(serverAddress).type(getServerType(helloResult)).canonicalAddress(helloResult.containsKey("me") ? helloResult.getString("me").getValue() : null).hosts(listToSet(helloResult.getArray("hosts", new BsonArray()))).passives(listToSet(helloResult.getArray("passives", new BsonArray()))).arbiters(listToSet(helloResult.getArray("arbiters", new BsonArray()))).primary(getString(helloResult, "primary")).maxDocumentSize(getMaxBsonObjectSize(helloResult)).tagSet(getTagSetFromDocument(helloResult.getDocument("tags", new BsonDocument()))).setName(getString(helloResult, "setName")).minWireVersion(getMinWireVersion(helloResult)).maxWireVersion(getMaxWireVersion(helloResult)).electionId(getElectionId(helloResult)).setVersion(getSetVersion(helloResult)).topologyVersion(getTopologyVersion(helloResult)).lastWriteDate(getLastWriteDate(helloResult)).roundTripTime(roundTripTime, TimeUnit.NANOSECONDS).logicalSessionTimeoutMinutes(getLogicalSessionTimeoutMinutes(helloResult)).helloOk(helloResult.getBoolean("helloOk", BsonBoolean.FALSE).getValue()).ok(CommandHelper.isCommandOk(helloResult)).build();
   }

   private static int getMinWireVersion(BsonDocument helloResult) {
      return helloResult.getInt32("minWireVersion", new BsonInt32(ServerDescription.getDefaultMinWireVersion())).getValue();
   }

   private static int getMaxWireVersion(BsonDocument helloResult) {
      return helloResult.getInt32("maxWireVersion", new BsonInt32(ServerDescription.getDefaultMaxWireVersion())).getValue();
   }

   @Nullable
   private static Date getLastWriteDate(BsonDocument helloResult) {
      return !helloResult.containsKey("lastWrite") ? null : new Date(helloResult.getDocument("lastWrite").getDateTime("lastWriteDate").getValue());
   }

   @Nullable
   private static ObjectId getElectionId(BsonDocument helloResult) {
      return helloResult.containsKey("electionId") ? helloResult.getObjectId("electionId").getValue() : null;
   }

   @Nullable
   private static Integer getSetVersion(BsonDocument helloResult) {
      return helloResult.containsKey("setVersion") ? helloResult.getNumber("setVersion").intValue() : null;
   }

   @Nullable
   private static TopologyVersion getTopologyVersion(BsonDocument helloResult) {
      return helloResult.containsKey("topologyVersion") && helloResult.get("topologyVersion").isDocument() ? new TopologyVersion(helloResult.getDocument("topologyVersion")) : null;
   }

   @Nullable
   private static ObjectId getServiceId(BsonDocument helloResult) {
      return helloResult.containsKey("serviceId") && helloResult.get("serviceId").isObjectId() ? helloResult.getObjectId("serviceId").getValue() : null;
   }

   private static int getMaxMessageSizeBytes(BsonDocument helloResult) {
      return helloResult.getInt32("maxMessageSizeBytes", new BsonInt32(ConnectionDescription.getDefaultMaxMessageSize())).getValue();
   }

   private static int getMaxBsonObjectSize(BsonDocument helloResult) {
      return helloResult.getInt32("maxBsonObjectSize", new BsonInt32(ServerDescription.getDefaultMaxDocumentSize())).getValue();
   }

   private static int getMaxWriteBatchSize(BsonDocument helloResult) {
      return helloResult.getInt32("maxWriteBatchSize", new BsonInt32(ConnectionDescription.getDefaultMaxWriteBatchSize())).getValue();
   }

   @Nullable
   private static Integer getLogicalSessionTimeoutMinutes(BsonDocument helloResult) {
      return helloResult.isNumber("logicalSessionTimeoutMinutes") ? helloResult.getNumber("logicalSessionTimeoutMinutes").intValue() : null;
   }

   @Nullable
   private static String getString(BsonDocument response, String key) {
      return response.containsKey(key) ? response.getString(key).getValue() : null;
   }

   private static Set<String> listToSet(@Nullable BsonArray array) {
      if (array != null && !array.isEmpty()) {
         Set<String> set = new HashSet();
         Iterator var2 = array.iterator();

         while(var2.hasNext()) {
            BsonValue value = (BsonValue)var2.next();
            set.add(value.asString().getValue());
         }

         return set;
      } else {
         return Collections.emptySet();
      }
   }

   private static ServerType getServerType(BsonDocument helloResult) {
      if (!CommandHelper.isCommandOk(helloResult)) {
         return ServerType.UNKNOWN;
      } else if (isReplicaSetMember(helloResult)) {
         if (helloResult.getBoolean("hidden", BsonBoolean.FALSE).getValue()) {
            return ServerType.REPLICA_SET_OTHER;
         } else if (helloResult.getBoolean("isWritablePrimary", BsonBoolean.FALSE).getValue()) {
            return ServerType.REPLICA_SET_PRIMARY;
         } else if (helloResult.getBoolean(CommandHelper.LEGACY_HELLO_LOWER, BsonBoolean.FALSE).getValue()) {
            return ServerType.REPLICA_SET_PRIMARY;
         } else if (helloResult.getBoolean("secondary", BsonBoolean.FALSE).getValue()) {
            return ServerType.REPLICA_SET_SECONDARY;
         } else if (helloResult.getBoolean("arbiterOnly", BsonBoolean.FALSE).getValue()) {
            return ServerType.REPLICA_SET_ARBITER;
         } else {
            return helloResult.containsKey("setName") && helloResult.containsKey("hosts") ? ServerType.REPLICA_SET_OTHER : ServerType.REPLICA_SET_GHOST;
         }
      } else {
         return helloResult.containsKey("msg") && helloResult.get("msg").equals(new BsonString("isdbgrid")) ? ServerType.SHARD_ROUTER : ServerType.STANDALONE;
      }
   }

   private static boolean isReplicaSetMember(BsonDocument helloResult) {
      return helloResult.containsKey("setName") || helloResult.getBoolean("isreplicaset", BsonBoolean.FALSE).getValue();
   }

   private static TagSet getTagSetFromDocument(BsonDocument tagsDocuments) {
      List<Tag> tagList = new ArrayList();
      Iterator var2 = tagsDocuments.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, BsonValue> curEntry = (Entry)var2.next();
         tagList.add(new Tag((String)curEntry.getKey(), ((BsonValue)curEntry.getValue()).asString().getValue()));
      }

      return new TagSet(tagList);
   }

   private static List<String> getCompressors(BsonDocument helloResult) {
      List<String> compressorList = new ArrayList();
      Iterator var2 = helloResult.getArray("compression", new BsonArray()).iterator();

      while(var2.hasNext()) {
         BsonValue compressor = (BsonValue)var2.next();
         compressorList.add(compressor.asString().getValue());
      }

      return compressorList;
   }

   private DescriptionHelper() {
   }
}
