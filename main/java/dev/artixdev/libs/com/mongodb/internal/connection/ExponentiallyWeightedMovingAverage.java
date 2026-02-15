package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.atomic.AtomicLong;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

class ExponentiallyWeightedMovingAverage {
   private static final long EMPTY = -1L;
   private final double alpha;
   private final AtomicLong average;

   ExponentiallyWeightedMovingAverage(double alpha) {
      Assertions.isTrueArgument("alpha >= 0.0 and <= 1.0", alpha >= 0.0D && alpha <= 1.0D);
      this.alpha = alpha;
      this.average = new AtomicLong(-1L);
   }

   void reset() {
      this.average.set(-1L);
   }

   long addSample(long sample) {
      return this.average.accumulateAndGet(sample, (average, givenSample) -> {
         return average == -1L ? givenSample : (long)(this.alpha * (double)givenSample + (1.0D - this.alpha) * (double)average);
      });
   }

   long getAverage() {
      long average = this.average.get();
      return average == -1L ? 0L : average;
   }
}
