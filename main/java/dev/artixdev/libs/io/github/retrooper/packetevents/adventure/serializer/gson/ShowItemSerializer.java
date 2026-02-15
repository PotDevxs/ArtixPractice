package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;

final class ShowItemSerializer extends TypeAdapter<HoverEvent.ShowItem> {
   private final Gson gson;

   static TypeAdapter<HoverEvent.ShowItem> create(Gson gson) {
      return (new ShowItemSerializer(gson)).nullSafe();
   }

   private ShowItemSerializer(Gson gson) {
      this.gson = gson;
   }

   public HoverEvent.ShowItem read(JsonReader in) throws IOException {
      in.beginObject();
      Key key = null;
      int count = 1;
      BinaryTagHolder nbt = null;

      while(true) {
         while(in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("id")) {
               key = (Key)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.KEY_TYPE);
            } else if (fieldName.equals("count")) {
               count = in.nextInt();
            } else if (fieldName.equals("tag")) {
               JsonToken token = in.peek();
               if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
                  if (token == JsonToken.BOOLEAN) {
                     nbt = BinaryTagHolder.binaryTagHolder(String.valueOf(in.nextBoolean()));
                  } else {
                     if (token != JsonToken.NULL) {
                        throw new JsonParseException("Expected tag to be a string");
                     }

                     in.nextNull();
                  }
               } else {
                  nbt = BinaryTagHolder.binaryTagHolder(in.nextString());
               }
            } else {
               in.skipValue();
            }
         }

         if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
         }

         in.endObject();
         return HoverEvent.ShowItem.showItem(key, count, nbt);
      }
   }

   public void write(JsonWriter out, HoverEvent.ShowItem value) throws IOException {
      out.beginObject();
      out.name("id");
      this.gson.toJson(value.item(), SerializerFactory.KEY_TYPE, (JsonWriter)out);
      int count = value.count();
      if (count != 1) {
         out.name("count");
         out.value((long)count);
      }

      BinaryTagHolder nbt = value.nbt();
      if (nbt != null) {
         out.name("tag");
         out.value(nbt.string());
      }

      out.endObject();
   }
}
