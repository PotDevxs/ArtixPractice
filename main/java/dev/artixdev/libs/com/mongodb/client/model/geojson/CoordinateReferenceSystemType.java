package dev.artixdev.libs.com.mongodb.client.model.geojson;

public enum CoordinateReferenceSystemType {
   NAME("name"),
   LINK("link");

   private final String typeName;

   public String getTypeName() {
      return this.typeName;
   }

   private CoordinateReferenceSystemType(String typeName) {
      this.typeName = typeName;
   }

   // $FF: synthetic method
   private static CoordinateReferenceSystemType[] $values() {
      return new CoordinateReferenceSystemType[]{NAME, LINK};
   }
}
