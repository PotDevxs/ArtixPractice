package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public final class InsertManyOptions {
   private boolean ordered = true;
   private Boolean bypassDocumentValidation;
   private BsonValue comment;

   public boolean isOrdered() {
      return this.ordered;
   }

   public InsertManyOptions ordered(boolean ordered) {
      this.ordered = ordered;
      return this;
   }

   @Nullable
   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public InsertManyOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public InsertManyOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public InsertManyOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public String toString() {
      return "InsertManyOptions{ordered=" + this.ordered + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", comment=" + this.comment + '}';
   }
}
