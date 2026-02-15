package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Hash;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectCollection;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectSpliterator;

public class Long2ObjectOpenHashMap<V> extends AbstractLong2ObjectMap<V> implements Serializable, Cloneable, Hash {
   private static final long serialVersionUID = 0L;
   private static final boolean ASSERTS = false;
   protected transient long[] key;
   protected transient V[] value;
   protected transient int mask;
   protected transient boolean containsNullKey;
   protected transient int n;
   protected transient int maxFill;
   protected final transient int minN;
   protected int size;
   protected final float f;
   protected transient Long2ObjectMap.FastEntrySet<V> entries;
   protected transient LongSet keys;
   protected transient ObjectCollection<V> values;

   public Long2ObjectOpenHashMap(int expected, float f) {
      if (!(f <= 0.0F) && !(f >= 1.0F)) {
         if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
         } else {
            this.f = f;
            this.minN = this.n = HashCommon.arraySize(expected, f);
            this.mask = this.n - 1;
            this.maxFill = HashCommon.maxFill(this.n, f);
            this.key = new long[this.n + 1];
            this.value = new Object[this.n + 1];
         }
      } else {
         throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
      }
   }

   public Long2ObjectOpenHashMap(int expected) {
      this(expected, 0.75F);
   }

   public Long2ObjectOpenHashMap() {
      this(16, 0.75F);
   }

   public Long2ObjectOpenHashMap(Map<? extends Long, ? extends V> m, float f) {
      this(m.size(), f);
      this.putAll(m);
   }

   public Long2ObjectOpenHashMap(Map<? extends Long, ? extends V> m) {
      this(m, 0.75F);
   }

   public Long2ObjectOpenHashMap(Long2ObjectMap<V> m, float f) {
      this(m.size(), f);
      this.putAll(m);
   }

   public Long2ObjectOpenHashMap(Long2ObjectMap<V> m) {
      this(m, 0.75F);
   }

   public Long2ObjectOpenHashMap(long[] k, V[] v, float f) {
      this(k.length, f);
      if (k.length != v.length) {
         throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
      } else {
         for(int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
         }

      }
   }

   public Long2ObjectOpenHashMap(long[] k, V[] v) {
      this(k, v, 0.75F);
   }

   private int realSize() {
      return this.containsNullKey ? this.size - 1 : this.size;
   }

   public void ensureCapacity(int capacity) {
      int needed = HashCommon.arraySize(capacity, this.f);
      if (needed > this.n) {
         this.rehash(needed);
      }

   }

   private void tryCapacity(long capacity) {
      int needed = (int)Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((double)((float)capacity / this.f)))));
      if (needed > this.n) {
         this.rehash(needed);
      }

   }

   private V removeEntry(int pos) {
      V oldValue = this.value[pos];
      this.value[pos] = null;
      --this.size;
      this.shiftKeys(pos);
      if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
         this.rehash(this.n / 2);
      }

      return oldValue;
   }

   private V removeNullEntry() {
      this.containsNullKey = false;
      V oldValue = this.value[this.n];
      this.value[this.n] = null;
      --this.size;
      if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
         this.rehash(this.n / 2);
      }

      return oldValue;
   }

   public void putAll(Map<? extends Long, ? extends V> m) {
      if ((double)this.f <= 0.5D) {
         this.ensureCapacity(m.size());
      } else {
         this.tryCapacity((long)(this.size() + m.size()));
      }

      super.putAll(m);
   }

   private int find(long k) {
      if (k == 0L) {
         return this.containsNullKey ? this.n : -(this.n + 1);
      } else {
         long[] key = this.key;
         long curr;
         int pos;
         if ((curr = key[pos = (int)HashCommon.mix(k) & this.mask]) == 0L) {
            return -(pos + 1);
         } else if (k == curr) {
            return pos;
         } else {
            while((curr = key[pos = pos + 1 & this.mask]) != 0L) {
               if (k == curr) {
                  return pos;
               }
            }

            return -(pos + 1);
         }
      }
   }

   private void insert(int pos, long k, V v) {
      if (pos == this.n) {
         this.containsNullKey = true;
      }

      this.key[pos] = k;
      this.value[pos] = v;
      if (this.size++ >= this.maxFill) {
         this.rehash(HashCommon.arraySize(this.size + 1, this.f));
      }

   }

   public V put(long k, V v) {
      int pos = this.find(k);
      if (pos < 0) {
         this.insert(-pos - 1, k, v);
         return this.defRetValue;
      } else {
         V oldValue = this.value[pos];
         this.value[pos] = v;
         return oldValue;
      }
   }

   protected final void shiftKeys(int pos) {
      long[] key = this.key;

      while(true) {
         int last = pos;
         pos = pos + 1 & this.mask;

         long curr;
         while(true) {
            if ((curr = key[pos]) == 0L) {
               key[last] = 0L;
               this.value[last] = null;
               return;
            }

            int slot = (int)HashCommon.mix(curr) & this.mask;
            if (last <= pos) {
               if (last >= slot || slot > pos) {
                  break;
               }
            } else if (last >= slot && slot > pos) {
               break;
            }

            pos = pos + 1 & this.mask;
         }

         key[last] = curr;
         this.value[last] = this.value[pos];
      }
   }

   public V remove(long k) {
      if (k == 0L) {
         return this.containsNullKey ? this.removeNullEntry() : this.defRetValue;
      } else {
         long[] key = this.key;
         long curr;
         int pos;
         if ((curr = key[pos = (int)HashCommon.mix(k) & this.mask]) == 0L) {
            return this.defRetValue;
         } else if (k == curr) {
            return this.removeEntry(pos);
         } else {
            while((curr = key[pos = pos + 1 & this.mask]) != 0L) {
               if (k == curr) {
                  return this.removeEntry(pos);
               }
            }

            return this.defRetValue;
         }
      }
   }

   public V get(long k) {
      if (k == 0L) {
         return this.containsNullKey ? this.value[this.n] : this.defRetValue;
      } else {
         long[] key = this.key;
         long curr;
         int pos;
         if ((curr = key[pos = (int)HashCommon.mix(k) & this.mask]) == 0L) {
            return this.defRetValue;
         } else if (k == curr) {
            return this.value[pos];
         } else {
            while((curr = key[pos = pos + 1 & this.mask]) != 0L) {
               if (k == curr) {
                  return this.value[pos];
               }
            }

            return this.defRetValue;
         }
      }
   }

   public boolean containsKey(long k) {
      if (k == 0L) {
         return this.containsNullKey;
      } else {
         long[] key = this.key;
         long curr;
         int pos;
         if ((curr = key[pos = (int)HashCommon.mix(k) & this.mask]) == 0L) {
            return false;
         } else if (k == curr) {
            return true;
         } else {
            while((curr = key[pos = pos + 1 & this.mask]) != 0L) {
               if (k == curr) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public boolean containsValue(Object v) {
      V[] value = this.value;
      long[] key = this.key;
      if (this.containsNullKey && Objects.equals(value[this.n], v)) {
         return true;
      } else {
         int i = this.n;

         do {
            if (i-- == 0) {
               return false;
            }
         } while(key[i] == 0L || !Objects.equals(value[i], v));

         return true;
      }
   }

   public V getOrDefault(long k, V defaultValue) {
      if (k == 0L) {
         return this.containsNullKey ? this.value[this.n] : defaultValue;
      } else {
         long[] key = this.key;
         long curr;
         int pos;
         if ((curr = key[pos = (int)HashCommon.mix(k) & this.mask]) == 0L) {
            return defaultValue;
         } else if (k == curr) {
            return this.value[pos];
         } else {
            while((curr = key[pos = pos + 1 & this.mask]) != 0L) {
               if (k == curr) {
                  return this.value[pos];
               }
            }

            return defaultValue;
         }
      }
   }

   public V putIfAbsent(long k, V v) {
      int pos = this.find(k);
      if (pos >= 0) {
         return this.value[pos];
      } else {
         this.insert(-pos - 1, k, v);
         return this.defRetValue;
      }
   }

   public boolean remove(long k, Object v) {
      if (k == 0L) {
         if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
            this.removeNullEntry();
            return true;
         } else {
            return false;
         }
      } else {
         long[] key = this.key;
         long curr;
         int pos;
         if ((curr = key[pos = (int)HashCommon.mix(k) & this.mask]) == 0L) {
            return false;
         } else if (k == curr && Objects.equals(v, this.value[pos])) {
            this.removeEntry(pos);
            return true;
         } else {
            do {
               if ((curr = key[pos = pos + 1 & this.mask]) == 0L) {
                  return false;
               }
            } while(k != curr || !Objects.equals(v, this.value[pos]));

            this.removeEntry(pos);
            return true;
         }
      }
   }

   public boolean replace(long k, V oldValue, V v) {
      int pos = this.find(k);
      if (pos >= 0 && Objects.equals(oldValue, this.value[pos])) {
         this.value[pos] = v;
         return true;
      } else {
         return false;
      }
   }

   public V replace(long k, V v) {
      int pos = this.find(k);
      if (pos < 0) {
         return this.defRetValue;
      } else {
         V oldValue = this.value[pos];
         this.value[pos] = v;
         return oldValue;
      }
   }

   public V computeIfAbsent(long k, LongFunction<? extends V> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      int pos = this.find(k);
      if (pos >= 0) {
         return this.value[pos];
      } else {
         V newValue = mappingFunction.apply(k);
         this.insert(-pos - 1, k, newValue);
         return newValue;
      }
   }

   public V computeIfAbsent(long key, Long2ObjectFunction<? extends V> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      int pos = this.find(key);
      if (pos >= 0) {
         return this.value[pos];
      } else if (!mappingFunction.containsKey(key)) {
         return this.defRetValue;
      } else {
         V newValue = mappingFunction.get(key);
         this.insert(-pos - 1, key, newValue);
         return newValue;
      }
   }

   public V computeIfPresent(long k, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      int pos = this.find(k);
      if (pos < 0) {
         return this.defRetValue;
      } else if (this.value[pos] == null) {
         return this.defRetValue;
      } else {
         V newValue = remappingFunction.apply(k, this.value[pos]);
         if (newValue == null) {
            if (k == 0L) {
               this.removeNullEntry();
            } else {
               this.removeEntry(pos);
            }

            return this.defRetValue;
         } else {
            return this.value[pos] = newValue;
         }
      }
   }

   public V compute(long k, BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      int pos = this.find(k);
      V newValue = remappingFunction.apply(k, pos >= 0 ? this.value[pos] : null);
      if (newValue == null) {
         if (pos >= 0) {
            if (k == 0L) {
               this.removeNullEntry();
            } else {
               this.removeEntry(pos);
            }
         }

         return this.defRetValue;
      } else if (pos < 0) {
         this.insert(-pos - 1, k, newValue);
         return newValue;
      } else {
         return this.value[pos] = newValue;
      }
   }

   public V merge(long k, V v, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      Objects.requireNonNull(v);
      int pos = this.find(k);
      if (pos >= 0 && this.value[pos] != null) {
         V newValue = remappingFunction.apply(this.value[pos], v);
         if (newValue == null) {
            if (k == 0L) {
               this.removeNullEntry();
            } else {
               this.removeEntry(pos);
            }

            return this.defRetValue;
         } else {
            return this.value[pos] = newValue;
         }
      } else {
         if (pos < 0) {
            this.insert(-pos - 1, k, v);
         } else {
            this.value[pos] = v;
         }

         return v;
      }
   }

   public void clear() {
      if (this.size != 0) {
         this.size = 0;
         this.containsNullKey = false;
         Arrays.fill(this.key, 0L);
         Arrays.fill(this.value, (Object)null);
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public Long2ObjectMap.FastEntrySet<V> long2ObjectEntrySet() {
      if (this.entries == null) {
         this.entries = new Long2ObjectOpenHashMap.MapEntrySet();
      }

      return this.entries;
   }

   public LongSet keySet() {
      if (this.keys == null) {
         this.keys = new Long2ObjectOpenHashMap.KeySet();
      }

      return this.keys;
   }

   public ObjectCollection<V> values() {
      if (this.values == null) {
         this.values = new AbstractObjectCollection<V>() {
            public ObjectIterator<V> iterator() {
               return Long2ObjectOpenHashMap.this.new ValueIterator();
            }

            public ObjectSpliterator<V> spliterator() {
               return Long2ObjectOpenHashMap.this.new ValueSpliterator();
            }

            public void forEach(Consumer<? super V> consumer) {
               if (Long2ObjectOpenHashMap.this.containsNullKey) {
                  consumer.accept(Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n]);
               }

               int pos = Long2ObjectOpenHashMap.this.n;

               while(pos-- != 0) {
                  if (Long2ObjectOpenHashMap.this.key[pos] != 0L) {
                     consumer.accept(Long2ObjectOpenHashMap.this.value[pos]);
                  }
               }

            }

            public int size() {
               return Long2ObjectOpenHashMap.this.size;
            }

            public boolean contains(Object v) {
               return Long2ObjectOpenHashMap.this.containsValue(v);
            }

            public void clear() {
               Long2ObjectOpenHashMap.this.clear();
            }
         };
      }

      return this.values;
   }

   public boolean trim() {
      return this.trim(this.size);
   }

   public boolean trim(int n) {
      int l = HashCommon.nextPowerOfTwo((int)Math.ceil((double)((float)n / this.f)));
      if (l < this.n && this.size <= HashCommon.maxFill(l, this.f)) {
         try {
            this.rehash(l);
            return true;
         } catch (OutOfMemoryError ignored) {
            return false;
         }
      } else {
         return true;
      }
   }

   protected void rehash(int newN) {
      long[] key = this.key;
      V[] value = this.value;
      int mask = newN - 1;
      long[] newKey = new long[newN + 1];
      V[] newValue = new Object[newN + 1];
      int i = this.n;

      int pos;
      for(int var9 = this.realSize(); var9-- != 0; newValue[pos] = value[i]) {
         do {
            --i;
         } while(key[i] == 0L);

         if (newKey[pos = (int)HashCommon.mix(key[i]) & mask] != 0L) {
            while(newKey[pos = pos + 1 & mask] != 0L) {
            }
         }

         newKey[pos] = key[i];
      }

      newValue[newN] = value[this.n];
      this.n = newN;
      this.mask = mask;
      this.maxFill = HashCommon.maxFill(this.n, this.f);
      this.key = newKey;
      this.value = newValue;
   }

   public Long2ObjectOpenHashMap<V> clone() {
      Long2ObjectOpenHashMap c;
      try {
         c = (Long2ObjectOpenHashMap)super.clone();
      } catch (CloneNotSupportedException e) {
         throw new InternalError(e);
      }

      c.keys = null;
      c.values = null;
      c.entries = null;
      c.containsNullKey = this.containsNullKey;
      c.key = (long[])this.key.clone();
      c.value = (Object[])this.value.clone();
      return c;
   }

   public int hashCode() {
      int h = 0;
      int j = this.realSize();
      int i = 0;

      for(boolean var4 = false; j-- != 0; ++i) {
         while(this.key[i] == 0L) {
            ++i;
         }

         int t = HashCommon.long2int(this.key[i]);
         if (this != this.value[i]) {
            t ^= this.value[i] == null ? 0 : this.value[i].hashCode();
         }

         h += t;
      }

      if (this.containsNullKey) {
         h += this.value[this.n] == null ? 0 : this.value[this.n].hashCode();
      }

      return h;
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      long[] key = this.key;
      V[] value = this.value;
      Long2ObjectOpenHashMap<V>.EntryIterator i = new Long2ObjectOpenHashMap.EntryIterator();
      s.defaultWriteObject();
      int var5 = this.size;

      while(var5-- != 0) {
         int e = i.nextEntry();
         s.writeLong(key[e]);
         s.writeObject(value[e]);
      }

   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.n = HashCommon.arraySize(this.size, this.f);
      this.maxFill = HashCommon.maxFill(this.n, this.f);
      this.mask = this.n - 1;
      long[] key = this.key = new long[this.n + 1];
      V[] value = this.value = new Object[this.n + 1];

      Object v;
      int pos;
      for(int var7 = this.size; var7-- != 0; value[pos] = v) {
         long k = s.readLong();
         v = s.readObject();
         if (k == 0L) {
            pos = this.n;
            this.containsNullKey = true;
         } else {
            for(pos = (int)HashCommon.mix(k) & this.mask; key[pos] != 0L; pos = pos + 1 & this.mask) {
            }
         }

         key[pos] = k;
      }

   }

   private void checkTable() {
   }

   private final class MapEntrySet extends AbstractObjectSet<Long2ObjectMap.Entry<V>> implements Long2ObjectMap.FastEntrySet<V> {
      private MapEntrySet() {
      }

      public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
         return Long2ObjectOpenHashMap.this.new EntryIterator();
      }

      public ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator() {
         return Long2ObjectOpenHashMap.this.new FastEntryIterator();
      }

      public ObjectSpliterator<Long2ObjectMap.Entry<V>> spliterator() {
         return Long2ObjectOpenHashMap.this.new EntrySpliterator();
      }

      public boolean contains(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            if (e.getKey() != null && e.getKey() instanceof Long) {
               long k = (Long)e.getKey();
               V v = e.getValue();
               if (k == 0L) {
                  return Long2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n], v);
               } else {
                  long[] key = Long2ObjectOpenHashMap.this.key;
                  long curr;
                  int pos;
                  if ((curr = key[pos = (int)HashCommon.mix(k) & Long2ObjectOpenHashMap.this.mask]) == 0L) {
                     return false;
                  } else if (k == curr) {
                     return Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v);
                  } else {
                     while((curr = key[pos = pos + 1 & Long2ObjectOpenHashMap.this.mask]) != 0L) {
                        if (k == curr) {
                           return Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v);
                        }
                     }

                     return false;
                  }
               }
            } else {
               return false;
            }
         }
      }

      public boolean remove(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<?, ?> e = (java.util.Map.Entry)o;
            if (e.getKey() != null && e.getKey() instanceof Long) {
               long k = (Long)e.getKey();
               V v = e.getValue();
               if (k == 0L) {
                  if (Long2ObjectOpenHashMap.this.containsNullKey && Objects.equals(Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n], v)) {
                     Long2ObjectOpenHashMap.this.removeNullEntry();
                     return true;
                  } else {
                     return false;
                  }
               } else {
                  long[] key = Long2ObjectOpenHashMap.this.key;
                  long curr;
                  int pos;
                  if ((curr = key[pos = (int)HashCommon.mix(k) & Long2ObjectOpenHashMap.this.mask]) == 0L) {
                     return false;
                  } else if (curr == k) {
                     if (Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v)) {
                        Long2ObjectOpenHashMap.this.removeEntry(pos);
                        return true;
                     } else {
                        return false;
                     }
                  } else {
                     do {
                        if ((curr = key[pos = pos + 1 & Long2ObjectOpenHashMap.this.mask]) == 0L) {
                           return false;
                        }
                     } while(curr != k || !Objects.equals(Long2ObjectOpenHashMap.this.value[pos], v));

                     Long2ObjectOpenHashMap.this.removeEntry(pos);
                     return true;
                  }
               }
            } else {
               return false;
            }
         }
      }

      public int size() {
         return Long2ObjectOpenHashMap.this.size;
      }

      public void clear() {
         Long2ObjectOpenHashMap.this.clear();
      }

      public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
         if (Long2ObjectOpenHashMap.this.containsNullKey) {
            consumer.accept(Long2ObjectOpenHashMap.this.new MapEntry(Long2ObjectOpenHashMap.this.n));
         }

         int pos = Long2ObjectOpenHashMap.this.n;

         while(pos-- != 0) {
            if (Long2ObjectOpenHashMap.this.key[pos] != 0L) {
               consumer.accept(Long2ObjectOpenHashMap.this.new MapEntry(pos));
            }
         }

      }

      public void fastForEach(Consumer<? super Long2ObjectMap.Entry<V>> consumer) {
         Long2ObjectOpenHashMap<V>.MapEntry entry = Long2ObjectOpenHashMap.this.new MapEntry();
         if (Long2ObjectOpenHashMap.this.containsNullKey) {
            entry.index = Long2ObjectOpenHashMap.this.n;
            consumer.accept(entry);
         }

         int pos = Long2ObjectOpenHashMap.this.n;

         while(pos-- != 0) {
            if (Long2ObjectOpenHashMap.this.key[pos] != 0L) {
               entry.index = pos;
               consumer.accept(entry);
            }
         }

      }

      // $FF: synthetic method
      MapEntrySet(Object x1) {
         this();
      }
   }

   private final class KeySet extends AbstractLongSet {
      private KeySet() {
      }

      public LongIterator iterator() {
         return Long2ObjectOpenHashMap.this.new KeyIterator();
      }

      public LongSpliterator spliterator() {
         return Long2ObjectOpenHashMap.this.new KeySpliterator();
      }

      public void forEach(java.util.function.LongConsumer consumer) {
         if (Long2ObjectOpenHashMap.this.containsNullKey) {
            consumer.accept(Long2ObjectOpenHashMap.this.key[Long2ObjectOpenHashMap.this.n]);
         }

         int pos = Long2ObjectOpenHashMap.this.n;

         while(pos-- != 0) {
            long k = Long2ObjectOpenHashMap.this.key[pos];
            if (k != 0L) {
               consumer.accept(k);
            }
         }

      }

      public int size() {
         return Long2ObjectOpenHashMap.this.size;
      }

      public boolean contains(long k) {
         return Long2ObjectOpenHashMap.this.containsKey(k);
      }

      public boolean remove(long k) {
         int oldSize = Long2ObjectOpenHashMap.this.size;
         Long2ObjectOpenHashMap.this.remove(k);
         return Long2ObjectOpenHashMap.this.size != oldSize;
      }

      public void clear() {
         Long2ObjectOpenHashMap.this.clear();
      }

      // $FF: synthetic method
      KeySet(Object x1) {
         this();
      }
   }

   private final class EntryIterator extends Long2ObjectOpenHashMap.MapIterator implements ObjectIterator {
      private Long2ObjectOpenHashMap<V>.MapEntry entry;

      private EntryIterator() {
         super(null);
      }

      public Long2ObjectOpenHashMap<V>.MapEntry next() {
         return this.entry = Long2ObjectOpenHashMap.this.new MapEntry(this.nextEntry());
      }

      final void acceptOnIndex(Consumer<? super Long2ObjectMap.Entry<V>> action, int index) {
         action.accept(this.entry = Long2ObjectOpenHashMap.this.new MapEntry(index));
      }

      public void remove() {
         super.remove();
         this.entry.index = -1;
      }

      // $FF: synthetic method
      EntryIterator(Object x1) {
         this();
      }
   }

   private final class ValueSpliterator extends Long2ObjectOpenHashMap.MapSpliterator implements ObjectSpliterator {
      private static final int POST_SPLIT_CHARACTERISTICS = 0;

      ValueSpliterator() {
         super();
      }

      ValueSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
         super(pos, max, mustReturnNull, hasSplit);
      }

      public int characteristics() {
         return this.hasSplit ? 0 : 64;
      }

      final void acceptOnIndex(Consumer<? super V> action, int index) {
         action.accept(Long2ObjectOpenHashMap.this.value[index]);
      }

      final Long2ObjectOpenHashMap<V>.ValueSpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
         return Long2ObjectOpenHashMap.this.new ValueSpliterator(pos, max, mustReturnNull, true);
      }
   }

   private final class ValueIterator extends Long2ObjectOpenHashMap.MapIterator implements ObjectIterator {
      public ValueIterator() {
         super(null);
      }

      final void acceptOnIndex(Consumer<? super V> action, int index) {
         action.accept(Long2ObjectOpenHashMap.this.value[index]);
      }

      public V next() {
         return Long2ObjectOpenHashMap.this.value[this.nextEntry()];
      }
   }

   private final class KeySpliterator extends Long2ObjectOpenHashMap.MapSpliterator implements LongSpliterator {
      private static final int POST_SPLIT_CHARACTERISTICS = 257;

      KeySpliterator() {
         super();
      }

      KeySpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
         super(pos, max, mustReturnNull, hasSplit);
      }

      public int characteristics() {
         return this.hasSplit ? 257 : 321;
      }

      final void acceptOnIndex(java.util.function.LongConsumer action, int index) {
         action.accept(Long2ObjectOpenHashMap.this.key[index]);
      }

      final Long2ObjectOpenHashMap<V>.KeySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
         return Long2ObjectOpenHashMap.this.new KeySpliterator(pos, max, mustReturnNull, true);
      }
   }

   private final class KeyIterator extends Long2ObjectOpenHashMap.MapIterator implements LongIterator {
      public KeyIterator() {
         super(null);
      }

      final void acceptOnIndex(java.util.function.LongConsumer action, int index) {
         action.accept(Long2ObjectOpenHashMap.this.key[index]);
      }

      public long nextLong() {
         return Long2ObjectOpenHashMap.this.key[this.nextEntry()];
      }
   }

   private final class EntrySpliterator extends Long2ObjectOpenHashMap.MapSpliterator implements ObjectSpliterator {
      private static final int POST_SPLIT_CHARACTERISTICS = 1;

      EntrySpliterator() {
         super();
      }

      EntrySpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
         super(pos, max, mustReturnNull, hasSplit);
      }

      public int characteristics() {
         return this.hasSplit ? 1 : 65;
      }

      final void acceptOnIndex(Consumer<? super Long2ObjectMap.Entry<V>> action, int index) {
         action.accept(Long2ObjectOpenHashMap.this.new MapEntry(index));
      }

      final Long2ObjectOpenHashMap<V>.EntrySpliterator makeForSplit(int pos, int max, boolean mustReturnNull) {
         return Long2ObjectOpenHashMap.this.new EntrySpliterator(pos, max, mustReturnNull, true);
      }
   }

   private abstract class MapSpliterator<ConsumerType, SplitType extends Long2ObjectOpenHashMap<V>.MapSpliterator<ConsumerType, SplitType>> {
      int pos = 0;
      int max;
      int c;
      boolean mustReturnNull;
      boolean hasSplit;

      MapSpliterator() {
         this.max = Long2ObjectOpenHashMap.this.n;
         this.c = 0;
         this.mustReturnNull = Long2ObjectOpenHashMap.this.containsNullKey;
         this.hasSplit = false;
      }

      MapSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
         this.max = Long2ObjectOpenHashMap.this.n;
         this.c = 0;
         this.mustReturnNull = Long2ObjectOpenHashMap.this.containsNullKey;
         this.hasSplit = false;
         this.pos = pos;
         this.max = max;
         this.mustReturnNull = mustReturnNull;
         this.hasSplit = hasSplit;
      }

      abstract void acceptOnIndex(ConsumerType var1, int var2);

      abstract SplitType makeForSplit(int var1, int var2, boolean var3);

      public boolean tryAdvance(ConsumerType action) {
         if (this.mustReturnNull) {
            this.mustReturnNull = false;
            ++this.c;
            this.acceptOnIndex(action, Long2ObjectOpenHashMap.this.n);
            return true;
         } else {
            for(long[] key = Long2ObjectOpenHashMap.this.key; this.pos < this.max; ++this.pos) {
               if (key[this.pos] != 0L) {
                  ++this.c;
                  this.acceptOnIndex(action, this.pos++);
                  return true;
               }
            }

            return false;
         }
      }

      public void forEachRemaining(ConsumerType action) {
         if (this.mustReturnNull) {
            this.mustReturnNull = false;
            ++this.c;
            this.acceptOnIndex(action, Long2ObjectOpenHashMap.this.n);
         }

         for(long[] key = Long2ObjectOpenHashMap.this.key; this.pos < this.max; ++this.pos) {
            if (key[this.pos] != 0L) {
               this.acceptOnIndex(action, this.pos);
               ++this.c;
            }
         }

      }

      public long estimateSize() {
         return !this.hasSplit ? (long)(Long2ObjectOpenHashMap.this.size - this.c) : Math.min((long)(Long2ObjectOpenHashMap.this.size - this.c), (long)((double)Long2ObjectOpenHashMap.this.realSize() / (double)Long2ObjectOpenHashMap.this.n * (double)(this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
      }

      public SplitType trySplit() {
         if (this.pos >= this.max - 1) {
            return null;
         } else {
            int retLen = this.max - this.pos >> 1;
            if (retLen <= 1) {
               return null;
            } else {
               int myNewPos = this.pos + retLen;
               int retPos = this.pos;
               SplitType split = this.makeForSplit(retPos, myNewPos, this.mustReturnNull);
               this.pos = myNewPos;
               this.mustReturnNull = false;
               this.hasSplit = true;
               return split;
            }
         }
      }

      public long skip(long n) {
         if (n < 0L) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n == 0L) {
            return 0L;
         } else {
            long skipped = 0L;
            if (this.mustReturnNull) {
               this.mustReturnNull = false;
               ++skipped;
               --n;
            }

            long[] key = Long2ObjectOpenHashMap.this.key;

            while(this.pos < this.max && n > 0L) {
               if (key[this.pos++] != 0L) {
                  ++skipped;
                  --n;
               }
            }

            return skipped;
         }
      }
   }

   private final class FastEntryIterator extends Long2ObjectOpenHashMap.MapIterator implements ObjectIterator {
      private final Long2ObjectOpenHashMap<V>.MapEntry entry;

      private FastEntryIterator() {
         super(null);
         this.entry = Long2ObjectOpenHashMap.this.new MapEntry();
      }

      public Long2ObjectOpenHashMap<V>.MapEntry next() {
         this.entry.index = this.nextEntry();
         return this.entry;
      }

      final void acceptOnIndex(Consumer<? super Long2ObjectMap.Entry<V>> action, int index) {
         this.entry.index = index;
         action.accept(this.entry);
      }

      // $FF: synthetic method
      FastEntryIterator(Object x1) {
         this();
      }
   }

   private abstract class MapIterator<ConsumerType> {
      int pos;
      int last;
      int c;
      boolean mustReturnNullKey;
      LongArrayList wrapped;

      private MapIterator() {
         this.pos = Long2ObjectOpenHashMap.this.n;
         this.last = -1;
         this.c = Long2ObjectOpenHashMap.this.size;
         this.mustReturnNullKey = Long2ObjectOpenHashMap.this.containsNullKey;
      }

      abstract void acceptOnIndex(ConsumerType var1, int var2);

      public boolean hasNext() {
         return this.c != 0;
      }

      public int nextEntry() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            --this.c;
            if (this.mustReturnNullKey) {
               this.mustReturnNullKey = false;
               return this.last = Long2ObjectOpenHashMap.this.n;
            } else {
               long[] key = Long2ObjectOpenHashMap.this.key;

               while(--this.pos >= 0) {
                  if (key[this.pos] != 0L) {
                     return this.last = this.pos;
                  }
               }

               this.last = Integer.MIN_VALUE;
               long k = this.wrapped.getLong(-this.pos - 1);

               int p;
               for(p = (int)HashCommon.mix(k) & Long2ObjectOpenHashMap.this.mask; k != key[p]; p = p + 1 & Long2ObjectOpenHashMap.this.mask) {
               }

               return p;
            }
         }
      }

      public void forEachRemaining(ConsumerType action) {
         if (this.mustReturnNullKey) {
            this.mustReturnNullKey = false;
            this.acceptOnIndex(action, this.last = Long2ObjectOpenHashMap.this.n);
            --this.c;
         }

         long[] key = Long2ObjectOpenHashMap.this.key;

         while(true) {
            while(this.c != 0) {
               if (--this.pos < 0) {
                  this.last = Integer.MIN_VALUE;
                  long k = this.wrapped.getLong(-this.pos - 1);

                  int p;
                  for(p = (int)HashCommon.mix(k) & Long2ObjectOpenHashMap.this.mask; k != key[p]; p = p + 1 & Long2ObjectOpenHashMap.this.mask) {
                  }

                  this.acceptOnIndex(action, p);
                  --this.c;
               } else if (key[this.pos] != 0L) {
                  this.acceptOnIndex(action, this.last = this.pos);
                  --this.c;
               }
            }

            return;
         }
      }

      private void shiftKeys(int pos) {
         long[] key = Long2ObjectOpenHashMap.this.key;

         while(true) {
            int last = pos;
            pos = pos + 1 & Long2ObjectOpenHashMap.this.mask;

            long curr;
            while(true) {
               if ((curr = key[pos]) == 0L) {
                  key[last] = 0L;
                  Long2ObjectOpenHashMap.this.value[last] = null;
                  return;
               }

               int slot = (int)HashCommon.mix(curr) & Long2ObjectOpenHashMap.this.mask;
               if (last <= pos) {
                  if (last >= slot || slot > pos) {
                     break;
                  }
               } else if (last >= slot && slot > pos) {
                  break;
               }

               pos = pos + 1 & Long2ObjectOpenHashMap.this.mask;
            }

            if (pos < last) {
               if (this.wrapped == null) {
                  this.wrapped = new LongArrayList(2);
               }

               this.wrapped.add(key[pos]);
            }

            key[last] = curr;
            Long2ObjectOpenHashMap.this.value[last] = Long2ObjectOpenHashMap.this.value[pos];
         }
      }

      public void remove() {
         if (this.last == -1) {
            throw new IllegalStateException();
         } else {
            if (this.last == Long2ObjectOpenHashMap.this.n) {
               Long2ObjectOpenHashMap.this.containsNullKey = false;
               Long2ObjectOpenHashMap.this.value[Long2ObjectOpenHashMap.this.n] = null;
            } else {
               if (this.pos < 0) {
                  Long2ObjectOpenHashMap.this.remove(this.wrapped.getLong(-this.pos - 1));
                  this.last = -1;
                  return;
               }

               this.shiftKeys(this.last);
            }

            --Long2ObjectOpenHashMap.this.size;
            this.last = -1;
         }
      }

      public int skip(int n) {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextEntry();
         }

         return n - i - 1;
      }

      // $FF: synthetic method
      MapIterator(Object x1) {
         this();
      }
   }

   final class MapEntry implements java.util.Map.Entry<Long, V>, Long2ObjectMap.Entry<V>, LongObjectPair<V> {
      int index;

      MapEntry(int index) {
         this.index = index;
      }

      MapEntry() {
      }

      public long getLongKey() {
         return Long2ObjectOpenHashMap.this.key[this.index];
      }

      public long leftLong() {
         return Long2ObjectOpenHashMap.this.key[this.index];
      }

      public V getValue() {
         return Long2ObjectOpenHashMap.this.value[this.index];
      }

      public V right() {
         return Long2ObjectOpenHashMap.this.value[this.index];
      }

      public V setValue(V v) {
         V oldValue = Long2ObjectOpenHashMap.this.value[this.index];
         Long2ObjectOpenHashMap.this.value[this.index] = v;
         return oldValue;
      }

      public LongObjectPair<V> right(V v) {
         Long2ObjectOpenHashMap.this.value[this.index] = v;
         return this;
      }

      /** @deprecated */
      @Deprecated
      public Long getKey() {
         return Long2ObjectOpenHashMap.this.key[this.index];
      }

      public boolean equals(Object o) {
         if (!(o instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry<Long, V> e = (java.util.Map.Entry)o;
            return Long2ObjectOpenHashMap.this.key[this.index] == (Long)e.getKey() && Objects.equals(Long2ObjectOpenHashMap.this.value[this.index], e.getValue());
         }
      }

      public int hashCode() {
         return HashCommon.long2int(Long2ObjectOpenHashMap.this.key[this.index]) ^ (Long2ObjectOpenHashMap.this.value[this.index] == null ? 0 : Long2ObjectOpenHashMap.this.value[this.index].hashCode());
      }

      public String toString() {
         return Long2ObjectOpenHashMap.this.key[this.index] + "=>" + Long2ObjectOpenHashMap.this.value[this.index];
      }
   }
}
