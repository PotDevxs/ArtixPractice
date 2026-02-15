package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class Polygon extends Geometry {
   private final PolygonCoordinates coordinates;

   @SafeVarargs
   public Polygon(List<Position> exterior, List<Position>... holes) {
      this(new PolygonCoordinates(exterior, holes));
   }

   public Polygon(List<Position> exterior, List<List<Position>> holes) {
      this(new PolygonCoordinates(exterior, holes));
   }

   public Polygon(PolygonCoordinates coordinates) {
      this((CoordinateReferenceSystem)null, (PolygonCoordinates)coordinates);
   }

   public Polygon(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, PolygonCoordinates coordinates) {
      super(coordinateReferenceSystem);
      this.coordinates = (PolygonCoordinates)Assertions.notNull("coordinates", coordinates);
   }

   public GeoJsonObjectType getType() {
      return GeoJsonObjectType.POLYGON;
   }

   public PolygonCoordinates getCoordinates() {
      return this.coordinates;
   }

   public List<Position> getExterior() {
      return this.coordinates.getExterior();
   }

   public List<List<Position>> getHoles() {
      return this.coordinates.getHoles();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            Polygon polygon = (Polygon)o;
            return this.coordinates.equals(polygon.coordinates);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.coordinates.hashCode();
      return result;
   }

   public String toString() {
      return "Polygon{exterior=" + this.coordinates.getExterior() + (this.coordinates.getHoles().isEmpty() ? "" : ", holes=" + this.coordinates.getHoles()) + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
   }
}
