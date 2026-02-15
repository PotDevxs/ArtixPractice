package dev.artixdev.libs.com.google.gson.internal.bind;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonSyntaxException;
import dev.artixdev.libs.com.google.gson.ToNumberPolicy;
import dev.artixdev.libs.com.google.gson.ToNumberStrategy;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

public final class NumberTypeAdapter extends TypeAdapter<Number> {
   private static final TypeAdapterFactory LAZILY_PARSED_NUMBER_FACTORY;
   private final ToNumberStrategy toNumberStrategy;

   private NumberTypeAdapter(ToNumberStrategy toNumberStrategy) {
      this.toNumberStrategy = toNumberStrategy;
   }

   private static TypeAdapterFactory newFactory(ToNumberStrategy toNumberStrategy) {
      final NumberTypeAdapter adapter = new NumberTypeAdapter(toNumberStrategy);
      return new TypeAdapterFactory() {
         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            return type.getRawType() == Number.class ? (TypeAdapter<T>)adapter : null;
         }
      };
   }

   public static TypeAdapterFactory getFactory(ToNumberStrategy toNumberStrategy) {
      return toNumberStrategy == ToNumberPolicy.LAZILY_PARSED_NUMBER ? LAZILY_PARSED_NUMBER_FACTORY : newFactory(toNumberStrategy);
   }

   public Number read(JsonReader in) throws IOException {
      JsonToken jsonToken = in.peek();
      switch(jsonToken) {
      case NULL:
         in.nextNull();
         return null;
      case NUMBER:
      case STRING:
         return this.toNumberStrategy.readNumber(in);
      default:
         throw new JsonSyntaxException("Expecting number, got: " + jsonToken + "; at path " + in.getPath());
      }
   }

   public void write(JsonWriter out, Number value) throws IOException {
      out.value(value);
   }

   static {
      LAZILY_PARSED_NUMBER_FACTORY = newFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
   }
}
