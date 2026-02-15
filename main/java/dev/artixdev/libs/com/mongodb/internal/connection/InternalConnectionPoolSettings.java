package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;

@Immutable
public final class InternalConnectionPoolSettings {
   private final boolean prestartAsyncWorkManager;

   private InternalConnectionPoolSettings(InternalConnectionPoolSettings.Builder builder) {
      this.prestartAsyncWorkManager = builder.prestartAsyncWorkManager;
   }

   public static InternalConnectionPoolSettings.Builder builder() {
      return new InternalConnectionPoolSettings.Builder();
   }

   public boolean isPrestartAsyncWorkManager() {
      return this.prestartAsyncWorkManager;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         InternalConnectionPoolSettings that = (InternalConnectionPoolSettings)o;
         return this.prestartAsyncWorkManager == that.prestartAsyncWorkManager;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.prestartAsyncWorkManager});
   }

   public String toString() {
      return "InternalConnectionPoolSettings{prestartAsyncWorkManager=" + this.prestartAsyncWorkManager + '}';
   }

   // $FF: synthetic method
   InternalConnectionPoolSettings(InternalConnectionPoolSettings.Builder x0, Object x1) {
      this(x0);
   }

   @NotThreadSafe
   public static final class Builder {
      private boolean prestartAsyncWorkManager;

      private Builder() {
         this.prestartAsyncWorkManager = false;
      }

      public InternalConnectionPoolSettings.Builder prestartAsyncWorkManager(boolean prestart) {
         this.prestartAsyncWorkManager = prestart;
         return this;
      }

      public InternalConnectionPoolSettings build() {
         return new InternalConnectionPoolSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
