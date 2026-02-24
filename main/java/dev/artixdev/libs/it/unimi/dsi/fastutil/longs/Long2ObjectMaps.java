package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

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
import java.util.function.LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectCollections;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectIterable;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSet;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSets;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSpliterator;

public final class Long2ObjectMaps {
   public static final Long2ObjectMaps.EmptyMap EMPTY_MAP = new Long2ObjectMaps.EmptyMap();

   private Long2ObjectMaps() {
   }

   public static <V> ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator(Long2ObjectMap<V> map) {
      ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
      return entries instanceof Long2ObjectMap.FastEntrySet ? ((Long2ObjectMap.FastEntrySet)entries).fastIterator() : entries.iterator();
   }

   public static <V> void fastForEach(Long2ObjectMap<V> map, Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
      ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
      if (entries instanceof Long2ObjectMap.FastEntrySet) {
         ((Long2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
      } else {
         entries.forEach(consumer);
      }

   }

   public static <V> ObjectIterable<Long2ObjectMap.Entry<V>> fastIterable(Long2ObjectMap<V> map) {
      final ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
      return (ObjectIterable)(entries instanceof Long2ObjectMap.FastEntrySet ? new ObjectIterable<Long2ObjectMap.Entry<V>>() {
         public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
            return ((Long2ObjectMap.FastEntrySet)entries).fastIterator();
         }

         public ObjectSpliterator<Long2ObjectMap.Entry<V>> spliterator() {
            return entries.spliterator();
         }

         public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
            ((Long2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
         }
      } : entries);
   }

   public static <V> Long2ObjectMap<V> emptyMap() {
      return EMPTY_MAP;
   }

   public static <V> Long2ObjectMap<V> singleton(long key, V value) {
      return new Long2ObjectMaps.Singleton(key, value);
   }

   public static <V> Long2ObjectMap<V> singleton(Long key, V value) {
      return new Long2ObjectMaps.Singleton(key, value);
   }

   public static <V> Long2ObjectMap<V> synchronize(Long2ObjectMap<V> m) {
      return new Long2ObjectMaps.SynchronizedMap(m);
   }

   public static <V> Long2ObjectMap<V> synchronize(Long2ObjectMap<V> m, Object sync) {
      return new Long2ObjectMaps.SynchronizedMap(m, sync);
   }

   public static <V> Long2ObjectMap<V> unmodifiable(Long2ObjectMap<? extends V> m) {
      return new Long2ObjectMaps.UnmodifiableMap(m);
   }

   public static class EmptyMap<V> extends Long2ObjectFunctions.EmptyFunction<V> implements Serializable, Cloneable, Long2ObjectMap<V> {
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

      public V getOrDefault(long key, V defaultValue) {
         return defaultValue;
      }

      public void putAll(Map<? extends Long, ? extends V> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
         return ObjectSets.EMPTY_SET;
      }

      public LongSet keySet() {
         return LongSets.EMPTY_SET;
      }

      public ObjectCollection<V> values() {
         return ObjectSets.EMPTY_SET;
      }

      public void forEach(BiConsumer<? super Long, ? super V> consumer) {
      }

      public Object clone() {
         return Long2ObjectMaps.EMPTY_MAP;
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

   public static class Singleton<V> extends Long2ObjectFunctions.Singleton<V> implements Serializable, Cloneable, Long2ObjectMap<V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected transient ObjectSet<Long2ObjectMap.Entry<V>> entries;
      protected transient LongSet keys;
      protected transient ObjectCollection<V> values;

      protected Singleton(long key, V value) {
         super(key, value);
      }

      public boolean containsValue(Object v) {
         return Objects.equals(this.value, v);
      }

      public void putAll(Map<? extends Long, ? extends V> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
         if (this.entries == null) {
            this.entries = ObjectSets.singleton(new AbstractLong2ObjectMap.BasicEntry(this.key, this.value));
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      @SuppressWarnings("unchecked")
      public ObjectSet<Map.Entry<Long, V>> entrySet() {
         return (ObjectSet<Map.Entry<Long, V>>) (ObjectSet<?>) this.long2ObjectEntrySet();
      }

      public LongSet keySet() {
         if (this.keys == null) {
            this.keys = LongSets.singleton(this.key);
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
         return HashCommon.long2int(this.key) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Map)) {
            return false;
         } else {
            Map<?, ?> m = (Map)o;
            return m.size() != 1 ? false : ((Entry)m.entrySet().iterator().next()).equals(this.entrySet().iterator().next());
         }
      }

      public String toString() {
         return "{" + this.key + "=>" + this.value + "}";
      }
   }

   public static class SynchronizedMap<V> extends Long2ObjectFunctions.SynchronizedFunction<V> implements Serializable, Long2ObjectMap<V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Long2ObjectMap<V> map;
      protected transient ObjectSet<Long2ObjectMap.Entry<V>> entries;
      protected transient LongSet keys;
      protected transient ObjectCollection<V> values;

      protected SynchronizedMap(Long2ObjectMap<V> m, Object sync) {
         super(m, sync);
         this.map = m;
      }

      protected SynchronizedMap(Long2ObjectMap<V> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(Object v) {
         synchronized(this.sync) {
            return this.map.containsValue(v);
         }
      }

      public void putAll(Map<? extends Long, ? extends V> m) {
         synchronized(this.sync) {
            this.map.putAll(m);
         }
      }

      public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
         synchronized(this.sync) {
            if (this.entries == null) {
               this.entries = ObjectSets.synchronize(this.map.long2ObjectEntrySet(), this.sync);
            }

            return this.entries;
         }
      }

      /** @deprecated */
      @Deprecated
      @SuppressWarnings("unchecked")
      public ObjectSet<Map.Entry<Long, V>> entrySet() {
         return (ObjectSet<Map.Entry<Long, V>>) (ObjectSet<?>) this.long2ObjectEntrySet();
      }

      public LongSet keySet() {
         synchronized(this.sync) {
            if (this.keys == null) {
               this.keys = LongSets.synchronize(this.map.keySet(), this.sync);
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

      public V getOrDefault(long key, V defaultValue) {
         synchronized(this.sync) {
            return this.map.getOrDefault(key, defaultValue);
         }
      }

      public void forEach(BiConsumer<? super Long, ? super V> action) {
         synchronized(this.sync) {
            this.map.forEach(action);
         }
      }

      public void replaceAll(BiFunction<? super Long, ? super V, ? extends V> function) {
         synchronized(this.sync) {
            this.map.replaceAll(function);
         }
      }

      public V putIfAbsent(long key, V value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      public boolean remove(long key, Object value) {
         synchronized(this.sync) {
            return this.map.remove(key, value);
         }
      }

      public V replace(long key, V value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      public boolean replace(long key, V oldValue, V newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      public V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public V computeIfPresent(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfPresent(key, remappingFunction);
         }
      }

      public V compute(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.compute(key, remappingFunction);
         }
      }

      public V merge(long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
      public V replace(Long key, V value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(Long key, V oldValue, V newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public V putIfAbsent(Long key, V value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public V computeIfAbsent(Long key, Function<? super Long, ? extends V> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public V computeIfPresent(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfPresent(key, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public V compute(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.compute(key, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public V merge(Long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         synchronized(this.sync) {
            return this.map.merge(key, value, remappingFunction);
         }
      }
   }

   public static class UnmodifiableMap<V> extends Long2ObjectFunctions.UnmodifiableFunction<V> implements Serializable, Long2ObjectMap<V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Long2ObjectMap<? extends V> map;
      protected transient ObjectSet<Long2ObjectMap.Entry<V>> entries;
      protected transient LongSet keys;
      protected transient ObjectCollection<V> values;

      protected UnmodifiableMap(Long2ObjectMap<? extends V> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(Object v) {
         return this.map.containsValue(v);
      }

      public void putAll(Map<? extends Long, ? extends V> m) {
         throw new UnsupportedOperationException();
      }

      @SuppressWarnings("unchecked")
      public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
         if (this.entries == null) {
            this.entries = (ObjectSet<Long2ObjectMap.Entry<V>>) (ObjectSet<?>) ObjectSets.unmodifiable(this.map.long2ObjectEntrySet());
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      @SuppressWarnings("unchecked")
      public ObjectSet<Map.Entry<Long, V>> entrySet() {
         return (ObjectSet<Map.Entry<Long, V>>) (ObjectSet<?>) this.long2ObjectEntrySet();
      }

      public LongSet keySet() {
         if (this.keys == null) {
            this.keys = LongSets.unmodifiable(this.map.keySet());
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

      public V getOrDefault(long key, V defaultValue) {
         V v = this.map.get(key);
         return v == this.map.defaultReturnValue() && !this.map.containsKey(key) ? defaultValue : v;
      }

      public void forEach(BiConsumer<? super Long, ? super V> action) {
         this.map.forEach(action);
      }

      public void replaceAll(BiFunction<? super Long, ? super V, ? extends V> function) {
         throw new UnsupportedOperationException();
      }

      public V putIfAbsent(long key, V value) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(long key, Object value) {
         throw new UnsupportedOperationException();
      }

      public V replace(long key, V value) {
         throw new UnsupportedOperationException();
      }

      public boolean replace(long key, V oldValue, V newValue) {
         throw new UnsupportedOperationException();
      }

      public V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V computeIfPresent(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V compute(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public V merge(long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V getOrDefault(Object key, V defaultValue) {
         if (key == null) return defaultValue;
         long k = (Long) key;
         V v = this.map.get(k);
         return v == this.map.defaultReturnValue() && !this.map.containsKey(k) ? defaultValue : v;
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object key, Object value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V replace(Long key, V value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(Long key, V oldValue, V newValue) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V putIfAbsent(Long key, V value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V computeIfAbsent(Long key, Function<? super Long, ? extends V> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V computeIfPresent(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V compute(Long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V merge(Long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         throw new UnsupportedOperationException();
      }
   }
}
