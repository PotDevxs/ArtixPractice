package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class MultiPoint extends Geometry {
   private final List<Position> coordinates;

   public MultiPoint(List<Position> coordinates) {
      this((CoordinateReferenceSystem)null, coordinates);
   }

   public MultiPoint(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, List<Position> coordinates) {
      super(coordinateReferenceSystem);
      Assertions.notNull("coordinates", coordinates);
      Assertions.isTrueArgument("coordinates contains only non-null positions", !coordinates.contains((Object)null));
      this.coordinates = Collections.unmodifiableList(coordinates);
   }

   public GeoJsonObjectType getType() {
      return GeoJsonObjectType.MULTI_POINT;
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
            MultiPoint multiPoint = (MultiPoint)o;
            return this.coordinates.equals(multiPoint.coordinates);
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
      return "MultiPoint{coordinates=" + this.coordinates + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
   }
}
