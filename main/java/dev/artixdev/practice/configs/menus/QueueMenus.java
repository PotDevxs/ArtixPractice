package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class QueueMenus extends ChildYamlStorage {
   @ConfigValue(
      path = "QUEUE-MENUS.SOLO-MENU.TITLE",
      priority = 1
   )
   public static String QUEUE_SOLO_TITLE = "<type> » Solo";
   @ConfigValue(
      path = "QUEUE-MENUS.SOLO-MENU.SIZE",
      priority = 2
   )
   public static int QUEUE_SOLO_SIZE = -1;
   @ConfigValue(
      path = "QUEUE-MENUS.SOLO-MENU.KIT-NAME",
      priority = 2
   )
   public static String QUEUE_SOLO_KIT_NAME = "&c<kit>";
   @ConfigValue(
      path = "QUEUE-MENUS.SOLO-MENU.UNRANKED-LORE",
      priority = 3
   )
   public static List<String> UNRANKED_LORE_SOLO = Arrays.asList("", "&fPlaying: &c<playing>", "&fQueuing: &c<queued>", "", "&fYour Win Streak: &c<winstreak>", "&fHighest Win Streak: &c<highestWinstreak>", "", " &f1. &7<player1> &f- &c<winstreak1>", " &f2. &7<player2> &f- &c<winstreak2>", " &f3. &7<player3> &f- &c<winstreak3>", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.SOLO-MENU.RANKED-LORE",
      priority = 4
   )
   public static List<String> RANKED_LORE_SOLO = Arrays.asList("", "&fPlaying: &c<playing>", "&fQueuing: &c<queued>", "", "&fYour Elo: &c<elo>", "&fYour Win Streak: &c<winstreak> ", "&fHighest Win Streak: &c<highestWinstreak>", "", " &f1. &7<player1> &f- &c<elo1>", " &f2. &7<player2> &f- &c<elo2>", " &f3. &7<player3> &f- &c<elo3>", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.DUO-MENU.TITLE",
      priority = 4
   )
   public static String QUEUE_DUO_TITLE = "<type> » Duo";
   @ConfigValue(
      path = "QUEUE-MENUS.SOLO-MENU.SIZE",
      priority = 2
   )
   public static int QUEUE_DUO_SIZE = -1;
   @ConfigValue(
      path = "QUEUE-MENUS.DUO-MENU.KIT-NAME",
      priority = 5
   )
   public static String QUEUE_DUO_KIT_NAME = "&c<kit>";
   @ConfigValue(
      path = "QUEUE-MENUS.DUO-MENU.UNRANKED-LORE",
      priority = 6
   )
   public static List<String> UNRANKED_LORE_DUO = Arrays.asList("", "&fPlaying: &c<playing>", "&fQueuing: &c<queued>", "", "&fYour Win Streak: &c<winstreak>", "&fHighest Win Streak: &c<highestWinstreak>", "", " &f1. &7<player1> &f- &c<winstreak1>", " &f2. &7<player2> &f- &c<winstreak2>", " &f3. &7<player3> &f- &c<winstreak3>", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.DUO-MENU.RANKED-LORE",
      priority = 7
   )
   public static List<String> RANKED_LORE_DUO = Arrays.asList("", "&fPlaying: &c<playing>", "&fQueuing: &c<queued>", "", "&fYour Elo: &c<elo>", "&fYour Win Streak: &c<winstreak>", "&fHighest Win Streak: &c<highestWinstreak>", "", " &f1. &7<player1> &f- &c<elo1>", " &f2. &7<player2> &f- &c<elo2>", " &f3. &7<player3> &f- &c<elo3>", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.TITLE",
      priority = 8
   )
   public static String QUEUE_TYPE_TITLE = "&7Select a type of Queue";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SIZE",
      priority = 9
   )
   public static int QUEUE_TYPE_SIZE = 27;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.UNRANKED-NAME",
      priority = 10
   )
   public static String SOLO_TYPE_UNRANKED_NAME = "&aQueue Unranked";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.UNRANKED-MATERIAL",
      priority = 11
   )
   public static String SOLO_TYPE_UNRANKED_MATERIAL = "IRON_SWORD";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.UNRANKED-DURABILITY",
      priority = 12
   )
   public static int SOLO_TYPE_UNRANKED_DURABILITY = 0;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.UNRANKED-SLOT",
      priority = 13
   )
   public static int SOLO_TYPE_UNRANKED_SLOT = 12;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.UNRANKED-LORE",
      priority = 14
   )
   public static List<String> SOLO_TYPE_UNRANKED_LORE = Arrays.asList("", "&7Casual 1v1 Matches", "&7with no loss penalty!", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.RANKED-NAME",
      priority = 15
   )
   public static String SOLO_TYPE_RANKED_NAME = "&bQueue Ranked";
   @ConfigValue(
      path = "QUEUE-MENUS.SOLO-TYPE.RANKED-MATERIAL",
      priority = 16
   )
   public static String SOLO_TYPE_RANKED_MATERIAL = "DIAMOND_SWORD";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.RANKED-DURABILITY",
      priority = 17
   )
   public static int SOLO_TYPE_RANKED_DURABILITY = 0;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.RANKED-SLOT",
      priority = 18
   )
   public static int SOLO_TYPE_RANKED_SLOT = 14;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.SOLO-TYPE.RANKED-LORE",
      priority = 19
   )
   public static List<String> SOLO_TYPE_RANKED_LORE = Arrays.asList("", "&7Competitive 1v1 Matches", "&7with a loss penalty!", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.UNRANKED-NAME",
      priority = 20
   )
   public static String DUO_TYPE_UNRANKED_NAME = "&aQueue Unranked";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.UNRANKED-MATERIAL",
      priority = 21
   )
   public static String DUO_TYPE_UNRANKED_MATERIAL = "IRON_SWORD";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.UNRANKED-DURABILITY",
      priority = 22
   )
   public static int DUO_TYPE_UNRANKED_DURABILITY = 0;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.UNRANKED-SLOT",
      priority = 23
   )
   public static int DUO_TYPE_UNRANKED_SLOT = 12;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.UNRANKED-LORE",
      priority = 24
   )
   public static List<String> DUO_TYPE_UNRANKED_LORE = Arrays.asList("", "&7Casual 1v1 Matches", "&7with no loss penalty!", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.RANKED-NAME",
      priority = 25
   )
   public static String DUO_TYPE_RANKED_NAME = "&bQueue Ranked";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.RANKED-MATERIAL",
      priority = 26
   )
   public static String DUO_TYPE_RANKED_MATERIAL = "DIAMOND_SWORD";
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.RANKED-DURABILITY",
      priority = 27
   )
   public static int DUO_TYPE_RANKED_DURABILITY = 0;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.RANKED-SLOT",
      priority = 28
   )
   public static int DUO_TYPE_RANKED_SLOT = 14;
   @ConfigValue(
      path = "QUEUE-MENUS.QUEUE-TYPE.DUO-TYPE.RANKED-LORE",
      priority = 29
   )
   public static List<String> DUO_TYPE_RANKED_LORE = Arrays.asList("", "&7Competitive 1v1 Matches", "&7with a loss penalty!", "", "&eClick to play!");
   @ConfigValue(
      path = "QUEUE-MENUS.RANDOM-QUEUE-BUTTON.ENABLED",
      priority = 30
   )
   public static boolean RANDOM_QUEUE_BUTTON_ENABLED = true;
   @ConfigValue(
      path = "QUEUE-MENUS.RANDOM-QUEUE-BUTTON.SLOT",
      priority = 31
   )
   public static int RANDOM_QUEUE_SLOT = 49;
   @ConfigValue(
      path = "QUEUE-MENUS.RANDOM-QUEUE-BUTTON.NAME",
      priority = 32
   )
   public static String RANDOM_QUEUE_NAME = "&c&lRandom Queue";
   @ConfigValue(
      path = "QUEUE-MENUS.RANDOM-QUEUE-BUTTON.MATERIAL",
      priority = 33
   )
   public static String RANDOM_QUEUE_MATERIAL = "EYE_OF_ENDER";
   @ConfigValue(
      path = "QUEUE-MENUS.RANDOM-QUEUE-BUTTON.DURABILITY",
      priority = 34
   )
   public static int RANDOM_QUEUE_DURABILITY = 0;
   @ConfigValue(
      path = "QUEUE-MENUS.RANDOM-QUEUE-BUTTON.LORE",
      priority = 35
   )
   public static List<String> RANDOM_QUEUE_LORE = Arrays.asList("", "&7Click to join a random queue.");
   @ConfigValue(
      path = "BOT-PRESET-MENU.TITLE",
      priority = 36
   )
   public static String BOT_PRESET_TITLE = "&7Select a Bot Preset";
   @ConfigValue(
      path = "BOT-PRESET-MENU.SIZE",
      priority = 37
   )
   public static int BOT_PRESET_SIZE = 36;
   @ConfigValue(
      path = "BOT-PRESET-MENU.PRESET-BUTTON.NAME",
      priority = 38
   )
   public static String BOT_PRESET_NAME = "<display_name>";
   @ConfigValue(
      path = "BOT-PRESET-MENU.PRESET-BUTTON.LORE",
      priority = 39
   )
   public static List<String> BOT_PRESET_LORE = Arrays.asList("&7", " &c&lDifficulty", "&c  • &fCPS: &c<cps>", "&c  • &f&lRange: &c<range>", "&c  • &f&lPing &c<ping>ms", "", " &c&lTraits", "&c  • &fTry-hard: &c<tryhard>", "&c  • &fStrafe: &c<strafe>", "&c  • &fW-Tap: &c<wtap>", "&c  • &fBlockHit: &c<blockhit>", "&c  • &fSide Pearl: &c<sidepearl> &7(Requires Try-hard)", "", " &eClick to select &r<display_name>&e.", "&7");
   @ConfigValue(
      path = "BOT-PRESET-MENU.CUSTOM-PRESET.NAME",
      priority = 40
   )
   public static String BOT_PRESET_CUSTOM_NAME = "&c&lCustom Preset";
   @ConfigValue(
      path = "BOT-PRESET-MENU.CUSTOM-PRESET.MATERIAL",
      priority = 41
   )
   public static String BOT_PRESET_CUSTOM_MATERIAL = "NETHER_STAR";
   @ConfigValue(
      path = "BOT-PRESET-MENU.CUSTOM-PRESET.DURABILITY",
      priority = 42
   )
   public static int BOT_PRESET_CUSTOM_DURABILITY = 0;
   @ConfigValue(
      path = "BOT-PRESET-MENU.CUSTOM-PRESET.SLOT",
      priority = 43
   )
   public static int BOT_PRESET_CUSTOM_SLOT = 31;
   @ConfigValue(
      path = "BOT-PRESET-MENU.CUSTOM-PRESET.LORE",
      priority = 44
   )
   public static List<String> BOT_PRESET_CUSTOM_LORE = Arrays.asList("", "&7Make a custom preset and change", "&7all preset settings yourself to", "&7have a completely unique experience.", "", "&eLeft-Click to use custom preset.", "&eRight-Click to edit custom preset.");
   @ConfigValue(
      path = "BOT-PRESET-MENU.CUSTOM-PRESET.NO-PERM-LORE",
      priority = 45
   )
   public static List<String> BOT_PRESET_CUSTOM_NO_PERM_LORE = Arrays.asList("", "&7Make a custom preset and change", "&7all preset settings yourself to", "&7have a completely unique experience.", "", "&cYou don't have permission to use this!");

   public QueueMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = QueueMenus.class.getFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(ConfigValue.class)) {
            annotatedFields.add(field);
         }
      }

      return annotatedFields;
   }
}
