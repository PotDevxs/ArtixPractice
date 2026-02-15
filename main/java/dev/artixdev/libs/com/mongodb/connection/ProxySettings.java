package dev.artixdev.libs.com.mongodb.connection;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public final class ProxySettings {
   private static final int DEFAULT_PORT = 1080;
   @Nullable
   private final String host;
   @Nullable
   private final Integer port;
   @Nullable
   private final String username;
   @Nullable
   private final String password;

   public static ProxySettings.Builder builder() {
      return new ProxySettings.Builder();
   }

   public static ProxySettings.Builder builder(ProxySettings proxySettings) {
      return builder().applySettings(proxySettings);
   }

   @Nullable
   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port != null ? this.port : 1080;
   }

   @Nullable
   public String getUsername() {
      return this.username;
   }

   @Nullable
   public String getPassword() {
      return this.password;
   }

   public boolean isProxyEnabled() {
      return this.host != null;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ProxySettings that = (ProxySettings)o;
         return Objects.equals(this.host, that.host) && Objects.equals(this.port, that.port) && Objects.equals(this.username, that.username) && Objects.equals(this.password, that.password);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.host, this.port, this.username, this.password});
   }

   public String toString() {
      return "ProxySettings{host=" + this.host + ", port=" + this.port + ", username=" + this.username + ", password=" + this.password + '}';
   }

   private ProxySettings(ProxySettings.Builder builder) {
      if (builder.host == null) {
         Assertions.isTrue("proxyPort can only be specified with proxyHost", builder.port == null);
         Assertions.isTrue("proxyPassword can only be specified with proxyHost", builder.password == null);
         Assertions.isTrue("proxyUsername can only be specified with proxyHost", builder.username == null);
      }

      Assertions.isTrue("Both proxyUsername and proxyPassword must be set together. They cannot be set individually", builder.username == null == (builder.password == null));
      this.host = builder.host;
      this.port = builder.port;
      this.username = builder.username;
      this.password = builder.password;
   }

   // $FF: synthetic method
   ProxySettings(ProxySettings.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private String host;
      private Integer port;
      private String username;
      private String password;

      private Builder() {
      }

      public ProxySettings.Builder applySettings(ProxySettings proxySettings) {
         Assertions.notNull("ProxySettings", proxySettings);
         this.host = proxySettings.host;
         this.port = proxySettings.port;
         this.username = proxySettings.username;
         this.password = proxySettings.password;
         return this;
      }

      public ProxySettings.Builder host(String host) {
         Assertions.notNull("proxyHost", host);
         Assertions.isTrueArgument("proxyHost is not empty", host.trim().length() > 0);
         this.host = host;
         return this;
      }

      public ProxySettings.Builder port(int port) {
         Assertions.isTrueArgument("proxyPort is within the valid range (0 to 65535)", port >= 0 && port <= 65535);
         this.port = port;
         return this;
      }

      public ProxySettings.Builder username(String username) {
         Assertions.notNull("username", username);
         Assertions.isTrueArgument("username is not empty", !username.isEmpty());
         Assertions.isTrueArgument("username's length in bytes is not greater than 255", username.getBytes(StandardCharsets.UTF_8).length <= 255);
         this.username = username;
         return this;
      }

      public ProxySettings.Builder password(String password) {
         Assertions.notNull("password", password);
         Assertions.isTrueArgument("password is not empty", !password.isEmpty());
         Assertions.isTrueArgument("password's length in bytes is not greater than 255", password.getBytes(StandardCharsets.UTF_8).length <= 255);
         this.password = password;
         return this;
      }

      public ProxySettings.Builder applyConnectionString(ConnectionString connectionString) {
         String proxyHost = connectionString.getProxyHost();
         if (proxyHost != null) {
            this.host(proxyHost);
         }

         Integer proxyPort = connectionString.getProxyPort();
         if (proxyPort != null) {
            this.port(proxyPort);
         }

         String proxyUsername = connectionString.getProxyUsername();
         if (proxyUsername != null) {
            this.username(proxyUsername);
         }

         String proxyPassword = connectionString.getProxyPassword();
         if (proxyPassword != null) {
            this.password(proxyPassword);
         }

         return this;
      }

      public ProxySettings build() {
         return new ProxySettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
