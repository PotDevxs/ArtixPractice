package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class MultiPolygon extends Geometry {
   private final List<PolygonCoordinates> coordinates;

   public MultiPolygon(List<PolygonCoordinates> coordinates) {
      this((CoordinateReferenceSystem)null, coordinates);
   }

   public MultiPolygon(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, List<PolygonCoordinates> coordinates) {
      super(coordinateReferenceSystem);
      Assertions.notNull("coordinates", coordinates);
      Assertions.doesNotContainNull("coordinates", coordinates);
      this.coordinates = Collections.unmodifiableList(coordinates);
   }

   public GeoJsonObjectType getType() {
      return GeoJsonObjectType.MULTI_POLYGON;
   }

   public List<PolygonCoordinates> getCoordinates() {
      return this.coordinates;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            MultiPolygon that = (MultiPolygon)o;
            return this.coordinates.equals(that.coordinates);
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
      return "MultiPolygon{coordinates=" + this.coordinates + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
   }
}
