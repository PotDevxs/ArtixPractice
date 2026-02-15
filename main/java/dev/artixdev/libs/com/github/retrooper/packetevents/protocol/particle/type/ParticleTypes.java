package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.type;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleBlockStateData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleDustColorTransitionData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleItemStackData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleSculkChargeData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleShriekData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleVibrationData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilder;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilderData;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleTypes {
   private static final Map<String, ParticleType> PARTICLE_TYPE_MAP = new HashMap();
   private static final Map<Byte, Map<Integer, ParticleType>> PARTICLE_TYPE_ID_MAP = new HashMap();
   private static final TypesBuilder TYPES_BUILDER;
   public static final ParticleType AMBIENT_ENTITY_EFFECT;
   public static final ParticleType ANGRY_VILLAGER;
   public static final ParticleType BLOCK;
   public static final ParticleType BLOCK_MARKER;
   public static final ParticleType BUBBLE;
   public static final ParticleType CLOUD;
   public static final ParticleType CRIT;
   public static final ParticleType DAMAGE_INDICATOR;
   public static final ParticleType DRAGON_BREATH;
   public static final ParticleType DRIPPING_LAVA;
   public static final ParticleType FALLING_LAVA;
   public static final ParticleType LANDING_LAVA;
   public static final ParticleType DRIPPING_WATER;
   public static final ParticleType FALLING_WATER;
   public static final ParticleType DUST;
   public static final ParticleType DUST_COLOR_TRANSITION;
   public static final ParticleType EFFECT;
   public static final ParticleType ELDER_GUARDIAN;
   public static final ParticleType ENCHANTED_HIT;
   public static final ParticleType ENCHANT;
   public static final ParticleType END_ROD;
   public static final ParticleType ENTITY_EFFECT;
   public static final ParticleType EXPLOSION_EMITTER;
   public static final ParticleType EXPLOSION;
   public static final ParticleType SONIC_BOOM;
   public static final ParticleType FALLING_DUST;
   public static final ParticleType FIREWORK;
   public static final ParticleType FISHING;
   public static final ParticleType FLAME;
   public static final ParticleType SCULK_SOUL;
   public static final ParticleType SCULK_CHARGE;
   public static final ParticleType SCULK_CHARGE_POP;
   public static final ParticleType SOUL_FIRE_FLAME;
   public static final ParticleType SOUL;
   public static final ParticleType FLASH;
   public static final ParticleType HAPPY_VILLAGER;
   public static final ParticleType COMPOSTER;
   public static final ParticleType HEART;
   public static final ParticleType INSTANT_EFFECT;
   public static final ParticleType ITEM;
   public static final ParticleType VIBRATION;
   public static final ParticleType ITEM_SLIME;
   public static final ParticleType ITEM_SNOWBALL;
   public static final ParticleType LARGE_SMOKE;
   public static final ParticleType LAVA;
   public static final ParticleType MYCELIUM;
   public static final ParticleType NOTE;
   public static final ParticleType POOF;
   public static final ParticleType PORTAL;
   public static final ParticleType RAIN;
   public static final ParticleType SMOKE;
   public static final ParticleType SNEEZE;
   public static final ParticleType SPIT;
   public static final ParticleType SQUID_INK;
   public static final ParticleType SWEEP_ATTACK;
   public static final ParticleType TOTEM_OF_UNDYING;
   public static final ParticleType UNDERWATER;
   public static final ParticleType SPLASH;
   public static final ParticleType WITCH;
   public static final ParticleType BUBBLE_POP;
   public static final ParticleType CURRENT_DOWN;
   public static final ParticleType BUBBLE_COLUMN_UP;
   public static final ParticleType NAUTILUS;
   public static final ParticleType DOLPHIN;
   public static final ParticleType CAMPFIRE_COSY_SMOKE;
   public static final ParticleType CAMPFIRE_SIGNAL_SMOKE;
   public static final ParticleType DRIPPING_HONEY;
   public static final ParticleType FALLING_HONEY;
   public static final ParticleType LANDING_HONEY;
   public static final ParticleType FALLING_NECTAR;
   public static final ParticleType FALLING_SPORE_BLOSSOM;
   public static final ParticleType ASH;
   public static final ParticleType CRIMSON_SPORE;
   public static final ParticleType WARPED_SPORE;
   public static final ParticleType SPORE_BLOSSOM_AIR;
   public static final ParticleType DRIPPING_OBSIDIAN_TEAR;
   public static final ParticleType FALLING_OBSIDIAN_TEAR;
   public static final ParticleType LANDING_OBSIDIAN_TEAR;
   public static final ParticleType REVERSE_PORTAL;
   public static final ParticleType WHITE_ASH;
   public static final ParticleType SMALL_FLAME;
   public static final ParticleType SNOWFLAKE;
   public static final ParticleType DRIPPING_DRIPSTONE_LAVA;
   public static final ParticleType FALLING_DRIPSTONE_LAVA;
   public static final ParticleType DRIPPING_DRIPSTONE_WATER;
   public static final ParticleType FALLING_DRIPSTONE_WATER;
   public static final ParticleType GLOW_SQUID_INK;
   public static final ParticleType GLOW;
   public static final ParticleType WAX_ON;
   public static final ParticleType WAX_OFF;
   public static final ParticleType ELECTRIC_SPARK;
   public static final ParticleType SCRAPE;
   public static final ParticleType SHRIEK;
   public static final ParticleType DRIPPING_CHERRY_LEAVES;
   public static final ParticleType FALLING_CHERRY_LEAVES;
   public static final ParticleType LANDING_CHERRY_LEAVES;
   public static final ParticleType CHERRY_LEAVES;
   public static final ParticleType EGG_CRACK;
   public static final ParticleType GUST;
   public static final ParticleType GUST_EMITTER;
   public static final ParticleType WHITE_SMOKE;
   public static final ParticleType DUST_PLUME;
   public static final ParticleType GUST_DUST;
   public static final ParticleType TRIAL_SPAWNER_DETECTION;

   public static ParticleType define(String key, final Function<PacketWrapper<?>, ParticleData> readDataFunction, final BiConsumer<PacketWrapper<?>, ParticleData> writeDataFunction) {
      final TypesBuilderData data = TYPES_BUILDER.defineFromArray(key);
      ParticleType particleType = new ParticleType() {
         private final int[] ids = data.getData();

         public ResourceLocation getName() {
            return data.getName();
         }

         public int getId(ClientVersion version) {
            int index = ParticleTypes.TYPES_BUILDER.getDataIndex(version);
            return this.ids[index];
         }

         public Function<PacketWrapper<?>, ParticleData> readDataFunction() {
            return readDataFunction;
         }

         public BiConsumer<PacketWrapper<?>, ParticleData> writeDataFunction() {
            return writeDataFunction;
         }

         public boolean equals(Object obj) {
            return obj instanceof ParticleType ? this.getName().equals(((ParticleType)obj).getName()) : false;
         }
      };
      PARTICLE_TYPE_MAP.put(particleType.getName().toString(), particleType);
      ClientVersion[] var5 = TYPES_BUILDER.getVersions();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ClientVersion version = var5[var7];
         int index = TYPES_BUILDER.getDataIndex(version);
         Map<Integer, ParticleType> typeIdMap = (Map)PARTICLE_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
            return new HashMap();
         });
         typeIdMap.put(particleType.getId(version), particleType);
      }

      return particleType;
   }

   public static ParticleType define(String key) {
      return define(key, (wrapper) -> {
         return new ParticleData();
      }, (wrapper, data) -> {
      });
   }

   public static ParticleType getByName(String name) {
      return (ParticleType)PARTICLE_TYPE_MAP.get(name);
   }

   public static ParticleType getById(ClientVersion version, int id) {
      int index = TYPES_BUILDER.getDataIndex(version);
      Map<Integer, ParticleType> typeIdMap = (Map)PARTICLE_TYPE_ID_MAP.get((byte)index);
      return (ParticleType)typeIdMap.get(id);
   }

   static {
      TYPES_BUILDER = new TypesBuilder("particle/particle_type_mappings", new ClientVersion[]{ClientVersion.V_1_12_2, ClientVersion.V_1_13, ClientVersion.V_1_13_2, ClientVersion.V_1_14, ClientVersion.V_1_15, ClientVersion.V_1_16, ClientVersion.V_1_16_2, ClientVersion.V_1_17, ClientVersion.V_1_18, ClientVersion.V_1_19, ClientVersion.V_1_19_4, ClientVersion.V_1_20, ClientVersion.V_1_20_3});
      AMBIENT_ENTITY_EFFECT = define("ambient_entity_effect");
      ANGRY_VILLAGER = define("angry_villager");
      BLOCK = define("block", ParticleBlockStateData::read, (wrapper, data) -> {
         ParticleBlockStateData.write(wrapper, (ParticleBlockStateData)data);
      });
      BLOCK_MARKER = define("block_marker", ParticleBlockStateData::read, (wrapper, data) -> {
         ParticleBlockStateData.write(wrapper, (ParticleBlockStateData)data);
      });
      BUBBLE = define("bubble");
      CLOUD = define("cloud");
      CRIT = define("crit");
      DAMAGE_INDICATOR = define("damage_indicator");
      DRAGON_BREATH = define("dragon_breath");
      DRIPPING_LAVA = define("dripping_lava");
      FALLING_LAVA = define("falling_lava");
      LANDING_LAVA = define("landing_lava");
      DRIPPING_WATER = define("dripping_water");
      FALLING_WATER = define("falling_water");
      DUST = define("dust", ParticleDustData::read, (wrapper, data) -> {
         ParticleDustData.write(wrapper, (ParticleDustData)data);
      });
      DUST_COLOR_TRANSITION = define("dust_color_transition", ParticleDustColorTransitionData::read, (wrapper, data) -> {
         ParticleDustColorTransitionData.write(wrapper, (ParticleDustColorTransitionData)data);
      });
      EFFECT = define("effect");
      ELDER_GUARDIAN = define("elder_guardian");
      ENCHANTED_HIT = define("enchanted_hit");
      ENCHANT = define("enchant");
      END_ROD = define("end_rod");
      ENTITY_EFFECT = define("entity_effect");
      EXPLOSION_EMITTER = define("explosion_emitter");
      EXPLOSION = define("explosion");
      SONIC_BOOM = define("sonic_boom");
      FALLING_DUST = define("falling_dust", ParticleBlockStateData::read, (wrapper, data) -> {
         ParticleBlockStateData.write(wrapper, (ParticleBlockStateData)data);
      });
      FIREWORK = define("firework");
      FISHING = define("fishing");
      FLAME = define("flame");
      SCULK_SOUL = define("sculk_soul");
      SCULK_CHARGE = define("sculk_charge", ParticleSculkChargeData::read, (wrapper, data) -> {
         ParticleSculkChargeData.write(wrapper, (ParticleSculkChargeData)data);
      });
      SCULK_CHARGE_POP = define("sculk_charge_pop");
      SOUL_FIRE_FLAME = define("soul_fire_flame");
      SOUL = define("soul");
      FLASH = define("flash");
      HAPPY_VILLAGER = define("happy_villager");
      COMPOSTER = define("composter");
      HEART = define("heart");
      INSTANT_EFFECT = define("instant_effect");
      ITEM = define("item", ParticleItemStackData::read, (wrapper, data) -> {
         ParticleItemStackData.write(wrapper, (ParticleItemStackData)data);
      });
      VIBRATION = define("vibration", ParticleVibrationData::read, (wrapper, data) -> {
         ParticleVibrationData.write(wrapper, (ParticleVibrationData)data);
      });
      ITEM_SLIME = define("item_slime");
      ITEM_SNOWBALL = define("item_snowball");
      LARGE_SMOKE = define("large_smoke");
      LAVA = define("lava");
      MYCELIUM = define("mycelium");
      NOTE = define("note");
      POOF = define("poof");
      PORTAL = define("portal");
      RAIN = define("rain");
      SMOKE = define("smoke");
      SNEEZE = define("sneeze");
      SPIT = define("spit");
      SQUID_INK = define("squid_ink");
      SWEEP_ATTACK = define("sweep_attack");
      TOTEM_OF_UNDYING = define("totem_of_undying");
      UNDERWATER = define("underwater");
      SPLASH = define("splash");
      WITCH = define("witch");
      BUBBLE_POP = define("bubble_pop");
      CURRENT_DOWN = define("current_down");
      BUBBLE_COLUMN_UP = define("bubble_column_up");
      NAUTILUS = define("nautilus");
      DOLPHIN = define("dolphin");
      CAMPFIRE_COSY_SMOKE = define("campfire_cosy_smoke");
      CAMPFIRE_SIGNAL_SMOKE = define("campfire_signal_smoke");
      DRIPPING_HONEY = define("dripping_honey");
      FALLING_HONEY = define("falling_honey");
      LANDING_HONEY = define("landing_honey");
      FALLING_NECTAR = define("falling_nectar");
      FALLING_SPORE_BLOSSOM = define("falling_spore_blossom");
      ASH = define("ash");
      CRIMSON_SPORE = define("crimson_spore");
      WARPED_SPORE = define("warped_spore");
      SPORE_BLOSSOM_AIR = define("spore_blossom_air");
      DRIPPING_OBSIDIAN_TEAR = define("dripping_obsidian_tear");
      FALLING_OBSIDIAN_TEAR = define("falling_obsidian_tear");
      LANDING_OBSIDIAN_TEAR = define("landing_obsidian_tear");
      REVERSE_PORTAL = define("reverse_portal");
      WHITE_ASH = define("white_ash");
      SMALL_FLAME = define("small_flame");
      SNOWFLAKE = define("snowflake");
      DRIPPING_DRIPSTONE_LAVA = define("dripping_dripstone_lava");
      FALLING_DRIPSTONE_LAVA = define("falling_dripstone_lava");
      DRIPPING_DRIPSTONE_WATER = define("dripping_dripstone_water");
      FALLING_DRIPSTONE_WATER = define("falling_dripstone_water");
      GLOW_SQUID_INK = define("glow_squid_ink");
      GLOW = define("glow");
      WAX_ON = define("wax_on");
      WAX_OFF = define("wax_off");
      ELECTRIC_SPARK = define("electric_spark");
      SCRAPE = define("scrape");
      SHRIEK = define("shriek", ParticleShriekData::read, (wrapper, data) -> {
         ParticleShriekData.write(wrapper, (ParticleShriekData)data);
      });
      DRIPPING_CHERRY_LEAVES = define("dripping_cherry_leaves");
      FALLING_CHERRY_LEAVES = define("falling_cherry_leaves");
      LANDING_CHERRY_LEAVES = define("landing_cherry_leaves");
      CHERRY_LEAVES = define("cherry_leaves");
      EGG_CRACK = define("egg_crack");
      GUST = define("gust");
      GUST_EMITTER = define("gust_emitter");
      WHITE_SMOKE = define("white_smoke");
      DUST_PLUME = define("dust_plume");
      GUST_DUST = define("gust_dust");
      TRIAL_SPAWNER_DETECTION = define("trial_spawner_detection");
      TYPES_BUILDER.unloadFileMappings();
   }
}
