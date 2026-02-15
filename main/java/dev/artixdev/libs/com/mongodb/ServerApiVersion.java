package dev.artixdev.libs.com.mongodb;

public enum ServerApiVersion {
   V1("1");

   private final String versionString;

   private ServerApiVersion(String versionString) {
      this.versionString = versionString;
   }

   public String getValue() {
      return this.versionString;
   }

   public static ServerApiVersion findByValue(String value) {
      byte var2 = -1;
      switch(value.hashCode()) {
      case 49:
         if (value.equals("1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            return V1;
         default:
            throw new MongoClientException("Unsupported server API version: " + value);
         }
      }
   }

   // $FF: synthetic method
   private static ServerApiVersion[] $values() {
      return new ServerApiVersion[]{V1};
   }
}
