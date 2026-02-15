package dev.artixdev.libs.org.bson.codecs;

import java.util.UUID;
import dev.artixdev.libs.org.bson.BSONException;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonBinarySubType;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.internal.UuidHelper;

public class UuidCodec implements Codec<UUID> {
   private final UuidRepresentation uuidRepresentation;

   public UuidCodec(UuidRepresentation uuidRepresentation) {
      Assertions.notNull("uuidRepresentation", uuidRepresentation);
      this.uuidRepresentation = uuidRepresentation;
   }

   public UuidCodec() {
      this.uuidRepresentation = UuidRepresentation.UNSPECIFIED;
   }

   public UuidRepresentation getUuidRepresentation() {
      return this.uuidRepresentation;
   }

   public void encode(BsonWriter writer, UUID value, EncoderContext encoderContext) {
      if (this.uuidRepresentation == UuidRepresentation.UNSPECIFIED) {
         throw new CodecConfigurationException("The uuidRepresentation has not been specified, so the UUID cannot be encoded.");
      } else {
         byte[] binaryData = UuidHelper.encodeUuidToBinary(value, this.uuidRepresentation);
         if (this.uuidRepresentation == UuidRepresentation.STANDARD) {
            writer.writeBinaryData(new BsonBinary(BsonBinarySubType.UUID_STANDARD, binaryData));
         } else {
            writer.writeBinaryData(new BsonBinary(BsonBinarySubType.UUID_LEGACY, binaryData));
         }

      }
   }

   public UUID decode(BsonReader reader, DecoderContext decoderContext) {
      byte subType = reader.peekBinarySubType();
      if (subType != BsonBinarySubType.UUID_LEGACY.getValue() && subType != BsonBinarySubType.UUID_STANDARD.getValue()) {
         throw new BSONException("Unexpected BsonBinarySubType");
      } else {
         byte[] bytes = reader.readBinaryData().getData();
         return UuidHelper.decodeBinaryToUuid(bytes, subType, this.uuidRepresentation);
      }
   }

   public Class<UUID> getEncoderClass() {
      return UUID.class;
   }

   public String toString() {
      return "UuidCodec{uuidRepresentation=" + this.uuidRepresentation + '}';
   }
}
