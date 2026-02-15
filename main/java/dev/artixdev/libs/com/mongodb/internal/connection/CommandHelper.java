package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Locale;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.MongoServerException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.internal.IgnorableRequestContext;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;

public final class CommandHelper {
   static final String HELLO = "hello";
   static final String LEGACY_HELLO = "isMaster";
   static final String LEGACY_HELLO_LOWER;

   static BsonDocument executeCommand(String database, BsonDocument command, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi, InternalConnection internalConnection) {
      return sendAndReceive(database, command, (ClusterClock)null, clusterConnectionMode, serverApi, internalConnection);
   }

   public static BsonDocument executeCommand(String database, BsonDocument command, ClusterClock clusterClock, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi, InternalConnection internalConnection) {
      return sendAndReceive(database, command, clusterClock, clusterConnectionMode, serverApi, internalConnection);
   }

   static BsonDocument executeCommandWithoutCheckingForFailure(String database, BsonDocument command, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi, InternalConnection internalConnection) {
      try {
         return sendAndReceive(database, command, (ClusterClock)null, clusterConnectionMode, serverApi, internalConnection);
      } catch (MongoServerException e) {
         return new BsonDocument();
      }
   }

   static void executeCommandAsync(String database, BsonDocument command, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi, InternalConnection internalConnection, SingleResultCallback<BsonDocument> callback) {
      internalConnection.sendAndReceiveAsync(getCommandMessage(database, command, internalConnection, clusterConnectionMode, serverApi), new BsonDocumentCodec(), NoOpSessionContext.INSTANCE, IgnorableRequestContext.INSTANCE, new OperationContext(), (result, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            callback.onResult(result, (Throwable)null);
         }

      });
   }

   static boolean isCommandOk(BsonDocument response) {
      if (!response.containsKey("ok")) {
         return false;
      } else {
         BsonValue okValue = response.get("ok");
         if (okValue.isBoolean()) {
            return okValue.asBoolean().getValue();
         } else if (okValue.isNumber()) {
            return okValue.asNumber().intValue() == 1;
         } else {
            return false;
         }
      }
   }

   private static BsonDocument sendAndReceive(String database, BsonDocument command, @Nullable ClusterClock clusterClock, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi, InternalConnection internalConnection) {
      SessionContext sessionContext = clusterClock == null ? NoOpSessionContext.INSTANCE : new ClusterClockAdvancingSessionContext(NoOpSessionContext.INSTANCE, clusterClock);
      return (BsonDocument)Assertions.assertNotNull((BsonDocument)internalConnection.sendAndReceive(getCommandMessage(database, command, internalConnection, clusterConnectionMode, serverApi), new BsonDocumentCodec(), (SessionContext)sessionContext, IgnorableRequestContext.INSTANCE, new OperationContext()));
   }

   private static CommandMessage getCommandMessage(String database, BsonDocument command, InternalConnection internalConnection, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      return new CommandMessage(new MongoNamespace(database, "$cmd"), command, new NoOpFieldNameValidator(), ReadPreference.primary(), MessageSettings.builder().maxWireVersion(internalConnection.getDescription().getMaxWireVersion()).serverType(internalConnection.getDescription().getServerType()).build(), clusterConnectionMode, serverApi);
   }

   private CommandHelper() {
   }

   static {
      LEGACY_HELLO_LOWER = "isMaster".toLowerCase(Locale.ROOT);
   }
}
