package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;

public final class Long2ObjectFunctions {
   public static final Long2ObjectFunctions.EmptyFunction EMPTY_FUNCTION = new Long2ObjectFunctions.EmptyFunction();

   private Long2ObjectFunctions() {
   }

   public static <V> Long2ObjectFunction<V> singleton(long key, V value) {
      return new Long2ObjectFunctions.Singleton(key, value);
   }

   public static <V> Long2ObjectFunction<V> singleton(Long key, V value) {
      return new Long2ObjectFunctions.Singleton(key, value);
   }

   public static <V> Long2ObjectFunction<V> synchronize(Long2ObjectFunction<V> f) {
      return new Long2ObjectFunctions.SynchronizedFunction(f);
   }

   public static <V> Long2ObjectFunction<V> synchronize(Long2ObjectFunction<V> f, Object sync) {
      return new Long2ObjectFunctions.SynchronizedFunction(f, sync);
   }

   public static <V> Long2ObjectFunction<V> unmodifiable(Long2ObjectFunction<? extends V> f) {
      return new Long2ObjectFunctions.UnmodifiableFunction(f);
   }

   public static <V> Long2ObjectFunction<V> primitive(Function<? super Long, ? extends V> f) {
      Objects.requireNonNull(f);
      if (f instanceof Long2ObjectFunction) {
         return (Long2ObjectFunction)f;
      } else if (f instanceof LongFunction) {
         LongFunction var10000 = (LongFunction)f;
         Objects.requireNonNull((LongFunction)f);
         return var10000::apply;
      } else {
         return new Long2ObjectFunctions.PrimitiveFunction(f);
      }
   }

   public static class Singleton<V> extends AbstractLong2ObjectFunction<V> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final long key;
      protected final V value;

      protected Singleton(long key, V value) {
         this.key = key;
         this.value = value;
      }

      public boolean containsKey(long k) {
         return this.key == k;
      }

      public V get(long k) {
         return this.key == k ? this.value : this.defRetValue;
      }

      public V getOrDefault(long k, V defaultValue) {
         return this.key == k ? this.value : defaultValue;
      }

      public int size() {
         return 1;
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedFunction<V> implements Serializable, Long2ObjectFunction<V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Long2ObjectFunction<V> function;
      protected final Object sync;

      protected SynchronizedFunction(Long2ObjectFunction<V> f, Object sync) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = sync;
         }
      }

      protected SynchronizedFunction(Long2ObjectFunction<V> f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = this;
         }
      }

      public V apply(long operand) {
         synchronized(this.sync) {
            return this.function.apply(operand);
         }
      }

      /** @deprecated */
      @Deprecated
      public V apply(Long key) {
         synchronized(this.sync) {
            return this.function.apply(key);
         }
      }

      public int size() {
         synchronized(this.sync) {
            return this.function.size();
         }
      }

      public V defaultReturnValue() {
         synchronized(this.sync) {
            return this.function.defaultReturnValue();
         }
      }

      public void defaultReturnValue(V defRetValue) {
         synchronized(this.sync) {
            this.function.defaultReturnValue(defRetValue);
         }
      }

      public boolean containsKey(long k) {
         synchronized(this.sync) {
            return this.function.containsKey(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean containsKey(Object k) {
         synchronized(this.sync) {
            return this.function.containsKey(k);
         }
      }

      public V put(long k, V v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      public V get(long k) {
         synchronized(this.sync) {
            return this.function.get(k);
         }
      }

      public V getOrDefault(long k, V defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      public V remove(long k) {
         synchronized(this.sync) {
            return this.function.remove(k);
         }
      }

      public void clear() {
         synchronized(this.sync) {
            this.function.clear();
         }
      }

      /** @deprecated */
      @Deprecated
      public V put(Long k, V v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      /** @deprecated */
      @Deprecated
      public V get(Object k) {
         synchronized(this.sync) {
            return this.function.get(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public V getOrDefault(Object k, V defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public V remove(Object k) {
         synchronized(this.sync) {
            return this.function.remove(k);
         }
      }

      public int hashCode() {
         synchronized(this.sync) {
            return this.function.hashCode();
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.sync) {
               return this.function.equals(o);
            }
         }
      }

      public String toString() {
         synchronized(this.sync) {
            return this.function.toString();
         }
      }

      private void writeObject(ObjectOutputStream s) throws IOException {
         synchronized(this.sync) {
            s.defaultWriteObject();
         }
      }
   }

   public static class UnmodifiableFunction<V> extends AbstractLong2ObjectFunction<V> implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Long2ObjectFunction<? extends V> function;

      protected UnmodifiableFunction(Long2ObjectFunction<? extends V> f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
         }
      }

      public int size() {
         return this.function.size();
      }

      public V defaultReturnValue() {
         return this.function.defaultReturnValue();
      }

      public void defaultReturnValue(V defRetValue) {
         throw new UnsupportedOperationException();
      }

      public boolean containsKey(long k) {
         return this.function.containsKey(k);
      }

      public V put(long k, V v) {
         throw new UnsupportedOperationException();
      }

      public V get(long k) {
         return this.function.get(k);
      }

      public V getOrDefault(long k, V defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      public V remove(long k) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V put(Long k, V v) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public V get(Object k) {
         return this.function.get(k);
      }

      /** @deprecated */
      @Deprecated
      public V getOrDefault(Object k, V defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      /** @deprecated */
      @Deprecated
      public V remove(Object k) {
         throw new UnsupportedOperationException();
      }

      public int hashCode() {
         return this.function.hashCode();
      }

      public boolean equals(Object o) {
         return o == this || this.function.equals(o);
      }

      public String toString() {
         return this.function.toString();
      }
   }

   public static class PrimitiveFunction<V> implements Long2ObjectFunction<V> {
      protected final Function<? super Long, ? extends V> function;

      protected PrimitiveFunction(Function<? super Long, ? extends V> function) {
         this.function = function;
      }

      public boolean containsKey(long key) {
         return this.function.apply(key) != null;
      }

      /** @deprecated */
      @Deprecated
      public boolean containsKey(Object key) {
         if (key == null) {
            return false;
         } else {
            return this.function.apply((Long)key) != null;
         }
      }

      public V get(long key) {
         V v = this.function.apply(key);
         return v == null ? null : v;
      }

      public V getOrDefault(long key, V defaultValue) {
         V v = this.function.apply(key);
         return v == null ? defaultValue : v;
      }

      /** @deprecated */
      @Deprecated
      public V get(Object key) {
         return key == null ? null : this.function.apply((Long)key);
      }

      /** @deprecated */
      @Deprecated
      public V getOrDefault(Object key, V defaultValue) {
         if (key == null) {
            return defaultValue;
         } else {
            Object v;
            return (v = this.function.apply((Long)key)) == null ? defaultValue : v;
         }
      }

      /** @deprecated */
      @Deprecated
      public V put(Long key, V value) {
         throw new UnsupportedOperationException();
      }
   }

   public static class EmptyFunction<V> extends AbstractLong2ObjectFunction<V> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyFunction() {
      }

      public V get(long k) {
         return null;
      }

      public V getOrDefault(long k, V defaultValue) {
         return defaultValue;
      }

      public boolean containsKey(long k) {
         return false;
      }

      public V defaultReturnValue() {
         return null;
      }

      public void defaultReturnValue(V defRetValue) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return 0;
      }

      public void clear() {
      }

      public Object clone() {
         return Long2ObjectFunctions.EMPTY_FUNCTION;
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         if (!(o instanceof dev.artixdev.libs.it.unimi.dsi.fastutil.Function)) {
            return false;
         } else {
            return ((dev.artixdev.libs.it.unimi.dsi.fastutil.Function)o).size() == 0;
         }
      }

      public String toString() {
         return "{}";
      }

      private Object readResolve() {
         return Long2ObjectFunctions.EMPTY_FUNCTION;
      }
   }
}
