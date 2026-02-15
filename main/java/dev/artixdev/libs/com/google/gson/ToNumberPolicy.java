package dev.artixdev.libs.com.google.gson;

import java.io.IOException;
import java.math.BigDecimal;
import dev.artixdev.libs.com.google.gson.internal.LazilyParsedNumber;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.MalformedJsonException;

public enum ToNumberPolicy implements ToNumberStrategy {
   DOUBLE {
      public Double readNumber(JsonReader in) throws IOException {
         return in.nextDouble();
      }
   },
   LAZILY_PARSED_NUMBER {
      public Number readNumber(JsonReader in) throws IOException {
         return new LazilyParsedNumber(in.nextString());
      }
   },
   LONG_OR_DOUBLE {
      public Number readNumber(JsonReader in) throws IOException, JsonParseException {
         String value = in.nextString();

         try {
            return Long.parseLong(value);
         } catch (NumberFormatException ignored) {
            try {
               Double d = Double.valueOf(value);
               if ((d.isInfinite() || d.isNaN()) && !in.isLenient()) {
                  throw new MalformedJsonException("JSON forbids NaN and infinities: " + d + "; at path " + in.getPreviousPath());
               } else {
                  return d;
               }
            } catch (NumberFormatException e) {
               throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), e);
            }
         }
      }
   },
   BIG_DECIMAL {
      public BigDecimal readNumber(JsonReader in) throws IOException {
         String value = in.nextString();

         try {
            return new BigDecimal(value);
         } catch (NumberFormatException e) {
            throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), e);
         }
      }
   };

   private ToNumberPolicy() {
   }

   ToNumberPolicy(Object ignored) {
      this();
   }
}
