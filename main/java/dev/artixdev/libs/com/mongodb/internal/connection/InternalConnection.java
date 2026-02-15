package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.connection.BufferProvider;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public interface InternalConnection extends BufferProvider {
   int NOT_INITIALIZED_GENERATION = -1;

   ConnectionDescription getDescription();

   ServerDescription getInitialServerDescription();

   void open();

   void openAsync(SingleResultCallback<Void> var1);

   void close();

   boolean opened();

   boolean isClosed();

   int getGeneration();

   @Nullable
   <T> T sendAndReceive(CommandMessage var1, Decoder<T> var2, SessionContext var3, RequestContext var4, OperationContext var5);

   <T> void send(CommandMessage var1, Decoder<T> var2, SessionContext var3);

   <T> T receive(Decoder<T> var1, SessionContext var2);

   default boolean supportsAdditionalTimeout() {
      return false;
   }

   default <T> T receive(Decoder<T> decoder, SessionContext sessionContext, int additionalTimeout) {
      throw new UnsupportedOperationException();
   }

   boolean hasMoreToCome();

   <T> void sendAndReceiveAsync(CommandMessage var1, Decoder<T> var2, SessionContext var3, RequestContext var4, OperationContext var5, SingleResultCallback<T> var6);

   void sendMessage(List<ByteBuf> var1, int var2);

   ResponseBuffers receiveMessage(int var1);

   void sendMessageAsync(List<ByteBuf> var1, int var2, SingleResultCallback<Void> var3);

   void receiveMessageAsync(int var1, SingleResultCallback<ResponseBuffers> var2);

   default void markAsPinned(Connection.PinningMode pinningMode) {
   }
}
