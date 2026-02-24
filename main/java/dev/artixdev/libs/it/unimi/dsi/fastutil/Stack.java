package dev.artixdev.libs.it.unimi.dsi.fastutil;

/**
 * Base interface for stack types with boxed element type.
 *
 * @param <K> the type of elements on the stack
 */
public interface Stack<K> {

   void push(K o);

   K pop();

   K top();

   K peek(int i);
}
