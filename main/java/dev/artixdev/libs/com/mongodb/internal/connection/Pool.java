package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.TimeUnit;

interface Pool<T> {
   T get();

   T get(long var1, TimeUnit var3);

   void release(T var1);

   void close();

   void release(T var1, boolean var2);
}
