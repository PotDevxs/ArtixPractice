package dev.artixdev.libs.com.mongodb.client.model.changestream;

public enum OperationType {
   INSERT("insert"),
   UPDATE("update"),
   REPLACE("replace"),
   DELETE("delete"),
   INVALIDATE("invalidate"),
   DROP("drop"),
   DROP_DATABASE("dropDatabase"),
   RENAME("rename"),
   OTHER("other");

   private final String value;

   private OperationType(String operationTypeName) {
      this.value = operationTypeName;
   }

   public String getValue() {
      return this.value;
   }

   public static OperationType fromString(String operationTypeName) {
      if (operationTypeName != null) {
         OperationType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            OperationType operationType = var1[var3];
            if (operationTypeName.equals(operationType.value)) {
               return operationType;
            }
         }
      }

      return OTHER;
   }

   public String toString() {
      return "OperationType{value='" + this.value + "'}";
   }

   // $FF: synthetic method
   private static OperationType[] $values() {
      return new OperationType[]{INSERT, UPDATE, REPLACE, DELETE, INVALIDATE, DROP, DROP_DATABASE, RENAME, OTHER};
   }
}
