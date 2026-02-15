package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;

final class ShowEntitySerializer extends TypeAdapter<HoverEvent.ShowEntity> {
   private final Gson gson;

   static TypeAdapter<HoverEvent.ShowEntity> create(Gson gson) {
      return (new ShowEntitySerializer(gson)).nullSafe();
   }

   private ShowEntitySerializer(Gson gson) {
      this.gson = gson;
   }

   public HoverEvent.ShowEntity read(JsonReader in) throws IOException {
      in.beginObject();
      Key type = null;
      UUID id = null;
      Component name = null;

      while(in.hasNext()) {
         String fieldName = in.nextName();
         if (fieldName.equals("type")) {
            type = (Key)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.KEY_TYPE);
         } else if (fieldName.equals("id")) {
            id = UUID.fromString(in.nextString());
         } else if (fieldName.equals("name")) {
            name = (Component)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.COMPONENT_TYPE);
         } else {
            in.skipValue();
         }
      }

      if (type != null && id != null) {
         in.endObject();
         return HoverEvent.ShowEntity.showEntity(type, id, name);
      } else {
         throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
      }
   }

   public void write(JsonWriter out, HoverEvent.ShowEntity value) throws IOException {
      out.beginObject();
      out.name("type");
      this.gson.toJson(value.type(), SerializerFactory.KEY_TYPE, (JsonWriter)out);
      out.name("id");
      out.value(value.id().toString());
      Component name = value.name();
      if (name != null) {
         out.name("name");
         this.gson.toJson(name, SerializerFactory.COMPONENT_TYPE, (JsonWriter)out);
      }

      out.endObject();
   }
}
