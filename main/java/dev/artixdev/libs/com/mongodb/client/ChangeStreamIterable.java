package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.changestream.ChangeStreamDocument;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocument;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonValue;

public interface ChangeStreamIterable<TResult> extends MongoIterable<ChangeStreamDocument<TResult>> {
   MongoChangeStreamCursor<ChangeStreamDocument<TResult>> cursor();

   ChangeStreamIterable<TResult> fullDocument(FullDocument var1);

   ChangeStreamIterable<TResult> fullDocumentBeforeChange(FullDocumentBeforeChange var1);

   ChangeStreamIterable<TResult> resumeAfter(BsonDocument var1);

   ChangeStreamIterable<TResult> batchSize(int var1);

   ChangeStreamIterable<TResult> maxAwaitTime(long var1, TimeUnit var3);

   ChangeStreamIterable<TResult> collation(@Nullable Collation var1);

   <TDocument> MongoIterable<TDocument> withDocumentClass(Class<TDocument> var1);

   ChangeStreamIterable<TResult> startAtOperationTime(BsonTimestamp var1);

   ChangeStreamIterable<TResult> startAfter(BsonDocument var1);

   ChangeStreamIterable<TResult> comment(@Nullable String var1);

   ChangeStreamIterable<TResult> comment(@Nullable BsonValue var1);

   ChangeStreamIterable<TResult> showExpandedEvents(boolean var1);
}
