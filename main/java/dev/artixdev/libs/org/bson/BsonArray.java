package dev.artixdev.libs.org.bson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import dev.artixdev.libs.org.bson.codecs.BsonArrayCodec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.json.JsonReader;

public class BsonArray extends BsonValue implements Cloneable, List<BsonValue> {
   private final List<BsonValue> values;

   public BsonArray(List<? extends BsonValue> values) {
      this(values, true);
   }

   public BsonArray() {
      this(new ArrayList(), false);
   }

   public BsonArray(int initialCapacity) {
      this(new ArrayList(initialCapacity), false);
   }

   BsonArray(List<? extends BsonValue> values, boolean copy) {
      if (copy) {
         this.values = new ArrayList<BsonValue>(values);
      } else {
         @SuppressWarnings("unchecked")
         List<BsonValue> castValues = (List<BsonValue>) values;
         this.values = castValues;
      }
   }

   public static BsonArray parse(String json) {
      return (new BsonArrayCodec()).decode(new JsonReader(json), DecoderContext.builder().build());
   }

   public List<BsonValue> getValues() {
      return Collections.unmodifiableList(this.values);
   }

   public BsonType getBsonType() {
      return BsonType.ARRAY;
   }

   public int size() {
      return this.values.size();
   }

   public boolean isEmpty() {
      return this.values.isEmpty();
   }

   public boolean contains(Object o) {
      return this.values.contains(o);
   }

   public Iterator<BsonValue> iterator() {
      return this.values.iterator();
   }

   public Object[] toArray() {
      return this.values.toArray();
   }

   public <T> T[] toArray(T[] a) {
      return this.values.toArray(a);
   }

   public boolean add(BsonValue bsonValue) {
      return this.values.add(bsonValue);
   }

   public boolean remove(Object o) {
      return this.values.remove(o);
   }

   public boolean containsAll(Collection<?> c) {
      return this.values.containsAll(c);
   }

   public boolean addAll(Collection<? extends BsonValue> c) {
      return this.values.addAll(c);
   }

   public boolean addAll(int index, Collection<? extends BsonValue> c) {
      return this.values.addAll(index, c);
   }

   public boolean removeAll(Collection<?> c) {
      return this.values.removeAll(c);
   }

   public boolean retainAll(Collection<?> c) {
      return this.values.retainAll(c);
   }

   public void clear() {
      this.values.clear();
   }

   public BsonValue get(int index) {
      return (BsonValue)this.values.get(index);
   }

   public BsonValue set(int index, BsonValue element) {
      return (BsonValue)this.values.set(index, element);
   }

   public void add(int index, BsonValue element) {
      this.values.add(index, element);
   }

   public BsonValue remove(int index) {
      return (BsonValue)this.values.remove(index);
   }

   public int indexOf(Object o) {
      return this.values.indexOf(o);
   }

   public int lastIndexOf(Object o) {
      return this.values.lastIndexOf(o);
   }

   public ListIterator<BsonValue> listIterator() {
      return this.values.listIterator();
   }

   public ListIterator<BsonValue> listIterator(int index) {
      return this.values.listIterator(index);
   }

   public List<BsonValue> subList(int fromIndex, int toIndex) {
      return this.values.subList(fromIndex, toIndex);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof BsonArray)) {
         return false;
      } else {
         BsonArray that = (BsonArray)o;
         return this.getValues().equals(that.getValues());
      }
   }

   public int hashCode() {
      return this.values.hashCode();
   }

   public String toString() {
      return "BsonArray{values=" + this.getValues() + '}';
   }

   public BsonArray clone() {
      BsonArray to = new BsonArray(this.size());
      Iterator<BsonValue> iterator = this.iterator();

      while(iterator.hasNext()) {
         BsonValue cur = iterator.next();
         switch(cur.getBsonType()) {
         case DOCUMENT:
            to.add((BsonValue)cur.asDocument().clone());
            break;
         case ARRAY:
            to.add((BsonValue)cur.asArray().clone());
            break;
         case BINARY:
            to.add((BsonValue)BsonBinary.clone(cur.asBinary()));
            break;
         case JAVASCRIPT_WITH_SCOPE:
            to.add((BsonValue)BsonJavaScriptWithScope.clone(cur.asJavaScriptWithScope()));
            break;
         default:
            to.add(cur);
         }
      }

      return to;
   }
}
