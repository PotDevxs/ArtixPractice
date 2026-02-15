package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiPolygon;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class MultiPolygonCodec extends AbstractGeometryCodec<MultiPolygon> {
   public MultiPolygonCodec(CodecRegistry registry) {
      super(registry, MultiPolygon.class);
   }
}
