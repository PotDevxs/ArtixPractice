package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserProfile {
   private UUID uuid;
   private String name;
   private List<TextureProperty> textureProperties;

   public UserProfile(UUID uuid, String name) {
      this.uuid = uuid;
      this.name = name;
      this.textureProperties = new ArrayList();
   }

   public UserProfile(UUID uuid, String name, List<TextureProperty> textureProperties) {
      this.uuid = uuid;
      this.name = name;
      this.textureProperties = textureProperties;
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public void setUUID(UUID uuid) {
      this.uuid = uuid;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<TextureProperty> getTextureProperties() {
      return this.textureProperties;
   }

   public void setTextureProperties(List<TextureProperty> textureProperties) {
      this.textureProperties = textureProperties;
   }
}
