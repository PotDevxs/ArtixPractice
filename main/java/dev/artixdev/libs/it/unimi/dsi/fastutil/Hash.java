package dev.artixdev.libs.it.unimi.dsi.fastutil;

/**
 * Marker interface for hash-based data structures.
 */
public interface Hash {

   /**
    * Strategy for computing hash codes and comparing equality of objects.
    *
    * @param <K> the type of objects to hash and compare
    */
   interface Strategy<K> {
      int hashCode(K o);

      boolean equals(K a, K b);
   }
}
