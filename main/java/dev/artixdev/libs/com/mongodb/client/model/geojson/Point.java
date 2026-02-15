package dev.artixdev.libs.com.mongodb.client.model.geojson;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class Point extends Geometry {
   private final Position coordinate;

   public Point(Position coordinate) {
      this((CoordinateReferenceSystem)null, coordinate);
   }

   public Point(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, Position coordinate) {
      super(coordinateReferenceSystem);
      this.coordinate = (Position)Assertions.notNull("coordinates", coordinate);
   }

   public GeoJsonObjectType getType() {
      return GeoJsonObjectType.POINT;
   }

   public Position getCoordinates() {
      return this.coordinate;
   }

   public Position getPosition() {
      return this.coordinate;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            Point point = (Point)o;
            return this.coordinate.equals(point.coordinate);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      return 31 * result + this.coordinate.hashCode();
   }

   public String toString() {
      return "Point{coordinate=" + this.coordinate + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
   }
}
