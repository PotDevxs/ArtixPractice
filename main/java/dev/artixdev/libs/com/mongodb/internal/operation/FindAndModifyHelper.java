package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.com.mongodb.MongoWriteConcernException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.WriteConcernResult;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;

final class FindAndModifyHelper {
   static <T> SyncOperationHelper.CommandWriteTransformer<BsonDocument, T> transformer() {
      return (result, connection) -> {
         return transformDocument(result, connection.getDescription().getServerAddress());
      };
   }

   static <T> AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, T> asyncTransformer() {
      return (result, connection) -> {
         return transformDocument(result, connection.getDescription().getServerAddress());
      };
   }

   @Nullable
   private static <T> T transformDocument(BsonDocument result, ServerAddress serverAddress) {
      if (WriteConcernHelper.hasWriteConcernError(result)) {
         MongoWriteConcernException writeConcernException = new MongoWriteConcernException(WriteConcernHelper.createWriteConcernError(result.getDocument("writeConcernError")), createWriteConcernResult(result.getDocument("lastErrorObject", new BsonDocument())), serverAddress);
         Stream<String> errorLabels = result.getArray("errorLabels", new BsonArray()).stream().map((i) -> i.asString().getValue());
         Objects.requireNonNull(writeConcernException);
         errorLabels.forEach(writeConcernException::addLabel);
         throw writeConcernException;
      } else {
         return !result.isDocument("value") ? null : BsonDocumentWrapperHelper.toDocument(result.getDocument("value", (BsonDocument)null));
      }
   }

   private static WriteConcernResult createWriteConcernResult(BsonDocument result) {
      BsonBoolean updatedExisting = result.getBoolean("updatedExisting", BsonBoolean.FALSE);
      return WriteConcernResult.acknowledged(result.getNumber("n", new BsonInt32(0)).intValue(), updatedExisting.getValue(), result.get("upserted"));
   }

   private FindAndModifyHelper() {
   }
}
