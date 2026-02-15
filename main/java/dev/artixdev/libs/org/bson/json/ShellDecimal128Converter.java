package dev.artixdev.libs.org.bson.json;

import dev.artixdev.libs.org.bson.types.Decimal128;

class ShellDecimal128Converter implements Converter<Decimal128> {
   public void convert(Decimal128 value, StrictJsonWriter writer) {
      writer.writeRaw(String.format("NumberDecimal(\"%s\")", value.toString()));
   }
}
