package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.MapReduceAction;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.conversions.Bson;

/** @deprecated */
@Deprecated
public interface MapReduceIterable<TResult> extends MongoIterable<TResult> {
   void toCollection();

   MapReduceIterable<TResult> collectionName(String var1);

   MapReduceIterable<TResult> finalizeFunction(@Nullable String var1);

   MapReduceIterable<TResult> scope(@Nullable Bson var1);

   MapReduceIterable<TResult> sort(@Nullable Bson var1);

   MapReduceIterable<TResult> filter(@Nullable Bson var1);

   MapReduceIterable<TResult> limit(int var1);

   MapReduceIterable<TResult> jsMode(boolean var1);

   MapReduceIterable<TResult> verbose(boolean var1);

   MapReduceIterable<TResult> maxTime(long var1, TimeUnit var3);

   MapReduceIterable<TResult> action(MapReduceAction var1);

   MapReduceIterable<TResult> databaseName(@Nullable String var1);

   /** @deprecated */
   @Deprecated
   MapReduceIterable<TResult> sharded(boolean var1);

   /** @deprecated */
   @Deprecated
   MapReduceIterable<TResult> nonAtomic(boolean var1);

   MapReduceIterable<TResult> batchSize(int var1);

   MapReduceIterable<TResult> bypassDocumentValidation(@Nullable Boolean var1);

   MapReduceIterable<TResult> collation(@Nullable Collation var1);
}
