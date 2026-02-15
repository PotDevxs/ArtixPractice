package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

final class DocumentHelper {
   private DocumentHelper() {
   }

   static void putIfTrue(BsonDocument command, String key, boolean condition) {
      if (condition) {
         command.put((String)key, (BsonValue)BsonBoolean.TRUE);
      }

   }

   static void putIfFalse(BsonDocument command, String key, boolean condition) {
      if (!condition) {
         command.put((String)key, (BsonValue)BsonBoolean.FALSE);
      }

   }

   static void putIfNotNullOrEmpty(BsonDocument command, String key, @Nullable BsonDocument documentValue) {
      if (documentValue != null && !documentValue.isEmpty()) {
         command.put((String)key, (BsonValue)documentValue);
      }

   }

   static void putIfNotNull(BsonDocument command, String key, @Nullable BsonValue value) {
      if (value != null) {
         command.put(key, value);
      }

   }

   static void putIfNotNull(BsonDocument command, String key, @Nullable String value) {
      if (value != null) {
         command.put((String)key, (BsonValue)(new BsonString(value)));
      }

   }

   static void putIfNotZero(BsonDocument command, String key, int value) {
      if (value != 0) {
         command.put((String)key, (BsonValue)(new BsonInt32(value)));
      }

   }

   static void putIfNotZero(BsonDocument command, String key, long value) {
      if (value != 0L) {
         command.put((String)key, (BsonValue)(new BsonInt64(value)));
      }

   }
}
