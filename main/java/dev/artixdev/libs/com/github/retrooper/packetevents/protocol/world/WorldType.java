package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world;

import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public enum WorldType {
   DEFAULT("default"),
   FLAT("flat"),
   LARGE_BIOMES("largeBiomes"),
   AMPLIFIED("amplified"),
   CUSTOMIZED("customized"),
   BUFFET("buffet"),
   DEBUG_ALL_BLOCK_STATES("debug_all_block_states"),
   DEFAULT_1_1("default_1_1");

   private static final WorldType[] VALUES = values();
   private final String name;

   private WorldType(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   @Nullable
   public static WorldType getByName(String name) {
      for (WorldType type : VALUES) {
         if (type.name.equals(name)) {
            return type;
         }
      }
      return null;
   }

   // $FF: synthetic method
   private static WorldType[] $values() {
      return new WorldType[]{DEFAULT, FLAT, LARGE_BIOMES, AMPLIFIED, CUSTOMIZED, BUFFET, DEBUG_ALL_BLOCK_STATES, DEFAULT_1_1};
   }
}
