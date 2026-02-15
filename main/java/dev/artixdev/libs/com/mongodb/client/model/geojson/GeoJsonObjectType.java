package dev.artixdev.libs.com.mongodb.client.model.geojson;

public enum GeoJsonObjectType {
   GEOMETRY_COLLECTION("GeometryCollection"),
   LINE_STRING("LineString"),
   MULTI_LINE_STRING("MultiLineString"),
   MULTI_POINT("MultiPoint"),
   MULTI_POLYGON("MultiPolygon"),
   POINT("Point"),
   POLYGON("Polygon");

   private final String typeName;

   public String getTypeName() {
      return this.typeName;
   }

   private GeoJsonObjectType(String typeName) {
      this.typeName = typeName;
   }

   // $FF: synthetic method
   private static GeoJsonObjectType[] $values() {
      return new GeoJsonObjectType[]{GEOMETRY_COLLECTION, LINE_STRING, MULTI_LINE_STRING, MULTI_POINT, MULTI_POLYGON, POINT, POLYGON};
   }
}
