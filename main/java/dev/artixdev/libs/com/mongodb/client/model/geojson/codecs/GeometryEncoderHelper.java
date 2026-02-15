package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Geometry;
import dev.artixdev.libs.com.mongodb.client.model.geojson.GeometryCollection;
import dev.artixdev.libs.com.mongodb.client.model.geojson.LineString;
import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiLineString;
import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiPoint;
import dev.artixdev.libs.com.mongodb.client.model.geojson.MultiPolygon;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Point;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Polygon;
import dev.artixdev.libs.com.mongodb.client.model.geojson.PolygonCoordinates;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Position;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class GeometryEncoderHelper {
   static void encodeGeometry(BsonWriter writer, Geometry value, EncoderContext encoderContext, CodecRegistry registry) {
      writer.writeStartDocument();
      writer.writeString("type", value.getType().getTypeName());
      if (value instanceof GeometryCollection) {
         writer.writeName("geometries");
         encodeGeometryCollection(writer, (GeometryCollection)value, encoderContext, registry);
      } else {
         writer.writeName("coordinates");
         if (value instanceof Point) {
            encodePoint(writer, (Point)value);
         } else if (value instanceof MultiPoint) {
            encodeMultiPoint(writer, (MultiPoint)value);
         } else if (value instanceof Polygon) {
            encodePolygon(writer, (Polygon)value);
         } else if (value instanceof MultiPolygon) {
            encodeMultiPolygon(writer, (MultiPolygon)value);
         } else if (value instanceof LineString) {
            encodeLineString(writer, (LineString)value);
         } else {
            if (!(value instanceof MultiLineString)) {
               throw new CodecConfigurationException(String.format("Unsupported Geometry: %s", value));
            }

            encodeMultiLineString(writer, (MultiLineString)value);
         }
      }

      encodeCoordinateReferenceSystem(writer, value, encoderContext, registry);
      writer.writeEndDocument();
   }

   private static void encodePoint(BsonWriter writer, Point value) {
      encodePosition(writer, value.getPosition());
   }

   private static void encodeMultiPoint(BsonWriter writer, MultiPoint value) {
      writer.writeStartArray();
      Iterator var2 = value.getCoordinates().iterator();

      while(var2.hasNext()) {
         Position position = (Position)var2.next();
         encodePosition(writer, position);
      }

      writer.writeEndArray();
   }

   private static void encodePolygon(BsonWriter writer, Polygon value) {
      encodePolygonCoordinates(writer, value.getCoordinates());
   }

   private static void encodeMultiPolygon(BsonWriter writer, MultiPolygon value) {
      writer.writeStartArray();
      Iterator var2 = value.getCoordinates().iterator();

      while(var2.hasNext()) {
         PolygonCoordinates polygonCoordinates = (PolygonCoordinates)var2.next();
         encodePolygonCoordinates(writer, polygonCoordinates);
      }

      writer.writeEndArray();
   }

   private static void encodeLineString(BsonWriter writer, LineString value) {
      writer.writeStartArray();
      Iterator var2 = value.getCoordinates().iterator();

      while(var2.hasNext()) {
         Position position = (Position)var2.next();
         encodePosition(writer, position);
      }

      writer.writeEndArray();
   }

   private static void encodeMultiLineString(BsonWriter writer, MultiLineString value) {
      writer.writeStartArray();
      Iterator var2 = value.getCoordinates().iterator();

      while(var2.hasNext()) {
         List<Position> ring = (List)var2.next();
         writer.writeStartArray();
         Iterator var4 = ring.iterator();

         while(var4.hasNext()) {
            Position position = (Position)var4.next();
            encodePosition(writer, position);
         }

         writer.writeEndArray();
      }

      writer.writeEndArray();
   }

   private static void encodeGeometryCollection(BsonWriter writer, GeometryCollection value, EncoderContext encoderContext, CodecRegistry registry) {
      writer.writeStartArray();
      Iterator var4 = value.getGeometries().iterator();

      while(var4.hasNext()) {
         Geometry geometry = (Geometry)var4.next();
         encodeGeometry(writer, geometry, encoderContext, registry);
      }

      writer.writeEndArray();
   }

   static void encodeCoordinateReferenceSystem(BsonWriter writer, Geometry geometry, EncoderContext encoderContext, CodecRegistry registry) {
      CoordinateReferenceSystem coordinateReferenceSystem = geometry.getCoordinateReferenceSystem();
      if (coordinateReferenceSystem != null) {
         writer.writeName("crs");
         Codec codec = registry.get(coordinateReferenceSystem.getClass());
         encoderContext.encodeWithChildContext(codec, writer, coordinateReferenceSystem);
      }

   }

   static void encodePolygonCoordinates(BsonWriter writer, PolygonCoordinates polygonCoordinates) {
      writer.writeStartArray();
      encodeLinearRing(polygonCoordinates.getExterior(), writer);
      Iterator var2 = polygonCoordinates.getHoles().iterator();

      while(var2.hasNext()) {
         List<Position> ring = (List)var2.next();
         encodeLinearRing(ring, writer);
      }

      writer.writeEndArray();
   }

   private static void encodeLinearRing(List<Position> ring, BsonWriter writer) {
      writer.writeStartArray();
      Iterator var2 = ring.iterator();

      while(var2.hasNext()) {
         Position position = (Position)var2.next();
         encodePosition(writer, position);
      }

      writer.writeEndArray();
   }

   static void encodePosition(BsonWriter writer, Position value) {
      writer.writeStartArray();
      Iterator var2 = value.getValues().iterator();

      while(var2.hasNext()) {
         double number = (Double)var2.next();
         writer.writeDouble(number);
      }

      writer.writeEndArray();
   }

   private GeometryEncoderHelper() {
   }
}
