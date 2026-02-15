package dev.artixdev.libs.org.bson.types;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import dev.artixdev.libs.org.bson.assertions.Assertions;

class StringRangeSet implements Set<String> {
   private static final String[] STRINGS = new String[1024];
   private final int size;

   StringRangeSet(int size) {
      Assertions.isTrue("size >= 0", size >= 0);
      this.size = size;
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean contains(Object o) {
      if (!(o instanceof String)) {
         return false;
      } else {
         try {
            int i = Integer.parseInt((String)o);
            return i >= 0 && i < this.size();
         } catch (NumberFormatException e) {
            return false;
         }
      }
   }

   public Iterator<String> iterator() {
      return new Iterator<String>() {
         private int cur = 0;

         public boolean hasNext() {
            return this.cur < StringRangeSet.this.size;
         }

         public String next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return StringRangeSet.this.intToString(this.cur++);
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public Object[] toArray() {
      Object[] retVal = new Object[this.size()];

      for(int i = 0; i < this.size(); ++i) {
         retVal[i] = this.intToString(i);
      }

      return retVal;
   }

   public <T> T[] toArray(T[] a) {
      @SuppressWarnings("unchecked")
      T[] retVal = a.length >= this.size() ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), this.size);

      for(int i = 0; i < this.size(); ++i) {
         @SuppressWarnings("unchecked")
         T element = (T) this.intToString(i);
         retVal[i] = element;
      }

      if (a.length > this.size()) {
         a[this.size] = null;
      }

      return retVal;
   }

   public boolean add(String integer) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(Object o) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection<?> c) {
      Iterator<?> iterator = c.iterator();

      Object e;
      do {
         if (!iterator.hasNext()) {
            return true;
         }

         e = iterator.next();
      } while(this.contains(e));

      return false;
   }

   public boolean addAll(Collection<? extends String> c) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   private String intToString(int i) {
      return i < STRINGS.length ? STRINGS[i] : Integer.toString(i);
   }

   static {
      for(int i = 0; i < STRINGS.length; ++i) {
         STRINGS[i] = String.valueOf(i);
      }

   }
}
