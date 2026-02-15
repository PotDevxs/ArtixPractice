package dev.artixdev.libs.com.mongodb.connection;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.UnixServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.connection.PowerOfTwoBufferPool;
import dev.artixdev.libs.com.mongodb.internal.connection.SocketStream;
import dev.artixdev.libs.com.mongodb.internal.connection.UnixSocketChannelStream;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

/** @deprecated */
@Deprecated
public class SocketStreamFactory implements StreamFactory {
   private final SocketSettings settings;
   private final SslSettings sslSettings;
   private final SocketFactory socketFactory;
   private final BufferProvider bufferProvider;

   public SocketStreamFactory(SocketSettings settings, SslSettings sslSettings) {
      this(settings, sslSettings, (SocketFactory)null);
   }

   public SocketStreamFactory(SocketSettings settings, SslSettings sslSettings, @Nullable SocketFactory socketFactory) {
      this.bufferProvider = PowerOfTwoBufferPool.DEFAULT;
      this.settings = (SocketSettings)Assertions.notNull("settings", settings);
      this.sslSettings = (SslSettings)Assertions.notNull("sslSettings", sslSettings);
      this.socketFactory = socketFactory;
   }

   public Stream create(ServerAddress serverAddress) {
      Object stream;
      if (serverAddress instanceof UnixServerAddress) {
         if (this.sslSettings.isEnabled()) {
            throw new MongoClientException("Socket based connections do not support ssl");
         }

         stream = new UnixSocketChannelStream((UnixServerAddress)serverAddress, this.settings, this.sslSettings, this.bufferProvider);
      } else if (this.socketFactory != null) {
         stream = new SocketStream(serverAddress, this.settings, this.sslSettings, this.socketFactory, this.bufferProvider);
      } else if (this.sslSettings.isEnabled()) {
         stream = new SocketStream(serverAddress, this.settings, this.sslSettings, this.getSslContext().getSocketFactory(), this.bufferProvider);
      } else {
         stream = new SocketStream(serverAddress, this.settings, this.sslSettings, SocketFactory.getDefault(), this.bufferProvider);
      }

      return (Stream)stream;
   }

   private SSLContext getSslContext() {
      try {
         return (SSLContext)Optional.ofNullable(this.sslSettings.getContext()).orElse(SSLContext.getDefault());
      } catch (NoSuchAlgorithmException e) {
         throw new MongoClientException("Unable to create default SSLContext", e);
      }
   }
}
