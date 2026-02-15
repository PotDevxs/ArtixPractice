package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.CreateIndexCommitQuorum;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class CreateIndexOptions {
   private long maxTimeMS;
   private CreateIndexCommitQuorum commitQuorum;

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public CreateIndexOptions maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   @Nullable
   public CreateIndexCommitQuorum getCommitQuorum() {
      return this.commitQuorum;
   }

   public CreateIndexOptions commitQuorum(CreateIndexCommitQuorum commitQuorum) {
      this.commitQuorum = commitQuorum;
      return this;
   }

   public String toString() {
      return "CreateIndexOptions{maxTimeMS=" + this.maxTimeMS + ", commitQuorum=" + this.commitQuorum + '}';
   }
}
