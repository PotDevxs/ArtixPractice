package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.io.BsonOutput;

public class SplittablePayloadBsonWriter extends LevelCountingBsonWriter {
   private final BsonWriter writer;
   private final BsonOutput bsonOutput;
   private final SplittablePayload payload;
   private final int maxSplittableDocumentSize;
   private final MessageSettings settings;
   private final int messageStartPosition;

   public SplittablePayloadBsonWriter(BsonBinaryWriter writer, BsonOutput bsonOutput, MessageSettings settings, SplittablePayload payload, int maxSplittableDocumentSize) {
      this(writer, bsonOutput, 0, settings, payload, maxSplittableDocumentSize);
   }

   public SplittablePayloadBsonWriter(BsonBinaryWriter writer, BsonOutput bsonOutput, int messageStartPosition, MessageSettings settings, SplittablePayload payload) {
      this(writer, bsonOutput, messageStartPosition, settings, payload, settings.getMaxDocumentSize());
   }

   public SplittablePayloadBsonWriter(BsonBinaryWriter writer, BsonOutput bsonOutput, int messageStartPosition, MessageSettings settings, SplittablePayload payload, int maxSplittableDocumentSize) {
      super(writer);
      this.writer = writer;
      this.bsonOutput = bsonOutput;
      this.messageStartPosition = messageStartPosition;
      this.settings = settings;
      this.payload = payload;
      this.maxSplittableDocumentSize = maxSplittableDocumentSize;
   }

   public void writeStartDocument() {
      super.writeStartDocument();
   }

   public void writeEndDocument() {
      if (this.getCurrentLevel() == 0 && this.payload.hasPayload()) {
         BsonWriterHelper.writePayloadArray(this.writer, this.bsonOutput, this.settings, this.messageStartPosition, this.payload, this.maxSplittableDocumentSize);
      }

      super.writeEndDocument();
   }
}
