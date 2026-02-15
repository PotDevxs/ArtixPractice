package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;

class CommandProtocolImpl<T> implements CommandProtocol<T> {
   private final MongoNamespace namespace;
   private final BsonDocument command;
   private final SplittablePayload payload;
   private final ReadPreference readPreference;
   private final FieldNameValidator commandFieldNameValidator;
   private final FieldNameValidator payloadFieldNameValidator;
   private final Decoder<T> commandResultDecoder;
   private final boolean responseExpected;
   private final ClusterConnectionMode clusterConnectionMode;
   private final RequestContext requestContext;
   private SessionContext sessionContext;
   private final ServerApi serverApi;
   private final OperationContext operationContext;

   CommandProtocolImpl(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi, RequestContext requestContext, OperationContext operationContext) {
      Assertions.notNull("database", database);
      this.namespace = new MongoNamespace((String)Assertions.notNull("database", database), "$cmd");
      this.command = (BsonDocument)Assertions.notNull("command", command);
      this.commandFieldNameValidator = (FieldNameValidator)Assertions.notNull("commandFieldNameValidator", commandFieldNameValidator);
      this.readPreference = readPreference;
      this.commandResultDecoder = (Decoder)Assertions.notNull("commandResultDecoder", commandResultDecoder);
      this.responseExpected = responseExpected;
      this.payload = payload;
      this.payloadFieldNameValidator = payloadFieldNameValidator;
      this.clusterConnectionMode = (ClusterConnectionMode)Assertions.notNull("clusterConnectionMode", clusterConnectionMode);
      this.serverApi = serverApi;
      this.requestContext = (RequestContext)Assertions.notNull("requestContext", requestContext);
      this.operationContext = operationContext;
      Assertions.isTrueArgument("payloadFieldNameValidator cannot be null if there is a payload.", payload == null || payloadFieldNameValidator != null);
   }

   @Nullable
   public T execute(InternalConnection connection) {
      return connection.sendAndReceive(this.getCommandMessage(connection), this.commandResultDecoder, this.sessionContext, this.requestContext, this.operationContext);
   }

   public void executeAsync(InternalConnection connection, SingleResultCallback<T> callback) {
      try {
         connection.sendAndReceiveAsync(this.getCommandMessage(connection), this.commandResultDecoder, this.sessionContext, this.requestContext, this.operationContext, (result, t) -> {
            if (t != null) {
               callback.onResult(null, t);
            } else {
               callback.onResult(result, (Throwable)null);
            }

         });
      } catch (Throwable throwable) {
         callback.onResult(null, throwable);
      }

   }

   public CommandProtocolImpl<T> sessionContext(SessionContext sessionContext) {
      this.sessionContext = sessionContext;
      return this;
   }

   private CommandMessage getCommandMessage(InternalConnection connection) {
      return new CommandMessage(this.namespace, this.command, this.commandFieldNameValidator, this.readPreference, ProtocolHelper.getMessageSettings(connection.getDescription()), this.responseExpected, this.payload, this.payloadFieldNameValidator, this.clusterConnectionMode, this.serverApi);
   }
}
