package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoConnectionPoolClearedException;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.types.ObjectId;

@ThreadSafe
interface ConnectionPool extends Closeable {
   InternalConnection get(OperationContext var1) throws MongoConnectionPoolClearedException;

   InternalConnection get(OperationContext var1, long var2, TimeUnit var4) throws MongoConnectionPoolClearedException;

   void getAsync(OperationContext var1, SingleResultCallback<InternalConnection> var2);

   void invalidate(@Nullable Throwable var1);

   void invalidate(ObjectId var1, int var2);

   void ready();

   void close();

   int getGeneration();
}
