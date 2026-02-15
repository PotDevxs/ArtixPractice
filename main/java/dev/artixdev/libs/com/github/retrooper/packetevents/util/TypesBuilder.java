package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.google.gson.JsonArray;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;

public class TypesBuilder {
   private final String mapPath;
   private JsonObject fileMappings;
   private final VersionMapper versionMapper;

   public TypesBuilder(String mapPath, ClientVersion... versions) {
      this.mapPath = mapPath;
      this.versionMapper = new VersionMapper(versions);
   }

   public JsonObject getFileMappings() {
      return this.fileMappings;
   }

   public ClientVersion[] getVersions() {
      return this.versionMapper.getVersions();
   }

   public ClientVersion[] getReversedVersions() {
      return this.versionMapper.getReversedVersions();
   }

   public int getDataIndex(ClientVersion rawVersion) {
      return this.versionMapper.getIndex(rawVersion);
   }

   public void unloadFileMappings() {
      this.fileMappings = null;
   }

   public TypesBuilderData defineFromArray(String key) {
      if (this.fileMappings == null) {
         this.fileMappings = MappingHelper.getJSONObject(this.mapPath);
      }

      ResourceLocation name = new ResourceLocation(key);
      int[] ids = new int[this.getVersions().length];
      int index = 0;
      for (ClientVersion v : this.getVersions()) {
         JsonArray array = this.fileMappings.getAsJsonArray(v.name());
         int tempId = 0;
         for (JsonElement element : array) {
            if (element.isJsonPrimitive()) {
               String elementString = element.getAsString();
               if (elementString.equals(key)) {
                  ids[index] = tempId;
                  break;
               }
               ++tempId;
            }
         }
         ++index;
      }

      return new TypesBuilderData(name, ids);
   }

   public TypesBuilderData define(String key) {
      if (this.fileMappings == null) {
         this.fileMappings = MappingHelper.getJSONObject(this.mapPath);
      }

      ResourceLocation name = new ResourceLocation(key);
      int[] ids = new int[this.getVersions().length];
      int index = 0;
      for (ClientVersion v : this.getVersions()) {
         if (this.fileMappings.has(v.name())) {
            JsonObject jsonMap = this.fileMappings.getAsJsonObject(v.name());
            if (jsonMap.has(key)) {
               int id = jsonMap.get(key).getAsInt();
               ids[index] = id;
            } else {
               ids[index] = -1;
            }
         }

         ++index;
      }

      return new TypesBuilderData(name, ids);
   }
}
