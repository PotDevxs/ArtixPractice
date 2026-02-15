package dev.artixdev.libs.com.mongodb.client.model;

import java.util.List;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class UpdateOptions {
   private boolean upsert;
   private Boolean bypassDocumentValidation;
   private Collation collation;
   private List<? extends Bson> arrayFilters;
   private Bson hint;
   private String hintString;
   private BsonValue comment;
   private Bson variables;

   public boolean isUpsert() {
      return this.upsert;
   }

   public UpdateOptions upsert(boolean upsert) {
      this.upsert = upsert;
      return this;
   }

   @Nullable
   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public UpdateOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public UpdateOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public UpdateOptions arrayFilters(@Nullable List<? extends Bson> arrayFilters) {
      this.arrayFilters = arrayFilters;
      return this;
   }

   @Nullable
   public List<? extends Bson> getArrayFilters() {
      return this.arrayFilters;
   }

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   public UpdateOptions hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public UpdateOptions hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public UpdateOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public UpdateOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   @Nullable
   public Bson getLet() {
      return this.variables;
   }

   public UpdateOptions let(Bson variables) {
      this.variables = variables;
      return this;
   }

   public String toString() {
      return "UpdateOptions{upsert=" + this.upsert + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", collation=" + this.collation + ", arrayFilters=" + this.arrayFilters + ", hint=" + this.hint + ", hintString=" + this.hintString + ", comment=" + this.comment + ", let=" + this.variables + '}';
   }
}
