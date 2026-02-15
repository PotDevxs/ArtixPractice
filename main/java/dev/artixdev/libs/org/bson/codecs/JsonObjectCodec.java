package dev.artixdev.libs.org.bson.codecs;

import java.io.StringWriter;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.json.JsonObject;
import dev.artixdev.libs.org.bson.json.JsonReader;
import dev.artixdev.libs.org.bson.json.JsonWriter;
import dev.artixdev.libs.org.bson.json.JsonWriterSettings;

public class JsonObjectCodec implements Codec<JsonObject> {
   private final JsonWriterSettings writerSettings;

   public JsonObjectCodec() {
      this(JsonWriterSettings.builder().build());
   }

   public JsonObjectCodec(JsonWriterSettings writerSettings) {
      this.writerSettings = writerSettings;
   }

   public void encode(BsonWriter writer, JsonObject value, EncoderContext encoderContext) {
      writer.pipe(new JsonReader(value.getJson()));
   }

   public JsonObject decode(BsonReader reader, DecoderContext decoderContext) {
      StringWriter stringWriter = new StringWriter();
      (new JsonWriter(stringWriter, this.writerSettings)).pipe(reader);
      return new JsonObject(stringWriter.toString());
   }

   public Class<JsonObject> getEncoderClass() {
      return JsonObject.class;
   }
}
