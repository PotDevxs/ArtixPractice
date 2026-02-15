package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;

public final class Object2ObjectFunctions {
   public static final Object2ObjectFunctions.EmptyFunction EMPTY_FUNCTION = new Object2ObjectFunctions.EmptyFunction();

   private Object2ObjectFunctions() {
   }

   public static <K, V> Object2ObjectFunction<K, V> singleton(K key, V value) {
      return new Object2ObjectFunctions.Singleton(key, value);
   }

   public static <K, V> Object2ObjectFunction<K, V> synchronize(Object2ObjectFunction<K, V> f) {
      return new Object2ObjectFunctions.SynchronizedFunction(f);
   }

   public static <K, V> Object2ObjectFunction<K, V> synchronize(Object2ObjectFunction<K, V> f, Object sync) {
      return new Object2ObjectFunctions.SynchronizedFunction(f, sync);
   }

   public static <K, V> Object2ObjectFunction<K, V> unmodifiable(Object2ObjectFunction<? extends K, ? extends V> f) {
      return new Object2ObjectFunctions.UnmodifiableFunction(f);
   }

   public static class Singleton<K, V> extends AbstractObject2ObjectFunction<K, V> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final K key;
      protected final V value;

      protected Singleton(K key, V value) {
         this.key = key;
         this.value = value;
      }

      public boolean containsKey(Object k) {
         return Objects.equals(this.key, k);
      }

      public V get(Object k) {
         return Objects.equals(this.key, k) ? this.value : this.defRetValue;
      }

      public V getOrDefault(Object k, V defaultValue) {
         return Objects.equals(this.key, k) ? this.value : defaultValue;
      }

      public int size() {
         return 1;
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedFunction<K, V> implements Serializable, Object2ObjectFunction<K, V> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2ObjectFunction<K, V> function;
      protected final Object sync;

      protected SynchronizedFunction(Object2ObjectFunction<K, V> f, Object sync) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = sync;
         }
      }

      protected SynchronizedFunction(Object2ObjectFunction<K, V> f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = this;
         }
      }

      public V apply(K key) {
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

      public boolean containsKey(Object k) {
         synchronized(this.sync) {
            return this.function.containsKey(k);
         }
      }

      public V put(K k, V v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      public V get(Object k) {
         synchronized(this.sync) {
            return this.function.get(k);
         }
      }

      public V getOrDefault(Object k, V defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      public V remove(Object k) {
         synchronized(this.sync) {
            return this.function.remove(k);
         }
      }

      public void clear() {
         synchronized(this.sync) {
            this.function.clear();
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

   public static class UnmodifiableFunction<K, V> extends AbstractObject2ObjectFunction<K, V> implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2ObjectFunction<? extends K, ? extends V> function;

      protected UnmodifiableFunction(Object2ObjectFunction<? extends K, ? extends V> f) {
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

      public boolean containsKey(Object k) {
         return this.function.containsKey(k);
      }

      public V put(K k, V v) {
         throw new UnsupportedOperationException();
      }

      public V get(Object k) {
         return this.function.get(k);
      }

      public V getOrDefault(Object k, V defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      public V remove(Object k) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
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

   public static class EmptyFunction<K, V> extends AbstractObject2ObjectFunction<K, V> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyFunction() {
      }

      public V get(Object k) {
         return null;
      }

      public V getOrDefault(Object k, V defaultValue) {
         return defaultValue;
      }

      public boolean containsKey(Object k) {
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
         return Object2ObjectFunctions.EMPTY_FUNCTION;
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         if (!(o instanceof Function)) {
            return false;
         } else {
            return ((Function)o).size() == 0;
         }
      }

      public String toString() {
         return "{}";
      }

      private Object readResolve() {
         return Object2ObjectFunctions.EMPTY_FUNCTION;
      }
   }
}
