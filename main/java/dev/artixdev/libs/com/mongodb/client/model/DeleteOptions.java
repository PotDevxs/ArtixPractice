package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class DeleteOptions {
   private Bson hint;
   private String hintString;
   private Collation collation;
   private BsonValue comment;
   private Bson variables;

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public DeleteOptions collation(@Nullable Collation collation) {
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

   public DeleteOptions hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   public DeleteOptions hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public DeleteOptions comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public DeleteOptions comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   @Nullable
   public Bson getLet() {
      return this.variables;
   }

   public DeleteOptions let(Bson variables) {
      this.variables = variables;
      return this;
   }

   public String toString() {
      return "DeleteOptions{collation=" + this.collation + ", hint=" + this.hint + ", hintString='" + this.hintString + '\'' + ", comment=" + this.comment + ", let=" + this.variables + '}';
   }
}
