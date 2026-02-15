package dev.artixdev.libs.com.cryptomorin.xseries;

import com.google.common.base.Enums;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public enum XEnchantment {
   ARROW_DAMAGE(new String[]{"POWER", "ARROW_DAMAGE", "ARROW_POWER", "AD"}),
   ARROW_FIRE(new String[]{"FLAME", "FLAME_ARROW", "FIRE_ARROW", "AF"}),
   ARROW_INFINITE(new String[]{"INFINITY", "INF_ARROWS", "INFINITE_ARROWS", "INFINITE", "UNLIMITED", "UNLIMITED_ARROWS", "AI"}),
   ARROW_KNOCKBACK(new String[]{"PUNCH", "ARROW_KNOCKBACK", "ARROWKB", "ARROW_PUNCH", "AK"}),
   BINDING_CURSE(true, new String[]{"BINDING_CURSE", "BIND_CURSE", "BINDING", "BIND"}),
   CHANNELING(true, new String[]{"CHANNELLING", "CHANELLING", "CHANELING", "CHANNEL"}),
   DAMAGE_ALL(new String[]{"SHARPNESS", "ALL_DAMAGE", "ALL_DMG", "SHARP", "DAL"}),
   DAMAGE_ARTHROPODS(new String[]{"BANE_OF_ARTHROPODS", "ARDMG", "BANE_OF_ARTHROPOD", "ARTHROPOD", "DAR"}),
   DAMAGE_UNDEAD(new String[]{"SMITE", "UNDEAD_DAMAGE", "DU"}),
   DEPTH_STRIDER(true, new String[]{"DEPTH", "STRIDER"}),
   DIG_SPEED(new String[]{"EFFICIENCY", "MINE_SPEED", "CUT_SPEED", "DS", "EFF"}),
   DURABILITY(new String[]{"UNBREAKING", "DURA"}),
   FIRE_ASPECT(true, new String[]{"FIRE", "MELEE_FIRE", "MELEE_FLAME", "FA"}),
   FROST_WALKER(true, new String[]{"FROST", "WALKER"}),
   IMPALING(true, new String[]{"IMPALE", "OCEAN_DAMAGE", "OCEAN_DMG"}),
   KNOCKBACK(true, new String[]{"K_BACK", "KB"}),
   LOOT_BONUS_BLOCKS(new String[]{"FORTUNE", "BLOCKS_LOOT_BONUS", "FORT", "LBB"}),
   LOOT_BONUS_MOBS(new String[]{"LOOTING", "MOB_LOOT", "MOBS_LOOT_BONUS", "LBM"}),
   LOYALTY(true, new String[]{"LOYAL", "RETURN"}),
   LUCK(new String[]{"LUCK_OF_THE_SEA", "LUCK_OF_SEA", "LUCK_OF_SEAS", "ROD_LUCK"}),
   LURE(true, new String[]{"ROD_LURE"}),
   MENDING(true, new String[0]),
   MULTISHOT(true, new String[]{"TRIPLE_SHOT"}),
   OXYGEN(new String[]{"RESPIRATION", "BREATH", "BREATHING", "O2", "O"}),
   PIERCING(true, new String[0]),
   PROTECTION_ENVIRONMENTAL(new String[]{"PROTECTION", "PROTECT", "PROT"}),
   PROTECTION_EXPLOSIONS(new String[]{"BLAST_PROTECTION", "BLAST_PROTECT", "EXPLOSIONS_PROTECTION", "EXPLOSION_PROTECTION", "BLAST_PROTECTION", "PE"}),
   PROTECTION_FALL(new String[]{"FEATHER_FALLING", "FALL_PROT", "FEATHER_FALL", "FALL_PROTECTION", "FEATHER_FALLING", "PFA"}),
   PROTECTION_FIRE(new String[]{"FIRE_PROTECTION", "FIRE_PROT", "FIRE_PROTECT", "FIRE_PROTECTION", "FLAME_PROTECTION", "FLAME_PROTECT", "FLAME_PROT", "PF"}),
   PROTECTION_PROJECTILE(new String[]{"PROJECTILE_PROTECTION", "PROJECTILE_PROTECTION", "PROJ_PROT", "PP"}),
   QUICK_CHARGE(true, new String[]{"QUICKCHARGE", "QUICK_DRAW", "FAST_CHARGE", "FAST_DRAW"}),
   RIPTIDE(true, new String[]{"RIP", "TIDE", "LAUNCH"}),
   SILK_TOUCH(true, new String[]{"SOFT_TOUCH", "ST"}),
   SOUL_SPEED(true, new String[]{"SPEED_SOUL", "SOUL_RUNNER"}),
   SWEEPING_EDGE(new String[]{"SWEEPING", "SWEEPING_EDGE", "SWEEP_EDGE"}),
   SWIFT_SNEAK(true, new String[]{"SNEAK_SWIFT"}),
   THORNS(true, new String[]{"HIGHCRIT", "THORN", "HIGHERCRIT", "T"}),
   VANISHING_CURSE(true, new String[]{"VANISHING_CURSE", "VANISH_CURSE", "VANISHING", "VANISH"}),
   WATER_WORKER(new String[]{"AQUA_AFFINITY", "WATER_WORKER", "AQUA_AFFINITY", "WATER_MINE", "WW"});

   public static final XEnchantment[] VALUES = values();
   public static final Set<EntityType> EFFECTIVE_SMITE_ENTITIES;
   public static final Set<EntityType> EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES;
   @Nullable
   private final Enchantment enchantment;

   private XEnchantment(@Nonnull String... names) {
      this(false, names);
   }

   private XEnchantment(boolean self, @Nonnull String... aliases) {
      XEnchantment.Data.NAMES.put(this.name(), this);
      for (String legacy : aliases) {
         XEnchantment.Data.NAMES.put(legacy, this);
      }

      Enchantment enchantment;
      if (XEnchantment.Data.ISFLAT) {
         String vanilla = self ? this.name() : aliases[0];
         Object key = createMinecraftKey(vanilla.toLowerCase(Locale.ENGLISH));
         enchantment = key != null ? getEnchantmentByKey(key) : null;
      } else {
         enchantment = Enchantment.getByName(this.name());
      }

      this.enchantment = enchantment;
   }

   public static boolean isSmiteEffectiveAgainst(@Nullable EntityType type) {
      return type != null && EFFECTIVE_SMITE_ENTITIES.contains(type);
   }

   public static boolean isArthropodsEffectiveAgainst(@Nullable EntityType type) {
      return type != null && EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES.contains(type);
   }

   @Nonnull
   private static String format(@Nonnull String name) {
      int len = name.length();
      char[] chs = new char[len];
      int count = 0;
      boolean appendUnderline = false;

      for(int i = 0; i < len; ++i) {
         char ch = name.charAt(i);
         if (!appendUnderline && count != 0 && (ch == '-' || ch == ' ' || ch == '_') && chs[count] != '_') {
            appendUnderline = true;
         } else if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
            if (appendUnderline) {
               chs[count++] = '_';
               appendUnderline = false;
            }

            chs[count++] = (char)(ch & 95);
         }
      }

      return new String(chs, 0, count);
   }

   @Nonnull
   public static Optional<XEnchantment> matchXEnchantment(@Nonnull String enchantment) {
      if (enchantment != null && !enchantment.isEmpty()) {
         return Optional.ofNullable((XEnchantment)XEnchantment.Data.NAMES.get(format(enchantment)));
      } else {
         throw new IllegalArgumentException("Enchantment name cannot be null or empty");
      }
   }

   @Nonnull
   public static XEnchantment matchXEnchantment(@Nonnull Enchantment enchantment) {
      Objects.requireNonNull(enchantment, "Cannot parse XEnchantment of a null enchantment");
      return (XEnchantment)Objects.requireNonNull((XEnchantment)XEnchantment.Data.NAMES.get(enchantment.getName()), () -> {
         return "Unsupported enchantment: " + enchantment.getName();
      });
   }

   @Nonnull
   public ItemStack getBook(int level) {
      ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
      meta.addStoredEnchant(this.enchantment, level, true);
      book.setItemMeta(meta);
      return book;
   }

   @Nullable
   public Enchantment getEnchant() {
      return this.enchantment;
   }

   public boolean isSupported() {
      return this.enchantment != null;
   }

   @Nullable
   public XEnchantment or(@Nullable XEnchantment alternateEnchantment) {
      return this.isSupported() ? this : alternateEnchantment;
   }

   @Nonnull
   public String toString() {
      return (String)Arrays.stream(this.name().split("_")).map((t) -> {
         return t.charAt(0) + t.substring(1).toLowerCase();
      }).collect(Collectors.joining(" "));
   }

   // $FF: synthetic method
   private static XEnchantment[] $values() {
      return new XEnchantment[]{ARROW_DAMAGE, ARROW_FIRE, ARROW_INFINITE, ARROW_KNOCKBACK, BINDING_CURSE, CHANNELING, DAMAGE_ALL, DAMAGE_ARTHROPODS, DAMAGE_UNDEAD, DEPTH_STRIDER, DIG_SPEED, DURABILITY, FIRE_ASPECT, FROST_WALKER, IMPALING, KNOCKBACK, LOOT_BONUS_BLOCKS, LOOT_BONUS_MOBS, LOYALTY, LUCK, LURE, MENDING, MULTISHOT, OXYGEN, PIERCING, PROTECTION_ENVIRONMENTAL, PROTECTION_EXPLOSIONS, PROTECTION_FALL, PROTECTION_FIRE, PROTECTION_PROJECTILE, QUICK_CHARGE, RIPTIDE, SILK_TOUCH, SOUL_SPEED, SWEEPING_EDGE, SWIFT_SNEAK, THORNS, VANISHING_CURSE, WATER_WORKER};
   }

   static {
      EntityType bee = (EntityType)Enums.getIfPresent(EntityType.class, "BEE").orNull();
      EntityType phantom = (EntityType)Enums.getIfPresent(EntityType.class, "PHANTOM").orNull();
      EntityType drowned = (EntityType)Enums.getIfPresent(EntityType.class, "DROWNED").orNull();
      EntityType witherSkeleton = (EntityType)Enums.getIfPresent(EntityType.class, "WITHER_SKELETON").orNull();
      EntityType skeletonHorse = (EntityType)Enums.getIfPresent(EntityType.class, "SKELETON_HORSE").orNull();
      EntityType stray = (EntityType)Enums.getIfPresent(EntityType.class, "STRAY").orNull();
      EntityType husk = (EntityType)Enums.getIfPresent(EntityType.class, "HUSK").orNull();
      Set<EntityType> arthorposEffective = EnumSet.of(EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SILVERFISH, EntityType.ENDERMITE);
      if (bee != null) {
         arthorposEffective.add(bee);
      }

      EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES = Collections.unmodifiableSet(arthorposEffective);
      Set<EntityType> smiteEffective = EnumSet.of(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER);
      if (phantom != null) {
         smiteEffective.add(phantom);
      }

      if (drowned != null) {
         smiteEffective.add(drowned);
      }

      if (witherSkeleton != null) {
         smiteEffective.add(witherSkeleton);
      }

      if (skeletonHorse != null) {
         smiteEffective.add(skeletonHorse);
      }

      if (stray != null) {
         smiteEffective.add(stray);
      }

      if (husk != null) {
         smiteEffective.add(husk);
      }

      EFFECTIVE_SMITE_ENTITIES = Collections.unmodifiableSet(smiteEffective);
   }

   @Nullable
   private static Object createMinecraftKey(String key) {
      try {
         Class<?> namespacedKeyClass = Class.forName("org.bukkit.NamespacedKey");
         return namespacedKeyClass.getMethod("minecraft", String.class).invoke(null, key);
      } catch (Exception e) {
         return null;
      }
   }

   @Nullable
   private static Enchantment getEnchantmentByKey(Object namespacedKey) {
      if (namespacedKey == null) return null;
      try {
         Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
         return (Enchantment) Enchantment.class.getMethod("getByKey", keyClass).invoke(null, namespacedKey);
      } catch (Exception e) {
         return null;
      }
   }

   private static final class Data {
      private static final boolean ISFLAT;
      private static final Map<String, XEnchantment> NAMES = new HashMap();

      static {
         boolean flat;
         try {
            Class<?> namespacedKeyClass = Class.forName("org.bukkit.NamespacedKey");
            Class<?> enchantmentClass = Class.forName("org.bukkit.enchantments.Enchantment");
            enchantmentClass.getDeclaredMethod("getByKey", namespacedKeyClass);
            flat = true;
         } catch (NoSuchMethodException | ClassNotFoundException ignored) {
            flat = false;
         }

         ISFLAT = flat;
      }
   }
}
