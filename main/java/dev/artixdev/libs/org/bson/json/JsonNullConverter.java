package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.BsonNull;

class JsonNullConverter implements Converter<BsonNull> {
   public void convert(BsonNull value, StrictJsonWriter writer) {
      writer.writeNull();
   }
}
