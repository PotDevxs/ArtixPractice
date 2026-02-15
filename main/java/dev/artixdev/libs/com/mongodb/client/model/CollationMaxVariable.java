package dev.artixdev.libs.com.mongodb.client.model;

public enum CollationMaxVariable {
   PUNCT("punct"),
   SPACE("space");

   private final String value;

   private CollationMaxVariable(String caseFirst) {
      this.value = caseFirst;
   }

   public String getValue() {
      return this.value;
   }

   public static CollationMaxVariable fromString(String collationMaxVariable) {
      if (collationMaxVariable != null) {
         CollationMaxVariable[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            CollationMaxVariable maxVariable = var1[var3];
            if (collationMaxVariable.equals(maxVariable.value)) {
               return maxVariable;
            }
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid collationMaxVariable", collationMaxVariable));
   }

   // $FF: synthetic method
   private static CollationMaxVariable[] $values() {
      return new CollationMaxVariable[]{PUNCT, SPACE};
   }
}
