package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class CountOptions {
   private Bson hint;
   private String hintString;
   private int limit;
   private int skip;
   private long maxTimeMS;
   private Collation collation;
   private BsonValue comment;

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public CountOptions hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   public CountOptions hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   public int getLimit() {
      return this.limit;
   }

   public CountOptions limit(int limit) {
      this.limit = limit;
      return this;
   }

   public int getSkip() {
      return this.skip;
   }

   public CountOptions skip(int skip) {
      this.skip = skip;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public CountOptions maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public CountOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public CountOptions comment(@Nullable String comment) {
      this.comment = comment == null ? null : new BsonString(comment);
      return this;
   }

   public CountOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public String toString() {
      return "CountOptions{hint=" + this.hint + ", hintString='" + this.hintString + '\'' + ", limit=" + this.limit + ", skip=" + this.skip + ", maxTimeMS=" + this.maxTimeMS + ", collation=" + this.collation + ", comment=" + this.comment + '}';
   }
}
