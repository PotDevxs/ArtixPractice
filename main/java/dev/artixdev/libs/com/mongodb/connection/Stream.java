package dev.artixdev.libs.com.mongodb.connection;

import java.io.IOException;
import java.util.List;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.org.bson.ByteBuf;

/** @deprecated */
@Deprecated
public interface Stream extends BufferProvider {
   void open() throws IOException;

   void openAsync(AsyncCompletionHandler<Void> var1);

   void write(List<ByteBuf> var1) throws IOException;

   ByteBuf read(int var1) throws IOException;

   default boolean supportsAdditionalTimeout() {
      return false;
   }

   default ByteBuf read(int numBytes, int additionalTimeout) throws IOException {
      throw new UnsupportedOperationException();
   }

   void writeAsync(List<ByteBuf> var1, AsyncCompletionHandler<Void> var2);

   void readAsync(int var1, AsyncCompletionHandler<ByteBuf> var2);

   ServerAddress getAddress();

   void close();

   boolean isClosed();
}
