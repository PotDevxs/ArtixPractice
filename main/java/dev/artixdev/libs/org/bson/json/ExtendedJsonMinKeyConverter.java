package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonMinKey;

class ExtendedJsonMinKeyConverter implements Converter<BsonMinKey> {
   public void convert(BsonMinKey value, StrictJsonWriter writer) {
      writer.writeStartObject();
      writer.writeNumber("$minKey", "1");
      writer.writeEndObject();
   }
}
