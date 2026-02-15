package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class PartyMenus extends ChildYamlStorage {
   @ConfigValue(
      priority = 14,
      path = "PARTY-MENUS.GAME-MENU.TITLE"
   )
   public static String PARTY_GAMES_TITLE = "Party » Games";
   @ConfigValue(
      priority = 14,
      path = "PARTY-MENUS.GAME-MENU.SIZE"
   )
   public static int PARTY_GAMES_SIZE = 45;
   @ConfigValue(
      priority = 15,
      path = "PARTY-MENUS.GAME-MENU.PARTY-SPLIT.NAME"
   )
   public static String PARTY_SPLIT_NAME = "&c&lParty Split";
   @ConfigValue(
      priority = 16,
      path = "PARTY-MENUS.GAME-MENU.PARTY-SPLIT.MATERIAL"
   )
   public static String PARTY_SPLIT_MATERIAL = "DIAMOND_SWORD";
   @ConfigValue(
      priority = 17,
      path = "PARTY-MENUS.GAME-MENU.PARTY-SPLIT.DURABILITY"
   )
   public static int PARTY_SPLIT_DURABILITY = 0;
   @ConfigValue(
      priority = 17,
      path = "PARTY-MENUS.GAME-MENU.PARTY-SPLIT.SLOT"
   )
   public static int PARTY_SPLIT_SLOT = 11;
   @ConfigValue(
      priority = 18,
      path = "PARTY-MENUS.GAME-MENU.PARTY-SPLIT.LORE"
   )
   public static List<String> PARTY_SPLIT_LORE = Arrays.asList("", "&7Split your party into teams", "&7and battle it out in any gamemode!", "", "&cClick to play!");
   @ConfigValue(
      priority = 19,
      path = "PARTY-MENUS.GAME-MENU.PARTY-FFA.NAME"
   )
   public static String PARTY_FFA_NAME = "&c&lParty FFA";
   @ConfigValue(
      priority = 20,
      path = "PARTY-MENUS.GAME-MENU.PARTY-FFA.MATERIAL"
   )
   public static String PARTY_FFA_MATERIAL = "GOLD_AXE";
   @ConfigValue(
      priority = 21,
      path = "PARTY-MENUS.GAME-MENU.PARTY-FFA.DURABILITY"
   )
   public static int PARTY_FFA_DURABILITY = 0;
   @ConfigValue(
      priority = 21,
      path = "PARTY-MENUS.GAME-MENU.PARTY-FFA.SLOT"
   )
   public static int PARTY_FFA_SLOT = 13;
   @ConfigValue(
      priority = 22,
      path = "PARTY-MENUS.GAME-MENU.PARTY-FFA.LORE"
   )
   public static List<String> PARTY_FFA_LORE = Arrays.asList("", "&7Start a free-for-all match", "&7with players in your party!", "", "&cClick to play!");
   @ConfigValue(
      priority = 23,
      path = "PARTY-MENUS.GAME-MENU.PARTY-TEAM-FIGHT.NAME"
   )
   public static String PARTY_TEAM_FIGHT_NAME = "&c&lParty TeamFight";
   @ConfigValue(
      priority = 24,
      path = "PARTY-MENUS.GAME-MENU.PARTY-TEAM-FIGHT.MATERIAL"
   )
   public static String PARTY_TEAM_FIGHT_MATERIAL = "BLAZE_POWDER";
   @ConfigValue(
      priority = 25,
      path = "PARTY-MENUS.GAME-MENU.PARTY-TEAM-FIGHT.DURABILITY"
   )
   public static int PARTY_TEAM_FIGHT_DURABILITY = 0;
   @ConfigValue(
      priority = 25,
      path = "PARTY-MENUS.GAME-MENU.PARTY-TEAM-FIGHT.SLOT"
   )
   public static int PARTY_TEAM_FIGHT_SLOT = 15;
   @ConfigValue(
      priority = 26,
      path = "PARTY-MENUS.GAME-MENU.PARTY-TEAM-FIGHT.LORE"
   )
   public static List<String> PARTY_TEAM_FIGHT_LORE = Arrays.asList("", "&7Fight it out in a split match with HCF", "&7pvp classes, the last team standing wins!", "", "&cClick to play!");
   @ConfigValue(
      priority = 27,
      path = "PARTY-MENUS.GAME-MENU.PARTY-DUEL.NAME"
   )
   public static String PARTY_DUEL_NAME = "&c&lDuel Party";
   @ConfigValue(
      priority = 28,
      path = "PARTY-MENUS.GAME-MENU.PARTY-DUEL.MATERIAL"
   )
   public static String PARTY_DUEL_MATERIAL = "COMPASS";
   @ConfigValue(
      priority = 29,
      path = "PARTY-MENUS.GAME-MENU.PARTY-DUEL.DURABILITY"
   )
   public static int PARTY_DUEL_DURABILITY = 0;
   @ConfigValue(
      priority = 29,
      path = "PARTY-MENUS.GAME-MENU.PARTY-DUEL.SLOT"
   )
   public static int PARTY_DUEL_SLOT = 31;
   @ConfigValue(
      priority = 30,
      path = "PARTY-MENUS.GAME-MENU.PARTY-DUEL.LORE"
   )
   public static List<String> PARTY_DUEL_LORE = Arrays.asList("", "&7Fight it out in a duel with another party", "&7until there is a last team standing.", "", "&cClick to play!");
   @ConfigValue(
      priority = 31,
      path = "PARTY-MENUS.OTHER-PARTIES.TITLE"
   )
   public static String OTHER_PARTIES_TITLE = "Party » Games » Duel";
   @ConfigValue(
      priority = 32,
      path = "PARTY-MENUS.OTHER-PARTIES.SIZE"
   )
   public static int OTHER_PARTIES_SIZE = 45;
   @ConfigValue(
      priority = 33,
      path = "PARTY-MENUS.OTHER-PARTIES.PARTY-BUTTON.NAME"
   )
   public static String OTHER_PARTIES_NAME = "&c<leader>'s &fParty";
   @ConfigValue(
      priority = 34,
      path = "PARTY-MENUS.OTHER-PARTIES.PARTY-BUTTON.LORE"
   )
   public static List<String> OTHER_PARTIES_LORE = Arrays.asList("", "&fLeader: &c<leader>", "&fSize: &c<size>", "", "&cMember List", "<list>", "<others>", "", "<duel_message>");
   @ConfigValue(
      priority = 35,
      path = "PARTY-MENUS.OTHER-PARTIES.PARTY-BUTTON.DUEL-MESSAGE"
   )
   public static String OTHER_PARTIES_DUEL_MESSAGE = "&aClick to duel!";
   @ConfigValue(
      priority = 35,
      path = "PARTY-MENUS.OTHER-PARTIES.PARTY-BUTTON.BUSY-MESSAGE"
   )
   public static String OTHER_PARTIES_BUSY_MESSAGE = "&cNot available!";
   @ConfigValue(
      priority = 35,
      path = "PARTY-MENUS.OTHER-PARTIES.PARTY-BUTTON.LIST-FORMAT"
   )
   public static String OTHER_PARTIES_PLAYER_LIST_FORMAT = "&7■ &c<member>";
   @ConfigValue(
      priority = 35,
      path = "PARTY-MENUS.OTHER-PARTIES.PARTY-BUTTON.OTHER-PLAYERS"
   )
   public static String OTHER_PARTIES_OTHER_PLAYERS = "&7&oand <count> others...";
   @ConfigValue(
      priority = 36,
      path = "PARTY-MENUS.OTHER-PARTIES.NO-PARTIES.NAME"
   )
   public static String OTHER_PARTIES_NO_PARTIES_FOUND_NAME = "&c&lNo Parties Found";
   @ConfigValue(
      priority = 37,
      path = "PARTY-MENUS.OTHER-PARTIES.NO-PARTIES.MATERIAL"
   )
   public static String OTHER_PARTIES_NO_PARTIES_FOUND_MATERIAL = "REDSTONE_BLOCK";
   @ConfigValue(
      priority = 38,
      path = "PARTY-MENUS.OTHER-PARTIES.NO-PARTIES.DURABILITY"
   )
   public static int OTHER_PARTIES_NO_PARTIES_FOUND_DURABILITY = 0;
   @ConfigValue(
      priority = 38,
      path = "PARTY-MENUS.OTHER-PARTIES.NO-PARTIES.SLOT"
   )
   public static int OTHER_PARTIES_NO_PARTIES_FOUND_SLOT = 4;
   @ConfigValue(
      priority = 39,
      path = "PARTY-MENUS.OTHER-PARTIES.NO-PARTIES.LORE"
   )
   public static List<String> OTHER_PARTIES_NO_PARTIES_FOUND_LORE = Arrays.asList("", "&7No parties are currently available.", "&7When someone creates one it will appear here.");
   @ConfigValue(
      priority = 40,
      path = "PARTY-MENUS.SETTINGS-MENU.TITLE"
   )
   public static String PARTY_SETTINGS_MENU_TITLE = "&7Party Settings";
   @ConfigValue(
      priority = 41,
      path = "PARTY-MENUS.SETTINGS-MENU.DUEL-REQUESTS.NAME"
   )
   public static String PARTY_SETTINGS_MENU_DUEL_REQUESTS_NAME = "&e&lDuel Requests";
   @ConfigValue(
      priority = 42,
      path = "PARTY-MENUS.SETTINGS-MENU.DUEL-REQUESTS.MATERIAL"
   )
   public static String PARTY_SETTINGS_MENU_DUEL_REQUESTS_MATERIAL = "DIAMOND_SWORD";
   @ConfigValue(
      priority = 43,
      path = "PARTY-MENUS.SETTINGS-MENU.DUEL-REQUESTS.DURABILITY"
   )
   public static int PARTY_SETTINGS_MENU_DUEL_REQUESTS_DURABILITY = 0;
   @ConfigValue(
      priority = 44,
      path = "PARTY-MENUS.SETTINGS-MENU.DUEL-REQUESTS.SLOT"
   )
   public static int PARTY_SETTINGS_MENU_DUEL_REQUESTS_SLOT = 10;
   @ConfigValue(
      priority = 45,
      path = "PARTY-MENUS.SETTINGS-MENU.DUEL-REQUESTS.ENABLED-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_DUEL_REQUESTS_ENABLED_LORE = Arrays.asList("&7Would you like to receive duel", "&7from other parties?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to disable.");
   @ConfigValue(
      priority = 46,
      path = "PARTY-MENUS.SETTINGS-MENU.DUEL-REQUESTS.DISABLED-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_DUEL_REQUESTS_DISABLED_LORE = Arrays.asList("&7Would you like to receive duel", "&7from other parties?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to enable.");
   @ConfigValue(
      priority = 47,
      path = "PARTY-MENUS.SETTINGS-MENU.PING-FACTOR.NAME"
   )
   public static String PARTY_SETTINGS_MENU_PING_FACTOR_NAME = "&e&lPing Range";
   @ConfigValue(
      priority = 48,
      path = "PARTY-MENUS.SETTINGS-MENU.PING-FACTOR.MATERIAL"
   )
   public static String PARTY_SETTINGS_MENU_PING_FACTOR_MATERIAL = "LEVER";
   @ConfigValue(
      priority = 49,
      path = "PARTY-MENUS.SETTINGS-MENU.PING-FACTOR.DURABILITY"
   )
   public static int PARTY_SETTINGS_MENU_PING_FACTOR_DURABILITY = 0;
   @ConfigValue(
      priority = 50,
      path = "PARTY-MENUS.SETTINGS-MENU.PING-FACTOR.SLOT"
   )
   public static int PARTY_SETTINGS_MENU_PING_FACTOR_SLOT = 11;
   @ConfigValue(
      priority = 51,
      path = "PARTY-MENUS.SETTINGS-MENU.PING-FACTOR.LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PING_FACTOR_ENABLED_LORE = Arrays.asList("&7Would you like to enable ping difference limit", "&7between the party members you queue with?", "", "&a■ &f<ping_fact_amount>", " ", "&fClick to change.");
   @ConfigValue(
      priority = 52,
      path = "PARTY-MENUS.SETTINGS-MENU.PING-FACTOR.NO-PERMISSION-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PING_FACTOR_NO_PERM_LORE = Arrays.asList("&7Would you like to enable ping difference limit", "&7between the players you queue with?", "", "&a■ &fUnrestricted", " ", "&cNo permission.");
   @ConfigValue(
      priority = 53,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-LIMIT.NAME"
   )
   public static String PARTY_SETTINGS_MENU_PARTY_LIMIT_NAME = "&e&lParty Limit";
   @ConfigValue(
      priority = 54,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-LIMIT.MATERIAL"
   )
   public static String PARTY_SETTINGS_MENU_PARTY_LIMIT_MATERIAL = "BOOK_AND_QUILL";
   @ConfigValue(
      priority = 55,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-LIMIT.DURABILITY"
   )
   public static int PARTY_SETTINGS_MENU_PARTY_LIMIT_DURABILITY = 0;
   @ConfigValue(
      priority = 56,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-LIMIT.SLOT"
   )
   public static int PARTY_SETTINGS_MENU_PARTY_LIMIT_SLOT = 12;
   @ConfigValue(
      priority = 57,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-LIMIT.LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PARTY_LIMIT_ENABLED_LORE = Arrays.asList("&7How many players would you like", "&7to be able to join your party?", "", "&a■ &f<party_limit_amount>", " ", "&fClick to change.");
   @ConfigValue(
      priority = 58,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-LIMIT.NO-PERMISSION-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PARTY_LIMIT_NO_PERM_LORE = Arrays.asList("&7How many players would you like", "&7to be able to join your party?", "", "&a■ &f<party_limit_amount>", " ", "&cNo permission.");
   @ConfigValue(
      priority = 59,
      path = "PARTY-MENUS.SETTINGS-MENU.MUTE-CHAT.NAME"
   )
   public static String PARTY_SETTINGS_MENU_MUTE_CHAT_NAME = "&e&lMute Chat";
   @ConfigValue(
      priority = 60,
      path = "PARTY-MENUS.SETTINGS-MENU.MUTE-CHAT.MATERIAL"
   )
   public static String PARTY_SETTINGS_MENU_MUTE_CHAT_MATERIAL = "NOTE_BLOCK";
   @ConfigValue(
      priority = 61,
      path = "PARTY-MENUS.SETTINGS-MENU.MUTE-CHAT.DURABILITY"
   )
   public static int PARTY_SETTINGS_MENU_MUTE_CHAT_DURABILITY = 0;
   @ConfigValue(
      priority = 62,
      path = "PARTY-MENUS.SETTINGS-MENU.MUTE-CHAT.SLOT"
   )
   public static int PARTY_SETTINGS_MENU_MUTE_CHAT_SLOT = 13;
   @ConfigValue(
      priority = 63,
      path = "PARTY-MENUS.SETTINGS-MENU.MUTE-CHAT.LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_MUTE_CHAT_ENABLED_LORE = Arrays.asList("&7Would you like party members to be", "&7able to talk in party chat?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to change.");
   @ConfigValue(
      priority = 64,
      path = "PARTY-MENUS.SETTINGS-MENU.MUTE-CHAT.DISABLED-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_MUTE_CHAT_DISABLED_LORE = Arrays.asList("&7Would you like party members to be", "&7able to talk in party chat?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to change.");
   @ConfigValue(
      priority = 65,
      path = "PARTY-MENUS.SETTINGS-MENU.ALL-INVITE.NAME"
   )
   public static String PARTY_SETTINGS_MENU_ALL_INVITE_NAME = "&e&lParty All-Invite";
   @ConfigValue(
      priority = 66,
      path = "PARTY-MENUS.SETTINGS-MENU.ALL-INVITE.MATERIAL"
   )
   public static String PARTY_SETTINGS_MENU_ALL_INVITE_MATERIAL = "NAME_TAG";
   @ConfigValue(
      priority = 67,
      path = "PARTY-MENUS.SETTINGS-MENU.ALL-INVITE.DURABILITY"
   )
   public static int PARTY_SETTINGS_MENU_ALL_INVITE_DURABILITY = 0;
   @ConfigValue(
      priority = 68,
      path = "PARTY-MENUS.SETTINGS-MENU.ALL-INVITE.SLOT"
   )
   public static int PARTY_SETTINGS_MENU_ALL_INVITE_SLOT = 14;
   @ConfigValue(
      priority = 69,
      path = "PARTY-MENUS.SETTINGS-MENU.ALL-INVITE.LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_ALL_INVITE_ENABLED_LORE = Arrays.asList("&7Would you like party members to be able", "&7to invite other players to your party?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to change.");
   @ConfigValue(
      priority = 70,
      path = "PARTY-MENUS.SETTINGS-MENU.ALL-INVITE.DISABLED-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_ALL_INVITE_DISABLED_LORE = Arrays.asList("&7Would you like party members to be able", "&7to invite other players to your party?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to change.");
   @ConfigValue(
      priority = 71,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-PUBLIC.NAME"
   )
   public static String PARTY_SETTINGS_MENU_PARTY_PUBLIC_NAME = "&e&lParty Public";
   @ConfigValue(
      priority = 72,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-PUBLIC.MATERIAL"
   )
   public static String PARTY_SETTINGS_MENU_PARTY_PUBLIC_MATERIAL = "NETHER_STAR";
   @ConfigValue(
      priority = 73,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-PUBLIC.DURABILITY"
   )
   public static int PARTY_SETTINGS_MENU_PARTY_PUBLIC_DURABILITY = 0;
   @ConfigValue(
      priority = 74,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-PUBLIC.SLOT"
   )
   public static int PARTY_SETTINGS_MENU_PARTY_PUBLIC_SLOT = 15;
   @ConfigValue(
      priority = 75,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-PUBLIC.LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PARTY_PUBLIC_ENABLED_LORE = Arrays.asList("&7Would you like players to be able to", "&7join your party uninvited?", "", "&a■ &fEnabled", "&7■ &fDisabled", " ", "&fClick to change.");
   @ConfigValue(
      priority = 76,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-PUBLIC.DISABLED-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PARTY_PUBLIC_DISABLED_LORE = Arrays.asList("&7Would you like players to be able to", "&7join your party uninvited?", "", "&7■ &fEnabled", "&a■ &fDisabled", " ", "&fClick to change.");
   @ConfigValue(
      priority = 77,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-PUBLIC.NO-PERMISSION-LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PARTY_PUBLIC_NO_PERM_LORE = Arrays.asList("&7Would you like players to be able to", "&7join your party uninvited?", "", "&7■ &fEnabled", "&7■ &fDisabled", " ", "&cNo permission.");
   @ConfigValue(
      priority = 77,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-MANAGE.NAME"
   )
   public static String PARTY_SETTINGS_MENU_PARTY_MANAGE_NAME = "&e&lParty Manage";
   @ConfigValue(
      priority = 78,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-MANAGE.MATERIAL"
   )
   public static String PARTY_SETTINGS_MENU_PARTY_MANAGE_MATERIAL = "PLAYER_HEAD";
   @ConfigValue(
      priority = 79,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-MANAGE.DURABILITY"
   )
   public static int PARTY_SETTINGS_MENU_PARTY_MANAGE_DURABILITY = 0;
   @ConfigValue(
      priority = 80,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-MANAGE.SLOT"
   )
   public static int PARTY_SETTINGS_MENU_PARTY_MANAGE_SLOT = 16;
   @ConfigValue(
      priority = 81,
      path = "PARTY-MENUS.SETTINGS-MENU.PARTY-MANAGE.LORE"
   )
   public static List<String> PARTY_SETTINGS_MENU_PARTY_MANAGE_LORE = Arrays.asList("&7Would you like to manage the", "&7current party members?", "", "&eClick to manage!");
   @ConfigValue(
      priority = 82,
      path = "PARTY-MENUS.PARTY-MANAGE.TITLE"
   )
   public static String PARTY_MANAGE_TITLE = "&e&lParty Manage";
   @ConfigValue(
      priority = 83,
      path = "PARTY-MENUS.PARTY-MANAGE.BUTTON.NAME"
   )
   public static String PARTY_MANAGE_NAME = "&c<player>";
   @ConfigValue(
      priority = 84,
      path = "PARTY-MENUS.PARTY-MANAGE.BUTTON.MATERIAL"
   )
   public static String PARTY_MANAGE_MATERIAL = "PLAYER_HEAD";
   @ConfigValue(
      priority = 85,
      path = "PARTY-MENUS.PARTY-MANAGE.BUTTON.DURABILITY"
   )
   public static int PARTY_MANAGE_DURABILITY = 0;
   @ConfigValue(
      priority = 86,
      path = "PARTY-MENUS.PARTY-MANAGE.BUTTON.LORE"
   )
   public static List<String> PARTY_MANAGE_LORE = Arrays.asList("", "&fLeft-Click to kick this player.", "&fRight click to promote to leader.");

   public PartyMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = PartyMenus.class.getFields();
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
