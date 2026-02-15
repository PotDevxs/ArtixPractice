package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class MatchMenus extends ChildYamlStorage {
   @ConfigValue(
      priority = 1,
      path = "MATCH-MENUS.MATCH-HISTORY.TITLE"
   )
   public static String MATCH_HISTORY_TITLE = "Match History » <player>";
   @ConfigValue(
      priority = 2,
      path = "MATCH-MENUS.MATCH-HISTORY.SIZE",
      comment = "This size is only for when history is empty"
   )
   public static int MATCH_HISTORY_SIZE = 27;
   @ConfigValue(
      priority = 2,
      comment = "Uses kit's icon instead of the material below",
      path = "MATCH-MENUS.MATCH-HISTORY.KIT-DISPLAY-ICON"
   )
   public static Boolean MATCH_HISTORY_USE_KIT_DISPLAY_ICON = true;
   @ConfigValue(
      priority = 3,
      path = "MATCH-MENUS.MATCH-HISTORY.MATERIAL"
   )
   public static String MATCH_HISTORY_MATERIAL = "BOOK";
   @ConfigValue(
      priority = 4,
      path = "MATCH-MENUS.MATCH-HISTORY.NAME"
   )
   public static String MATCH_HISTORY_NAME = "&c&lMatch #<id> &7┃ <status>";
   @ConfigValue(
      priority = 5,
      path = "MATCH-MENUS.MATCH-HISTORY.LORE"
   )
   public static List<String> MATCH_HISTORY_LORE = Arrays.asList("&8<createdAt>", "", "&fWinner: &a<winner>", "&fLoser: &c<loser>", "", "&fType: &c<type>", "&fKit: &c<kit>", "&fArena: &c<arena>", "", "&fLeft click to view match inventories.", "<revertElo>");
   @ConfigValue(
      priority = 6,
      path = "MATCH-MENUS.MATCH-HISTORY.STATUS-WON"
   )
   public static String MATCH_HISTORY_STATUS_WON = "&a(Won)";
   @ConfigValue(
      priority = 7,
      path = "MATCH-MENUS.MATCH-HISTORY.STATUS-LOST"
   )
   public static String MATCH_HISTORY_STATUS_LOST = "&c(Lost)";
   @ConfigValue(
      priority = 8,
      path = "MATCH-MENUS.MATCH-HISTORY.WINNER-ELO"
   )
   public static String MATCH_HISTORY_WINNER_ELO = " &a(+<elo> ELO)";
   @ConfigValue(
      priority = 9,
      path = "MATCH-MENUS.MATCH-HISTORY.LOSER-ELO"
   )
   public static String MATCH_HISTORY_LOSER_ELO = " &c(-<elo> ELO)";
   @ConfigValue(
      priority = 10,
      path = "MATCH-MENUS.MATCH-HISTORY.POINTS"
   )
   public static String MATCH_HISTORY_POINTS = " &7(<points> Points)";
   @ConfigValue(
      priority = 10,
      path = "MATCH-MENUS.MATCH-HISTORY.LIVES"
   )
   public static String MATCH_HISTORY_LIVES = " &7(<lives> Lives)";
   @ConfigValue(
      priority = 12,
      path = "MATCH-MENUS.MATCH-HISTORY.REVERT-ELO"
   )
   public static String MATCH_HISTORY_REVERT_ELO = "&fMiddle click to revert elo changes";
   @ConfigValue(
      priority = 13,
      path = "MATCH-MENUS.MATCH-HISTORY.NO-MATCHES.MATERIAL"
   )
   public static String MATCH_HISTORY_NO_MATCHES_ITEM = "REDSTONE_BLOCK";
   @ConfigValue(
      priority = 14,
      path = "MATCH-MENUS.MATCH-HISTORY.NO-MATCHES.DURABILITY"
   )
   public static int MATCH_HISTORY_NO_MATCHES_DURABILITY = 0;
   @ConfigValue(
      priority = 15,
      path = "MATCH-MENUS.MATCH-HISTORY.NO-MATCHES.NAME"
   )
   public static String MATCH_HISTORY_NO_MATCHES_NAME = "&c&lNo Matches Found";
   @ConfigValue(
      priority = 16,
      path = "MATCH-MENUS.MATCH-HISTORY.NO-MATCHES.SELF-LORE"
   )
   public static List<String> MATCH_HISTORY_NO_MATCHES_SELF_LORE = Arrays.asList("", "&7No matches could be found on your profile.", "&7When you play a match it will be logged here.");
   @ConfigValue(
      priority = 16,
      path = "MATCH-MENUS.MATCH-HISTORY.NO-MATCHES.OTHER-LORE"
   )
   public static List<String> MATCH_HISTORY_NO_MATCHES_OTHER_LORE = Arrays.asList("", "&7No matches could be found on <player>'s profile.", "&7When they play a match it will be logged here.");
   @ConfigValue(
      priority = 17,
      path = "MATCH-MENUS.MATCH-HISTORY.NO-MATCHES.SLOT"
   )
   public static int NO_MATCH_HISTORY_FOUND_SLOT = 4;
   @ConfigValue(
      priority = 18,
      path = "MATCH-MENUS.REVERT-HISTORY.TITLE"
   )
   public static String MATCH_MENUS_REVERT_ELO_TITLE = "Revert Elo";
   @ConfigValue(
      priority = 19,
      path = "MATCH-MENUS.REVERT-HISTORY.NAME"
   )
   public static String MATCH_MENUS_REVERT_ELO_NAME = "&a&lRevert Elo";
   @ConfigValue(
      priority = 20,
      path = "MATCH-MENUS.REVERT-HISTORY.LORE"
   )
   public static List<String> MATCH_MENUS_REVERT_ELO_LORE = Arrays.asList("", "&7Are you sure you want to", "&7make the following changes:", "", "&f<winner> ELO&7: &c-<winnerEloChange>", "&f<loser> ELO&7: &a+<loserEloChange>", "", "&aLeft-Click to confirm stats changes.", "&cRight-Click to cancel stats changes.");
   @ConfigValue(
      priority = 21,
      path = "MATCH-MENUS.MATCH-INVENTORY.TITLE"
   )
   public static String MATCH_INVENTORY_TITLE = "Inventory » <player>";
   @ConfigValue(
      priority = 22,
      path = "MATCH-MENUS.MATCH-INVENTORY.NEXT-INVENTORY-BUTTON.NAME"
   )
   public static String MATCH_INVENTORY_NEXT_INVENTORY_NAME = "&aNext Inventory";
   @ConfigValue(
      priority = 23,
      path = "MATCH-MENUS.MATCH-INVENTORY.NEXT-INVENTORY-BUTTON.MATERIAL"
   )
   public static String MATCH_INVENTORY_NEXT_INVENTORY_MATERIAL = "LEVER";
   @ConfigValue(
      priority = 24,
      path = "MATCH-MENUS.MATCH-INVENTORY.NEXT-INVENTORY-BUTTON.DURABILITY"
   )
   public static int MATCH_INVENTORY_NEXT_INVENTORY_DURABILITY = 0;
   @ConfigValue(
      priority = 25,
      path = "MATCH-MENUS.MATCH-INVENTORY.NEXT-INVENTORY-BUTTON.LORE"
   )
   public static List<String> MATCH_INVENTORY_NEXT_INVENTORY_LORE = Arrays.asList(" &7* &7Switch to &c<player> &7's inventory");
   @ConfigValue(
      priority = 22,
      path = "MATCH-MENUS.MATCH-INVENTORY.HEALTH-BUTTON.NAME"
   )
   public static String MATCH_INVENTORY_HEALTH_NAME = "&cHealth: &f<health> &4❤";
   @ConfigValue(
      priority = 23,
      path = "MATCH-MENUS.MATCH-INVENTORY.HEALTH-BUTTON.MATERIAL"
   )
   public static String MATCH_INVENTORY_HEALTH_MATERIAL = "PLAYER_HEAD";
   @ConfigValue(
      priority = 24,
      path = "MATCH-MENUS.MATCH-INVENTORY.HEALTH-BUTTON.DURABILITY"
   )
   public static int MATCH_INVENTORY_HEALTH_DURABILITY = 0;
   @ConfigValue(
      priority = 25,
      path = "MATCH-MENUS.MATCH-INVENTORY.HEALTH-BUTTON.LORE"
   )
   public static List<String> MATCH_INVENTORY_HEALTH_LORE = Arrays.asList();
   @ConfigValue(
      priority = 26,
      path = "MATCH-MENUS.MATCH-INVENTORY.HUNGER-BUTTON.NAME"
   )
   public static String MATCH_INVENTORY_HUNGER_NAME = "&cHunger: &f<hunger>";
   @ConfigValue(
      priority = 27,
      path = "MATCH-MENUS.MATCH-INVENTORY.HUNGER-BUTTON.MATERIAL"
   )
   public static String MATCH_INVENTORY_HUNGER_MATERIAL = "COOKED_BEEF";
   @ConfigValue(
      priority = 28,
      path = "MATCH-MENUS.MATCH-INVENTORY.HUNGER-BUTTON.DURABILITY"
   )
   public static int MATCH_INVENTORY_HUNGER_DURABILITY = 0;
   @ConfigValue(
      priority = 29,
      path = "MATCH-MENUS.MATCH-INVENTORY.HUNGER-BUTTON.LORE"
   )
   public static List<String> MATCH_INVENTORY_HUNGER_LORE = Arrays.asList();
   @ConfigValue(
      priority = 30,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTION-EFFECTS-BUTTON.NAME"
   )
   public static String MATCH_INVENTORY_POTION_EFFECTS_NAME = "&cPotion Effects";
   @ConfigValue(
      priority = 31,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTION-EFFECTS-BUTTON.FORMAT"
   )
   public static String MATCH_INVENTORY_POTION_EFFECTS_FORMAT = " &7* &c<effect> &7(<duration>)";
   @ConfigValue(
      priority = 32,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTION-EFFECTS-BUTTON.MATERIAL"
   )
   public static String MATCH_INVENTORY_POTION_EFFECTS_MATERIAL = "BREWING_STAND";
   @ConfigValue(
      priority = 33,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTION-EFFECTS-BUTTON.DURABILITY"
   )
   public static int MATCH_INVENTORY_POTION_EFFECTS_DURABILITY = 0;
   @ConfigValue(
      priority = 34,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTION-EFFECTS-BUTTON.EFFECTS-LORE"
   )
   public static List<String> MATCH_INVENTORY_POTION_EFFECTS_LORE = Arrays.asList("", "<effects>");
   @ConfigValue(
      priority = 35,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTION-EFFECTS-BUTTON.NO-EFFECTS-LORE"
   )
   public static List<String> MATCH_INVENTORY_NO_POTION_EFFECTS_LORE = Arrays.asList(" &7* &7No potion effects.");
   @ConfigValue(
      priority = 36,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTIONS-BUTTON.NAME"
   )
   public static String MATCH_INVENTORY_POTIONS_NAME = "&cHealth Potions";
   @ConfigValue(
      priority = 37,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTIONS-BUTTON.MATERIAL"
   )
   public static String MATCH_INVENTORY_POTIONS_MATERIAL = "SPLASH_POTION";
   @ConfigValue(
      priority = 38,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTIONS-BUTTON.DURABILITY"
   )
   public static int MATCH_INVENTORY_POTIONS_DURABILITY = 16421;
   @ConfigValue(
      priority = 39,
      path = "MATCH-MENUS.MATCH-INVENTORY.POTIONS-BUTTON.LORE"
   )
   public static List<String> MATCH_INVENTORY_POTIONS_LORE = Arrays.asList("", " &7* &c<player> &fhad &c<count> &cpotion(s) &fleft.");
   @ConfigValue(
      priority = 40,
      path = "MATCH-MENUS.MATCH-INVENTORY.STATISTICS-BUTTON.NAME"
   )
   public static String MATCH_INVENTORY_STATISTICS_NAME = "&cStatistics";
   @ConfigValue(
      priority = 41,
      path = "MATCH-MENUS.MATCH-INVENTORY.STATISTICS-BUTTON.MATERIAL"
   )
   public static String MATCH_INVENTORY_STATISTICS_MATERIAL = "PAPER";
   @ConfigValue(
      priority = 42,
      path = "MATCH-MENUS.MATCH-INVENTORY.STATISTICS-BUTTON.DURABILITY"
   )
   public static int MATCH_INVENTORY_STATISTICS_DURABILITY = 0;
   @ConfigValue(
      priority = 43,
      path = "MATCH-MENUS.MATCH-INVENTORY.STATISTICS-BUTTON.LORE"
   )
   public static List<String> MATCH_INVENTORY_STATISTICS_LORE = Arrays.asList("", " &7* &fNormal Hits: &c<totalHits>", " &7* &fBlocked Hits: &c<blockedHits>", " &7* &fLatest Combo: &c<totalCombos>", " &7* &fLongest Combo: &c<longestCombo>", " &7* &fPotions Thrown: &c<potionsThrown>", " &7* &fPotions Missed: &c<potionsMissed>", " &7* &fPotion Accuracy: &c<potionAccuracy>");

   public MatchMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = MatchMenus.class.getFields();
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
