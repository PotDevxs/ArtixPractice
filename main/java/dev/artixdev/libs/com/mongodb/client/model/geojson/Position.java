package dev.artixdev.libs.com.mongodb.client.model.geojson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Immutable
public final class Position {
   private final List<Double> values;

   public Position(List<Double> values) {
      Assertions.notNull("values", values);
      Assertions.doesNotContainNull("values", values);
      Assertions.isTrueArgument("value must contain at least two elements", values.size() >= 2);
      this.values = Collections.unmodifiableList(values);
   }

   public Position(double first, double second, double... remaining) {
      List<Double> values = new ArrayList();
      values.add(first);
      values.add(second);
      double[] var7 = remaining;
      int var8 = remaining.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         double cur = var7[var9];
         values.add(cur);
      }

      this.values = Collections.unmodifiableList(values);
   }

   public List<Double> getValues() {
      return this.values;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Position that = (Position)o;
         return this.values.equals(that.values);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.values.hashCode();
   }

   public String toString() {
      return "Position{values=" + this.values + '}';
   }
}
