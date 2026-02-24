package dev.artixdev.practice.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import dev.artixdev.practice.interfaces.Expirable;

public class ExpirableList<E> implements List<E>, Expirable<E> {
    
    private final List<E> elements;
    private final long expirationTime;
    private final Map<E, Long> timestamps;
    private int maxSize = -1; // -1 means no limit

    public ExpirableList(TimeUnit timeUnit, long duration) {
        this.elements = new ObjectArrayList<>();
        this.timestamps = new Object2LongOpenHashMap<>();
        this.expirationTime = timeUnit.toNanos(duration);
    }

    @Override
    public void onExpire(E element) {
        // Handle element expiration
    }

    @Override
    public long getTimestamp(E element) {
        return timestamps.getOrDefault(element, 0L);
    }

    private void clearExpired() {
        long currentTime = System.nanoTime();
        Iterator<E> iterator = elements.iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            Long timestamp = timestamps.get(element);
            if (timestamp != null && (currentTime - timestamp) > expirationTime) {
                onExpire(element);
                iterator.remove();
                timestamps.remove(element);
            }
        }
    }

    private void trimToMaxSize() {
        if (maxSize < 0) return;
        while (elements.size() > maxSize) {
            E oldest = elements.remove(0);
            timestamps.remove(oldest);
            onExpire(oldest);
        }
    }

    @Override
    public int size() {
        clearExpired();
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        clearExpired();
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        clearExpired();
        return elements.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        clearExpired();
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        clearExpired();
        return elements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        clearExpired();
        return elements.toArray(a);
    }

    @Override
    public boolean add(E e) {
        clearExpired();
        timestamps.put(e, System.nanoTime());
        boolean added = elements.add(e);
        trimToMaxSize();
        return added;
    }

    @Override
    public boolean remove(Object o) {
        clearExpired();
        timestamps.remove(o);
        return elements.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        clearExpired();
        return elements.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        clearExpired();
        long currentTime = System.nanoTime();
        for (E element : c) {
            timestamps.put(element, currentTime);
        }
        boolean added = elements.addAll(c);
        trimToMaxSize();
        return added;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        clearExpired();
        long currentTime = System.nanoTime();
        for (E element : c) {
            timestamps.put(element, currentTime);
        }
        boolean added = elements.addAll(index, c);
        trimToMaxSize();
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        clearExpired();
        for (Object o : c) {
            timestamps.remove(o);
        }
        return elements.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        clearExpired();
        return elements.retainAll(c);
    }

    @Override
    public void clear() {
        elements.clear();
        timestamps.clear();
    }

    @Override
    public E get(int index) {
        clearExpired();
        return elements.get(index);
    }

    @Override
    public E set(int index, E element) {
        clearExpired();
        E oldElement = elements.set(index, element);
        timestamps.remove(oldElement);
        timestamps.put(element, System.nanoTime());
        return oldElement;
    }

    @Override
    public void add(int index, E element) {
        clearExpired();
        elements.add(index, element);
        timestamps.put(element, System.nanoTime());
        trimToMaxSize();
    }

    @Override
    public E remove(int index) {
        clearExpired();
        E element = elements.remove(index);
        timestamps.remove(element);
        return element;
    }

    @Override
    public int indexOf(Object o) {
        clearExpired();
        return elements.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        clearExpired();
        return elements.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        clearExpired();
        return elements.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        clearExpired();
        return elements.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        clearExpired();
        return elements.subList(fromIndex, toIndex);
    }

    public void setMaxSize(int i) {
        maxSize = i;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
