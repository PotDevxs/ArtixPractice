package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;

@FunctionalInterface
public interface ByteConsumer extends Consumer<Byte>, IntConsumer {
   void accept(byte var1);

   /** @deprecated */
   @Deprecated
   default void accept(int t) {
      this.accept(SafeMath.safeIntToByte(t));
   }

   /** @deprecated */
   @Deprecated
   default void accept(Byte t) {
      this.accept(t);
   }

   default ByteConsumer andThen(ByteConsumer after) {
      Objects.requireNonNull(after);
      return (t) -> {
         this.accept(t);
         after.accept(t);
      };
   }

   default ByteConsumer andThen(IntConsumer after) {
      ByteConsumer var10001;
      if (after instanceof ByteConsumer) {
         var10001 = (ByteConsumer)after;
      } else {
         Objects.requireNonNull(after);
         var10001 = after::accept;
      }

      return this.andThen(var10001);
   }

   /** @deprecated */
   @Deprecated
   default Consumer<Byte> andThen(Consumer<? super Byte> after) {
      return super.andThen(after);
   }
}
