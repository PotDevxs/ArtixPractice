package dev.artixdev.libs.com.mongodb.internal.connection;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;

public final class SslHelper {
   public static void enableHostNameVerification(SSLParameters sslParameters) {
      sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
   }

   public static void enableSni(String host, SSLParameters sslParameters) {
      try {
         SNIServerName sniHostName = new SNIHostName(host);
         sslParameters.setServerNames(Collections.singletonList(sniHostName));
      } catch (IllegalArgumentException ignored) {
      }

   }

   public static void configureSslSocket(Socket socket, SslSettings sslSettings, InetSocketAddress inetSocketAddress) throws MongoInternalException {
      if (sslSettings.isEnabled() || socket instanceof SSLSocket) {
         if (!(socket instanceof SSLSocket)) {
            throw new MongoInternalException("SSL is enabled but the socket is not an instance of javax.net.ssl.SSLSocket");
         }

         SSLSocket sslSocket = (SSLSocket)socket;
         SSLParameters sslParameters = sslSocket.getSSLParameters();
         if (sslParameters == null) {
            sslParameters = new SSLParameters();
         }

         enableSni(inetSocketAddress.getHostName(), sslParameters);
         if (!sslSettings.isInvalidHostNameAllowed()) {
            enableHostNameVerification(sslParameters);
         }

         sslSocket.setSSLParameters(sslParameters);
      }

   }

   private SslHelper() {
   }
}
