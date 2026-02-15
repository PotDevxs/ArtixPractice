package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class FindAndDeleteOperation<T> extends BaseFindAndModifyOperation<T> {
   public FindAndDeleteOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder) {
      super(namespace, writeConcern, retryWrites, decoder);
   }

   public FindAndDeleteOperation<T> filter(@Nullable BsonDocument filter) {
      super.filter(filter);
      return this;
   }

   public FindAndDeleteOperation<T> projection(@Nullable BsonDocument projection) {
      super.projection(projection);
      return this;
   }

   public FindAndDeleteOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      super.maxTime(maxTime, timeUnit);
      return this;
   }

   public FindAndDeleteOperation<T> sort(@Nullable BsonDocument sort) {
      super.sort(sort);
      return this;
   }

   public FindAndDeleteOperation<T> hint(@Nullable Bson hint) {
      super.hint(hint);
      return this;
   }

   public FindAndDeleteOperation<T> hintString(@Nullable String hint) {
      super.hintString(hint);
      return this;
   }

   public FindAndDeleteOperation<T> collation(@Nullable Collation collation) {
      super.collation(collation);
      return this;
   }

   public FindAndDeleteOperation<T> comment(@Nullable BsonValue comment) {
      super.comment(comment);
      return this;
   }

   public FindAndDeleteOperation<T> let(@Nullable BsonDocument variables) {
      super.let(variables);
      return this;
   }

   protected FieldNameValidator getFieldNameValidator() {
      return new NoOpFieldNameValidator();
   }

   protected void specializeCommand(BsonDocument commandDocument, ConnectionDescription connectionDescription) {
      commandDocument.put((String)"remove", (BsonValue)BsonBoolean.TRUE);
   }
}
