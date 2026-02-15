package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.types.Decimal128;

class ExtendedJsonDecimal128Converter implements Converter<Decimal128> {
   public void convert(Decimal128 value, StrictJsonWriter writer) {
      writer.writeStartObject();
      writer.writeName("$numberDecimal");
      writer.writeString(value.toString());
      writer.writeEndObject();
   }
}
