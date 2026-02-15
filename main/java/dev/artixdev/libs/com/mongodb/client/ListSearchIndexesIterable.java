package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.annotations.Evolving;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;

@Evolving
public interface ListSearchIndexesIterable<TResult> extends MongoIterable<TResult> {
   ListSearchIndexesIterable<TResult> name(String var1);

   ListSearchIndexesIterable<TResult> allowDiskUse(@Nullable Boolean var1);

   ListSearchIndexesIterable<TResult> batchSize(int var1);

   ListSearchIndexesIterable<TResult> maxTime(long var1, TimeUnit var3);

   ListSearchIndexesIterable<TResult> collation(@Nullable Collation var1);

   ListSearchIndexesIterable<TResult> comment(@Nullable String var1);

   ListSearchIndexesIterable<TResult> comment(@Nullable BsonValue var1);

   Document explain();

   Document explain(ExplainVerbosity var1);

   <E> E explain(Class<E> var1);

   <E> E explain(Class<E> var1, ExplainVerbosity var2);
}
