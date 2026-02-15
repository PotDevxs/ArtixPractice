package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.conversions.Bson;

public interface AggregateIterable<TResult> extends MongoIterable<TResult> {
   void toCollection();

   AggregateIterable<TResult> allowDiskUse(@Nullable Boolean var1);

   AggregateIterable<TResult> batchSize(int var1);

   AggregateIterable<TResult> maxTime(long var1, TimeUnit var3);

   AggregateIterable<TResult> maxAwaitTime(long var1, TimeUnit var3);

   AggregateIterable<TResult> bypassDocumentValidation(@Nullable Boolean var1);

   AggregateIterable<TResult> collation(@Nullable Collation var1);

   AggregateIterable<TResult> comment(@Nullable String var1);

   AggregateIterable<TResult> comment(@Nullable BsonValue var1);

   AggregateIterable<TResult> hint(@Nullable Bson var1);

   AggregateIterable<TResult> hintString(@Nullable String var1);

   AggregateIterable<TResult> let(@Nullable Bson var1);

   Document explain();

   Document explain(ExplainVerbosity var1);

   <E> E explain(Class<E> var1);

   <E> E explain(Class<E> var1, ExplainVerbosity var2);
}
