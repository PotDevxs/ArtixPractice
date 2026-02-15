package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonUndefined;

class ExtendedJsonUndefinedConverter implements Converter<BsonUndefined> {
   public void convert(BsonUndefined value, StrictJsonWriter writer) {
      writer.writeStartObject();
      writer.writeBoolean("$undefined", true);
      writer.writeEndObject();
   }
}
