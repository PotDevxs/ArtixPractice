package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.conversions.Bson;

public interface ListDatabasesIterable<TResult> extends MongoIterable<TResult> {
   ListDatabasesIterable<TResult> maxTime(long var1, TimeUnit var3);

   ListDatabasesIterable<TResult> batchSize(int var1);

   ListDatabasesIterable<TResult> filter(@Nullable Bson var1);

   ListDatabasesIterable<TResult> nameOnly(@Nullable Boolean var1);

   ListDatabasesIterable<TResult> authorizedDatabasesOnly(@Nullable Boolean var1);

   ListDatabasesIterable<TResult> comment(@Nullable String var1);

   ListDatabasesIterable<TResult> comment(@Nullable BsonValue var1);
}
