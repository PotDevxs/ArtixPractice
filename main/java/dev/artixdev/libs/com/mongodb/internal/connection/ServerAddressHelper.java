package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.UnixServerAddress;

public final class ServerAddressHelper {
   public static ServerAddress createServerAddress(String host) {
      return createServerAddress(host, ServerAddress.defaultPort());
   }

   public static ServerAddress createServerAddress(String host, int port) {
      return (ServerAddress)(host != null && host.endsWith(".sock") ? new UnixServerAddress(host) : new ServerAddress(host, port));
   }

   private ServerAddressHelper() {
   }
}
