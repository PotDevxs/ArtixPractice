package dev.artixdev.libs.com.mongodb.internal.async;

import java.io.Closeable;
import java.util.List;

public interface AsyncBatchCursor<T> extends Closeable {
   void next(SingleResultCallback<List<T>> var1);

   void setBatchSize(int var1);

   int getBatchSize();

   boolean isClosed();

   void close();
}
