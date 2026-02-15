package dev.artixdev.libs.io.github.retrooper.packetevents.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public class InjectedList<E> implements List<E> {
   private final List<E> originalList;
   private final Consumer<E> pushBackAction;

   public InjectedList(List<E> originalList, Consumer<E> pushBackAction) {
      Iterator<E> var3 = originalList.iterator();

      while(var3.hasNext()) {
         E key = var3.next();
         pushBackAction.accept(key);
      }

      this.originalList = originalList;
      this.pushBackAction = pushBackAction;
   }

   public List<E> originalList() {
      return this.originalList;
   }

   public Consumer<E> pushBackAction() {
      return this.pushBackAction;
   }

   public synchronized boolean add(E e) {
      this.pushBackAction.accept(e);
      return this.originalList.add(e);
   }

   public synchronized boolean addAll(@NotNull Collection<? extends E> c) {
      Iterator<? extends E> var2 = c.iterator();

      while(var2.hasNext()) {
         E element = var2.next();
         this.pushBackAction.accept(element);
      }

      return this.originalList.addAll(c);
   }

   public synchronized boolean addAll(int index, @NotNull Collection<? extends E> c) {
      Iterator<? extends E> var3 = c.iterator();

      while(var3.hasNext()) {
         E element = var3.next();
         this.pushBackAction.accept(element);
      }

      return this.originalList.addAll(index, c);
   }

   public synchronized void add(int index, E element) {
      this.pushBackAction.accept(element);
      this.originalList.add(index, element);
   }

   public synchronized int size() {
      return this.originalList.size();
   }

   public synchronized boolean isEmpty() {
      return this.originalList.isEmpty();
   }

   public synchronized boolean contains(Object o) {
      return this.originalList.contains(o);
   }

   @NotNull
   public synchronized Iterator<E> iterator() {
      return this.originalList.iterator();
   }

   @NotNull
   public synchronized Object[] toArray() {
      return this.originalList.toArray();
   }

   @NotNull
   public synchronized <T> T[] toArray(@NotNull T[] a) {
      return this.originalList.toArray(a);
   }

   public synchronized boolean remove(Object o) {
      return this.originalList.remove(o);
   }

   public synchronized boolean containsAll(@NotNull Collection<?> c) {
      return this.originalList.containsAll(c);
   }

   public synchronized boolean removeAll(@NotNull Collection<?> c) {
      return this.originalList.removeAll(c);
   }

   public synchronized boolean retainAll(@NotNull Collection<?> c) {
      return this.originalList.retainAll(c);
   }

   public synchronized void clear() {
      this.originalList.clear();
   }

   public synchronized E get(int index) {
      return this.originalList.get(index);
   }

   public synchronized E set(int index, E element) {
      return this.originalList.set(index, element);
   }

   public synchronized E remove(int index) {
      return this.originalList.remove(index);
   }

   public synchronized int indexOf(Object o) {
      return this.originalList.indexOf(o);
   }

   public synchronized int lastIndexOf(Object o) {
      return this.originalList.lastIndexOf(o);
   }

   @NotNull
   public synchronized ListIterator<E> listIterator() {
      return this.originalList.listIterator();
   }

   @NotNull
   public synchronized ListIterator<E> listIterator(int index) {
      return this.originalList.listIterator(index);
   }

   @NotNull
   public synchronized List<E> subList(int fromIndex, int toIndex) {
      return this.originalList.subList(fromIndex, toIndex);
   }
}
