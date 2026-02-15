package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public enum ReadConcernLevel {
   LOCAL("local"),
   MAJORITY("majority"),
   LINEARIZABLE("linearizable"),
   SNAPSHOT("snapshot"),
   AVAILABLE("available");

   private final String value;

   private ReadConcernLevel(String readConcernLevel) {
      this.value = readConcernLevel;
   }

   public String getValue() {
      return this.value;
   }

   public static ReadConcernLevel fromString(String readConcernLevel) {
      Assertions.notNull("readConcernLevel", readConcernLevel);
      ReadConcernLevel[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ReadConcernLevel level = var1[var3];
         if (readConcernLevel.equalsIgnoreCase(level.value)) {
            return level;
         }
      }

      throw new IllegalArgumentException(String.format("'%s' is not a valid readConcernLevel", readConcernLevel));
   }

   // $FF: synthetic method
   private static ReadConcernLevel[] $values() {
      return new ReadConcernLevel[]{LOCAL, MAJORITY, LINEARIZABLE, SNAPSHOT, AVAILABLE};
   }
}
