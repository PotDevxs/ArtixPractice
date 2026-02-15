package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;

public interface MapReduceAsyncBatchCursor<T> extends AsyncBatchCursor<T> {
   MapReduceStatistics getStatistics();
}
