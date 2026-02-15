package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

@NotThreadSafe
public interface AggregateResponseBatchCursor<T> extends BatchCursor<T> {
   BsonDocument getPostBatchResumeToken();

   BsonTimestamp getOperationTime();

   boolean isFirstBatchEmpty();

   int getMaxWireVersion();
}
