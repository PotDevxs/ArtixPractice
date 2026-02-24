package dev.artixdev.libs.it.unimi.dsi.fastutil;

/**
 * A simple pair of (left, right) elements.
 *
 * @param <L> the type of the left element
 * @param <R> the type of the right element
 */
public interface Pair<L, R> {

   L left();

   R right();

   default Pair<L, R> right(R v) {
      throw new UnsupportedOperationException();
   }
}
