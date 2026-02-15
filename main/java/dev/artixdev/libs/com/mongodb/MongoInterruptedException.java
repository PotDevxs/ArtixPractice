package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class MongoInterruptedException extends MongoException {
   private static final long serialVersionUID = -4110417867718417860L;

   public MongoInterruptedException(@Nullable String message, @Nullable Exception e) {
      super(message, e);
   }
}
