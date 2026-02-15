package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class MultiLineString extends Geometry {
   private final List<List<Position>> coordinates;

   public MultiLineString(List<List<Position>> coordinates) {
      this((CoordinateReferenceSystem)null, coordinates);
   }

   public MultiLineString(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, List<List<Position>> coordinates) {
      super(coordinateReferenceSystem);
      Assertions.notNull("coordinates", coordinates);
      Iterator var3 = coordinates.iterator();

      while(var3.hasNext()) {
         List<Position> line = (List)var3.next();
         Assertions.notNull("line", line);
         Assertions.doesNotContainNull("line", line);
      }

      this.coordinates = Collections.unmodifiableList(coordinates);
   }

   public GeoJsonObjectType getType() {
      return GeoJsonObjectType.MULTI_LINE_STRING;
   }

   public List<List<Position>> getCoordinates() {
      return this.coordinates;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            MultiLineString polygon = (MultiLineString)o;
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
      return "MultiLineString{coordinates=" + this.coordinates + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
   }
}
