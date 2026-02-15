package dev.artixdev.libs.org.bson;

public enum BsonType {
   END_OF_DOCUMENT(0),
   DOUBLE(1),
   STRING(2),
   DOCUMENT(3),
   ARRAY(4),
   BINARY(5),
   UNDEFINED(6),
   OBJECT_ID(7),
   BOOLEAN(8),
   DATE_TIME(9),
   NULL(10),
   REGULAR_EXPRESSION(11),
   DB_POINTER(12),
   JAVASCRIPT(13),
   SYMBOL(14),
   JAVASCRIPT_WITH_SCOPE(15),
   INT32(16),
   TIMESTAMP(17),
   INT64(18),
   DECIMAL128(19),
   MIN_KEY(255),
   MAX_KEY(127);

   private static final BsonType[] LOOKUP_TABLE = new BsonType[MIN_KEY.getValue() + 1];
   private final int value;

   private BsonType(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   public static BsonType findByValue(int value) {
      return LOOKUP_TABLE[value & 255];
   }

   public boolean isContainer() {
      return this == DOCUMENT || this == ARRAY;
   }

   // $FF: synthetic method
   private static BsonType[] $values() {
      return new BsonType[]{END_OF_DOCUMENT, DOUBLE, STRING, DOCUMENT, ARRAY, BINARY, UNDEFINED, OBJECT_ID, BOOLEAN, DATE_TIME, NULL, REGULAR_EXPRESSION, DB_POINTER, JAVASCRIPT, SYMBOL, JAVASCRIPT_WITH_SCOPE, INT32, TIMESTAMP, INT64, DECIMAL128, MIN_KEY, MAX_KEY};
   }

   static {
      BsonType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         BsonType cur = var0[var2];
         LOOKUP_TABLE[cur.getValue()] = cur;
      }

   }
}
