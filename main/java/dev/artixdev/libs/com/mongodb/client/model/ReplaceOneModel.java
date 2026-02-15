package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class ReplaceOneModel<T> extends WriteModel<T> {
   private final Bson filter;
   private final T replacement;
   private final ReplaceOptions options;

   public ReplaceOneModel(Bson filter, T replacement) {
      this(filter, replacement, new ReplaceOptions());
   }

   public ReplaceOneModel(Bson filter, T replacement, ReplaceOptions options) {
      this.filter = (Bson)Assertions.notNull("filter", filter);
      this.options = (ReplaceOptions)Assertions.notNull("options", options);
      this.replacement = Assertions.notNull("replacement", replacement);
   }

   public Bson getFilter() {
      return this.filter;
   }

   public T getReplacement() {
      return this.replacement;
   }

   public ReplaceOptions getReplaceOptions() {
      return this.options;
   }

   public String toString() {
      return "ReplaceOneModel{filter=" + this.filter + ", replacement=" + this.replacement + ", options=" + this.options + '}';
   }
}
