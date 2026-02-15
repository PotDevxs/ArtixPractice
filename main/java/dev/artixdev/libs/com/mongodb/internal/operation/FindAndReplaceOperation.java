package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.validator.MappedFieldNameValidator;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.internal.validator.ReplacingDocumentFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class FindAndReplaceOperation<T> extends BaseFindAndModifyOperation<T> {
   private final BsonDocument replacement;
   private boolean returnOriginal = true;
   private boolean upsert;
   private Boolean bypassDocumentValidation;

   public FindAndReplaceOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder, BsonDocument replacement) {
      super(namespace, writeConcern, retryWrites, decoder);
      this.replacement = (BsonDocument)Assertions.notNull("replacement", replacement);
   }

   public BsonDocument getReplacement() {
      return this.replacement;
   }

   public boolean isReturnOriginal() {
      return this.returnOriginal;
   }

   public FindAndReplaceOperation<T> returnOriginal(boolean returnOriginal) {
      this.returnOriginal = returnOriginal;
      return this;
   }

   public boolean isUpsert() {
      return this.upsert;
   }

   public FindAndReplaceOperation<T> upsert(boolean upsert) {
      this.upsert = upsert;
      return this;
   }

   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public FindAndReplaceOperation<T> bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   public FindAndReplaceOperation<T> filter(@Nullable BsonDocument filter) {
      super.filter(filter);
      return this;
   }

   public FindAndReplaceOperation<T> projection(@Nullable BsonDocument projection) {
      super.projection(projection);
      return this;
   }

   public FindAndReplaceOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      super.maxTime(maxTime, timeUnit);
      return this;
   }

   public FindAndReplaceOperation<T> sort(@Nullable BsonDocument sort) {
      super.sort(sort);
      return this;
   }

   public FindAndReplaceOperation<T> hint(@Nullable Bson hint) {
      super.hint(hint);
      return this;
   }

   public FindAndReplaceOperation<T> hintString(@Nullable String hint) {
      super.hintString(hint);
      return this;
   }

   public FindAndReplaceOperation<T> collation(@Nullable Collation collation) {
      super.collation(collation);
      return this;
   }

   public FindAndReplaceOperation<T> comment(@Nullable BsonValue comment) {
      super.comment(comment);
      return this;
   }

   public FindAndReplaceOperation<T> let(@Nullable BsonDocument variables) {
      super.let(variables);
      return this;
   }

   protected FieldNameValidator getFieldNameValidator() {
      Map<String, FieldNameValidator> map = new HashMap();
      map.put("update", new ReplacingDocumentFieldNameValidator());
      return new MappedFieldNameValidator(new NoOpFieldNameValidator(), map);
   }

   protected void specializeCommand(BsonDocument commandDocument, ConnectionDescription connectionDescription) {
      commandDocument.put((String)"new", (BsonValue)(new BsonBoolean(!this.isReturnOriginal())));
      DocumentHelper.putIfTrue(commandDocument, "upsert", this.isUpsert());
      commandDocument.put((String)"update", (BsonValue)this.getReplacement());
      if (this.bypassDocumentValidation != null) {
         commandDocument.put((String)"bypassDocumentValidation", (BsonValue)BsonBoolean.valueOf(this.bypassDocumentValidation));
      }

   }
}
