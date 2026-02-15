package dev.artixdev.libs.com.mongodb;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public final class TransactionOptions {
   private final ReadConcern readConcern;
   private final WriteConcern writeConcern;
   private final ReadPreference readPreference;
   private final Long maxCommitTimeMS;

   @Nullable
   public ReadConcern getReadConcern() {
      return this.readConcern;
   }

   @Nullable
   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   @Nullable
   public ReadPreference getReadPreference() {
      return this.readPreference;
   }

   @Nullable
   public Long getMaxCommitTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return this.maxCommitTimeMS == null ? null : timeUnit.convert(this.maxCommitTimeMS, TimeUnit.MILLISECONDS);
   }

   public static TransactionOptions.Builder builder() {
      return new TransactionOptions.Builder();
   }

   public static TransactionOptions merge(TransactionOptions options, TransactionOptions defaultOptions) {
      Assertions.notNull("options", options);
      Assertions.notNull("defaultOptions", defaultOptions);
      return builder().writeConcern(options.getWriteConcern() == null ? defaultOptions.getWriteConcern() : options.getWriteConcern()).readConcern(options.getReadConcern() == null ? defaultOptions.getReadConcern() : options.getReadConcern()).readPreference(options.getReadPreference() == null ? defaultOptions.getReadPreference() : options.getReadPreference()).maxCommitTime(options.getMaxCommitTime(TimeUnit.MILLISECONDS) == null ? defaultOptions.getMaxCommitTime(TimeUnit.MILLISECONDS) : options.getMaxCommitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).build();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TransactionOptions that = (TransactionOptions)o;
         if (!Objects.equals(this.maxCommitTimeMS, that.maxCommitTimeMS)) {
            return false;
         } else if (!Objects.equals(this.readConcern, that.readConcern)) {
            return false;
         } else if (!Objects.equals(this.writeConcern, that.writeConcern)) {
            return false;
         } else {
            return Objects.equals(this.readPreference, that.readPreference);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.readConcern != null ? this.readConcern.hashCode() : 0;
      result = 31 * result + (this.writeConcern != null ? this.writeConcern.hashCode() : 0);
      result = 31 * result + (this.readPreference != null ? this.readPreference.hashCode() : 0);
      result = 31 * result + (this.maxCommitTimeMS != null ? this.maxCommitTimeMS.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "TransactionOptions{readConcern=" + this.readConcern + ", writeConcern=" + this.writeConcern + ", readPreference=" + this.readPreference + ", maxCommitTimeMS" + this.maxCommitTimeMS + '}';
   }

   private TransactionOptions(TransactionOptions.Builder builder) {
      this.readConcern = builder.readConcern;
      this.writeConcern = builder.writeConcern;
      this.readPreference = builder.readPreference;
      this.maxCommitTimeMS = builder.maxCommitTimeMS;
   }

   // $FF: synthetic method
   TransactionOptions(TransactionOptions.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private ReadConcern readConcern;
      private WriteConcern writeConcern;
      private ReadPreference readPreference;
      private Long maxCommitTimeMS;

      public TransactionOptions.Builder readConcern(@Nullable ReadConcern readConcern) {
         this.readConcern = readConcern;
         return this;
      }

      public TransactionOptions.Builder writeConcern(@Nullable WriteConcern writeConcern) {
         this.writeConcern = writeConcern;
         return this;
      }

      public TransactionOptions.Builder readPreference(@Nullable ReadPreference readPreference) {
         this.readPreference = readPreference;
         return this;
      }

      public TransactionOptions.Builder maxCommitTime(@Nullable Long maxCommitTime, TimeUnit timeUnit) {
         if (maxCommitTime == null) {
            this.maxCommitTimeMS = null;
         } else {
            Assertions.notNull("timeUnit", timeUnit);
            Assertions.isTrueArgument("maxCommitTime > 0", maxCommitTime > 0L);
            this.maxCommitTimeMS = TimeUnit.MILLISECONDS.convert(maxCommitTime, timeUnit);
         }

         return this;
      }

      public TransactionOptions build() {
         return new TransactionOptions(this);
      }

      private Builder() {
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
