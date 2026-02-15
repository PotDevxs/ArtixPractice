package dev.artixdev.libs.com.mongodb.connection;

import java.util.Objects;
import javax.net.ssl.SSLContext;
import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public class SslSettings {
   private final boolean enabled;
   private final boolean invalidHostNameAllowed;
   private final SSLContext context;

   public static SslSettings.Builder builder() {
      return new SslSettings.Builder();
   }

   public static SslSettings.Builder builder(SslSettings sslSettings) {
      return builder().applySettings(sslSettings);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isInvalidHostNameAllowed() {
      return this.invalidHostNameAllowed;
   }

   @Nullable
   public SSLContext getContext() {
      return this.context;
   }

   SslSettings(SslSettings.Builder builder) {
      this.enabled = builder.enabled;
      this.invalidHostNameAllowed = builder.invalidHostNameAllowed;
      this.context = builder.context;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SslSettings that = (SslSettings)o;
         if (this.enabled != that.enabled) {
            return false;
         } else {
            return this.invalidHostNameAllowed != that.invalidHostNameAllowed ? false : Objects.equals(this.context, that.context);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.enabled ? 1 : 0;
      result = 31 * result + (this.invalidHostNameAllowed ? 1 : 0);
      result = 31 * result + (this.context != null ? this.context.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "SslSettings{enabled=" + this.enabled + ", invalidHostNameAllowed=" + this.invalidHostNameAllowed + ", context=" + this.context + '}';
   }

   @NotThreadSafe
   public static final class Builder {
      private boolean enabled;
      private boolean invalidHostNameAllowed;
      private SSLContext context;

      private Builder() {
      }

      public SslSettings.Builder applySettings(SslSettings sslSettings) {
         Assertions.notNull("sslSettings", sslSettings);
         this.enabled = sslSettings.enabled;
         this.invalidHostNameAllowed = sslSettings.invalidHostNameAllowed;
         this.context = sslSettings.context;
         return this;
      }

      public SslSettings.Builder enabled(boolean enabled) {
         this.enabled = enabled;
         return this;
      }

      public SslSettings.Builder invalidHostNameAllowed(boolean invalidHostNameAllowed) {
         this.invalidHostNameAllowed = invalidHostNameAllowed;
         return this;
      }

      public SslSettings.Builder context(SSLContext context) {
         this.context = context;
         return this;
      }

      public SslSettings.Builder applyConnectionString(ConnectionString connectionString) {
         Boolean sslEnabled = connectionString.getSslEnabled();
         if (sslEnabled != null) {
            this.enabled = sslEnabled;
         }

         Boolean sslInvalidHostnameAllowed = connectionString.getSslInvalidHostnameAllowed();
         if (sslInvalidHostnameAllowed != null) {
            this.invalidHostNameAllowed = sslInvalidHostnameAllowed;
         }

         return this;
      }

      public SslSettings build() {
         return new SslSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
