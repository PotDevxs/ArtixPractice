package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketOption;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;

final class SocketStreamHelper {
   private static final String TCP_KEEPIDLE = "TCP_KEEPIDLE";
   private static final int TCP_KEEPIDLE_DURATION = 120;
   private static final String TCP_KEEPCOUNT = "TCP_KEEPCOUNT";
   private static final int TCP_KEEPCOUNT_LIMIT = 9;
   private static final String TCP_KEEPINTERVAL = "TCP_KEEPINTERVAL";
   private static final int TCP_KEEPINTERVAL_DURATION = 10;
   private static final SocketOption<Integer> KEEP_COUNT_OPTION;
   private static final SocketOption<Integer> KEEP_IDLE_OPTION;
   private static final SocketOption<Integer> KEEP_INTERVAL_OPTION;
   private static final Method SET_OPTION_METHOD;

   static void initialize(Socket socket, InetSocketAddress inetSocketAddress, SocketSettings settings, SslSettings sslSettings) throws IOException {
      configureSocket(socket, settings);
      SslHelper.configureSslSocket(socket, sslSettings, inetSocketAddress);
      socket.connect(inetSocketAddress, settings.getConnectTimeout(TimeUnit.MILLISECONDS));
   }

   static void configureSocket(Socket socket, SocketSettings settings) throws SocketException {
      socket.setTcpNoDelay(true);
      socket.setSoTimeout(settings.getReadTimeout(TimeUnit.MILLISECONDS));
      socket.setKeepAlive(true);
      setExtendedSocketOptions(socket);
      if (settings.getReceiveBufferSize() > 0) {
         socket.setReceiveBufferSize(settings.getReceiveBufferSize());
      }

      if (settings.getSendBufferSize() > 0) {
         socket.setSendBufferSize(settings.getSendBufferSize());
      }

   }

   static void setExtendedSocketOptions(Socket socket) {
      try {
         if (SET_OPTION_METHOD != null) {
            if (KEEP_COUNT_OPTION != null) {
               SET_OPTION_METHOD.invoke(socket, KEEP_COUNT_OPTION, 9);
            }

            if (KEEP_IDLE_OPTION != null) {
               SET_OPTION_METHOD.invoke(socket, KEEP_IDLE_OPTION, 120);
            }

            if (KEEP_INTERVAL_OPTION != null) {
               SET_OPTION_METHOD.invoke(socket, KEEP_INTERVAL_OPTION, 10);
            }
         }
      } catch (InvocationTargetException | IllegalAccessException ignored) {
      }

   }

   private SocketStreamHelper() {
   }

   static {
      SocketOption<Integer> keepCountOption = null;
      SocketOption<Integer> keepIdleOption = null;
      SocketOption<Integer> keepIntervalOption = null;
      Method setOptionMethod = null;

      try {
         setOptionMethod = Socket.class.getMethod("setOption", SocketOption.class, Object.class);
         Class extendedSocketOptionsClass = Class.forName("jdk.net.ExtendedSocketOptions");
         keepCountOption = (SocketOption)extendedSocketOptionsClass.getDeclaredField("TCP_KEEPCOUNT").get((Object)null);
         keepIdleOption = (SocketOption)extendedSocketOptionsClass.getDeclaredField("TCP_KEEPIDLE").get((Object)null);
         keepIntervalOption = (SocketOption)extendedSocketOptionsClass.getDeclaredField("TCP_KEEPINTERVAL").get((Object)null);
      } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | ClassNotFoundException ignored) {
      }

      KEEP_COUNT_OPTION = keepCountOption;
      KEEP_IDLE_OPTION = keepIdleOption;
      KEEP_INTERVAL_OPTION = keepIntervalOption;
      SET_OPTION_METHOD = setOptionMethod;
   }
}
