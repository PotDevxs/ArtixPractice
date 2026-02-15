package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.BindingContext;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class DefaultServerConnection extends AbstractReferenceCounted implements AsyncConnection, Connection {
   private static final Logger LOGGER = Loggers.getLogger("connection");
   private final InternalConnection wrapped;
   private final ProtocolExecutor protocolExecutor;
   private final ClusterConnectionMode clusterConnectionMode;

   public DefaultServerConnection(InternalConnection wrapped, ProtocolExecutor protocolExecutor, ClusterConnectionMode clusterConnectionMode) {
      this.wrapped = wrapped;
      this.protocolExecutor = protocolExecutor;
      this.clusterConnectionMode = clusterConnectionMode;
   }

   public DefaultServerConnection retain() {
      super.retain();
      return this;
   }

   public int release() {
      int count = super.release();
      if (count == 0) {
         this.wrapped.close();
      }

      return count;
   }

   public ConnectionDescription getDescription() {
      return this.wrapped.getDescription();
   }

   @Nullable
   public <T> T command(String database, BsonDocument command, FieldNameValidator fieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context) {
      return this.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, context, true, (SplittablePayload)null, (FieldNameValidator)null);
   }

   @Nullable
   public <T> T command(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator) {
      return this.executeProtocol(new CommandProtocolImpl<T>(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, responseExpected, payload, payloadFieldNameValidator, this.clusterConnectionMode, context.getServerApi(), context.getRequestContext(), context.getOperationContext()), context.getSessionContext());
   }

   public <T> void commandAsync(String database, BsonDocument command, FieldNameValidator fieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context, SingleResultCallback<T> callback) {
      this.commandAsync(database, command, fieldNameValidator, readPreference, commandResultDecoder, context, true, (SplittablePayload)null, (FieldNameValidator)null, callback);
   }

   public <T> void commandAsync(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator, SingleResultCallback<T> callback) {
      this.executeProtocolAsync(new CommandProtocolImpl<T>(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, responseExpected, payload, payloadFieldNameValidator, this.clusterConnectionMode, context.getServerApi(), context.getRequestContext(), context.getOperationContext()), context.getSessionContext(), callback);
   }

   public void markAsPinned(Connection.PinningMode pinningMode) {
      this.wrapped.markAsPinned(pinningMode);
   }

   @Nullable
   private <T> T executeProtocol(CommandProtocol<T> protocol, SessionContext sessionContext) {
      return this.protocolExecutor.execute(protocol, this.wrapped, sessionContext);
   }

   private <T> void executeProtocolAsync(CommandProtocol<T> protocol, SessionContext sessionContext, SingleResultCallback<T> callback) {
      SingleResultCallback errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER);

      try {
         this.protocolExecutor.executeAsync(protocol, this.wrapped, sessionContext, errHandlingCallback);
      } catch (Throwable throwable) {
         errHandlingCallback.onResult(null, throwable);
      }

   }
}
