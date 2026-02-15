package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoWriteConcernException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.WriteConcernResult;
import dev.artixdev.libs.com.mongodb.bulk.WriteConcernError;
import dev.artixdev.libs.com.mongodb.internal.connection.ProtocolHelper;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public final class WriteConcernHelper {
   public static void appendWriteConcernToCommand(WriteConcern writeConcern, BsonDocument commandDocument) {
      if (writeConcern != null && !writeConcern.isServerDefault()) {
         commandDocument.put((String)"writeConcern", (BsonValue)writeConcern.asDocument());
      }

   }

   public static void throwOnWriteConcernError(BsonDocument result, ServerAddress serverAddress, int maxWireVersion) {
      if (hasWriteConcernError(result)) {
         MongoException exception = ProtocolHelper.createSpecialException(result, serverAddress, "errmsg");
         if (exception == null) {
            exception = createWriteConcernException(result, serverAddress);
         }

         CommandOperationHelper.addRetryableWriteErrorLabel((MongoException)exception, maxWireVersion);
         throw exception;
      }
   }

   public static boolean hasWriteConcernError(BsonDocument result) {
      return result.containsKey("writeConcernError");
   }

   public static MongoWriteConcernException createWriteConcernException(BsonDocument result, ServerAddress serverAddress) {
      MongoWriteConcernException writeConcernException = new MongoWriteConcernException(createWriteConcernError(result.getDocument("writeConcernError")), WriteConcernResult.acknowledged(0, false, (BsonValue)null), serverAddress);
      Stream<String> errorLabels = result.getArray("errorLabels", new BsonArray()).stream().map((i) -> i.asString().getValue());
      Objects.requireNonNull(writeConcernException);
      errorLabels.forEach(writeConcernException::addLabel);
      return writeConcernException;
   }

   public static WriteConcernError createWriteConcernError(BsonDocument writeConcernErrorDocument) {
      return new WriteConcernError(writeConcernErrorDocument.getNumber("code").intValue(), writeConcernErrorDocument.getString("codeName", new BsonString("")).getValue(), writeConcernErrorDocument.getString("errmsg").getValue(), writeConcernErrorDocument.getDocument("errInfo", new BsonDocument()), (Set)writeConcernErrorDocument.getArray("errorLabels", new BsonArray()).stream().map((i) -> {
         return i.asString().getValue();
      }).collect(Collectors.toSet()));
   }

   private WriteConcernHelper() {
   }
}
