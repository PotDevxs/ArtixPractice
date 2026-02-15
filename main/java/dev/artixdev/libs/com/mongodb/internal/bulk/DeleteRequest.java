package dev.artixdev.libs.com.mongodb.internal.bulk;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class DeleteRequest extends WriteRequest {
   private final BsonDocument filter;
   private boolean isMulti = true;
   private Collation collation;
   private Bson hint;
   private String hintString;

   public DeleteRequest(BsonDocument filter) {
      this.filter = (BsonDocument)Assertions.notNull("filter", filter);
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public DeleteRequest multi(boolean isMulti) {
      this.isMulti = isMulti;
      return this;
   }

   public boolean isMulti() {
      return this.isMulti;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public DeleteRequest collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   public DeleteRequest hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public DeleteRequest hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   public WriteRequest.Type getType() {
      return WriteRequest.Type.DELETE;
   }
}
