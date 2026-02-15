package dev.artixdev.libs.com.mongodb.client;

import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.TransactionOptions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface ClientSession extends dev.artixdev.libs.com.mongodb.session.ClientSession {
   @Nullable
   ServerAddress getPinnedServerAddress();

   boolean hasActiveTransaction();

   boolean notifyMessageSent();

   void notifyOperationInitiated(Object var1);

   TransactionOptions getTransactionOptions();

   void startTransaction();

   void startTransaction(TransactionOptions var1);

   void commitTransaction();

   void abortTransaction();

   <T> T withTransaction(TransactionBody<T> var1);

   <T> T withTransaction(TransactionBody<T> var1, TransactionOptions var2);
}
