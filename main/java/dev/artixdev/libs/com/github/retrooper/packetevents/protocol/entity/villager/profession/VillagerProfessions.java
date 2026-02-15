package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.profession;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;

public class VillagerProfessions {
   private static final Map<String, VillagerProfession> VILLAGER_PROFESSION_MAP = new HashMap();
   private static final Map<Byte, VillagerProfession> VILLAGER_PROFESSION_ID_MAP = new HashMap();
   public static final VillagerProfession NONE = define(0, "minecraft:none");
   public static final VillagerProfession ARMORER = define(1, "minecraft:armorer");
   public static final VillagerProfession BUTCHER = define(2, "minecraft:butcher");
   public static final VillagerProfession CARTOGRAPHER = define(3, "minecraft:cartographer");
   public static final VillagerProfession CLERIC = define(4, "minecraft:cleric");
   public static final VillagerProfession FARMER = define(5, "minecraft:farmer");
   public static final VillagerProfession FISHERMAN = define(6, "minecraft:fisherman");
   public static final VillagerProfession FLETCHER = define(7, "minecraft:fletcher");
   public static final VillagerProfession LEATHERWORKER = define(8, "minecraft:leatherworker");
   public static final VillagerProfession LIBRARIAN = define(9, "minecraft:librarian");
   public static final VillagerProfession MASON = define(10, "minecraft:mason");
   public static final VillagerProfession NITWIT = define(11, "minecraft:nitwit");
   public static final VillagerProfession SHEPHERD = define(12, "minecraft:shepherd");
   public static final VillagerProfession TOOLSMITH = define(13, "minecraft:toolsmith");
   public static final VillagerProfession WEAPONSMITH = define(14, "minecraft:weaponsmith");

   public static VillagerProfession define(final int id, String name) {
      final ResourceLocation location = new ResourceLocation(name);
      VillagerProfession type = new VillagerProfession() {
         public ResourceLocation getName() {
            return location;
         }

         public int getId() {
            return id;
         }
      };
      VILLAGER_PROFESSION_MAP.put(type.getName().toString(), type);
      VILLAGER_PROFESSION_ID_MAP.put((byte)type.getId(), type);
      return type;
   }

   public static VillagerProfession getById(int id) {
      return (VillagerProfession)VILLAGER_PROFESSION_ID_MAP.get((byte)id);
   }

   public static VillagerProfession getByName(String name) {
      return (VillagerProfession)VILLAGER_PROFESSION_MAP.get(name);
   }
}
