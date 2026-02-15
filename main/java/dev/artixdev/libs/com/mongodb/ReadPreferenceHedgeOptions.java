package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;

@Immutable
public final class ReadPreferenceHedgeOptions {
   private final boolean enabled;

   public boolean isEnabled() {
      return this.enabled;
   }

   public static ReadPreferenceHedgeOptions.Builder builder() {
      return new ReadPreferenceHedgeOptions.Builder();
   }

   public BsonDocument toBsonDocument() {
      return new BsonDocument("enabled", new BsonBoolean(this.enabled));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ReadPreferenceHedgeOptions that = (ReadPreferenceHedgeOptions)o;
         return this.enabled == that.enabled;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.enabled ? 1 : 0;
   }

   public String toString() {
      return "ReadPreferenceHedgeOptions{enabled=" + this.enabled + '}';
   }

   private ReadPreferenceHedgeOptions(ReadPreferenceHedgeOptions.Builder builder) {
      this.enabled = builder.enabled;
   }

   // $FF: synthetic method
   ReadPreferenceHedgeOptions(ReadPreferenceHedgeOptions.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private boolean enabled;

      public ReadPreferenceHedgeOptions.Builder enabled(boolean enabled) {
         this.enabled = enabled;
         return this;
      }

      public ReadPreferenceHedgeOptions build() {
         return new ReadPreferenceHedgeOptions(this);
      }

      private Builder() {
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
