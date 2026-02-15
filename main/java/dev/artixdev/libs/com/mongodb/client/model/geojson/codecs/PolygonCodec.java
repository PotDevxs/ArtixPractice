package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.Polygon;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class PolygonCodec extends AbstractGeometryCodec<Polygon> {
   public PolygonCodec(CodecRegistry registry) {
      super(registry, Polygon.class);
   }
}
