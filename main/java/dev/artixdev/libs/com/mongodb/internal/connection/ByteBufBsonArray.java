package dev.artixdev.libs.com.mongodb.internal.connection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.io.ByteBufferBsonInput;

final class ByteBufBsonArray extends BsonArray {
   private final ByteBuf byteBuf;
   private static final String READ_ONLY_MESSAGE = "This BsonArray instance is read-only";

   ByteBufBsonArray(ByteBuf byteBuf) {
      this.byteBuf = byteBuf;
   }

   public Iterator<BsonValue> iterator() {
      return new ByteBufBsonArray.ByteBufBsonArrayIterator();
   }

   public List<BsonValue> getValues() {
      List<BsonValue> values = new ArrayList();
      Iterator<BsonValue> iterator = this.iterator();

      while(iterator.hasNext()) {
         BsonValue cur = iterator.next();
         values.add(cur);
      }

      return values;
   }

   public int size() {
      int size = 0;

      for(Iterator<BsonValue> iterator = this.iterator(); iterator.hasNext(); ++size) {
         BsonValue ignored = iterator.next();
      }

      return size;
   }

   public boolean isEmpty() {
      return !this.iterator().hasNext();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof List)) {
         return false;
      } else {
         Iterator<BsonValue> e1 = this.iterator();
         Iterator e2 = ((List)o).iterator();

         while(e1.hasNext() && e2.hasNext()) {
            if (!Objects.equals(e1.next(), e2.next())) {
               return false;
            }
         }

         return !e1.hasNext() && !e2.hasNext();
      }
   }

   public int hashCode() {
      int hashCode = 1;

      BsonValue cur;
      for(Iterator<BsonValue> iterator = this.iterator(); iterator.hasNext(); hashCode = 31 * hashCode + (cur == null ? 0 : cur.hashCode())) {
         cur = iterator.next();
      }

      return hashCode;
   }

   public boolean contains(Object o) {
      Iterator<BsonValue> iterator = this.iterator();

      BsonValue cur;
      do {
         if (!iterator.hasNext()) {
            return false;
         }

         cur = iterator.next();
      } while(!Objects.equals(o, cur));

      return true;
   }

   public Object[] toArray() {
      Object[] retVal = new Object[this.size()];
      Iterator<BsonValue> it = this.iterator();

      for(int i = 0; i < retVal.length; ++i) {
         retVal[i] = it.next();
      }

      return retVal;
   }

   public <T> T[] toArray(T[] a) {
      int size = this.size();
      T[] retVal = a.length >= size ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), size);
      Iterator<BsonValue> it = this.iterator();

      for(int i = 0; i < retVal.length; ++i) {
         retVal[i] = (T) it.next();
      }

      return retVal;
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

   public BsonValue get(int index) {
      if (index < 0) {
         throw new IndexOutOfBoundsException("Index out of range: " + index);
      } else {
         int i = 0;
         Iterator<BsonValue> iterator = this.iterator();

         BsonValue cur;
         do {
            if (!iterator.hasNext()) {
               throw new IndexOutOfBoundsException("Index out of range: " + index);
            }

            cur = iterator.next();
         } while(i++ != index);

         return cur;
      }
   }

   public int indexOf(Object o) {
      int i = 0;

      for(Iterator<BsonValue> iterator = this.iterator(); iterator.hasNext(); ++i) {
         BsonValue cur = iterator.next();
         if (Objects.equals(o, cur)) {
            return i;
         }
      }

      return -1;
   }

   public int lastIndexOf(Object o) {
      ListIterator listIterator = this.listIterator(this.size());

      do {
         if (!listIterator.hasPrevious()) {
            return -1;
         }
      } while(!Objects.equals(o, listIterator.previous()));

      return listIterator.nextIndex();
   }

   public ListIterator<BsonValue> listIterator() {
      return this.listIterator(0);
   }

   public ListIterator<BsonValue> listIterator(int index) {
      return (new ArrayList(this)).listIterator(index);
   }

   public List<BsonValue> subList(int fromIndex, int toIndex) {
      if (fromIndex < 0) {
         throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
      } else if (fromIndex > toIndex) {
         throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
      } else {
         List<BsonValue> subList = new ArrayList();
         int i = 0;

         for(Iterator<BsonValue> iterator = this.iterator(); iterator.hasNext(); ++i) {
            BsonValue cur = iterator.next();
            if (i == toIndex) {
               break;
            }

            if (i >= fromIndex) {
               subList.add(cur);
            }
         }

         if (toIndex > i) {
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
         } else {
            return subList;
         }
      }
   }

   public boolean add(BsonValue bsonValue) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public boolean remove(Object o) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public boolean addAll(Collection<? extends BsonValue> c) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public boolean addAll(int index, Collection<? extends BsonValue> c) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public void clear() {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public BsonValue set(int index, BsonValue element) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public void add(int index, BsonValue element) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   public BsonValue remove(int index) {
      throw new UnsupportedOperationException("This BsonArray instance is read-only");
   }

   private class ByteBufBsonArrayIterator implements Iterator<BsonValue> {
      private final ByteBuf duplicatedByteBuf;
      private final BsonBinaryReader bsonReader;

      private ByteBufBsonArrayIterator() {
         this.duplicatedByteBuf = ByteBufBsonArray.this.byteBuf.duplicate();
         this.bsonReader = new BsonBinaryReader(new ByteBufferBsonInput(this.duplicatedByteBuf));
         this.bsonReader.readStartDocument();
         this.bsonReader.readBsonType();
      }

      public boolean hasNext() {
         return this.bsonReader.getCurrentBsonType() != BsonType.END_OF_DOCUMENT;
      }

      public BsonValue next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.bsonReader.skipName();
            BsonValue value = ByteBufBsonHelper.readBsonValue(this.duplicatedByteBuf, this.bsonReader);
            this.bsonReader.readBsonType();
            return value;
         }
      }

      // $FF: synthetic method
      ByteBufBsonArrayIterator(Object x1) {
         this();
      }
   }
}
