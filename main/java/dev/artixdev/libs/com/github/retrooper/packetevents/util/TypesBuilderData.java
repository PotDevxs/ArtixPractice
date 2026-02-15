package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;

public class TypesBuilderData {
   private final int[] data;
   private final ResourceLocation name;

   public TypesBuilderData(ResourceLocation name, int[] data) {
      this.name = name;
      this.data = data;
   }

   public int[] getData() {
      return this.data;
   }

   public ResourceLocation getName() {
      return this.name;
   }
}
