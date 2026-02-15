package dev.artixdev.libs.com.mongodb.client.model.changestream;

import java.util.Objects;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonCreator;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonProperty;

public final class SplitEvent {
   private final int fragment;
   private final int of;

   @BsonCreator
   public SplitEvent(@BsonProperty("fragment") int fragment, @BsonProperty("of") int of) {
      this.fragment = fragment;
      this.of = of;
   }

   public int getFragment() {
      return this.fragment;
   }

   public int getOf() {
      return this.of;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SplitEvent that = (SplitEvent)o;
         return this.fragment == that.fragment && this.of == that.of;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.fragment, this.of});
   }

   public String toString() {
      return "SplitEvent{fragment=" + this.fragment + ", of=" + this.of + '}';
   }
}
