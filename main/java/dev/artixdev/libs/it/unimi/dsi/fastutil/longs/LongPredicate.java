package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface LongPredicate extends java.util.function.LongPredicate, Predicate<Long> {
   /** @deprecated */
   @Deprecated
   default boolean test(Long t) {
      return this.test(t);
   }

   default LongPredicate and(java.util.function.LongPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) && other.test(t);
      };
   }

   default LongPredicate and(LongPredicate other) {
      return this.and((java.util.function.LongPredicate)other);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Long> and(Predicate<? super Long> other) {
      Objects.requireNonNull(other);
      return (Long t) -> this.test(t) && other.test(t);
   }

   default LongPredicate negate() {
      return (t) -> {
         return !this.test(t);
      };
   }

   default LongPredicate or(java.util.function.LongPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) || other.test(t);
      };
   }

   default LongPredicate or(LongPredicate other) {
      return this.or((java.util.function.LongPredicate)other);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Long> or(Predicate<? super Long> other) {
      Objects.requireNonNull(other);
      return (Long t) -> this.test(t) || other.test(t);
   }
}
