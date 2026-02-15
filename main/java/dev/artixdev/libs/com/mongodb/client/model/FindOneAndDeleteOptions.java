package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class FindOneAndDeleteOptions {
   private Bson projection;
   private Bson sort;
   private long maxTimeMS;
   private Collation collation;
   private Bson hint;
   private String hintString;
   private BsonValue comment;
   private Bson variables;

   @Nullable
   public Bson getProjection() {
      return this.projection;
   }

   public FindOneAndDeleteOptions projection(@Nullable Bson projection) {
      this.projection = projection;
      return this;
   }

   @Nullable
   public Bson getSort() {
      return this.sort;
   }

   public FindOneAndDeleteOptions sort(@Nullable Bson sort) {
      this.sort = sort;
      return this;
   }

   public FindOneAndDeleteOptions maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public FindOneAndDeleteOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public FindOneAndDeleteOptions hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   public FindOneAndDeleteOptions hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public FindOneAndDeleteOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public FindOneAndDeleteOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   @Nullable
   public Bson getLet() {
      return this.variables;
   }

   public FindOneAndDeleteOptions let(Bson variables) {
      this.variables = variables;
      return this;
   }

   public String toString() {
      return "FindOneAndDeleteOptions{projection=" + this.projection + ", sort=" + this.sort + ", maxTimeMS=" + this.maxTimeMS + ", collation=" + this.collation + ", hint=" + this.hint + ", hintString='" + this.hintString + '\'' + ", comment=" + this.comment + ", let=" + this.variables + '}';
   }
}
