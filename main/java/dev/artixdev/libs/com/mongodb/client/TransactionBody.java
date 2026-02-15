package dev.artixdev.libs.com.mongodb.client;

public interface TransactionBody<T> {
   T execute();
}
