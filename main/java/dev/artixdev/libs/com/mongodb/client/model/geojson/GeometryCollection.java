package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class GeometryCollection extends Geometry {
   private final List<? extends Geometry> geometries;

   public GeometryCollection(List<? extends Geometry> geometries) {
      this((CoordinateReferenceSystem)null, geometries);
   }

   public GeometryCollection(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, List<? extends Geometry> geometries) {
      super(coordinateReferenceSystem);
      Assertions.notNull("geometries", geometries);
      Assertions.doesNotContainNull("geometries", geometries);
      this.geometries = Collections.unmodifiableList(geometries);
   }

   public GeoJsonObjectType getType() {
      return GeoJsonObjectType.GEOMETRY_COLLECTION;
   }

   public List<? extends Geometry> getGeometries() {
      return this.geometries;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            GeometryCollection that = (GeometryCollection)o;
            return this.geometries.equals(that.geometries);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.geometries.hashCode();
      return result;
   }

   public String toString() {
      CoordinateReferenceSystem coordinateReferenceSystem = this.getCoordinateReferenceSystem();
      return "GeometryCollection{geometries=" + this.geometries + (coordinateReferenceSystem == null ? "" : ", coordinateReferenceSystem=" + coordinateReferenceSystem) + '}';
   }
}
