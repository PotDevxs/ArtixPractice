package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.Geometry;
import dev.artixdev.libs.com.mongodb.client.model.geojson.GeometryCollection;
import dev.artixdev.libs.com.mongodb.client.model.geojson.LineString;
import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiLineString;
import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiPoint;
import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiPolygon;
import dev.artixdev.libs.com.mongodb.client.model.geojson.NamedCoordinateReferenceSystem;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Point;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Polygon;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class GeoJsonCodecProvider implements CodecProvider {
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (clazz.equals(Polygon.class)) {
         return (Codec<T>) new PolygonCodec(registry);
      } else if (clazz.equals(Point.class)) {
         return (Codec<T>) new PointCodec(registry);
      } else if (clazz.equals(LineString.class)) {
         return (Codec<T>) new LineStringCodec(registry);
      } else if (clazz.equals(MultiPoint.class)) {
         return (Codec<T>) new MultiPointCodec(registry);
      } else if (clazz.equals(MultiLineString.class)) {
         return (Codec<T>) new MultiLineStringCodec(registry);
      } else if (clazz.equals(MultiPolygon.class)) {
         return (Codec<T>) new MultiPolygonCodec(registry);
      } else if (clazz.equals(GeometryCollection.class)) {
         return (Codec<T>) new GeometryCollectionCodec(registry);
      } else if (clazz.equals(NamedCoordinateReferenceSystem.class)) {
         return (Codec<T>) new NamedCoordinateReferenceSystemCodec();
      } else {
         return clazz.equals(Geometry.class) ? (Codec<T>) new GeometryCodec(registry) : null;
      }
   }

   public String toString() {
      return "GeoJsonCodecProvider{}";
   }
}
