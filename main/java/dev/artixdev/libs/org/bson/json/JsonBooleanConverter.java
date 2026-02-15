package dev.artixdev.libs.org.bson.json;

class JsonBooleanConverter implements Converter<Boolean> {
   public void convert(Boolean value, StrictJsonWriter writer) {
      writer.writeBoolean(value);
   }
}
