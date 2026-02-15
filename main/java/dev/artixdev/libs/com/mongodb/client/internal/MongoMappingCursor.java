package dev.artixdev.libs.com.mongodb.client.internal;

import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.MongoCursor;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

class MongoMappingCursor<T, U> implements MongoCursor<U> {
   private final MongoCursor<T> proxied;
   private final Function<T, U> mapper;

   MongoMappingCursor(MongoCursor<T> proxied, Function<T, U> mapper) {
      this.proxied = (MongoCursor)Assertions.notNull("proxied", proxied);
      this.mapper = (Function)Assertions.notNull("mapper", mapper);
   }

   public void close() {
      this.proxied.close();
   }

   public boolean hasNext() {
      return this.proxied.hasNext();
   }

   public U next() {
      return this.mapper.apply(this.proxied.next());
   }

   public int available() {
      return this.proxied.available();
   }

   @Nullable
   public U tryNext() {
      T proxiedNext = this.proxied.tryNext();
      return proxiedNext == null ? null : this.mapper.apply(proxiedNext);
   }

   public void remove() {
      this.proxied.remove();
   }

   @Nullable
   public ServerCursor getServerCursor() {
      return this.proxied.getServerCursor();
   }

   public ServerAddress getServerAddress() {
      return this.proxied.getServerAddress();
   }
}
