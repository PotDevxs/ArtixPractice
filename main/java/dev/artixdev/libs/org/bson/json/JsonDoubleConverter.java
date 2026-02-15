package dev.artixdev.libs.org.bson.json;

class JsonDoubleConverter implements Converter<Double> {
   public void convert(Double value, StrictJsonWriter writer) {
      writer.writeNumber(Double.toString(value));
   }
}
