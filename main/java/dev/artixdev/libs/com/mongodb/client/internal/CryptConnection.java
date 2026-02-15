package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.binding.BindingContext;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.MessageSettings;
import dev.artixdev.libs.com.mongodb.internal.connection.SplittablePayload;
import dev.artixdev.libs.com.mongodb.internal.connection.SplittablePayloadBsonWriter;
import dev.artixdev.libs.com.mongodb.internal.operation.ServerVersionHelper;
import dev.artixdev.libs.com.mongodb.internal.validator.MappedFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonBinaryWriterSettings;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.BsonWriterSettings;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.RawBsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.io.BasicOutputBuffer;

class CryptConnection implements Connection {
   private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
   private static final int MAX_SPLITTABLE_DOCUMENT_SIZE = 2097152;
   private final Connection wrapped;
   private final Crypt crypt;

   CryptConnection(Connection wrapped, Crypt crypt) {
      this.wrapped = wrapped;
      this.crypt = crypt;
   }

   public int getCount() {
      return this.wrapped.getCount();
   }

   public CryptConnection retain() {
      this.wrapped.retain();
      return this;
   }

   public int release() {
      return this.wrapped.release();
   }

   public ConnectionDescription getDescription() {
      return this.wrapped.getDescription();
   }

   @Nullable
   public <T> T command(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator) {
      if (ServerVersionHelper.serverIsLessThanVersionFourDotTwo(this.wrapped.getDescription())) {
         throw new MongoClientException("Auto-encryption requires a minimum MongoDB version of 4.2");
      } else {
         BasicOutputBuffer bsonOutput = new BasicOutputBuffer();
         BsonBinaryWriter bsonBinaryWriter = new BsonBinaryWriter(new BsonWriterSettings(), new BsonBinaryWriterSettings(this.getDescription().getMaxDocumentSize()), bsonOutput, this.getFieldNameValidator(payload, commandFieldNameValidator, payloadFieldNameValidator));
         BsonWriter writer = payload == null ? bsonBinaryWriter : new SplittablePayloadBsonWriter(bsonBinaryWriter, bsonOutput, this.createSplittablePayloadMessageSettings(), payload, 2097152);
         this.getEncoder(command).encode((BsonWriter)writer, command, EncoderContext.builder().build());
         RawBsonDocument encryptedCommand = this.crypt.encrypt(database, new RawBsonDocument(bsonOutput.getInternalBuffer(), 0, bsonOutput.getSize()));
         RawBsonDocument encryptedResponse = (RawBsonDocument)this.wrapped.command(database, encryptedCommand, commandFieldNameValidator, readPreference, new RawBsonDocumentCodec(), context, responseExpected, (SplittablePayload)null, (FieldNameValidator)null);
         if (encryptedResponse == null) {
            return null;
         } else {
            RawBsonDocument decryptedResponse = this.crypt.decrypt(encryptedResponse);
            BsonBinaryReader reader = new BsonBinaryReader(decryptedResponse.getByteBuffer().asNIO());
            return commandResultDecoder.decode(reader, DecoderContext.builder().build());
         }
      }
   }

   @Nullable
   public <T> T command(String database, BsonDocument command, FieldNameValidator fieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context) {
      return this.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, context, true, (SplittablePayload)null, (FieldNameValidator)null);
   }

   private Codec<BsonDocument> getEncoder(BsonDocument command) {
      return (Codec<BsonDocument>) REGISTRY.get(command.getClass());
   }

   private FieldNameValidator getFieldNameValidator(@Nullable SplittablePayload payload, FieldNameValidator commandFieldNameValidator, @Nullable FieldNameValidator payloadFieldNameValidator) {
      if (payload == null) {
         return commandFieldNameValidator;
      } else {
         Map<String, FieldNameValidator> rootMap = new HashMap();
         rootMap.put(payload.getPayloadName(), payloadFieldNameValidator);
         return new MappedFieldNameValidator(commandFieldNameValidator, rootMap);
      }
   }

   private MessageSettings createSplittablePayloadMessageSettings() {
      return MessageSettings.builder().maxBatchCount(this.getDescription().getMaxBatchCount()).maxMessageSize(this.getDescription().getMaxMessageSize()).maxDocumentSize(this.getDescription().getMaxDocumentSize()).build();
   }

   public void markAsPinned(Connection.PinningMode pinningMode) {
      this.wrapped.markAsPinned(pinningMode);
   }
}
