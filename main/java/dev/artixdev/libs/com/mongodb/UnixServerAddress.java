package dev.artixdev.libs.com.mongodb;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import jnr.unixsocket.UnixSocketAddress;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class UnixServerAddress extends ServerAddress {
   private static final long serialVersionUID = 154466643544866543L;

   public UnixServerAddress(String path) {
      super((String)Assertions.notNull("The path cannot be null", path));
      Assertions.isTrueArgument("The path must end in .sock", path.endsWith(".sock"));
   }

   /** @deprecated */
   @Deprecated
   public InetSocketAddress getSocketAddress() {
      throw new UnsupportedOperationException("Cannot return a InetSocketAddress from a UnixServerAddress");
   }

   /** @deprecated */
   @Deprecated
   public SocketAddress getUnixSocketAddress() {
      return new UnixSocketAddress(this.getHost());
   }

   public String toString() {
      return this.getHost();
   }
}
