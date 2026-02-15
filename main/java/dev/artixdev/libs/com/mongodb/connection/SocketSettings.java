package dev.artixdev.libs.com.mongodb.connection;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.Block;
import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class SocketSettings {
   private final long connectTimeoutMS;
   private final long readTimeoutMS;
   private final int receiveBufferSize;
   private final int sendBufferSize;
   private final ProxySettings proxySettings;

   public static SocketSettings.Builder builder() {
      return new SocketSettings.Builder();
   }

   public static SocketSettings.Builder builder(SocketSettings socketSettings) {
      return builder().applySettings(socketSettings);
   }

   public int getConnectTimeout(TimeUnit timeUnit) {
      return (int)timeUnit.convert(this.connectTimeoutMS, TimeUnit.MILLISECONDS);
   }

   public int getReadTimeout(TimeUnit timeUnit) {
      return (int)timeUnit.convert(this.readTimeoutMS, TimeUnit.MILLISECONDS);
   }

   public ProxySettings getProxySettings() {
      return this.proxySettings;
   }

   public int getReceiveBufferSize() {
      return this.receiveBufferSize;
   }

   public int getSendBufferSize() {
      return this.sendBufferSize;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SocketSettings that = (SocketSettings)o;
         if (this.connectTimeoutMS != that.connectTimeoutMS) {
            return false;
         } else if (this.readTimeoutMS != that.readTimeoutMS) {
            return false;
         } else if (this.receiveBufferSize != that.receiveBufferSize) {
            return false;
         } else {
            return this.sendBufferSize != that.sendBufferSize ? false : this.proxySettings.equals(that.proxySettings);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = (int)(this.connectTimeoutMS ^ this.connectTimeoutMS >>> 32);
      result = 31 * result + (int)(this.readTimeoutMS ^ this.readTimeoutMS >>> 32);
      result = 31 * result + this.receiveBufferSize;
      result = 31 * result + this.sendBufferSize;
      result = 31 * result + this.proxySettings.hashCode();
      return result;
   }

   public String toString() {
      return "SocketSettings{connectTimeoutMS=" + this.connectTimeoutMS + ", readTimeoutMS=" + this.readTimeoutMS + ", receiveBufferSize=" + this.receiveBufferSize + ", proxySettings=" + this.proxySettings + '}';
   }

   private SocketSettings(SocketSettings.Builder builder) {
      this.connectTimeoutMS = builder.connectTimeoutMS;
      this.readTimeoutMS = builder.readTimeoutMS;
      this.receiveBufferSize = builder.receiveBufferSize;
      this.sendBufferSize = builder.sendBufferSize;
      this.proxySettings = builder.proxySettingsBuilder.build();
   }

   // $FF: synthetic method
   SocketSettings(SocketSettings.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private long connectTimeoutMS;
      private long readTimeoutMS;
      private int receiveBufferSize;
      private int sendBufferSize;
      private ProxySettings.Builder proxySettingsBuilder;

      private Builder() {
         this.connectTimeoutMS = 10000L;
         this.proxySettingsBuilder = ProxySettings.builder();
      }

      public SocketSettings.Builder applySettings(SocketSettings socketSettings) {
         Assertions.notNull("socketSettings", socketSettings);
         this.connectTimeoutMS = socketSettings.connectTimeoutMS;
         this.readTimeoutMS = socketSettings.readTimeoutMS;
         this.receiveBufferSize = socketSettings.receiveBufferSize;
         this.sendBufferSize = socketSettings.sendBufferSize;
         this.proxySettingsBuilder.applySettings(socketSettings.getProxySettings());
         return this;
      }

      public SocketSettings.Builder connectTimeout(int connectTimeout, TimeUnit timeUnit) {
         this.connectTimeoutMS = TimeUnit.MILLISECONDS.convert((long)connectTimeout, timeUnit);
         return this;
      }

      public SocketSettings.Builder readTimeout(int readTimeout, TimeUnit timeUnit) {
         this.readTimeoutMS = TimeUnit.MILLISECONDS.convert((long)readTimeout, timeUnit);
         return this;
      }

      public SocketSettings.Builder receiveBufferSize(int receiveBufferSize) {
         this.receiveBufferSize = receiveBufferSize;
         return this;
      }

      public SocketSettings.Builder sendBufferSize(int sendBufferSize) {
         this.sendBufferSize = sendBufferSize;
         return this;
      }

      public SocketSettings.Builder applyToProxySettings(Block<ProxySettings.Builder> block) {
         ((Block)Assertions.notNull("block", block)).apply(this.proxySettingsBuilder);
         return this;
      }

      public SocketSettings.Builder applyConnectionString(ConnectionString connectionString) {
         Integer connectTimeout = connectionString.getConnectTimeout();
         if (connectTimeout != null) {
            this.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
         }

         Integer socketTimeout = connectionString.getSocketTimeout();
         if (socketTimeout != null) {
            this.readTimeout(socketTimeout, TimeUnit.MILLISECONDS);
         }

         this.proxySettingsBuilder.applyConnectionString(connectionString);
         return this;
      }

      public SocketSettings build() {
         return new SocketSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
