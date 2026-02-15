package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.bulk.DeleteRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.UpdateRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequest;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.conversions.Bson;

final class OperationHelper {
   public static final Logger LOGGER = Loggers.getLogger("operation");

   static void validateCollationAndWriteConcern(@Nullable Collation collation, WriteConcern writeConcern) {
      if (collation != null && !writeConcern.isAcknowledged()) {
         throw new MongoClientException("Specifying collation with an unacknowledged WriteConcern is not supported");
      }
   }

   private static void validateArrayFilters(WriteConcern writeConcern) {
      if (!writeConcern.isAcknowledged()) {
         throw new MongoClientException("Specifying array filters with an unacknowledged WriteConcern is not supported");
      }
   }

   private static void validateWriteRequestHint(ConnectionDescription connectionDescription, WriteConcern writeConcern, WriteRequest request) {
      if (!writeConcern.isAcknowledged()) {
         if (request instanceof UpdateRequest && ServerVersionHelper.serverIsLessThanVersionFourDotTwo(connectionDescription)) {
            throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", connectionDescription.getMaxWireVersion()));
         }

         if (request instanceof DeleteRequest && ServerVersionHelper.serverIsLessThanVersionFourDotFour(connectionDescription)) {
            throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", connectionDescription.getMaxWireVersion()));
         }
      }

   }

   static void validateHintForFindAndModify(ConnectionDescription connectionDescription, WriteConcern writeConcern) {
      if (ServerVersionHelper.serverIsLessThanVersionFourDotTwo(connectionDescription)) {
         throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", connectionDescription.getMaxWireVersion()));
      } else if (!writeConcern.isAcknowledged() && ServerVersionHelper.serverIsLessThanVersionFourDotFour(connectionDescription)) {
         throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", connectionDescription.getMaxWireVersion()));
      }
   }

   private static void validateWriteRequestCollations(List<? extends WriteRequest> requests, WriteConcern writeConcern) {
      Collation collation = null;
      Iterator<? extends WriteRequest> iterator = requests.iterator();

      while(iterator.hasNext()) {
         WriteRequest request = iterator.next();
         if (request instanceof UpdateRequest) {
            collation = ((UpdateRequest)request).getCollation();
         } else if (request instanceof DeleteRequest) {
            collation = ((DeleteRequest)request).getCollation();
         }

         if (collation != null) {
            break;
         }
      }

      validateCollationAndWriteConcern(collation, writeConcern);
   }

   private static void validateUpdateRequestArrayFilters(List<? extends WriteRequest> requests, WriteConcern writeConcern) {
      Iterator<? extends WriteRequest> iterator = requests.iterator();

      while(iterator.hasNext()) {
         WriteRequest request = iterator.next();
         List<BsonDocument> arrayFilters = null;
         if (request instanceof UpdateRequest) {
            arrayFilters = ((UpdateRequest)request).getArrayFilters();
         }

         if (arrayFilters != null) {
            validateArrayFilters(writeConcern);
            break;
         }
      }

   }

   private static void validateWriteRequestHints(ConnectionDescription connectionDescription, List<? extends WriteRequest> requests, WriteConcern writeConcern) {
      Iterator<? extends WriteRequest> iterator = requests.iterator();

      while(iterator.hasNext()) {
         WriteRequest request = iterator.next();
         Bson hint = null;
         String hintString = null;
         if (request instanceof UpdateRequest) {
            hint = ((UpdateRequest)request).getHint();
            hintString = ((UpdateRequest)request).getHintString();
         } else if (request instanceof DeleteRequest) {
            hint = ((DeleteRequest)request).getHint();
            hintString = ((DeleteRequest)request).getHintString();
         }

         if (hint != null || hintString != null) {
            validateWriteRequestHint(connectionDescription, writeConcern, request);
            break;
         }
      }

   }

   static void validateWriteRequests(ConnectionDescription connectionDescription, Boolean bypassDocumentValidation, List<? extends WriteRequest> requests, WriteConcern writeConcern) {
      checkBypassDocumentValidationIsSupported(bypassDocumentValidation, writeConcern);
      validateWriteRequestCollations(requests, writeConcern);
      validateUpdateRequestArrayFilters(requests, writeConcern);
      validateWriteRequestHints(connectionDescription, requests, writeConcern);
   }

   static <R> boolean validateWriteRequestsAndCompleteIfInvalid(ConnectionDescription connectionDescription, Boolean bypassDocumentValidation, List<? extends WriteRequest> requests, WriteConcern writeConcern, SingleResultCallback<R> callback) {
      try {
         validateWriteRequests(connectionDescription, bypassDocumentValidation, requests, writeConcern);
         return false;
      } catch (Throwable throwable) {
         callback.onResult(null, throwable);
         return true;
      }
   }

   private static void checkBypassDocumentValidationIsSupported(@Nullable Boolean bypassDocumentValidation, WriteConcern writeConcern) {
      if (bypassDocumentValidation != null && !writeConcern.isAcknowledged()) {
         throw new MongoClientException("Specifying bypassDocumentValidation with an unacknowledged WriteConcern is not supported");
      }
   }

   static boolean isRetryableWrite(boolean retryWrites, WriteConcern writeConcern, ConnectionDescription connectionDescription, SessionContext sessionContext) {
      if (!retryWrites) {
         return false;
      } else if (!writeConcern.isAcknowledged()) {
         LOGGER.debug("retryWrites set to true but the writeConcern is unacknowledged.");
         return false;
      } else if (sessionContext.hasActiveTransaction()) {
         LOGGER.debug("retryWrites set to true but in an active transaction.");
         return false;
      } else {
         return canRetryWrite(connectionDescription, sessionContext);
      }
   }

   static boolean canRetryWrite(ConnectionDescription connectionDescription, SessionContext sessionContext) {
      if (connectionDescription.getLogicalSessionTimeoutMinutes() == null) {
         LOGGER.debug("retryWrites set to true but the server does not support sessions.");
         return false;
      } else if (connectionDescription.getServerType().equals(ServerType.STANDALONE)) {
         LOGGER.debug("retryWrites set to true but the server is a standalone server.");
         return false;
      } else {
         return true;
      }
   }

   static boolean canRetryRead(ServerDescription serverDescription, SessionContext sessionContext) {
      if (sessionContext.hasActiveTransaction()) {
         LOGGER.debug("retryReads set to true but in an active transaction.");
         return false;
      } else {
         return true;
      }
   }

   static <T> QueryBatchCursor<T> createEmptyBatchCursor(MongoNamespace namespace, Decoder<T> decoder, ServerAddress serverAddress, int batchSize) {
      return new QueryBatchCursor(new QueryResult(namespace, Collections.emptyList(), 0L, serverAddress), 0, batchSize, decoder);
   }

   static <T> QueryResult<T> cursorDocumentToQueryResult(BsonDocument cursorDocument, ServerAddress serverAddress) {
      return cursorDocumentToQueryResult(cursorDocument, serverAddress, "firstBatch");
   }

   static <T> QueryResult<T> cursorDocumentToQueryResult(BsonDocument cursorDocument, ServerAddress serverAddress, String fieldNameContainingBatch) {
      long cursorId = ((BsonInt64)cursorDocument.get("id")).getValue();
      MongoNamespace queryResultNamespace = new MongoNamespace(cursorDocument.getString("ns").getValue());
      return new QueryResult(queryResultNamespace, BsonDocumentWrapperHelper.toList(cursorDocument, fieldNameContainingBatch), cursorId, serverAddress);
   }

   private OperationHelper() {
   }

   public static final class ResourceSupplierInternalException extends RuntimeException {
      private static final long serialVersionUID = 0L;

      ResourceSupplierInternalException(Throwable cause) {
         super((Throwable)Assertions.assertNotNull(cause));
      }

      @NonNull
      public Throwable getCause() {
         return (Throwable)Assertions.assertNotNull(super.getCause());
      }
   }
}
