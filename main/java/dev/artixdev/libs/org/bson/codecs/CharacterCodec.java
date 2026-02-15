package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonInvalidOperationException;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.assertions.Assertions;

public class CharacterCodec implements Codec<Character> {
   public void encode(BsonWriter writer, Character value, EncoderContext encoderContext) {
      Assertions.notNull("value", value);
      writer.writeString(value.toString());
   }

   public Character decode(BsonReader reader, DecoderContext decoderContext) {
      String string = reader.readString();
      if (string.length() != 1) {
         throw new BsonInvalidOperationException(String.format("Attempting to decode the string '%s' to a character, but its length is not equal to one", string));
      } else {
         return string.charAt(0);
      }
   }

   public Class<Character> getEncoderClass() {
      return Character.class;
   }
}
