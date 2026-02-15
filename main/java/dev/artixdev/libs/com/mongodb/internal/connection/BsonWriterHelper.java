package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonElement;
import dev.artixdev.libs.org.bson.BsonMaximumSizeExceededException;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.io.BsonOutput;

final class BsonWriterHelper {
   private static final int DOCUMENT_HEADROOM = 16384;
   private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
   private static final EncoderContext ENCODER_CONTEXT = EncoderContext.builder().build();

   static void writeElements(BsonWriter writer, List<BsonElement> bsonElements) {
      Iterator<BsonElement> iterator = bsonElements.iterator();

      while(iterator.hasNext()) {
         BsonElement bsonElement = iterator.next();
         writer.writeName(bsonElement.getName());
         getCodec(bsonElement.getValue()).encode(writer, bsonElement.getValue(), ENCODER_CONTEXT);
      }

   }

   static void writePayloadArray(BsonWriter writer, BsonOutput bsonOutput, MessageSettings settings, int messageStartPosition, SplittablePayload payload, int maxSplittableDocumentSize) {
      writer.writeStartArray(payload.getPayloadName());
      writePayload(writer, bsonOutput, getDocumentMessageSettings(settings), messageStartPosition, payload, maxSplittableDocumentSize);
      writer.writeEndArray();
   }

   static void writePayload(BsonWriter writer, BsonOutput bsonOutput, MessageSettings settings, int messageStartPosition, SplittablePayload payload, int maxSplittableDocumentSize) {
      MessageSettings payloadSettings = getPayloadMessageSettings(payload.getPayloadType(), settings);
      List<BsonDocument> payloadDocuments = payload.getPayload();

      for(int i = 0; i < payloadDocuments.size() && writeDocument(writer, bsonOutput, payloadSettings, (BsonDocument)payloadDocuments.get(i), messageStartPosition, i + 1, maxSplittableDocumentSize); ++i) {
         payload.setPosition(i + 1);
      }

      if (payload.getPosition() == 0) {
         throw new BsonMaximumSizeExceededException(String.format("Payload document size is larger than maximum of %d.", payloadSettings.getMaxDocumentSize()));
      }
   }

   private static boolean writeDocument(BsonWriter writer, BsonOutput bsonOutput, MessageSettings settings, BsonDocument document, int messageStartPosition, int batchItemCount, int maxSplittableDocumentSize) {
      int currentPosition = bsonOutput.getPosition();
      getCodec(document).encode(writer, document, ENCODER_CONTEXT);
      int messageSize = bsonOutput.getPosition() - messageStartPosition;
      int documentSize = bsonOutput.getPosition() - currentPosition;
      if (!exceedsLimits(settings, messageSize, documentSize, batchItemCount) && (batchItemCount <= 1 || bsonOutput.getPosition() - messageStartPosition <= maxSplittableDocumentSize)) {
         return true;
      } else {
         bsonOutput.truncateToPosition(currentPosition);
         return false;
      }
   }

   private static Codec<BsonValue> getCodec(BsonValue bsonValue) {
      return (Codec<BsonValue>) REGISTRY.get(bsonValue.getClass());
   }

   private static MessageSettings getPayloadMessageSettings(SplittablePayload.Type type, MessageSettings settings) {
      MessageSettings payloadMessageSettings = settings;
      if (type != SplittablePayload.Type.INSERT) {
         payloadMessageSettings = createMessageSettingsBuilder(settings).maxDocumentSize(settings.getMaxDocumentSize() + 16384).build();
      }

      return payloadMessageSettings;
   }

   private static MessageSettings getDocumentMessageSettings(MessageSettings settings) {
      return createMessageSettingsBuilder(settings).maxMessageSize(settings.getMaxDocumentSize() + 16384).build();
   }

   private static MessageSettings.Builder createMessageSettingsBuilder(MessageSettings settings) {
      return MessageSettings.builder().maxBatchCount(settings.getMaxBatchCount()).maxMessageSize(settings.getMaxMessageSize()).maxDocumentSize(settings.getMaxDocumentSize()).maxWireVersion(settings.getMaxWireVersion());
   }

   private static boolean exceedsLimits(MessageSettings settings, int messageSize, int documentSize, int batchItemCount) {
      if (batchItemCount > settings.getMaxBatchCount()) {
         return true;
      } else if (messageSize > settings.getMaxMessageSize()) {
         return true;
      } else {
         return documentSize > settings.getMaxDocumentSize();
      }
   }

   private BsonWriterHelper() {
   }
}
