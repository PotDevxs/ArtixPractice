package dev.artixdev.libs.it.unimi.dsi.fastutil;

import java.util.Iterator;

public interface BidirectionalIterator<K> extends Iterator<K> {
   K previous();

   boolean hasPrevious();
}
