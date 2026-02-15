package dev.artixdev.libs.com.mongodb.internal.binding;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractReferenceCounted implements ReferenceCounted {
   private final AtomicInteger referenceCount = new AtomicInteger(1);

   public int getCount() {
      return this.referenceCount.get();
   }

   public ReferenceCounted retain() {
      if (this.referenceCount.incrementAndGet() == 1) {
         throw new IllegalStateException("Attempted to increment the reference count when it is already 0");
      } else {
         return this;
      }
   }

   public int release() {
      int decrementedValue = this.referenceCount.decrementAndGet();
      if (decrementedValue < 0) {
         throw new IllegalStateException("Attempted to decrement the reference count below 0");
      } else {
         return decrementedValue;
      }
   }
}
