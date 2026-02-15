package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import dev.artixdev.libs.com.mongodb.LoggerSettings;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.internal.logging.LogMessage;
import dev.artixdev.libs.com.mongodb.internal.logging.StructuredLogger;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.codecs.RawBsonDocumentCodec;
import dev.artixdev.libs.org.bson.json.JsonMode;
import dev.artixdev.libs.org.bson.json.JsonWriter;
import dev.artixdev.libs.org.bson.json.JsonWriterSettings;

class LoggingCommandEventSender implements CommandEventSender {
   private static final double NANOS_PER_MILLI = 1000000.0D;
   private final ConnectionDescription description;
   @Nullable
   private final CommandListener commandListener;
   private final RequestContext requestContext;
   private final OperationContext operationContext;
   private final StructuredLogger logger;
   private final LoggerSettings loggerSettings;
   private final long startTimeNanos;
   private final CommandMessage message;
   private final String commandName;
   private volatile BsonDocument commandDocument;
   private final boolean redactionRequired;

   LoggingCommandEventSender(Set<String> securitySensitiveCommands, Set<String> securitySensitiveHelloCommands, ConnectionDescription description, @Nullable CommandListener commandListener, RequestContext requestContext, OperationContext operationContext, CommandMessage message, ByteBufferBsonOutput bsonOutput, StructuredLogger logger, LoggerSettings loggerSettings) {
      this.description = description;
      this.commandListener = commandListener;
      this.requestContext = requestContext;
      this.operationContext = operationContext;
      this.logger = logger;
      this.loggerSettings = loggerSettings;
      this.startTimeNanos = System.nanoTime();
      this.message = message;
      this.commandDocument = message.getCommandDocument(bsonOutput);
      this.commandName = this.commandDocument.getFirstKey();
      this.redactionRequired = securitySensitiveCommands.contains(this.commandName) || securitySensitiveHelloCommands.contains(this.commandName) && this.commandDocument.containsKey("speculativeAuthenticate");
   }

   public void sendStartedEvent() {
      if (this.loggingRequired()) {
         String messagePrefix = "Command \"{}\" started on database \"{}\"";
         String command = this.redactionRequired ? "{}" : this.getTruncatedJsonCommand(this.commandDocument);
         this.logEventMessage(messagePrefix, (String)"Command started", (Throwable)null, (entries) -> {
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.COMMAND_NAME, this.commandName));
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DATABASE_NAME, this.message.getNamespace().getDatabaseName()));
         }, (entries) -> {
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.COMMAND_CONTENT, command));
         });
      }

      if (this.eventRequired()) {
         BsonDocument commandDocumentForEvent = this.redactionRequired ? new BsonDocument() : this.commandDocument;
         ProtocolHelper.sendCommandStartedEvent(this.message, this.message.getNamespace().getDatabaseName(), this.commandName, commandDocumentForEvent, this.description, (CommandListener)Assertions.assertNotNull(this.commandListener), this.requestContext, this.operationContext);
      }

      this.commandDocument = null;
   }

   public void sendFailedEvent(Throwable t) {
      Throwable commandEventException = t;
      if (t instanceof MongoCommandException && this.redactionRequired) {
         MongoCommandException originalCommandException = (MongoCommandException)t;
         commandEventException = new MongoCommandException(new BsonDocument(), originalCommandException.getServerAddress());
         ((Throwable)commandEventException).setStackTrace(t.getStackTrace());
      }

      long elapsedTimeNanos = System.nanoTime() - this.startTimeNanos;
      if (this.loggingRequired()) {
         String messagePrefix = "Command \"{}\" failed on database \"{}\" in {} ms";
         this.logEventMessage(messagePrefix, (String)"Command failed", (Throwable)commandEventException, (entries) -> {
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.COMMAND_NAME, this.commandName));
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DATABASE_NAME, this.message.getNamespace().getDatabaseName()));
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DURATION_MS, (double)elapsedTimeNanos / 1000000.0D));
         }, (entries) -> {
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.COMMAND_CONTENT, (Object)null));
         });
      }

      if (this.eventRequired()) {
         ProtocolHelper.sendCommandFailedEvent(this.message, this.message.getNamespace().getDatabaseName(), this.commandName, this.description, elapsedTimeNanos, (Throwable)commandEventException, this.commandListener, this.requestContext, this.operationContext);
      }

   }

   public void sendSucceededEvent(ResponseBuffers responseBuffers) {
      this.sendSucceededEvent(responseBuffers.getResponseDocument(this.message.getId(), new RawBsonDocumentCodec()));
   }

   public void sendSucceededEventForOneWayCommand() {
      this.sendSucceededEvent(new BsonDocument("ok", new BsonInt32(1)));
   }

   private void sendSucceededEvent(BsonDocument reply) {
      long elapsedTimeNanos = System.nanoTime() - this.startTimeNanos;
      if (this.loggingRequired()) {
         String format = "Command \"{}\" succeeded on database \"{}\" in {} ms using a connection with driver-generated ID {}[ and server-generated ID {}] to {}:{}[ with service ID {}]. The request ID is {} and the operation ID is {}. Command reply: {}";
         BsonDocument responseDocumentForEvent = this.redactionRequired ? new BsonDocument() : reply;
         String replyString = this.redactionRequired ? "{}" : this.getTruncatedJsonCommand(responseDocumentForEvent);
         this.logEventMessage("Command succeeded", null, (entries) -> {
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.COMMAND_NAME, this.commandName));
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DATABASE_NAME, this.message.getNamespace().getDatabaseName()));
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DURATION_MS, (double)elapsedTimeNanos / 1000000.0D));
         }, (entries) -> {
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.REPLY, replyString));
         }, (String)format);
      }

      if (this.eventRequired()) {
         BsonDocument responseDocumentForEvent = this.redactionRequired ? new BsonDocument() : reply;
         ProtocolHelper.sendCommandSucceededEvent(this.message, this.message.getNamespace().getDatabaseName(), this.commandName, responseDocumentForEvent, this.description, elapsedTimeNanos, this.commandListener, this.requestContext, this.operationContext);
      }

   }

   private boolean loggingRequired() {
      return this.logger.isRequired(LogMessage.Level.DEBUG, this.getClusterId());
   }

   private ClusterId getClusterId() {
      return this.description.getConnectionId().getServerId().getClusterId();
   }

   private boolean eventRequired() {
      return this.commandListener != null;
   }

   private void logEventMessage(String messagePrefix, String messageId, @Nullable Throwable exception, Consumer<List<LogMessage.Entry>> prefixEntriesMutator, Consumer<List<LogMessage.Entry>> suffixEntriesMutator) {
      String format = messagePrefix + " using a connection with driver-generated ID {}[ and server-generated ID {}] to {}:{}[ with service ID {}]. The request ID is {} and the operation ID is {}.[ Command: {}]";
      this.logEventMessage(messageId, exception, prefixEntriesMutator, suffixEntriesMutator, format);
   }

   private void logEventMessage(String messageId, @Nullable Throwable exception, Consumer<List<LogMessage.Entry>> prefixEntriesMutator, Consumer<List<LogMessage.Entry>> suffixEntriesMutator, String format) {
      List<LogMessage.Entry> entries = new ArrayList();
      prefixEntriesMutator.accept(entries);
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DRIVER_CONNECTION_ID, this.description.getConnectionId().getLocalValue()));
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.SERVER_CONNECTION_ID, this.description.getConnectionId().getServerValue()));
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.SERVER_HOST, this.description.getServerAddress().getHost()));
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.SERVER_PORT, this.description.getServerAddress().getPort()));
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.SERVICE_ID, this.description.getServiceId()));
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.REQUEST_ID, this.message.getId()));
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.OPERATION_ID, this.operationContext.getId()));
      suffixEntriesMutator.accept(entries);
      this.logger.log(new LogMessage(LogMessage.Component.COMMAND, LogMessage.Level.DEBUG, messageId, this.getClusterId(), exception, entries, format));
   }

   private String getTruncatedJsonCommand(BsonDocument commandDocument) {
      StringWriter writer = new StringWriter();
      BsonReader bsonReader = commandDocument.asBsonReader();

      String var5;
      try {
         JsonWriter jsonWriter = new JsonWriter(writer, JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).maxLength(this.loggerSettings.getMaxDocumentLength()).build());
         jsonWriter.pipe(bsonReader);
         if (jsonWriter.isTruncated()) {
            writer.append(" ...");
         }

         var5 = writer.toString();
      } catch (Throwable throwable) {
         if (bsonReader != null) {
            try {
               bsonReader.close();
            } catch (Throwable suppressed) {
               throwable.addSuppressed(suppressed);
            }
         }

         throw throwable;
      }

      if (bsonReader != null) {
         bsonReader.close();
      }

      return var5;
   }
}
