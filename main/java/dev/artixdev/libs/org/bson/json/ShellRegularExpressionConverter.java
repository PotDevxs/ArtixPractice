package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonRegularExpression;

class ShellRegularExpressionConverter implements Converter<BsonRegularExpression> {
   public void convert(BsonRegularExpression value, StrictJsonWriter writer) {
      String escaped = value.getPattern().equals("") ? "(?:)" : value.getPattern().replace("/", "\\/");
      writer.writeRaw("/" + escaped + "/" + value.getOptions());
   }
}
