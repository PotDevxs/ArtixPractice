package dev.artixdev.libs.com.mongodb.client.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.connection.SslHelper;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;

class KeyManagementService {
   private static final Logger LOGGER = Loggers.getLogger("client");
   private final Map<String, SSLContext> kmsProviderSslContextMap;
   private final int timeoutMillis;

   KeyManagementService(Map<String, SSLContext> kmsProviderSslContextMap, int timeoutMillis) {
      this.kmsProviderSslContextMap = (Map)Assertions.notNull("kmsProviderSslContextMap", kmsProviderSslContextMap);
      this.timeoutMillis = timeoutMillis;
   }

   public InputStream stream(String kmsProvider, String host, ByteBuffer message) throws IOException {
      ServerAddress serverAddress = new ServerAddress(host);
      LOGGER.info("Connecting to KMS server at " + serverAddress);
      SSLContext sslContext = (SSLContext)this.kmsProviderSslContextMap.get(kmsProvider);
      SocketFactory sslSocketFactory = sslContext == null ? SSLSocketFactory.getDefault() : sslContext.getSocketFactory();
      SSLSocket socket = (SSLSocket)((SocketFactory)sslSocketFactory).createSocket();
      this.enableHostNameVerification(socket);

      try {
         socket.setSoTimeout(this.timeoutMillis);
         socket.connect(new InetSocketAddress(InetAddress.getByName(serverAddress.getHost()), serverAddress.getPort()), this.timeoutMillis);
      } catch (IOException e) {
         this.closeSocket(socket);
         throw e;
      }

      try {
         OutputStream outputStream = socket.getOutputStream();
         byte[] bytes = new byte[message.remaining()];
         message.get(bytes);
         outputStream.write(bytes);
      } catch (IOException e) {
         this.closeSocket(socket);
         throw e;
      }

      try {
         return socket.getInputStream();
      } catch (IOException e) {
         this.closeSocket(socket);
         throw e;
      }
   }

   private void enableHostNameVerification(SSLSocket socket) {
      SSLParameters sslParameters = socket.getSSLParameters();
      if (sslParameters == null) {
         sslParameters = new SSLParameters();
      }

      SslHelper.enableHostNameVerification(sslParameters);
      socket.setSSLParameters(sslParameters);
   }

   private void closeSocket(Socket socket) {
      try {
         socket.close();
      } catch (RuntimeException | IOException ignored) {
      }

   }
}
