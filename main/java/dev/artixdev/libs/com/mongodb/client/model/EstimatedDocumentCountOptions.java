package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public class EstimatedDocumentCountOptions {
   private long maxTimeMS;
   private BsonValue comment;

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public EstimatedDocumentCountOptions maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public EstimatedDocumentCountOptions comment(@Nullable String comment) {
      this.comment = comment == null ? null : new BsonString(comment);
      return this;
   }

   public EstimatedDocumentCountOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public String toString() {
      return "EstimatedCountOptions{, maxTimeMS=" + this.maxTimeMS + ", comment=" + this.comment + '}';
   }
}
