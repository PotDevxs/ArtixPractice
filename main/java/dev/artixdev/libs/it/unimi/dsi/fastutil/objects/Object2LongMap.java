package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongBinaryOperator;
import java.util.function.ToLongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongCollection;

public interface Object2LongMap<K> extends Map<K, Long>, Object2LongFunction<K> {
   int size();

   default void clear() {
      throw new UnsupportedOperationException();
   }

   void defaultReturnValue(long var1);

   long defaultReturnValue();

   ObjectSet<Object2LongMap.Entry<K>> object2LongEntrySet();

   /** @deprecated */
   @Deprecated
   @SuppressWarnings("unchecked")
   default ObjectSet<java.util.Map.Entry<K, Long>> entrySet() {
      return (ObjectSet<java.util.Map.Entry<K, Long>>)(ObjectSet<?>)this.object2LongEntrySet();
   }

   /** @deprecated */
   @Deprecated
   default Long put(K key, Long value) {
      return Object2LongFunction.super.put(key, value);
   }

   /** @deprecated */
   @Deprecated
   default Long get(Object key) {
      return Object2LongFunction.super.get(key);
   }

   /** @deprecated */
   @Deprecated
   default Long remove(Object key) {
      return Object2LongFunction.super.remove(key);
   }

   ObjectSet<K> keySet();

   LongCollection values();

   boolean containsKey(Object var1);

   boolean containsValue(long var1);

   /** @deprecated */
   @Deprecated
   default boolean containsValue(Object value) {
      return value == null ? false : this.containsValue((Long)value);
   }

   default void forEach(BiConsumer<? super K, ? super Long> consumer) {
      ObjectSet<Object2LongMap.Entry<K>> entrySet = this.object2LongEntrySet();
      Consumer<Object2LongMap.Entry<K>> wrappingConsumer = (entry) -> {
         consumer.accept(entry.getKey(), entry.getLongValue());
      };
      if (entrySet instanceof Object2LongMap.FastEntrySet) {
         ((Object2LongMap.FastEntrySet)entrySet).fastForEach(wrappingConsumer);
      } else {
         entrySet.forEach(wrappingConsumer);
      }

   }

   default long getOrDefault(Object key, long defaultValue) {
      long v;
      return (v = this.getLong(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Long getOrDefault(Object key, Long defaultValue) {
      return Map.super.getOrDefault(key, defaultValue);
   }

   default long putIfAbsent(K key, long value) {
      long v = this.getLong(key);
      long drv = this.defaultReturnValue();
      if (v == drv && !this.containsKey(key)) {
         this.put(key, value);
         return drv;
      } else {
         return v;
      }
   }

   default boolean remove(Object key, long value) {
      long curValue = this.getLong(key);
      if (curValue == value && (curValue != this.defaultReturnValue() || this.containsKey(key))) {
         this.removeLong(key);
         return true;
      } else {
         return false;
      }
   }

   default boolean replace(K key, long oldValue, long newValue) {
      long curValue = this.getLong(key);
      if (curValue == oldValue && (curValue != this.defaultReturnValue() || this.containsKey(key))) {
         this.put(key, newValue);
         return true;
      } else {
         return false;
      }
   }

   default long replace(K key, long value) {
      return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
   }

   default long computeIfAbsent(K key, ToLongFunction<? super K> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      long v = this.getLong(key);
      if (v == this.defaultReturnValue() && !this.containsKey(key)) {
         long newValue = mappingFunction.applyAsLong(key);
         this.put(key, newValue);
         return newValue;
      } else {
         return v;
      }
   }

   /** @deprecated */
   @Deprecated
   default long computeLongIfAbsent(K key, ToLongFunction<? super K> mappingFunction) {
      return this.computeIfAbsent(key, mappingFunction);
   }

   default long computeIfAbsent(K key, Object2LongFunction<? super K> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      long v = this.getLong(key);
      long drv = this.defaultReturnValue();
      if (v == drv && !this.containsKey(key)) {
         if (!mappingFunction.containsKey(key)) {
            return drv;
         } else {
            long newValue = mappingFunction.getLong(key);
            this.put(key, newValue);
            return newValue;
         }
      } else {
         return v;
      }
   }

   /** @deprecated */
   @Deprecated
   default long computeLongIfAbsentPartial(K key, Object2LongFunction<? super K> mappingFunction) {
      return this.computeIfAbsent(key, mappingFunction);
   }

   default long computeLongIfPresent(K key, BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      long oldValue = this.getLong(key);
      long drv = this.defaultReturnValue();
      if (oldValue == drv && !this.containsKey(key)) {
         return drv;
      } else {
         Long newValue = (Long)remappingFunction.apply(key, oldValue);
         if (newValue == null) {
            this.removeLong(key);
            return drv;
         } else {
            long newVal = newValue;
            this.put(key, newVal);
            return newVal;
         }
      }
   }

   default long computeLong(K key, BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      long oldValue = this.getLong(key);
      long drv = this.defaultReturnValue();
      boolean contained = oldValue != drv || this.containsKey(key);
      Long newValue = (Long)remappingFunction.apply(key, contained ? oldValue : null);
      if (newValue == null) {
         if (contained) {
            this.removeLong(key);
         }

         return drv;
      } else {
         long newVal = newValue;
         this.put(key, newVal);
         return newVal;
      }
   }

   default long merge(K key, long value, BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      long oldValue = this.getLong(key);
      long drv = this.defaultReturnValue();
      long newValue;
      if (oldValue == drv && !this.containsKey(key)) {
         newValue = value;
      } else {
         Long mergedValue = (Long)remappingFunction.apply(oldValue, value);
         if (mergedValue == null) {
            this.removeLong(key);
            return drv;
         }

         newValue = mergedValue;
      }

      this.put(key, newValue);
      return newValue;
   }

   default long mergeLong(K key, long value, LongBinaryOperator remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      long oldValue = this.getLong(key);
      long drv = this.defaultReturnValue();
      long newValue = oldValue == drv && !this.containsKey(key) ? value : remappingFunction.applyAsLong(oldValue, value);
      this.put(key, newValue);
      return newValue;
   }

   default long mergeLong(K key, long value, dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongBinaryOperator remappingFunction) {
      return this.mergeLong(key, value, (LongBinaryOperator)remappingFunction);
   }

   /** @deprecated */
   @Deprecated
   default long mergeLong(K key, long value, BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
      return this.merge(key, value, remappingFunction);
   }

   /** @deprecated */
   @Deprecated
   default Long putIfAbsent(K key, Long value) {
      return Map.super.putIfAbsent(key, value);
   }

   /** @deprecated */
   @Deprecated
   default boolean remove(Object key, Object value) {
      return Map.super.remove(key, value);
   }

   /** @deprecated */
   @Deprecated
   default boolean replace(K key, Long oldValue, Long newValue) {
      return Map.super.replace(key, oldValue, newValue);
   }

   /** @deprecated */
   @Deprecated
   default Long replace(K key, Long value) {
      return Map.super.replace(key, value);
   }

   /** @deprecated */
   @Deprecated
   default Long merge(K key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
      return Map.super.merge(key, value, remappingFunction);
   }

   public interface FastEntrySet<K> extends ObjectSet<Object2LongMap.Entry<K>> {
      ObjectIterator<Object2LongMap.Entry<K>> fastIterator();

      default void fastForEach(Consumer<? super Object2LongMap.Entry<K>> consumer) {
         this.forEach(consumer);
      }
   }

   public interface Entry<K> extends java.util.Map.Entry<K, Long> {
      long getLongValue();

      long setValue(long var1);

      /** @deprecated */
      @Deprecated
      default Long getValue() {
         return this.getLongValue();
      }

      /** @deprecated */
      @Deprecated
      default Long setValue(Long value) {
         return this.setValue(value);
      }
   }
}
