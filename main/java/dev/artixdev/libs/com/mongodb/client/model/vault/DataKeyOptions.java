package dev.artixdev.libs.com.mongodb.client.model.vault;

import java.util.Arrays;
import java.util.List;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

public class DataKeyOptions {
   private List<String> keyAltNames;
   private BsonDocument masterKey;
   private byte[] keyMaterial;

   public DataKeyOptions keyAltNames(List<String> keyAltNames) {
      this.keyAltNames = keyAltNames;
      return this;
   }

   public DataKeyOptions masterKey(BsonDocument masterKey) {
      this.masterKey = masterKey;
      return this;
   }

   public DataKeyOptions keyMaterial(byte[] keyMaterial) {
      this.keyMaterial = keyMaterial;
      return this;
   }

   @Nullable
   public List<String> getKeyAltNames() {
      return this.keyAltNames;
   }

   @Nullable
   public BsonDocument getMasterKey() {
      return this.masterKey;
   }

   @Nullable
   public byte[] getKeyMaterial() {
      return this.keyMaterial;
   }

   public String toString() {
      return "DataKeyOptions{keyAltNames=" + this.keyAltNames + ", masterKey=" + this.masterKey + ", keyMaterial=" + Arrays.toString(this.keyMaterial) + '}';
   }
}
