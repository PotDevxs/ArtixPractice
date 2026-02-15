package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.types.ObjectId;

public class ObjectIdGenerator implements IdGenerator {
   public Object generate() {
      return new ObjectId();
   }
}
