package dev.artixdev.libs.com.cryptomorin.xseries;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

public enum XMaterial {
   ACACIA_BOAT(new String[]{"BOAT_ACACIA"}),
   ACACIA_BUTTON(new String[]{"WOOD_BUTTON"}),
   ACACIA_CHEST_BOAT(new String[0]),
   ACACIA_DOOR(new String[]{"ACACIA_DOOR", "ACACIA_DOOR_ITEM"}),
   ACACIA_FENCE(new String[0]),
   ACACIA_FENCE_GATE(new String[0]),
   ACACIA_HANGING_SIGN(new String[0]),
   ACACIA_LEAVES(0, new String[]{"LEAVES_2"}),
   ACACIA_LOG(0, new String[]{"LOG_2"}),
   ACACIA_PLANKS(4, new String[]{"WOOD"}),
   ACACIA_PRESSURE_PLATE(new String[]{"WOOD_PLATE"}),
   ACACIA_SAPLING(4, new String[]{"SAPLING"}),
   ACACIA_SIGN(new String[]{"SIGN_POST", "SIGN"}),
   ACACIA_SLAB(4, new String[]{"WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB"}),
   ACACIA_STAIRS(new String[0]),
   ACACIA_TRAPDOOR(new String[]{"TRAP_DOOR"}),
   ACACIA_WALL_HANGING_SIGN(new String[0]),
   ACACIA_WALL_SIGN(new String[]{"WALL_SIGN"}),
   ACACIA_WOOD(0, new String[]{"LOG_2"}),
   ACTIVATOR_RAIL(new String[0]),
   AIR(new String[0]),
   ALLAY_SPAWN_EGG(new String[0]),
   ALLIUM(2, new String[]{"RED_ROSE"}),
   AMETHYST_BLOCK(new String[0]),
   AMETHYST_CLUSTER(new String[0]),
   AMETHYST_SHARD(new String[0]),
   ANCIENT_DEBRIS(new String[0]),
   ANDESITE(5, new String[]{"STONE"}),
   ANDESITE_SLAB(new String[0]),
   ANDESITE_STAIRS(new String[0]),
   ANDESITE_WALL(new String[0]),
   ANGLER_POTTERY_SHERD(new String[0]),
   ANVIL(new String[0]),
   APPLE(new String[0]),
   ARCHER_POTTERY_SHERD(new String[0]),
   ARMOR_STAND(new String[0]),
   ARMS_UP_POTTERY_SHERD(new String[0]),
   ARROW(new String[0]),
   ATTACHED_MELON_STEM(7, new String[]{"MELON_STEM"}),
   ATTACHED_PUMPKIN_STEM(7, new String[]{"PUMPKIN_STEM"}),
   AXOLOTL_BUCKET(new String[0]),
   AXOLOTL_SPAWN_EGG(new String[0]),
   AZALEA(new String[0]),
   AZALEA_LEAVES(new String[0]),
   AZURE_BLUET(3, new String[]{"RED_ROSE"}),
   BAKED_POTATO(new String[0]),
   BAMBOO(new String[0]),
   BAMBOO_BLOCK(new String[0]),
   BAMBOO_BUTTON(new String[0]),
   BAMBOO_CHEST_RAFT(new String[0]),
   BAMBOO_DOOR(new String[0]),
   BAMBOO_FENCE(new String[0]),
   BAMBOO_FENCE_GATE(new String[0]),
   BAMBOO_HANGING_SIGN(new String[0]),
   BAMBOO_MOSAIC(new String[0]),
   BAMBOO_MOSAIC_SLAB(new String[0]),
   BAMBOO_MOSAIC_STAIRS(new String[0]),
   BAMBOO_PLANKS(new String[0]),
   BAMBOO_PRESSURE_PLATE(new String[0]),
   BAMBOO_RAFT(new String[0]),
   BAMBOO_SAPLING(new String[0]),
   BAMBOO_SIGN(new String[0]),
   BAMBOO_SLAB(new String[0]),
   BAMBOO_STAIRS(new String[0]),
   BAMBOO_TRAPDOOR(new String[0]),
   BAMBOO_WALL_HANGING_SIGN(new String[0]),
   BAMBOO_WALL_SIGN(new String[0]),
   BARREL(new String[0]),
   BARRIER(new String[0]),
   BASALT(new String[0]),
   BAT_SPAWN_EGG(65, new String[]{"MONSTER_EGG"}),
   BEACON(new String[0]),
   BEDROCK(new String[0]),
   BEEF(new String[]{"RAW_BEEF"}),
   BEEHIVE(new String[0]),
   BEETROOT(new String[]{"BEETROOT_BLOCK"}),
   BEETROOTS(new String[]{"BEETROOT"}),
   BEETROOT_SEEDS(new String[0]),
   BEETROOT_SOUP(new String[0]),
   BEE_NEST(new String[0]),
   BEE_SPAWN_EGG(new String[0]),
   BELL(new String[0]),
   BIG_DRIPLEAF(new String[0]),
   BIG_DRIPLEAF_STEM(new String[0]),
   BIRCH_BOAT(new String[]{"BOAT_BIRCH"}),
   BIRCH_BUTTON(new String[]{"WOOD_BUTTON"}),
   BIRCH_CHEST_BOAT(new String[0]),
   BIRCH_DOOR(new String[]{"BIRCH_DOOR", "BIRCH_DOOR_ITEM"}),
   BIRCH_FENCE(new String[0]),
   BIRCH_FENCE_GATE(new String[0]),
   BIRCH_HANGING_SIGN(new String[0]),
   BIRCH_LEAVES(2, new String[]{"LEAVES"}),
   BIRCH_LOG(2, new String[]{"LOG"}),
   BIRCH_PLANKS(2, new String[]{"WOOD"}),
   BIRCH_PRESSURE_PLATE(new String[]{"WOOD_PLATE"}),
   BIRCH_SAPLING(2, new String[]{"SAPLING"}),
   BIRCH_SIGN(new String[]{"SIGN_POST", "SIGN"}),
   BIRCH_SLAB(2, new String[]{"WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB"}),
   BIRCH_STAIRS(new String[]{"BIRCH_WOOD_STAIRS"}),
   BIRCH_TRAPDOOR(new String[]{"TRAP_DOOR"}),
   BIRCH_WALL_HANGING_SIGN(new String[0]),
   BIRCH_WALL_SIGN(new String[]{"WALL_SIGN"}),
   BIRCH_WOOD(2, new String[]{"LOG"}),
   BLACKSTONE(new String[0]),
   BLACKSTONE_SLAB(new String[0]),
   BLACKSTONE_STAIRS(new String[0]),
   BLACKSTONE_WALL(new String[0]),
   BLACK_BANNER(new String[]{"STANDING_BANNER", "BANNER"}),
   BLACK_BED(supports(12) ? 15 : 0, new String[]{"BED_BLOCK", "BED"}),
   BLACK_CANDLE(new String[0]),
   BLACK_CANDLE_CAKE(new String[0]),
   BLACK_CARPET(15, new String[]{"CARPET"}),
   BLACK_CONCRETE(15, new String[]{"CONCRETE"}),
   BLACK_CONCRETE_POWDER(15, new String[]{"CONCRETE_POWDER"}),
   BLACK_DYE(new String[0]),
   BLACK_GLAZED_TERRACOTTA(new String[0]),
   BLACK_SHULKER_BOX(new String[0]),
   BLACK_STAINED_GLASS(15, new String[]{"STAINED_GLASS"}),
   BLACK_STAINED_GLASS_PANE(15, new String[]{"STAINED_GLASS_PANE"}),
   BLACK_TERRACOTTA(15, new String[]{"STAINED_CLAY"}),
   BLACK_WALL_BANNER(new String[]{"WALL_BANNER"}),
   BLACK_WOOL(15, new String[]{"WOOL"}),
   BLADE_POTTERY_SHERD(new String[0]),
   BLAST_FURNACE(new String[0]),
   BLAZE_POWDER(new String[0]),
   BLAZE_ROD(new String[0]),
   BLAZE_SPAWN_EGG(61, new String[]{"MONSTER_EGG"}),
   BLUE_BANNER(4, new String[]{"STANDING_BANNER", "BANNER"}),
   BLUE_BED(supports(12) ? 11 : 0, new String[]{"BED_BLOCK", "BED"}),
   BLUE_CANDLE(new String[0]),
   BLUE_CANDLE_CAKE(new String[0]),
   BLUE_CARPET(11, new String[]{"CARPET"}),
   BLUE_CONCRETE(11, new String[]{"CONCRETE"}),
   BLUE_CONCRETE_POWDER(11, new String[]{"CONCRETE_POWDER"}),
   BLUE_DYE(4, new String[]{"INK_SACK", "LAPIS_LAZULI"}),
   BLUE_GLAZED_TERRACOTTA(new String[0]),
   BLUE_ICE(new String[0]),
   BLUE_ORCHID(1, new String[]{"RED_ROSE"}),
   BLUE_SHULKER_BOX(new String[0]),
   BLUE_STAINED_GLASS(11, new String[]{"STAINED_GLASS"}),
   BLUE_STAINED_GLASS_PANE(11, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   BLUE_TERRACOTTA(11, new String[]{"STAINED_CLAY"}),
   BLUE_WALL_BANNER(4, new String[]{"WALL_BANNER"}),
   BLUE_WOOL(11, new String[]{"WOOL"}),
   BONE(new String[0]),
   BONE_BLOCK(new String[0]),
   BONE_MEAL(15, new String[]{"INK_SACK"}),
   BOOK(new String[0]),
   BOOKSHELF(new String[0]),
   BOW(new String[0]),
   BOWL(new String[0]),
   BRAIN_CORAL(new String[0]),
   BRAIN_CORAL_BLOCK(new String[0]),
   BRAIN_CORAL_FAN(new String[0]),
   BRAIN_CORAL_WALL_FAN(new String[0]),
   BREAD(new String[0]),
   BREWER_POTTERY_SHERD(new String[0]),
   BREWING_STAND(new String[]{"BREWING_STAND", "BREWING_STAND_ITEM"}),
   BRICK(new String[]{"CLAY_BRICK"}),
   BRICKS(new String[]{"BRICK"}),
   BRICK_SLAB(4, new String[]{"STEP"}),
   BRICK_STAIRS(new String[0]),
   BRICK_WALL(new String[0]),
   BROWN_BANNER(3, new String[]{"STANDING_BANNER", "BANNER"}),
   BROWN_BED(supports(12) ? 12 : 0, new String[]{"BED_BLOCK", "BED"}),
   BROWN_CANDLE(new String[0]),
   BROWN_CANDLE_CAKE(new String[0]),
   BROWN_CARPET(12, new String[]{"CARPET"}),
   BROWN_CONCRETE(12, new String[]{"CONCRETE"}),
   BROWN_CONCRETE_POWDER(12, new String[]{"CONCRETE_POWDER"}),
   BROWN_DYE(3, new String[]{"INK_SACK", "DYE", "COCOA_BEANS"}),
   BROWN_GLAZED_TERRACOTTA(new String[0]),
   BROWN_MUSHROOM(new String[0]),
   BROWN_MUSHROOM_BLOCK(new String[]{"BROWN_MUSHROOM", "HUGE_MUSHROOM_1"}),
   BROWN_SHULKER_BOX(new String[0]),
   BROWN_STAINED_GLASS(12, new String[]{"STAINED_GLASS"}),
   BROWN_STAINED_GLASS_PANE(12, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   BROWN_TERRACOTTA(12, new String[]{"STAINED_CLAY"}),
   BROWN_WALL_BANNER(3, new String[]{"WALL_BANNER"}),
   BROWN_WOOL(12, new String[]{"WOOL"}),
   BRUSH(new String[0]),
   BUBBLE_COLUMN(new String[0]),
   BUBBLE_CORAL(new String[0]),
   BUBBLE_CORAL_BLOCK(new String[0]),
   BUBBLE_CORAL_FAN(new String[0]),
   BUBBLE_CORAL_WALL_FAN(new String[0]),
   BUCKET(new String[0]),
   BUDDING_AMETHYST(new String[0]),
   BUNDLE(new String[0]),
   BURN_POTTERY_SHERD(new String[0]),
   CACTUS(new String[0]),
   CAKE(new String[]{"CAKE_BLOCK"}),
   CALCITE(new String[0]),
   CALIBRATED_SCULK_SENSOR(new String[0]),
   CAMEL_SPAWN_EGG(new String[0]),
   CAMPFIRE(new String[0]),
   CANDLE(new String[0]),
   CANDLE_CAKE(new String[0]),
   CARROT(new String[]{"CARROT_ITEM"}),
   CARROTS(new String[]{"CARROT"}),
   CARROT_ON_A_STICK(new String[]{"CARROT_STICK"}),
   CARTOGRAPHY_TABLE(new String[0]),
   CARVED_PUMPKIN(new String[0]),
   CAT_SPAWN_EGG(new String[0]),
   CAULDRON(new String[]{"CAULDRON", "CAULDRON_ITEM"}),
   CAVE_AIR(new String[]{"AIR"}),
   CAVE_SPIDER_SPAWN_EGG(59, new String[]{"MONSTER_EGG"}),
   CAVE_VINES(new String[0]),
   CAVE_VINES_PLANT(new String[0]),
   CHAIN(new String[0]),
   CHAINMAIL_BOOTS(new String[0]),
   CHAINMAIL_CHESTPLATE(new String[0]),
   CHAINMAIL_HELMET(new String[0]),
   CHAINMAIL_LEGGINGS(new String[0]),
   CHAIN_COMMAND_BLOCK(new String[]{"COMMAND", "COMMAND_CHAIN"}),
   CHARCOAL(1, new String[]{"COAL"}),
   CHERRY_BOAT(new String[0]),
   CHERRY_BUTTON(new String[0]),
   CHERRY_CHEST_BOAT(new String[0]),
   CHERRY_DOOR(new String[0]),
   CHERRY_FENCE(new String[0]),
   CHERRY_FENCE_GATE(new String[0]),
   CHERRY_HANGING_SIGN(new String[0]),
   CHERRY_LEAVES(new String[0]),
   CHERRY_LOG(new String[0]),
   CHERRY_PLANKS(new String[0]),
   CHERRY_PRESSURE_PLATE(new String[0]),
   CHERRY_SAPLING(new String[0]),
   CHERRY_SIGN(new String[0]),
   CHERRY_SLAB(new String[0]),
   CHERRY_STAIRS(new String[0]),
   CHERRY_TRAPDOOR(new String[0]),
   CHERRY_WALL_HANGING_SIGN(new String[0]),
   CHERRY_WALL_SIGN(new String[0]),
   CHERRY_WOOD(new String[0]),
   CHEST(new String[]{"LOCKED_CHEST"}),
   CHEST_MINECART(new String[]{"STORAGE_MINECART"}),
   CHICKEN(new String[]{"RAW_CHICKEN"}),
   CHICKEN_SPAWN_EGG(93, new String[]{"MONSTER_EGG"}),
   CHIPPED_ANVIL(1, new String[]{"ANVIL"}),
   CHISELED_BOOKSHELF(new String[0]),
   CHISELED_DEEPSLATE(new String[0]),
   CHISELED_NETHER_BRICKS(1, new String[]{"NETHER_BRICKS"}),
   CHISELED_POLISHED_BLACKSTONE(new String[]{"POLISHED_BLACKSTONE"}),
   CHISELED_QUARTZ_BLOCK(1, new String[]{"QUARTZ_BLOCK"}),
   CHISELED_RED_SANDSTONE(1, new String[]{"RED_SANDSTONE"}),
   CHISELED_SANDSTONE(1, new String[]{"SANDSTONE"}),
   CHISELED_STONE_BRICKS(3, new String[]{"SMOOTH_BRICK"}),
   CHORUS_FLOWER(new String[0]),
   CHORUS_FRUIT(new String[0]),
   CHORUS_PLANT(new String[0]),
   CLAY(new String[0]),
   CLAY_BALL(new String[0]),
   CLOCK(new String[]{"WATCH"}),
   COAL(new String[0]),
   COAL_BLOCK(new String[0]),
   COAL_ORE(new String[0]),
   COARSE_DIRT(1, new String[]{"DIRT"}),
   COAST_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   COBBLED_DEEPSLATE(new String[0]),
   COBBLED_DEEPSLATE_SLAB(new String[0]),
   COBBLED_DEEPSLATE_STAIRS(new String[0]),
   COBBLED_DEEPSLATE_WALL(new String[0]),
   COBBLESTONE(new String[0]),
   COBBLESTONE_SLAB(3, new String[]{"STEP"}),
   COBBLESTONE_STAIRS(new String[0]),
   COBBLESTONE_WALL(new String[]{"COBBLE_WALL"}),
   COBWEB(new String[]{"WEB"}),
   COCOA(new String[0]),
   COCOA_BEANS(3, new String[]{"INK_SACK"}),
   COD(new String[]{"RAW_FISH"}),
   COD_BUCKET(new String[0]),
   COD_SPAWN_EGG(new String[0]),
   COMMAND_BLOCK(new String[]{"COMMAND"}),
   COMMAND_BLOCK_MINECART(new String[]{"COMMAND_MINECART"}),
   COMPARATOR(new String[]{"REDSTONE_COMPARATOR_OFF", "REDSTONE_COMPARATOR_ON", "REDSTONE_COMPARATOR"}),
   COMPASS(new String[0]),
   COMPOSTER(new String[0]),
   CONDUIT(new String[0]),
   COOKED_BEEF(new String[0]),
   COOKED_CHICKEN(new String[0]),
   COOKED_COD(new String[]{"COOKED_FISH"}),
   COOKED_MUTTON(new String[0]),
   COOKED_PORKCHOP(new String[]{"GRILLED_PORK"}),
   COOKED_RABBIT(new String[0]),
   COOKED_SALMON(1, new String[]{"COOKED_FISH"}),
   COOKIE(new String[0]),
   COPPER_BLOCK(new String[0]),
   COPPER_INGOT(new String[0]),
   COPPER_ORE(new String[0]),
   CORNFLOWER(new String[0]),
   COW_SPAWN_EGG(92, new String[]{"MONSTER_EGG"}),
   CRACKED_DEEPSLATE_BRICKS(new String[0]),
   CRACKED_DEEPSLATE_TILES(new String[0]),
   CRACKED_NETHER_BRICKS(2, new String[]{"NETHER_BRICKS"}),
   CRACKED_POLISHED_BLACKSTONE_BRICKS(new String[]{"POLISHED_BLACKSTONE_BRICKS"}),
   CRACKED_STONE_BRICKS(2, new String[]{"SMOOTH_BRICK"}),
   CRAFTING_TABLE(new String[]{"WORKBENCH"}),
   CREEPER_BANNER_PATTERN(new String[0]),
   CREEPER_HEAD(4, new String[]{"SKULL", "SKULL_ITEM"}),
   CREEPER_SPAWN_EGG(50, new String[]{"MONSTER_EGG"}),
   CREEPER_WALL_HEAD(4, new String[]{"SKULL", "SKULL_ITEM"}),
   CRIMSON_BUTTON(new String[0]),
   CRIMSON_DOOR(new String[0]),
   CRIMSON_FENCE(new String[0]),
   CRIMSON_FENCE_GATE(new String[0]),
   CRIMSON_FUNGUS(new String[0]),
   CRIMSON_HANGING_SIGN(new String[0]),
   CRIMSON_HYPHAE(new String[0]),
   CRIMSON_NYLIUM(new String[0]),
   CRIMSON_PLANKS(new String[0]),
   CRIMSON_PRESSURE_PLATE(new String[0]),
   CRIMSON_ROOTS(new String[0]),
   CRIMSON_SIGN(new String[]{"SIGN_POST"}),
   CRIMSON_SLAB(new String[0]),
   CRIMSON_STAIRS(new String[0]),
   CRIMSON_STEM(new String[0]),
   CRIMSON_TRAPDOOR(new String[0]),
   CRIMSON_WALL_HANGING_SIGN(new String[0]),
   CRIMSON_WALL_SIGN(new String[]{"WALL_SIGN"}),
   CROSSBOW(new String[0]),
   CRYING_OBSIDIAN(new String[0]),
   CUT_COPPER(new String[0]),
   CUT_COPPER_SLAB(new String[0]),
   CUT_COPPER_STAIRS(new String[0]),
   CUT_RED_SANDSTONE(new String[0]),
   CUT_RED_SANDSTONE_SLAB(new String[]{"STONE_SLAB2"}),
   CUT_SANDSTONE(new String[0]),
   CUT_SANDSTONE_SLAB(1, new String[]{"STEP"}),
   CYAN_BANNER(6, new String[]{"STANDING_BANNER", "BANNER"}),
   CYAN_BED(supports(12) ? 9 : 0, new String[]{"BED_BLOCK", "BED"}),
   CYAN_CANDLE(new String[0]),
   CYAN_CANDLE_CAKE(new String[0]),
   CYAN_CARPET(9, new String[]{"CARPET"}),
   CYAN_CONCRETE(9, new String[]{"CONCRETE"}),
   CYAN_CONCRETE_POWDER(9, new String[]{"CONCRETE_POWDER"}),
   CYAN_DYE(6, new String[]{"INK_SACK"}),
   CYAN_GLAZED_TERRACOTTA(new String[0]),
   CYAN_SHULKER_BOX(new String[0]),
   CYAN_STAINED_GLASS(9, new String[]{"STAINED_GLASS"}),
   CYAN_STAINED_GLASS_PANE(9, new String[]{"STAINED_GLASS_PANE"}),
   CYAN_TERRACOTTA(9, new String[]{"STAINED_CLAY"}),
   CYAN_WALL_BANNER(6, new String[]{"WALL_BANNER"}),
   CYAN_WOOL(9, new String[]{"WOOL"}),
   DAMAGED_ANVIL(2, new String[]{"ANVIL"}),
   DANDELION(new String[]{"YELLOW_FLOWER"}),
   DANGER_POTTERY_SHERD(new String[0]),
   DARK_OAK_BOAT(new String[]{"BOAT_DARK_OAK"}),
   DARK_OAK_BUTTON(new String[]{"WOOD_BUTTON"}),
   DARK_OAK_CHEST_BOAT(new String[0]),
   DARK_OAK_DOOR(new String[]{"DARK_OAK_DOOR", "DARK_OAK_DOOR_ITEM"}),
   DARK_OAK_FENCE(new String[0]),
   DARK_OAK_FENCE_GATE(new String[0]),
   DARK_OAK_HANGING_SIGN(new String[0]),
   DARK_OAK_LEAVES(1, new String[]{"LEAVES_2"}),
   DARK_OAK_LOG(1, new String[]{"LOG_2"}),
   DARK_OAK_PLANKS(5, new String[]{"WOOD"}),
   DARK_OAK_PRESSURE_PLATE(new String[]{"WOOD_PLATE"}),
   DARK_OAK_SAPLING(5, new String[]{"SAPLING"}),
   DARK_OAK_SIGN(new String[]{"SIGN_POST", "SIGN"}),
   DARK_OAK_SLAB(5, new String[]{"WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB"}),
   DARK_OAK_STAIRS(new String[0]),
   DARK_OAK_TRAPDOOR(new String[]{"TRAP_DOOR"}),
   DARK_OAK_WALL_HANGING_SIGN(new String[0]),
   DARK_OAK_WALL_SIGN(new String[]{"WALL_SIGN"}),
   DARK_OAK_WOOD(1, new String[]{"LOG_2"}),
   DARK_PRISMARINE(2, new String[]{"PRISMARINE"}),
   DARK_PRISMARINE_SLAB(new String[0]),
   DARK_PRISMARINE_STAIRS(new String[0]),
   DAYLIGHT_DETECTOR(new String[]{"DAYLIGHT_DETECTOR_INVERTED"}),
   DEAD_BRAIN_CORAL(new String[0]),
   DEAD_BRAIN_CORAL_BLOCK(new String[0]),
   DEAD_BRAIN_CORAL_FAN(new String[0]),
   DEAD_BRAIN_CORAL_WALL_FAN(new String[0]),
   DEAD_BUBBLE_CORAL(new String[0]),
   DEAD_BUBBLE_CORAL_BLOCK(new String[0]),
   DEAD_BUBBLE_CORAL_FAN(new String[0]),
   DEAD_BUBBLE_CORAL_WALL_FAN(new String[0]),
   DEAD_BUSH(new String[]{"LONG_GRASS"}),
   DEAD_FIRE_CORAL(new String[0]),
   DEAD_FIRE_CORAL_BLOCK(new String[0]),
   DEAD_FIRE_CORAL_FAN(new String[0]),
   DEAD_FIRE_CORAL_WALL_FAN(new String[0]),
   DEAD_HORN_CORAL(new String[0]),
   DEAD_HORN_CORAL_BLOCK(new String[0]),
   DEAD_HORN_CORAL_FAN(new String[0]),
   DEAD_HORN_CORAL_WALL_FAN(new String[0]),
   DEAD_TUBE_CORAL(new String[0]),
   DEAD_TUBE_CORAL_BLOCK(new String[0]),
   DEAD_TUBE_CORAL_FAN(new String[0]),
   DEAD_TUBE_CORAL_WALL_FAN(new String[0]),
   DEBUG_STICK(new String[0]),
   DECORATED_POT(new String[0]),
   DEEPSLATE(new String[0]),
   DEEPSLATE_BRICKS(new String[0]),
   DEEPSLATE_BRICK_SLAB(new String[0]),
   DEEPSLATE_BRICK_STAIRS(new String[0]),
   DEEPSLATE_BRICK_WALL(new String[0]),
   DEEPSLATE_COAL_ORE(new String[0]),
   DEEPSLATE_COPPER_ORE(new String[0]),
   DEEPSLATE_DIAMOND_ORE(new String[0]),
   DEEPSLATE_EMERALD_ORE(new String[0]),
   DEEPSLATE_GOLD_ORE(new String[0]),
   DEEPSLATE_IRON_ORE(new String[0]),
   DEEPSLATE_LAPIS_ORE(new String[0]),
   DEEPSLATE_REDSTONE_ORE(new String[0]),
   DEEPSLATE_TILES(new String[0]),
   DEEPSLATE_TILE_SLAB(new String[0]),
   DEEPSLATE_TILE_STAIRS(new String[0]),
   DEEPSLATE_TILE_WALL(new String[0]),
   DETECTOR_RAIL(new String[0]),
   DIAMOND(new String[0]),
   DIAMOND_AXE(new String[0]),
   DIAMOND_BLOCK(new String[0]),
   DIAMOND_BOOTS(new String[0]),
   DIAMOND_CHESTPLATE(new String[0]),
   DIAMOND_HELMET(new String[0]),
   DIAMOND_HOE(new String[0]),
   DIAMOND_HORSE_ARMOR(new String[]{"DIAMOND_BARDING"}),
   DIAMOND_LEGGINGS(new String[0]),
   DIAMOND_ORE(new String[0]),
   DIAMOND_PICKAXE(new String[0]),
   DIAMOND_SHOVEL(new String[]{"DIAMOND_SPADE"}),
   DIAMOND_SWORD(new String[0]),
   DIORITE(3, new String[]{"STONE"}),
   DIORITE_SLAB(new String[0]),
   DIORITE_STAIRS(new String[0]),
   DIORITE_WALL(new String[0]),
   DIRT(new String[0]),
   DIRT_PATH(new String[]{"GRASS_PATH"}),
   DISC_FRAGMENT_5(new String[0]),
   DISPENSER(new String[0]),
   DOLPHIN_SPAWN_EGG(new String[0]),
   DONKEY_SPAWN_EGG(32, new String[]{"MONSTER_EGG"}),
   DRAGON_BREATH(new String[]{"DRAGONS_BREATH"}),
   DRAGON_EGG(new String[0]),
   DRAGON_HEAD(5, new String[]{"SKULL", "SKULL_ITEM"}),
   DRAGON_WALL_HEAD(5, new String[]{"SKULL", "SKULL_ITEM"}),
   DRIED_KELP(new String[0]),
   DRIED_KELP_BLOCK(new String[0]),
   DRIPSTONE_BLOCK(new String[0]),
   DROPPER(new String[0]),
   DROWNED_SPAWN_EGG(new String[0]),
   DUNE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   ECHO_SHARD(new String[0]),
   EGG(new String[0]),
   ELDER_GUARDIAN_SPAWN_EGG(4, new String[]{"MONSTER_EGG"}),
   ELYTRA(new String[0]),
   EMERALD(new String[0]),
   EMERALD_BLOCK(new String[0]),
   EMERALD_ORE(new String[0]),
   ENCHANTED_BOOK(new String[0]),
   ENCHANTED_GOLDEN_APPLE(1, new String[]{"GOLDEN_APPLE"}),
   ENCHANTING_TABLE(new String[]{"ENCHANTMENT_TABLE"}),
   ENDERMAN_SPAWN_EGG(58, new String[]{"MONSTER_EGG"}),
   ENDERMITE_SPAWN_EGG(67, new String[]{"MONSTER_EGG"}),
   ENDER_CHEST(new String[0]),
   ENDER_DRAGON_SPAWN_EGG(new String[0]),
   ENDER_EYE(new String[]{"EYE_OF_ENDER"}),
   ENDER_PEARL(new String[0]),
   END_CRYSTAL(new String[0]),
   END_GATEWAY(new String[0]),
   END_PORTAL(new String[]{"ENDER_PORTAL"}),
   END_PORTAL_FRAME(new String[]{"ENDER_PORTAL_FRAME"}),
   END_ROD(new String[0]),
   END_STONE(new String[]{"ENDER_STONE"}),
   END_STONE_BRICKS(new String[]{"END_BRICKS"}),
   END_STONE_BRICK_SLAB(new String[0]),
   END_STONE_BRICK_STAIRS(new String[0]),
   END_STONE_BRICK_WALL(new String[0]),
   EVOKER_SPAWN_EGG(34, new String[]{"MONSTER_EGG"}),
   EXPERIENCE_BOTTLE(new String[]{"EXP_BOTTLE"}),
   EXPLORER_POTTERY_SHERD(new String[0]),
   EXPOSED_COPPER(new String[0]),
   EXPOSED_CUT_COPPER(new String[0]),
   EXPOSED_CUT_COPPER_SLAB(new String[0]),
   EXPOSED_CUT_COPPER_STAIRS(new String[0]),
   EYE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   FARMLAND(new String[]{"SOIL"}),
   FEATHER(new String[0]),
   FERMENTED_SPIDER_EYE(new String[0]),
   FERN(2, new String[]{"LONG_GRASS"}),
   FILLED_MAP(new String[]{"MAP"}),
   FIRE(new String[0]),
   FIREWORK_ROCKET(new String[]{"FIREWORK"}),
   FIREWORK_STAR(new String[]{"FIREWORK_CHARGE"}),
   FIRE_CHARGE(new String[]{"FIREBALL"}),
   FIRE_CORAL(new String[0]),
   FIRE_CORAL_BLOCK(new String[0]),
   FIRE_CORAL_FAN(new String[0]),
   FIRE_CORAL_WALL_FAN(new String[0]),
   FISHING_ROD(new String[0]),
   FLETCHING_TABLE(new String[0]),
   FLINT(new String[0]),
   FLINT_AND_STEEL(new String[0]),
   FLOWERING_AZALEA(new String[0]),
   FLOWERING_AZALEA_LEAVES(new String[0]),
   FLOWER_BANNER_PATTERN(new String[0]),
   FLOWER_POT(new String[]{"FLOWER_POT", "FLOWER_POT_ITEM"}),
   FOX_SPAWN_EGG(new String[0]),
   FRIEND_POTTERY_SHERD(new String[0]),
   FROGSPAWN(new String[0]),
   FROG_SPAWN_EGG(new String[0]),
   FROSTED_ICE(new String[0]),
   FURNACE(new String[]{"BURNING_FURNACE"}),
   FURNACE_MINECART(new String[]{"POWERED_MINECART"}),
   GHAST_SPAWN_EGG(56, new String[]{"MONSTER_EGG"}),
   GHAST_TEAR(new String[0]),
   GILDED_BLACKSTONE(new String[0]),
   GLASS(new String[0]),
   GLASS_BOTTLE(new String[0]),
   GLASS_PANE(new String[]{"THIN_GLASS"}),
   GLISTERING_MELON_SLICE(new String[]{"SPECKLED_MELON"}),
   GLOBE_BANNER_PATTERN(new String[0]),
   GLOWSTONE(new String[0]),
   GLOWSTONE_DUST(new String[0]),
   GLOW_BERRIES(new String[0]),
   GLOW_INK_SAC(new String[0]),
   GLOW_ITEM_FRAME(new String[0]),
   GLOW_LICHEN(new String[0]),
   GLOW_SQUID_SPAWN_EGG(new String[0]),
   GOAT_HORN(new String[0]),
   GOAT_SPAWN_EGG(new String[0]),
   GOLDEN_APPLE(new String[0]),
   GOLDEN_AXE(new String[]{"GOLD_AXE"}),
   GOLDEN_BOOTS(new String[]{"GOLD_BOOTS"}),
   GOLDEN_CARROT(new String[0]),
   GOLDEN_CHESTPLATE(new String[]{"GOLD_CHESTPLATE"}),
   GOLDEN_HELMET(new String[]{"GOLD_HELMET"}),
   GOLDEN_HOE(new String[]{"GOLD_HOE"}),
   GOLDEN_HORSE_ARMOR(new String[]{"GOLD_BARDING"}),
   GOLDEN_LEGGINGS(new String[]{"GOLD_LEGGINGS"}),
   GOLDEN_PICKAXE(new String[]{"GOLD_PICKAXE"}),
   GOLDEN_SHOVEL(new String[]{"GOLD_SPADE"}),
   GOLDEN_SWORD(new String[]{"GOLD_SWORD"}),
   GOLD_BLOCK(new String[0]),
   GOLD_INGOT(new String[0]),
   GOLD_NUGGET(new String[0]),
   GOLD_ORE(new String[0]),
   GRANITE(1, new String[]{"STONE"}),
   GRANITE_SLAB(new String[0]),
   GRANITE_STAIRS(new String[0]),
   GRANITE_WALL(new String[0]),
   GRASS(1, new String[]{"LONG_GRASS"}),
   GRASS_BLOCK(new String[]{"GRASS"}),
   GRAVEL(new String[0]),
   GRAY_BANNER(8, new String[]{"STANDING_BANNER", "BANNER"}),
   GRAY_BED(supports(12) ? 7 : 0, new String[]{"BED_BLOCK", "BED"}),
   GRAY_CANDLE(new String[0]),
   GRAY_CANDLE_CAKE(new String[0]),
   GRAY_CARPET(7, new String[]{"CARPET"}),
   GRAY_CONCRETE(7, new String[]{"CONCRETE"}),
   GRAY_CONCRETE_POWDER(7, new String[]{"CONCRETE_POWDER"}),
   GRAY_DYE(8, new String[]{"INK_SACK"}),
   GRAY_GLAZED_TERRACOTTA(new String[0]),
   GRAY_SHULKER_BOX(new String[0]),
   GRAY_STAINED_GLASS(7, new String[]{"STAINED_GLASS"}),
   GRAY_STAINED_GLASS_PANE(7, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   GRAY_TERRACOTTA(7, new String[]{"STAINED_CLAY"}),
   GRAY_WALL_BANNER(8, new String[]{"WALL_BANNER"}),
   GRAY_WOOL(7, new String[]{"WOOL"}),
   GREEN_BANNER(2, new String[]{"STANDING_BANNER", "BANNER"}),
   GREEN_BED(supports(12) ? 13 : 0, new String[]{"BED_BLOCK", "BED"}),
   GREEN_CANDLE(new String[0]),
   GREEN_CANDLE_CAKE(new String[0]),
   GREEN_CARPET(13, new String[]{"CARPET"}),
   GREEN_CONCRETE(13, new String[]{"CONCRETE"}),
   GREEN_CONCRETE_POWDER(13, new String[]{"CONCRETE_POWDER"}),
   GREEN_DYE(2, new String[]{"INK_SACK", "CACTUS_GREEN"}),
   GREEN_GLAZED_TERRACOTTA(new String[0]),
   GREEN_SHULKER_BOX(new String[0]),
   GREEN_STAINED_GLASS(13, new String[]{"STAINED_GLASS"}),
   GREEN_STAINED_GLASS_PANE(13, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   GREEN_TERRACOTTA(13, new String[]{"STAINED_CLAY"}),
   GREEN_WALL_BANNER(2, new String[]{"WALL_BANNER"}),
   GREEN_WOOL(13, new String[]{"WOOL"}),
   GRINDSTONE(new String[0]),
   GUARDIAN_SPAWN_EGG(68, new String[]{"MONSTER_EGG"}),
   GUNPOWDER(new String[]{"SULPHUR"}),
   HANGING_ROOTS(new String[0]),
   HAY_BLOCK(new String[0]),
   HEARTBREAK_POTTERY_SHERD(new String[0]),
   HEART_OF_THE_SEA(new String[0]),
   HEART_POTTERY_SHERD(new String[0]),
   HEAVY_WEIGHTED_PRESSURE_PLATE(new String[]{"IRON_PLATE"}),
   HOGLIN_SPAWN_EGG(new String[]{"MONSTER_EGG"}),
   HONEYCOMB(new String[0]),
   HONEYCOMB_BLOCK(new String[0]),
   HONEY_BLOCK(new String[0]),
   HONEY_BOTTLE(new String[0]),
   HOPPER(new String[0]),
   HOPPER_MINECART(new String[0]),
   HORN_CORAL(new String[0]),
   HORN_CORAL_BLOCK(new String[0]),
   HORN_CORAL_FAN(new String[0]),
   HORN_CORAL_WALL_FAN(new String[0]),
   HORSE_SPAWN_EGG(100, new String[]{"MONSTER_EGG"}),
   HOST_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   HOWL_POTTERY_SHERD(new String[0]),
   HUSK_SPAWN_EGG(23, new String[]{"MONSTER_EGG"}),
   ICE(new String[0]),
   INFESTED_CHISELED_STONE_BRICKS(5, new String[]{"MONSTER_EGGS"}),
   INFESTED_COBBLESTONE(1, new String[]{"MONSTER_EGGS"}),
   INFESTED_CRACKED_STONE_BRICKS(4, new String[]{"MONSTER_EGGS"}),
   INFESTED_DEEPSLATE(new String[0]),
   INFESTED_MOSSY_STONE_BRICKS(3, new String[]{"MONSTER_EGGS"}),
   INFESTED_STONE(new String[]{"MONSTER_EGGS"}),
   INFESTED_STONE_BRICKS(2, new String[]{"MONSTER_EGGS"}),
   INK_SAC(new String[]{"INK_SACK"}),
   IRON_AXE(new String[0]),
   IRON_BARS(new String[]{"IRON_FENCE"}),
   IRON_BLOCK(new String[0]),
   IRON_BOOTS(new String[0]),
   IRON_CHESTPLATE(new String[0]),
   IRON_DOOR(new String[]{"IRON_DOOR_BLOCK"}),
   IRON_GOLEM_SPAWN_EGG(new String[0]),
   IRON_HELMET(new String[0]),
   IRON_HOE(new String[0]),
   IRON_HORSE_ARMOR(new String[]{"IRON_BARDING"}),
   IRON_INGOT(new String[0]),
   IRON_LEGGINGS(new String[0]),
   IRON_NUGGET(new String[0]),
   IRON_ORE(new String[0]),
   IRON_PICKAXE(new String[0]),
   IRON_SHOVEL(new String[]{"IRON_SPADE"}),
   IRON_SWORD(new String[0]),
   IRON_TRAPDOOR(new String[0]),
   ITEM_FRAME(new String[0]),
   JACK_O_LANTERN(new String[0]),
   JIGSAW(new String[0]),
   JUKEBOX(new String[0]),
   JUNGLE_BOAT(new String[]{"BOAT_JUNGLE"}),
   JUNGLE_BUTTON(new String[]{"WOOD_BUTTON"}),
   JUNGLE_CHEST_BOAT(new String[0]),
   JUNGLE_DOOR(new String[]{"JUNGLE_DOOR", "JUNGLE_DOOR_ITEM"}),
   JUNGLE_FENCE(new String[0]),
   JUNGLE_FENCE_GATE(new String[0]),
   JUNGLE_HANGING_SIGN(new String[0]),
   JUNGLE_LEAVES(3, new String[]{"LEAVES"}),
   JUNGLE_LOG(3, new String[]{"LOG"}),
   JUNGLE_PLANKS(3, new String[]{"WOOD"}),
   JUNGLE_PRESSURE_PLATE(new String[]{"WOOD_PLATE"}),
   JUNGLE_SAPLING(3, new String[]{"SAPLING"}),
   JUNGLE_SIGN(new String[]{"SIGN_POST", "SIGN"}),
   JUNGLE_SLAB(3, new String[]{"WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB"}),
   JUNGLE_STAIRS(new String[]{"JUNGLE_WOOD_STAIRS"}),
   JUNGLE_TRAPDOOR(new String[]{"TRAP_DOOR"}),
   JUNGLE_WALL_HANGING_SIGN(new String[0]),
   JUNGLE_WALL_SIGN(new String[]{"WALL_SIGN"}),
   JUNGLE_WOOD(3, new String[]{"LOG"}),
   KELP(new String[0]),
   KELP_PLANT(new String[0]),
   KNOWLEDGE_BOOK(new String[]{"BOOK"}),
   LADDER(new String[0]),
   LANTERN(new String[0]),
   LAPIS_BLOCK(new String[0]),
   LAPIS_LAZULI(4, new String[]{"INK_SACK"}),
   LAPIS_ORE(new String[0]),
   LARGE_AMETHYST_BUD(new String[0]),
   LARGE_FERN(3, new String[]{"DOUBLE_PLANT"}),
   LAVA(new String[]{"STATIONARY_LAVA"}),
   LAVA_BUCKET(new String[0]),
   LAVA_CAULDRON(new String[0]),
   LEAD(new String[]{"LEASH"}),
   LEATHER(new String[0]),
   LEATHER_BOOTS(new String[0]),
   LEATHER_CHESTPLATE(new String[0]),
   LEATHER_HELMET(new String[0]),
   LEATHER_HORSE_ARMOR(new String[]{"IRON_HORSE_ARMOR"}),
   LEATHER_LEGGINGS(new String[0]),
   LECTERN(new String[0]),
   LEVER(new String[0]),
   LIGHT(new String[0]),
   LIGHTNING_ROD(new String[0]),
   LIGHT_BLUE_BANNER(12, new String[]{"STANDING_BANNER", "BANNER"}),
   LIGHT_BLUE_BED(supports(12) ? 3 : 0, new String[]{"BED_BLOCK", "BED"}),
   LIGHT_BLUE_CANDLE(new String[0]),
   LIGHT_BLUE_CANDLE_CAKE(new String[0]),
   LIGHT_BLUE_CARPET(3, new String[]{"CARPET"}),
   LIGHT_BLUE_CONCRETE(3, new String[]{"CONCRETE"}),
   LIGHT_BLUE_CONCRETE_POWDER(3, new String[]{"CONCRETE_POWDER"}),
   LIGHT_BLUE_DYE(12, new String[]{"INK_SACK"}),
   LIGHT_BLUE_GLAZED_TERRACOTTA(new String[0]),
   LIGHT_BLUE_SHULKER_BOX(new String[0]),
   LIGHT_BLUE_STAINED_GLASS(3, new String[]{"STAINED_GLASS"}),
   LIGHT_BLUE_STAINED_GLASS_PANE(3, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   LIGHT_BLUE_TERRACOTTA(3, new String[]{"STAINED_CLAY"}),
   LIGHT_BLUE_WALL_BANNER(12, new String[]{"WALL_BANNER", "STANDING_BANNER", "BANNER"}),
   LIGHT_BLUE_WOOL(3, new String[]{"WOOL"}),
   LIGHT_GRAY_BANNER(7, new String[]{"STANDING_BANNER", "BANNER"}),
   LIGHT_GRAY_BED(supports(12) ? 8 : 0, new String[]{"BED_BLOCK", "BED"}),
   LIGHT_GRAY_CANDLE(new String[0]),
   LIGHT_GRAY_CANDLE_CAKE(new String[0]),
   LIGHT_GRAY_CARPET(8, new String[]{"CARPET"}),
   LIGHT_GRAY_CONCRETE(8, new String[]{"CONCRETE"}),
   LIGHT_GRAY_CONCRETE_POWDER(8, new String[]{"CONCRETE_POWDER"}),
   LIGHT_GRAY_DYE(7, new String[]{"INK_SACK"}),
   LIGHT_GRAY_GLAZED_TERRACOTTA(new String[]{"SILVER_GLAZED_TERRACOTTA"}),
   LIGHT_GRAY_SHULKER_BOX(new String[]{"SILVER_SHULKER_BOX"}),
   LIGHT_GRAY_STAINED_GLASS(8, new String[]{"STAINED_GLASS"}),
   LIGHT_GRAY_STAINED_GLASS_PANE(8, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   LIGHT_GRAY_TERRACOTTA(8, new String[]{"STAINED_CLAY"}),
   LIGHT_GRAY_WALL_BANNER(7, new String[]{"WALL_BANNER"}),
   LIGHT_GRAY_WOOL(8, new String[]{"WOOL"}),
   LIGHT_WEIGHTED_PRESSURE_PLATE(new String[]{"GOLD_PLATE"}),
   LILAC(1, new String[]{"DOUBLE_PLANT"}),
   LILY_OF_THE_VALLEY(new String[0]),
   LILY_PAD(new String[]{"WATER_LILY"}),
   LIME_BANNER(10, new String[]{"STANDING_BANNER", "BANNER"}),
   LIME_BED(supports(12) ? 5 : 0, new String[]{"BED_BLOCK", "BED"}),
   LIME_CANDLE(new String[0]),
   LIME_CANDLE_CAKE(new String[0]),
   LIME_CARPET(5, new String[]{"CARPET"}),
   LIME_CONCRETE(5, new String[]{"CONCRETE"}),
   LIME_CONCRETE_POWDER(5, new String[]{"CONCRETE_POWDER"}),
   LIME_DYE(10, new String[]{"INK_SACK"}),
   LIME_GLAZED_TERRACOTTA(new String[0]),
   LIME_SHULKER_BOX(new String[0]),
   LIME_STAINED_GLASS(5, new String[]{"STAINED_GLASS"}),
   LIME_STAINED_GLASS_PANE(5, new String[]{"STAINED_GLASS_PANE"}),
   LIME_TERRACOTTA(5, new String[]{"STAINED_CLAY"}),
   LIME_WALL_BANNER(10, new String[]{"WALL_BANNER"}),
   LIME_WOOL(5, new String[]{"WOOL"}),
   LINGERING_POTION(new String[0]),
   LLAMA_SPAWN_EGG(103, new String[]{"MONSTER_EGG"}),
   LODESTONE(new String[0]),
   LOOM(new String[0]),
   MAGENTA_BANNER(13, new String[]{"STANDING_BANNER", "BANNER"}),
   MAGENTA_BED(supports(12) ? 2 : 0, new String[]{"BED_BLOCK", "BED"}),
   MAGENTA_CANDLE(new String[0]),
   MAGENTA_CANDLE_CAKE(new String[0]),
   MAGENTA_CARPET(2, new String[]{"CARPET"}),
   MAGENTA_CONCRETE(2, new String[]{"CONCRETE"}),
   MAGENTA_CONCRETE_POWDER(2, new String[]{"CONCRETE_POWDER"}),
   MAGENTA_DYE(13, new String[]{"INK_SACK"}),
   MAGENTA_GLAZED_TERRACOTTA(new String[0]),
   MAGENTA_SHULKER_BOX(new String[0]),
   MAGENTA_STAINED_GLASS(2, new String[]{"STAINED_GLASS"}),
   MAGENTA_STAINED_GLASS_PANE(2, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   MAGENTA_TERRACOTTA(2, new String[]{"STAINED_CLAY"}),
   MAGENTA_WALL_BANNER(13, new String[]{"WALL_BANNER"}),
   MAGENTA_WOOL(2, new String[]{"WOOL"}),
   MAGMA_BLOCK(new String[]{"MAGMA"}),
   MAGMA_CREAM(new String[0]),
   MAGMA_CUBE_SPAWN_EGG(62, new String[]{"MONSTER_EGG"}),
   MANGROVE_BOAT(new String[0]),
   MANGROVE_BUTTON(new String[0]),
   MANGROVE_CHEST_BOAT(new String[0]),
   MANGROVE_DOOR(new String[0]),
   MANGROVE_FENCE(new String[0]),
   MANGROVE_FENCE_GATE(new String[0]),
   MANGROVE_HANGING_SIGN(new String[0]),
   MANGROVE_LEAVES(new String[0]),
   MANGROVE_LOG(new String[0]),
   MANGROVE_PLANKS(new String[0]),
   MANGROVE_PRESSURE_PLATE(new String[0]),
   MANGROVE_PROPAGULE(new String[0]),
   MANGROVE_ROOTS(new String[0]),
   MANGROVE_SIGN(new String[0]),
   MANGROVE_SLAB(new String[0]),
   MANGROVE_STAIRS(new String[0]),
   MANGROVE_TRAPDOOR(new String[0]),
   MANGROVE_WALL_HANGING_SIGN(new String[0]),
   MANGROVE_WALL_SIGN(new String[0]),
   MANGROVE_WOOD(new String[0]),
   MAP(new String[]{"EMPTY_MAP"}),
   MEDIUM_AMETHYST_BUD(new String[0]),
   MELON(new String[]{"MELON_BLOCK"}),
   MELON_SEEDS(new String[0]),
   MELON_SLICE(new String[]{"MELON"}),
   MELON_STEM(new String[0]),
   MILK_BUCKET(new String[0]),
   MINECART(new String[0]),
   MINER_POTTERY_SHERD(new String[0]),
   MOJANG_BANNER_PATTERN(new String[0]),
   MOOSHROOM_SPAWN_EGG(96, new String[]{"MONSTER_EGG"}),
   MOSSY_COBBLESTONE(new String[0]),
   MOSSY_COBBLESTONE_SLAB(new String[0]),
   MOSSY_COBBLESTONE_STAIRS(new String[0]),
   MOSSY_COBBLESTONE_WALL(1, new String[]{"COBBLE_WALL", "COBBLESTONE_WALL"}),
   MOSSY_STONE_BRICKS(1, new String[]{"SMOOTH_BRICK"}),
   MOSSY_STONE_BRICK_SLAB(new String[0]),
   MOSSY_STONE_BRICK_STAIRS(new String[0]),
   MOSSY_STONE_BRICK_WALL(new String[0]),
   MOSS_BLOCK(new String[0]),
   MOSS_CARPET(new String[0]),
   MOURNER_POTTERY_SHERD(new String[0]),
   MOVING_PISTON(new String[]{"PISTON_MOVING_PIECE"}),
   MUD(new String[0]),
   MUDDY_MANGROVE_ROOTS(new String[0]),
   MUD_BRICKS(new String[0]),
   MUD_BRICK_SLAB(new String[0]),
   MUD_BRICK_STAIRS(new String[0]),
   MUD_BRICK_WALL(new String[0]),
   MULE_SPAWN_EGG(32, new String[]{"MONSTER_EGG"}),
   MUSHROOM_STEM(new String[]{"BROWN_MUSHROOM"}),
   MUSHROOM_STEW(new String[]{"MUSHROOM_SOUP"}),
   MUSIC_DISC_11(new String[]{"RECORD_11"}),
   MUSIC_DISC_13(new String[]{"GOLD_RECORD"}),
   MUSIC_DISC_5(new String[0]),
   MUSIC_DISC_BLOCKS(new String[]{"RECORD_3"}),
   MUSIC_DISC_CAT(new String[]{"GREEN_RECORD"}),
   MUSIC_DISC_CHIRP(new String[]{"RECORD_4"}),
   MUSIC_DISC_FAR(new String[]{"RECORD_5"}),
   MUSIC_DISC_MALL(new String[]{"RECORD_6"}),
   MUSIC_DISC_MELLOHI(new String[]{"RECORD_7"}),
   MUSIC_DISC_OTHERSIDE(new String[0]),
   MUSIC_DISC_PIGSTEP(new String[0]),
   MUSIC_DISC_RELIC(new String[0]),
   MUSIC_DISC_STAL(new String[]{"RECORD_8"}),
   MUSIC_DISC_STRAD(new String[]{"RECORD_9"}),
   MUSIC_DISC_WAIT(new String[]{"RECORD_12"}),
   MUSIC_DISC_WARD(new String[]{"RECORD_10"}),
   MUTTON(new String[0]),
   MYCELIUM(new String[]{"MYCEL"}),
   NAME_TAG(new String[0]),
   NAUTILUS_SHELL(new String[0]),
   NETHERITE_AXE(new String[0]),
   NETHERITE_BLOCK(new String[0]),
   NETHERITE_BOOTS(new String[0]),
   NETHERITE_CHESTPLATE(new String[0]),
   NETHERITE_HELMET(new String[0]),
   NETHERITE_HOE(new String[0]),
   NETHERITE_INGOT(new String[0]),
   NETHERITE_LEGGINGS(new String[0]),
   NETHERITE_PICKAXE(new String[0]),
   NETHERITE_SCRAP(new String[0]),
   NETHERITE_SHOVEL(new String[0]),
   NETHERITE_SWORD(new String[0]),
   NETHERITE_UPGRADE_SMITHING_TEMPLATE(new String[0]),
   NETHERRACK(new String[0]),
   NETHER_BRICK(new String[]{"NETHER_BRICK_ITEM"}),
   NETHER_BRICKS(new String[]{"NETHER_BRICK"}),
   NETHER_BRICK_FENCE(new String[]{"NETHER_FENCE"}),
   NETHER_BRICK_SLAB(6, new String[]{"STEP"}),
   NETHER_BRICK_STAIRS(new String[0]),
   NETHER_BRICK_WALL(new String[0]),
   NETHER_GOLD_ORE(new String[0]),
   NETHER_PORTAL(new String[]{"PORTAL"}),
   NETHER_QUARTZ_ORE(new String[]{"QUARTZ_ORE"}),
   NETHER_SPROUTS(new String[0]),
   NETHER_STAR(new String[0]),
   NETHER_WART(new String[]{"NETHER_WARTS", "NETHER_STALK"}),
   NETHER_WART_BLOCK(new String[0]),
   NOTE_BLOCK(new String[0]),
   OAK_BOAT(new String[]{"BOAT"}),
   OAK_BUTTON(new String[]{"WOOD_BUTTON"}),
   OAK_CHEST_BOAT(new String[0]),
   OAK_DOOR(new String[]{"WOODEN_DOOR", "WOOD_DOOR"}),
   OAK_FENCE(new String[]{"FENCE"}),
   OAK_FENCE_GATE(new String[]{"FENCE_GATE"}),
   OAK_HANGING_SIGN(new String[0]),
   OAK_LEAVES(new String[]{"LEAVES"}),
   OAK_LOG(new String[]{"LOG"}),
   OAK_PLANKS(new String[]{"WOOD"}),
   OAK_PRESSURE_PLATE(new String[]{"WOOD_PLATE"}),
   OAK_SAPLING(new String[]{"SAPLING"}),
   OAK_SIGN(new String[]{"SIGN_POST", "SIGN"}),
   OAK_SLAB(new String[]{"WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB"}),
   OAK_STAIRS(new String[]{"WOOD_STAIRS"}),
   OAK_TRAPDOOR(new String[]{"TRAP_DOOR"}),
   OAK_WALL_HANGING_SIGN(new String[0]),
   OAK_WALL_SIGN(new String[]{"WALL_SIGN"}),
   OAK_WOOD(new String[]{"LOG"}),
   OBSERVER(new String[0]),
   OBSIDIAN(new String[0]),
   OCELOT_SPAWN_EGG(98, new String[]{"MONSTER_EGG"}),
   OCHRE_FROGLIGHT(new String[0]),
   ORANGE_BANNER(14, new String[]{"STANDING_BANNER", "BANNER"}),
   ORANGE_BED(supports(12) ? 1 : 0, new String[]{"BED_BLOCK", "BED"}),
   ORANGE_CANDLE(new String[0]),
   ORANGE_CANDLE_CAKE(new String[0]),
   ORANGE_CARPET(1, new String[]{"CARPET"}),
   ORANGE_CONCRETE(1, new String[]{"CONCRETE"}),
   ORANGE_CONCRETE_POWDER(1, new String[]{"CONCRETE_POWDER"}),
   ORANGE_DYE(14, new String[]{"INK_SACK"}),
   ORANGE_GLAZED_TERRACOTTA(new String[0]),
   ORANGE_SHULKER_BOX(new String[0]),
   ORANGE_STAINED_GLASS(1, new String[]{"STAINED_GLASS"}),
   ORANGE_STAINED_GLASS_PANE(1, new String[]{"STAINED_GLASS_PANE"}),
   ORANGE_TERRACOTTA(1, new String[]{"STAINED_CLAY"}),
   ORANGE_TULIP(5, new String[]{"RED_ROSE"}),
   ORANGE_WALL_BANNER(14, new String[]{"WALL_BANNER"}),
   ORANGE_WOOL(1, new String[]{"WOOL"}),
   OXEYE_DAISY(8, new String[]{"RED_ROSE"}),
   OXIDIZED_COPPER(new String[0]),
   OXIDIZED_CUT_COPPER(new String[0]),
   OXIDIZED_CUT_COPPER_SLAB(new String[0]),
   OXIDIZED_CUT_COPPER_STAIRS(new String[0]),
   PACKED_ICE(new String[0]),
   PACKED_MUD(new String[0]),
   PAINTING(new String[0]),
   PANDA_SPAWN_EGG(new String[0]),
   PAPER(new String[0]),
   PARROT_SPAWN_EGG(105, new String[]{"MONSTER_EGG"}),
   PEARLESCENT_FROGLIGHT(new String[0]),
   PEONY(5, new String[]{"DOUBLE_PLANT"}),
   PETRIFIED_OAK_SLAB(new String[]{"WOOD_STEP"}),
   PHANTOM_MEMBRANE(new String[0]),
   PHANTOM_SPAWN_EGG(new String[0]),
   PIGLIN_BANNER_PATTERN(new String[0]),
   PIGLIN_BRUTE_SPAWN_EGG(new String[0]),
   PIGLIN_HEAD(new String[0]),
   PIGLIN_SPAWN_EGG(57, new String[]{"MONSTER_EGG"}),
   PIGLIN_WALL_HEAD(new String[0]),
   PIG_SPAWN_EGG(90, new String[]{"MONSTER_EGG"}),
   PILLAGER_SPAWN_EGG(new String[0]),
   PINK_BANNER(9, new String[]{"STANDING_BANNER", "BANNER"}),
   PINK_BED(supports(12) ? 6 : 0, new String[]{"BED_BLOCK", "BED"}),
   PINK_CANDLE(new String[0]),
   PINK_CANDLE_CAKE(new String[0]),
   PINK_CARPET(6, new String[]{"CARPET"}),
   PINK_CONCRETE(6, new String[]{"CONCRETE"}),
   PINK_CONCRETE_POWDER(6, new String[]{"CONCRETE_POWDER"}),
   PINK_DYE(9, new String[]{"INK_SACK"}),
   PINK_GLAZED_TERRACOTTA(new String[0]),
   PINK_PETALS(new String[0]),
   PINK_SHULKER_BOX(new String[0]),
   PINK_STAINED_GLASS(6, new String[]{"STAINED_GLASS"}),
   PINK_STAINED_GLASS_PANE(6, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   PINK_TERRACOTTA(6, new String[]{"STAINED_CLAY"}),
   PINK_TULIP(7, new String[]{"RED_ROSE"}),
   PINK_WALL_BANNER(9, new String[]{"WALL_BANNER"}),
   PINK_WOOL(6, new String[]{"WOOL"}),
   PISTON(new String[]{"PISTON_BASE"}),
   PISTON_HEAD(new String[]{"PISTON_EXTENSION"}),
   PITCHER_CROP(new String[0]),
   PITCHER_PLANT(new String[0]),
   PITCHER_POD(new String[0]),
   PLAYER_HEAD(3, new String[]{"SKULL", "SKULL_ITEM"}),
   PLAYER_WALL_HEAD(3, new String[]{"SKULL", "SKULL_ITEM"}),
   PLENTY_POTTERY_SHERD(new String[0]),
   PODZOL(2, new String[]{"DIRT"}),
   POINTED_DRIPSTONE(new String[0]),
   POISONOUS_POTATO(new String[0]),
   POLAR_BEAR_SPAWN_EGG(102, new String[]{"MONSTER_EGG"}),
   POLISHED_ANDESITE(6, new String[]{"STONE"}),
   POLISHED_ANDESITE_SLAB(new String[0]),
   POLISHED_ANDESITE_STAIRS(new String[0]),
   POLISHED_BASALT(new String[0]),
   POLISHED_BLACKSTONE(new String[0]),
   POLISHED_BLACKSTONE_BRICKS(new String[0]),
   POLISHED_BLACKSTONE_BRICK_SLAB(new String[0]),
   POLISHED_BLACKSTONE_BRICK_STAIRS(new String[0]),
   POLISHED_BLACKSTONE_BRICK_WALL(new String[0]),
   POLISHED_BLACKSTONE_BUTTON(new String[0]),
   POLISHED_BLACKSTONE_PRESSURE_PLATE(new String[0]),
   POLISHED_BLACKSTONE_SLAB(new String[0]),
   POLISHED_BLACKSTONE_STAIRS(new String[0]),
   POLISHED_BLACKSTONE_WALL(new String[0]),
   POLISHED_DEEPSLATE(new String[0]),
   POLISHED_DEEPSLATE_SLAB(new String[0]),
   POLISHED_DEEPSLATE_STAIRS(new String[0]),
   POLISHED_DEEPSLATE_WALL(new String[0]),
   POLISHED_DIORITE(4, new String[]{"STONE"}),
   POLISHED_DIORITE_SLAB(new String[0]),
   POLISHED_DIORITE_STAIRS(new String[0]),
   POLISHED_GRANITE(2, new String[]{"STONE"}),
   POLISHED_GRANITE_SLAB(new String[0]),
   POLISHED_GRANITE_STAIRS(new String[0]),
   POPPED_CHORUS_FRUIT(new String[]{"CHORUS_FRUIT_POPPED"}),
   POPPY(new String[]{"RED_ROSE"}),
   PORKCHOP(new String[]{"PORK"}),
   POTATO(new String[]{"POTATO_ITEM"}),
   POTATOES(new String[]{"POTATO"}),
   POTION(new String[0]),
   POTTED_ACACIA_SAPLING(4, new String[]{"FLOWER_POT"}),
   POTTED_ALLIUM(2, new String[]{"FLOWER_POT"}),
   POTTED_AZALEA_BUSH(new String[0]),
   POTTED_AZURE_BLUET(3, new String[]{"FLOWER_POT"}),
   POTTED_BAMBOO(new String[0]),
   POTTED_BIRCH_SAPLING(2, new String[]{"FLOWER_POT"}),
   POTTED_BLUE_ORCHID(1, new String[]{"FLOWER_POT"}),
   POTTED_BROWN_MUSHROOM(new String[]{"FLOWER_POT"}),
   POTTED_CACTUS(new String[]{"FLOWER_POT"}),
   POTTED_CHERRY_SAPLING(new String[0]),
   POTTED_CORNFLOWER(new String[0]),
   POTTED_CRIMSON_FUNGUS(new String[0]),
   POTTED_CRIMSON_ROOTS(new String[0]),
   POTTED_DANDELION(new String[]{"FLOWER_POT"}),
   POTTED_DARK_OAK_SAPLING(5, new String[]{"FLOWER_POT"}),
   POTTED_DEAD_BUSH(new String[]{"FLOWER_POT"}),
   POTTED_FERN(2, new String[]{"FLOWER_POT"}),
   POTTED_FLOWERING_AZALEA_BUSH(new String[0]),
   POTTED_JUNGLE_SAPLING(3, new String[]{"FLOWER_POT"}),
   POTTED_LILY_OF_THE_VALLEY(new String[0]),
   POTTED_MANGROVE_PROPAGULE(new String[0]),
   POTTED_OAK_SAPLING(new String[]{"FLOWER_POT"}),
   POTTED_ORANGE_TULIP(5, new String[]{"FLOWER_POT"}),
   POTTED_OXEYE_DAISY(8, new String[]{"FLOWER_POT"}),
   POTTED_PINK_TULIP(7, new String[]{"FLOWER_POT"}),
   POTTED_POPPY(new String[]{"FLOWER_POT"}),
   POTTED_RED_MUSHROOM(new String[]{"FLOWER_POT"}),
   POTTED_RED_TULIP(4, new String[]{"FLOWER_POT"}),
   POTTED_SPRUCE_SAPLING(1, new String[]{"FLOWER_POT"}),
   POTTED_TORCHFLOWER(new String[0]),
   POTTED_WARPED_FUNGUS(new String[0]),
   POTTED_WARPED_ROOTS(new String[0]),
   POTTED_WHITE_TULIP(6, new String[]{"FLOWER_POT"}),
   POTTED_WITHER_ROSE(new String[0]),
   POTTERY_SHARD_ARCHER(new String[0]),
   POTTERY_SHARD_ARMS_UP(new String[0]),
   POTTERY_SHARD_PRIZE(new String[0]),
   POTTERY_SHARD_SKULL(new String[0]),
   POWDER_SNOW(new String[0]),
   POWDER_SNOW_BUCKET(new String[0]),
   POWDER_SNOW_CAULDRON(new String[0]),
   POWERED_RAIL(new String[0]),
   PRISMARINE(new String[0]),
   PRISMARINE_BRICKS(1, new String[]{"PRISMARINE"}),
   PRISMARINE_BRICK_SLAB(new String[0]),
   PRISMARINE_BRICK_STAIRS(new String[0]),
   PRISMARINE_CRYSTALS(new String[0]),
   PRISMARINE_SHARD(new String[0]),
   PRISMARINE_SLAB(new String[0]),
   PRISMARINE_STAIRS(new String[0]),
   PRISMARINE_WALL(new String[0]),
   PRIZE_POTTERY_SHERD(new String[0]),
   PUFFERFISH(3, new String[]{"RAW_FISH"}),
   PUFFERFISH_BUCKET(new String[0]),
   PUFFERFISH_SPAWN_EGG(new String[0]),
   PUMPKIN(new String[0]),
   PUMPKIN_PIE(new String[0]),
   PUMPKIN_SEEDS(new String[0]),
   PUMPKIN_STEM(new String[0]),
   PURPLE_BANNER(5, new String[]{"STANDING_BANNER", "BANNER"}),
   PURPLE_BED(supports(12) ? 10 : 0, new String[]{"BED_BLOCK", "BED"}),
   PURPLE_CANDLE(new String[0]),
   PURPLE_CANDLE_CAKE(new String[0]),
   PURPLE_CARPET(10, new String[]{"CARPET"}),
   PURPLE_CONCRETE(10, new String[]{"CONCRETE"}),
   PURPLE_CONCRETE_POWDER(10, new String[]{"CONCRETE_POWDER"}),
   PURPLE_DYE(5, new String[]{"INK_SACK"}),
   PURPLE_GLAZED_TERRACOTTA(new String[0]),
   PURPLE_SHULKER_BOX(new String[0]),
   PURPLE_STAINED_GLASS(10, new String[]{"STAINED_GLASS"}),
   PURPLE_STAINED_GLASS_PANE(10, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   PURPLE_TERRACOTTA(10, new String[]{"STAINED_CLAY"}),
   PURPLE_WALL_BANNER(5, new String[]{"WALL_BANNER"}),
   PURPLE_WOOL(10, new String[]{"WOOL"}),
   PURPUR_BLOCK(new String[0]),
   PURPUR_PILLAR(new String[0]),
   PURPUR_SLAB(new String[]{"PURPUR_DOUBLE_SLAB"}),
   PURPUR_STAIRS(new String[0]),
   QUARTZ(new String[0]),
   QUARTZ_BLOCK(new String[0]),
   QUARTZ_BRICKS(new String[0]),
   QUARTZ_PILLAR(2, new String[]{"QUARTZ_BLOCK"}),
   QUARTZ_SLAB(7, new String[]{"STEP"}),
   QUARTZ_STAIRS(new String[0]),
   RABBIT(new String[0]),
   RABBIT_FOOT(new String[0]),
   RABBIT_HIDE(new String[0]),
   RABBIT_SPAWN_EGG(101, new String[]{"MONSTER_EGG"}),
   RABBIT_STEW(new String[0]),
   RAIL(new String[]{"RAILS"}),
   RAISER_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   RAVAGER_SPAWN_EGG(new String[0]),
   RAW_COPPER(new String[0]),
   RAW_COPPER_BLOCK(new String[0]),
   RAW_GOLD(new String[0]),
   RAW_GOLD_BLOCK(new String[0]),
   RAW_IRON(new String[0]),
   RAW_IRON_BLOCK(new String[0]),
   RECOVERY_COMPASS(new String[0]),
   REDSTONE(new String[0]),
   REDSTONE_BLOCK(new String[0]),
   REDSTONE_LAMP(new String[]{"REDSTONE_LAMP_ON", "REDSTONE_LAMP_OFF"}),
   REDSTONE_ORE(new String[]{"GLOWING_REDSTONE_ORE"}),
   REDSTONE_TORCH(new String[]{"REDSTONE_TORCH_OFF", "REDSTONE_TORCH_ON"}),
   REDSTONE_WALL_TORCH(new String[0]),
   REDSTONE_WIRE(new String[0]),
   RED_BANNER(1, new String[]{"STANDING_BANNER", "BANNER"}),
   RED_BED(supports(12) ? 14 : 0, new String[]{"BED_BLOCK", "BED"}),
   RED_CANDLE(new String[0]),
   RED_CANDLE_CAKE(new String[0]),
   RED_CARPET(14, new String[]{"CARPET"}),
   RED_CONCRETE(14, new String[]{"CONCRETE"}),
   RED_CONCRETE_POWDER(14, new String[]{"CONCRETE_POWDER"}),
   RED_DYE(1, new String[]{"INK_SACK", "ROSE_RED"}),
   RED_GLAZED_TERRACOTTA(new String[0]),
   RED_MUSHROOM(new String[0]),
   RED_MUSHROOM_BLOCK(new String[]{"RED_MUSHROOM", "HUGE_MUSHROOM_2"}),
   RED_NETHER_BRICKS(new String[]{"RED_NETHER_BRICK"}),
   RED_NETHER_BRICK_SLAB(new String[0]),
   RED_NETHER_BRICK_STAIRS(new String[0]),
   RED_NETHER_BRICK_WALL(new String[0]),
   RED_SAND(1, new String[]{"SAND"}),
   RED_SANDSTONE(new String[0]),
   RED_SANDSTONE_SLAB(new String[]{"DOUBLE_STONE_SLAB2", "STONE_SLAB2"}),
   RED_SANDSTONE_STAIRS(new String[0]),
   RED_SANDSTONE_WALL(new String[0]),
   RED_SHULKER_BOX(new String[0]),
   RED_STAINED_GLASS(14, new String[]{"STAINED_GLASS"}),
   RED_STAINED_GLASS_PANE(14, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   RED_TERRACOTTA(14, new String[]{"STAINED_CLAY"}),
   RED_TULIP(4, new String[]{"RED_ROSE"}),
   RED_WALL_BANNER(1, new String[]{"WALL_BANNER"}),
   RED_WOOL(14, new String[]{"WOOL"}),
   REINFORCED_DEEPSLATE(new String[0]),
   REPEATER(new String[]{"DIODE_BLOCK_ON", "DIODE_BLOCK_OFF", "DIODE"}),
   REPEATING_COMMAND_BLOCK(new String[]{"COMMAND", "COMMAND_REPEATING"}),
   RESPAWN_ANCHOR(new String[0]),
   RIB_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   ROOTED_DIRT(new String[0]),
   ROSE_BUSH(4, new String[]{"DOUBLE_PLANT"}),
   ROTTEN_FLESH(new String[0]),
   SADDLE(new String[0]),
   SALMON(1, new String[]{"RAW_FISH"}),
   SALMON_BUCKET(new String[0]),
   SALMON_SPAWN_EGG(new String[0]),
   SAND(new String[0]),
   SANDSTONE(new String[0]),
   SANDSTONE_SLAB(1, new String[]{"DOUBLE_STEP", "STEP", "STONE_SLAB"}),
   SANDSTONE_STAIRS(new String[0]),
   SANDSTONE_WALL(new String[0]),
   SCAFFOLDING(new String[0]),
   SCULK(new String[0]),
   SCULK_CATALYST(new String[0]),
   SCULK_SENSOR(new String[0]),
   SCULK_SHRIEKER(new String[0]),
   SCULK_VEIN(new String[0]),
   SCUTE(new String[0]),
   SEAGRASS(new String[0]),
   SEA_LANTERN(new String[0]),
   SEA_PICKLE(new String[0]),
   SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   SHEAF_POTTERY_SHERD(new String[0]),
   SHEARS(new String[0]),
   SHEEP_SPAWN_EGG(91, new String[]{"MONSTER_EGG"}),
   SHELTER_POTTERY_SHERD(new String[0]),
   SHIELD(new String[0]),
   SHROOMLIGHT(new String[0]),
   SHULKER_BOX(new String[]{"PURPLE_SHULKER_BOX"}),
   SHULKER_SHELL(new String[0]),
   SHULKER_SPAWN_EGG(69, new String[]{"MONSTER_EGG"}),
   SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   SILVERFISH_SPAWN_EGG(60, new String[]{"MONSTER_EGG"}),
   SKELETON_HORSE_SPAWN_EGG(28, new String[]{"MONSTER_EGG"}),
   SKELETON_SKULL(new String[]{"SKULL", "SKULL_ITEM"}),
   SKELETON_SPAWN_EGG(51, new String[]{"MONSTER_EGG"}),
   SKELETON_WALL_SKULL(new String[]{"SKULL", "SKULL_ITEM"}),
   SKULL_BANNER_PATTERN(new String[0]),
   SKULL_POTTERY_SHERD(new String[0]),
   SLIME_BALL(new String[0]),
   SLIME_BLOCK(new String[0]),
   SLIME_SPAWN_EGG(55, new String[]{"MONSTER_EGG"}),
   SMALL_AMETHYST_BUD(new String[0]),
   SMALL_DRIPLEAF(new String[0]),
   SMITHING_TABLE(new String[0]),
   SMOKER(new String[0]),
   SMOOTH_BASALT(new String[0]),
   SMOOTH_QUARTZ(new String[0]),
   SMOOTH_QUARTZ_SLAB(new String[0]),
   SMOOTH_QUARTZ_STAIRS(new String[0]),
   SMOOTH_RED_SANDSTONE(2, new String[]{"RED_SANDSTONE"}),
   SMOOTH_RED_SANDSTONE_SLAB(new String[]{"STONE_SLAB2"}),
   SMOOTH_RED_SANDSTONE_STAIRS(new String[0]),
   SMOOTH_SANDSTONE(2, new String[]{"SANDSTONE"}),
   SMOOTH_SANDSTONE_SLAB(new String[0]),
   SMOOTH_SANDSTONE_STAIRS(new String[0]),
   SMOOTH_STONE(new String[0]),
   SMOOTH_STONE_SLAB(new String[0]),
   SNIFFER_EGG(new String[0]),
   SNIFFER_SPAWN_EGG(new String[0]),
   SNORT_POTTERY_SHERD(new String[0]),
   SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   SNOW(new String[0]),
   SNOWBALL(new String[]{"SNOW_BALL"}),
   SNOW_BLOCK(new String[0]),
   SNOW_GOLEM_SPAWN_EGG(new String[0]),
   SOUL_CAMPFIRE(new String[0]),
   SOUL_FIRE(new String[0]),
   SOUL_LANTERN(new String[0]),
   SOUL_SAND(new String[0]),
   SOUL_SOIL(new String[0]),
   SOUL_TORCH(new String[0]),
   SOUL_WALL_TORCH(new String[0]),
   SPAWNER(new String[]{"MOB_SPAWNER"}),
   SPECTRAL_ARROW(new String[0]),
   SPIDER_EYE(new String[0]),
   SPIDER_SPAWN_EGG(52, new String[]{"MONSTER_EGG"}),
   SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   SPLASH_POTION(new String[0]),
   SPONGE(new String[0]),
   SPORE_BLOSSOM(new String[0]),
   SPRUCE_BOAT(new String[]{"BOAT_SPRUCE"}),
   SPRUCE_BUTTON(new String[]{"WOOD_BUTTON"}),
   SPRUCE_CHEST_BOAT(new String[0]),
   SPRUCE_DOOR(new String[]{"SPRUCE_DOOR", "SPRUCE_DOOR_ITEM"}),
   SPRUCE_FENCE(new String[0]),
   SPRUCE_FENCE_GATE(new String[0]),
   SPRUCE_HANGING_SIGN(new String[0]),
   SPRUCE_LEAVES(1, new String[]{"LEAVES"}),
   SPRUCE_LOG(1, new String[]{"LOG"}),
   SPRUCE_PLANKS(1, new String[]{"WOOD"}),
   SPRUCE_PRESSURE_PLATE(new String[]{"WOOD_PLATE"}),
   SPRUCE_SAPLING(1, new String[]{"SAPLING"}),
   SPRUCE_SIGN(new String[]{"SIGN_POST", "SIGN"}),
   SPRUCE_SLAB(1, new String[]{"WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB"}),
   SPRUCE_STAIRS(new String[]{"SPRUCE_WOOD_STAIRS"}),
   SPRUCE_TRAPDOOR(new String[]{"TRAP_DOOR"}),
   SPRUCE_WALL_HANGING_SIGN(new String[0]),
   SPRUCE_WALL_SIGN(new String[]{"WALL_SIGN"}),
   SPRUCE_WOOD(1, new String[]{"LOG"}),
   SPYGLASS(new String[0]),
   SQUID_SPAWN_EGG(94, new String[]{"MONSTER_EGG"}),
   STICK(new String[0]),
   STICKY_PISTON(new String[]{"PISTON_BASE", "PISTON_STICKY_BASE"}),
   STONE(new String[0]),
   STONECUTTER(new String[0]),
   STONE_AXE(new String[0]),
   STONE_BRICKS(new String[]{"SMOOTH_BRICK"}),
   STONE_BRICK_SLAB(5, new String[]{"DOUBLE_STEP", "STEP", "STONE_SLAB"}),
   STONE_BRICK_STAIRS(new String[]{"SMOOTH_STAIRS"}),
   STONE_BRICK_WALL(new String[0]),
   STONE_BUTTON(new String[0]),
   STONE_HOE(new String[0]),
   STONE_PICKAXE(new String[0]),
   STONE_PRESSURE_PLATE(new String[]{"STONE_PLATE"}),
   STONE_SHOVEL(new String[]{"STONE_SPADE"}),
   STONE_SLAB(new String[]{"DOUBLE_STEP", "STEP"}),
   STONE_STAIRS(new String[0]),
   STONE_SWORD(new String[0]),
   STRAY_SPAWN_EGG(6, new String[]{"MONSTER_EGG"}),
   STRIDER_SPAWN_EGG(new String[0]),
   STRING(new String[0]),
   STRIPPED_ACACIA_LOG(new String[0]),
   STRIPPED_ACACIA_WOOD(new String[0]),
   STRIPPED_BAMBOO_BLOCK(new String[0]),
   STRIPPED_BIRCH_LOG(new String[0]),
   STRIPPED_BIRCH_WOOD(new String[0]),
   STRIPPED_CHERRY_LOG(new String[0]),
   STRIPPED_CHERRY_WOOD(new String[0]),
   STRIPPED_CRIMSON_HYPHAE(new String[0]),
   STRIPPED_CRIMSON_STEM(new String[0]),
   STRIPPED_DARK_OAK_LOG(new String[0]),
   STRIPPED_DARK_OAK_WOOD(new String[0]),
   STRIPPED_JUNGLE_LOG(new String[0]),
   STRIPPED_JUNGLE_WOOD(new String[0]),
   STRIPPED_MANGROVE_LOG(new String[0]),
   STRIPPED_MANGROVE_WOOD(new String[0]),
   STRIPPED_OAK_LOG(new String[0]),
   STRIPPED_OAK_WOOD(new String[0]),
   STRIPPED_SPRUCE_LOG(new String[0]),
   STRIPPED_SPRUCE_WOOD(new String[0]),
   STRIPPED_WARPED_HYPHAE(new String[0]),
   STRIPPED_WARPED_STEM(new String[0]),
   STRUCTURE_BLOCK(new String[0]),
   STRUCTURE_VOID(10, new String[]{"BARRIER"}),
   SUGAR(new String[0]),
   SUGAR_CANE(new String[]{"SUGAR_CANE_BLOCK"}),
   SUNFLOWER(new String[]{"DOUBLE_PLANT"}),
   SUSPICIOUS_GRAVEL(new String[0]),
   SUSPICIOUS_SAND(new String[0]),
   SUSPICIOUS_STEW(new String[0]),
   SWEET_BERRIES(new String[0]),
   SWEET_BERRY_BUSH(new String[0]),
   TADPOLE_BUCKET(new String[0]),
   TADPOLE_SPAWN_EGG(new String[0]),
   TALL_GRASS(2, new String[]{"DOUBLE_PLANT"}),
   TALL_SEAGRASS(new String[0]),
   TARGET(new String[0]),
   TERRACOTTA(new String[]{"HARD_CLAY"}),
   TIDE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   TINTED_GLASS(new String[0]),
   TIPPED_ARROW(new String[0]),
   TNT(new String[0]),
   TNT_MINECART(new String[]{"EXPLOSIVE_MINECART"}),
   TORCH(new String[0]),
   TORCHFLOWER(new String[0]),
   TORCHFLOWER_CROP(new String[0]),
   TORCHFLOWER_SEEDS(new String[0]),
   TOTEM_OF_UNDYING(new String[]{"TOTEM"}),
   TRADER_LLAMA_SPAWN_EGG(new String[0]),
   TRAPPED_CHEST(new String[0]),
   TRIDENT(new String[0]),
   TRIPWIRE(new String[0]),
   TRIPWIRE_HOOK(new String[0]),
   TROPICAL_FISH(2, new String[]{"RAW_FISH"}),
   TROPICAL_FISH_BUCKET(new String[]{"BUCKET", "WATER_BUCKET"}),
   TROPICAL_FISH_SPAWN_EGG(new String[]{"MONSTER_EGG"}),
   TUBE_CORAL(new String[0]),
   TUBE_CORAL_BLOCK(new String[0]),
   TUBE_CORAL_FAN(new String[0]),
   TUBE_CORAL_WALL_FAN(new String[0]),
   TUFF(new String[0]),
   TURTLE_EGG(new String[0]),
   TURTLE_HELMET(new String[0]),
   TURTLE_SPAWN_EGG(new String[0]),
   TWISTING_VINES(new String[0]),
   TWISTING_VINES_PLANT(new String[0]),
   VERDANT_FROGLIGHT(new String[0]),
   VEX_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   VEX_SPAWN_EGG(35, new String[]{"MONSTER_EGG"}),
   VILLAGER_SPAWN_EGG(120, new String[]{"MONSTER_EGG"}),
   VINDICATOR_SPAWN_EGG(36, new String[]{"MONSTER_EGG"}),
   VINE(new String[0]),
   VOID_AIR(new String[]{"AIR"}),
   WALL_TORCH(new String[]{"TORCH"}),
   WANDERING_TRADER_SPAWN_EGG(new String[0]),
   WARDEN_SPAWN_EGG(new String[0]),
   WARD_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   WARPED_BUTTON(new String[0]),
   WARPED_DOOR(new String[0]),
   WARPED_FENCE(new String[0]),
   WARPED_FENCE_GATE(new String[0]),
   WARPED_FUNGUS(new String[0]),
   WARPED_FUNGUS_ON_A_STICK(new String[0]),
   WARPED_HANGING_SIGN(new String[0]),
   WARPED_HYPHAE(new String[0]),
   WARPED_NYLIUM(new String[0]),
   WARPED_PLANKS(new String[0]),
   WARPED_PRESSURE_PLATE(new String[0]),
   WARPED_ROOTS(new String[0]),
   WARPED_SIGN(new String[]{"SIGN_POST"}),
   WARPED_SLAB(new String[0]),
   WARPED_STAIRS(new String[0]),
   WARPED_STEM(new String[0]),
   WARPED_TRAPDOOR(new String[0]),
   WARPED_WALL_HANGING_SIGN(new String[0]),
   WARPED_WALL_SIGN(new String[]{"WALL_SIGN"}),
   WARPED_WART_BLOCK(new String[0]),
   WATER(new String[]{"STATIONARY_WATER"}),
   WATER_BUCKET(new String[0]),
   WATER_CAULDRON(new String[0]),
   WAXED_COPPER_BLOCK(new String[0]),
   WAXED_CUT_COPPER(new String[0]),
   WAXED_CUT_COPPER_SLAB(new String[0]),
   WAXED_CUT_COPPER_STAIRS(new String[0]),
   WAXED_EXPOSED_COPPER(new String[0]),
   WAXED_EXPOSED_CUT_COPPER(new String[0]),
   WAXED_EXPOSED_CUT_COPPER_SLAB(new String[0]),
   WAXED_EXPOSED_CUT_COPPER_STAIRS(new String[0]),
   WAXED_OXIDIZED_COPPER(new String[0]),
   WAXED_OXIDIZED_CUT_COPPER(new String[0]),
   WAXED_OXIDIZED_CUT_COPPER_SLAB(new String[0]),
   WAXED_OXIDIZED_CUT_COPPER_STAIRS(new String[0]),
   WAXED_WEATHERED_COPPER(new String[0]),
   WAXED_WEATHERED_CUT_COPPER(new String[0]),
   WAXED_WEATHERED_CUT_COPPER_SLAB(new String[0]),
   WAXED_WEATHERED_CUT_COPPER_STAIRS(new String[0]),
   WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   WEATHERED_COPPER(new String[0]),
   WEATHERED_CUT_COPPER(new String[0]),
   WEATHERED_CUT_COPPER_SLAB(new String[0]),
   WEATHERED_CUT_COPPER_STAIRS(new String[0]),
   WEEPING_VINES(new String[0]),
   WEEPING_VINES_PLANT(new String[0]),
   WET_SPONGE(1, new String[]{"SPONGE"}),
   WHEAT(new String[]{"CROPS"}),
   WHEAT_SEEDS(new String[]{"SEEDS"}),
   WHITE_BANNER(15, new String[]{"STANDING_BANNER", "BANNER"}),
   WHITE_BED(new String[]{"BED_BLOCK", "BED"}),
   WHITE_CANDLE(new String[0]),
   WHITE_CANDLE_CAKE(new String[0]),
   WHITE_CARPET(new String[]{"CARPET"}),
   WHITE_CONCRETE(new String[]{"CONCRETE"}),
   WHITE_CONCRETE_POWDER(new String[]{"CONCRETE_POWDER"}),
   WHITE_DYE(15, new String[]{"INK_SACK", "BONE_MEAL"}),
   WHITE_GLAZED_TERRACOTTA(new String[0]),
   WHITE_SHULKER_BOX(new String[0]),
   WHITE_STAINED_GLASS(new String[]{"STAINED_GLASS"}),
   WHITE_STAINED_GLASS_PANE(new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   WHITE_TERRACOTTA(new String[]{"STAINED_CLAY"}),
   WHITE_TULIP(6, new String[]{"RED_ROSE"}),
   WHITE_WALL_BANNER(15, new String[]{"WALL_BANNER"}),
   WHITE_WOOL(new String[]{"WOOL"}),
   WILD_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]),
   WITCH_SPAWN_EGG(66, new String[]{"MONSTER_EGG"}),
   WITHER_ROSE(new String[0]),
   WITHER_SKELETON_SKULL(1, new String[]{"SKULL", "SKULL_ITEM"}),
   WITHER_SKELETON_SPAWN_EGG(5, new String[]{"MONSTER_EGG"}),
   WITHER_SKELETON_WALL_SKULL(1, new String[]{"SKULL", "SKULL_ITEM"}),
   WITHER_SPAWN_EGG(new String[0]),
   WOLF_SPAWN_EGG(95, new String[]{"MONSTER_EGG"}),
   WOODEN_AXE(new String[]{"WOOD_AXE"}),
   WOODEN_HOE(new String[]{"WOOD_HOE"}),
   WOODEN_PICKAXE(new String[]{"WOOD_PICKAXE"}),
   WOODEN_SHOVEL(new String[]{"WOOD_SPADE"}),
   WOODEN_SWORD(new String[]{"WOOD_SWORD"}),
   WRITABLE_BOOK(new String[]{"BOOK_AND_QUILL"}),
   WRITTEN_BOOK(new String[0]),
   YELLOW_BANNER(11, new String[]{"STANDING_BANNER", "BANNER"}),
   YELLOW_BED(supports(12) ? 4 : 0, new String[]{"BED_BLOCK", "BED"}),
   YELLOW_CANDLE(new String[0]),
   YELLOW_CANDLE_CAKE(new String[0]),
   YELLOW_CARPET(4, new String[]{"CARPET"}),
   YELLOW_CONCRETE(4, new String[]{"CONCRETE"}),
   YELLOW_CONCRETE_POWDER(4, new String[]{"CONCRETE_POWDER"}),
   YELLOW_DYE(11, new String[]{"INK_SACK", "DANDELION_YELLOW"}),
   YELLOW_GLAZED_TERRACOTTA(new String[0]),
   YELLOW_SHULKER_BOX(new String[0]),
   YELLOW_STAINED_GLASS(4, new String[]{"STAINED_GLASS"}),
   YELLOW_STAINED_GLASS_PANE(4, new String[]{"THIN_GLASS", "STAINED_GLASS_PANE"}),
   YELLOW_TERRACOTTA(4, new String[]{"STAINED_CLAY"}),
   YELLOW_WALL_BANNER(11, new String[]{"WALL_BANNER"}),
   YELLOW_WOOL(4, new String[]{"WOOL"}),
   ZOGLIN_SPAWN_EGG(new String[0]),
   ZOMBIE_HEAD(2, new String[]{"SKULL", "SKULL_ITEM"}),
   ZOMBIE_HORSE_SPAWN_EGG(29, new String[]{"MONSTER_EGG"}),
   ZOMBIE_SPAWN_EGG(54, new String[]{"MONSTER_EGG"}),
   ZOMBIE_VILLAGER_SPAWN_EGG(27, new String[]{"MONSTER_EGG"}),
   ZOMBIE_WALL_HEAD(2, new String[]{"SKULL", "SKULL_ITEM"}),
   ZOMBIFIED_PIGLIN_SPAWN_EGG(57, new String[]{"MONSTER_EGG", "ZOMBIE_PIGMAN_SPAWN_EGG"});

   public static final XMaterial[] VALUES = values();
   private static final Map<String, XMaterial> NAMES = new HashMap();
   private static final Cache<String, XMaterial> NAME_CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.HOURS).build();
   private static final Cache<String, Pattern> CACHED_REGEX = CacheBuilder.newBuilder().expireAfterAccess(3L, TimeUnit.HOURS).build();
   private static final byte MAX_DATA_VALUE = 120;
   private static final byte UNKNOWN_DATA_VALUE = -1;
   private static final short MAX_ID = 2267;
   private static final Set<String> DUPLICATED;
   private final byte data;
   @Nonnull
   private final String[] legacy;
   @Nullable
   private final Material material;

   private XMaterial(int data, @Nonnull String... legacy) {
      this.data = (byte)data;
      this.legacy = legacy;
      Material mat = null;
      if (!XMaterial.Data.ISFLAT && this.isDuplicated() || (mat = Material.getMaterial(this.name())) == null) {
         for(int i = legacy.length - 1; i >= 0; --i) {
            mat = Material.getMaterial(legacy[i]);
            if (mat != null) {
               break;
            }
         }
      }

      this.material = mat;
   }

   private XMaterial(String... legacy) {
      this(0, legacy);
   }

   @Nonnull
   private static Optional<XMaterial> getIfPresent(@Nonnull String name) {
      return Optional.ofNullable((XMaterial)NAMES.get(name));
   }

   public static int getVersion() {
      return XMaterial.Data.VERSION;
   }

   @Nullable
   private static XMaterial requestOldXMaterial(@Nonnull String name, byte data) {
      String holder = name + data;
      XMaterial cache = (XMaterial)NAME_CACHE.getIfPresent(holder);
      if (cache != null) {
         return cache;
      } else {
         for (XMaterial material : VALUES) {
            if ((data == -1 || data == material.data) && material.anyMatchLegacy(name)) {
               NAME_CACHE.put(holder, material);
               return material;
            }
         }

         return null;
      }
   }

   @Nonnull
   private static Optional<XMaterial> matchXMaterialWithData(@Nonnull String name) {
      int index = name.indexOf(58);
      if (index != -1) {
         String mat = format(name.substring(0, index));

         try {
            byte data = (byte)Integer.parseInt(name.substring(index + 1).replace(" ", ""));
            return data >= 0 && data < 120 ? matchDefinedXMaterial(mat, data) : matchDefinedXMaterial(mat, (byte)-1);
         } catch (NumberFormatException ignored) {
            return matchDefinedXMaterial(mat, (byte)-1);
         }
      } else {
         return Optional.empty();
      }
   }

   @Nonnull
   public static Optional<XMaterial> matchXMaterial(@Nonnull String name) {
      if (name != null && !name.isEmpty()) {
         Optional<XMaterial> oldMatch = matchXMaterialWithData(name);
         return oldMatch.isPresent() ? oldMatch : matchDefinedXMaterial(format(name), (byte)-1);
      } else {
         throw new IllegalArgumentException("Cannot match a material with null or empty material name");
      }
   }

   @Nonnull
   public static XMaterial matchXMaterial(@Nonnull Material material) {
      Objects.requireNonNull(material, "Cannot match null material");
      return (XMaterial)matchDefinedXMaterial(material.name(), (byte)-1).orElseThrow(() -> {
         return new IllegalArgumentException("Unsupported material with no data value: " + material.name());
      });
   }

   @Nonnull
   public static XMaterial matchXMaterial(@Nonnull ItemStack item) {
      Objects.requireNonNull(item, "Cannot match null ItemStack");
      String material = item.getType().name();
      byte data = (byte)(!XMaterial.Data.ISFLAT && !material.equals("MAP") && item.getType().getMaxDurability() <= 0 ? item.getDurability() : 0);
      if (supports(9) && !supports(13) && item.hasItemMeta() && material.equals("MONSTER_EGG")) {
         ItemMeta meta = item.getItemMeta();
         if (isSpawnEggMeta(meta)) {
            try {
               Object spawnedType = meta.getClass().getMethod("getSpawnedType").invoke(meta);
               if (spawnedType != null) material = spawnedType.getClass().getMethod("name").invoke(spawnedType) + "_SPAWN_EGG";
            } catch (Exception ignored) {
            }
         }
      }

      if (!supports(9) && material.endsWith("ION")) {
         return Potion.fromItemStack(item).isSplash() ? SPLASH_POTION : POTION;
      } else {
         if (supports(13) && !supports(14)) {
            if (material.equals("CACTUS_GREEN")) {
               return GREEN_DYE;
            }

            if (material.equals("ROSE_RED")) {
               return RED_DYE;
            }

            if (material.equals("DANDELION_YELLOW")) {
               return YELLOW_DYE;
            }
         }

         Optional<XMaterial> result = matchDefinedXMaterial(material, data);
         if (result.isPresent()) {
            return (XMaterial)result.get();
         } else {
            throw new IllegalArgumentException("Unsupported material from item: " + material + " (" + data + ')');
         }
      }
   }

   /** @deprecated */
   @Nonnull
   @Deprecated
   public static Optional<XMaterial> matchXMaterial(int id, byte data) {
      if (id >= 0 && id <= 2267 && data >= 0) {
         for (XMaterial mat : VALUES) {
            if (mat.data == data && mat.getId() == id) {
               return Optional.of(mat);
            }
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   @Nonnull
   protected static Optional<XMaterial> matchDefinedXMaterial(@Nonnull String name, byte data) {
      boolean isAMap = name.equalsIgnoreCase("MAP");
      boolean duplicated = isDuplicated(name);
      if (XMaterial.Data.ISFLAT || (!isAMap && data <= 0 && !duplicated)) {
         Optional<XMaterial> xMaterial = getIfPresent(name);
         if (xMaterial.isPresent()) {
            return xMaterial;
         }
      }

      XMaterial oldXMaterial = requestOldXMaterial(name, data);
      if (oldXMaterial == null) {
         return data >= 0 && isAMap ? Optional.of(FILLED_MAP) : Optional.empty();
      } else {
         boolean isPlural = oldXMaterial == CARROTS || oldXMaterial == POTATOES || oldXMaterial == BRICKS;
         if (!XMaterial.Data.ISFLAT && isPlural) {
            if (!duplicated) {
               if (isDuplicated(name)) {
                  return getIfPresent(name);
               }
            } else if (duplicated) {
               return getIfPresent(name);
            }
         }

         return Optional.of(oldXMaterial);
      }
   }

   @Nonnull
   protected static String format(@Nonnull String name) {
      int len = name.length();
      char[] chs = new char[len];
      int count = 0;
      boolean appendUnderline = false;

      for(int i = 0; i < len; ++i) {
         char ch = name.charAt(i);
         if (!appendUnderline && count != 0 && (ch == '-' || ch == ' ' || ch == '_') && chs[count] != '_') {
            appendUnderline = true;
         } else {
            boolean number = false;
            if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || (number = ch >= '0' && ch <= '9')) {
               if (appendUnderline) {
                  chs[count++] = '_';
                  appendUnderline = false;
               }

               if (number) {
                  chs[count++] = ch;
               } else {
                  chs[count++] = (char)(ch & 95);
               }
            }
         }
      }

      return new String(chs, 0, count);
   }

   public static boolean supports(int version) {
      return XMaterial.Data.VERSION >= version;
   }

   public String[] getLegacy() {
      return this.legacy;
   }

   /** @deprecated */
   @Deprecated
   public boolean isOneOf(@Nullable Collection<String> materials) {
      if (materials != null && !materials.isEmpty()) {
         String name = this.name();
         Iterator<String> materialIterator = materials.iterator();

         while (materialIterator.hasNext()) {
            String comp = materialIterator.next();
            String checker = comp.toUpperCase(Locale.ENGLISH);
            if (checker.startsWith("CONTAINS:")) {
               comp = format(checker.substring(9));
               if (name.contains(comp)) {
                  return true;
               }
            } else if (checker.startsWith("REGEX:")) {
               comp = comp.substring(6);
               Pattern pattern = (Pattern)CACHED_REGEX.getIfPresent(comp);
               if (pattern == null) {
                  try {
                     pattern = Pattern.compile(comp);
                     CACHED_REGEX.put(comp, pattern);
                  } catch (PatternSyntaxException e) {
                     e.printStackTrace();
                  }
               }

               if (pattern != null && pattern.matcher(name).matches()) {
                  return true;
               }
            } else {
               Optional<XMaterial> xMat = matchXMaterial(comp);
               if (xMat.isPresent() && xMat.get() == this) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Nonnull
   public ItemStack setType(@Nonnull ItemStack item) {
      Objects.requireNonNull(item, "Cannot set material for null ItemStack");
      Material material = this.parseMaterial();
      Objects.requireNonNull(material, () -> {
         return "Unsupported material: " + this.name();
      });
      item.setType(material);
      if (!XMaterial.Data.ISFLAT && material.getMaxDurability() <= 0) {
         item.setDurability((short)this.data);
      }

      return item;
   }

   private boolean anyMatchLegacy(@Nonnull String name) {
      for(int i = this.legacy.length - 1; i >= 0; --i) {
         if (name.equals(this.legacy[i])) {
            return true;
         }
      }

      return false;
   }

   @Nonnull
   public String toString() {
      return (String)Arrays.stream(this.name().split("_")).map((t) -> {
         return t.charAt(0) + t.substring(1).toLowerCase();
      }).collect(Collectors.joining(" "));
   }

   public int getId() {
      Material material = this.parseMaterial();
      if (material == null) {
         return -1;
      } else {
         try {
            return material.getId();
         } catch (IllegalArgumentException ignored) {
            return -1;
         }
      }
   }

   public byte getData() {
      return this.data;
   }

   @Nullable
   public ItemStack parseItem() {
      Material material = this.parseMaterial();
      if (material == null) {
         return null;
      } else {
         return XMaterial.Data.ISFLAT ? new ItemStack(material) : new ItemStack(material, 1, (short)this.data);
      }
   }

   @Nullable
   public Material parseMaterial() {
      return this.material;
   }

   public boolean isSimilar(@Nonnull ItemStack item) {
      Objects.requireNonNull(item, "Cannot compare with null ItemStack");
      if (item.getType() != this.parseMaterial()) {
         return false;
      } else {
         return XMaterial.Data.ISFLAT || item.getDurability() == this.data || item.getType().getMaxDurability() > 0;
      }
   }

   public boolean isSupported() {
      return this.material != null;
   }

   @Nullable
   public XMaterial or(@Nullable XMaterial alternateMaterial) {
      return this.isSupported() ? this : alternateMaterial;
   }

   private static boolean isSpawnEggMeta(ItemMeta meta) {
      if (meta == null) return false;
      try {
         return Class.forName("org.bukkit.inventory.meta.SpawnEggMeta").isInstance(meta);
      } catch (ClassNotFoundException e) {
         return false;
      }
   }

   private static boolean isDuplicated(@Nonnull String name) {
      return DUPLICATED.contains(name);
   }

   private boolean isDuplicated() {
      String name = this.name();
      byte dupIndex = -1;
      switch (name.hashCode()) {
      case -1929109465:
         if (name.equals("POTATO")) {
            dupIndex = 2;
         }
         break;
      case -1722057187:
         if (name.equals("DARK_OAK_DOOR")) {
            dupIndex = 6;
         }
         break;
      case -519277571:
         if (name.equals("BIRCH_DOOR")) {
            dupIndex = 8;
         }
         break;
      case -333218805:
         if (name.equals("SPRUCE_DOOR")) {
            dupIndex = 10;
         }
         break;
      case -328086150:
         if (name.equals("NETHER_BRICK")) {
            dupIndex = 5;
         }
         break;
      case 76092:
         if (name.equals("MAP")) {
            dupIndex = 11;
         }
         break;
      case 63467553:
         if (name.equals("BRICK")) {
            dupIndex = 4;
         }
         break;
      case 68077974:
         if (name.equals("GRASS")) {
            dupIndex = 3;
         }
         break;
      case 73242259:
         if (name.equals("MELON")) {
            dupIndex = 0;
         }
         break;
      case 478520881:
         if (name.equals("ACACIA_DOOR")) {
            dupIndex = 7;
         }
         break;
      case 868145122:
         if (name.equals("CAULDRON")) {
            dupIndex = 12;
         }
         break;
      case 1379814896:
         if (name.equals("JUNGLE_DOOR")) {
            dupIndex = 9;
         }
         break;
      case 1401892433:
         if (name.equals("FLOWER_POT")) {
            dupIndex = 14;
         }
         break;
      case 1545025079:
         if (name.equals("BREWING_STAND")) {
            dupIndex = 13;
         }
         break;
      case 1980706179:
         if (name.equals("CARROT")) {
            dupIndex = 1;
         }
      }

      switch (dupIndex) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
         return true;
      default:
         return false;
      }
   }

   // $FF: synthetic method
   private static XMaterial[] $values() {
      return new XMaterial[]{ACACIA_BOAT, ACACIA_BUTTON, ACACIA_CHEST_BOAT, ACACIA_DOOR, ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_HANGING_SIGN, ACACIA_LEAVES, ACACIA_LOG, ACACIA_PLANKS, ACACIA_PRESSURE_PLATE, ACACIA_SAPLING, ACACIA_SIGN, ACACIA_SLAB, ACACIA_STAIRS, ACACIA_TRAPDOOR, ACACIA_WALL_HANGING_SIGN, ACACIA_WALL_SIGN, ACACIA_WOOD, ACTIVATOR_RAIL, AIR, ALLAY_SPAWN_EGG, ALLIUM, AMETHYST_BLOCK, AMETHYST_CLUSTER, AMETHYST_SHARD, ANCIENT_DEBRIS, ANDESITE, ANDESITE_SLAB, ANDESITE_STAIRS, ANDESITE_WALL, ANGLER_POTTERY_SHERD, ANVIL, APPLE, ARCHER_POTTERY_SHERD, ARMOR_STAND, ARMS_UP_POTTERY_SHERD, ARROW, ATTACHED_MELON_STEM, ATTACHED_PUMPKIN_STEM, AXOLOTL_BUCKET, AXOLOTL_SPAWN_EGG, AZALEA, AZALEA_LEAVES, AZURE_BLUET, BAKED_POTATO, BAMBOO, BAMBOO_BLOCK, BAMBOO_BUTTON, BAMBOO_CHEST_RAFT, BAMBOO_DOOR, BAMBOO_FENCE, BAMBOO_FENCE_GATE, BAMBOO_HANGING_SIGN, BAMBOO_MOSAIC, BAMBOO_MOSAIC_SLAB, BAMBOO_MOSAIC_STAIRS, BAMBOO_PLANKS, BAMBOO_PRESSURE_PLATE, BAMBOO_RAFT, BAMBOO_SAPLING, BAMBOO_SIGN, BAMBOO_SLAB, BAMBOO_STAIRS, BAMBOO_TRAPDOOR, BAMBOO_WALL_HANGING_SIGN, BAMBOO_WALL_SIGN, BARREL, BARRIER, BASALT, BAT_SPAWN_EGG, BEACON, BEDROCK, BEEF, BEEHIVE, BEETROOT, BEETROOTS, BEETROOT_SEEDS, BEETROOT_SOUP, BEE_NEST, BEE_SPAWN_EGG, BELL, BIG_DRIPLEAF, BIG_DRIPLEAF_STEM, BIRCH_BOAT, BIRCH_BUTTON, BIRCH_CHEST_BOAT, BIRCH_DOOR, BIRCH_FENCE, BIRCH_FENCE_GATE, BIRCH_HANGING_SIGN, BIRCH_LEAVES, BIRCH_LOG, BIRCH_PLANKS, BIRCH_PRESSURE_PLATE, BIRCH_SAPLING, BIRCH_SIGN, BIRCH_SLAB, BIRCH_STAIRS, BIRCH_TRAPDOOR, BIRCH_WALL_HANGING_SIGN, BIRCH_WALL_SIGN, BIRCH_WOOD, BLACKSTONE, BLACKSTONE_SLAB, BLACKSTONE_STAIRS, BLACKSTONE_WALL, BLACK_BANNER, BLACK_BED, BLACK_CANDLE, BLACK_CANDLE_CAKE, BLACK_CARPET, BLACK_CONCRETE, BLACK_CONCRETE_POWDER, BLACK_DYE, BLACK_GLAZED_TERRACOTTA, BLACK_SHULKER_BOX, BLACK_STAINED_GLASS, BLACK_STAINED_GLASS_PANE, BLACK_TERRACOTTA, BLACK_WALL_BANNER, BLACK_WOOL, BLADE_POTTERY_SHERD, BLAST_FURNACE, BLAZE_POWDER, BLAZE_ROD, BLAZE_SPAWN_EGG, BLUE_BANNER, BLUE_BED, BLUE_CANDLE, BLUE_CANDLE_CAKE, BLUE_CARPET, BLUE_CONCRETE, BLUE_CONCRETE_POWDER, BLUE_DYE, BLUE_GLAZED_TERRACOTTA, BLUE_ICE, BLUE_ORCHID, BLUE_SHULKER_BOX, BLUE_STAINED_GLASS, BLUE_STAINED_GLASS_PANE, BLUE_TERRACOTTA, BLUE_WALL_BANNER, BLUE_WOOL, BONE, BONE_BLOCK, BONE_MEAL, BOOK, BOOKSHELF, BOW, BOWL, BRAIN_CORAL, BRAIN_CORAL_BLOCK, BRAIN_CORAL_FAN, BRAIN_CORAL_WALL_FAN, BREAD, BREWER_POTTERY_SHERD, BREWING_STAND, BRICK, BRICKS, BRICK_SLAB, BRICK_STAIRS, BRICK_WALL, BROWN_BANNER, BROWN_BED, BROWN_CANDLE, BROWN_CANDLE_CAKE, BROWN_CARPET, BROWN_CONCRETE, BROWN_CONCRETE_POWDER, BROWN_DYE, BROWN_GLAZED_TERRACOTTA, BROWN_MUSHROOM, BROWN_MUSHROOM_BLOCK, BROWN_SHULKER_BOX, BROWN_STAINED_GLASS, BROWN_STAINED_GLASS_PANE, BROWN_TERRACOTTA, BROWN_WALL_BANNER, BROWN_WOOL, BRUSH, BUBBLE_COLUMN, BUBBLE_CORAL, BUBBLE_CORAL_BLOCK, BUBBLE_CORAL_FAN, BUBBLE_CORAL_WALL_FAN, BUCKET, BUDDING_AMETHYST, BUNDLE, BURN_POTTERY_SHERD, CACTUS, CAKE, CALCITE, CALIBRATED_SCULK_SENSOR, CAMEL_SPAWN_EGG, CAMPFIRE, CANDLE, CANDLE_CAKE, CARROT, CARROTS, CARROT_ON_A_STICK, CARTOGRAPHY_TABLE, CARVED_PUMPKIN, CAT_SPAWN_EGG, CAULDRON, CAVE_AIR, CAVE_SPIDER_SPAWN_EGG, CAVE_VINES, CAVE_VINES_PLANT, CHAIN, CHAINMAIL_BOOTS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET, CHAINMAIL_LEGGINGS, CHAIN_COMMAND_BLOCK, CHARCOAL, CHERRY_BOAT, CHERRY_BUTTON, CHERRY_CHEST_BOAT, CHERRY_DOOR, CHERRY_FENCE, CHERRY_FENCE_GATE, CHERRY_HANGING_SIGN, CHERRY_LEAVES, CHERRY_LOG, CHERRY_PLANKS, CHERRY_PRESSURE_PLATE, CHERRY_SAPLING, CHERRY_SIGN, CHERRY_SLAB, CHERRY_STAIRS, CHERRY_TRAPDOOR, CHERRY_WALL_HANGING_SIGN, CHERRY_WALL_SIGN, CHERRY_WOOD, CHEST, CHEST_MINECART, CHICKEN, CHICKEN_SPAWN_EGG, CHIPPED_ANVIL, CHISELED_BOOKSHELF, CHISELED_DEEPSLATE, CHISELED_NETHER_BRICKS, CHISELED_POLISHED_BLACKSTONE, CHISELED_QUARTZ_BLOCK, CHISELED_RED_SANDSTONE, CHISELED_SANDSTONE, CHISELED_STONE_BRICKS, CHORUS_FLOWER, CHORUS_FRUIT, CHORUS_PLANT, CLAY, CLAY_BALL, CLOCK, COAL, COAL_BLOCK, COAL_ORE, COARSE_DIRT, COAST_ARMOR_TRIM_SMITHING_TEMPLATE, COBBLED_DEEPSLATE, COBBLED_DEEPSLATE_SLAB, COBBLED_DEEPSLATE_STAIRS, COBBLED_DEEPSLATE_WALL, COBBLESTONE, COBBLESTONE_SLAB, COBBLESTONE_STAIRS, COBBLESTONE_WALL, COBWEB, COCOA, COCOA_BEANS, COD, COD_BUCKET, COD_SPAWN_EGG, COMMAND_BLOCK, COMMAND_BLOCK_MINECART, COMPARATOR, COMPASS, COMPOSTER, CONDUIT, COOKED_BEEF, COOKED_CHICKEN, COOKED_COD, COOKED_MUTTON, COOKED_PORKCHOP, COOKED_RABBIT, COOKED_SALMON, COOKIE, COPPER_BLOCK, COPPER_INGOT, COPPER_ORE, CORNFLOWER, COW_SPAWN_EGG, CRACKED_DEEPSLATE_BRICKS, CRACKED_DEEPSLATE_TILES, CRACKED_NETHER_BRICKS, CRACKED_POLISHED_BLACKSTONE_BRICKS, CRACKED_STONE_BRICKS, CRAFTING_TABLE, CREEPER_BANNER_PATTERN, CREEPER_HEAD, CREEPER_SPAWN_EGG, CREEPER_WALL_HEAD, CRIMSON_BUTTON, CRIMSON_DOOR, CRIMSON_FENCE, CRIMSON_FENCE_GATE, CRIMSON_FUNGUS, CRIMSON_HANGING_SIGN, CRIMSON_HYPHAE, CRIMSON_NYLIUM, CRIMSON_PLANKS, CRIMSON_PRESSURE_PLATE, CRIMSON_ROOTS, CRIMSON_SIGN, CRIMSON_SLAB, CRIMSON_STAIRS, CRIMSON_STEM, CRIMSON_TRAPDOOR, CRIMSON_WALL_HANGING_SIGN, CRIMSON_WALL_SIGN, CROSSBOW, CRYING_OBSIDIAN, CUT_COPPER, CUT_COPPER_SLAB, CUT_COPPER_STAIRS, CUT_RED_SANDSTONE, CUT_RED_SANDSTONE_SLAB, CUT_SANDSTONE, CUT_SANDSTONE_SLAB, CYAN_BANNER, CYAN_BED, CYAN_CANDLE, CYAN_CANDLE_CAKE, CYAN_CARPET, CYAN_CONCRETE, CYAN_CONCRETE_POWDER, CYAN_DYE, CYAN_GLAZED_TERRACOTTA, CYAN_SHULKER_BOX, CYAN_STAINED_GLASS, CYAN_STAINED_GLASS_PANE, CYAN_TERRACOTTA, CYAN_WALL_BANNER, CYAN_WOOL, DAMAGED_ANVIL, DANDELION, DANGER_POTTERY_SHERD, DARK_OAK_BOAT, DARK_OAK_BUTTON, DARK_OAK_CHEST_BOAT, DARK_OAK_DOOR, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_HANGING_SIGN, DARK_OAK_LEAVES, DARK_OAK_LOG, DARK_OAK_PLANKS, DARK_OAK_PRESSURE_PLATE, DARK_OAK_SAPLING, DARK_OAK_SIGN, DARK_OAK_SLAB, DARK_OAK_STAIRS, DARK_OAK_TRAPDOOR, DARK_OAK_WALL_HANGING_SIGN, DARK_OAK_WALL_SIGN, DARK_OAK_WOOD, DARK_PRISMARINE, DARK_PRISMARINE_SLAB, DARK_PRISMARINE_STAIRS, DAYLIGHT_DETECTOR, DEAD_BRAIN_CORAL, DEAD_BRAIN_CORAL_BLOCK, DEAD_BRAIN_CORAL_FAN, DEAD_BRAIN_CORAL_WALL_FAN, DEAD_BUBBLE_CORAL, DEAD_BUBBLE_CORAL_BLOCK, DEAD_BUBBLE_CORAL_FAN, DEAD_BUBBLE_CORAL_WALL_FAN, DEAD_BUSH, DEAD_FIRE_CORAL, DEAD_FIRE_CORAL_BLOCK, DEAD_FIRE_CORAL_FAN, DEAD_FIRE_CORAL_WALL_FAN, DEAD_HORN_CORAL, DEAD_HORN_CORAL_BLOCK, DEAD_HORN_CORAL_FAN, DEAD_HORN_CORAL_WALL_FAN, DEAD_TUBE_CORAL, DEAD_TUBE_CORAL_BLOCK, DEAD_TUBE_CORAL_FAN, DEAD_TUBE_CORAL_WALL_FAN, DEBUG_STICK, DECORATED_POT, DEEPSLATE, DEEPSLATE_BRICKS, DEEPSLATE_BRICK_SLAB, DEEPSLATE_BRICK_STAIRS, DEEPSLATE_BRICK_WALL, DEEPSLATE_COAL_ORE, DEEPSLATE_COPPER_ORE, DEEPSLATE_DIAMOND_ORE, DEEPSLATE_EMERALD_ORE, DEEPSLATE_GOLD_ORE, DEEPSLATE_IRON_ORE, DEEPSLATE_LAPIS_ORE, DEEPSLATE_REDSTONE_ORE, DEEPSLATE_TILES, DEEPSLATE_TILE_SLAB, DEEPSLATE_TILE_STAIRS, DEEPSLATE_TILE_WALL, DETECTOR_RAIL, DIAMOND, DIAMOND_AXE, DIAMOND_BLOCK, DIAMOND_BOOTS, DIAMOND_CHESTPLATE, DIAMOND_HELMET, DIAMOND_HOE, DIAMOND_HORSE_ARMOR, DIAMOND_LEGGINGS, DIAMOND_ORE, DIAMOND_PICKAXE, DIAMOND_SHOVEL, DIAMOND_SWORD, DIORITE, DIORITE_SLAB, DIORITE_STAIRS, DIORITE_WALL, DIRT, DIRT_PATH, DISC_FRAGMENT_5, DISPENSER, DOLPHIN_SPAWN_EGG, DONKEY_SPAWN_EGG, DRAGON_BREATH, DRAGON_EGG, DRAGON_HEAD, DRAGON_WALL_HEAD, DRIED_KELP, DRIED_KELP_BLOCK, DRIPSTONE_BLOCK, DROPPER, DROWNED_SPAWN_EGG, DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, ECHO_SHARD, EGG, ELDER_GUARDIAN_SPAWN_EGG, ELYTRA, EMERALD, EMERALD_BLOCK, EMERALD_ORE, ENCHANTED_BOOK, ENCHANTED_GOLDEN_APPLE, ENCHANTING_TABLE, ENDERMAN_SPAWN_EGG, ENDERMITE_SPAWN_EGG, ENDER_CHEST, ENDER_DRAGON_SPAWN_EGG, ENDER_EYE, ENDER_PEARL, END_CRYSTAL, END_GATEWAY, END_PORTAL, END_PORTAL_FRAME, END_ROD, END_STONE, END_STONE_BRICKS, END_STONE_BRICK_SLAB, END_STONE_BRICK_STAIRS, END_STONE_BRICK_WALL, EVOKER_SPAWN_EGG, EXPERIENCE_BOTTLE, EXPLORER_POTTERY_SHERD, EXPOSED_COPPER, EXPOSED_CUT_COPPER, EXPOSED_CUT_COPPER_SLAB, EXPOSED_CUT_COPPER_STAIRS, EYE_ARMOR_TRIM_SMITHING_TEMPLATE, FARMLAND, FEATHER, FERMENTED_SPIDER_EYE, FERN, FILLED_MAP, FIRE, FIREWORK_ROCKET, FIREWORK_STAR, FIRE_CHARGE, FIRE_CORAL, FIRE_CORAL_BLOCK, FIRE_CORAL_FAN, FIRE_CORAL_WALL_FAN, FISHING_ROD, FLETCHING_TABLE, FLINT, FLINT_AND_STEEL, FLOWERING_AZALEA, FLOWERING_AZALEA_LEAVES, FLOWER_BANNER_PATTERN, FLOWER_POT, FOX_SPAWN_EGG, FRIEND_POTTERY_SHERD, FROGSPAWN, FROG_SPAWN_EGG, FROSTED_ICE, FURNACE, FURNACE_MINECART, GHAST_SPAWN_EGG, GHAST_TEAR, GILDED_BLACKSTONE, GLASS, GLASS_BOTTLE, GLASS_PANE, GLISTERING_MELON_SLICE, GLOBE_BANNER_PATTERN, GLOWSTONE, GLOWSTONE_DUST, GLOW_BERRIES, GLOW_INK_SAC, GLOW_ITEM_FRAME, GLOW_LICHEN, GLOW_SQUID_SPAWN_EGG, GOAT_HORN, GOAT_SPAWN_EGG, GOLDEN_APPLE, GOLDEN_AXE, GOLDEN_BOOTS, GOLDEN_CARROT, GOLDEN_CHESTPLATE, GOLDEN_HELMET, GOLDEN_HOE, GOLDEN_HORSE_ARMOR, GOLDEN_LEGGINGS, GOLDEN_PICKAXE, GOLDEN_SHOVEL, GOLDEN_SWORD, GOLD_BLOCK, GOLD_INGOT, GOLD_NUGGET, GOLD_ORE, GRANITE, GRANITE_SLAB, GRANITE_STAIRS, GRANITE_WALL, GRASS, GRASS_BLOCK, GRAVEL, GRAY_BANNER, GRAY_BED, GRAY_CANDLE, GRAY_CANDLE_CAKE, GRAY_CARPET, GRAY_CONCRETE, GRAY_CONCRETE_POWDER, GRAY_DYE, GRAY_GLAZED_TERRACOTTA, GRAY_SHULKER_BOX, GRAY_STAINED_GLASS, GRAY_STAINED_GLASS_PANE, GRAY_TERRACOTTA, GRAY_WALL_BANNER, GRAY_WOOL, GREEN_BANNER, GREEN_BED, GREEN_CANDLE, GREEN_CANDLE_CAKE, GREEN_CARPET, GREEN_CONCRETE, GREEN_CONCRETE_POWDER, GREEN_DYE, GREEN_GLAZED_TERRACOTTA, GREEN_SHULKER_BOX, GREEN_STAINED_GLASS, GREEN_STAINED_GLASS_PANE, GREEN_TERRACOTTA, GREEN_WALL_BANNER, GREEN_WOOL, GRINDSTONE, GUARDIAN_SPAWN_EGG, GUNPOWDER, HANGING_ROOTS, HAY_BLOCK, HEARTBREAK_POTTERY_SHERD, HEART_OF_THE_SEA, HEART_POTTERY_SHERD, HEAVY_WEIGHTED_PRESSURE_PLATE, HOGLIN_SPAWN_EGG, HONEYCOMB, HONEYCOMB_BLOCK, HONEY_BLOCK, HONEY_BOTTLE, HOPPER, HOPPER_MINECART, HORN_CORAL, HORN_CORAL_BLOCK, HORN_CORAL_FAN, HORN_CORAL_WALL_FAN, HORSE_SPAWN_EGG, HOST_ARMOR_TRIM_SMITHING_TEMPLATE, HOWL_POTTERY_SHERD, HUSK_SPAWN_EGG, ICE, INFESTED_CHISELED_STONE_BRICKS, INFESTED_COBBLESTONE, INFESTED_CRACKED_STONE_BRICKS, INFESTED_DEEPSLATE, INFESTED_MOSSY_STONE_BRICKS, INFESTED_STONE, INFESTED_STONE_BRICKS, INK_SAC, IRON_AXE, IRON_BARS, IRON_BLOCK, IRON_BOOTS, IRON_CHESTPLATE, IRON_DOOR, IRON_GOLEM_SPAWN_EGG, IRON_HELMET, IRON_HOE, IRON_HORSE_ARMOR, IRON_INGOT, IRON_LEGGINGS, IRON_NUGGET, IRON_ORE, IRON_PICKAXE, IRON_SHOVEL, IRON_SWORD, IRON_TRAPDOOR, ITEM_FRAME, JACK_O_LANTERN, JIGSAW, JUKEBOX, JUNGLE_BOAT, JUNGLE_BUTTON, JUNGLE_CHEST_BOAT, JUNGLE_DOOR, JUNGLE_FENCE, JUNGLE_FENCE_GATE, JUNGLE_HANGING_SIGN, JUNGLE_LEAVES, JUNGLE_LOG, JUNGLE_PLANKS, JUNGLE_PRESSURE_PLATE, JUNGLE_SAPLING, JUNGLE_SIGN, JUNGLE_SLAB, JUNGLE_STAIRS, JUNGLE_TRAPDOOR, JUNGLE_WALL_HANGING_SIGN, JUNGLE_WALL_SIGN, JUNGLE_WOOD, KELP, KELP_PLANT, KNOWLEDGE_BOOK, LADDER, LANTERN, LAPIS_BLOCK, LAPIS_LAZULI, LAPIS_ORE, LARGE_AMETHYST_BUD, LARGE_FERN, LAVA, LAVA_BUCKET, LAVA_CAULDRON, LEAD, LEATHER, LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_HORSE_ARMOR, LEATHER_LEGGINGS, LECTERN, LEVER, LIGHT, LIGHTNING_ROD, LIGHT_BLUE_BANNER, LIGHT_BLUE_BED, LIGHT_BLUE_CANDLE, LIGHT_BLUE_CANDLE_CAKE, LIGHT_BLUE_CARPET, LIGHT_BLUE_CONCRETE, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_BLUE_DYE, LIGHT_BLUE_GLAZED_TERRACOTTA, LIGHT_BLUE_SHULKER_BOX, LIGHT_BLUE_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS_PANE, LIGHT_BLUE_TERRACOTTA, LIGHT_BLUE_WALL_BANNER, LIGHT_BLUE_WOOL, LIGHT_GRAY_BANNER, LIGHT_GRAY_BED, LIGHT_GRAY_CANDLE, LIGHT_GRAY_CANDLE_CAKE, LIGHT_GRAY_CARPET, LIGHT_GRAY_CONCRETE, LIGHT_GRAY_CONCRETE_POWDER, LIGHT_GRAY_DYE, LIGHT_GRAY_GLAZED_TERRACOTTA, LIGHT_GRAY_SHULKER_BOX, LIGHT_GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS_PANE, LIGHT_GRAY_TERRACOTTA, LIGHT_GRAY_WALL_BANNER, LIGHT_GRAY_WOOL, LIGHT_WEIGHTED_PRESSURE_PLATE, LILAC, LILY_OF_THE_VALLEY, LILY_PAD, LIME_BANNER, LIME_BED, LIME_CANDLE, LIME_CANDLE_CAKE, LIME_CARPET, LIME_CONCRETE, LIME_CONCRETE_POWDER, LIME_DYE, LIME_GLAZED_TERRACOTTA, LIME_SHULKER_BOX, LIME_STAINED_GLASS, LIME_STAINED_GLASS_PANE, LIME_TERRACOTTA, LIME_WALL_BANNER, LIME_WOOL, LINGERING_POTION, LLAMA_SPAWN_EGG, LODESTONE, LOOM, MAGENTA_BANNER, MAGENTA_BED, MAGENTA_CANDLE, MAGENTA_CANDLE_CAKE, MAGENTA_CARPET, MAGENTA_CONCRETE, MAGENTA_CONCRETE_POWDER, MAGENTA_DYE, MAGENTA_GLAZED_TERRACOTTA, MAGENTA_SHULKER_BOX, MAGENTA_STAINED_GLASS, MAGENTA_STAINED_GLASS_PANE, MAGENTA_TERRACOTTA, MAGENTA_WALL_BANNER, MAGENTA_WOOL, MAGMA_BLOCK, MAGMA_CREAM, MAGMA_CUBE_SPAWN_EGG, MANGROVE_BOAT, MANGROVE_BUTTON, MANGROVE_CHEST_BOAT, MANGROVE_DOOR, MANGROVE_FENCE, MANGROVE_FENCE_GATE, MANGROVE_HANGING_SIGN, MANGROVE_LEAVES, MANGROVE_LOG, MANGROVE_PLANKS, MANGROVE_PRESSURE_PLATE, MANGROVE_PROPAGULE, MANGROVE_ROOTS, MANGROVE_SIGN, MANGROVE_SLAB, MANGROVE_STAIRS, MANGROVE_TRAPDOOR, MANGROVE_WALL_HANGING_SIGN, MANGROVE_WALL_SIGN, MANGROVE_WOOD, MAP, MEDIUM_AMETHYST_BUD, MELON, MELON_SEEDS, MELON_SLICE, MELON_STEM, MILK_BUCKET, MINECART, MINER_POTTERY_SHERD, MOJANG_BANNER_PATTERN, MOOSHROOM_SPAWN_EGG, MOSSY_COBBLESTONE, MOSSY_COBBLESTONE_SLAB, MOSSY_COBBLESTONE_STAIRS, MOSSY_COBBLESTONE_WALL, MOSSY_STONE_BRICKS, MOSSY_STONE_BRICK_SLAB, MOSSY_STONE_BRICK_STAIRS, MOSSY_STONE_BRICK_WALL, MOSS_BLOCK, MOSS_CARPET, MOURNER_POTTERY_SHERD, MOVING_PISTON, MUD, MUDDY_MANGROVE_ROOTS, MUD_BRICKS, MUD_BRICK_SLAB, MUD_BRICK_STAIRS, MUD_BRICK_WALL, MULE_SPAWN_EGG, MUSHROOM_STEM, MUSHROOM_STEW, MUSIC_DISC_11, MUSIC_DISC_13, MUSIC_DISC_5, MUSIC_DISC_BLOCKS, MUSIC_DISC_CAT, MUSIC_DISC_CHIRP, MUSIC_DISC_FAR, MUSIC_DISC_MALL, MUSIC_DISC_MELLOHI, MUSIC_DISC_OTHERSIDE, MUSIC_DISC_PIGSTEP, MUSIC_DISC_RELIC, MUSIC_DISC_STAL, MUSIC_DISC_STRAD, MUSIC_DISC_WAIT, MUSIC_DISC_WARD, MUTTON, MYCELIUM, NAME_TAG, NAUTILUS_SHELL, NETHERITE_AXE, NETHERITE_BLOCK, NETHERITE_BOOTS, NETHERITE_CHESTPLATE, NETHERITE_HELMET, NETHERITE_HOE, NETHERITE_INGOT, NETHERITE_LEGGINGS, NETHERITE_PICKAXE, NETHERITE_SCRAP, NETHERITE_SHOVEL, NETHERITE_SWORD, NETHERITE_UPGRADE_SMITHING_TEMPLATE, NETHERRACK, NETHER_BRICK, NETHER_BRICKS, NETHER_BRICK_FENCE, NETHER_BRICK_SLAB, NETHER_BRICK_STAIRS, NETHER_BRICK_WALL, NETHER_GOLD_ORE, NETHER_PORTAL, NETHER_QUARTZ_ORE, NETHER_SPROUTS, NETHER_STAR, NETHER_WART, NETHER_WART_BLOCK, NOTE_BLOCK, OAK_BOAT, OAK_BUTTON, OAK_CHEST_BOAT, OAK_DOOR, OAK_FENCE, OAK_FENCE_GATE, OAK_HANGING_SIGN, OAK_LEAVES, OAK_LOG, OAK_PLANKS, OAK_PRESSURE_PLATE, OAK_SAPLING, OAK_SIGN, OAK_SLAB, OAK_STAIRS, OAK_TRAPDOOR, OAK_WALL_HANGING_SIGN, OAK_WALL_SIGN, OAK_WOOD, OBSERVER, OBSIDIAN, OCELOT_SPAWN_EGG, OCHRE_FROGLIGHT, ORANGE_BANNER, ORANGE_BED, ORANGE_CANDLE, ORANGE_CANDLE_CAKE, ORANGE_CARPET, ORANGE_CONCRETE, ORANGE_CONCRETE_POWDER, ORANGE_DYE, ORANGE_GLAZED_TERRACOTTA, ORANGE_SHULKER_BOX, ORANGE_STAINED_GLASS, ORANGE_STAINED_GLASS_PANE, ORANGE_TERRACOTTA, ORANGE_TULIP, ORANGE_WALL_BANNER, ORANGE_WOOL, OXEYE_DAISY, OXIDIZED_COPPER, OXIDIZED_CUT_COPPER, OXIDIZED_CUT_COPPER_SLAB, OXIDIZED_CUT_COPPER_STAIRS, PACKED_ICE, PACKED_MUD, PAINTING, PANDA_SPAWN_EGG, PAPER, PARROT_SPAWN_EGG, PEARLESCENT_FROGLIGHT, PEONY, PETRIFIED_OAK_SLAB, PHANTOM_MEMBRANE, PHANTOM_SPAWN_EGG, PIGLIN_BANNER_PATTERN, PIGLIN_BRUTE_SPAWN_EGG, PIGLIN_HEAD, PIGLIN_SPAWN_EGG, PIGLIN_WALL_HEAD, PIG_SPAWN_EGG, PILLAGER_SPAWN_EGG, PINK_BANNER, PINK_BED, PINK_CANDLE, PINK_CANDLE_CAKE, PINK_CARPET, PINK_CONCRETE, PINK_CONCRETE_POWDER, PINK_DYE, PINK_GLAZED_TERRACOTTA, PINK_PETALS, PINK_SHULKER_BOX, PINK_STAINED_GLASS, PINK_STAINED_GLASS_PANE, PINK_TERRACOTTA, PINK_TULIP, PINK_WALL_BANNER, PINK_WOOL, PISTON, PISTON_HEAD, PITCHER_CROP, PITCHER_PLANT, PITCHER_POD, PLAYER_HEAD, PLAYER_WALL_HEAD, PLENTY_POTTERY_SHERD, PODZOL, POINTED_DRIPSTONE, POISONOUS_POTATO, POLAR_BEAR_SPAWN_EGG, POLISHED_ANDESITE, POLISHED_ANDESITE_SLAB, POLISHED_ANDESITE_STAIRS, POLISHED_BASALT, POLISHED_BLACKSTONE, POLISHED_BLACKSTONE_BRICKS, POLISHED_BLACKSTONE_BRICK_SLAB, POLISHED_BLACKSTONE_BRICK_STAIRS, POLISHED_BLACKSTONE_BRICK_WALL, POLISHED_BLACKSTONE_BUTTON, POLISHED_BLACKSTONE_PRESSURE_PLATE, POLISHED_BLACKSTONE_SLAB, POLISHED_BLACKSTONE_STAIRS, POLISHED_BLACKSTONE_WALL, POLISHED_DEEPSLATE, POLISHED_DEEPSLATE_SLAB, POLISHED_DEEPSLATE_STAIRS, POLISHED_DEEPSLATE_WALL, POLISHED_DIORITE, POLISHED_DIORITE_SLAB, POLISHED_DIORITE_STAIRS, POLISHED_GRANITE, POLISHED_GRANITE_SLAB, POLISHED_GRANITE_STAIRS, POPPED_CHORUS_FRUIT, POPPY, PORKCHOP, POTATO, POTATOES, POTION, POTTED_ACACIA_SAPLING, POTTED_ALLIUM, POTTED_AZALEA_BUSH, POTTED_AZURE_BLUET, POTTED_BAMBOO, POTTED_BIRCH_SAPLING, POTTED_BLUE_ORCHID, POTTED_BROWN_MUSHROOM, POTTED_CACTUS, POTTED_CHERRY_SAPLING, POTTED_CORNFLOWER, POTTED_CRIMSON_FUNGUS, POTTED_CRIMSON_ROOTS, POTTED_DANDELION, POTTED_DARK_OAK_SAPLING, POTTED_DEAD_BUSH, POTTED_FERN, POTTED_FLOWERING_AZALEA_BUSH, POTTED_JUNGLE_SAPLING, POTTED_LILY_OF_THE_VALLEY, POTTED_MANGROVE_PROPAGULE, POTTED_OAK_SAPLING, POTTED_ORANGE_TULIP, POTTED_OXEYE_DAISY, POTTED_PINK_TULIP, POTTED_POPPY, POTTED_RED_MUSHROOM, POTTED_RED_TULIP, POTTED_SPRUCE_SAPLING, POTTED_TORCHFLOWER, POTTED_WARPED_FUNGUS, POTTED_WARPED_ROOTS, POTTED_WHITE_TULIP, POTTED_WITHER_ROSE, POTTERY_SHARD_ARCHER, POTTERY_SHARD_ARMS_UP, POTTERY_SHARD_PRIZE, POTTERY_SHARD_SKULL, POWDER_SNOW, POWDER_SNOW_BUCKET, POWDER_SNOW_CAULDRON, POWERED_RAIL, PRISMARINE, PRISMARINE_BRICKS, PRISMARINE_BRICK_SLAB, PRISMARINE_BRICK_STAIRS, PRISMARINE_CRYSTALS, PRISMARINE_SHARD, PRISMARINE_SLAB, PRISMARINE_STAIRS, PRISMARINE_WALL, PRIZE_POTTERY_SHERD, PUFFERFISH, PUFFERFISH_BUCKET, PUFFERFISH_SPAWN_EGG, PUMPKIN, PUMPKIN_PIE, PUMPKIN_SEEDS, PUMPKIN_STEM, PURPLE_BANNER, PURPLE_BED, PURPLE_CANDLE, PURPLE_CANDLE_CAKE, PURPLE_CARPET, PURPLE_CONCRETE, PURPLE_CONCRETE_POWDER, PURPLE_DYE, PURPLE_GLAZED_TERRACOTTA, PURPLE_SHULKER_BOX, PURPLE_STAINED_GLASS, PURPLE_STAINED_GLASS_PANE, PURPLE_TERRACOTTA, PURPLE_WALL_BANNER, PURPLE_WOOL, PURPUR_BLOCK, PURPUR_PILLAR, PURPUR_SLAB, PURPUR_STAIRS, QUARTZ, QUARTZ_BLOCK, QUARTZ_BRICKS, QUARTZ_PILLAR, QUARTZ_SLAB, QUARTZ_STAIRS, RABBIT, RABBIT_FOOT, RABBIT_HIDE, RABBIT_SPAWN_EGG, RABBIT_STEW, RAIL, RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, RAVAGER_SPAWN_EGG, RAW_COPPER, RAW_COPPER_BLOCK, RAW_GOLD, RAW_GOLD_BLOCK, RAW_IRON, RAW_IRON_BLOCK, RECOVERY_COMPASS, REDSTONE, REDSTONE_BLOCK, REDSTONE_LAMP, REDSTONE_ORE, REDSTONE_TORCH, REDSTONE_WALL_TORCH, REDSTONE_WIRE, RED_BANNER, RED_BED, RED_CANDLE, RED_CANDLE_CAKE, RED_CARPET, RED_CONCRETE, RED_CONCRETE_POWDER, RED_DYE, RED_GLAZED_TERRACOTTA, RED_MUSHROOM, RED_MUSHROOM_BLOCK, RED_NETHER_BRICKS, RED_NETHER_BRICK_SLAB, RED_NETHER_BRICK_STAIRS, RED_NETHER_BRICK_WALL, RED_SAND, RED_SANDSTONE, RED_SANDSTONE_SLAB, RED_SANDSTONE_STAIRS, RED_SANDSTONE_WALL, RED_SHULKER_BOX, RED_STAINED_GLASS, RED_STAINED_GLASS_PANE, RED_TERRACOTTA, RED_TULIP, RED_WALL_BANNER, RED_WOOL, REINFORCED_DEEPSLATE, REPEATER, REPEATING_COMMAND_BLOCK, RESPAWN_ANCHOR, RIB_ARMOR_TRIM_SMITHING_TEMPLATE, ROOTED_DIRT, ROSE_BUSH, ROTTEN_FLESH, SADDLE, SALMON, SALMON_BUCKET, SALMON_SPAWN_EGG, SAND, SANDSTONE, SANDSTONE_SLAB, SANDSTONE_STAIRS, SANDSTONE_WALL, SCAFFOLDING, SCULK, SCULK_CATALYST, SCULK_SENSOR, SCULK_SHRIEKER, SCULK_VEIN, SCUTE, SEAGRASS, SEA_LANTERN, SEA_PICKLE, SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, SHEAF_POTTERY_SHERD, SHEARS, SHEEP_SPAWN_EGG, SHELTER_POTTERY_SHERD, SHIELD, SHROOMLIGHT, SHULKER_BOX, SHULKER_SHELL, SHULKER_SPAWN_EGG, SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, SILVERFISH_SPAWN_EGG, SKELETON_HORSE_SPAWN_EGG, SKELETON_SKULL, SKELETON_SPAWN_EGG, SKELETON_WALL_SKULL, SKULL_BANNER_PATTERN, SKULL_POTTERY_SHERD, SLIME_BALL, SLIME_BLOCK, SLIME_SPAWN_EGG, SMALL_AMETHYST_BUD, SMALL_DRIPLEAF, SMITHING_TABLE, SMOKER, SMOOTH_BASALT, SMOOTH_QUARTZ, SMOOTH_QUARTZ_SLAB, SMOOTH_QUARTZ_STAIRS, SMOOTH_RED_SANDSTONE, SMOOTH_RED_SANDSTONE_SLAB, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE, SMOOTH_SANDSTONE_SLAB, SMOOTH_SANDSTONE_STAIRS, SMOOTH_STONE, SMOOTH_STONE_SLAB, SNIFFER_EGG, SNIFFER_SPAWN_EGG, SNORT_POTTERY_SHERD, SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, SNOW, SNOWBALL, SNOW_BLOCK, SNOW_GOLEM_SPAWN_EGG, SOUL_CAMPFIRE, SOUL_FIRE, SOUL_LANTERN, SOUL_SAND, SOUL_SOIL, SOUL_TORCH, SOUL_WALL_TORCH, SPAWNER, SPECTRAL_ARROW, SPIDER_EYE, SPIDER_SPAWN_EGG, SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, SPLASH_POTION, SPONGE, SPORE_BLOSSOM, SPRUCE_BOAT, SPRUCE_BUTTON, SPRUCE_CHEST_BOAT, SPRUCE_DOOR, SPRUCE_FENCE, SPRUCE_FENCE_GATE, SPRUCE_HANGING_SIGN, SPRUCE_LEAVES, SPRUCE_LOG, SPRUCE_PLANKS, SPRUCE_PRESSURE_PLATE, SPRUCE_SAPLING, SPRUCE_SIGN, SPRUCE_SLAB, SPRUCE_STAIRS, SPRUCE_TRAPDOOR, SPRUCE_WALL_HANGING_SIGN, SPRUCE_WALL_SIGN, SPRUCE_WOOD, SPYGLASS, SQUID_SPAWN_EGG, STICK, STICKY_PISTON, STONE, STONECUTTER, STONE_AXE, STONE_BRICKS, STONE_BRICK_SLAB, STONE_BRICK_STAIRS, STONE_BRICK_WALL, STONE_BUTTON, STONE_HOE, STONE_PICKAXE, STONE_PRESSURE_PLATE, STONE_SHOVEL, STONE_SLAB, STONE_STAIRS, STONE_SWORD, STRAY_SPAWN_EGG, STRIDER_SPAWN_EGG, STRING, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BAMBOO_BLOCK, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_CHERRY_LOG, STRIPPED_CHERRY_WOOD, STRIPPED_CRIMSON_HYPHAE, STRIPPED_CRIMSON_STEM, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_MANGROVE_LOG, STRIPPED_MANGROVE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, STRIPPED_WARPED_HYPHAE, STRIPPED_WARPED_STEM, STRUCTURE_BLOCK, STRUCTURE_VOID, SUGAR, SUGAR_CANE, SUNFLOWER, SUSPICIOUS_GRAVEL, SUSPICIOUS_SAND, SUSPICIOUS_STEW, SWEET_BERRIES, SWEET_BERRY_BUSH, TADPOLE_BUCKET, TADPOLE_SPAWN_EGG, TALL_GRASS, TALL_SEAGRASS, TARGET, TERRACOTTA, TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, TINTED_GLASS, TIPPED_ARROW, TNT, TNT_MINECART, TORCH, TORCHFLOWER, TORCHFLOWER_CROP, TORCHFLOWER_SEEDS, TOTEM_OF_UNDYING, TRADER_LLAMA_SPAWN_EGG, TRAPPED_CHEST, TRIDENT, TRIPWIRE, TRIPWIRE_HOOK, TROPICAL_FISH, TROPICAL_FISH_BUCKET, TROPICAL_FISH_SPAWN_EGG, TUBE_CORAL, TUBE_CORAL_BLOCK, TUBE_CORAL_FAN, TUBE_CORAL_WALL_FAN, TUFF, TURTLE_EGG, TURTLE_HELMET, TURTLE_SPAWN_EGG, TWISTING_VINES, TWISTING_VINES_PLANT, VERDANT_FROGLIGHT, VEX_ARMOR_TRIM_SMITHING_TEMPLATE, VEX_SPAWN_EGG, VILLAGER_SPAWN_EGG, VINDICATOR_SPAWN_EGG, VINE, VOID_AIR, WALL_TORCH, WANDERING_TRADER_SPAWN_EGG, WARDEN_SPAWN_EGG, WARD_ARMOR_TRIM_SMITHING_TEMPLATE, WARPED_BUTTON, WARPED_DOOR, WARPED_FENCE, WARPED_FENCE_GATE, WARPED_FUNGUS, WARPED_FUNGUS_ON_A_STICK, WARPED_HANGING_SIGN, WARPED_HYPHAE, WARPED_NYLIUM, WARPED_PLANKS, WARPED_PRESSURE_PLATE, WARPED_ROOTS, WARPED_SIGN, WARPED_SLAB, WARPED_STAIRS, WARPED_STEM, WARPED_TRAPDOOR, WARPED_WALL_HANGING_SIGN, WARPED_WALL_SIGN, WARPED_WART_BLOCK, WATER, WATER_BUCKET, WATER_CAULDRON, WAXED_COPPER_BLOCK, WAXED_CUT_COPPER, WAXED_CUT_COPPER_SLAB, WAXED_CUT_COPPER_STAIRS, WAXED_EXPOSED_COPPER, WAXED_EXPOSED_CUT_COPPER, WAXED_EXPOSED_CUT_COPPER_SLAB, WAXED_EXPOSED_CUT_COPPER_STAIRS, WAXED_OXIDIZED_COPPER, WAXED_OXIDIZED_CUT_COPPER, WAXED_OXIDIZED_CUT_COPPER_SLAB, WAXED_OXIDIZED_CUT_COPPER_STAIRS, WAXED_WEATHERED_COPPER, WAXED_WEATHERED_CUT_COPPER, WAXED_WEATHERED_CUT_COPPER_SLAB, WAXED_WEATHERED_CUT_COPPER_STAIRS, WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, WEATHERED_COPPER, WEATHERED_CUT_COPPER, WEATHERED_CUT_COPPER_SLAB, WEATHERED_CUT_COPPER_STAIRS, WEEPING_VINES, WEEPING_VINES_PLANT, WET_SPONGE, WHEAT, WHEAT_SEEDS, WHITE_BANNER, WHITE_BED, WHITE_CANDLE, WHITE_CANDLE_CAKE, WHITE_CARPET, WHITE_CONCRETE, WHITE_CONCRETE_POWDER, WHITE_DYE, WHITE_GLAZED_TERRACOTTA, WHITE_SHULKER_BOX, WHITE_STAINED_GLASS, WHITE_STAINED_GLASS_PANE, WHITE_TERRACOTTA, WHITE_TULIP, WHITE_WALL_BANNER, WHITE_WOOL, WILD_ARMOR_TRIM_SMITHING_TEMPLATE, WITCH_SPAWN_EGG, WITHER_ROSE, WITHER_SKELETON_SKULL, WITHER_SKELETON_SPAWN_EGG, WITHER_SKELETON_WALL_SKULL, WITHER_SPAWN_EGG, WOLF_SPAWN_EGG, WOODEN_AXE, WOODEN_HOE, WOODEN_PICKAXE, WOODEN_SHOVEL, WOODEN_SWORD, WRITABLE_BOOK, WRITTEN_BOOK, YELLOW_BANNER, YELLOW_BED, YELLOW_CANDLE, YELLOW_CANDLE_CAKE, YELLOW_CARPET, YELLOW_CONCRETE, YELLOW_CONCRETE_POWDER, YELLOW_DYE, YELLOW_GLAZED_TERRACOTTA, YELLOW_SHULKER_BOX, YELLOW_STAINED_GLASS, YELLOW_STAINED_GLASS_PANE, YELLOW_TERRACOTTA, YELLOW_WALL_BANNER, YELLOW_WOOL, ZOGLIN_SPAWN_EGG, ZOMBIE_HEAD, ZOMBIE_HORSE_SPAWN_EGG, ZOMBIE_SPAWN_EGG, ZOMBIE_VILLAGER_SPAWN_EGG, ZOMBIE_WALL_HEAD, ZOMBIFIED_PIGLIN_SPAWN_EGG};
   }

   static {
      for (XMaterial material : VALUES) {
         NAMES.put(material.name(), material);
      }

      if (XMaterial.Data.ISFLAT) {
         DUPLICATED = null;
      } else {
         DUPLICATED = new HashSet(4);
         DUPLICATED.add(GRASS.name());
         DUPLICATED.add(MELON.name());
         DUPLICATED.add(BRICK.name());
         DUPLICATED.add(NETHER_BRICK.name());
      }

   }

   private static final class Data {
      private static final int VERSION;
      private static final boolean ISFLAT;

      static {
         String version = Bukkit.getVersion();
         Matcher matcher = Pattern.compile("MC: \\d\\.(\\d+)").matcher(version);
         if (matcher.find()) {
            VERSION = Integer.parseInt(matcher.group(1));
            ISFLAT = XMaterial.supports(13);
         } else {
            throw new IllegalArgumentException("Failed to parse server version from: " + version);
         }
      }
   }
}
