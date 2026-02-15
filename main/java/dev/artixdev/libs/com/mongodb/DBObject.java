package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.BSONObject;

public interface DBObject extends BSONObject {
   void markAsPartialObject();

   boolean isPartialObject();
}
