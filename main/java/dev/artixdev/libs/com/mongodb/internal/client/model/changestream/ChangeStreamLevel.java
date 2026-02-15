package dev.artixdev.libs.com.mongodb.internal.client.model.changestream;

public enum ChangeStreamLevel {
   CLIENT,
   DATABASE,
   COLLECTION;

   // $FF: synthetic method
   private static ChangeStreamLevel[] $values() {
      return new ChangeStreamLevel[]{CLIENT, DATABASE, COLLECTION};
   }
}
