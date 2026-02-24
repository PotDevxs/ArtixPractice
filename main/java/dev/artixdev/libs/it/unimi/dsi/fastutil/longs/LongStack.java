package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import dev.artixdev.libs.it.unimi.dsi.fastutil.Stack;

public interface LongStack extends Stack<Long> {
   void push(long var1);

   long popLong();

   long topLong();

   long peekLong(int var1);

   /** @deprecated */
   @Deprecated
   default void push(Long o) {
      this.push(o.longValue());
   }

   /** @deprecated */
   @Deprecated
   default Long pop() {
      return this.popLong();
   }

   /** @deprecated */
   @Deprecated
   default Long top() {
      return this.topLong();
   }

   /** @deprecated */
   @Deprecated
   default Long peek(int i) {
      return this.peekLong(i);
   }
}
