package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonBinaryWriterSettings;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonElement;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.BsonWriterSettings;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.io.BsonOutput;

abstract class RequestMessage {
   static final AtomicInteger REQUEST_ID = new AtomicInteger(1);
   static final int MESSAGE_PROLOGUE_LENGTH = 16;
   private static final int DOCUMENT_HEADROOM = 16384;
   private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
   private final String collectionName;
   private final MessageSettings settings;
   private final int id;
   private final OpCode opCode;
   private RequestMessage.EncodingMetadata encodingMetadata;

   public static int getCurrentGlobalId() {
      return REQUEST_ID.get();
   }

   RequestMessage(OpCode opCode, int requestId, MessageSettings settings) {
      this((String)null, opCode, requestId, settings);
   }

   RequestMessage(String collectionName, OpCode opCode, MessageSettings settings) {
      this(collectionName, opCode, REQUEST_ID.getAndIncrement(), settings);
   }

   private RequestMessage(@Nullable String collectionName, OpCode opCode, int requestId, MessageSettings settings) {
      this.collectionName = collectionName;
      this.settings = settings;
      this.id = requestId;
      this.opCode = opCode;
   }

   public int getId() {
      return this.id;
   }

   public OpCode getOpCode() {
      return this.opCode;
   }

   public MessageSettings getSettings() {
      return this.settings;
   }

   public void encode(BsonOutput bsonOutput, SessionContext sessionContext) {
      Assertions.notNull("sessionContext", sessionContext);
      int messageStartPosition = bsonOutput.getPosition();
      this.writeMessagePrologue(bsonOutput);
      RequestMessage.EncodingMetadata encodingMetadata = this.encodeMessageBodyWithMetadata(bsonOutput, sessionContext);
      this.backpatchMessageLength(messageStartPosition, bsonOutput);
      this.encodingMetadata = encodingMetadata;
   }

   public RequestMessage.EncodingMetadata getEncodingMetadata() {
      return this.encodingMetadata;
   }

   protected void writeMessagePrologue(BsonOutput bsonOutput) {
      bsonOutput.writeInt32(0);
      bsonOutput.writeInt32(this.id);
      bsonOutput.writeInt32(0);
      bsonOutput.writeInt32(this.opCode.getValue());
   }

   protected abstract RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput, SessionContext sessionContext);

   protected void addDocument(BsonDocument document, BsonOutput bsonOutput, FieldNameValidator validator, @Nullable List<BsonElement> extraElements) {
      this.addDocument(document, this.getCodec(document), EncoderContext.builder().build(), bsonOutput, validator, this.settings.getMaxDocumentSize() + 16384, extraElements);
   }

   protected void backpatchMessageLength(int startPosition, BsonOutput bsonOutput) {
      int messageLength = bsonOutput.getPosition() - startPosition;
      bsonOutput.writeInt32(bsonOutput.getPosition() - messageLength, messageLength);
   }

   protected String getCollectionName() {
      return this.collectionName;
   }

   Codec<BsonDocument> getCodec(BsonDocument document) {
      return (Codec<BsonDocument>) REGISTRY.get(document.getClass());
   }

   private <T> void addDocument(T obj, Encoder<T> encoder, EncoderContext encoderContext, BsonOutput bsonOutput, FieldNameValidator validator, int maxDocumentSize, @Nullable List<BsonElement> extraElements) {
      BsonBinaryWriter bsonBinaryWriter = new BsonBinaryWriter(new BsonWriterSettings(), new BsonBinaryWriterSettings(maxDocumentSize), bsonOutput, validator);
      BsonWriter bsonWriter = extraElements == null ? bsonBinaryWriter : new ElementExtendingBsonWriter(bsonBinaryWriter, extraElements);
      encoder.encode((BsonWriter)bsonWriter, obj, encoderContext);
   }

   static class EncodingMetadata {
      private final int firstDocumentPosition;

      EncodingMetadata(int firstDocumentPosition) {
         this.firstDocumentPosition = firstDocumentPosition;
      }

      public int getFirstDocumentPosition() {
         return this.firstDocumentPosition;
      }
   }
}
