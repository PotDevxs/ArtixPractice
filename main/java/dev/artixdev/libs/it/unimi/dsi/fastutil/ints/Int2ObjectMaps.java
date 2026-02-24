package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectCollections;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectIterable;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSet;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSets;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSpliterator;

public final class Int2ObjectMaps {
   public static final Int2ObjectMaps.EmptyMap EMPTY_MAP = new Int2ObjectMaps.EmptyMap();

   private Int2ObjectMaps() {
   }

   public static <V> ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator(Int2ObjectMap<V> map) {
      ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
      return entries instanceof Int2ObjectMap.FastEntrySet ? ((Int2ObjectMap.FastEntrySet)entries).fastIterator() : entries.iterator();
   }

   public static <V> void fastForEach(Int2ObjectMap<V> map, Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
      ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
      if (entries instanceof Int2ObjectMap.FastEntrySet) {
         ((Int2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
      } else {
         entries.forEach(consumer);
      }

   }

   public static <V> ObjectIterable<Int2ObjectMap.Entry<V>> fastIterable(Int2ObjectMap<V> map) {
      final ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
      return (ObjectIterable)(entries instanceof Int2ObjectMap.FastEntrySet ? new ObjectIterable<Int2ObjectMap.Entry<V>>() {
         public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return ((Int2ObjectMap.FastEntrySet)entries).fastIterator();
         }

         public ObjectSpliterator<Int2ObjectMap.Entry<V>> spliterator() {
            return entries.spliterator();
         }

         public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            ((Int2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
         }
      } : entries);
   }

   public static <V> Int2ObjectMap<V> emptyMap() {
      return EMPTY_MAP;
   }

   public static <V> Int2ObjectMap<V> singleton(int key, V value) {
      return new Int2ObjectMaps.Singleton(key, value);
   }

   public static <V> Int2ObjectMap<V> singleton(Integer key, V value) {
      return new Int2ObjectMaps.Singleton(key, value);
   }

   public static <V> Int2ObjectMap<V> synchronize(Int2ObjectMap<V> m) {
      return new Int2ObjectMaps.SynchronizedMap(m);
   }

   public static <V> Int2ObjectMap<V> synchronize(Int2ObjectMap<V> m, Object sync) {
      return new Int2ObjectMaps.SynchronizedMap(m, sync);
   }

   public static <V> Int2ObjectMap<V> unmodifiable(Int2ObjectMap<? extends V> m) {
      return new Int2ObjectMaps.UnmodifiableMap(m);
   }

   public static class EmptyMap<V> extends Int2ObjectFunctions.EmptyFunction<V> implements Serializable, Cloneable, Int2ObjectMap<V> {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyMap() {
      }

      public boolean containsValue(Object v) {
         return false;
      }

      /** @deprecated */
      @Deprecated
      public V getOrDefault(Object key, V defaultValue) {
         return defaultValue;
      }

      public V getOrDefault(int key, V defaultValue) {
         return defaultValue;
      }

      public void putAll(Map<? extends Integer, ? extends V> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
         return ObjectSets.EMPTY_SET;
      }

      public IntSet keySet() {
         return IntSets.EMPTY_SET;
      }

      public ObjectCollection<V> values() {
         return ObjectSets.EMPTY_SET;
      }

      public void forEach(BiConsumer<? super Integer, ? super V> consumer) {
      }

      public Object clone() {
         return Int2ObjectMaps.EMPTY_MAP;
      }

      public boolean isEmpty() {
         return true;
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         return !(o instanceof Map) ? false : ((Map)o).isEmpty();
      }

      public String toString() {
         return "{}";
      }
   }

   public static class Singleton<V> extends Int2ObjectFunctions.Singleton<V> implements Serializable, Cloneable, Int2ObjectMap<V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected transient ObjectSet<Int2ObjectMap.Entry<V>> entries;
      protected transient IntSet keys;
      protected transient ObjectCollection<V> values;

      protected Singleton(int key, V value) {
         super(key, value);
      }

      public boolean containsValue(Object v) {
         return Objects.equals(this.value, v);
      }

      public void putAll(Map<? extends Integer, ? extends V> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
         if (this.entries == null) {
            this.entries = ObjectSets.singleton(new AbstractInt2ObjectMap.BasicEntry(this.key, this.value));
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      @SuppressWarnings("unchecked")
      public ObjectSet<java.util.Map.Entry<Integer, V>> entrySet() {
         return (ObjectSet<java.util.Map.Entry<Integer, V>>) (ObjectSet<?>) this.int2ObjectEntrySet();
      }

      public IntSet keySet() {
         if (this.keys == null) {
            this.keys = IntSets.singleton(this.key);
         }

         return this.keys;
      }

      public ObjectCollection<V> values() {
         if (this.values == null) {
            this.values = ObjectSets.singleton(this.value);
         }

         return this.values;
      }

      public boolean isEmpty() {
         return false;
      }

      public int hashCode() {
         return this.key ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Map)) {
            return false;
         } else {
            Map<?, ?> m = (Map)o;
            return m.size() != 1 ? false : ((java.util.Map.Entry<?, ?>) m.entrySet().iterator().next()).equals(this.entrySet().iterator().next());
         }
      }

      public String toString() {
         return "{" + this.key + "=>" + this.value + "}";
      }
   }

   public static class SynchronizedMap<V> extends Int2ObjectFunctions.SynchronizedFunction<V> implements Serializable, Int2ObjectMap<V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Int2ObjectMap<V> map;
      protected transient ObjectSet<Int2ObjectMap.Entry<V>> entries;
      protected transient IntSet keys;
      protected transient ObjectCollection<V> values;

      protected SynchronizedMap(Int2ObjectMap<V> m, Object sync) {
         super(m, sync);
         this.map = m;
      }

      protected SynchronizedMap(Int2ObjectMap<V> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(Object v) {
         synchronized(this.sync) {
            return this.map.containsValue(v);
         }
      }

      public void putAll(Map<? extends Integer, ? extends V> m) {
         synchronized(this.sync) {
            this.map.putAll(m);
         }
      }

      public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
         synchronized(this.sync) {
            if (this.entries == null) {
               this.entries = ObjectSets.synchronize(this.map.int2ObjectEntrySet(), this.sync);
            }

            return this.entries;
         }
      }

      /** @deprecated */
      @Deprecated
      @SuppressWarnings("unchecked")
      public ObjectSet<java.util.Map.Entry<Integer, V>> entrySet() {
         return (ObjectSet<java.util.Map.Entry<Integer, V>>) (ObjectSet<?>) this.int2ObjectEntrySet();
      }

      public IntSet keySet() {
         synchronized(this.sync) {
            if (this.keys == null) {
               this.keys = IntSets.synchronize(this.map.keySet(), this.sync);
            }

            return this.keys;
         }
      }

      public ObjectCollection<V> values() {
         synchronized(this.sync) {
            if (this.values == null) {
               this.values = ObjectCollections.synchronize(this.map.values(), this.sync);
            }

            return this.values;
         }
      }

      public boolean isEmpty() {
         synchronized(this.sync) {
            return this.map.isEmpty();
         }
      }

      public int hashCode() {
         synchronized(this.sync) {
            return this.map.hashCode();
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.sync) {
               return this.map.equals(o);
            }
         }
      }

      private void writeObject(ObjectOutputStream s) throws IOException {
         synchronized(this.sync) {
            s.defaultWriteObject();
         }
      }

      public V getOrDefault(int key, V defaultValue) {
         synchronized(this.sync) {
            return this.map.getOrDefault(key, defaultValue);
         }
      }

      public void forEach(BiConsumer<? super Integer, ? super V> action) {
         synchronized(this.sync) {
            this.map.forEach(action);
         }
      }

      public void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> function) {
         synchronized(this.sync) {
            this.map.replaceAll(function);
         }
      }

      public V putIfAbsent(int key, V value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      public boolean remove(int key, Object value) {
         synchronized(this.sync) {
            return this.map.remove(key, value);
         }
      }

      public V replace(int key, V value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      public boolean replace(int key, V oldValue, V newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      public V computeIfAbsent(int key, IntFunction<? extends V> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public V computeIfPresent(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfPresent(key, remappingFunction);
         }
      }

      public V compute(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.compute(key, remappingFunction);
         }
      }

      public V merge(int key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.merge(key, value, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public V getOrDefault(Object key, V defaultValue) {
         synchronized(this.sync) {
            return this.map.getOrDefault(key, defaultValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object key, Object value) {
         synchronized(this.sync) {
            return this.map.remove(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public V replace(Integer key, V value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(Integer key, V oldValue, V newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public V putIfAbsent(Integer key, V value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public V computeIfAbsent(Integer key, Function<? super Integer, ? extends V> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public V computeIfPresent(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfPresent(key, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public V compute(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.compute(key, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public V merge(Integer key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.merge(key, value, remappingFunction);
         }
      }
   }

   public static class UnmodifiableMap<V> extends Int2ObjectFunctions.UnmodifiableFunction<V> implements Serializable, Int2ObjectMap<V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Int2ObjectMap<? extends V> map;
      protected transient ObjectSet<Int2ObjectMap.Entry<V>> entries;
      protected transient IntSet keys;
      protected transient ObjectCollection<V> values;

      protected UnmodifiableMap(Int2ObjectMap<? extends V> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(Object v) {
         return this.map.containsValue(v);
      }

      public void putAll(Map<? extends Integer, ? extends V> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
         if (this.entries == null) {
            @SuppressWarnings("unchecked")
            ObjectSet<Int2ObjectMap.Entry<V>> unmodifiable = (ObjectSet<Int2ObjectMap.Entry<V>>) (ObjectSet<?>) ObjectSets.unmodifiable(this.map.int2ObjectEntrySet());
            this.entries = unmodifiable;
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      @SuppressWarnings("unchecked")
      public ObjectSet<java.util.Map.Entry<Integer, V>> entrySet() {
         return (ObjectSet<java.util.Map.Entry<Integer, V>>) (ObjectSet<?>) this.int2ObjectEntrySet();
      }

      public IntSet keySet() {
         if (this.keys == null) {
            this.keys = IntSets.unmodifiable(this.map.keySet());
         }

         return this.keys;
      }

      public ObjectCollection<V> values() {
         if (this.values == null) {
            this.values = ObjectCollections.unmodifiable(this.map.values());
         }

         return this.values;
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public int hashCode() {
         return this.map.hashCode();
      }

      public boolean equals(Object o) {
         return o == this ? true : this.map.equals(o);
      }

      @SuppressWarnings("unchecked")
      public V getOrDefault(int key, V defaultValue) {
         return ((Int2ObjectMap<V>) this.map).getOrDefault(key, defaultValue);
      }

      public void forEach(BiConsumer<? super Integer, ? super V> action) {
         this.map.forEach(action);
      }

      public void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> function) {
         throw new UnsupportedOperationException();
      }

      public V putIfAbsent(int key, V value) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(int key, Object value) {
         throw new UnsupportedOperationException();
      }

      public V replace(int key, V value) {
         throw new UnsupportedOperationException();
      }

      public boolean replace(int key, V oldValue, V newValue) {
         throw new UnsupportedOperationException();
      }

      public V computeIfAbsent(int key, IntFunction<? extends V> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V computeIfPresent(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V compute(int key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V merge(int key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      @SuppressWarnings("unchecked")
      public V getOrDefault(Object key, V defaultValue) {
         return ((Int2ObjectMap<V>) this.map).getOrDefault(key, defaultValue);
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object key, Object value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V replace(Integer key, V value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(Integer key, V oldValue, V newValue) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V putIfAbsent(Integer key, V value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V computeIfAbsent(Integer key, Function<? super Integer, ? extends V> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V computeIfPresent(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V compute(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V merge(Integer key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }
   }
}
