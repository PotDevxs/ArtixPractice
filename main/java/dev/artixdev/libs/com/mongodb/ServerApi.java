package dev.artixdev.libs.com.mongodb;

import java.util.Objects;
import java.util.Optional;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class ServerApi {
   private final ServerApiVersion version;
   private final Boolean deprecationErrors;
   private final Boolean strict;

   private ServerApi(ServerApiVersion version, @Nullable Boolean strict, @Nullable Boolean deprecationErrors) {
      this.version = (ServerApiVersion)Assertions.notNull("version", version);
      this.deprecationErrors = deprecationErrors;
      this.strict = strict;
   }

   public ServerApiVersion getVersion() {
      return this.version;
   }

   public Optional<Boolean> getStrict() {
      return Optional.ofNullable(this.strict);
   }

   public Optional<Boolean> getDeprecationErrors() {
      return Optional.ofNullable(this.deprecationErrors);
   }

   public static ServerApi.Builder builder() {
      return new ServerApi.Builder();
   }

   public String toString() {
      return "ServerApi{version=" + this.version + ", deprecationErrors=" + this.deprecationErrors + ", strict=" + this.strict + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ServerApi serverApi = (ServerApi)o;
         if (this.version != serverApi.version) {
            return false;
         } else if (!Objects.equals(this.deprecationErrors, serverApi.deprecationErrors)) {
            return false;
         } else {
            return Objects.equals(this.strict, serverApi.strict);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.version.hashCode();
      result = 31 * result + (this.deprecationErrors != null ? this.deprecationErrors.hashCode() : 0);
      result = 31 * result + (this.strict != null ? this.strict.hashCode() : 0);
      return result;
   }

   // $FF: synthetic method
   ServerApi(ServerApiVersion x0, Boolean x1, Boolean x2, Object x3) {
      this(x0, x1, x2);
   }

   @NotThreadSafe
   public static final class Builder {
      private ServerApiVersion version;
      private Boolean deprecationErrors;
      private Boolean strict;

      private Builder() {
      }

      public ServerApi.Builder version(ServerApiVersion version) {
         this.version = version;
         return this;
      }

      public ServerApi.Builder deprecationErrors(boolean deprecationErrors) {
         this.deprecationErrors = deprecationErrors;
         return this;
      }

      public ServerApi.Builder strict(boolean strict) {
         this.strict = strict;
         return this;
      }

      public ServerApi build() {
         return new ServerApi(this.version, this.strict, this.deprecationErrors);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
