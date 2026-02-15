package dev.artixdev.libs.com.mongodb.client.model;

public enum CollationCaseFirst {
   UPPER("upper"),
   LOWER("lower"),
   OFF("off");

   private final String value;

   private CollationCaseFirst(String caseFirst) {
      this.value = caseFirst;
   }

   public String getValue() {
      return this.value;
   }

   public static CollationCaseFirst fromString(String collationCaseFirst) {
      if (collationCaseFirst != null) {
         CollationCaseFirst[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            CollationCaseFirst caseFirst = var1[var3];
            if (collationCaseFirst.equals(caseFirst.value)) {
               return caseFirst;
            }
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid collationCaseFirst", collationCaseFirst));
   }

   // $FF: synthetic method
   private static CollationCaseFirst[] $values() {
      return new CollationCaseFirst[]{UPPER, LOWER, OFF};
   }
}
