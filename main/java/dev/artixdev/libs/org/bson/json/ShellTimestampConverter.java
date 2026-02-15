package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonTimestamp;

class ShellTimestampConverter implements Converter<BsonTimestamp> {
   public void convert(BsonTimestamp value, StrictJsonWriter writer) {
      writer.writeRaw(String.format("Timestamp(%d, %d)", value.getTime(), value.getInc()));
   }
}
