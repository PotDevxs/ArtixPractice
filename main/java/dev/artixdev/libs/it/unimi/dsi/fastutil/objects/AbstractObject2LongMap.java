package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Size64;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongBinaryOperator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongSpliterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongSpliterators;

public abstract class AbstractObject2LongMap<K> extends AbstractObject2LongFunction<K> implements Serializable, Object2LongMap<K> {
   private static final long serialVersionUID = -4940583368468432370L;

   protected AbstractObject2LongMap() {
   }

   public boolean containsKey(Object k) {
      ObjectIterator i = this.object2LongEntrySet().iterator();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(((Object2LongMap.Entry)i.next()).getKey() != k);

      return true;
   }

   public boolean containsValue(long v) {
      ObjectIterator i = this.object2LongEntrySet().iterator();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(((Object2LongMap.Entry)i.next()).getLongValue() != v);

      return true;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public final long mergeLong(K key, long value, LongBinaryOperator remappingFunction) {
      return this.mergeLong(key, value, remappingFunction);
   }

   public ObjectSet<K> keySet() {
      return new AbstractObjectSet<K>() {
         public boolean contains(Object k) {
            return AbstractObject2LongMap.this.containsKey(k);
         }

         public int size() {
            return AbstractObject2LongMap.this.size();
         }

         public void clear() {
            AbstractObject2LongMap.this.clear();
         }

         public ObjectIterator<K> iterator() {
            return new ObjectIterator<K>() {
               private final ObjectIterator<Object2LongMap.Entry<K>> i = Object2LongMaps.fastIterator(AbstractObject2LongMap.this);

               public K next() {
                  return this.i.next().getKey();
               }

               public boolean hasNext() {
                  return this.i.hasNext();
               }

               public void remove() {
                  this.i.remove();
               }

               public void forEachRemaining(Consumer<? super K> action) {
                  this.i.forEachRemaining((entry) -> {
                     action.accept(entry.getKey());
                  });
               }
            };
         }

         public ObjectSpliterator<K> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)AbstractObject2LongMap.this), 65);
         }
      };
   }

   public LongCollection values() {
      return new AbstractLongCollection() {
         public boolean contains(long k) {
            return AbstractObject2LongMap.this.containsValue(k);
         }

         public int size() {
            return AbstractObject2LongMap.this.size();
         }

         public void clear() {
            AbstractObject2LongMap.this.clear();
         }

         public LongIterator iterator() {
            return new LongIterator() {
               private final ObjectIterator<Object2LongMap.Entry<K>> i = Object2LongMaps.fastIterator(AbstractObject2LongMap.this);

               public long nextLong() {
                  return ((Object2LongMap.Entry)this.i.next()).getLongValue();
               }

               public boolean hasNext() {
                  return this.i.hasNext();
               }

               public void remove() {
                  this.i.remove();
               }

               public void forEachRemaining(LongConsumer action) {
                  this.i.forEachRemaining((entry) -> {
                     action.accept(entry.getLongValue());
                  });
               }
            };
         }

         public LongSpliterator spliterator() {
            return LongSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)AbstractObject2LongMap.this), 320);
         }
      };
   }

   public void putAll(Map<? extends K, ? extends Long> m) {
      if (m instanceof Object2LongMap) {
         ObjectIterator i = Object2LongMaps.fastIterator((Object2LongMap)m);

         while(i.hasNext()) {
            Object2LongMap.Entry<? extends K> e = (Object2LongMap.Entry)i.next();
            this.put(e.getKey(), e.getLongValue());
         }
      } else {
         int n = m.size();
         Iterator i = m.entrySet().iterator();

         while(n-- != 0) {
            java.util.Map.Entry<? extends K, ? extends Long> e = (java.util.Map.Entry)i.next();
            this.put(e.getKey(), (Long)e.getValue());
         }
      }

   }

   public int hashCode() {
      int h = 0;
      int n = this.size();

      for(ObjectIterator i = Object2LongMaps.fastIterator(this); n-- != 0; h += ((Object2LongMap.Entry)i.next()).hashCode()) {
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
         return m.size() != this.size() ? false : this.object2LongEntrySet().containsAll(m.entrySet());
      }
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      ObjectIterator<Object2LongMap.Entry<K>> i = Object2LongMaps.fastIterator(this);
      int n = this.size();
      boolean first = true;
      s.append("{");

      while(n-- != 0) {
         if (first) {
            first = false;
         } else {
            s.append(", ");
         }

         Object2LongMap.Entry<K> e = (Object2LongMap.Entry)i.next();
         if (this == e.getKey()) {
            s.append("(this map)");
         } else {
            s.append(String.valueOf(e.getKey()));
         }

         s.append("=>");
         s.append(String.valueOf(e.getLongValue()));
      }

      s.append("}");
      return s.toString();
   }

   public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Object2LongMap.Entry<K>> {
      protected final Object2LongMap<K> map;

      public BasicEntrySet(Object2LongMap<K> map) {
         this.map = map;
      }

      public boolean contains(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            Object k;
            if (o instanceof Object2LongMap.Entry) {
               Object2LongMap.Entry<K> e = (Object2LongMap.Entry)o;
               k = e.getKey();
               return this.map.containsKey(k) && this.map.getLong(k) == e.getLongValue();
            } else {
               java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
               k = e.getKey();
               Object value = e.getValue();
               if (value != null && value instanceof Long) {
                  return this.map.containsKey(k) && this.map.getLong(k) == (Long)value;
               } else {
                  return false;
               }
            }
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else if (o instanceof Object2LongMap.Entry) {
            Object2LongMap.Entry<K> e = (Object2LongMap.Entry)o;
            return this.map.remove(e.getKey(), e.getLongValue());
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            Object k = e.getKey();
            Object value = e.getValue();
            if (value != null && value instanceof Long) {
               long v = (Long)value;
               return this.map.remove(k, v);
            } else {
               return false;
            }
         }
      }

      public int size() {
         return this.map.size();
      }

      public ObjectSpliterator<Object2LongMap.Entry<K>> spliterator() {
         return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Map)this.map), 65);
      }
   }

   public static class BasicEntry<K> implements Object2LongMap.Entry<K> {
      protected K key;
      protected long value;

      public BasicEntry() {
      }

      public BasicEntry(K key, Long value) {
         this.key = key;
         this.value = value;
      }

      public BasicEntry(K key, long value) {
         this.key = key;
         this.value = value;
      }

      public K getKey() {
         return this.key;
      }

      public long getLongValue() {
         return this.value;
      }

      public long setValue(long value) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else if (o instanceof Object2LongMap.Entry) {
            Object2LongMap.Entry<K> e = (Object2LongMap.Entry)o;
            return Objects.equals(this.key, e.getKey()) && this.value == e.getLongValue();
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            Object key = e.getKey();
            Object value = e.getValue();
            if (value != null && value instanceof Long) {
               return Objects.equals(this.key, key) && this.value == (Long)value;
            } else {
               return false;
            }
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ HashCommon.long2int(this.value);
      }

      public String toString() {
         return this.key + "->" + this.value;
      }
   }
}
