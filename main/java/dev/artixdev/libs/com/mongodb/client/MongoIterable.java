package dev.artixdev.libs.com.mongodb.client;

import java.util.Collection;
import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface MongoIterable<TResult> extends Iterable<TResult> {
   MongoCursor<TResult> iterator();

   MongoCursor<TResult> cursor();

   @Nullable
   TResult first();

   <U> MongoIterable<U> map(Function<TResult, U> var1);

   <A extends Collection<? super TResult>> A into(A var1);

   MongoIterable<TResult> batchSize(int var1);
}
