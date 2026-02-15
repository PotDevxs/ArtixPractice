package dev.artixdev.practice.configs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.BasicYamlStorage;

public class BotsConfig extends BasicYamlStorage {
   public static double TICK_FACTOR = 22.549999999999997D;
   public static double ALT_TICK_FACTOR = 23.75D;
   @ConfigValue(
      priority = 1,
      path = "BOT-SETTINGS.ENABLED",
      comment = "Should the bots be enabled at all?"
   )
   public static boolean BOTS_ENABLED = true;
   @ConfigValue(
      priority = 3,
      path = "BOT-SETTINGS.FAST-POTS",
      comment = "This essentially makes the bot w-tap before potting. Use this if your potion settings in spigot\nare configured to have fast-potions and only work when they w-tap, otherwise bot will keep potting and not fight."
   )
   public static boolean FAST_POTIONS = false;
   @ConfigValue(
      priority = 3,
      path = "BOT-SETTINGS.ALTERNATE-HIT-FACTOR",
      comment = "Use an alternative tick factor which makes the hit delay a bit higher than the normal one.\nThis may or may not improve knockback and over experience of bot pvp depending on knockback settings."
   )
   public static boolean ALTERNATE_HIT_FACTOR = false;
   @ConfigValue(
      priority = 3,
      path = "BOT-SETTINGS.REDUCE-RANGE-ON-COMBO",
      comment = "Should we reduce the bot's attack range when it's getting comboed for more smoother combos."
   )
   public static boolean REDUCE_RANGE_ON_COMBO = true;
   @ConfigValue(
      priority = 3,
      path = "BOT-SETTINGS.SPIGOT-BASED-KB",
      comment = "This will disable bolt's bot knockback system and use spigot's knockback system instead.\nIt is recommended to only enable this if you know the spigot will support Citizens NPC knockback.\nIf the spigot uses a kohi-based knockback system or mSpigot based knockback system, then it's likely it will."
   )
   public static boolean SPIGOT_BASED_KB = false;
   @ConfigValue(
      priority = 4,
      path = "BOT-SETTINGS.NORMAL-MOVEMENT-SPEED",
      comment = "This is the movement speed for normal non-tryhard bots."
   )
   public static double NORMAL_MOVEMENT_SPEED = 1.0D;
   @ConfigValue(
      priority = 4,
      path = "BOT-SETTINGS.TRY-HARD-MOVEMENT-SPEED",
      comment = "This is the movement speed for tryhard bots."
   )
   public static double TRY_HARD_MOVEMENT_SPEED = 1.15D;
   @ConfigValue(
      priority = 4,
      path = "BOT-SETTINGS.SPEED-EFFECT-MOVEMENT-SPEED",
      comment = "This is the movement speed for that is applied when bot has a speed potion effect."
   )
   public static double SPEED_EFFECT_MOVEMENT_SPEED = 1.29D;
   @ConfigValue(
      priority = 5,
      path = "BOT-SETTINGS.MODES.DEFAULT.KITS"
   )
   public static List<String> DEFAULT_KITS = Arrays.asList("Sumo");
   @ConfigValue(
      priority = 6,
      path = "BOT-SETTINGS.MODES.SPEED-PVP.ENABLED"
   )
   public static boolean SPEED_PVP_ENABLED = true;
   @ConfigValue(
      priority = 7,
      path = "BOT-SETTINGS.MODES.SPEED-PVP.KITS",
      comment = "Configure what kits do you want assigned to this bot mode."
   )
   public static List<String> SPEED_KITS = Arrays.asList("NoDebuff", "Debuff");
   @ConfigValue(
      priority = 8,
      path = "BOT-SETTINGS.MODES.GOLDEN-APPLE.ENABLED"
   )
   public static boolean GAPPLE_ENABLED = true;
   @ConfigValue(
      priority = 9,
      path = "BOT-SETTINGS.MODES.GOLDEN-APPLE.KITS",
      comment = "Configure what kits do you want assigned to this bot mode."
   )
   public static List<String> GAPPLE_KITS = Arrays.asList("Gapple", "Combo");
   @ConfigValue(
      priority = 10,
      path = "BOT-SETTINGS.MODES.UHC.ENABLED"
   )
   public static boolean UHC_ENABLED = true;
   @ConfigValue(
      priority = 11,
      path = "BOT-SETTINGS.MODES.UHC.KITS",
      comment = "Configure what kits do you want assigned to this bot mode."
   )
   public static List<String> UHC_KITS = Arrays.asList("BuildUHC", "FinalUHC", "SG");
   @ConfigValue(
      priority = 12,
      path = "BOT-SETTINGS.MODES.SOUP.ENABLED"
   )
   public static boolean SOUP_ENABLED = true;
   @ConfigValue(
      priority = 13,
      path = "BOT-SETTINGS.MODES.SOUP.KITS",
      comment = "Configure what kits do you want assigned to this bot mode."
   )
   public static List<String> SOUP_KITS = Arrays.asList("Soup");
   @ConfigValue(
      priority = 100,
      path = "BOT-NAMES"
   )
   public static List<String> BOT_NAMES = Arrays.asList("Marcel", "Jewdah", "Defused", "OhEmilyyy", "Amaan", "Zefew", "Miami", "ByRez", "Tryhard", "PotFast", "LigmaSpark", "Brewage", "Bl4stymineman", "sbeenz", "Clare", "kayalust", "ZIBLACKINGGG", "Staind", "Dowze", "SumTing", "gopu", "Scholar", "idiol", "CL0CK", "flaxeneel2", "Zeynah", "Adviser", "Dreamer_420", "Rases", "AYEAR", "Labdy", "Mnyue", "VolcrzR", "Dubai", "Eloies", "Vers", "Hydrize", "P0kemon", "bcz", "IIDK", "yungsaphars", "Swampie", "ODxRay", "Posed", "Slexi", "Zylowh", "Elb1to", "Zyphing", "DANTEH", "Topu", "Futi", "Glory", "Reboting", "Tylarzz", "kayalust", "Suchspeed", "Tediosuy", "Huahwi", "NotDrizzy", "Creaxx");

   public BotsConfig(JavaPlugin plugin, String name) {
      super(plugin, name, false);
   }

   public void addSeparateComments() {
      this.addComment("BOT-SETTINGS.MODES.DEFAULT", "This is the default bot-mode, which has no logic pre-determined.\nKits like Sumo which do not have any logic except just knocking the player off the platform are applicable here.");
      this.addComment("BOT-SETTINGS.MODES.UHC.ENABLED", "Do you want to enable the UHC bot mode?");
      this.addComment("BOT-SETTINGS.MODES.SPEED-PVP.ENABLED", "Do you want to enable the Speed PvP bot mode?");
      this.addComment("BOT-SETTINGS.MODES.GOLDEN-APPLE.ENABLED", "Do you want to enable the Golden Apple bot mode?");
      this.addComment("BOT-SETTINGS.MODES.SOUP.ENABLED", "Do you want to enable the Soup bot mode?");
      this.addCommentWithBlankLine("BOT-PRESETS", "Here you can add/change bot presets, these are built-in configured settings of the bot.\nThese settings directly affect the pvp experience and its difficulty.");
      this.addCommentWithBlankLine("BOT-NAMES", "Bolt randomly picks a name from this list, although if you want to enforce only one name\nthen just put one single name here. Note that putting any color codes here won't work, that's in the Bot-Prefix");
      this.addCommentWithBlankLine("BOT-SETTINGS.MODES", "Bot modes are essentially pre-determined logic related to the kit for the bot.\nFor example, Speed PvP mode would be catering to Speed kits like NoDebuff, Debuff etc.\nMeanwhile, UHC mode would be catering to BuildUHC or SG type of kits. (Keep in mind, Bot cannot build right now)");
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = BotsConfig.class.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
            ConfigValue configValue = (ConfigValue)field.getAnnotation(ConfigValue.class);
            if (configValue != null) {
               annotatedFields.add(field);
            }
         }
      }

      annotatedFields.sort(Comparator.comparingInt((fieldx) -> {
         return ((ConfigValue)fieldx.getAnnotation(ConfigValue.class)).priority();
      }));
      return annotatedFields;
   }

   public String[] getHeader() {
      return new String[]{"This is the configuration file for the PvP-Bots of Bolt. There's a lot of things to configure along", "with the knockback profiles specifically made for the bots. The default config contains four bot presets", "that have pre-defined combat settings which will be perfectly fine if you use them as is. As always if you", "need any help, feel free to contact customer support at https://dsc.gg/refine", null};
   }
}
