package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public enum ValidationAction {
   ERROR("error"),
   WARN("warn");

   private final String value;

   private ValidationAction(String value) {
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   public static ValidationAction fromString(String validationAction) {
      Assertions.notNull("validationAction", validationAction);
      ValidationAction[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ValidationAction action = var1[var3];
         if (validationAction.equalsIgnoreCase(action.value)) {
            return action;
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid validationAction", validationAction));
   }

   // $FF: synthetic method
   private static ValidationAction[] $values() {
      return new ValidationAction[]{ERROR, WARN};
   }
}
