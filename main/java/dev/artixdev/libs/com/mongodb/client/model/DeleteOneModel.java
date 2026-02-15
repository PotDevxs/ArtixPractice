package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class DeleteOneModel<T> extends WriteModel<T> {
   private final Bson filter;
   private final DeleteOptions options;

   public DeleteOneModel(Bson filter) {
      this(filter, new DeleteOptions());
   }

   public DeleteOneModel(Bson filter, DeleteOptions options) {
      this.filter = (Bson)Assertions.notNull("filter", filter);
      this.options = (DeleteOptions)Assertions.notNull("options", options);
   }

   public Bson getFilter() {
      return this.filter;
   }

   public DeleteOptions getOptions() {
      return this.options;
   }

   public String toString() {
      return "DeleteOneModel{filter=" + this.filter + ", options=" + this.options + '}';
   }
}
