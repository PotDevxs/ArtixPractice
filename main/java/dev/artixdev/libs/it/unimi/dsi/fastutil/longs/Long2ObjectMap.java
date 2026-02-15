package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSet;

public interface Long2ObjectMap<V> extends Map<Long, V>, Long2ObjectFunction<V> {
   int size();

   default void clear() {
      throw new UnsupportedOperationException();
   }

   void defaultReturnValue(V var1);

   V defaultReturnValue();

   ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet();

   /** @deprecated */
   @Deprecated
   default ObjectSet<java.util.Map.Entry<Long, V>> entrySet() {
      return this.long2ObjectEntrySet();
   }

   /** @deprecated */
   @Deprecated
   default V put(Long key, V value) {
      return Long2ObjectFunction.super.put(key, value);
   }

   /** @deprecated */
   @Deprecated
   default V get(Object key) {
      return Long2ObjectFunction.super.get(key);
   }

   /** @deprecated */
   @Deprecated
   default V remove(Object key) {
      return Long2ObjectFunction.super.remove(key);
   }

   LongSet keySet();

   ObjectCollection<V> values();

   boolean containsKey(long var1);

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return Long2ObjectFunction.super.containsKey(key);
   }

   default void forEach(BiConsumer<? super Long, ? super V> consumer) {
      ObjectSet<Long2ObjectMap.Entry<V>> entrySet = this.long2ObjectEntrySet();
      Consumer<Long2ObjectMap.Entry<V>> wrappingConsumer = (entry) -> {
         consumer.accept(entry.getLongKey(), entry.getValue());
      };
      if (entrySet instanceof Long2ObjectMap.FastEntrySet) {
         ((Long2ObjectMap.FastEntrySet)entrySet).fastForEach(wrappingConsumer);
      } else {
         entrySet.forEach(wrappingConsumer);
      }

   }

   default V getOrDefault(long key, V defaultValue) {
      Object v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default V getOrDefault(Object key, V defaultValue) {
      return super.getOrDefault(key, defaultValue);
   }

   default V putIfAbsent(long key, V value) {
      V v = this.get(key);
      V drv = this.defaultReturnValue();
      if (v == drv && !this.containsKey(key)) {
         this.put(key, value);
         return drv;
      } else {
         return v;
      }
   }

   default boolean remove(long key, Object value) {
      V curValue = this.get(key);
      if (Objects.equals(curValue, value) && (curValue != this.defaultReturnValue() || this.containsKey(key))) {
         this.remove(key);
         return true;
      } else {
         return false;
      }
   }

   default boolean replace(long key, V oldValue, V newValue) {
      V curValue = this.get(key);
      if (Objects.equals(curValue, oldValue) && (curValue != this.defaultReturnValue() || this.containsKey(key))) {
         this.put(key, newValue);
         return true;
      } else {
         return false;
      }
   }

   default V replace(long key, V value) {
      return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
   }

   default V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      V v = this.get(key);
      if (v == this.defaultReturnValue() && !this.containsKey(key)) {
         V newValue = mappingFunction.apply(key);
         this.put(key, newValue);
         return newValue;
      } else {
         return v;
      }
   }

   default V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      V v = this.get(key);
      V drv = this.defaultReturnValue();
      if (v == drv && !this.containsKey(key)) {
         if (!mappingFunction.containsKey(key)) {
            return drv;
         } else {
            V newValue = mappingFunction.get(key);
            this.put(key, newValue);
            return newValue;
         }
      } else {
         return v;
      }
   }

   /** @deprecated */
   @Deprecated
   default V computeIfAbsentPartial(long key, Long2ObjectFunction<? extends V> mappingFunction) {
      return this.computeIfAbsent(key, mappingFunction);
   }

   default V computeIfPresent(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      V oldValue = this.get(key);
      V drv = this.defaultReturnValue();
      if (oldValue == drv && !this.containsKey(key)) {
         return drv;
      } else {
         V newValue = remappingFunction.apply(key, oldValue);
         if (newValue == null) {
            this.remove(key);
            return drv;
         } else {
            this.put(key, newValue);
            return newValue;
         }
      }
   }

   default V compute(long key, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      V oldValue = this.get(key);
      V drv = this.defaultReturnValue();
      boolean contained = oldValue != drv || this.containsKey(key);
      V newValue = remappingFunction.apply(key, contained ? oldValue : null);
      if (newValue == null) {
         if (contained) {
            this.remove(key);
         }

         return drv;
      } else {
         this.put(key, newValue);
         return newValue;
      }
   }

   default V merge(long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      Objects.requireNonNull(value);
      V oldValue = this.get(key);
      V drv = this.defaultReturnValue();
      Object newValue;
      if (oldValue == drv && !this.containsKey(key)) {
         newValue = value;
      } else {
         V mergedValue = remappingFunction.apply(oldValue, value);
         if (mergedValue == null) {
            this.remove(key);
            return drv;
         }

         newValue = mergedValue;
      }

      this.put(key, newValue);
      return newValue;
   }

   public interface FastEntrySet<V> extends ObjectSet<Long2ObjectMap.Entry<V>> {
      ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator();

      default void fastForEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
         this.forEach(consumer);
      }
   }

   public interface Entry<V> extends java.util.Map.Entry<Long, V> {
      long getLongKey();

      /** @deprecated */
      @Deprecated
      default Long getKey() {
         return this.getLongKey();
      }
   }
}
