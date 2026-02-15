package dev.artixdev.practice.interfaces;

public interface Expirable<E> {
    
    void onExpire(E element);
    
    long getTimestamp(E element);
}
