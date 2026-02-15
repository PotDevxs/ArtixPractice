package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Size64;

public interface ObjectCollection<K> extends Collection<K>, ObjectIterable<K> {
   ObjectIterator<K> iterator();

   default ObjectSpliterator<K> spliterator() {
      return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Collection)this), 64);
   }
}
