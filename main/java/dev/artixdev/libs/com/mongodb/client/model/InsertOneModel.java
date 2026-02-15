package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public final class InsertOneModel<T> extends WriteModel<T> {
   private final T document;

   public InsertOneModel(T document) {
      this.document = Assertions.notNull("document", document);
   }

   public T getDocument() {
      return this.document;
   }

   public String toString() {
      return "InsertOneModel{document=" + this.document + '}';
   }
}
