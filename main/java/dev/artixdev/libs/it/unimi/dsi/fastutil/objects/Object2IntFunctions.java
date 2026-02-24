package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class Object2IntFunctions {
   public static final Object2IntFunctions.EmptyFunction EMPTY_FUNCTION = new Object2IntFunctions.EmptyFunction();

   private Object2IntFunctions() {
   }

   public static <K> Object2IntFunction<K> singleton(K key, int value) {
      return new Object2IntFunctions.Singleton(key, value);
   }

   public static <K> Object2IntFunction<K> singleton(K key, Integer value) {
      return new Object2IntFunctions.Singleton(key, value);
   }

   public static <K> Object2IntFunction<K> synchronize(Object2IntFunction<K> f) {
      return new Object2IntFunctions.SynchronizedFunction(f);
   }

   public static <K> Object2IntFunction<K> synchronize(Object2IntFunction<K> f, Object sync) {
      return new Object2IntFunctions.SynchronizedFunction(f, sync);
   }

   public static <K> Object2IntFunction<K> unmodifiable(Object2IntFunction<? extends K> f) {
      return new Object2IntFunctions.UnmodifiableFunction(f);
   }

   public static <K> Object2IntFunction<K> primitive(Function<? super K, ? extends Integer> f) {
      Objects.requireNonNull(f);
      if (f instanceof Object2IntFunction) {
         return (Object2IntFunction<K>)f;
      } else {
         return (Object2IntFunction<K>)(f instanceof ToIntFunction ? (Object2IntFunction<K>)(key) -> {
            return ((ToIntFunction)f).applyAsInt(key);
         } : new Object2IntFunctions.PrimitiveFunction(f));
      }
   }

   public static class Singleton<K> extends AbstractObject2IntFunction<K> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final K key;
      protected final int value;

      protected Singleton(K key, int value) {
         this.key = key;
         this.value = value;
      }

      public boolean containsKey(Object k) {
         return Objects.equals(this.key, k);
      }

      public int getInt(Object k) {
         return Objects.equals(this.key, k) ? this.value : this.defRetValue;
      }

      public int getOrDefault(Object k, int defaultValue) {
         return Objects.equals(this.key, k) ? this.value : defaultValue;
      }

      public int size() {
         return 1;
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedFunction<K> implements Serializable, Object2IntFunction<K> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2IntFunction<K> function;
      protected final Object sync;

      protected SynchronizedFunction(Object2IntFunction<K> f, Object sync) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = sync;
         }
      }

      protected SynchronizedFunction(Object2IntFunction<K> f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
            this.sync = this;
         }
      }

      public int applyAsInt(K operand) {
         synchronized(this.sync) {
            return this.function.applyAsInt(operand);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer apply(K key) {
         synchronized(this.sync) {
            return this.function.get(key);
         }
      }

      public int size() {
         throw new UnsupportedOperationException();
      }

      public int defaultReturnValue() {
         synchronized(this.sync) {
            return this.function.defaultReturnValue();
         }
      }

      public void defaultReturnValue(int defRetValue) {
         synchronized(this.sync) {
            this.function.defaultReturnValue(defRetValue);
         }
      }

      public boolean containsKey(Object k) {
         synchronized(this.sync) {
            return this.function.containsKey(k);
         }
      }

      public int put(K k, int v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      public int getInt(Object k) {
         synchronized(this.sync) {
            return this.function.getInt(k);
         }
      }

      public int getOrDefault(Object k, int defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      public int removeInt(Object k) {
         synchronized(this.sync) {
            return this.function.removeInt(k);
         }
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer put(K k, Integer v) {
         synchronized(this.sync) {
            return this.function.put(k, v);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer get(Object k) {
         synchronized(this.sync) {
            return this.function.get(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object k, Integer defaultValue) {
         synchronized(this.sync) {
            return this.function.getOrDefault(k, defaultValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer remove(Object k) {
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

   public static class UnmodifiableFunction<K> extends AbstractObject2IntFunction<K> implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2IntFunction<? extends K> function;

      protected UnmodifiableFunction(Object2IntFunction<? extends K> f) {
         if (f == null) {
            throw new NullPointerException();
         } else {
            this.function = f;
         }
      }

      public int size() {
         throw new UnsupportedOperationException();
      }

      public int defaultReturnValue() {
         return this.function.defaultReturnValue();
      }

      public void defaultReturnValue(int defRetValue) {
         throw new UnsupportedOperationException();
      }

      public boolean containsKey(Object k) {
         return this.function.containsKey(k);
      }

      public int put(K k, int v) {
         throw new UnsupportedOperationException();
      }

      public int getInt(Object k) {
         return this.function.getInt(k);
      }

      public int getOrDefault(Object k, int defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      public int removeInt(Object k) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer put(K k, Integer v) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer get(Object k) {
         return this.function.get(k);
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object k, Integer defaultValue) {
         return this.function.getOrDefault(k, defaultValue);
      }

      /** @deprecated */
      @Deprecated
      public Integer remove(Object k) {
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

   @SuppressWarnings("unchecked")
   public static class PrimitiveFunction<K> implements Object2IntFunction<K> {
      protected final Function<? super K, ? extends Integer> function;

      protected PrimitiveFunction(Function<? super K, ? extends Integer> function) {
         this.function = function;
      }

      public boolean containsKey(Object key) {
         return this.function.apply((K)key) != null;
      }

      public int getInt(Object key) {
         Integer v = (Integer)this.function.apply((K)key);
         return v == null ? this.defaultReturnValue() : v;
      }

      public int getOrDefault(Object key, int defaultValue) {
         Integer v = (Integer)this.function.apply((K)key);
         return v == null ? defaultValue : v;
      }

      /** @deprecated */
      @Deprecated
      public Integer get(Object key) {
         return (Integer)this.function.apply((K)key);
      }

      /** @deprecated */
      @Deprecated
      public Integer getOrDefault(Object key, Integer defaultValue) {
         Integer v;
         return (v = (Integer)this.function.apply((K)key)) == null ? defaultValue : v;
      }

      /** @deprecated */
      @Deprecated
      public Integer put(K key, Integer value) {
         throw new UnsupportedOperationException();
      }
   }

   public static class EmptyFunction<K> extends AbstractObject2IntFunction<K> implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyFunction() {
      }

      public int getInt(Object k) {
         return 0;
      }

      public int getOrDefault(Object k, int defaultValue) {
         return defaultValue;
      }

      public boolean containsKey(Object k) {
         return false;
      }

      public int defaultReturnValue() {
         return 0;
      }

      public void defaultReturnValue(int defRetValue) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return 0;
      }

      public void clear() {
      }

      public Object clone() {
         return Object2IntFunctions.EMPTY_FUNCTION;
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         return o == this || o instanceof EmptyFunction;
      }

      public String toString() {
         return "{}";
      }

      private Object readResolve() {
         return Object2IntFunctions.EMPTY_FUNCTION;
      }
   }
}
