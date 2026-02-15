package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.IntCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.IntCollections;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.IntSets;

public final class Object2IntMaps {
   public static final Object2IntMaps.EmptyMap EMPTY_MAP = new Object2IntMaps.EmptyMap();

   private Object2IntMaps() {
   }

   public static <K> ObjectIterator<Object2IntMap.Entry<K>> fastIterator(Object2IntMap<K> map) {
      ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
      return entries instanceof Object2IntMap.FastEntrySet ? ((Object2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
   }

   public static <K> void fastForEach(Object2IntMap<K> map, Consumer<? super Object2IntMap.Entry<K>> consumer) {
      ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
      if (entries instanceof Object2IntMap.FastEntrySet) {
         ((Object2IntMap.FastEntrySet)entries).fastForEach(consumer);
      } else {
         entries.forEach(consumer);
      }

   }

   public static <K> ObjectIterable<Object2IntMap.Entry<K>> fastIterable(Object2IntMap<K> map) {
      final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
      return (ObjectIterable)(entries instanceof Object2IntMap.FastEntrySet ? new ObjectIterable<Object2IntMap.Entry<K>>() {
         public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
            return ((Object2IntMap.FastEntrySet)entries).fastIterator();
         }

         public ObjectSpliterator<Object2IntMap.Entry<K>> spliterator() {
            return entries.spliterator();
         }

         public void forEach(Consumer<? super Object2IntMap.Entry<K>> consumer) {
            ((Object2IntMap.FastEntrySet)entries).fastForEach(consumer);
         }
      } : entries);
   }

   public static <K> Object2IntMap<K> emptyMap() {
      return EMPTY_MAP;
   }

   public static <K> Object2IntMap<K> singleton(K key, int value) {
      return new Object2IntMaps.Singleton(key, value);
   }

   public static <K> Object2IntMap<K> singleton(K key, Integer value) {
      return new Object2IntMaps.Singleton(key, value);
   }

   public static <K> Object2IntMap<K> synchronize(Object2IntMap<K> m) {
      return new Object2IntMaps.SynchronizedMap(m);
   }

   public static <K> Object2IntMap<K> synchronize(Object2IntMap<K> m, Object sync) {
      return new Object2IntMaps.SynchronizedMap(m, sync);
   }

   public static <K> Object2IntMap<K> unmodifiable(Object2IntMap<? extends K> m) {
      return new Object2IntMaps.UnmodifiableMap(m);
   }

   public static class EmptyMap<K> extends Object2IntFunctions.EmptyFunction<K> implements Serializable, Cloneable, Object2IntMap<K> {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyMap() {
      }

      public boolean containsValue(int v) {
         return false;
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object key, Integer defaultValue) {
         return defaultValue;
      }

      public int getOrDefault(Object key, int defaultValue) {
         return defaultValue;
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         return false;
      }

      public void putAll(Map<? extends K, ? extends Integer> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
         return ObjectSets.EMPTY_SET;
      }

      public ObjectSet<K> keySet() {
         return ObjectSets.EMPTY_SET;
      }

      public IntCollection values() {
         return IntSets.EMPTY_SET;
      }

      public void forEach(BiConsumer<? super K, ? super Integer> consumer) {
      }

      public Object clone() {
         return Object2IntMaps.EMPTY_MAP;
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

   public static class Singleton<K> extends Object2IntFunctions.Singleton<K> implements Serializable, Cloneable, Object2IntMap<K> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected transient ObjectSet<Object2IntMap.Entry<K>> entries;
      protected transient ObjectSet<K> keys;
      protected transient IntCollection values;

      protected Singleton(K key, int value) {
         super(key, value);
      }

      public boolean containsValue(int v) {
         return this.value == v;
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         return (Integer)ov == this.value;
      }

      public void putAll(Map<? extends K, ? extends Integer> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
         if (this.entries == null) {
            this.entries = ObjectSets.singleton(new AbstractObject2IntMap.BasicEntry(this.key, this.value));
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      public ObjectSet<Entry<K, Integer>> entrySet() {
         return this.object2IntEntrySet();
      }

      public ObjectSet<K> keySet() {
         if (this.keys == null) {
            this.keys = ObjectSets.singleton(this.key);
         }

         return this.keys;
      }

      public IntCollection values() {
         if (this.values == null) {
            this.values = IntSets.singleton(this.value);
         }

         return this.values;
      }

      public boolean isEmpty() {
         return false;
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ this.value;
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

   public static class SynchronizedMap<K> extends Object2IntFunctions.SynchronizedFunction<K> implements Serializable, Object2IntMap<K> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2IntMap<K> map;
      protected transient ObjectSet<Object2IntMap.Entry<K>> entries;
      protected transient ObjectSet<K> keys;
      protected transient IntCollection values;

      protected SynchronizedMap(Object2IntMap<K> m, Object sync) {
         super(m, sync);
         this.map = m;
      }

      protected SynchronizedMap(Object2IntMap<K> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(int v) {
         synchronized(this.sync) {
            return this.map.containsValue(v);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         synchronized(this.sync) {
            return this.map.containsValue(ov);
         }
      }

      public void putAll(Map<? extends K, ? extends Integer> m) {
         synchronized(this.sync) {
            this.map.putAll(m);
         }
      }

      public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
         synchronized(this.sync) {
            if (this.entries == null) {
               this.entries = ObjectSets.synchronize(this.map.object2IntEntrySet(), this.sync);
            }

            return this.entries;
         }
      }

      /** @deprecated */
      @Deprecated
      public ObjectSet<Entry<K, Integer>> entrySet() {
         return this.object2IntEntrySet();
      }

      public ObjectSet<K> keySet() {
         synchronized(this.sync) {
            if (this.keys == null) {
               this.keys = ObjectSets.synchronize(this.map.keySet(), this.sync);
            }

            return this.keys;
         }
      }

      public IntCollection values() {
         synchronized(this.sync) {
            if (this.values == null) {
               this.values = IntCollections.synchronize(this.map.values(), this.sync);
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

      public int getOrDefault(Object key, int defaultValue) {
         synchronized(this.sync) {
            return this.map.getOrDefault(key, defaultValue);
         }
      }

      public void forEach(BiConsumer<? super K, ? super Integer> action) {
         synchronized(this.sync) {
            this.map.forEach(action);
         }
      }

      public void replaceAll(BiFunction<? super K, ? super Integer, ? extends Integer> function) {
         synchronized(this.sync) {
            this.map.replaceAll(function);
         }
      }

      public int putIfAbsent(K key, int value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      public boolean remove(Object key, int value) {
         synchronized(this.sync) {
            return this.map.remove(key, value);
         }
      }

      public int replace(K key, int value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      public boolean replace(K key, int oldValue, int newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      public int computeIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public int computeIfAbsent(K key, Object2IntFunction<? super K> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public int computeIntIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIntIfPresent(key, remappingFunction);
         }
      }

      public int computeInt(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeInt(key, remappingFunction);
         }
      }

      public int merge(K key, int value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
         synchronized(this.sync) {
            return this.map.merge(key, value, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object key, Integer defaultValue) {
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
      public Integer replace(K key, Integer value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(K key, Integer oldValue, Integer newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer putIfAbsent(K key, Integer value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      public Integer computeIfAbsent(K key, Function<? super K, ? extends Integer> mappingFunction) {
         synchronized(this.sync) {
            return (Integer)this.map.computeIfAbsent(key, (Function)mappingFunction);
         }
      }

      public Integer computeIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         synchronized(this.sync) {
            return (Integer)this.map.computeIfPresent(key, remappingFunction);
         }
      }

      public Integer compute(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         synchronized(this.sync) {
            return (Integer)this.map.compute(key, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer merge(K key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
         synchronized(this.sync) {
            return this.map.merge(key, value, remappingFunction);
         }
      }
   }

   public static class UnmodifiableMap<K> extends Object2IntFunctions.UnmodifiableFunction<K> implements Serializable, Object2IntMap<K> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2IntMap<? extends K> map;
      protected transient ObjectSet<Object2IntMap.Entry<K>> entries;
      protected transient ObjectSet<K> keys;
      protected transient IntCollection values;

      protected UnmodifiableMap(Object2IntMap<? extends K> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(int v) {
         return this.map.containsValue(v);
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         return this.map.containsValue(ov);
      }

      public void putAll(Map<? extends K, ? extends Integer> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
         if (this.entries == null) {
            this.entries = ObjectSets.unmodifiable(this.map.object2IntEntrySet());
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      public ObjectSet<Entry<K, Integer>> entrySet() {
         return this.object2IntEntrySet();
      }

      public ObjectSet<K> keySet() {
         if (this.keys == null) {
            this.keys = ObjectSets.unmodifiable(this.map.keySet());
         }

         return this.keys;
      }

      public IntCollection values() {
         if (this.values == null) {
            this.values = IntCollections.unmodifiable(this.map.values());
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

      public int getOrDefault(Object key, int defaultValue) {
         return this.map.getOrDefault(key, defaultValue);
      }

      public void forEach(BiConsumer<? super K, ? super Integer> action) {
         this.map.forEach(action);
      }

      public void replaceAll(BiFunction<? super K, ? super Integer, ? extends Integer> function) {
         throw new UnsupportedOperationException();
      }

      public int putIfAbsent(K key, int value) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object key, int value) {
         throw new UnsupportedOperationException();
      }

      public int replace(K key, int value) {
         throw new UnsupportedOperationException();
      }

      public boolean replace(K key, int oldValue, int newValue) {
         throw new UnsupportedOperationException();
      }

      public int computeIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public int computeIfAbsent(K key, Object2IntFunction<? super K> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public int computeIntIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public int computeInt(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public int merge(K key, int value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object key, Integer defaultValue) {
         return this.map.getOrDefault(key, defaultValue);
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object key, Object value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer replace(K key, Integer value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(K key, Integer oldValue, Integer newValue) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer putIfAbsent(K key, Integer value) {
         throw new UnsupportedOperationException();
      }

      public Integer computeIfAbsent(K key, Function<? super K, ? extends Integer> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public Integer computeIfPresent(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public Integer compute(K key, BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer merge(K key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
         throw new UnsupportedOperationException();
      }
   }
}
