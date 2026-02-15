package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.atomic.AtomicLong;

public class OperationContext {
   private static final AtomicLong NEXT_ID = new AtomicLong(0L);
   private final long id;

   public OperationContext() {
      this.id = NEXT_ID.incrementAndGet();
   }

   public long getId() {
      return this.id;
   }
}
