package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiLineString;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class MultiLineStringCodec extends AbstractGeometryCodec<MultiLineString> {
   public MultiLineStringCodec(CodecRegistry registry) {
      super(registry, MultiLineString.class);
   }
}
