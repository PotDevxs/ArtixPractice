package dev.artixdev.libs.com.mongodb.internal.async;

import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;

public interface AsyncAggregateResponseBatchCursor<T> extends AsyncBatchCursor<T> {
   BsonDocument getPostBatchResumeToken();

   BsonTimestamp getOperationTime();

   boolean isFirstBatchEmpty();

   int getMaxWireVersion();
}
