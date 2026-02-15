package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class FindOneAndReplaceOptions {
   private Bson projection;
   private Bson sort;
   private boolean upsert;
   private ReturnDocument returnDocument;
   private long maxTimeMS;
   private Boolean bypassDocumentValidation;
   private Collation collation;
   private Bson hint;
   private String hintString;
   private BsonValue comment;
   private Bson variables;

   public FindOneAndReplaceOptions() {
      this.returnDocument = ReturnDocument.BEFORE;
   }

   @Nullable
   public Bson getProjection() {
      return this.projection;
   }

   public FindOneAndReplaceOptions projection(@Nullable Bson projection) {
      this.projection = projection;
      return this;
   }

   @Nullable
   public Bson getSort() {
      return this.sort;
   }

   public FindOneAndReplaceOptions sort(@Nullable Bson sort) {
      this.sort = sort;
      return this;
   }

   public boolean isUpsert() {
      return this.upsert;
   }

   public FindOneAndReplaceOptions upsert(boolean upsert) {
      this.upsert = upsert;
      return this;
   }

   public ReturnDocument getReturnDocument() {
      return this.returnDocument;
   }

   public FindOneAndReplaceOptions returnDocument(ReturnDocument returnDocument) {
      this.returnDocument = (ReturnDocument)Assertions.notNull("returnDocument", returnDocument);
      return this;
   }

   public FindOneAndReplaceOptions maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   @Nullable
   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public FindOneAndReplaceOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public FindOneAndReplaceOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   public FindOneAndReplaceOptions hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public FindOneAndReplaceOptions hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public FindOneAndReplaceOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public FindOneAndReplaceOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   @Nullable
   public Bson getLet() {
      return this.variables;
   }

   public FindOneAndReplaceOptions let(Bson variables) {
      this.variables = variables;
      return this;
   }

   public String toString() {
      return "FindOneAndReplaceOptions{projection=" + this.projection + ", sort=" + this.sort + ", upsert=" + this.upsert + ", returnDocument=" + this.returnDocument + ", maxTimeMS=" + this.maxTimeMS + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", collation=" + this.collation + ", hint=" + this.hint + ", hintString" + this.hintString + ", comment=" + this.comment + ", let=" + this.variables + '}';
   }
}
