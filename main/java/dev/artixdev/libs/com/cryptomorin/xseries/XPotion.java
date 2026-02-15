package dev.artixdev.libs.com.cryptomorin.xseries;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public enum XPotion {
   ABSORPTION(new String[]{"ABSORB"}),
   BAD_OMEN(new String[]{"OMEN_BAD", "PILLAGER"}),
   BLINDNESS(new String[]{"BLIND"}),
   CONDUIT_POWER(new String[]{"CONDUIT", "POWER_CONDUIT"}),
   CONFUSION(new String[]{"NAUSEA", "SICKNESS", "SICK"}),
   DAMAGE_RESISTANCE(new String[]{"RESISTANCE", "ARMOR", "DMG_RESIST", "DMG_RESISTANCE"}),
   DARKNESS(new String[0]),
   DOLPHINS_GRACE(new String[]{"DOLPHIN", "GRACE"}),
   FAST_DIGGING(new String[]{"HASTE", "SUPER_PICK", "DIGFAST", "DIG_SPEED", "QUICK_MINE", "SHARP"}),
   FIRE_RESISTANCE(new String[]{"FIRE_RESIST", "RESIST_FIRE", "FIRE_RESISTANCE"}),
   GLOWING(new String[]{"GLOW", "SHINE", "SHINY"}),
   HARM(new String[]{"INJURE", "DAMAGE", "HARMING", "INFLICT", "INSTANT_DAMAGE"}),
   HEAL(new String[]{"HEALTH", "INSTA_HEAL", "INSTANT_HEAL", "INSTA_HEALTH", "INSTANT_HEALTH"}),
   HEALTH_BOOST(new String[]{"BOOST_HEALTH", "BOOST", "HP"}),
   HERO_OF_THE_VILLAGE(new String[]{"HERO", "VILLAGE_HERO"}),
   HUNGER(new String[]{"STARVE", "HUNGRY"}),
   INCREASE_DAMAGE(new String[]{"STRENGTH", "BULL", "STRONG", "ATTACK"}),
   INVISIBILITY(new String[]{"INVISIBLE", "VANISH", "INVIS", "DISAPPEAR", "HIDE"}),
   JUMP(new String[]{"LEAP", "JUMP_BOOST"}),
   LEVITATION(new String[]{"LEVITATE"}),
   LUCK(new String[]{"LUCKY"}),
   NIGHT_VISION(new String[]{"VISION", "VISION_NIGHT"}),
   POISON(new String[]{"VENOM"}),
   REGENERATION(new String[]{"REGEN"}),
   SATURATION(new String[]{"FOOD"}),
   SLOW(new String[]{"SLOWNESS", "SLUGGISH"}),
   SLOW_DIGGING(new String[]{"FATIGUE", "DULL", "DIGGING", "SLOW_DIG", "DIG_SLOW"}),
   SLOW_FALLING(new String[]{"SLOW_FALL", "FALL_SLOW"}),
   SPEED(new String[]{"SPRINT", "RUNFAST", "SWIFT", "FAST"}),
   UNLUCK(new String[]{"UNLUCKY"}),
   WATER_BREATHING(new String[]{"WATER_BREATH", "UNDERWATER_BREATHING", "UNDERWATER_BREATH", "AIR"}),
   WEAKNESS(new String[]{"WEAK"}),
   WITHER(new String[]{"DECAY"});

   public static final XPotion[] VALUES = values();
   public static final Set<XPotion> DEBUFFS = Collections.unmodifiableSet(EnumSet.of(BAD_OMEN, BLINDNESS, CONFUSION, HARM, HUNGER, LEVITATION, POISON, SLOW, SLOW_DIGGING, UNLUCK, WEAKNESS, WITHER));
   private static final XPotion[] POTIONEFFECTTYPE_MAPPING = new XPotion[VALUES.length + 1];
   private final PotionEffectType type = PotionEffectType.getByName(this.name());

   private XPotion(@Nonnull String... aliases) {
      XPotion.Data.NAMES.put(this.name(), this);
      for (String legacy : aliases) {
         XPotion.Data.NAMES.put(legacy, this);
      }

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
   public static Optional<XPotion> matchXPotion(@Nonnull String potion) {
      if (potion != null && !potion.isEmpty()) {
         PotionEffectType idType = fromId(potion);
         if (idType != null) {
            XPotion type = (XPotion)XPotion.Data.NAMES.get(idType.getName());
            if (type == null) {
               throw new NullPointerException("Unsupported potion effect type ID: " + idType);
            } else {
               return Optional.of(type);
            }
         } else {
            return Optional.ofNullable((XPotion)XPotion.Data.NAMES.get(format(potion)));
         }
      } else {
         throw new IllegalArgumentException("Cannot match XPotion of a null or empty potion effect type");
      }
   }

   @Nonnull
   public static XPotion matchXPotion(@Nonnull PotionEffectType type) {
      Objects.requireNonNull(type, "Cannot match XPotion of a null potion effect type");
      return POTIONEFFECTTYPE_MAPPING[type.getId()];
   }

   @Nullable
   private static PotionEffectType fromId(@Nonnull String type) {
      try {
         int id = Integer.parseInt(type);
         return PotionEffectType.getById(id);
      } catch (NumberFormatException ignored) {
         return null;
      }
   }

   private static List<String> split(@Nonnull String str, char separatorChar) {
      List<String> list = new ArrayList(5);
      boolean match = false;
      boolean lastMatch = false;
      int len = str.length();
      int start = 0;

      for(int i = 0; i < len; ++i) {
         if (str.charAt(i) == separatorChar) {
            if (match) {
               list.add(str.substring(start, i));
               match = false;
               lastMatch = true;
            }

            start = i + 1;
         } else {
            lastMatch = false;
            match = true;
         }
      }

      if (match || lastMatch) {
         list.add(str.substring(start, len));
      }

      return list;
   }

   @Nullable
   public static XPotion.Effect parseEffect(@Nullable String potion) {
      if (!Strings.isNullOrEmpty(potion) && !potion.equalsIgnoreCase("none")) {
         List<String> split = split(potion.replace(" ", ""), ',');
         if (split.isEmpty()) {
            split = split(potion, ' ');
         }

         double chance = 100.0D;
         int chanceIndex = 0;
         if (split.size() > 2) {
            chanceIndex = ((String)split.get(2)).indexOf(37);
            if (chanceIndex != -1) {
               try {
                  chance = Double.parseDouble(((String)split.get(2)).substring(chanceIndex + 1));
               } catch (NumberFormatException ignored) {
                  chance = 100.0D;
               }
            }
         }

         Optional<XPotion> typeOpt = matchXPotion((String)split.get(0));
         if (!typeOpt.isPresent()) {
            return null;
         } else {
            PotionEffectType type = ((XPotion)typeOpt.get()).type;
            if (type == null) {
               return null;
            } else {
               int duration = 2400;
               int amplifier = 0;
               if (split.size() > 1) {
                  duration = toInt((String)split.get(1), 1) * 20;
                  if (split.size() > 2) {
                     amplifier = toInt(chanceIndex <= 0 ? (String)split.get(2) : ((String)split.get(2)).substring(0, chanceIndex), 1) - 1;
                  }
               }

               return new XPotion.Effect(new PotionEffect(type, duration, amplifier), chance);
            }
         }
      } else {
         return null;
      }
   }

   private static int toInt(String str, int defaultValue) {
      try {
         return Integer.parseInt(str);
      } catch (NumberFormatException ignored) {
         return defaultValue;
      }
   }

   public static void addEffects(@Nonnull LivingEntity entity, @Nullable List<String> effects) {
      Objects.requireNonNull(entity, "Cannot add potion effects to null entity");
      for (XPotion.Effect effect : parseEffects(effects)) {
         effect.apply(entity);
      }
   }

   public static List<XPotion.Effect> parseEffects(@Nullable List<String> effectsString) {
      if (effectsString != null && !effectsString.isEmpty()) {
         List<XPotion.Effect> effects = new ArrayList(effectsString.size());
         for (String effectStr : effectsString) {
            XPotion.Effect effect = parseEffect(effectStr);
            if (effect != null) {
               effects.add(effect);
            }
         }
         return effects;
      } else {
         return new ArrayList();
      }
   }

   @Nonnull
   public static ThrownPotion throwPotion(@Nonnull LivingEntity entity, @Nullable Color color, @Nullable PotionEffect... effects) {
      Objects.requireNonNull(entity, "Cannot throw potion from null entity");
      Material splashPotionMaterial = Material.getMaterial("SPLASH_POTION");
      ItemStack potion = splashPotionMaterial == null ? new ItemStack(Material.POTION, 1, (short)16398) : new ItemStack(splashPotionMaterial);
      PotionMeta meta = (PotionMeta)potion.getItemMeta();
      // Use reflection to call setColor, as method may not be available in all Bukkit versions
      if (color != null) {
         try {
            java.lang.reflect.Method setColorMethod = PotionMeta.class.getMethod("setColor", Color.class);
            setColorMethod.invoke(meta, color);
         } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            // If setColor is not available, silently ignore (color may be set via potion type/effects in newer versions)
         }
      }
      if (effects != null) {
         for (PotionEffect effect : effects) {
            meta.addCustomEffect(effect, true);
         }
      }

      potion.setItemMeta(meta);
      ThrownPotion thrownPotion = (ThrownPotion)entity.launchProjectile(ThrownPotion.class);
      thrownPotion.setItem(potion);
      return thrownPotion;
   }

   @Nonnull
   public static ItemStack buildItemWithEffects(@Nonnull Material type, @Nullable Color color, @Nullable PotionEffect... effects) {
      Objects.requireNonNull(type, "Cannot build an effected item with null type");
      if (!canHaveEffects(type)) {
         throw new IllegalArgumentException("Cannot build item with " + type.name() + " potion type");
      } else {
         ItemStack item = new ItemStack(type);
         PotionMeta meta = (PotionMeta)item.getItemMeta();
         // Use reflection to call setColor, as method may not be available in all Bukkit versions
         if (color != null) {
            try {
               java.lang.reflect.Method setColorMethod = PotionMeta.class.getMethod("setColor", Color.class);
               setColorMethod.invoke(meta, color);
            } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
               // If setColor is not available, silently ignore (color may be set via potion type/effects in newer versions)
            }
         }
         Material splashPotion = Material.getMaterial("SPLASH_POTION");
         Material tippedArrow = Material.getMaterial("TIPPED_ARROW");
         Material lingeringPotion = Material.getMaterial("LINGERING_POTION");
         meta.setDisplayName(type == Material.POTION ? "Potion" : (splashPotion != null && type == splashPotion ? "Splash Potion" : (tippedArrow != null && type == tippedArrow ? "Tipped Arrow" : (lingeringPotion != null && type == lingeringPotion ? "Lingering Potion" : "Potion"))));
         if (effects != null) {
            for (PotionEffect effect : effects) {
               meta.addCustomEffect(effect, true);
            }
         }

         item.setItemMeta(meta);
         return item;
      }
   }

   public static boolean canHaveEffects(@Nullable Material material) {
      return material != null && (material.name().endsWith("POTION") || material.name().startsWith("TIPPED_ARROW"));
   }

   @Nullable
   public PotionEffectType getPotionEffectType() {
      return this.type;
   }

   public boolean isSupported() {
      return this.type != null;
   }

   @Nullable
   public XPotion or(@Nullable XPotion alternatePotion) {
      return this.isSupported() ? this : alternatePotion;
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public PotionType getPotionType() {
      return this.type == null ? null : PotionType.getByEffect(this.type);
   }

   @Nullable
   public PotionEffect buildPotionEffect(int duration, int amplifier) {
      return this.type == null ? null : new PotionEffect(this.type, duration, amplifier - 1);
   }

   public String toString() {
      return (String)Arrays.stream(this.name().split("_")).map((t) -> {
         return t.charAt(0) + t.substring(1).toLowerCase();
      }).collect(Collectors.joining(" "));
   }

   // $FF: synthetic method
   private static XPotion[] $values() {
      return new XPotion[]{ABSORPTION, BAD_OMEN, BLINDNESS, CONDUIT_POWER, CONFUSION, DAMAGE_RESISTANCE, DARKNESS, DOLPHINS_GRACE, FAST_DIGGING, FIRE_RESISTANCE, GLOWING, HARM, HEAL, HEALTH_BOOST, HERO_OF_THE_VILLAGE, HUNGER, INCREASE_DAMAGE, INVISIBILITY, JUMP, LEVITATION, LUCK, NIGHT_VISION, POISON, REGENERATION, SATURATION, SLOW, SLOW_DIGGING, SLOW_FALLING, SPEED, UNLUCK, WATER_BREATHING, WEAKNESS, WITHER};
   }

   static {
      for (XPotion pot : VALUES) {
         if (pot.type != null) {
            POTIONEFFECTTYPE_MAPPING[pot.type.getId()] = pot;
         }
      }
   }

   private static final class Data {
      private static final Map<String, XPotion> NAMES = new HashMap();
   }

   public static class Effect {
      private PotionEffect effect;
      private double chance;

      public Effect(PotionEffect effect, double chance) {
         this.effect = effect;
         this.chance = chance;
      }

      public XPotion getXPotion() {
         return XPotion.matchXPotion(this.effect.getType());
      }

      public double getChance() {
         return this.chance;
      }

      public boolean hasChance() {
         return this.chance >= 100.0D || ThreadLocalRandom.current().nextDouble(0.0D, 100.0D) <= this.chance;
      }

      public void setChance(double chance) {
         this.chance = chance;
      }

      public void apply(LivingEntity entity) {
         if (this.hasChance()) {
            entity.addPotionEffect(this.effect);
         }

      }

      public PotionEffect getEffect() {
         return this.effect;
      }

      public void setEffect(PotionEffect effect) {
         this.effect = effect;
      }
   }
}
