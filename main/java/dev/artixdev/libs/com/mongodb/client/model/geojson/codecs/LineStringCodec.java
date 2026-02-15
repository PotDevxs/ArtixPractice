package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.LineString;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class LineStringCodec extends AbstractGeometryCodec<LineString> {
   public LineStringCodec(CodecRegistry registry) {
      super(registry, LineString.class);
   }
}
