package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonMinKey;

class ShellMinKeyConverter implements Converter<BsonMinKey> {
   public void convert(BsonMinKey value, StrictJsonWriter writer) {
      writer.writeRaw("MinKey");
   }
}
