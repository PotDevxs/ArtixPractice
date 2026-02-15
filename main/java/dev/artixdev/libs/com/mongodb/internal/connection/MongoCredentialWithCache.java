package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class MongoCredentialWithCache {
   private final MongoCredential credential;
   private final MongoCredentialWithCache.Cache cache;

   public MongoCredentialWithCache(MongoCredential credential) {
      this(credential, (MongoCredentialWithCache.Cache)null);
   }

   private MongoCredentialWithCache(MongoCredential credential, @Nullable MongoCredentialWithCache.Cache cache) {
      this.credential = credential;
      this.cache = cache != null ? cache : new MongoCredentialWithCache.Cache();
   }

   public MongoCredentialWithCache withMechanism(AuthenticationMechanism mechanism) {
      return new MongoCredentialWithCache(this.credential.withMechanism(mechanism), this.cache);
   }

   @Nullable
   public AuthenticationMechanism getAuthenticationMechanism() {
      return this.credential.getAuthenticationMechanism();
   }

   public MongoCredential getCredential() {
      return this.credential;
   }

   @Nullable
   public <T> T getFromCache(Object key, Class<T> clazz) {
      return clazz.cast(this.cache.get(key));
   }

   public void putInCache(Object key, Object value) {
      this.cache.set(key, value);
   }

   public Lock getLock() {
      return this.cache.lock;
   }

   static class Cache {
      private final ReentrantLock lock = new ReentrantLock();
      private Object cacheKey;
      private Object cacheValue;

      Object get(Object key) {
         return Locks.withInterruptibleLock(this.lock, (Supplier)(() -> {
            return this.cacheKey != null && this.cacheKey.equals(key) ? this.cacheValue : null;
         }));
      }

      void set(Object key, Object value) {
         Locks.withInterruptibleLock(this.lock, (Runnable)(() -> {
            this.cacheKey = key;
            this.cacheValue = value;
         }));
      }
   }
}
