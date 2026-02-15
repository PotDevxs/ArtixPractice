package dev.artixdev.libs.com.mongodb.internal.time;

import java.time.Duration;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class TimePoint implements Comparable<TimePoint> {
   private final long nanos;

   private TimePoint(long nanos) {
      this.nanos = nanos;
   }

   public static TimePoint now() {
      return at(System.nanoTime());
   }

   static TimePoint at(long nanos) {
      return new TimePoint(nanos);
   }

   public Duration durationSince(TimePoint t) {
      return Duration.ofNanos(this.nanos - t.nanos);
   }

   public Duration elapsed() {
      return Duration.ofNanos(System.nanoTime() - this.nanos);
   }

   public TimePoint add(Duration duration) {
      long durationNanos = duration.toNanos();
      return at(this.nanos + durationNanos);
   }

   public int compareTo(TimePoint t) {
      return Long.signum(this.nanos - t.nanos);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TimePoint timePoint = (TimePoint)o;
         return this.nanos == timePoint.nanos;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Long.hashCode(this.nanos);
   }

   public String toString() {
      return "TimePoint{nanos=" + this.nanos + '}';
   }
}
