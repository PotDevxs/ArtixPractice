package dev.artixdev.libs.org.bson.json;

import java.util.Base64;
import dev.artixdev.libs.org.bson.BsonBinary;

class LegacyExtendedJsonBinaryConverter implements Converter<BsonBinary> {
   public void convert(BsonBinary value, StrictJsonWriter writer) {
      writer.writeStartObject();
      writer.writeString("$binary", Base64.getEncoder().encodeToString(value.getData()));
      writer.writeString("$type", String.format("%02X", value.getType()));
      writer.writeEndObject();
   }
}
