package dev.artixdev.libs.it.unimi.dsi.fastutil;

/**
 * A list iterator for big lists (with 64-bit indices).
 *
 * @param <K> the type of elements returned by this iterator
 */
public interface BigListIterator<K> extends BidirectionalIterator<K> {

   long nextIndex();

   long previousIndex();

   default void set(K k) {
      throw new UnsupportedOperationException();
   }

   default void add(K k) {
      throw new UnsupportedOperationException();
   }
}
