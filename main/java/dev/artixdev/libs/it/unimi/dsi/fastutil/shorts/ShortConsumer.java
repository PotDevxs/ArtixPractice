package dev.artixdev.libs.it.unimi.dsi.fastutil.shorts;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;

@FunctionalInterface
public interface ShortConsumer extends Consumer<Short>, IntConsumer {
   void accept(short var1);

   /** @deprecated */
   @Deprecated
   default void accept(int t) {
      this.accept(SafeMath.safeIntToShort(t));
   }

   /** @deprecated */
   @Deprecated
   default void accept(Short t) {
      this.accept(t);
   }

   default ShortConsumer andThen(ShortConsumer after) {
      Objects.requireNonNull(after);
      return (t) -> {
         this.accept(t);
         after.accept(t);
      };
   }

   default ShortConsumer andThen(IntConsumer after) {
      ShortConsumer var10001;
      if (after instanceof ShortConsumer) {
         var10001 = (ShortConsumer)after;
      } else {
         Objects.requireNonNull(after);
         var10001 = after::accept;
      }

      return this.andThen(var10001);
   }

   /** @deprecated */
   @Deprecated
   default Consumer<Short> andThen(Consumer<? super Short> after) {
      return super.andThen(after);
   }
}
