package dev.artixdev.libs.com.mongodb.internal;

@FunctionalInterface
public interface CheckedSupplier<T, E extends Exception> {
   T get() throws E;
}
