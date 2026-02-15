package dev.artixdev.libs.com.mongodb.client.model.geojson;

import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class NamedCoordinateReferenceSystem extends CoordinateReferenceSystem {
   public static final NamedCoordinateReferenceSystem EPSG_4326 = new NamedCoordinateReferenceSystem("EPSG:4326");
   public static final NamedCoordinateReferenceSystem CRS_84 = new NamedCoordinateReferenceSystem("urn:ogc:def:crs:OGC:1.3:CRS84");
   public static final NamedCoordinateReferenceSystem EPSG_4326_STRICT_WINDING = new NamedCoordinateReferenceSystem("urn:x-mongodb:crs:strictwinding:EPSG:4326");
   private final String name;

   public NamedCoordinateReferenceSystem(String name) {
      this.name = (String)Assertions.notNull("name", name);
   }

   public CoordinateReferenceSystemType getType() {
      return CoordinateReferenceSystemType.NAME;
   }

   public String getName() {
      return this.name;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         NamedCoordinateReferenceSystem that = (NamedCoordinateReferenceSystem)o;
         return this.name.equals(that.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return "NamedCoordinateReferenceSystem{name='" + this.name + '\'' + '}';
   }
}
