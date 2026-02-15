package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public interface ListCollectionsIterable<TResult> extends MongoIterable<TResult> {
   ListCollectionsIterable<TResult> filter(@Nullable Bson var1);

   ListCollectionsIterable<TResult> maxTime(long var1, TimeUnit var3);

   ListCollectionsIterable<TResult> batchSize(int var1);

   ListCollectionsIterable<TResult> comment(@Nullable String var1);

   ListCollectionsIterable<TResult> comment(@Nullable BsonValue var1);
}
