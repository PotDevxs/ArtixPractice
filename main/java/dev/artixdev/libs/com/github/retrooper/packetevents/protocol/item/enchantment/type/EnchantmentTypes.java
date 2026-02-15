package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.enchantment.type;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilder;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilderData;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class EnchantmentTypes {
   private static final Map<String, EnchantmentType> ENCHANTMENT_TYPE_MAPPINGS = new HashMap();
   private static final Map<Byte, Map<Integer, EnchantmentType>> ENCHANTMENT_TYPE_ID_MAPPINGS = new HashMap();
   private static final TypesBuilder TYPES_BUILDER;
   public static final EnchantmentType ALL_DAMAGE_PROTECTION;
   public static final EnchantmentType FIRE_PROTECTION;
   public static final EnchantmentType FALL_PROTECTION;
   public static final EnchantmentType BLAST_PROTECTION;
   public static final EnchantmentType PROJECTILE_PROTECTION;
   public static final EnchantmentType RESPIRATION;
   public static final EnchantmentType AQUA_AFFINITY;
   public static final EnchantmentType THORNS;
   public static final EnchantmentType DEPTH_STRIDER;
   public static final EnchantmentType FROST_WALKER;
   public static final EnchantmentType BINDING_CURSE;
   public static final EnchantmentType SOUL_SPEED;
   public static final EnchantmentType SWIFT_SNEAK;
   public static final EnchantmentType SHARPNESS;
   public static final EnchantmentType SMITE;
   public static final EnchantmentType BANE_OF_ARTHROPODS;
   public static final EnchantmentType KNOCKBACK;
   public static final EnchantmentType FIRE_ASPECT;
   public static final EnchantmentType MOB_LOOTING;
   public static final EnchantmentType SWEEPING_EDGE;
   public static final EnchantmentType BLOCK_EFFICIENCY;
   public static final EnchantmentType SILK_TOUCH;
   public static final EnchantmentType UNBREAKING;
   public static final EnchantmentType BLOCK_FORTUNE;
   public static final EnchantmentType POWER_ARROWS;
   public static final EnchantmentType PUNCH_ARROWS;
   public static final EnchantmentType FLAMING_ARROWS;
   public static final EnchantmentType INFINITY_ARROWS;
   public static final EnchantmentType FISHING_LUCK;
   public static final EnchantmentType FISHING_SPEED;
   public static final EnchantmentType LOYALTY;
   public static final EnchantmentType IMPALING;
   public static final EnchantmentType RIPTIDE;
   public static final EnchantmentType CHANNELING;
   public static final EnchantmentType MULTISHOT;
   public static final EnchantmentType QUICK_CHARGE;
   public static final EnchantmentType PIERCING;
   public static final EnchantmentType MENDING;
   public static final EnchantmentType VANISHING_CURSE;

   public static EnchantmentType define(String key) {
      final TypesBuilderData data = TYPES_BUILDER.define(key);
      EnchantmentType enchantmentType = new EnchantmentType() {
         private final int[] ids = data.getData();

         public ResourceLocation getName() {
            return data.getName();
         }

         public int getId(ClientVersion version) {
            int index = EnchantmentTypes.TYPES_BUILDER.getDataIndex(version);
            return this.ids[index];
         }

         public boolean equals(Object obj) {
            if (obj instanceof EnchantmentType) {
               return this.getName() == ((EnchantmentType)obj).getName();
            } else {
               return false;
            }
         }
      };
      ENCHANTMENT_TYPE_MAPPINGS.put(enchantmentType.getName().toString(), enchantmentType);
      ClientVersion[] var3 = TYPES_BUILDER.getVersions();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ClientVersion version = var3[var5];
         int index = TYPES_BUILDER.getDataIndex(version);
         Map<Integer, EnchantmentType> typeIdMap = (Map)ENCHANTMENT_TYPE_ID_MAPPINGS.computeIfAbsent((byte)index, (k) -> {
            return new HashMap();
         });
         typeIdMap.put(enchantmentType.getId(version), enchantmentType);
      }

      return enchantmentType;
   }

   @Nullable
   public static EnchantmentType getByName(String name) {
      return (EnchantmentType)ENCHANTMENT_TYPE_MAPPINGS.get(name);
   }

   @Nullable
   public static EnchantmentType getById(ClientVersion version, int id) {
      int index = TYPES_BUILDER.getDataIndex(version);
      Map<Integer, EnchantmentType> typeIdMap = (Map)ENCHANTMENT_TYPE_ID_MAPPINGS.get((byte)index);
      return (EnchantmentType)typeIdMap.get(id);
   }

   static {
      TYPES_BUILDER = new TypesBuilder("enchantment/enchantment_type_mappings", new ClientVersion[]{ClientVersion.V_1_12, ClientVersion.V_1_13, ClientVersion.V_1_14, ClientVersion.V_1_16, ClientVersion.V_1_19});
      ALL_DAMAGE_PROTECTION = define("protection");
      FIRE_PROTECTION = define("fire_protection");
      FALL_PROTECTION = define("feather_falling");
      BLAST_PROTECTION = define("blast_protection");
      PROJECTILE_PROTECTION = define("projectile_protection");
      RESPIRATION = define("respiration");
      AQUA_AFFINITY = define("aqua_affinity");
      THORNS = define("thorns");
      DEPTH_STRIDER = define("depth_strider");
      FROST_WALKER = define("frost_walker");
      BINDING_CURSE = define("binding_curse");
      SOUL_SPEED = define("soul_speed");
      SWIFT_SNEAK = define("swift_sneak");
      SHARPNESS = define("sharpness");
      SMITE = define("smite");
      BANE_OF_ARTHROPODS = define("bane_of_arthropods");
      KNOCKBACK = define("knockback");
      FIRE_ASPECT = define("fire_aspect");
      MOB_LOOTING = define("looting");
      SWEEPING_EDGE = define("sweeping");
      BLOCK_EFFICIENCY = define("efficiency");
      SILK_TOUCH = define("silk_touch");
      UNBREAKING = define("unbreaking");
      BLOCK_FORTUNE = define("fortune");
      POWER_ARROWS = define("power");
      PUNCH_ARROWS = define("punch");
      FLAMING_ARROWS = define("flame");
      INFINITY_ARROWS = define("infinity");
      FISHING_LUCK = define("luck_of_the_sea");
      FISHING_SPEED = define("lure");
      LOYALTY = define("loyalty");
      IMPALING = define("impaling");
      RIPTIDE = define("riptide");
      CHANNELING = define("channeling");
      MULTISHOT = define("multishot");
      QUICK_CHARGE = define("quick_charge");
      PIERCING = define("piercing");
      MENDING = define("mending");
      VANISHING_CURSE = define("vanishing_curse");
      TYPES_BUILDER.unloadFileMappings();
   }
}
