package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;

@FunctionalInterface
public interface CharConsumer extends Consumer<Character>, IntConsumer {
   void accept(char var1);

   /** @deprecated */
   @Deprecated
   default void accept(int t) {
      this.accept(SafeMath.safeIntToChar(t));
   }

   /** @deprecated */
   @Deprecated
   default void accept(Character t) {
      this.accept(t.charValue());
   }

   default CharConsumer andThen(CharConsumer after) {
      Objects.requireNonNull(after);
      return (t) -> {
         this.accept(t);
         after.accept(t);
      };
   }

   default CharConsumer andThen(IntConsumer after) {
      CharConsumer var10001;
      if (after instanceof CharConsumer) {
         var10001 = (CharConsumer)after;
      } else {
         Objects.requireNonNull(after);
         var10001 = t -> after.accept(t);
      }

      return this.andThen(var10001);
   }

   /** @deprecated */
   @Deprecated
   default Consumer<Character> andThen(Consumer<? super Character> after) {
      return Consumer.super.andThen(after);
   }
}
