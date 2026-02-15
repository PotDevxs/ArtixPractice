package dev.artixdev.libs.com.mongodb;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public class ServerAddress implements Serializable {
   private static final long serialVersionUID = 4027873363095395504L;
   private final String host;
   private final int port;

   public ServerAddress() {
      this(defaultHost(), defaultPort());
   }

   public ServerAddress(@Nullable String host) {
      this(host, defaultPort());
   }

   public ServerAddress(InetAddress inetAddress) {
      this(inetAddress.getHostName(), defaultPort());
   }

   public ServerAddress(InetAddress inetAddress, int port) {
      this(inetAddress.getHostName(), port);
   }

   public ServerAddress(InetSocketAddress inetSocketAddress) {
      this(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
   }

   public ServerAddress(@Nullable String host, int port) {
      String hostToUse = host;
      if (host == null) {
         hostToUse = defaultHost();
      }

      hostToUse = hostToUse.trim();
      if (hostToUse.length() == 0) {
         hostToUse = defaultHost();
      }

      int portToUse = port;
      int idx;
      int portIdx;
      if (hostToUse.startsWith("[")) {
         idx = host.indexOf("]");
         if (idx == -1) {
            throw new IllegalArgumentException("an IPV6 address must be encosed with '[' and ']' according to RFC 2732.");
         }

         portIdx = host.indexOf("]:");
         if (portIdx != -1) {
            if (port != defaultPort()) {
               throw new IllegalArgumentException("can't specify port in construct and via host");
            }

            portToUse = Integer.parseInt(host.substring(portIdx + 2));
         }

         hostToUse = host.substring(1, idx);
      } else {
         idx = hostToUse.indexOf(":");
         portIdx = hostToUse.lastIndexOf(":");
         if (idx == portIdx && idx > 0) {
            if (port != defaultPort()) {
               throw new IllegalArgumentException("can't specify port in construct and via host");
            }

            try {
               portToUse = Integer.parseInt(hostToUse.substring(idx + 1));
            } catch (NumberFormatException e) {
               throw new MongoException("host and port should be specified in host:port format");
            }

            hostToUse = hostToUse.substring(0, idx).trim();
         }
      }

      this.host = hostToUse.toLowerCase();
      this.port = portToUse;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ServerAddress that = (ServerAddress)o;
         if (this.port != that.port) {
            return false;
         } else {
            return this.host.equals(that.host);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.host.hashCode();
      result = 31 * result + this.port;
      return result;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   /** @deprecated */
   @Deprecated
   public InetSocketAddress getSocketAddress() {
      try {
         return new InetSocketAddress(InetAddress.getByName(this.host), this.port);
      } catch (UnknownHostException e) {
         throw new MongoSocketException(e.getMessage(), this, e);
      }
   }

   /** @deprecated */
   @Deprecated
   public List<InetSocketAddress> getSocketAddresses() {
      try {
         InetAddress[] inetAddresses = InetAddress.getAllByName(this.host);
         List<InetSocketAddress> inetSocketAddressList = new ArrayList();
         InetAddress[] var3 = inetAddresses;
         int var4 = inetAddresses.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            InetAddress inetAddress = var3[var5];
            inetSocketAddressList.add(new InetSocketAddress(inetAddress, this.port));
         }

         return inetSocketAddressList;
      } catch (UnknownHostException e) {
         throw new MongoSocketException(e.getMessage(), this, e);
      }
   }

   public String toString() {
      return this.host + ":" + this.port;
   }

   public static String defaultHost() {
      return "127.0.0.1";
   }

   public static int defaultPort() {
      return 27017;
   }
}
