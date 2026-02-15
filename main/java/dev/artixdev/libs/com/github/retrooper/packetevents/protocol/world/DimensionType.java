package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world;

import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

/** @deprecated */
@Deprecated
public enum DimensionType {
   NETHER(-1, "minecraft:the_nether"),
   OVERWORLD(0, "minecraft:overworld"),
   END(1, "minecraft:the_end"),
   CUSTOM(-999, "minecraft:custom");

   private static final DimensionType[] VALUES = values();
   private final int id;
   private final String name;

   private DimensionType(int id, String name) {
      this.id = id;
      this.name = name;
   }

   public static DimensionType getById(int id) {
      return VALUES[id + 1];
   }

   @Nullable
   public static DimensionType getByName(String name) {
      DimensionType[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         DimensionType type = var1[var3];
         if (type.name.equals(name)) {
            return type;
         }
      }

      return null;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public static boolean isFlat(String levelType) {
      return WorldType.FLAT.getName().equals(levelType);
   }

   public static boolean isDebug(String levelType) {
      return WorldType.DEBUG_ALL_BLOCK_STATES.getName().equals(levelType);
   }

   // $FF: synthetic method
   private static DimensionType[] $values() {
      return new DimensionType[]{NETHER, OVERWORLD, END, CUSTOM};
   }
}
