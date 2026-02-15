package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world;

import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3i;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public class WorldBlockPosition {
   private ResourceLocation world;
   private Vector3i blockPosition;

   public WorldBlockPosition(@NotNull ResourceLocation world, @NotNull Vector3i blockPosition) {
      this.world = world;
      this.blockPosition = blockPosition;
   }

   public WorldBlockPosition(@NotNull ResourceLocation world, int x, int y, int z) {
      this.world = world;
      this.blockPosition = new Vector3i(x, y, z);
   }

   public WorldBlockPosition(@NotNull Dimension dimension, @NotNull Vector3i blockPosition) {
      this.world = new ResourceLocation(dimension.getDimensionName());
      this.blockPosition = blockPosition;
   }

   public ResourceLocation getWorld() {
      return this.world;
   }

   public void setWorld(ResourceLocation world) {
      this.world = world;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }
}
