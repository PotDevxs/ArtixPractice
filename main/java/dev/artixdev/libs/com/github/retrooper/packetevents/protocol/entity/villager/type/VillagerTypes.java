package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.type;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;

public class VillagerTypes {
   private static final Map<String, VillagerType> VILLAGER_TYPE_MAP = new HashMap();
   private static final Map<Byte, VillagerType> VILLAGER_TYPE_ID_MAP = new HashMap();
   public static final VillagerType DESERT = define(0, "minecraft:desert");
   public static final VillagerType JUNGLE = define(1, "minecraft:jungle");
   public static final VillagerType PLAINS = define(2, "minecraft:plains");
   public static final VillagerType SAVANNA = define(3, "minecraft:savanna");
   public static final VillagerType SNOW = define(4, "minecraft:snow");
   public static final VillagerType SWAMP = define(5, "minecraft:swamp");
   public static final VillagerType TAIGA = define(6, "minecraft:taiga");

   public static VillagerType define(final int id, String name) {
      final ResourceLocation location = new ResourceLocation(name);
      VillagerType type = new VillagerType() {
         public ResourceLocation getName() {
            return location;
         }

         public int getId() {
            return id;
         }
      };
      VILLAGER_TYPE_MAP.put(type.getName().toString(), type);
      VILLAGER_TYPE_ID_MAP.put((byte)type.getId(), type);
      return type;
   }

   public static VillagerType getById(int id) {
      return (VillagerType)VILLAGER_TYPE_ID_MAP.get((byte)id);
   }

   public static VillagerType getByName(String name) {
      return (VillagerType)VILLAGER_TYPE_MAP.get(name);
   }
}
