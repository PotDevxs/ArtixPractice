package dev.artixdev.libs.com.mongodb.client.model.changestream;

public enum FullDocument {
   DEFAULT("default"),
   UPDATE_LOOKUP("updateLookup"),
   WHEN_AVAILABLE("whenAvailable"),
   REQUIRED("required");

   private final String value;

   private FullDocument(String caseFirst) {
      this.value = caseFirst;
   }

   public String getValue() {
      return this.value;
   }

   public static FullDocument fromString(String changeStreamFullDocument) {
      if (changeStreamFullDocument != null) {
         FullDocument[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            FullDocument fullDocument = var1[var3];
            if (changeStreamFullDocument.equals(fullDocument.value)) {
               return fullDocument;
            }
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid ChangeStreamFullDocument", changeStreamFullDocument));
   }

   // $FF: synthetic method
   private static FullDocument[] $values() {
      return new FullDocument[]{DEFAULT, UPDATE_LOOKUP, WHEN_AVAILABLE, REQUIRED};
   }
}
