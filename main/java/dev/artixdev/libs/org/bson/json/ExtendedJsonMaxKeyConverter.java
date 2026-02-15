package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonMaxKey;

class ExtendedJsonMaxKeyConverter implements Converter<BsonMaxKey> {
   public void convert(BsonMaxKey value, StrictJsonWriter writer) {
      writer.writeStartObject();
      writer.writeNumber("$maxKey", "1");
      writer.writeEndObject();
   }
}
