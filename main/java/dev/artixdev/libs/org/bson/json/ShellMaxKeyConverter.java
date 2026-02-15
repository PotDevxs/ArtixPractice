package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonMaxKey;

class ShellMaxKeyConverter implements Converter<BsonMaxKey> {
   public void convert(BsonMaxKey value, StrictJsonWriter writer) {
      writer.writeRaw("MaxKey");
   }
}
