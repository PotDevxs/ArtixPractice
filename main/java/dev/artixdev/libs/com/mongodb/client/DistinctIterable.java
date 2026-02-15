package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public interface DistinctIterable<TResult> extends MongoIterable<TResult> {
   DistinctIterable<TResult> filter(@Nullable Bson var1);

   DistinctIterable<TResult> maxTime(long var1, TimeUnit var3);

   DistinctIterable<TResult> batchSize(int var1);

   DistinctIterable<TResult> collation(@Nullable Collation var1);

   DistinctIterable<TResult> comment(@Nullable String var1);

   DistinctIterable<TResult> comment(@Nullable BsonValue var1);
}
