package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.org.bson.types.ObjectId;

@ThreadSafe
interface ConnectionGenerationSupplier {
   int getGeneration();

   int getGeneration(@NonNull ObjectId var1);
}
