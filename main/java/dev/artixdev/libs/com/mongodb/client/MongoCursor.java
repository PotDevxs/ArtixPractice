package dev.artixdev.libs.com.mongodb.client;

import java.io.Closeable;
import java.util.Iterator;
import java.util.function.Consumer;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@NotThreadSafe
public interface MongoCursor<TResult> extends Closeable, Iterator<TResult> {
   void close();

   boolean hasNext();

   TResult next();

   int available();

   @Nullable
   TResult tryNext();

   @Nullable
   ServerCursor getServerCursor();

   ServerAddress getServerAddress();

   default void forEachRemaining(Consumer<? super TResult> action) {
      try {
         Iterator.super.forEachRemaining(action);
      } finally {
         this.close();
      }
   }
}
