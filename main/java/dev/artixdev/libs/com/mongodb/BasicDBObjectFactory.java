package dev.artixdev.libs.com.mongodb;

import java.util.List;

class BasicDBObjectFactory implements DBObjectFactory {
   public DBObject getInstance() {
      return new BasicDBObject();
   }

   public DBObject getInstance(List<String> path) {
      return new BasicDBObject();
   }
}
