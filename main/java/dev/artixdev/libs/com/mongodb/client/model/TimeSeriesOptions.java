package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class TimeSeriesOptions {
   private final String timeField;
   private String metaField;
   private TimeSeriesGranularity granularity;
   private Long bucketMaxSpanSeconds;
   private Long bucketRoundingSeconds;

   public TimeSeriesOptions(String timeField) {
      this.timeField = (String)Assertions.notNull("timeField", timeField);
   }

   public String getTimeField() {
      return this.timeField;
   }

   @Nullable
   public String getMetaField() {
      return this.metaField;
   }

   public TimeSeriesOptions metaField(@Nullable String metaField) {
      this.metaField = metaField;
      return this;
   }

   @Nullable
   public TimeSeriesGranularity getGranularity() {
      return this.granularity;
   }

   public TimeSeriesOptions granularity(@Nullable TimeSeriesGranularity granularity) {
      Assertions.isTrue("granularity is not allowed when bucketMaxSpan is set", this.bucketMaxSpanSeconds == null);
      Assertions.isTrue("granularity is not allowed when bucketRounding is set", this.bucketRoundingSeconds == null);
      this.granularity = granularity;
      return this;
   }

   @Nullable
   public Long getBucketMaxSpan(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return this.bucketMaxSpanSeconds == null ? null : timeUnit.convert(this.bucketMaxSpanSeconds, TimeUnit.SECONDS);
   }

   public TimeSeriesOptions bucketMaxSpan(@Nullable Long bucketMaxSpan, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      if (bucketMaxSpan == null) {
         this.bucketMaxSpanSeconds = null;
      } else {
         Assertions.isTrue("bucketMaxSpan is not allowed when granularity is set", this.granularity == null);
         long seconds = TimeUnit.SECONDS.convert(bucketMaxSpan, timeUnit);
         Assertions.isTrueArgument("bucketMaxSpan, after conversion to seconds, must be >= 1", seconds > 0L);
         this.bucketMaxSpanSeconds = seconds;
      }

      return this;
   }

   @Nullable
   public Long getBucketRounding(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return this.bucketRoundingSeconds == null ? null : timeUnit.convert(this.bucketRoundingSeconds, TimeUnit.SECONDS);
   }

   public TimeSeriesOptions bucketRounding(@Nullable Long bucketRounding, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      if (bucketRounding == null) {
         this.bucketRoundingSeconds = null;
      } else {
         Assertions.isTrue("bucketRounding is not allowed when granularity is set", this.granularity == null);
         long seconds = TimeUnit.SECONDS.convert(bucketRounding, timeUnit);
         Assertions.isTrueArgument("bucketRounding, after conversion to seconds, must be >= 1", seconds > 0L);
         this.bucketRoundingSeconds = seconds;
      }

      return this;
   }

   public String toString() {
      return "TimeSeriesOptions{timeField='" + this.timeField + '\'' + ", metaField='" + this.metaField + '\'' + ", granularity=" + this.granularity + ", bucketMaxSpanSeconds=" + this.bucketMaxSpanSeconds + ", bucketRoundingSeconds=" + this.bucketRoundingSeconds + '}';
   }
}
