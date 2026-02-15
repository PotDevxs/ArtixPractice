package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import dev.artixdev.libs.com.mongodb.client.model.geojson.NamedCoordinateReferenceSystem;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

public class NamedCoordinateReferenceSystemCodec implements Codec<NamedCoordinateReferenceSystem> {
   public void encode(BsonWriter writer, NamedCoordinateReferenceSystem value, EncoderContext encoderContext) {
      writer.writeStartDocument();
      writer.writeString("type", value.getType().getTypeName());
      writer.writeStartDocument("properties");
      writer.writeString("name", value.getName());
      writer.writeEndDocument();
      writer.writeEndDocument();
   }

   public Class<NamedCoordinateReferenceSystem> getEncoderClass() {
      return NamedCoordinateReferenceSystem.class;
   }

   public NamedCoordinateReferenceSystem decode(BsonReader reader, DecoderContext decoderContext) {
      CoordinateReferenceSystem crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
      if (crs != null && crs instanceof NamedCoordinateReferenceSystem) {
         return (NamedCoordinateReferenceSystem)crs;
      } else {
         throw new CodecConfigurationException("Invalid NamedCoordinateReferenceSystem.");
      }
   }
}
