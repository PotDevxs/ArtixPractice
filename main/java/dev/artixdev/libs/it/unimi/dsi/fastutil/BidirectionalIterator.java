package dev.artixdev.libs.it.unimi.dsi.fastutil;

import java.util.Iterator;

/**
 * A bidirectional iterator that can move backwards as well as forwards.
 *
 * @param <K> the type of elements returned by this iterator
 */
public interface BidirectionalIterator<K> extends Iterator<K> {

   boolean hasPrevious();

   K previous();
}
