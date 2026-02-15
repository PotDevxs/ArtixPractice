package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Objects;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Pair;

public class LongObjectImmutablePair<V> implements Serializable, LongObjectPair<V> {
   private static final long serialVersionUID = 0L;
   protected final long left;
   protected final V right;

   public LongObjectImmutablePair(long left, V right) {
      this.left = left;
      this.right = right;
   }

   public static <V> LongObjectImmutablePair<V> of(long left, V right) {
      return new LongObjectImmutablePair(left, right);
   }

   public long leftLong() {
      return this.left;
   }

   public V right() {
      return this.right;
   }

   public boolean equals(Object other) {
      if (other == null) {
         return false;
      } else if (other instanceof LongObjectPair) {
         return this.left == ((LongObjectPair)other).leftLong() && Objects.equals(this.right, ((LongObjectPair)other).right());
      } else if (!(other instanceof Pair)) {
         return false;
      } else {
         return Objects.equals(this.left, ((Pair)other).left()) && Objects.equals(this.right, ((Pair)other).right());
      }
   }

   public int hashCode() {
      return HashCommon.long2int(this.left) * 19 + (this.right == null ? 0 : this.right.hashCode());
   }

   public String toString() {
      return "<" + this.leftLong() + "," + this.right() + ">";
   }
}
