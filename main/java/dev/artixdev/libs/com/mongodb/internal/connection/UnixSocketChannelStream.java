package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.net.Socket;
import javax.net.SocketFactory;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;
import dev.artixdev.libs.com.mongodb.UnixServerAddress;
import dev.artixdev.libs.com.mongodb.connection.BufferProvider;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;

public class UnixSocketChannelStream extends SocketStream {
   private final UnixServerAddress address;

   public UnixSocketChannelStream(UnixServerAddress address, SocketSettings settings, SslSettings sslSettings, BufferProvider bufferProvider) {
      super(address, settings, sslSettings, SocketFactory.getDefault(), bufferProvider);
      this.address = address;
   }

   protected Socket initializeSocket() throws IOException {
      return UnixSocketChannel.open((UnixSocketAddress)this.address.getUnixSocketAddress()).socket();
   }
}
