package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonUndefined;

class ShellUndefinedConverter implements Converter<BsonUndefined> {
   public void convert(BsonUndefined value, StrictJsonWriter writer) {
      writer.writeRaw("undefined");
   }
}
