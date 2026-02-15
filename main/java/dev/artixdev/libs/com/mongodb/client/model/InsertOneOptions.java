package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public final class InsertOneOptions {
   private Boolean bypassDocumentValidation;
   private BsonValue comment;

   @Nullable
   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public InsertOneOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public InsertOneOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public InsertOneOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public String toString() {
      return "InsertOneOptions{bypassDocumentValidation=" + this.bypassDocumentValidation + ", comment=" + this.comment + '}';
   }
}
