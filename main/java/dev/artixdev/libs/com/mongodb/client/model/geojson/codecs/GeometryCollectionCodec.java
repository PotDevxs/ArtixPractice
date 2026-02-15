package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.GeometryCollection;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class GeometryCollectionCodec extends AbstractGeometryCodec<GeometryCollection> {
   public GeometryCollectionCodec(CodecRegistry registry) {
      super(registry, GeometryCollection.class);
   }
}
