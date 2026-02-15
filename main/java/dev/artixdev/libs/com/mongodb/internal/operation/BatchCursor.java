package dev.artixdev.libs.com.mongodb.internal.operation;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@NotThreadSafe
public interface BatchCursor<T> extends Closeable, Iterator<List<T>> {
   void close();

   boolean hasNext();

   List<T> next();

   int available();

   void setBatchSize(int var1);

   int getBatchSize();

   @Nullable
   List<T> tryNext();

   @Nullable
   ServerCursor getServerCursor();

   ServerAddress getServerAddress();
}
