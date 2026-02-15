package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.MongoSocketOpenException;
import dev.artixdev.libs.com.mongodb.MongoSocketReadException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.AsyncCompletionHandler;
import dev.artixdev.libs.com.mongodb.connection.BufferProvider;
import dev.artixdev.libs.com.mongodb.connection.ProxySettings;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;
import dev.artixdev.libs.com.mongodb.connection.Stream;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.org.bson.ByteBuf;

public class SocketStream implements Stream {
   private final ServerAddress address;
   private final SocketSettings settings;
   private final SslSettings sslSettings;
   private final SocketFactory socketFactory;
   private final BufferProvider bufferProvider;
   private volatile Socket socket;
   private volatile OutputStream outputStream;
   private volatile InputStream inputStream;
   private volatile boolean isClosed;

   public SocketStream(ServerAddress address, SocketSettings settings, SslSettings sslSettings, SocketFactory socketFactory, BufferProvider bufferProvider) {
      this.address = (ServerAddress)Assertions.notNull("address", address);
      this.settings = (SocketSettings)Assertions.notNull("settings", settings);
      this.sslSettings = (SslSettings)Assertions.notNull("sslSettings", sslSettings);
      this.socketFactory = (SocketFactory)Assertions.notNull("socketFactory", socketFactory);
      this.bufferProvider = (BufferProvider)Assertions.notNull("bufferProvider", bufferProvider);
   }

   public void open() {
      try {
         this.socket = this.initializeSocket();
         this.outputStream = this.socket.getOutputStream();
         this.inputStream = this.socket.getInputStream();
      } catch (IOException e) {
         this.close();
         throw (MongoInterruptedException)InterruptionUtil.translateInterruptedException(e, "Interrupted while connecting").orElseThrow(() -> {
            return new MongoSocketOpenException("Exception opening socket", this.getAddress(), e);
         });
      }
   }

   protected Socket initializeSocket() throws IOException {
      ProxySettings proxySettings = this.settings.getProxySettings();
      if (proxySettings.isProxyEnabled()) {
         if (this.sslSettings.isEnabled()) {
            Assertions.assertTrue(this.socketFactory instanceof SSLSocketFactory);
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory)this.socketFactory;
            return this.initializeSslSocketOverSocksProxy(sslSocketFactory);
         } else {
            return this.initializeSocketOverSocksProxy();
         }
      } else {
         Iterator inetSocketAddresses = this.address.getSocketAddresses().iterator();

         while(true) {
            if (inetSocketAddresses.hasNext()) {
               Socket socket = this.socketFactory.createSocket();

               try {
                  SocketStreamHelper.initialize(socket, (InetSocketAddress)inetSocketAddresses.next(), this.settings, this.sslSettings);
                  return socket;
               } catch (SocketTimeoutException e) {
                  if (inetSocketAddresses.hasNext()) {
                     continue;
                  }

                  throw e;
               }
            }

            throw new MongoSocketException("Exception opening socket", this.getAddress());
         }
      }
   }

   private SSLSocket initializeSslSocketOverSocksProxy(SSLSocketFactory sslSocketFactory) throws IOException {
      String serverHost = this.address.getHost();
      int serverPort = this.address.getPort();
      SocksSocket socksProxy = new SocksSocket(this.settings.getProxySettings());
      SocketStreamHelper.configureSocket(socksProxy, this.settings);
      InetSocketAddress inetSocketAddress = toSocketAddress(serverHost, serverPort);
      socksProxy.connect(inetSocketAddress, this.settings.getConnectTimeout(TimeUnit.MILLISECONDS));
      SSLSocket sslSocket = (SSLSocket)sslSocketFactory.createSocket(socksProxy, serverHost, serverPort, true);
      SslHelper.configureSslSocket(sslSocket, this.sslSettings, inetSocketAddress);
      return sslSocket;
   }

   private static InetSocketAddress toSocketAddress(String serverHost, int serverPort) {
      return InetSocketAddress.createUnresolved(serverHost, serverPort);
   }

   private Socket initializeSocketOverSocksProxy() throws IOException {
      Socket createdSocket = this.socketFactory.createSocket();
      SocketStreamHelper.configureSocket(createdSocket, this.settings);
      SocksSocket socksProxy = new SocksSocket(createdSocket, this.settings.getProxySettings());
      socksProxy.connect(toSocketAddress(this.address.getHost(), this.address.getPort()), this.settings.getConnectTimeout(TimeUnit.MILLISECONDS));
      return socksProxy;
   }

   public ByteBuf getBuffer(int size) {
      return this.bufferProvider.getBuffer(size);
   }

   public void write(List<ByteBuf> buffers) throws IOException {
      Iterator var2 = buffers.iterator();

      while(var2.hasNext()) {
         ByteBuf cur = (ByteBuf)var2.next();
         this.outputStream.write(cur.array(), 0, cur.limit());
      }

   }

   public ByteBuf read(int numBytes) throws IOException {
      ByteBuf buffer = this.bufferProvider.getBuffer(numBytes);

      try {
         int totalBytesRead = 0;

         int bytesRead;
         for(byte[] bytes = buffer.array(); totalBytesRead < buffer.limit(); totalBytesRead += bytesRead) {
            bytesRead = this.inputStream.read(bytes, totalBytesRead, buffer.limit() - totalBytesRead);
            if (bytesRead == -1) {
               throw new MongoSocketReadException("Prematurely reached end of stream", this.getAddress());
            }
         }

         return buffer;
      } catch (Exception e) {
         buffer.release();
         throw e;
      }
   }

   public boolean supportsAdditionalTimeout() {
      return true;
   }

   public ByteBuf read(int numBytes, int additionalTimeout) throws IOException {
      int curTimeout = this.socket.getSoTimeout();
      if (curTimeout > 0 && additionalTimeout > 0) {
         this.socket.setSoTimeout(curTimeout + additionalTimeout);
      }

      ByteBuf var4;
      try {
         var4 = this.read(numBytes);
      } finally {
         if (!this.socket.isClosed()) {
            this.socket.setSoTimeout(curTimeout);
         }

      }

      return var4;
   }

   public void openAsync(AsyncCompletionHandler<Void> handler) {
      throw new UnsupportedOperationException(this.getClass() + " does not support asynchronous operations.");
   }

   public void writeAsync(List<ByteBuf> buffers, AsyncCompletionHandler<Void> handler) {
      throw new UnsupportedOperationException(this.getClass() + " does not support asynchronous operations.");
   }

   public void readAsync(int numBytes, AsyncCompletionHandler<ByteBuf> handler) {
      throw new UnsupportedOperationException(this.getClass() + " does not support asynchronous operations.");
   }

   public ServerAddress getAddress() {
      return this.address;
   }

   SocketSettings getSettings() {
      return this.settings;
   }

   public void close() {
      try {
         this.isClosed = true;
         if (this.socket != null) {
            this.socket.close();
         }
      } catch (RuntimeException | IOException ignored) {
      }

   }

   public boolean isClosed() {
      return this.isClosed;
   }
}
