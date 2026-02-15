package dev.artixdev.libs.com.mongodb.internal.time;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public final class Timeout {
   private static final Timeout INFINITE = new Timeout(-1L, (TimePoint)null);
   private static final Timeout IMMEDIATE = new Timeout(0L, (TimePoint)null);
   private final long durationNanos;
   @Nullable
   private final TimePoint start;

   private Timeout(long durationNanos, @Nullable TimePoint start) {
      this.durationNanos = durationNanos;
      this.start = start;
   }

   public static Timeout started(long duration, TimeUnit unit, TimePoint at) {
      return started(unit.toNanos(duration), (TimePoint)Assertions.assertNotNull(at));
   }

   public static Timeout started(long durationNanos, TimePoint at) {
      if (durationNanos >= 0L && durationNanos != Long.MAX_VALUE) {
         return durationNanos == 0L ? immediate() : new Timeout(durationNanos, (TimePoint)Assertions.assertNotNull(at));
      } else {
         return infinite();
      }
   }

   public static Timeout startNow(long duration, TimeUnit unit) {
      return started(duration, unit, TimePoint.now());
   }

   public static Timeout startNow(long durationNanos) {
      return started(durationNanos, TimePoint.now());
   }

   public static Timeout infinite() {
      return INFINITE;
   }

   public static Timeout immediate() {
      return IMMEDIATE;
   }

   long remainingNanos(TimePoint now) {
      return Math.max(0L, this.durationNanos - now.durationSince((TimePoint)Assertions.assertNotNull(this.start)).toNanos());
   }

   public long remaining(TimeUnit unit) {
      Assertions.assertFalse(this.isInfinite());
      return this.isImmediate() ? 0L : convertRoundUp(this.remainingNanos(TimePoint.now()), unit);
   }

   public long remainingOrInfinite(TimeUnit unit) {
      return this.isInfinite() ? -1L : this.remaining(unit);
   }

   public boolean expired() {
      return expired(this.remainingOrInfinite(TimeUnit.NANOSECONDS));
   }

   public static boolean expired(long remaining) {
      return remaining == 0L;
   }

   public boolean isInfinite() {
      return this.equals(INFINITE);
   }

   public boolean isImmediate() {
      return this.equals(IMMEDIATE);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Timeout other = (Timeout)o;
         return this.durationNanos == other.durationNanos && Objects.equals(this.start, other.start());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.durationNanos, this.start});
   }

   public String toString() {
      return "Timeout{durationNanos=" + this.durationNanos + ", start=" + this.start + '}';
   }

   public String toUserString() {
      if (this.isInfinite()) {
         return "infinite";
      } else {
         return this.isImmediate() ? "0 ms (immediate)" : convertRoundUp(this.durationNanos, TimeUnit.MILLISECONDS) + " ms";
      }
   }

   long durationNanos() {
      return this.durationNanos;
   }

   @Nullable
   TimePoint start() {
      return this.start;
   }

   static long convertRoundUp(long nonNegativeNanos, TimeUnit unit) {
      Assertions.assertTrue(nonNegativeNanos >= 0L);
      if (unit == TimeUnit.NANOSECONDS) {
         return nonNegativeNanos;
      } else {
         long trimmed = unit.convert(nonNegativeNanos, TimeUnit.NANOSECONDS);
         return TimeUnit.NANOSECONDS.convert(trimmed, unit) < nonNegativeNanos ? trimmed + 1L : trimmed;
      }
   }
}
