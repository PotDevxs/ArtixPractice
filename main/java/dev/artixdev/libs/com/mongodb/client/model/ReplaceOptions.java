package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class ReplaceOptions {
   private boolean upsert;
   private Boolean bypassDocumentValidation;
   private Collation collation;
   private Bson hint;
   private String hintString;
   private BsonValue comment;
   private Bson variables;

   public boolean isUpsert() {
      return this.upsert;
   }

   public ReplaceOptions upsert(boolean upsert) {
      this.upsert = upsert;
      return this;
   }

   @Nullable
   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public ReplaceOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public ReplaceOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   public ReplaceOptions hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public ReplaceOptions hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public ReplaceOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public ReplaceOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   @Nullable
   public Bson getLet() {
      return this.variables;
   }

   public ReplaceOptions let(Bson variables) {
      this.variables = variables;
      return this;
   }

   public String toString() {
      return "ReplaceOptions{upsert=" + this.upsert + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", collation=" + this.collation + ", hint=" + this.hint + ", hintString=" + this.hintString + ", comment=" + this.comment + ", let=" + this.variables + '}';
   }
}
