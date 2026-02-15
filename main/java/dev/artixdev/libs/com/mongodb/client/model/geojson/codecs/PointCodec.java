package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.Point;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class PointCodec extends AbstractGeometryCodec<Point> {
   public PointCodec(CodecRegistry registry) {
      super(registry, Point.class);
   }
}
