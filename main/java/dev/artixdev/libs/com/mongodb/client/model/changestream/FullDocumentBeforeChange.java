package dev.artixdev.libs.com.mongodb.client.model.changestream;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public enum FullDocumentBeforeChange {
   DEFAULT("default"),
   OFF("off"),
   WHEN_AVAILABLE("whenAvailable"),
   REQUIRED("required");

   private final String value;

   public String getValue() {
      return this.value;
   }

   private FullDocumentBeforeChange(String value) {
      this.value = value;
   }

   public static FullDocumentBeforeChange fromString(String value) {
      Assertions.assertNotNull(value);
      FullDocumentBeforeChange[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         FullDocumentBeforeChange fullDocumentBeforeChange = var1[var3];
         if (value.equals(fullDocumentBeforeChange.value)) {
            return fullDocumentBeforeChange;
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid FullDocumentBeforeChange", value));
   }

   // $FF: synthetic method
   private static FullDocumentBeforeChange[] $values() {
      return new FullDocumentBeforeChange[]{DEFAULT, OFF, WHEN_AVAILABLE, REQUIRED};
   }
}
