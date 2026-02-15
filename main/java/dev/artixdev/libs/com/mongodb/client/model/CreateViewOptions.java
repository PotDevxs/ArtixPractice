package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class CreateViewOptions {
   private Collation collation;

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public CreateViewOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public String toString() {
      return "CreateViewOptions{collation=" + this.collation + '}';
   }
}
