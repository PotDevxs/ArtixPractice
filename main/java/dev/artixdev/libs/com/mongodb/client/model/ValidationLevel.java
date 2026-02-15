package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public enum ValidationLevel {
   OFF("off"),
   STRICT("strict"),
   MODERATE("moderate");

   private final String value;

   private ValidationLevel(String value) {
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   public static ValidationLevel fromString(String validationLevel) {
      Assertions.notNull("ValidationLevel", validationLevel);
      ValidationLevel[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ValidationLevel action = var1[var3];
         if (validationLevel.equalsIgnoreCase(action.value)) {
            return action;
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid ValidationLevel", validationLevel));
   }

   // $FF: synthetic method
   private static ValidationLevel[] $values() {
      return new ValidationLevel[]{OFF, STRICT, MODERATE};
   }
}
