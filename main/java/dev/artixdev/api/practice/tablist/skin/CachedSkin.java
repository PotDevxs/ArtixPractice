package dev.artixdev.api.practice.tablist.skin;

public class CachedSkin {
   private final String name;
   private final String value;
   private final String signature;

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof CachedSkin)) {
         return false;
      } else {
         CachedSkin skin = (CachedSkin)obj;
         return skin.getName().equals(this.name) && skin.getValue().equals(this.value) && skin.getSignature().equals(this.signature);
      }
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public String getSignature() {
      return this.signature;
   }

   public CachedSkin(String name, String value, String signature) {
      this.name = name;
      this.value = value;
      this.signature = signature;
   }
}
