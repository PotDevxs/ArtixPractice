package dev.artixdev.libs.com.mongodb.client;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.CursorType;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.conversions.Bson;

public interface FindIterable<TResult> extends MongoIterable<TResult> {
   FindIterable<TResult> filter(@Nullable Bson var1);

   FindIterable<TResult> limit(int var1);

   FindIterable<TResult> skip(int var1);

   FindIterable<TResult> maxTime(long var1, TimeUnit var3);

   FindIterable<TResult> maxAwaitTime(long var1, TimeUnit var3);

   FindIterable<TResult> projection(@Nullable Bson var1);

   FindIterable<TResult> sort(@Nullable Bson var1);

   FindIterable<TResult> noCursorTimeout(boolean var1);

   /** @deprecated */
   @Deprecated
   FindIterable<TResult> oplogReplay(boolean var1);

   FindIterable<TResult> partial(boolean var1);

   FindIterable<TResult> cursorType(CursorType var1);

   FindIterable<TResult> batchSize(int var1);

   FindIterable<TResult> collation(@Nullable Collation var1);

   FindIterable<TResult> comment(@Nullable String var1);

   FindIterable<TResult> comment(@Nullable BsonValue var1);

   FindIterable<TResult> hint(@Nullable Bson var1);

   FindIterable<TResult> hintString(@Nullable String var1);

   FindIterable<TResult> let(@Nullable Bson var1);

   FindIterable<TResult> max(@Nullable Bson var1);

   FindIterable<TResult> min(@Nullable Bson var1);

   FindIterable<TResult> returnKey(boolean var1);

   FindIterable<TResult> showRecordId(boolean var1);

   FindIterable<TResult> allowDiskUse(@Nullable Boolean var1);

   Document explain();

   Document explain(ExplainVerbosity var1);

   <E> E explain(Class<E> var1);

   <E> E explain(Class<E> var1, ExplainVerbosity var2);
}
