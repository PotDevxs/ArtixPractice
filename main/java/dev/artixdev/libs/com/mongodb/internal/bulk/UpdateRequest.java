package dev.artixdev.libs.com.mongodb.internal.bulk;

import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class UpdateRequest extends WriteRequest {
   private final BsonValue update;
   private final WriteRequest.Type updateType;
   private final BsonDocument filter;
   private boolean isMulti;
   private boolean isUpsert = false;
   private Collation collation;
   private List<BsonDocument> arrayFilters;
   @Nullable
   private Bson hint;
   @Nullable
   private String hintString;

   public UpdateRequest(BsonDocument filter, @Nullable BsonValue update, WriteRequest.Type updateType) {
      if (updateType != WriteRequest.Type.UPDATE && updateType != WriteRequest.Type.REPLACE) {
         throw new IllegalArgumentException("Update type must be UPDATE or REPLACE");
      } else if (update != null && !update.isDocument() && !update.isArray()) {
         throw new IllegalArgumentException("Update operation type must be a document or a pipeline");
      } else {
         this.filter = (BsonDocument)Assertions.notNull("filter", filter);
         this.update = (BsonValue)Assertions.notNull("update", update);
         this.updateType = updateType;
         this.isMulti = updateType == WriteRequest.Type.UPDATE;
      }
   }

   public WriteRequest.Type getType() {
      return this.updateType;
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public BsonValue getUpdateValue() {
      return this.update;
   }

   public boolean isMulti() {
      return this.isMulti;
   }

   public UpdateRequest multi(boolean isMulti) {
      if (isMulti && this.updateType == WriteRequest.Type.REPLACE) {
         throw new IllegalArgumentException("Replacements can not be multi");
      } else {
         this.isMulti = isMulti;
         return this;
      }
   }

   public boolean isUpsert() {
      return this.isUpsert;
   }

   public UpdateRequest upsert(boolean isUpsert) {
      this.isUpsert = isUpsert;
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public UpdateRequest collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public UpdateRequest arrayFilters(@Nullable List<BsonDocument> arrayFilters) {
      this.arrayFilters = arrayFilters;
      return this;
   }

   @Nullable
   public List<BsonDocument> getArrayFilters() {
      return this.arrayFilters;
   }

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   public UpdateRequest hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public UpdateRequest hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }
}
