package dev.artixdev.libs.com.mongodb.client.model;

public enum TimeSeriesGranularity {
   SECONDS,
   MINUTES,
   HOURS;

   // $FF: synthetic method
   private static TimeSeriesGranularity[] $values() {
      return new TimeSeriesGranularity[]{SECONDS, MINUTES, HOURS};
   }
}
