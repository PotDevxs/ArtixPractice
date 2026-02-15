package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.Geometry;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class GeometryCodec extends AbstractGeometryCodec<Geometry> {
   public GeometryCodec(CodecRegistry registry) {
      super(registry, Geometry.class);
   }
}
