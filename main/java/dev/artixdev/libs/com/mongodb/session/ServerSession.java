package dev.artixdev.libs.com.mongodb.session;

import dev.artixdev.libs.org.bson.BsonDocument;

public interface ServerSession {
   BsonDocument getIdentifier();

   long getTransactionNumber();

   long advanceTransactionNumber();

   boolean isClosed();

   void markDirty();

   boolean isMarkedDirty();
}
