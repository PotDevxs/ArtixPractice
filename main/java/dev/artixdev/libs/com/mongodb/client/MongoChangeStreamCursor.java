package dev.artixdev.libs.com.mongodb.client;

import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

@NotThreadSafe
public interface MongoChangeStreamCursor<TResult> extends MongoCursor<TResult> {
   @Nullable
   BsonDocument getResumeToken();
}
