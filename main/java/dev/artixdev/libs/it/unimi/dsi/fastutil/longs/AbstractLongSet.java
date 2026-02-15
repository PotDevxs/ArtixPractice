package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.Set;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;

public abstract class AbstractLongSet extends AbstractLongCollection implements Cloneable, LongSet {
   protected AbstractLongSet() {
   }

   public abstract LongIterator iterator();

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Set)) {
         return false;
      } else {
         Set<?> s = (Set)o;
         if (s.size() != this.size()) {
            return false;
         } else {
            return s instanceof LongSet ? this.containsAll((LongSet)s) : this.containsAll(s);
         }
      }
   }

   public int hashCode() {
      int h = 0;
      int n = this.size();

      long k;
      for(LongIterator i = this.iterator(); n-- != 0; h += HashCommon.long2int(k)) {
         k = i.nextLong();
      }

      return h;
   }

   public boolean remove(long k) {
      return super.rem(k);
   }

   /** @deprecated */
   @Deprecated
   public boolean rem(long k) {
      return this.remove(k);
   }
}
