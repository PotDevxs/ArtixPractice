package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.types.ObjectId;

class UsageTrackingInternalConnection implements InternalConnection {
   private static final Logger LOGGER = Loggers.getLogger("connection");
   private volatile long openedAt;
   private volatile long lastUsedAt;
   private volatile boolean closeSilently;
   private final InternalConnection wrapped;
   private final DefaultConnectionPool.ServiceStateManager serviceStateManager;

   UsageTrackingInternalConnection(InternalConnection wrapped, DefaultConnectionPool.ServiceStateManager serviceStateManager) {
      this.wrapped = wrapped;
      this.serviceStateManager = serviceStateManager;
      this.openedAt = Long.MAX_VALUE;
      this.lastUsedAt = this.openedAt;
   }

   public void open() {
      this.wrapped.open();
      this.openedAt = System.currentTimeMillis();
      this.lastUsedAt = this.openedAt;
      if (this.getDescription().getServiceId() != null) {
         this.serviceStateManager.addConnection((ObjectId)Assertions.assertNotNull(this.getDescription().getServiceId()));
      }

   }

   public void openAsync(SingleResultCallback<Void> callback) {
      this.wrapped.openAsync((result, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            this.openedAt = System.currentTimeMillis();
            this.lastUsedAt = this.openedAt;
            if (this.getDescription().getServiceId() != null) {
               this.serviceStateManager.addConnection(this.getDescription().getServiceId());
            }

            callback.onResult(null, (Throwable)null);
         }

      });
   }

   public void close() {
      try {
         this.wrapped.close();
      } finally {
         if (this.openedAt != Long.MAX_VALUE && this.getDescription().getServiceId() != null) {
            this.serviceStateManager.removeConnection((ObjectId)Assertions.assertNotNull(this.getDescription().getServiceId()));
         }

      }

   }

   public boolean opened() {
      return this.wrapped.opened();
   }

   public boolean isClosed() {
      return this.wrapped.isClosed();
   }

   public ByteBuf getBuffer(int size) {
      return this.wrapped.getBuffer(size);
   }

   public void sendMessage(List<ByteBuf> byteBuffers, int lastRequestId) {
      this.wrapped.sendMessage(byteBuffers, lastRequestId);
      this.lastUsedAt = System.currentTimeMillis();
   }

   public <T> T sendAndReceive(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, RequestContext requestContext, OperationContext operationContext) {
      T result = this.wrapped.sendAndReceive(message, decoder, sessionContext, requestContext, operationContext);
      this.lastUsedAt = System.currentTimeMillis();
      return result;
   }

   public <T> void send(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext) {
      this.wrapped.send(message, decoder, sessionContext);
      this.lastUsedAt = System.currentTimeMillis();
   }

   public <T> T receive(Decoder<T> decoder, SessionContext sessionContext) {
      T result = this.wrapped.receive(decoder, sessionContext);
      this.lastUsedAt = System.currentTimeMillis();
      return result;
   }

   public boolean supportsAdditionalTimeout() {
      return this.wrapped.supportsAdditionalTimeout();
   }

   public <T> T receive(Decoder<T> decoder, SessionContext sessionContext, int additionalTimeout) {
      T result = this.wrapped.receive(decoder, sessionContext, additionalTimeout);
      this.lastUsedAt = System.currentTimeMillis();
      return result;
   }

   public boolean hasMoreToCome() {
      return this.wrapped.hasMoreToCome();
   }

   public <T> void sendAndReceiveAsync(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, RequestContext requestContext, OperationContext operationContext, SingleResultCallback<T> callback) {
      SingleResultCallback<T> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
         this.lastUsedAt = System.currentTimeMillis();
         callback.onResult(result, t);
      }, LOGGER);
      this.wrapped.sendAndReceiveAsync(message, decoder, sessionContext, requestContext, operationContext, errHandlingCallback);
   }

   public ResponseBuffers receiveMessage(int responseTo) {
      ResponseBuffers responseBuffers = this.wrapped.receiveMessage(responseTo);
      this.lastUsedAt = System.currentTimeMillis();
      return responseBuffers;
   }

   public void sendMessageAsync(List<ByteBuf> byteBuffers, int lastRequestId, SingleResultCallback<Void> callback) {
      SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
         this.lastUsedAt = System.currentTimeMillis();
         callback.onResult(result, t);
      }, LOGGER);
      this.wrapped.sendMessageAsync(byteBuffers, lastRequestId, errHandlingCallback);
   }

   public void receiveMessageAsync(int responseTo, SingleResultCallback<ResponseBuffers> callback) {
      SingleResultCallback<ResponseBuffers> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
         this.lastUsedAt = System.currentTimeMillis();
         callback.onResult(result, t);
      }, LOGGER);
      this.wrapped.receiveMessageAsync(responseTo, errHandlingCallback);
   }

   public ConnectionDescription getDescription() {
      return this.wrapped.getDescription();
   }

   public ServerDescription getInitialServerDescription() {
      return this.wrapped.getInitialServerDescription();
   }

   public int getGeneration() {
      return this.wrapped.getGeneration();
   }

   long getOpenedAt() {
      return this.openedAt;
   }

   long getLastUsedAt() {
      return this.lastUsedAt;
   }

   void setCloseSilently() {
      this.closeSilently = true;
   }

   boolean isCloseSilently() {
      return this.closeSilently;
   }
}
