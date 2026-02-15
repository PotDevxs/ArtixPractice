package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.ErrorCategory;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoExecutionTimeoutException;
import dev.artixdev.libs.com.mongodb.MongoNodeIsRecoveringException;
import dev.artixdev.libs.com.mongodb.MongoNotPrimaryException;
import dev.artixdev.libs.com.mongodb.MongoQueryException;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.event.CommandFailedEvent;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.event.CommandStartedEvent;
import dev.artixdev.libs.com.mongodb.event.CommandSucceededEvent;
import dev.artixdev.libs.com.mongodb.internal.IgnorableRequestContext;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.io.ByteBufferBsonInput;

public final class ProtocolHelper {
   private static final Logger PROTOCOL_EVENT_LOGGER = Loggers.getLogger("protocol.event");
   private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
   private static final int NO_ERROR_CODE = -1;
   private static final List<Integer> NOT_PRIMARY_CODES = Arrays.asList(10107, 13435, 10058);
   private static final List<String> NOT_PRIMARY_MESSAGES = Collections.singletonList("not master");
   private static final List<Integer> RECOVERING_CODES = Arrays.asList(11600, 11602, 13436, 189, 91);
   private static final List<String> RECOVERING_MESSAGES = Arrays.asList("not master or secondary", "node is recovering");

   static boolean isCommandOk(BsonDocument response) {
      BsonValue okValue = response.get("ok");
      return isCommandOk(okValue);
   }

   static boolean isCommandOk(BsonReader bsonReader) {
      return isCommandOk(getField(bsonReader, "ok"));
   }

   static boolean isCommandOk(ResponseBuffers responseBuffers) {
      boolean var1;
      try {
         var1 = isCommandOk((BsonReader)createBsonReader(responseBuffers));
      } finally {
         responseBuffers.reset();
      }

      return var1;
   }

   @Nullable
   static MongoException createSpecialWriteConcernException(ResponseBuffers responseBuffers, ServerAddress serverAddress) {
      BsonValue writeConcernError = getField(createBsonReader(responseBuffers), "writeConcernError");
      return writeConcernError == null ? null : createSpecialException(writeConcernError.asDocument(), serverAddress, "errmsg");
   }

   @Nullable
   static BsonTimestamp getOperationTime(ResponseBuffers responseBuffers) {
      return getFieldValueAsTimestamp(responseBuffers, "operationTime");
   }

   @Nullable
   static BsonDocument getClusterTime(ResponseBuffers responseBuffers) {
      return getFieldValueAsDocument(responseBuffers, "$clusterTime");
   }

   @Nullable
   static BsonTimestamp getSnapshotTimestamp(ResponseBuffers responseBuffers) {
      BsonValue atClusterTimeValue = getNestedFieldValue(responseBuffers, "cursor", "atClusterTime");
      if (atClusterTimeValue == null) {
         atClusterTimeValue = getFieldValue(responseBuffers, "atClusterTime");
      }

      return atClusterTimeValue != null && atClusterTimeValue.isTimestamp() ? atClusterTimeValue.asTimestamp() : null;
   }

   @Nullable
   static BsonDocument getRecoveryToken(ResponseBuffers responseBuffers) {
      return getFieldValueAsDocument(responseBuffers, "recoveryToken");
   }

   @Nullable
   private static BsonTimestamp getFieldValueAsTimestamp(ResponseBuffers responseBuffers, String fieldName) {
      BsonValue value = getFieldValue(responseBuffers, fieldName);
      return value == null ? null : value.asTimestamp();
   }

   @Nullable
   private static BsonDocument getFieldValueAsDocument(ResponseBuffers responseBuffers, String fieldName) {
      BsonValue value = getFieldValue(responseBuffers, fieldName);
      return value == null ? null : value.asDocument();
   }

   @Nullable
   private static BsonValue getFieldValue(ResponseBuffers responseBuffers, String fieldName) {
      BsonValue var2;
      try {
         var2 = getField(createBsonReader(responseBuffers), fieldName);
      } finally {
         responseBuffers.reset();
      }

      return var2;
   }

   private static BsonBinaryReader createBsonReader(ResponseBuffers responseBuffers) {
      return new BsonBinaryReader(new ByteBufferBsonInput(responseBuffers.getBodyByteBuffer()));
   }

   @Nullable
   private static BsonValue getField(BsonReader bsonReader, String fieldName) {
      bsonReader.readStartDocument();

      while(bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         if (bsonReader.readName().equals(fieldName)) {
            return (BsonValue)REGISTRY.get(BsonValueCodecProvider.getClassForBsonType(bsonReader.getCurrentBsonType())).decode(bsonReader, DecoderContext.builder().build());
         }

         bsonReader.skipValue();
      }

      bsonReader.readEndDocument();
      return null;
   }

   @Nullable
   private static BsonValue getNestedFieldValue(ResponseBuffers responseBuffers, String topLevelFieldName, String nestedFieldName) {
      BsonValue var4;
      try {
         BsonReader bsonReader = createBsonReader(responseBuffers);
         bsonReader.readStartDocument();

         while(bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (bsonReader.readName().equals(topLevelFieldName)) {
               var4 = getField(bsonReader, nestedFieldName);
               return var4;
            }

            bsonReader.skipValue();
         }

         bsonReader.readEndDocument();
         var4 = null;
      } finally {
         responseBuffers.reset();
      }

      return var4;
   }

   private static boolean isCommandOk(@Nullable BsonValue okValue) {
      if (okValue == null) {
         return false;
      } else if (okValue.isBoolean()) {
         return okValue.asBoolean().getValue();
      } else if (okValue.isNumber()) {
         return okValue.asNumber().intValue() == 1;
      } else {
         return false;
      }
   }

   static MongoException getCommandFailureException(BsonDocument response, ServerAddress serverAddress) {
      MongoException specialException = createSpecialException(response, serverAddress, "errmsg");
      return (MongoException)(specialException != null ? specialException : new MongoCommandException(response, serverAddress));
   }

   static int getErrorCode(BsonDocument response) {
      return response.getNumber("code", new BsonInt32(-1)).intValue();
   }

   static String getErrorMessage(BsonDocument response, String errorMessageFieldName) {
      return response.getString(errorMessageFieldName, new BsonString("")).getValue();
   }

   static MongoException getQueryFailureException(BsonDocument errorDocument, ServerAddress serverAddress) {
      MongoException specialException = createSpecialException(errorDocument, serverAddress, "$err");
      return (MongoException)(specialException != null ? specialException : new MongoQueryException(errorDocument, serverAddress));
   }

   static MessageSettings getMessageSettings(ConnectionDescription connectionDescription) {
      return MessageSettings.builder().maxDocumentSize(connectionDescription.getMaxDocumentSize()).maxMessageSize(connectionDescription.getMaxMessageSize()).maxBatchCount(connectionDescription.getMaxBatchCount()).maxWireVersion(connectionDescription.getMaxWireVersion()).serverType(connectionDescription.getServerType()).sessionSupported(connectionDescription.getLogicalSessionTimeoutMinutes() != null).build();
   }

   @Nullable
   public static MongoException createSpecialException(@Nullable BsonDocument response, ServerAddress serverAddress, String errorMessageFieldName) {
      if (response == null) {
         return null;
      } else {
         int errorCode = getErrorCode(response);
         String errorMessage = getErrorMessage(response, errorMessageFieldName);
         if (ErrorCategory.fromErrorCode(errorCode) == ErrorCategory.EXECUTION_TIMEOUT) {
            return new MongoExecutionTimeoutException(errorCode, errorMessage, response);
         } else if (isNodeIsRecoveringError(errorCode, errorMessage)) {
            return new MongoNodeIsRecoveringException(response, serverAddress);
         } else if (isNotPrimaryError(errorCode, errorMessage)) {
            return new MongoNotPrimaryException(response, serverAddress);
         } else if (!response.containsKey("writeConcernError")) {
            return null;
         } else {
            MongoException writeConcernException = createSpecialException(response.getDocument("writeConcernError"), serverAddress, "errmsg");
            if (writeConcernException != null && response.isArray("errorLabels")) {
               Iterator<BsonValue> iterator = response.getArray("errorLabels").iterator();

               while(iterator.hasNext()) {
                  BsonValue errorLabel = iterator.next();
                  writeConcernException.addLabel(errorLabel.asString().getValue());
               }
            }

            return writeConcernException;
         }
      }
   }

   private static boolean isNotPrimaryError(int errorCode, String errorMessage) {
      boolean var2;
      if (!NOT_PRIMARY_CODES.contains(errorCode)) {
         label26: {
            if (errorCode == -1) {
               Objects.requireNonNull(errorMessage);
               if (NOT_PRIMARY_MESSAGES.stream().anyMatch(errorMessage::contains)) {
                  break label26;
               }
            }

            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   private static boolean isNodeIsRecoveringError(int errorCode, String errorMessage) {
      boolean var2;
      if (!RECOVERING_CODES.contains(errorCode)) {
         label26: {
            if (errorCode == -1) {
               Objects.requireNonNull(errorMessage);
               if (RECOVERING_MESSAGES.stream().anyMatch(errorMessage::contains)) {
                  break label26;
               }
            }

            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   static void sendCommandStartedEvent(RequestMessage message, String databaseName, String commandName, BsonDocument command, ConnectionDescription connectionDescription, CommandListener commandListener, RequestContext requestContext, OperationContext operationContext) {
      Assertions.notNull("requestContext", requestContext);

      try {
         commandListener.commandStarted(new CommandStartedEvent(getRequestContextForEvent(requestContext), operationContext.getId(), message.getId(), connectionDescription, databaseName, commandName, command));
      } catch (Exception exception) {
         if (PROTOCOL_EVENT_LOGGER.isWarnEnabled()) {
            PROTOCOL_EVENT_LOGGER.warn(String.format("Exception thrown raising command started event to listener %s", commandListener), exception);
         }
      }

   }

   static void sendCommandSucceededEvent(RequestMessage message, String databaseName, String commandName, BsonDocument response, ConnectionDescription connectionDescription, long elapsedTimeNanos, CommandListener commandListener, RequestContext requestContext, OperationContext operationContext) {
      Assertions.notNull("requestContext", requestContext);

      try {
         commandListener.commandSucceeded(new CommandSucceededEvent(getRequestContextForEvent(requestContext), operationContext.getId(), message.getId(), connectionDescription, databaseName, commandName, response, elapsedTimeNanos));
      } catch (Exception exception) {
         if (PROTOCOL_EVENT_LOGGER.isWarnEnabled()) {
            PROTOCOL_EVENT_LOGGER.warn(String.format("Exception thrown raising command succeeded event to listener %s", commandListener), exception);
         }
      }

   }

   static void sendCommandFailedEvent(RequestMessage message, String databaseName, String commandName, ConnectionDescription connectionDescription, long elapsedTimeNanos, Throwable throwable, CommandListener commandListener, RequestContext requestContext, OperationContext operationContext) {
      Assertions.notNull("requestContext", requestContext);

      try {
         commandListener.commandFailed(new CommandFailedEvent(getRequestContextForEvent(requestContext), operationContext.getId(), message.getId(), connectionDescription, databaseName, commandName, elapsedTimeNanos, throwable));
      } catch (Exception exception) {
         if (PROTOCOL_EVENT_LOGGER.isWarnEnabled()) {
            PROTOCOL_EVENT_LOGGER.warn(String.format("Exception thrown raising command failed event to listener %s", commandListener), exception);
         }
      }

   }

   @Nullable
   private static RequestContext getRequestContextForEvent(RequestContext requestContext) {
      return requestContext == IgnorableRequestContext.INSTANCE ? null : requestContext;
   }

   private ProtocolHelper() {
   }
}
