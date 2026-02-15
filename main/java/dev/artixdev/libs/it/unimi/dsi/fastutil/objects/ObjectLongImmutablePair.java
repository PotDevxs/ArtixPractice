package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Objects;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Pair;

public class ObjectLongImmutablePair<K> implements Serializable, ObjectLongPair<K> {
   private static final long serialVersionUID = 0L;
   protected final K left;
   protected final long right;

   public ObjectLongImmutablePair(K left, long right) {
      this.left = left;
      this.right = right;
   }

   public static <K> ObjectLongImmutablePair<K> of(K left, long right) {
      return new ObjectLongImmutablePair(left, right);
   }

   public K left() {
      return this.left;
   }

   public long rightLong() {
      return this.right;
   }

   public boolean equals(Object other) {
      if (other == null) {
         return false;
      } else if (other instanceof ObjectLongPair) {
         return Objects.equals(this.left, ((ObjectLongPair)other).left()) && this.right == ((ObjectLongPair)other).rightLong();
      } else if (!(other instanceof Pair)) {
         return false;
      } else {
         return Objects.equals(this.left, ((Pair)other).left()) && Objects.equals(this.right, ((Pair)other).right());
      }
   }

   public int hashCode() {
      return (this.left == null ? 0 : this.left.hashCode()) * 19 + HashCommon.long2int(this.right);
   }

   public String toString() {
      return "<" + this.left() + "," + this.rightLong() + ">";
   }
}
