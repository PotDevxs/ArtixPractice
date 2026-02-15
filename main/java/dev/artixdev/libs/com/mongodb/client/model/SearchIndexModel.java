package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class SearchIndexModel {
   @Nullable
   private final String name;
   private final Bson definition;

   public SearchIndexModel(Bson definition) {
      this.definition = (Bson)Assertions.notNull("definition", definition);
      this.name = null;
   }

   public SearchIndexModel(String name, Bson definition) {
      this.definition = (Bson)Assertions.notNull("definition", definition);
      this.name = (String)Assertions.notNull("name", name);
   }

   public Bson getDefinition() {
      return this.definition;
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   public String toString() {
      return "SearchIndexModel{name=" + this.name + ", definition=" + this.definition + '}';
   }
}
