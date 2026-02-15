package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class BulkWriteOptions {
   private boolean ordered = true;
   private Boolean bypassDocumentValidation;
   private BsonValue comment;
   private Bson variables;

   public boolean isOrdered() {
      return this.ordered;
   }

   public BulkWriteOptions ordered(boolean ordered) {
      this.ordered = ordered;
      return this;
   }

   @Nullable
   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public BulkWriteOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public BulkWriteOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public BulkWriteOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   @Nullable
   public Bson getLet() {
      return this.variables;
   }

   public BulkWriteOptions let(@Nullable Bson variables) {
      this.variables = variables;
      return this;
   }

   public String toString() {
      return "BulkWriteOptions{ordered=" + this.ordered + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", comment=" + this.comment + ", let=" + this.variables + '}';
   }
}
