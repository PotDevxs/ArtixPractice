package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.org.bson.types.ObjectId;

@ThreadSafe
interface InternalConnectionFactory {
   default InternalConnection create(ServerId serverId) {
      return this.create(serverId, new ConnectionGenerationSupplier() {
         public int getGeneration() {
            return 0;
         }

         public int getGeneration(@NonNull ObjectId serviceId) {
            return 0;
         }
      });
   }

   InternalConnection create(ServerId var1, ConnectionGenerationSupplier var2);
}
