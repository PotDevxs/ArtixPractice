package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.validator.MappedFieldNameValidator;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.internal.validator.UpdateFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class FindAndUpdateOperation<T> extends BaseFindAndModifyOperation<T> {
   private final BsonDocument update;
   private final List<BsonDocument> updatePipeline;
   private boolean returnOriginal = true;
   private boolean upsert;
   private Boolean bypassDocumentValidation;
   private List<BsonDocument> arrayFilters;

   public FindAndUpdateOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder, BsonDocument update) {
      super(namespace, writeConcern, retryWrites, decoder);
      this.update = (BsonDocument)Assertions.notNull("update", update);
      this.updatePipeline = null;
   }

   public FindAndUpdateOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder, List<BsonDocument> update) {
      super(namespace, writeConcern, retryWrites, decoder);
      this.updatePipeline = update;
      this.update = null;
   }

   @Nullable
   public BsonDocument getUpdate() {
      return this.update;
   }

   @Nullable
   public List<BsonDocument> getUpdatePipeline() {
      return this.updatePipeline;
   }

   public boolean isReturnOriginal() {
      return this.returnOriginal;
   }

   public FindAndUpdateOperation<T> returnOriginal(boolean returnOriginal) {
      this.returnOriginal = returnOriginal;
      return this;
   }

   public boolean isUpsert() {
      return this.upsert;
   }

   public FindAndUpdateOperation<T> upsert(boolean upsert) {
      this.upsert = upsert;
      return this;
   }

   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public FindAndUpdateOperation<T> bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   public FindAndUpdateOperation<T> arrayFilters(@Nullable List<BsonDocument> arrayFilters) {
      this.arrayFilters = arrayFilters;
      return this;
   }

   public List<BsonDocument> getArrayFilters() {
      return this.arrayFilters;
   }

   public FindAndUpdateOperation<T> filter(@Nullable BsonDocument filter) {
      super.filter(filter);
      return this;
   }

   public FindAndUpdateOperation<T> projection(@Nullable BsonDocument projection) {
      super.projection(projection);
      return this;
   }

   public FindAndUpdateOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      super.maxTime(maxTime, timeUnit);
      return this;
   }

   public FindAndUpdateOperation<T> sort(@Nullable BsonDocument sort) {
      super.sort(sort);
      return this;
   }

   public FindAndUpdateOperation<T> hint(@Nullable Bson hint) {
      super.hint(hint);
      return this;
   }

   public FindAndUpdateOperation<T> hintString(@Nullable String hint) {
      super.hintString(hint);
      return this;
   }

   public FindAndUpdateOperation<T> collation(@Nullable Collation collation) {
      super.collation(collation);
      return this;
   }

   public FindAndUpdateOperation<T> comment(@Nullable BsonValue comment) {
      super.comment(comment);
      return this;
   }

   public FindAndUpdateOperation<T> let(@Nullable BsonDocument variables) {
      super.let(variables);
      return this;
   }

   protected FieldNameValidator getFieldNameValidator() {
      Map<String, FieldNameValidator> map = new HashMap();
      map.put("update", new UpdateFieldNameValidator());
      return new MappedFieldNameValidator(new NoOpFieldNameValidator(), map);
   }

   protected void specializeCommand(BsonDocument commandDocument, ConnectionDescription connectionDescription) {
      commandDocument.put((String)"new", (BsonValue)(new BsonBoolean(!this.isReturnOriginal())));
      DocumentHelper.putIfTrue(commandDocument, "upsert", this.isUpsert());
      if (this.getUpdatePipeline() != null) {
         commandDocument.put((String)"update", (BsonValue)(new BsonArray(this.getUpdatePipeline())));
      } else {
         DocumentHelper.putIfNotNull(commandDocument, "update", (BsonValue)this.getUpdate());
      }

      if (this.bypassDocumentValidation != null) {
         commandDocument.put((String)"bypassDocumentValidation", (BsonValue)BsonBoolean.valueOf(this.bypassDocumentValidation));
      }

      if (this.arrayFilters != null) {
         commandDocument.put((String)"arrayFilters", (BsonValue)(new BsonArray(this.arrayFilters)));
      }

   }
}
