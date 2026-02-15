package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class LineString extends Geometry {
   private final List<Position> coordinates;

   public LineString(List<Position> coordinates) {
      this((CoordinateReferenceSystem)null, coordinates);
   }

   public LineString(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, List<Position> coordinates) {
      super(coordinateReferenceSystem);
      Assertions.notNull("coordinates", coordinates);
      Assertions.isTrueArgument("coordinates must contain at least two positions", coordinates.size() >= 2);
      Assertions.doesNotContainNull("coordinates", coordinates);
      this.coordinates = Collections.unmodifiableList(coordinates);
   }

   public GeoJsonObjectType getType() {
      return GeoJsonObjectType.LINE_STRING;
   }

   public List<Position> getCoordinates() {
      return this.coordinates;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            LineString lineString = (LineString)o;
            return this.coordinates.equals(lineString.coordinates);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      return 31 * result + this.coordinates.hashCode();
   }

   public String toString() {
      return "LineString{coordinates=" + this.coordinates + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
   }
}
