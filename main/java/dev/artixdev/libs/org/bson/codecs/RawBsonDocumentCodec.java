package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.io.BasicOutputBuffer;
import dev.artixdev.libs.org.bson.io.ByteBufferBsonInput;

public class RawBsonDocumentCodec implements Codec<RawBsonDocument> {
   public void encode(BsonWriter writer, RawBsonDocument value, EncoderContext encoderContext) {
      BsonBinaryReader reader = new BsonBinaryReader(new ByteBufferBsonInput(value.getByteBuffer()));

      try {
         writer.pipe(reader);
      } catch (Throwable e) {
         try {
            reader.close();
         } catch (Throwable suppressed) {
            e.addSuppressed(suppressed);
         }

         throw e;
      }

      reader.close();
   }

   public RawBsonDocument decode(BsonReader reader, DecoderContext decoderContext) {
      BasicOutputBuffer buffer = new BasicOutputBuffer(0);
      BsonBinaryWriter writer = new BsonBinaryWriter(buffer);

      RawBsonDocument var5;
      try {
         writer.pipe(reader);
         var5 = new RawBsonDocument(buffer.getInternalBuffer(), 0, buffer.getPosition());
      } finally {
         writer.close();
         buffer.close();
      }

      return var5;
   }

   public Class<RawBsonDocument> getEncoderClass() {
      return RawBsonDocument.class;
   }
}
