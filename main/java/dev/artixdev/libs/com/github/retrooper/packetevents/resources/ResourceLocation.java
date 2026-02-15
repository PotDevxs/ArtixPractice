package dev.artixdev.libs.com.github.retrooper.packetevents.resources;

public class ResourceLocation {
   protected final String namespace;
   protected final String key;

   public ResourceLocation(String namespace, String key) {
      this.namespace = namespace;
      this.key = key;
   }

   public ResourceLocation(String location) {
      String[] array = new String[]{"minecraft", location};
      int index = location.indexOf(":");
      if (index != -1) {
         array[1] = location.substring(index + 1);
         if (index >= 1) {
            array[0] = location.substring(0, index);
         }
      }

      this.namespace = array[0];
      this.key = array[1];
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getKey() {
      return this.key;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ResourceLocation)) {
         return false;
      } else {
         ResourceLocation other = (ResourceLocation)obj;
         return other.namespace.equals(this.namespace) && other.key.equals(this.key);
      }
   }

   public String toString() {
      return this.namespace + ":" + this.key;
   }

   public static ResourceLocation minecraft(String key) {
      return new ResourceLocation("minecraft", key);
   }
}
