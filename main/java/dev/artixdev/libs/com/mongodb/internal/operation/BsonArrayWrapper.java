package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.assertions.Assertions;

class BsonArrayWrapper<T> extends BsonArray {
   private final List<T> wrappedArray;

   BsonArrayWrapper(List<T> wrappedArray) {
      this.wrappedArray = (List)Assertions.notNull("wrappedArray", wrappedArray);
   }

   public List<T> getWrappedArray() {
      return this.wrappedArray;
   }

   public List<BsonValue> getValues() {
      throw new UnsupportedOperationException();
   }

   public int size() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      throw new UnsupportedOperationException();
   }

   public boolean contains(Object o) {
      throw new UnsupportedOperationException();
   }

   public Iterator<BsonValue> iterator() {
      throw new UnsupportedOperationException();
   }

   public Object[] toArray() {
      throw new UnsupportedOperationException();
   }

   public <T> T[] toArray(T[] a) {
      throw new UnsupportedOperationException();
   }

   public boolean add(BsonValue bsonValue) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(Object o) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection<? extends BsonValue> c) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(int index, Collection<? extends BsonValue> c) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public BsonValue get(int index) {
      throw new UnsupportedOperationException();
   }

   public BsonValue set(int index, BsonValue element) {
      throw new UnsupportedOperationException();
   }

   public void add(int index, BsonValue element) {
      throw new UnsupportedOperationException();
   }

   public BsonValue remove(int index) {
      throw new UnsupportedOperationException();
   }

   public int indexOf(Object o) {
      throw new UnsupportedOperationException();
   }

   public int lastIndexOf(Object o) {
      throw new UnsupportedOperationException();
   }

   public ListIterator<BsonValue> listIterator() {
      throw new UnsupportedOperationException();
   }

   public ListIterator<BsonValue> listIterator(int index) {
      throw new UnsupportedOperationException();
   }

   public List<BsonValue> subList(int fromIndex, int toIndex) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonArrayWrapper<?> that = (BsonArrayWrapper)o;
         return this.wrappedArray.equals(that.wrappedArray);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.wrappedArray.hashCode();
      return result;
   }

   public String toString() {
      return "BsonArrayWrapper{wrappedArray=" + this.wrappedArray + '}';
   }

   public BsonArray clone() {
      throw new UnsupportedOperationException("This should never be called on an instance of this type");
   }
}
