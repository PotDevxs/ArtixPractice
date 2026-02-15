package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.util.ListIterator;

public interface ObjectListIterator<K> extends ListIterator<K>, ObjectBidirectionalIterator<K> {
   default void set(K k) {
      throw new UnsupportedOperationException();
   }

   default void add(K k) {
      throw new UnsupportedOperationException();
   }

   default void remove() {
      throw new UnsupportedOperationException();
   }
}
