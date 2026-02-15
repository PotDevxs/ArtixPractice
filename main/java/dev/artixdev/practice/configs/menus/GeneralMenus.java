package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class GeneralMenus extends ChildYamlStorage {
   @ConfigValue(
      priority = 1,
      path = "GENERAL-MENUS.SETTINGS-MENU.TITLE"
   )
   public static String SETTINGS_MENU_TITLE = "&7Profile Settings";
   @ConfigValue(
      priority = 9,
      path = "GENERAL-MENUS.SETTINGS-MENU.VANILLA-TAB.NAME"
   )
   public static String SETTINGS_MENU_VANILLA_TAB_NAME = "&e&lVanilla Tablist";
   @ConfigValue(
      priority = 10,
      path = "GENERAL-MENUS.SETTINGS-MENU.VANILLA-TAB.MATERIAL"
   )
   public static String SETTINGS_MENU_VANILLA_TAB_MATERIAL = "WATCH";
   @ConfigValue(
      priority = 11,
      path = "GENERAL-MENUS.SETTINGS-MENU.VANILLA-TAB.DURABILITY"
   )
   public static int SETTINGS_MENU_VANILLA_TAB_DURABILITY = 0;
   @ConfigValue(
      priority = 12,
      path = "GENERAL-MENUS.SETTINGS-MENU.VANILLA-TAB.SLOT"
   )
   public static int SETTINGS_MENU_VANILLA_TAB_SLOT = 0;
   @ConfigValue(
      priority = 13,
      path = "GENERAL-MENUS.SETTINGS-MENU.VANILLA-TAB.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_VANILLA_TAB_ENABLED_LORE = Arrays.asList("&7Would you like to see modern", "&7or vanilla tab?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 14,
      path = "GENERAL-MENUS.SETTINGS-MENU.VANILLA-TAB.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_VANILLA_TAB_DISABLED_LORE = Arrays.asList("&7Would you like to see modern", "&7or vanilla tab?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 10,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-REQUESTS.NAME"
   )
   public static String SETTINGS_MENU_DUEL_REQUESTS_NAME = "&e&lDuel Requests";
   @ConfigValue(
      priority = 11,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-REQUESTS.MATERIAL"
   )
   public static String SETTINGS_MENU_DUEL_REQUESTS_MATERIAL = "DIAMOND_SWORD";
   @ConfigValue(
      priority = 12,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-REQUESTS.DURABILITY"
   )
   public static int SETTINGS_MENU_DUEL_REQUESTS_DURABILITY = 0;
   @ConfigValue(
      priority = 13,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-REQUESTS.SLOT"
   )
   public static int SETTINGS_MENU_DUEL_REQUESTS_SLOT = 1;
   @ConfigValue(
      priority = 14,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-REQUESTS.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_DUEL_REQUESTS_ENABLED_LORE = Arrays.asList("&7Would you like to receive duel", "&7from other players?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 15,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-REQUESTS.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_DUEL_REQUESTS_DISABLED_LORE = Arrays.asList("&7Would you like to receive duel", "&7from other players?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 14,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATORS.NAME"
   )
   public static String SETTINGS_MENU_SPECTATORS_NAME = "&e&lAllow Spectators";
   @ConfigValue(
      priority = 15,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATORS.MATERIAL"
   )
   public static String SETTINGS_MENU_SPECTATORS_MATERIAL = "ITEM_FRAME";
   @ConfigValue(
      priority = 16,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATORS.DURABILITY"
   )
   public static int SETTINGS_MENU_SPECTATORS_DURABILITY = 0;
   @ConfigValue(
      priority = 17,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATORS.SLOT"
   )
   public static int SETTINGS_MENU_SPECTATORS_SLOT = 2;
   @ConfigValue(
      priority = 18,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATORS.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_SPECTATORS_ENABLED_LORE = Arrays.asList("&7Would you like other players to", "&7be able to spectate your matches?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 19,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATORS.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_SPECTATORS_DISABLED_LORE = Arrays.asList("&7Would you like other players to", "&7be able to spectate your matches?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 20,
      path = "GENERAL-MENUS.SETTINGS-MENU.SIDEBAR-VISIBILITY.NAME"
   )
   public static String SETTINGS_MENU_SIDEBAR_VISIBILITY_NAME = "&e&lSidebar Visibility";
   @ConfigValue(
      priority = 21,
      path = "GENERAL-MENUS.SETTINGS-MENU.SIDEBAR-VISIBILITY.MATERIAL"
   )
   public static String SETTINGS_MENU_SIDEBAR_VISIBILITY_MATERIAL = "BOOK";
   @ConfigValue(
      priority = 22,
      path = "GENERAL-MENUS.SETTINGS-MENU.SIDEBAR-VISIBILITY.DURABILITY"
   )
   public static int SETTINGS_MENU_SIDEBAR_VISIBILITY_DURABILITY = 0;
   @ConfigValue(
      priority = 23,
      path = "GENERAL-MENUS.SETTINGS-MENU.SIDEBAR-VISIBILITY.SLOT"
   )
   public static int SETTINGS_MENU_SIDEBAR_VISIBILITY_SLOT = 3;
   @ConfigValue(
      priority = 24,
      path = "GENERAL-MENUS.SETTINGS-MENU.SIDEBAR-VISIBILITY.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_SIDEBAR_VISIBILITY_ENABLED_LORE = Arrays.asList("&7Would you like to be able", "&7to view the sidebar?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 25,
      path = "GENERAL-MENUS.SETTINGS-MENU.SIDEBAR-VISIBILITY.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_SIDEBAR_VISIBILITY_DISABLED_LORE = Arrays.asList("&7Would you like to be able", "&7to view the sidebar?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 26,
      path = "GENERAL-MENUS.SETTINGS-MENU.TOURNAMENT-MESSAGES.NAME"
   )
   public static String SETTINGS_MENU_TOURNAMENT_MESSAGES_NAME = "&e&lTournament Messages";
   @ConfigValue(
      priority = 27,
      path = "GENERAL-MENUS.SETTINGS-MENU.TOURNAMENT-MESSAGES.MATERIAL"
   )
   public static String SETTINGS_MENU_TOURNAMENT_MESSAGES_MATERIAL = "SIGN";
   @ConfigValue(
      priority = 28,
      path = "GENERAL-MENUS.SETTINGS-MENU.TOURNAMENT-MESSAGES.DURABILITY"
   )
   public static int SETTINGS_MENU_TOURNAMENT_MESSAGES_DURABILITY = 0;
   @ConfigValue(
      priority = 29,
      path = "GENERAL-MENUS.SETTINGS-MENU.TOURNAMENT-MESSAGES.SLOT"
   )
   public static int SETTINGS_MENU_TOURNAMENT_MESSAGES_SLOT = 4;
   @ConfigValue(
      priority = 30,
      path = "GENERAL-MENUS.SETTINGS-MENU.TOURNAMENT-MESSAGES.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_TOURNAMENT_MESSAGES_ENABLED_LORE = Arrays.asList("&7Would you like to see", "&7tournament messages?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 31,
      path = "GENERAL-MENUS.SETTINGS-MENU.TOURNAMENT-MESSAGES.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_TOURNAMENT_MESSAGES_DISABLED_LORE = Arrays.asList("&7Would you like to see", "&7tournament messages?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 32,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAYER-VISIBILITY.NAME"
   )
   public static String SETTINGS_MENU_PLAYER_VISIBILITY_NAME = "&e&lPlayer Visibility";
   @ConfigValue(
      priority = 33,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAYER-VISIBILITY.MATERIAL"
   )
   public static String SETTINGS_MENU_PLAYER_VISIBILITY_MATERIAL = "EYE_OF_ENDER";
   @ConfigValue(
      priority = 34,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAYER-VISIBILITY.DURABILITY"
   )
   public static int SETTINGS_MENU_PLAYER_VISIBILITY_DURABILITY = 0;
   @ConfigValue(
      priority = 35,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAYER-VISIBILITY.SLOT"
   )
   public static int SETTINGS_MENU_PLAYER_VISIBILITY_SLOT = 5;
   @ConfigValue(
      priority = 36,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAYER-VISIBILITY.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PLAYER_VISIBILITY_ENABLED_LORE = Arrays.asList("&7Would you like to be able to", "&7see other players?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 37,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAYER-VISIBILITY.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PLAYER_VISIBILITY_DISABLED_LORE = Arrays.asList("&7Would you like to be able to", "&7see other players?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 38,
      path = "GENERAL-MENUS.SETTINGS-MENU.PING-FACTOR.NAME"
   )
   public static String SETTINGS_MENU_PING_FACTOR_NAME = "&e&lPing Range";
   @ConfigValue(
      priority = 39,
      path = "GENERAL-MENUS.SETTINGS-MENU.PING-FACTOR.MATERIAL"
   )
   public static String SETTINGS_MENU_PING_FACTOR_MATERIAL = "LEVER";
   @ConfigValue(
      priority = 40,
      path = "GENERAL-MENUS.SETTINGS-MENU.PING-FACTOR.DURABILITY"
   )
   public static int SETTINGS_MENU_PING_FACTOR_DURABILITY = 0;
   @ConfigValue(
      priority = 41,
      path = "GENERAL-MENUS.SETTINGS-MENU.PING-FACTOR.SLOT"
   )
   public static int SETTINGS_MENU_PING_FACTOR_SLOT = 6;
   @ConfigValue(
      priority = 42,
      path = "GENERAL-MENUS.SETTINGS-MENU.PING-FACTOR.LORE"
   )
   public static List<String> SETTINGS_MENU_PING_FACTOR_ENABLED_LORE = Arrays.asList("&7Would you like to enable ping difference limit", "&7between the players you queue with?", "", "&a■ &f<ping_fact_amount>", " ", "&fClick to change.");
   @ConfigValue(
      priority = 43,
      path = "GENERAL-MENUS.SETTINGS-MENU.PING-FACTOR.NO-PERMISSION-LORE"
   )
   public static List<String> SETTINGS_MENU_PING_FACTOR_NO_PERM_LORE = Arrays.asList("&7Would you like to enable ping difference limit", "&7between the players you queue with?", "", "&a■ &fUnrestricted", " ", "&cNo permission.");
   @ConfigValue(
      priority = 44,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-SOUNDS.NAME"
   )
   public static String SETTINGS_MENU_DUEL_SOUNDS_NAME = "&e&lDuel Sounds";
   @ConfigValue(
      priority = 45,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-SOUNDS.MATERIAL"
   )
   public static String SETTINGS_MENU_DUEL_SOUNDS_MATERIAL = "NOTE_BLOCK";
   @ConfigValue(
      priority = 46,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-SOUNDS.DURABILITY"
   )
   public static int SETTINGS_MENU_DUEL_SOUNDS_DURABILITY = 0;
   @ConfigValue(
      priority = 47,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-SOUNDS.SLOT"
   )
   public static int SETTINGS_MENU_DUEL_SOUNDS_SLOT = 7;
   @ConfigValue(
      priority = 48,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-SOUNDS.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_DUEL_SOUNDS_ENABLED_LORE = Arrays.asList("&7Would you like to be able to hear duel", "&7sounds when someone sends a request?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 49,
      path = "GENERAL-MENUS.SETTINGS-MENU.DUEL-SOUNDS.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_DUEL_SOUNDS_DISABLED_LORE = Arrays.asList("&7Would you like to be able to hear duel", "&7sounds when someone sends a request?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 50,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN.NAME"
   )
   public static String SETTINGS_MENU_PLAY_AGAIN_NAME = "&e&lPlay Again";
   @ConfigValue(
      priority = 51,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN.MATERIAL"
   )
   public static String SETTINGS_MENU_PLAY_AGAIN_MATERIAL = "PAPER";
   @ConfigValue(
      priority = 52,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN.DURABILITY"
   )
   public static int SETTINGS_MENU_PLAY_AGAIN_DURABILITY = 0;
   @ConfigValue(
      priority = 53,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN.SLOT"
   )
   public static int SETTINGS_MENU_PLAY_AGAIN_SLOT = 8;
   @ConfigValue(
      priority = 54,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PLAY_AGAIN_ENABLED_LORE = Arrays.asList("&7Would you like to be able to use the", "&7play again item after a match?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 55,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PLAY_AGAIN_DISABLED_LORE = Arrays.asList("&7Would you like to be able to use the", "&7play again item after a match?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 62,
      path = "GENERAL-MENUS.SETTINGS-MENU.TIME.NAME"
   )
   public static String SETTINGS_MENU_TIME_NAME = "&e&lPlayer Time";
   @ConfigValue(
      priority = 63,
      path = "GENERAL-MENUS.SETTINGS-MENU.TIME.MATERIAL"
   )
   public static String SETTINGS_MENU_TIME_MATERIAL = "WATCH";
   @ConfigValue(
      priority = 64,
      path = "GENERAL-MENUS.SETTINGS-MENU.TIME.DURABILITY"
   )
   public static int SETTINGS_MENU_TIME_DURABILITY = 0;
   @ConfigValue(
      priority = 65,
      path = "GENERAL-MENUS.SETTINGS-MENU.TIME.SLOT"
   )
   public static int SETTINGS_MENU_TIME_SLOT = 9;
   @ConfigValue(
      priority = 66,
      path = "GENERAL-MENUS.SETTINGS-MENU.TIME.LORE"
   )
   public static List<String> SETTINGS_MENU_TIME_ENABLED_LORE = Arrays.asList("&7What time of the day would", "&7you like to have?", "", "<toggled_day>■ &fDay", "<toggled_sunset>■ &fSunset", "<toggled_night>■ &fNight", " ", "&fClick to change.");
   @ConfigValue(
      priority = 67,
      path = "GENERAL-MENUS.SETTINGS-MENU.ANIMATE-DEATH.NAME"
   )
   public static String SETTINGS_MENU_ANIMATE_DEATH_NAME = "&e&lDeath Animation";
   @ConfigValue(
      priority = 68,
      path = "GENERAL-MENUS.SETTINGS-MENU.ANIMATE-DEATH.MATERIAL"
   )
   public static String SETTINGS_MENU_ANIMATE_DEATH_MATERIAL = "ENCHANTED_BOOK";
   @ConfigValue(
      priority = 69,
      path = "GENERAL-MENUS.SETTINGS-MENU.ANIMATE-DEATH.DURABILITY"
   )
   public static int SETTINGS_MENU_ANIMATE_DEATH_DURABILITY = 0;
   @ConfigValue(
      priority = 70,
      path = "GENERAL-MENUS.SETTINGS-MENU.ANIMATE-DEATH.SLOT"
   )
   public static int SETTINGS_MENU_ANIMATE_DEATH_SLOT = 10;
   @ConfigValue(
      priority = 71,
      path = "GENERAL-MENUS.SETTINGS-MENU.ANIMATE-DEATH.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_ANIMATE_DEATH_ENABLED_LORE = Arrays.asList("&7Would you like to enable death animation", "&7for the targets that you kill?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 72,
      path = "GENERAL-MENUS.SETTINGS-MENU.ANIMATE-DEATH.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_ANIMATE_DEATH_DISABLED_LORE = Arrays.asList("&7Would you like to enable death animation", "&7for the targets that you kill?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 73,
      path = "GENERAL-MENUS.SETTINGS-MENU.ANIMATE-DEATH.NO-PERMISSION-LORE"
   )
   public static List<String> SETTINGS_MENU_ANIMATE_DEATH_NO_PERM_LORE = Arrays.asList("&7Would you like to enable death animation", "&7for the targets that you kill?", "", "&7■ &fEnabled", "&7■ &fDisabled", " ", "&cNo permission.");
   @ConfigValue(
      priority = 74,
      path = "GENERAL-MENUS.SETTINGS-MENU.DROP-ITEMS.NAME"
   )
   public static String SETTINGS_MENU_DROP_ITEMS_NAME = "&e&lDrop Items";
   @ConfigValue(
      priority = 75,
      path = "GENERAL-MENUS.SETTINGS-MENU.DROP-ITEMS.MATERIAL"
   )
   public static String SETTINGS_MENU_DROP_ITEMS_MATERIAL = "GLASS_BOTTLE";
   @ConfigValue(
      priority = 76,
      path = "GENERAL-MENUS.SETTINGS-MENU.DROP-ITEMS.DURABILITY"
   )
   public static int SETTINGS_MENU_DROP_ITEMS_DURABILITY = 0;
   @ConfigValue(
      priority = 77,
      path = "GENERAL-MENUS.SETTINGS-MENU.DROP-ITEMS.SLOT"
   )
   public static int SETTINGS_MENU_DROP_ITEMS_SLOT = 11;
   @ConfigValue(
      priority = 78,
      path = "GENERAL-MENUS.SETTINGS-MENU.DROP-ITEMS.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_DROP_ITEMS_ENABLED_LORE = Arrays.asList("&7Would you like to enable item drops", "&7for the targets that you kill? (Only for Solo)", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 79,
      path = "GENERAL-MENUS.SETTINGS-MENU.DROP-ITEMS.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_DROP_ITEMS_DISABLED_LORE = Arrays.asList("&7Would you like to enable item drops", "&7for the targets that you kill? (Only for Solo)", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 80,
      path = "GENERAL-MENUS.SETTINGS-MENU.DROP-ITEMS.NO-PERMISSION-LORE"
   )
   public static List<String> SETTINGS_MENU_DROP_ITEMS_NO_PERM_LORE = Arrays.asList("&7Would you like to enable item drops", "&7for the targets that you kill? (Only for Solo)", "", "&7■ &fEnabled", "&7■ &fDisabled", " ", "&cNo permission.");
   @ConfigValue(
      priority = 81,
      path = "GENERAL-MENUS.SETTINGS-MENU.FLY-MODE.NAME"
   )
   public static String SETTINGS_MENU_FLY_MODE_NAME = "&e&lFly Mode";
   @ConfigValue(
      priority = 82,
      path = "GENERAL-MENUS.SETTINGS-MENU.FLY-MODE.MATERIAL"
   )
   public static String SETTINGS_MENU_FLY_MODE_MATERIAL = "FEATHER";
   @ConfigValue(
      priority = 83,
      path = "GENERAL-MENUS.SETTINGS-MENU.FLY-MODE.DURABILITY"
   )
   public static int SETTINGS_MENU_FLY_MODE_DURABILITY = 0;
   @ConfigValue(
      priority = 84,
      path = "GENERAL-MENUS.SETTINGS-MENU.FLY-MODE.SLOT"
   )
   public static int SETTINGS_MENU_FLY_MODE_SLOT = 12;
   @ConfigValue(
      priority = 85,
      path = "GENERAL-MENUS.SETTINGS-MENU.FLY-MODE.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_FLY_MODE_ENABLED_LORE = Arrays.asList("&7Would you like to start flying when", "&7a solo match ends?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 86,
      path = "GENERAL-MENUS.SETTINGS-MENU.FLY-MODE.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_FLY_MODE_DISABLED_LORE = Arrays.asList("&7Would you like to start flying when", "&7a solo match ends?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 87,
      path = "GENERAL-MENUS.SETTINGS-MENU.FLY-MODE.NO-PERMISSION-LORE"
   )
   public static List<String> SETTINGS_MENU_FLY_MODE_NO_PERM_LORE = Arrays.asList("&7Would you like to start flying when", "&7a solo match ends?", "", "&7■ &fEnabled", "&7■ &fDisabled", " ", "&cNo permission.");
   @ConfigValue(
      priority = 88,
      path = "GENERAL-MENUS.SETTINGS-MENU.CLEAR-INVENTORY.NAME"
   )
   public static String SETTINGS_MENU_CLEAR_INVENTORY_NAME = "&e&lClear Inventory";
   @ConfigValue(
      priority = 89,
      path = "GENERAL-MENUS.SETTINGS-MENU.CLEAR-INVENTORY.MATERIAL"
   )
   public static String SETTINGS_MENU_CLEAR_INVENTORY_MATERIAL = "CHEST";
   @ConfigValue(
      priority = 90,
      path = "GENERAL-MENUS.SETTINGS-MENU.CLEAR-INVENTORY.DURABILITY"
   )
   public static int SETTINGS_MENU_CLEAR_INVENTORY_DURABILITY = 0;
   @ConfigValue(
      priority = 91,
      path = "GENERAL-MENUS.SETTINGS-MENU.CLEAR-INVENTORY.SLOT"
   )
   public static int SETTINGS_MENU_CLEAR_INVENTORY_SLOT = 13;
   @ConfigValue(
      priority = 92,
      path = "GENERAL-MENUS.SETTINGS-MENU.CLEAR-INVENTORY.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_CLEAR_INVENTORY_ENABLED_LORE = Arrays.asList("&7Would you like to clear your inventory", "&7when a solo match ends?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 93,
      path = "GENERAL-MENUS.SETTINGS-MENU.CLEAR-INVENTORY.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_CLEAR_INVENTORY_DISABLED_LORE = Arrays.asList("&7Would you like to clear your inventory", "&7when a solo match ends?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 94,
      path = "GENERAL-MENUS.SETTINGS-MENU.CLEAR-INVENTORY.NO-PERMISSION-LORE"
   )
   public static List<String> SETTINGS_MENU_CLEAR_INVENTORY_NO_PERM_LORE = Arrays.asList("&7Would you like to clear your inventory", "&7when a solo match ends?", "", "&7■ &fEnabled", "&7■ &fDisabled", " ", "&cNo permission.");
   @ConfigValue(
      priority = 95,
      path = "GENERAL-MENUS.SETTINGS-MENU.PARTY-REQUESTS.NAME"
   )
   public static String SETTINGS_MENU_PARTY_REQUESTS_NAME = "&e&lParty Requests";
   @ConfigValue(
      priority = 96,
      path = "GENERAL-MENUS.SETTINGS-MENU.PARTY-REQUESTS.MATERIAL"
   )
   public static String SETTINGS_MENU_PARTY_REQUESTS_MATERIAL = "ITEM_FRAME";
   @ConfigValue(
      priority = 97,
      path = "GENERAL-MENUS.SETTINGS-MENU.PARTY-REQUESTS.DURABILITY"
   )
   public static int SETTINGS_MENU_PARTY_REQUESTS_DURABILITY = 0;
   @ConfigValue(
      priority = 98,
      path = "GENERAL-MENUS.SETTINGS-MENU.PARTY-REQUESTS.SLOT"
   )
   public static int SETTINGS_MENU_PARTY_REQUESTS_SLOT = 14;
   @ConfigValue(
      priority = 99,
      path = "GENERAL-MENUS.SETTINGS-MENU.PARTY-REQUESTS.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PARTY_REQUESTS_ENABLED_LORE = Arrays.asList("&7Would you like to receive party", "&7from other players?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 100,
      path = "GENERAL-MENUS.SETTINGS-MENU.PARTY-REQUESTS.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PARTY_REQUESTS_DISABLED_LORE = Arrays.asList("&7Would you like to receive party", "&7from other players?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 101,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATOR-MESSAGES.NAME"
   )
   public static String SETTINGS_MENU_SPECTATOR_MESSAGES_NAME = "&e&lSpectator Messages";
   @ConfigValue(
      priority = 102,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATOR-MESSAGES.MATERIAL"
   )
   public static String SETTINGS_MENU_SPECTATOR_MESSAGES_MATERIAL;
   @ConfigValue(
      priority = 103,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATOR-MESSAGES.DURABILITY"
   )
   public static int SETTINGS_MENU_SPECTATOR_MESSAGES_DURABILITY;
   @ConfigValue(
      priority = 104,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATOR-MESSAGES.SLOT"
   )
   public static int SETTINGS_MENU_SPECTATOR_MESSAGES_SLOT;
   @ConfigValue(
      priority = 105,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATOR-MESSAGES.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_SPECTATOR_MESSAGES_ENABLED_LORE;
   @ConfigValue(
      priority = 106,
      path = "GENERAL-MENUS.SETTINGS-MENU.SPECTATOR-MESSAGES.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_SPECTATOR_MESSAGES_DISABLED_LORE;
   @ConfigValue(
      priority = 101,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN-TYPE.NAME"
   )
   public static String SETTINGS_MENU_PLAY_AGAIN_TYPE_NAME;
   @ConfigValue(
      priority = 102,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN-TYPE.MATERIAL"
   )
   public static String SETTINGS_MENU_PLAY_AGAIN_TYPE_MATERIAL;
   @ConfigValue(
      priority = 103,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN-TYPE.DURABILITY"
   )
   public static int SETTINGS_MENU_PLAY_AGAIN_TYPE_DURABILITY;
   @ConfigValue(
      priority = 104,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN-TYPE.SLOT"
   )
   public static int SETTINGS_MENU_PLAY_AGAIN_TYPE_SLOT;
   @ConfigValue(
      priority = 105,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN-TYPE.ENABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PLAY_AGAIN_TYPE_ENABLED_LORE;
   @ConfigValue(
      priority = 106,
      path = "GENERAL-MENUS.SETTINGS-MENU.PLAY-AGAIN-TYPE.DISABLED-LORE"
   )
   public static List<String> SETTINGS_MENU_PLAY_AGAIN_TYPE_DISABLED_LORE;
   @ConfigValue(
      priority = 107,
      path = "GENERAL-MENUS.SETTINGS-MENU.PHOENIX-SETTINGS-NAME"
   )
   public static String SETTINGS_MENU_PHOENIX_SETTINGS_NAME;
   @ConfigValue(
      priority = 107,
      path = "GENERAL-MENUS.SETTINGS-MENU.PHOENIX-SETTINGS-LORE"
   )
   public static List<String> SETTINGS_MENU_PHOENIX_SETTINGS_LORE;
   @ConfigValue(
      priority = 107,
      path = "GENERAL-MENUS.SETTINGS-MENU.PHOENIX-SETTINGS.MATERIAL"
   )
   public static String SETTINGS_MENU_PHOENIX_SETTINGS_MATERIAL;
   @ConfigValue(
      priority = 107,
      path = "GENERAL-MENUS.SETTINGS-MENU.PHOENIX-SETTINGS.DURABILITY"
   )
   public static int SETTINGS_MENU_PHOENIX_SETTINGS_DURABILITY;
   @ConfigValue(
      priority = 107,
      path = "GENERAL-MENUS.SETTINGS-MENU.PHOENIX-SETTINGS.SLOT"
   )
   public static int SETTINGS_MENU_PHOENIX_SETTINGS_SLOT;
   @ConfigValue(
      priority = 67,
      path = "GENERAL-MENUS.PROFILE-MENU.TITLE"
   )
   public static String PROFILE_MENU_TITLE;
   @ConfigValue(
      priority = 68,
      path = "GENERAL-MENUS.PROFILE-MENU.SIZE"
   )
   public static int PROFILE_MENU_SIZE;
   @ConfigValue(
      priority = 75,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.STATISTICS.NAME"
   )
   public static String PROFILE_MENU_STATISTICS_NAME;
   @ConfigValue(
      priority = 75,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.STATISTICS.ENABLED"
   )
   public static boolean PROFILE_MENU_STATISTICS_ENABLED;
   @ConfigValue(
      priority = 76,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.STATISTICS.MATERIAL"
   )
   public static String PROFILE_MENU_STATISTICS_MATERIAL;
   @ConfigValue(
      priority = 77,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.STATISTICS.DURABILITY"
   )
   public static int PROFILE_MENU_STATISTICS_DURABILITY;
   @ConfigValue(
      priority = 78,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.STATISTICS.SLOT"
   )
   public static int PROFILE_MENU_STATISTICS_SLOT;
   @ConfigValue(
      priority = 79,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.STATISTICS.LORE"
   )
   public static List<String> PROFILE_MENU_STATISTICS_LORE;
   @ConfigValue(
      priority = 80,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.HISTORY.NAME"
   )
   public static String PROFILE_MENU_HISTORY_NAME;
   @ConfigValue(
      priority = 75,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.HISTORY.ENABLED"
   )
   public static boolean PROFILE_MENU_HISTORY_ENABLED;
   @ConfigValue(
      priority = 81,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.HISTORY.MATERIAL"
   )
   public static String PROFILE_MENU_HISTORY_MATERIAL;
   @ConfigValue(
      priority = 82,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.HISTORY.DURABILITY"
   )
   public static int PROFILE_MENU_HISTORY_DURABILITY;
   @ConfigValue(
      priority = 83,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.HISTORY.SLOT"
   )
   public static int PROFILE_MENU_HISTORY_SLOT;
   @ConfigValue(
      priority = 84,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.HISTORY.LORE"
   )
   public static List<String> PROFILE_MENU_HISTORY_LORE;
   @ConfigValue(
      priority = 85,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.DIVISIONS.NAME"
   )
   public static String PROFILE_MENU_DIVISIONS_NAME;
   @ConfigValue(
      priority = 75,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.DIVISIONS.ENABLED"
   )
   public static boolean PROFILE_MENU_DIVISIONS_ENABLED;
   @ConfigValue(
      priority = 86,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.DIVISIONS.MATERIAL"
   )
   public static String PROFILE_MENU_DIVISIONS_MATERIAL;
   @ConfigValue(
      priority = 87,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.DIVISIONS.DURABILITY"
   )
   public static int PROFILE_MENU_DIVISIONS_DURABILITY;
   @ConfigValue(
      priority = 88,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.DIVISIONS.SLOT"
   )
   public static int PROFILE_MENU_DIVISIONS_SLOT;
   @ConfigValue(
      priority = 89,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.DIVISIONS.LORE"
   )
   public static List<String> PROFILE_MENU_DIVISIONS_LORE;
   @ConfigValue(
      priority = 90,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.SETTINGS.NAME"
   )
   public static String PROFILE_MENU_SETTINGS_NAME;
   @ConfigValue(
      priority = 75,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.SETTINGS.ENABLED"
   )
   public static boolean PROFILE_MENU_SETTINGS_ENABLED;
   @ConfigValue(
      priority = 91,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.SETTINGS.MATERIAL"
   )
   public static String PROFILE_MENU_SETTINGS_MATERIAL;
   @ConfigValue(
      priority = 92,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.SETTINGS.DURABILITY"
   )
   public static int PROFILE_MENU_SETTINGS_DURABILITY;
   @ConfigValue(
      priority = 93,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.SETTINGS.SLOT"
   )
   public static int PROFILE_MENU_SETTINGS_SLOT;
   @ConfigValue(
      priority = 94,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.SETTINGS.LORE"
   )
   public static List<String> PROFILE_MENU_SETTINGS_LORE;
   @ConfigValue(
      priority = 95,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.COSMETICS.NAME"
   )
   public static String PROFILE_MENU_COSMETICS_NAME;
   @ConfigValue(
      priority = 75,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.COSMETICS.ENABLED"
   )
   public static boolean PROFILE_MENU_COSMETICS_ENABLED;
   @ConfigValue(
      priority = 96,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.COSMETICS.MATERIAL"
   )
   public static String PROFILE_MENU_COSMETICS_MATERIAL;
   @ConfigValue(
      priority = 97,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.COSMETICS.DURABILITY"
   )
   public static int PROFILE_MENU_COSMETICS_DURABILITY;
   @ConfigValue(
      priority = 98,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.COSMETICS.SLOT"
   )
   public static int PROFILE_MENU_COSMETICS_SLOT;
   @ConfigValue(
      priority = 99,
      path = "GENERAL-MENUS.PROFILE-MENU.BUTTONS.COSMETICS.LORE"
   )
   public static List<String> PROFILE_MENU_COSMETICS_LORE;
   @ConfigValue(
      priority = 100,
      path = "GENERAL-MENUS.COSMETICS-MENU.TITLE"
   )
   public static String COSMETICS_MENU_TITLE;
   @ConfigValue(
      priority = 101,
      path = "GENERAL-MENUS.COSMETICS-MENU.SIZE"
   )
   public static int COSMETICS_MENU_SIZE;
   @ConfigValue(
      priority = 103,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-MESSAGES.NAME"
   )
   public static String COSMETICS_MENU_KILL_MESSAGES_NAME;
   @ConfigValue(
      priority = 104,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-MESSAGES.MATERIAL"
   )
   public static String COSMETICS_MENU_KILL_MESSAGES_MATERIAL;
   @ConfigValue(
      priority = 105,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-MESSAGES.DURABILITY"
   )
   public static int COSMETICS_MENU_KILL_MESSAGES_DURABILITY;
   @ConfigValue(
      priority = 106,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-MESSAGES.SLOT"
   )
   public static int COSMETICS_MENU_KILL_MESSAGES_SLOT;
   @ConfigValue(
      priority = 107,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-MESSAGES.LORE"
   )
   public static List<String> COSMETICS_MENU_KILL_MESSAGES_LORE;
   @ConfigValue(
      priority = 108,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-EFFECTS.NAME"
   )
   public static String COSMETICS_MENU_KILL_EFFECTS_NAME;
   @ConfigValue(
      priority = 109,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-EFFECTS.MATERIAL"
   )
   public static String COSMETICS_MENU_KILL_EFFECTS_MATERIAL;
   @ConfigValue(
      priority = 109,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-EFFECTS.DURABILITY"
   )
   public static int COSMETICS_MENU_KILL_EFFECTS_DURABILITY;
   @ConfigValue(
      priority = 109,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-EFFECTS.SLOT"
   )
   public static int COSMETICS_MENU_KILL_EFFECTS_SLOT;
   @ConfigValue(
      priority = 110,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.KILL-EFFECTS.LORE"
   )
   public static List<String> COSMETICS_MENU_KILL_EFFECTS_LORE;
   @ConfigValue(
      priority = 111,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.PROJECTILE-TRAILS.NAME"
   )
   public static String COSMETICS_MENU_PROJECTILE_TRAILS_NAME;
   @ConfigValue(
      priority = 112,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.PROJECTILE-TRAILS.MATERIAL"
   )
   public static String COSMETICS_MENU_PROJECTILE_TRAILS_MATERIAL;
   @ConfigValue(
      priority = 113,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.PROJECTILE-TRAILS.DURABILITY"
   )
   public static int COSMETICS_MENU_PROJECTILE_TRAILS_DURABILITY;
   @ConfigValue(
      priority = 114,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.PROJECTILE-TRAILS.SLOT"
   )
   public static int COSMETICS_MENU_PROJECTILE_TRAILS_SLOT;
   @ConfigValue(
      priority = 115,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.PROJECTILE-TRAILS.LORE"
   )
   public static List<String> COSMETICS_MENU_PROJECTILE_TRAILS_LORE;
   @ConfigValue(
      priority = 116,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.VICTORY-EFFECTS.NAME"
   )
   public static String COSMETICS_MENU_VICTORY_EFFECTS_NAME;
   @ConfigValue(
      priority = 117,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.VICTORY-EFFECTS.BEACON"
   )
   public static String COSMETICS_MENU_VICTORY_EFFECTS_MATERIAL;
   @ConfigValue(
      priority = 118,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.VICTORY-EFFECTS.DURABILITY"
   )
   public static int COSMETICS_MENU_VICTORY_EFFECTS_DURABILITY;
   @ConfigValue(
      priority = 119,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.VICTORY-EFFECTS.SLOT"
   )
   public static int COSMETICS_MENU_VICTORY_EFFECTS_SLOT;
   @ConfigValue(
      priority = 120,
      path = "GENERAL-MENUS.COSMETICS-MENU.BUTTONS.VICTORY-EFFECTS.LORE"
   )
   public static List<String> COSMETICS_MENU_VICTORY_EFFECTS_LORE;
   @ConfigValue(
      priority = 121,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.TITLE"
   )
   public static String KILL_MESSAGES_TITLE;
   @ConfigValue(
      priority = 123,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.SELECT-BUTTON.NAME"
   )
   public static String KILL_MESSAGES_NAME;
   @ConfigValue(
      priority = 124,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.SELECT-BUTTON.SELECTED"
   )
   public static String KILL_MESSAGES_MENU_SELECTED;
   @ConfigValue(
      priority = 124,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.SELECT-BUTTON.FORMAT"
   )
   public static String KILL_MESSAGES_MENU_FORMAT;
   @ConfigValue(
      priority = 125,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.SELECT-BUTTON.LORE"
   )
   public static List<String> KILL_MESSAGES_MENU_LORE;
   @ConfigValue(
      priority = 126,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.SELECT-BUTTON.NO-PERMISSION-LORE"
   )
   public static List<String> KILL_MESSAGES_MENU_NO_PERM_LORE;
   @ConfigValue(
      priority = 127,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.RESET-BUTTON.NAME"
   )
   public static String KILL_MESSAGES_MENU_RESET_NAME;
   @ConfigValue(
      priority = 127,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.RESET-BUTTON.MATERIAL"
   )
   public static String KILL_MESSAGES_MENU_RESET_MATERIAL;
   @ConfigValue(
      priority = 128,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.RESET-BUTTON.SLOT"
   )
   public static int KILL_MESSAGES_MENU_RESET_SLOT;
   @ConfigValue(
      priority = 129,
      path = "GENERAL-MENUS.KILL-MESSAGES-MENU.RESET-BUTTON.LORE"
   )
   public static List<String> KILL_MESSAGES_MENU_RESET_LORE;
   @ConfigValue(
      priority = 130,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.TITLE"
   )
   public static String KILL_EFFECTS_TITLE;
   @ConfigValue(
      priority = 132,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.SELECT-BUTTON.NAME"
   )
   public static String KILL_EFFECTS_NAME;
   @ConfigValue(
      priority = 133,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.SELECT-BUTTON.SELECTED"
   )
   public static String KILL_EFFECTS_MENU_SELECTED;
   @ConfigValue(
      priority = 134,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.SELECT-BUTTON.LORE"
   )
   public static List<String> KILL_EFFECTS_MENU_LORE;
   @ConfigValue(
      priority = 135,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.SELECT-BUTTON.NO-PERMISSION-LORE"
   )
   public static List<String> KILL_EFFECTS_MENU_NO_PERM_LORE;
   @ConfigValue(
      priority = 136,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.RESET-BUTTON.NAME"
   )
   public static String KILL_EFFECTS_MENU_RESET_NAME;
   @ConfigValue(
      priority = 137,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.RESET-BUTTON.MATERIAL"
   )
   public static String KILL_EFFECTS_MENU_RESET_MATERIAL;
   @ConfigValue(
      priority = 138,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.RESET-BUTTON.SLOT"
   )
   public static int KILL_EFFECTS_MENU_RESET_SLOT;
   @ConfigValue(
      priority = 139,
      path = "GENERAL-MENUS.KILL-EFFECTS-MENU.RESET-BUTTON.LORE"
   )
   public static List<String> KILL_EFFECTS_MENU_RESET_LORE;
   @ConfigValue(
      priority = 140,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.TITLE"
   )
   public static String PROJECTILE_TRAILS_TITLE;
   @ConfigValue(
      priority = 142,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.SELECT-BUTTON.NAME"
   )
   public static String PROJECTILE_TRAILS_NAME;
   @ConfigValue(
      priority = 143,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.SELECT-BUTTON.SELECTED"
   )
   public static String PROJECTILE_TRAILS_MENU_SELECTED;
   @ConfigValue(
      priority = 144,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.SELECT-BUTTON.LORE"
   )
   public static List<String> PROJECTILE_TRAILS_MENU_LORE;
   @ConfigValue(
      priority = 145,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.SELECT-BUTTON.NO-PERMISSION-LORE"
   )
   public static List<String> PROJECTILE_TRAILS_MENU_NO_PERM_LORE;
   @ConfigValue(
      priority = 146,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.RESET-BUTTON.NAME"
   )
   public static String PROJECTILE_TRAILS_MENU_RESET_NAME;
   @ConfigValue(
      priority = 147,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.RESET-BUTTON.MATERIAL"
   )
   public static String PROJECTILE_TRAILS_MENU_RESET_MATERIAL;
   @ConfigValue(
      priority = 148,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.RESET-BUTTON.SLOT"
   )
   public static int PROJECTILE_TRAILS_MENU_RESET_SLOT;
   @ConfigValue(
      priority = 149,
      path = "GENERAL-MENUS.PROJECTILE-TRAILS-MENU.RESET-BUTTON.LORE"
   )
   public static List<String> PROJECTILE_TRAILS_MENU_RESET_LORE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.TITLE"
   )
   public static String SELECT_ARENA_TITLE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.SIZE"
   )
   public static int SELECT_ARENA_SIZE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.NAME"
   )
   public static String SELECT_ARENA_NAME;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.LORE"
   )
   public static List<String> SELECT_ARENA_LORE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.RANDOM-BUTTON.ENABLED"
   )
   public static boolean SELECT_ARENA_RANDOM;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.RANDOM-BUTTON.NAME"
   )
   public static String SELECT_ARENA_RANDOM_NAME;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.RANDOM-BUTTON.MATERIAL"
   )
   public static String SELECT_ARENA_RANDOM_MATERIAL;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.RANDOM-BUTTON.DURABILITY"
   )
   public static int SELECT_ARENA_RANDOM_DURABILITY;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.RANDOM-BUTTON.SLOT"
   )
   public static int SELECT_ARENA_RANDOM_SLOT;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.ARENA-SELECT.RANDOM-BUTTON.LORE"
   )
   public static List<String> SELECT_ARENA_RANDOM_LORE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.TITLE"
   )
   public static String SELECT_KIT_TITLE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.SIZE"
   )
   public static int SELECT_KIT_SIZE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.NAME"
   )
   public static String SELECT_KIT_NAME;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.LORE"
   )
   public static List<String> SELECT_KIT_LORE;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.RANDOM-BUTTON.ENABLED"
   )
   public static boolean SELECT_KIT_RANDOM;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.RANDOM-BUTTON.NAME"
   )
   public static String SELECT_KIT_RANDOM_NAME;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.RANDOM-BUTTON.MATERIAL"
   )
   public static String SELECT_KIT_RANDOM_MATERIAL;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.RANDOM-BUTTON.DURABILITY"
   )
   public static int SELECT_KIT_RANDOM_DURABILITY;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.RANDOM-BUTTON.SLOT"
   )
   public static int SELECT_KIT_RANDOM_SLOT;
   @ConfigValue(
      priority = 150,
      path = "SELECTION-MENUS.KIT-SELECT.RANDOM-BUTTON.LORE"
   )
   public static List<String> SELECT_KIT_RANDOM_LORE;

   public GeneralMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = GeneralMenus.class.getFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(ConfigValue.class)) {
            annotatedFields.add(field);
         }
      }

      return annotatedFields;
   }

   static {
      SETTINGS_MENU_SPECTATOR_MESSAGES_MATERIAL = XMaterial.WRITABLE_BOOK.name();
      SETTINGS_MENU_SPECTATOR_MESSAGES_DURABILITY = 0;
      SETTINGS_MENU_SPECTATOR_MESSAGES_SLOT = 15;
      SETTINGS_MENU_SPECTATOR_MESSAGES_ENABLED_LORE = Arrays.asList("&7Would you like to receive spectator", "&7join and leave messages?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
      SETTINGS_MENU_SPECTATOR_MESSAGES_DISABLED_LORE = Arrays.asList("&7Would you like to receive spectator", "&7join and leave messages?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
      SETTINGS_MENU_PLAY_AGAIN_TYPE_NAME = "&e&lPlay-Again Type";
      SETTINGS_MENU_PLAY_AGAIN_TYPE_MATERIAL = XMaterial.EMERALD.name();
      SETTINGS_MENU_PLAY_AGAIN_TYPE_DURABILITY = 0;
      SETTINGS_MENU_PLAY_AGAIN_TYPE_SLOT = 16;
      SETTINGS_MENU_PLAY_AGAIN_TYPE_ENABLED_LORE = Arrays.asList("&7Would you like play-again to", "&7rematch the opponent when possible?", "", "&a■ &fRematch-Possible", "&7■ &fQueue-Only", " ", "&fClick to disable.");
      SETTINGS_MENU_PLAY_AGAIN_TYPE_DISABLED_LORE = Arrays.asList("&7Would you like play-again to", "&7rematch the opponent when possible?", "", "&7■ &fRematch-Possible", "&a■ &fQueue-Only", " ", "&fClick to enable.");
      SETTINGS_MENU_PHOENIX_SETTINGS_NAME = "&cPractice Settings";
      SETTINGS_MENU_PHOENIX_SETTINGS_LORE = Arrays.asList("&fHere you can manage your practice", "&fsettings and preferences.", "", "&eClick to open.");
      SETTINGS_MENU_PHOENIX_SETTINGS_MATERIAL = XMaterial.GOLDEN_SWORD.name();
      SETTINGS_MENU_PHOENIX_SETTINGS_DURABILITY = 0;
      SETTINGS_MENU_PHOENIX_SETTINGS_SLOT = 17;
      PROFILE_MENU_TITLE = "&7Profile Menu (<player>)";
      PROFILE_MENU_SIZE = 27;
      PROFILE_MENU_STATISTICS_NAME = "&c&lStatistics";
      PROFILE_MENU_STATISTICS_ENABLED = true;
      PROFILE_MENU_STATISTICS_MATERIAL = "EMERALD";
      PROFILE_MENU_STATISTICS_DURABILITY = 0;
      PROFILE_MENU_STATISTICS_SLOT = 11;
      PROFILE_MENU_STATISTICS_LORE = Arrays.asList("&7View your profile's statistics", "&7or view top 10 leaderboards for each kit.", "", "&eClick to view statistics.");
      PROFILE_MENU_HISTORY_NAME = "&c&lMatch History";
      PROFILE_MENU_HISTORY_ENABLED = true;
      PROFILE_MENU_HISTORY_MATERIAL = "PAPER";
      PROFILE_MENU_HISTORY_DURABILITY = 0;
      PROFILE_MENU_HISTORY_SLOT = 12;
      PROFILE_MENU_HISTORY_LORE = Arrays.asList("&7View your profile's match history.", "&7You can view match stats and inventories", "&7from each of your match history.", "", "&eClick to view match history.");
      PROFILE_MENU_DIVISIONS_NAME = "&c&lDivisions";
      PROFILE_MENU_DIVISIONS_ENABLED = true;
      PROFILE_MENU_DIVISIONS_MATERIAL = "GOLD_SWORD";
      PROFILE_MENU_DIVISIONS_DURABILITY = 0;
      PROFILE_MENU_DIVISIONS_SLOT = 13;
      PROFILE_MENU_DIVISIONS_LORE = Arrays.asList("&7View your all divisions achievable.", "&7You can view how much ELO it will take you", "&7to rank up and what is your current division.", "", "&eClick to view divisions.");
      PROFILE_MENU_SETTINGS_NAME = "&c&lProfile Settings";
      PROFILE_MENU_SETTINGS_ENABLED = true;
      PROFILE_MENU_SETTINGS_MATERIAL = "ANVIL";
      PROFILE_MENU_SETTINGS_DURABILITY = 0;
      PROFILE_MENU_SETTINGS_SLOT = 14;
      PROFILE_MENU_SETTINGS_LORE = Arrays.asList("&7View your profile's settings.", "&7You can modify each and individual setting.", "", "&eClick to view settings.");
      PROFILE_MENU_COSMETICS_NAME = "&c&lProfile Cosmetics";
      PROFILE_MENU_COSMETICS_ENABLED = true;
      PROFILE_MENU_COSMETICS_MATERIAL = "FIREWORK";
      PROFILE_MENU_COSMETICS_DURABILITY = 0;
      PROFILE_MENU_COSMETICS_SLOT = 15;
      PROFILE_MENU_COSMETICS_LORE = Arrays.asList("&7View your profile's cosmetics.", "&7You can modify and select cosmetics.", "", "&eClick to view cosmetics.");
      COSMETICS_MENU_TITLE = "&7Cosmetics Type";
      COSMETICS_MENU_SIZE = 27;
      COSMETICS_MENU_KILL_MESSAGES_NAME = "&c&lKill Messages";
      COSMETICS_MENU_KILL_MESSAGES_MATERIAL = "CHEST";
      COSMETICS_MENU_KILL_MESSAGES_DURABILITY = 0;
      COSMETICS_MENU_KILL_MESSAGES_SLOT = 10;
      COSMETICS_MENU_KILL_MESSAGES_LORE = Arrays.asList("", "&7Spice up your kill messages with an interesting, funny", "&7and witty variety of kill messages to pick from.", "", "&7■ &fUnlocked: &c<unlocked> / <total>", "&7■ &fSelected: &c<selected>", "", "&eClick to browse kill messages");
      COSMETICS_MENU_KILL_EFFECTS_NAME = "&c&lKill Effects";
      COSMETICS_MENU_KILL_EFFECTS_MATERIAL = "BLAZE_ROD";
      COSMETICS_MENU_KILL_EFFECTS_DURABILITY = 0;
      COSMETICS_MENU_KILL_EFFECTS_SLOT = 12;
      COSMETICS_MENU_KILL_EFFECTS_LORE = Arrays.asList("", "&7Make your kills interesting with a fun Kill Animation.", "", "&7■ &fUnlocked: &c<unlocked> / <total>", "&7■ &fSelected: &c<selected>", "", "&eClick to browse kill effects");
      COSMETICS_MENU_PROJECTILE_TRAILS_NAME = "&c&lProjectile Trails";
      COSMETICS_MENU_PROJECTILE_TRAILS_MATERIAL = "FIREWORK_STAR";
      COSMETICS_MENU_PROJECTILE_TRAILS_DURABILITY = 0;
      COSMETICS_MENU_PROJECTILE_TRAILS_SLOT = 14;
      COSMETICS_MENU_PROJECTILE_TRAILS_LORE = Arrays.asList("", "&7Make your projectiles have an amazing trail follow them", "&7as they move through their trajectory.", "", "&7■ &fUnlocked: &c<unlocked> / <total>", "&7■ &fSelected: &c<selected>", "", "&eClick to browse projectile trails");
      COSMETICS_MENU_VICTORY_EFFECTS_NAME = "&c&lVictory Effects";
      COSMETICS_MENU_VICTORY_EFFECTS_MATERIAL = "BEACON";
      COSMETICS_MENU_VICTORY_EFFECTS_DURABILITY = 0;
      COSMETICS_MENU_VICTORY_EFFECTS_SLOT = 16;
      COSMETICS_MENU_VICTORY_EFFECTS_LORE = Arrays.asList("", "&7Play selected Victory Dance effect", "&7to celebrate your victory in style!", "", "&c&lComing Soon...");
      KILL_MESSAGES_TITLE = "&7Cosmetics » Kill Messages";
      KILL_MESSAGES_NAME = "&c&l<name><selected>";
      KILL_MESSAGES_MENU_SELECTED = " &f(Selected)";
      KILL_MESSAGES_MENU_FORMAT = " &c• &f<message>";
      KILL_MESSAGES_MENU_LORE = Arrays.asList("", "&c&lIncluded", "<messages>", "&7", "&eClick to select.");
      KILL_MESSAGES_MENU_NO_PERM_LORE = Arrays.asList("", "&c&lIncluded", "<messages>", "&7", "&cNo permission.");
      KILL_MESSAGES_MENU_RESET_NAME = "&c&lReset Kill Message";
      KILL_MESSAGES_MENU_RESET_MATERIAL = "PLAYER_HEAD";
      KILL_MESSAGES_MENU_RESET_SLOT = 49;
      KILL_MESSAGES_MENU_RESET_LORE = Arrays.asList("", "&7■ &fSelected: &c<selected>", "", "&eClick to reset.");
      KILL_EFFECTS_TITLE = "&7Cosmetics » Kill Effects";
      KILL_EFFECTS_NAME = "&c&l<name><selected>";
      KILL_EFFECTS_MENU_SELECTED = " &f(Selected)";
      KILL_EFFECTS_MENU_LORE = Arrays.asList("", "&eClick to select.");
      KILL_EFFECTS_MENU_NO_PERM_LORE = Arrays.asList("", "&cNo permission.");
      KILL_EFFECTS_MENU_RESET_NAME = "&c&lReset Kill Effect";
      KILL_EFFECTS_MENU_RESET_MATERIAL = "PLAYER_HEAD";
      KILL_EFFECTS_MENU_RESET_SLOT = 49;
      KILL_EFFECTS_MENU_RESET_LORE = Arrays.asList("", "&7■ &fSelected: &c<selected>", "", "&eClick to reset.");
      PROJECTILE_TRAILS_TITLE = "&7Cosmetics » Projectile Trails";
      PROJECTILE_TRAILS_NAME = "&c&l<name><selected>";
      PROJECTILE_TRAILS_MENU_SELECTED = " &f(Selected)";
      PROJECTILE_TRAILS_MENU_LORE = Arrays.asList("", "&eClick to select.");
      PROJECTILE_TRAILS_MENU_NO_PERM_LORE = Arrays.asList("", "&cNo permission.");
      PROJECTILE_TRAILS_MENU_RESET_NAME = "&c&lReset Projectile Trail";
      PROJECTILE_TRAILS_MENU_RESET_MATERIAL = "PLAYER_HEAD";
      PROJECTILE_TRAILS_MENU_RESET_SLOT = 49;
      PROJECTILE_TRAILS_MENU_RESET_LORE = Arrays.asList("", "&7■ &fSelected: &c<selected>", "", "&eClick to reset.");
      SELECT_ARENA_TITLE = "&7Select an Arena";
      SELECT_ARENA_SIZE = -1;
      SELECT_ARENA_NAME = "<arena_display_name>";
      SELECT_ARENA_LORE = Arrays.asList("", "&eClick to select.");
      SELECT_ARENA_RANDOM = true;
      SELECT_ARENA_RANDOM_NAME = "&eRandom Arena";
      SELECT_ARENA_RANDOM_MATERIAL = XMaterial.ENDER_EYE.name();
      SELECT_ARENA_RANDOM_DURABILITY = 0;
      SELECT_ARENA_RANDOM_SLOT = 49;
      SELECT_ARENA_RANDOM_LORE = Arrays.asList("", "&eClick to select a Random Arena.");
      SELECT_KIT_TITLE = "&7Select an Kit";
      SELECT_KIT_SIZE = -1;
      SELECT_KIT_NAME = "<kit_display_name>";
      SELECT_KIT_LORE = Arrays.asList("", "&eClick to select.");
      SELECT_KIT_RANDOM = true;
      SELECT_KIT_RANDOM_NAME = "&eRandom Kit";
      SELECT_KIT_RANDOM_MATERIAL = XMaterial.ENDER_EYE.name();
      SELECT_KIT_RANDOM_DURABILITY = 0;
      SELECT_KIT_RANDOM_SLOT = 49;
      SELECT_KIT_RANDOM_LORE = Arrays.asList("", "&eClick to select a Random Kit.");
   }
}
