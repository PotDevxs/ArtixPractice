package dev.artixdev.libs.com.mongodb.client.model;

public enum CollationAlternate {
   NON_IGNORABLE("non-ignorable"),
   SHIFTED("shifted");

   private final String value;

   private CollationAlternate(String caseFirst) {
      this.value = caseFirst;
   }

   public String getValue() {
      return this.value;
   }

   public static CollationAlternate fromString(String collationAlternate) {
      if (collationAlternate != null) {
         CollationAlternate[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            CollationAlternate alternate = var1[var3];
            if (collationAlternate.equals(alternate.value)) {
               return alternate;
            }
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid collationAlternate", collationAlternate));
   }

   // $FF: synthetic method
   private static CollationAlternate[] $values() {
      return new CollationAlternate[]{NON_IGNORABLE, SHIFTED};
   }
}
