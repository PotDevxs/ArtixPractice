package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class DropCollectionOptions {
   private Bson encryptedFields;

   @Nullable
   @Beta({Beta.Reason.SERVER})
   public Bson getEncryptedFields() {
      return this.encryptedFields;
   }

   @Beta({Beta.Reason.SERVER})
   public DropCollectionOptions encryptedFields(@Nullable Bson encryptedFields) {
      this.encryptedFields = encryptedFields;
      return this;
   }

   public String toString() {
      return "DropCollectionOptions{, encryptedFields=" + this.encryptedFields + '}';
   }
}
