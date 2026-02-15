package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.io.StringWriter;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.json.JsonWriter;
import dev.artixdev.libs.org.bson.json.JsonWriterSettings;

public abstract class Geometry {
   private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new GeoJsonCodecProvider());
   private final CoordinateReferenceSystem coordinateReferenceSystem;

   protected Geometry() {
      this((CoordinateReferenceSystem)null);
   }

   protected Geometry(@Nullable CoordinateReferenceSystem coordinateReferenceSystem) {
      this.coordinateReferenceSystem = coordinateReferenceSystem;
   }

   public abstract GeoJsonObjectType getType();

   public String toJson() {
      StringWriter stringWriter = new StringWriter();
      JsonWriter writer = new JsonWriter(stringWriter, JsonWriterSettings.builder().build());
      Codec codec = getRegistry().get(this.getClass());
      codec.encode(writer, this, EncoderContext.builder().build());
      return stringWriter.toString();
   }

   static CodecRegistry getRegistry() {
      return REGISTRY;
   }

   @Nullable
   public CoordinateReferenceSystem getCoordinateReferenceSystem() {
      return this.coordinateReferenceSystem;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Geometry geometry = (Geometry)o;
         return Objects.equals(this.coordinateReferenceSystem, geometry.coordinateReferenceSystem);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.coordinateReferenceSystem != null ? this.coordinateReferenceSystem.hashCode() : 0;
   }
}
