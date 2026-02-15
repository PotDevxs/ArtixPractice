package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonRegularExpression;

class ExtendedJsonRegularExpressionConverter implements Converter<BsonRegularExpression> {
   public void convert(BsonRegularExpression value, StrictJsonWriter writer) {
      writer.writeStartObject();
      writer.writeStartObject("$regularExpression");
      writer.writeString("pattern", value.getPattern());
      writer.writeString("options", value.getOptions());
      writer.writeEndObject();
      writer.writeEndObject();
   }
}
