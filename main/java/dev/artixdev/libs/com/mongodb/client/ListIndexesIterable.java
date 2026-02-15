package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;

public interface ListIndexesIterable<TResult> extends MongoIterable<TResult> {
   ListIndexesIterable<TResult> maxTime(long var1, TimeUnit var3);

   ListIndexesIterable<TResult> batchSize(int var1);

   ListIndexesIterable<TResult> comment(@Nullable String var1);

   ListIndexesIterable<TResult> comment(@Nullable BsonValue var1);
}
