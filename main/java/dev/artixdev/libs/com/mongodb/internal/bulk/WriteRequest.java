package dev.artixdev.libs.com.mongodb.internal.bulk;

public abstract class WriteRequest {
   WriteRequest() {
   }

   public abstract WriteRequest.Type getType();

   public static enum Type {
      INSERT,
      UPDATE,
      REPLACE,
      DELETE;

      // $FF: synthetic method
      private static WriteRequest.Type[] $values() {
         return new WriteRequest.Type[]{INSERT, UPDATE, REPLACE, DELETE};
      }
   }
}
