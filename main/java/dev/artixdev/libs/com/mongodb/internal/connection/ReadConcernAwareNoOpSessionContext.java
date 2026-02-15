package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public final class ReadConcernAwareNoOpSessionContext extends NoOpSessionContext {
   private final ReadConcern readConcern;

   public ReadConcernAwareNoOpSessionContext(ReadConcern readConcern) {
      this.readConcern = (ReadConcern)Assertions.notNull("readConcern", readConcern);
   }

   public ReadConcern getReadConcern() {
      return this.readConcern;
   }
}
