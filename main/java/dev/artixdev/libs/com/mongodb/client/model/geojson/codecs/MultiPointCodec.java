package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiPoint;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class MultiPointCodec extends AbstractGeometryCodec<MultiPoint> {
   public MultiPointCodec(CodecRegistry registry) {
      super(registry, MultiPoint.class);
   }
}
