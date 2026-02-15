package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Size64;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSpliterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSpliterators;

public abstract class AbstractLong2ObjectMap<V> extends AbstractLong2ObjectFunction<V> implements Serializable, Long2ObjectMap<V> {
   private static final long serialVersionUID = -4940583368468432370L;

   protected AbstractLong2ObjectMap() {
   }

   public boolean containsKey(long k) {
      ObjectIterator i = this.long2ObjectEntrySet().iterator();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(((Long2ObjectMap.Entry)i.next()).getLongKey() != k);

      return true;
   }

   public boolean containsValue(Object v) {
      ObjectIterator i = this.long2ObjectEntrySet().iterator();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(((Long2ObjectMap.Entry)i.next()).getValue() != v);

      return true;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public LongSet keySet() {
      return new AbstractLongSet() {
         public boolean contains(long k) {
            return AbstractLong2ObjectMap.this.containsKey(k);
         }

         public int size() {
            return AbstractLong2ObjectMap.this.size();
         }

         public void clear() {
            AbstractLong2ObjectMap.this.clear();
         }

         public LongIterator iterator() {
            return new LongIterator() {
               private final ObjectIterator<Long2ObjectMap.Entry<V>> i = Long2ObjectMaps.fastIterator(AbstractLong2ObjectMap.this);

               public long nextLong() {
                  return ((Long2ObjectMap.Entry)this.i.next()).getLongKey();
               }

               public boolean hasNext() {
                  return this.i.hasNext();
               }

               public void remove() {
                  this.i.remove();
               }

               public void forEachRemaining(java.util.function.LongConsumer action) {
                  this.i.forEachRemaining((entry) -> {
                     action.accept(entry.getLongKey());
                  });
               }
            };
         }

         public LongSpliterator spliterator() {
            return LongSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)AbstractLong2ObjectMap.this), 321);
         }
      };
   }

   public ObjectCollection<V> values() {
      return new AbstractObjectCollection<V>() {
         public boolean contains(Object k) {
            return AbstractLong2ObjectMap.this.containsValue(k);
         }

         public int size() {
            return AbstractLong2ObjectMap.this.size();
         }

         public void clear() {
            AbstractLong2ObjectMap.this.clear();
         }

         public ObjectIterator<V> iterator() {
            return new ObjectIterator<V>() {
               private final ObjectIterator<Long2ObjectMap.Entry<V>> i = Long2ObjectMaps.fastIterator(AbstractLong2ObjectMap.this);

               public V next() {
                  return ((Long2ObjectMap.Entry)this.i.next()).getValue();
               }

               public boolean hasNext() {
                  return this.i.hasNext();
               }

               public void remove() {
                  this.i.remove();
               }

               public void forEachRemaining(Consumer<? super V> action) {
                  this.i.forEachRemaining((entry) -> {
                     action.accept(entry.getValue());
                  });
               }
            };
         }

         public ObjectSpliterator<V> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)AbstractLong2ObjectMap.this), 64);
         }
      };
   }

   public void putAll(Map<? extends Long, ? extends V> m) {
      if (m instanceof Long2ObjectMap) {
         ObjectIterator i = Long2ObjectMaps.fastIterator((Long2ObjectMap)m);

         while(i.hasNext()) {
            Long2ObjectMap.Entry<? extends V> e = (Long2ObjectMap.Entry)i.next();
            this.put(e.getLongKey(), e.getValue());
         }
      } else {
         int n = m.size();
         Iterator i = m.entrySet().iterator();

         while(n-- != 0) {
            java.util.Map.Entry<? extends Long, ? extends V> e = (java.util.Map.Entry)i.next();
            this.put((Long)e.getKey(), e.getValue());
         }
      }

   }

   public int hashCode() {
      int h = 0;
      int n = this.size();

      for(ObjectIterator i = Long2ObjectMaps.fastIterator(this); n-- != 0; h += ((Long2ObjectMap.Entry)i.next()).hashCode()) {
      }

      return h;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Map)) {
         return false;
      } else {
         Map<?, ?> m = (Map)o;
         return m.size() != this.size() ? false : this.long2ObjectEntrySet().containsAll(m.entrySet());
      }
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      ObjectIterator<Long2ObjectMap.Entry<V>> i = Long2ObjectMaps.fastIterator(this);
      int n = this.size();
      boolean first = true;
      s.append("{");

      while(n-- != 0) {
         if (first) {
            first = false;
         } else {
            s.append(", ");
         }

         Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry)i.next();
         s.append(String.valueOf(e.getLongKey()));
         s.append("=>");
         if (this == e.getValue()) {
            s.append("(this map)");
         } else {
            s.append(String.valueOf(e.getValue()));
         }
      }

      s.append("}");
      return s.toString();
   }

   public abstract static class BasicEntrySet<V> extends AbstractObjectSet<Long2ObjectMap.Entry<V>> {
      protected final Long2ObjectMap<V> map;

      public BasicEntrySet(Long2ObjectMap<V> map) {
         this.map = map;
      }

      public boolean contains(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else if (o instanceof Long2ObjectMap.Entry) {
            Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry)o;
            long k = e.getLongKey();
            return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            Object key = e.getKey();
            if (key != null && key instanceof Long) {
               long k = (Long)key;
               Object value = e.getValue();
               return this.map.containsKey(k) && Objects.equals(this.map.get(k), value);
            } else {
               return false;
            }
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else if (o instanceof Long2ObjectMap.Entry) {
            Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry)o;
            return this.map.remove(e.getLongKey(), e.getValue());
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            Object key = e.getKey();
            if (key != null && key instanceof Long) {
               long k = (Long)key;
               Object v = e.getValue();
               return this.map.remove(k, v);
            } else {
               return false;
            }
         }
      }

      public int size() {
         return this.map.size();
      }

      public ObjectSpliterator<Long2ObjectMap.Entry<V>> spliterator() {
         return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)this.map), 65);
      }
   }

   public static class BasicEntry<V> implements Long2ObjectMap.Entry<V> {
      protected long key;
      protected V value;

      public BasicEntry() {
      }

      public BasicEntry(Long key, V value) {
         this.key = key;
         this.value = value;
      }

      public BasicEntry(long key, V value) {
         this.key = key;
         this.value = value;
      }

      public long getLongKey() {
         return this.key;
      }

      public V getValue() {
         return this.value;
      }

      public V setValue(V value) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else if (o instanceof Long2ObjectMap.Entry) {
            Long2ObjectMap.Entry<V> e = (Long2ObjectMap.Entry)o;
            return this.key == e.getLongKey() && Objects.equals(this.value, e.getValue());
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            Object key = e.getKey();
            if (key != null && key instanceof Long) {
               Object value = e.getValue();
               return this.key == (Long)key && Objects.equals(this.value, value);
            } else {
               return false;
            }
         }
      }

      public int hashCode() {
         return HashCommon.long2int(this.key) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return this.key + "->" + this.value;
      }
   }
}
