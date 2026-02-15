package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.text.BlockNBTComponent;

final class BlockNBTComponentPosSerializer extends TypeAdapter<BlockNBTComponent.Pos> {
   static final TypeAdapter<BlockNBTComponent.Pos> INSTANCE = (new BlockNBTComponentPosSerializer()).nullSafe();

   private BlockNBTComponentPosSerializer() {
   }

   public BlockNBTComponent.Pos read(JsonReader in) throws IOException {
      String string = in.nextString();

      try {
         return BlockNBTComponent.Pos.fromString(string);
      } catch (IllegalArgumentException e) {
         throw new JsonParseException("Don't know how to turn " + string + " into a Position", e);
      }
   }

   public void write(JsonWriter out, BlockNBTComponent.Pos value) throws IOException {
      out.value(value.asString());
   }
}
