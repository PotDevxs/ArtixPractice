package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface IntPredicate extends java.util.function.IntPredicate, Predicate<Integer> {
   /** @deprecated */
   @Deprecated
   default boolean test(Integer t) {
      return this.test(t);
   }

   default IntPredicate and(java.util.function.IntPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) && other.test(t);
      };
   }

   default IntPredicate and(IntPredicate other) {
      return this.and((java.util.function.IntPredicate)other);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Integer> and(Predicate<? super Integer> other) {
      Objects.requireNonNull(other);
      return (Integer t) -> this.test(t) && other.test(t);
   }

   default IntPredicate negate() {
      return (t) -> {
         return !this.test(t);
      };
   }

   default IntPredicate or(java.util.function.IntPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) || other.test(t);
      };
   }

   default IntPredicate or(IntPredicate other) {
      return this.or((java.util.function.IntPredicate)other);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Integer> or(Predicate<? super Integer> other) {
      Objects.requireNonNull(other);
      return (Integer t) -> this.test(t) || other.test(t);
   }
}
