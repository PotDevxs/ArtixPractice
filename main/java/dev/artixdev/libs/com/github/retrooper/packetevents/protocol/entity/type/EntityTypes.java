package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilder;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilderData;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class EntityTypes {
   private static final Map<String, EntityType> ENTITY_TYPE_MAP = new HashMap();
   private static final Map<Byte, Map<Integer, EntityType>> ENTITY_TYPE_ID_MAP = new HashMap();
   private static final Map<Byte, Map<Integer, EntityType>> LEGACY_ENTITY_TYPE_ID_MAP = new HashMap();
   private static final TypesBuilder TYPES_BUILDER;
   private static final TypesBuilder LEGACY_TYPES_BUILDER;
   public static final EntityType ENTITY;
   public static final EntityType LIVINGENTITY;
   public static final EntityType ABSTRACT_INSENTIENT;
   public static final EntityType ABSTRACT_CREATURE;
   public static final EntityType ABSTRACT_AGEABLE;
   public static final EntityType ABSTRACT_ANIMAL;
   public static final EntityType ABSTRACT_TAMEABLE_ANIMAL;
   public static final EntityType ABSTRACT_PARROT;
   public static final EntityType ABSTRACT_HORSE;
   public static final EntityType CHESTED_HORSE;
   public static final EntityType ABSTRACT_GOLEM;
   public static final EntityType ABSTRACT_FISHES;
   public static final EntityType ABSTRACT_MONSTER;
   public static final EntityType ABSTRACT_PIGLIN;
   public static final EntityType ABSTRACT_ILLAGER_BASE;
   public static final EntityType ABSTRACT_EVO_ILLU_ILLAGER;
   public static final EntityType ABSTRACT_SKELETON;
   public static final EntityType ABSTRACT_FLYING;
   public static final EntityType ABSTRACT_AMBIENT;
   public static final EntityType ABSTRACT_WATERMOB;
   public static final EntityType ABSTRACT_HANGING;
   public static final EntityType ABSTRACT_LIGHTNING;
   public static final EntityType ABSTRACT_ARROW;
   public static final EntityType ABSTRACT_FIREBALL;
   public static final EntityType PROJECTILE_ABSTRACT;
   public static final EntityType MINECART_ABSTRACT;
   public static final EntityType CHESTED_MINECART_ABSTRACT;
   public static final EntityType AREA_EFFECT_CLOUD;
   public static final EntityType ARMOR_STAND;
   public static final EntityType ALLAY;
   public static final EntityType ARROW;
   public static final EntityType AXOLOTL;
   public static final EntityType BAT;
   public static final EntityType BEE;
   public static final EntityType BLAZE;
   public static final EntityType BOAT;
   public static final EntityType CHEST_BOAT;
   public static final EntityType CAT;
   public static final EntityType CAMEL;
   public static final EntityType SPIDER;
   public static final EntityType CAVE_SPIDER;
   public static final EntityType CHICKEN;
   public static final EntityType COD;
   public static final EntityType COW;
   public static final EntityType CREEPER;
   public static final EntityType DOLPHIN;
   public static final EntityType DONKEY;
   public static final EntityType DRAGON_FIREBALL;
   public static final EntityType ZOMBIE;
   public static final EntityType DROWNED;
   public static final EntityType GUARDIAN;
   public static final EntityType ELDER_GUARDIAN;
   public static final EntityType END_CRYSTAL;
   public static final EntityType ENDER_DRAGON;
   public static final EntityType ENDERMAN;
   public static final EntityType ENDERMITE;
   public static final EntityType EVOKER;
   public static final EntityType EVOKER_FANGS;
   public static final EntityType EXPERIENCE_ORB;
   public static final EntityType EYE_OF_ENDER;
   public static final EntityType FALLING_BLOCK;
   public static final EntityType FIREWORK_ROCKET;
   public static final EntityType FOX;
   public static final EntityType FROG;
   public static final EntityType GHAST;
   public static final EntityType GIANT;
   public static final EntityType ITEM_FRAME;
   public static final EntityType GLOW_ITEM_FRAME;
   public static final EntityType SQUID;
   public static final EntityType GLOW_SQUID;
   public static final EntityType GOAT;
   public static final EntityType HOGLIN;
   public static final EntityType HORSE;
   public static final EntityType HUSK;
   public static final EntityType ILLUSIONER;
   public static final EntityType IRON_GOLEM;
   public static final EntityType ITEM;
   public static final EntityType FIREBALL;
   public static final EntityType LEASH_KNOT;
   public static final EntityType LIGHTNING_ARTIX;
   public static final EntityType LLAMA;
   public static final EntityType LLAMA_SPIT;
   public static final EntityType SLIME;
   public static final EntityType MAGMA_CUBE;
   public static final EntityType MARKER;
   public static final EntityType MINECART;
   public static final EntityType CHEST_MINECART;
   public static final EntityType COMMAND_BLOCK_MINECART;
   public static final EntityType FURNACE_MINECART;
   public static final EntityType HOPPER_MINECART;
   public static final EntityType SPAWNER_MINECART;
   public static final EntityType TNT_MINECART;
   public static final EntityType MULE;
   public static final EntityType MOOSHROOM;
   public static final EntityType OCELOT;
   public static final EntityType PAINTING;
   public static final EntityType PANDA;
   public static final EntityType PARROT;
   public static final EntityType PHANTOM;
   public static final EntityType PIG;
   public static final EntityType PIGLIN;
   public static final EntityType PIGLIN_BRUTE;
   public static final EntityType PILLAGER;
   public static final EntityType POLAR_BEAR;
   public static final EntityType TNT;
   public static final EntityType PUFFERFISH;
   public static final EntityType RABBIT;
   public static final EntityType RAVAGER;
   public static final EntityType SALMON;
   public static final EntityType SHEEP;
   public static final EntityType SHULKER;
   public static final EntityType SHULKER_BULLET;
   public static final EntityType SILVERFISH;
   public static final EntityType SKELETON;
   public static final EntityType SKELETON_HORSE;
   public static final EntityType SMALL_FIREBALL;
   public static final EntityType SNOW_GOLEM;
   public static final EntityType SNOWBALL;
   public static final EntityType SPECTRAL_ARROW;
   public static final EntityType STRAY;
   public static final EntityType STRIDER;
   public static final EntityType EGG;
   public static final EntityType ENDER_PEARL;
   public static final EntityType EXPERIENCE_BOTTLE;
   public static final EntityType POTION;
   public static final EntityType TADPOLE;
   /** @deprecated */
   @Deprecated
   public static final EntityType TIPPED_ARROW;
   public static final EntityType TRIDENT;
   public static final EntityType TRADER_LLAMA;
   public static final EntityType TROPICAL_FISH;
   public static final EntityType TURTLE;
   public static final EntityType VEX;
   public static final EntityType VILLAGER;
   public static final EntityType VINDICATOR;
   public static final EntityType WANDERING_TRADER;
   public static final EntityType WARDEN;
   public static final EntityType WITCH;
   public static final EntityType WITHER;
   public static final EntityType WITHER_SKELETON;
   public static final EntityType WITHER_SKULL;
   public static final EntityType WOLF;
   public static final EntityType ZOGLIN;
   public static final EntityType ZOMBIE_HORSE;
   public static final EntityType ZOMBIE_VILLAGER;
   public static final EntityType ZOMBIFIED_PIGLIN;
   public static final EntityType PLAYER;
   public static final EntityType FISHING_BOBBER;
   public static final EntityType ENDER_SIGNAL;
   public static final EntityType THROWN_EXP_BOTTLE;
   public static final EntityType PRIMED_TNT;
   public static final EntityType FIREWORK;
   public static final EntityType MINECART_COMMAND;
   public static final EntityType MINECART_RIDEABLE;
   public static final EntityType MINECART_CHEST;
   public static final EntityType MINECART_FURNACE;
   public static final EntityType MINECART_TNT;
   public static final EntityType MINECART_HOPPER;
   public static final EntityType MINECART_MOB_SPAWNER;
   public static final EntityType DISPLAY;
   public static final EntityType BLOCK_DISPLAY;
   public static final EntityType ITEM_DISPLAY;
   public static final EntityType TEXT_DISPLAY;
   public static final EntityType INTERACTION;
   public static final EntityType SNIFFER;
   public static final EntityType BREEZE;
   public static final EntityType WIND_CHARGE;

   public static EntityType define(String key, @Nullable EntityType parent) {
      final TypesBuilderData data = TYPES_BUILDER.define(key);
      final TypesBuilderData legacyData = LEGACY_TYPES_BUILDER.define(key);
      final Optional<EntityType> optParent = Optional.ofNullable(parent);
      EntityType entityType = new EntityType() {
         private final int[] ids = data.getData();
         private final int[] legacyIds = legacyData.getData();

         public Optional<EntityType> getParent() {
            return optParent;
         }

         public int getLegacyId(ClientVersion version) {
            if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
               return -1;
            } else {
               int index = EntityTypes.LEGACY_TYPES_BUILDER.getDataIndex(version);
               return this.legacyIds[index];
            }
         }

         public ResourceLocation getName() {
            return data.getName();
         }

         public int getId(ClientVersion version) {
            int index = EntityTypes.TYPES_BUILDER.getDataIndex(version);
            return this.ids[index];
         }
      };
      ENTITY_TYPE_MAP.put(entityType.getName().toString(), entityType);
      ClientVersion[] var6 = TYPES_BUILDER.getVersions();
      int var7 = var6.length;

      int var8;
      ClientVersion version;
      int index;
      Map legacyTypeIdMap;
      for(var8 = 0; var8 < var7; ++var8) {
         version = var6[var8];
         index = TYPES_BUILDER.getDataIndex(version);
         legacyTypeIdMap = (Map)ENTITY_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
            return new HashMap();
         });
         legacyTypeIdMap.put(entityType.getId(version), entityType);
      }

      var6 = LEGACY_TYPES_BUILDER.getVersions();
      var7 = var6.length;

      for(var8 = 0; var8 < var7; ++var8) {
         version = var6[var8];
         index = LEGACY_TYPES_BUILDER.getDataIndex(version);
         legacyTypeIdMap = (Map)LEGACY_ENTITY_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
            return new HashMap();
         });
         legacyTypeIdMap.put(entityType.getLegacyId(version), entityType);
      }

      return entityType;
   }

   public static boolean isTypeInstanceOf(EntityType type, EntityType parent) {
      while(true) {
         if (type != null) {
            if (type == parent) {
               return true;
            }

            if (type.getParent().isPresent()) {
               type = (EntityType)type.getParent().get();
               continue;
            }

            return false;
         }

         return false;
      }
   }

   public static EntityType getByName(String name) {
      return (EntityType)ENTITY_TYPE_MAP.get(name);
   }

   public static EntityType getById(ClientVersion version, int id) {
      int index = TYPES_BUILDER.getDataIndex(version);
      return (EntityType)((Map)ENTITY_TYPE_ID_MAP.get((byte)index)).get(id);
   }

   public static EntityType getByLegacyId(ClientVersion version, int id) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
         return null;
      } else {
         int index = LEGACY_TYPES_BUILDER.getDataIndex(version);
         return (EntityType)((Map)LEGACY_ENTITY_TYPE_ID_MAP.get((byte)index)).get(id);
      }
   }

   static {
      TYPES_BUILDER = new TypesBuilder("entity/entity_type_mappings", new ClientVersion[]{ClientVersion.V_1_10, ClientVersion.V_1_11, ClientVersion.V_1_12, ClientVersion.V_1_13, ClientVersion.V_1_14, ClientVersion.V_1_15, ClientVersion.V_1_16, ClientVersion.V_1_16_2, ClientVersion.V_1_17, ClientVersion.V_1_19, ClientVersion.V_1_19_3, ClientVersion.V_1_19_4, ClientVersion.V_1_20_3});
      LEGACY_TYPES_BUILDER = new TypesBuilder("entity/legacy_entity_type_mappings", new ClientVersion[]{ClientVersion.V_1_8, ClientVersion.V_1_9, ClientVersion.V_1_10, ClientVersion.V_1_11, ClientVersion.V_1_12, ClientVersion.V_1_13});
      TYPES_BUILDER.unloadFileMappings();
      LEGACY_TYPES_BUILDER.unloadFileMappings();
      ENTITY = define("entity", (EntityType)null);
      LIVINGENTITY = define("livingentity", ENTITY);
      ABSTRACT_INSENTIENT = define("abstract_insentient", LIVINGENTITY);
      ABSTRACT_CREATURE = define("abstract_creature", ABSTRACT_INSENTIENT);
      ABSTRACT_AGEABLE = define("abstract_ageable", ABSTRACT_CREATURE);
      ABSTRACT_ANIMAL = define("abstract_animal", ABSTRACT_AGEABLE);
      ABSTRACT_TAMEABLE_ANIMAL = define("abstract_tameable_animal", ABSTRACT_ANIMAL);
      ABSTRACT_PARROT = define("abstract_parrot", ABSTRACT_TAMEABLE_ANIMAL);
      ABSTRACT_HORSE = define("abstract_horse", ABSTRACT_ANIMAL);
      CHESTED_HORSE = define("chested_horse", ABSTRACT_HORSE);
      ABSTRACT_GOLEM = define("abstract_golem", ABSTRACT_CREATURE);
      ABSTRACT_FISHES = define("abstract_fishes", ABSTRACT_CREATURE);
      ABSTRACT_MONSTER = define("abstract_monster", ABSTRACT_CREATURE);
      ABSTRACT_PIGLIN = define("abstract_piglin", ABSTRACT_MONSTER);
      ABSTRACT_ILLAGER_BASE = define("abstract_illager_base", ABSTRACT_MONSTER);
      ABSTRACT_EVO_ILLU_ILLAGER = define("abstract_evo_illu_illager", ABSTRACT_ILLAGER_BASE);
      ABSTRACT_SKELETON = define("abstract_skeleton", ABSTRACT_MONSTER);
      ABSTRACT_FLYING = define("abstract_flying", ABSTRACT_INSENTIENT);
      ABSTRACT_AMBIENT = define("abstract_ambient", ABSTRACT_INSENTIENT);
      ABSTRACT_WATERMOB = define("abstract_watermob", ABSTRACT_INSENTIENT);
      ABSTRACT_HANGING = define("abstract_hanging", ENTITY);
      ABSTRACT_LIGHTNING = define("abstract_lightning", ENTITY);
      ABSTRACT_ARROW = define("abstract_arrow", ENTITY);
      ABSTRACT_FIREBALL = define("abstract_fireball", ENTITY);
      PROJECTILE_ABSTRACT = define("projectile_abstract", ENTITY);
      MINECART_ABSTRACT = define("minecart_abstract", ENTITY);
      CHESTED_MINECART_ABSTRACT = define("chested_minecart_abstract", MINECART_ABSTRACT);
      AREA_EFFECT_CLOUD = define("area_effect_cloud", ENTITY);
      ARMOR_STAND = define("armor_stand", LIVINGENTITY);
      ALLAY = define("allay", ABSTRACT_CREATURE);
      ARROW = define("arrow", ABSTRACT_ARROW);
      AXOLOTL = define("axolotl", ABSTRACT_ANIMAL);
      BAT = define("bat", ABSTRACT_AMBIENT);
      BEE = define("bee", ABSTRACT_INSENTIENT);
      BLAZE = define("blaze", ABSTRACT_MONSTER);
      BOAT = define("boat", ENTITY);
      CHEST_BOAT = define("chest_boat", BOAT);
      CAT = define("cat", ABSTRACT_TAMEABLE_ANIMAL);
      CAMEL = define("camel", ABSTRACT_HORSE);
      SPIDER = define("spider", ABSTRACT_MONSTER);
      CAVE_SPIDER = define("cave_spider", SPIDER);
      CHICKEN = define("chicken", ABSTRACT_ANIMAL);
      COD = define("cod", ABSTRACT_FISHES);
      COW = define("cow", ABSTRACT_ANIMAL);
      CREEPER = define("creeper", ABSTRACT_MONSTER);
      DOLPHIN = define("dolphin", ABSTRACT_INSENTIENT);
      DONKEY = define("donkey", CHESTED_HORSE);
      DRAGON_FIREBALL = define("dragon_fireball", ABSTRACT_FIREBALL);
      ZOMBIE = define("zombie", ABSTRACT_MONSTER);
      DROWNED = define("drowned", ZOMBIE);
      GUARDIAN = define("guardian", ABSTRACT_MONSTER);
      ELDER_GUARDIAN = define("elder_guardian", GUARDIAN);
      END_CRYSTAL = define("end_crystal", ENTITY);
      ENDER_DRAGON = define("ender_dragon", ABSTRACT_INSENTIENT);
      ENDERMAN = define("enderman", ABSTRACT_MONSTER);
      ENDERMITE = define("endermite", ABSTRACT_MONSTER);
      EVOKER = define("evoker", ABSTRACT_EVO_ILLU_ILLAGER);
      EVOKER_FANGS = define("evoker_fangs", ENTITY);
      EXPERIENCE_ORB = define("experience_orb", ENTITY);
      EYE_OF_ENDER = define("eye_of_ender", ENTITY);
      FALLING_BLOCK = define("falling_block", ENTITY);
      FIREWORK_ROCKET = define("firework_rocket", ENTITY);
      FOX = define("fox", ABSTRACT_ANIMAL);
      FROG = define("frog", ABSTRACT_ANIMAL);
      GHAST = define("ghast", ABSTRACT_FLYING);
      GIANT = define("giant", ABSTRACT_MONSTER);
      ITEM_FRAME = define("item_frame", ABSTRACT_HANGING);
      GLOW_ITEM_FRAME = define("glow_item_frame", ITEM_FRAME);
      SQUID = define("squid", ABSTRACT_WATERMOB);
      GLOW_SQUID = define("glow_squid", SQUID);
      GOAT = define("goat", ABSTRACT_ANIMAL);
      HOGLIN = define("hoglin", ABSTRACT_ANIMAL);
      HORSE = define("horse", ABSTRACT_HORSE);
      HUSK = define("husk", ZOMBIE);
      ILLUSIONER = define("illusioner", ABSTRACT_EVO_ILLU_ILLAGER);
      IRON_GOLEM = define("iron_golem", ABSTRACT_GOLEM);
      ITEM = define("item", ENTITY);
      FIREBALL = define("fireball", ABSTRACT_FIREBALL);
      LEASH_KNOT = define("leash_knot", ABSTRACT_HANGING);
      LIGHTNING_ARTIX = define("lightning_artix", ABSTRACT_LIGHTNING);
      LLAMA = define("llama", CHESTED_HORSE);
      LLAMA_SPIT = define("llama_spit", ENTITY);
      SLIME = define("slime", ABSTRACT_INSENTIENT);
      MAGMA_CUBE = define("magma_cube", SLIME);
      MARKER = define("marker", ENTITY);
      MINECART = define("minecart", MINECART_ABSTRACT);
      CHEST_MINECART = define("chest_minecart", CHESTED_MINECART_ABSTRACT);
      COMMAND_BLOCK_MINECART = define("command_block_minecart", MINECART_ABSTRACT);
      FURNACE_MINECART = define("furnace_minecart", MINECART_ABSTRACT);
      HOPPER_MINECART = define("hopper_minecart", CHESTED_MINECART_ABSTRACT);
      SPAWNER_MINECART = define("spawner_minecart", MINECART_ABSTRACT);
      TNT_MINECART = define("tnt_minecart", MINECART_ABSTRACT);
      MULE = define("mule", CHESTED_HORSE);
      MOOSHROOM = define("mooshroom", COW);
      OCELOT = define("ocelot", ABSTRACT_TAMEABLE_ANIMAL);
      PAINTING = define("painting", ABSTRACT_HANGING);
      PANDA = define("panda", ABSTRACT_INSENTIENT);
      PARROT = define("parrot", ABSTRACT_PARROT);
      PHANTOM = define("phantom", ABSTRACT_FLYING);
      PIG = define("pig", ABSTRACT_ANIMAL);
      PIGLIN = define("piglin", ABSTRACT_PIGLIN);
      PIGLIN_BRUTE = define("piglin_brute", ABSTRACT_PIGLIN);
      PILLAGER = define("pillager", ABSTRACT_ILLAGER_BASE);
      POLAR_BEAR = define("polar_bear", ABSTRACT_ANIMAL);
      TNT = define("tnt", ENTITY);
      PUFFERFISH = define("pufferfish", ABSTRACT_FISHES);
      RABBIT = define("rabbit", ABSTRACT_ANIMAL);
      RAVAGER = define("ravager", ABSTRACT_MONSTER);
      SALMON = define("salmon", ABSTRACT_FISHES);
      SHEEP = define("sheep", ABSTRACT_ANIMAL);
      SHULKER = define("shulker", ABSTRACT_GOLEM);
      SHULKER_BULLET = define("shulker_bullet", ENTITY);
      SILVERFISH = define("silverfish", ABSTRACT_MONSTER);
      SKELETON = define("skeleton", ABSTRACT_SKELETON);
      SKELETON_HORSE = define("skeleton_horse", ABSTRACT_HORSE);
      SMALL_FIREBALL = define("small_fireball", ABSTRACT_FIREBALL);
      SNOW_GOLEM = define("snow_golem", ABSTRACT_GOLEM);
      SNOWBALL = define("snowball", PROJECTILE_ABSTRACT);
      SPECTRAL_ARROW = define("spectral_arrow", ABSTRACT_ARROW);
      STRAY = define("stray", ABSTRACT_SKELETON);
      STRIDER = define("strider", ABSTRACT_ANIMAL);
      EGG = define("egg", PROJECTILE_ABSTRACT);
      ENDER_PEARL = define("ender_pearl", PROJECTILE_ABSTRACT);
      EXPERIENCE_BOTTLE = define("experience_bottle", PROJECTILE_ABSTRACT);
      POTION = define("potion", PROJECTILE_ABSTRACT);
      TADPOLE = define("tadpole", ABSTRACT_FISHES);
      TIPPED_ARROW = define("tipped_arrow", ARROW);
      TRIDENT = define("trident", ABSTRACT_ARROW);
      TRADER_LLAMA = define("trader_llama", CHESTED_HORSE);
      TROPICAL_FISH = define("tropical_fish", ABSTRACT_FISHES);
      TURTLE = define("turtle", ABSTRACT_ANIMAL);
      VEX = define("vex", ABSTRACT_MONSTER);
      VILLAGER = define("villager", ABSTRACT_AGEABLE);
      VINDICATOR = define("vindicator", ABSTRACT_ILLAGER_BASE);
      WANDERING_TRADER = define("wandering_trader", ABSTRACT_AGEABLE);
      WARDEN = define("warden", ABSTRACT_MONSTER);
      WITCH = define("witch", ABSTRACT_MONSTER);
      WITHER = define("wither", ABSTRACT_MONSTER);
      WITHER_SKELETON = define("wither_skeleton", ABSTRACT_SKELETON);
      WITHER_SKULL = define("wither_skull", ABSTRACT_FIREBALL);
      WOLF = define("wolf", ABSTRACT_TAMEABLE_ANIMAL);
      ZOGLIN = define("zoglin", ABSTRACT_MONSTER);
      ZOMBIE_HORSE = define("zombie_horse", ABSTRACT_HORSE);
      ZOMBIE_VILLAGER = define("zombie_villager", ZOMBIE);
      ZOMBIFIED_PIGLIN = define("zombified_piglin", ZOMBIE);
      PLAYER = define("player", LIVINGENTITY);
      FISHING_BOBBER = define("fishing_bobber", ENTITY);
      ENDER_SIGNAL = define("ender_signal", ENTITY);
      THROWN_EXP_BOTTLE = define("thrown_exp_bottle", PROJECTILE_ABSTRACT);
      PRIMED_TNT = define("primed_tnt", ENTITY);
      FIREWORK = define("firework", ENTITY);
      MINECART_COMMAND = define("minecart_command", MINECART_ABSTRACT);
      MINECART_RIDEABLE = define("minecart_rideable", MINECART_ABSTRACT);
      MINECART_CHEST = define("minecart_chest", MINECART_ABSTRACT);
      MINECART_FURNACE = define("minecart_furnace", MINECART_ABSTRACT);
      MINECART_TNT = define("minecart_tnt", MINECART_ABSTRACT);
      MINECART_HOPPER = define("minecart_hopper", MINECART_ABSTRACT);
      MINECART_MOB_SPAWNER = define("minecart_mob_spawner", MINECART_ABSTRACT);
      DISPLAY = define("display", ENTITY);
      BLOCK_DISPLAY = define("block_display", DISPLAY);
      ITEM_DISPLAY = define("item_display", DISPLAY);
      TEXT_DISPLAY = define("text_display", DISPLAY);
      INTERACTION = define("interaction", DISPLAY);
      SNIFFER = define("sniffer", ABSTRACT_ANIMAL);
      BREEZE = define("breeze", ABSTRACT_MONSTER);
      WIND_CHARGE = define("wind_charge", PROJECTILE_ABSTRACT);
   }
}
