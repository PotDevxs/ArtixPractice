package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public final class PolygonCoordinates {
   private final List<Position> exterior;
   private final List<List<Position>> holes;

   @SafeVarargs
   public PolygonCoordinates(List<Position> exterior, List<Position>... holes) {
      this(exterior, Arrays.asList(holes));
   }

   public PolygonCoordinates(List<Position> exterior, List<List<Position>> holes) {
      Assertions.notNull("exteriorRing", exterior);
      Assertions.doesNotContainNull("exterior", exterior);
      Assertions.isTrueArgument("ring must contain at least four positions", exterior.size() >= 4);
      Assertions.isTrueArgument("first and last position must be the same", ((Position)exterior.get(0)).equals(exterior.get(exterior.size() - 1)));
      this.exterior = Collections.unmodifiableList(exterior);
      List<List<Position>> holesList = new ArrayList(holes.size());
      Iterator var4 = holes.iterator();

      while(var4.hasNext()) {
         List<Position> hole = (List)var4.next();
         Assertions.notNull("interiorRing", hole);
         Assertions.doesNotContainNull("hole", hole);
         Assertions.isTrueArgument("ring must contain at least four positions", hole.size() >= 4);
         Assertions.isTrueArgument("first and last position must be the same", ((Position)hole.get(0)).equals(hole.get(hole.size() - 1)));
         holesList.add(Collections.unmodifiableList(hole));
      }

      this.holes = Collections.unmodifiableList(holesList);
   }

   public List<Position> getExterior() {
      return this.exterior;
   }

   public List<List<Position>> getHoles() {
      return this.holes;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         PolygonCoordinates that = (PolygonCoordinates)o;
         if (!this.exterior.equals(that.exterior)) {
            return false;
         } else {
            return this.holes.equals(that.holes);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.exterior.hashCode();
      result = 31 * result + this.holes.hashCode();
      return result;
   }

   public String toString() {
      return "PolygonCoordinates{exterior=" + this.exterior + (this.holes.isEmpty() ? "" : ", holes=" + this.holes) + '}';
   }
}
