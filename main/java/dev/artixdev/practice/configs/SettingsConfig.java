package dev.artixdev.practice.configs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.BasicYamlStorage;
import dev.artixdev.api.practice.command.util.CC;
import dev.artixdev.practice.Main;

public class SettingsConfig extends BasicYamlStorage {
   private static final Logger log = LogManager.getLogger(SettingsConfig.class);
   public static boolean DEBUG_MODE;
   @ConfigValue(
      priority = 0,
      path = "SYSTEM.CONFIG-VERSION"
   )
   public static int CONFIG_VER = 1;
   @ConfigValue(
      priority = 1,
      path = "GENERAL.SPAWN-LOCATION",
      comment = "This is where players will initially spawn"
   )
   public static String SPAWN_LOCATION = "";
   @ConfigValue(
      priority = 2,
      path = "GENERAL.FFA-SPAWN-RADIUS",
      comment = "The radius of the spawning circle in FFA matches/events"
   )
   public static int FFA_SPAWN_RADIUS = 10;
   @ConfigValue(
      priority = 3,
      path = "GENERAL.DEFAULT-PARTY-LIMIT",
      comment = "You can set the default party members size limit here"
   )
   public static int PARTY_DEFAULT_LIMIT = 15;
   @ConfigValue(
      priority = 4,
      path = "GENERAL.ARENA-WORLD",
      comment = "This world is where arenas all arenas will be pasted according to grid"
   )
   public static String ARENAS_WORLD = "world";
   @ConfigValue(
      priority = 5,
      path = "GENERAL.ARENA-GRID-SPACING-X",
      comment = "Do NOT touch this, unless you know what you are doing!"
   )
   public static int ARENA_GRID_SPACING_X = 1000;
   @ConfigValue(
      priority = 5,
      path = "GENERAL.ARENA-GRID-SPACING-Z",
      comment = "Do NOT touch this, unless you know what you are doing!"
   )
   public static int ARENA_GRID_SPACING_Z = 1000;
   @ConfigValue(
      priority = 5,
      path = "GENERAL.ARENA-GRID-Y-LEVEL",
      comment = "Do NOT touch this, unless you know what you are doing!"
   )
   public static int ARENA_GRID_Y_LEVEL = 80;
   @ConfigValue(
      priority = 5,
      path = "GENERAL.STARTING-ELO",
      comment = "Only change this if you are going to reset stats or don't have any."
   )
   public static int STARTING_ELO = 1000;
   @ConfigValue(
      priority = 6,
      path = "GENERAL.PREVENT-FLY-OUT-OF-BOUNDS",
      comment = "Prevents donators from flying too far out from spawn or spectators from arena."
   )
   public static boolean PREVENT_FLYING_OUT = true;
   @ConfigValue(
      priority = 7,
      path = "GENERAL.PRE-LOAD-CHUNKS",
      comment = "Toggle whether you wants chunks to be pre-loaded on startup or not"
   )
   public static boolean CACHE_CHUNKS = false;
   @ConfigValue(
      priority = 7,
      path = "GENERAL.CLEAR-LEADERBOARDS-BEFORE-UPDATE",
      comment = "Should we clear the leaderboards before updating them?\nDisable this if your mongo is hosted externally and not locally."
   )
   public static boolean CLEAR_LEADERBOARDS_BEFORE_UPDATE = false;
   @ConfigValue(
      priority = 8,
      path = "GENERAL.CHECK-MOJANG-FOR-LEADERBOARD-NAMES",
      comment = "Turn this off if you have a cracked server, because its useless."
   )
   public static boolean CHECK_MOJANG_FOR_LEADERBOARD_NAMES = false;
   @ConfigValue(
      priority = 8,
      path = "GENERAL.SHIFT-CLICK-DUEL",
      comment = "If enabled, players in lobby can shift-right click a player to duel them."
   )
   public static boolean SHIFT_CLICK_DUEL = true;
   @ConfigValue(
      priority = 9,
      path = "GENERAL.HIDE-OTHER-PLAYERS",
      comment = "Should we hide players that aren't in your party?"
   )
   public static boolean HIDE_OTHER_PLAYERS_PARTY = false;
   @ConfigValue(
      priority = 10,
      path = "GENERAL.HELP-COMMAND",
      comment = "Should we enable help command?"
   )
   public static boolean HELP_COMMAND = true;
   @ConfigValue(
      priority = 10,
      path = "GENERAL.DISABLE-RESET-COMMAND",
      comment = "Should we disable the '/bolt reset' command that wipes mongo?"
   )
   public static boolean DISABLE_RESET_COMMAND = true;
   @ConfigValue(
      priority = 10,
      path = "GENERAL.DUEL-REQUEST-SOUND",
      comment = "Here you can change the duel request sound, the default is NOTE_PLING"
   )
   public static String DUEL_REQUEST_SOUND = "NOTE_PLING";
   @ConfigValue(
      priority = 10,
      path = "GENERAL.DUEL-CHANGES-STATS",
      comment = "Does profile/party duel affect stats?"
   )
   public static boolean DUEL_CHANGES_STATS = true;
   @ConfigValue(
      priority = 10,
      path = "GENERAL.FORCE-ENTITY-HIDER",
      comment = "Ignore the spigot's entity hider and use bolt's instead"
   )
   public static boolean FORCE_ENTITY_HIDER = false;
   @ConfigValue(
      priority = 11,
      path = "GENERAL.STATS-SAVE-INTERVAL",
      comment = "The profile statistics saving time in minutes\nIncreasing this from 15 minutes could help performance but also result in data loss\nWhilst decreasing this time interval can help prevent data loss but also affect performance"
   )
   public static int STATS_SAVE_INTERVAL = 15;
   @ConfigValue(
      priority = 12,
      path = "GENERAL.LEADERBOARD-UPDATE-INTERVAL",
      comment = "The leaderboards updating time in minutes\nIncreasing this from 3 minutes could help performance but also result in slow leaderboards\nWhilst decreasing this time interval can do the opposite"
   )
   public static int LEADERBOARD_UPDATE_INTERVAL = 3;
   @ConfigValue(
      priority = 13,
      path = "GENERAL.TOURNAMENT-WINNER-COMMANDS",
      comment = "You can put winner commands here for tournament.\nThese commands are ran on every single winner.\nThe placeholders are <winner> and <kit> for winner and kit name respectively."
   )
   public static List<String> TOURNAMENT_WINNER_COMMANDS = Collections.singletonList("elo add <winner> <kit> 10");
   @ConfigValue(
      priority = 14,
      path = "GENERAL.BLOCKED-COMMANDS",
      comment = "Here you can put any commands with the '/' included and bolt will block it"
   )
   public static List<String> GENERAL_BLOCKED_COMMANDS = new ArrayList();
   @ConfigValue(
      priority = 13,
      path = "PLACEHOLDER-API.MATCH-EXPANSIONS",
      comment = "Toggle Match PlaceholderAPI expansion"
   )
   public static boolean MATCH_EXPANSIONS = true;
   @ConfigValue(
      priority = 14,
      path = "PLACEHOLDER-API.STATS-EXPANSIONS",
      comment = "Toggle Stats PlaceholderAPI expansion"
   )
   public static boolean STATS_EXPANSIONS = true;
   @ConfigValue(
      priority = 15,
      path = "PLACEHOLDER-API.LEADERBOARD-FORMAT",
      comment = "Here you can change the format of the leadeboards placeholder"
   )
   public static String LEADERBOARD_PLACEHOLDER_FORMAT = "&c#<pos> &f<name> &7- &c<value> &7- <division>";
   @ConfigValue(
      priority = 9,
      path = "QUEUE.TICKING-MESSAGE-ENABLED",
      comment = "Should we periodically send queue ticking messages to players in queue?"
   )
   public static boolean QUEUE_MESSAGE_ENABLED = true;
   @ConfigValue(
      priority = 9,
      path = "QUEUE.FIRST-ELO-UPDATE",
      comment = "How much elo will be added and removed when you start queuing for ranked."
   )
   public static int FIRST_ELO_UPDATE = 25;
   @ConfigValue(
      priority = 9,
      path = "QUEUE.FIRST-PING-UPDATE",
      comment = "How much ping will be added and removed when you start queuing for ranked."
   )
   public static int FIRST_PING_UPDATE = 35;
   @ConfigValue(
      priority = 9,
      path = "QUEUE.FIRST-ELO-UPDATE",
      comment = "This value is added to the elo range every time the ranges are ticked."
   )
   public static int ELO_UPDATE_VALUE = 3;
   @ConfigValue(
      priority = 9,
      path = "QUEUE.ELO-UPDATE-DELAY",
      comment = "Queues are ticked everyone 0.2s, So 0.2 * 5 = 1 second of delay."
   )
   public static int ELO_UPDATE_TIME = 5;
   @ConfigValue(
      priority = 9,
      path = "QUEUE.FIRST-ELO-UPDATE",
      comment = "This value is added to the ping range every time the ranges are ticked."
   )
   public static int PING_UPDATE_VALUE = 10;
   @ConfigValue(
      priority = 9,
      path = "QUEUE.PING-UPDATE-DELAY",
      comment = "Queues are ticked everyone 0.2s, So 0.2 * 5 = 2 second of delay."
   )
   public static int PING_UPDATE_TIME = 5;
   @ConfigValue(
      priority = 10,
      path = "MATCH.BLOCKED-COMMANDS",
      comment = "Here you can put any commands with the '/' included and bolt will block it"
   )
   public static List<String> MATCH_BLOCKED_COMMANDS = new ArrayList();
   @ConfigValue(
      priority = 11,
      path = "MATCH.WIN-COMMANDS",
      comment = "The commands prefixed by 'CONSOLE:' are ran by console when player wins the match and for player, it's 'PLAYER:'\nDo not put '/' in here, it's automatically done.\nExample: 'CONSOLE:elo add <winner> NoDebuff 10"
   )
   public static List<String> MATCH_WIN_COMMANDS = new ArrayList();
   @ConfigValue(
      priority = 11,
      path = "MATCH.LOSE-COMMANDS",
      comment = "The commands prefixed by 'CONSOLE:' are ran by console when player loses the match and for player, it's 'PLAYER:'\nDo not put '/' in here, it's automatically done.\nExample: 'CONSOLE:elo remove <loser> NoDebuff 10"
   )
   public static List<String> MATCH_LOSE_COMMANDS = new ArrayList();
   @ConfigValue(
      priority = 12,
      path = "MATCH.STARTED-SOUND",
      comment = "Here you can change the match started sound, the default is NOTE_BASS"
   )
   public static String MATCH_STARTED_SOUND = "NOTE_BASS";
   @ConfigValue(
      priority = 13,
      path = "MATCH.COUNTDOWN-SOUND",
      comment = "Here you can change the match countdown sound, the default is NOTE_PLING"
   )
   public static String MATCH_COUNTDOWN_SOUND = "NOTE_PLING";
   @ConfigValue(
      priority = 14,
      path = "MATCH.SHOW-STARTED-TITLE",
      comment = "Enable the Match Started Title\nTitle and Subtitle is configurable in locale.yml"
   )
   public static boolean MATCH_TITLE_STARTED_ENABLED = true;
   @ConfigValue(
      priority = 15,
      path = "MATCH.SHOW-COUNTDOWN-TITLE",
      comment = "Enable the Match Countdown Title\nTitle and Subtitle is configurable in lang.yml"
   )
   public static boolean MATCH_TITLE_COUNTDOWN_ENABLED = true;
   @ConfigValue(
      priority = 16,
      path = "MATCH.SHOW-WINNER-TITLE",
      comment = "Enable the Match Winner Title\nTitle and Subtitle is configurable in lang.yml"
   )
   public static boolean MATCH_TITLE_WINNER_ENABLED = true;
   @ConfigValue(
      priority = 16,
      path = "MATCH.SHOW-LOOSER-TITLE",
      comment = "Enable the Match Looser Title\nTitle and Subtitle is configurable in lang.yml"
   )
   public static boolean MATCH_TITLE_LOSER_ENABLED = true;
   @ConfigValue(
      priority = 17,
      path = "MATCH.SHOW-RESPAWN-TITLE",
      comment = "Enable the Match Respawn Title\nTitle and Subtitle is configurable in lang.yml"
   )
   public static boolean MATCH_TITLE_RESPAWN_ENABLED = true;
   @ConfigValue(
      priority = 17,
      path = "MATCH.SHOW-RESPAWN-COUNTDOWN-TITLE",
      comment = "Enable the Match Respawn Countdown Title\nTitle and Subtitle is configurable in lang.yml"
   )
   public static boolean MATCH_TITLE_RESPAWN_COUNTDOWN_ENABLED = true;
   @ConfigValue(
      priority = 17,
      path = "MATCH.SHOW-BED-DESTROYED-TITLE",
      comment = "Enable the Match Bed Destroyed Title\nTitle and Subtitle is configurable in lang.yml"
   )
   public static boolean MATCH_TITLE_BED_DESTROYED_ENABLED = true;
   @ConfigValue(
      priority = 18,
      path = "MATCH.REMOVE-BOTTLES",
      comment = "Remove the bottles from player's hands as soon his drink is empty"
   )
   public static boolean MATCH_REMOVE_BOTTLES = true;
   @ConfigValue(
      priority = 19,
      path = "MATCH.CLEAR-BLOCKS",
      comment = "Should we clear placed blocks after each round of Bridges?"
   )
   public static boolean MATCH_CLEAR_BLOCKS = false;
   @ConfigValue(
      priority = 19,
      path = "MATCH.EXPLOSION-DAMAGES-PLAYERS",
      comment = "Should we allow explosion to damage players?"
   )
   public static boolean MATCH_EXPLOSION_AFFECTS_PLAYERS = false;
   @ConfigValue(
      priority = 19,
      path = "MATCH.SPLEEF-TRAJECTORY",
      comment = "Should we allow spleef to break a max of 4 in its trajectory?"
   )
   public static boolean MATCH_SPLEEF_TRAJECTORY = true;
   @ConfigValue(
      priority = 19,
      path = "MATCH.SHOW-KILL-EFFECTS",
      comment = "Should we display kill effects in respawn matches like Bridges?"
   )
   public static boolean MATCH_SHOW_KILL_EFFECT = false;
   @ConfigValue(
      priority = 20,
      path = "MATCH.DISCLAIMER-ENABLED",
      comment = "Should we send per kit disclaimer message?"
   )
   public static boolean MATCH_DISCLAIMER_ENABLED = true;
   @ConfigValue(
      priority = 20,
      path = "MATCH.ALLOW-LEAVE",
      comment = "If enabled, players can do /leave in a match to leave the match.\nThis only works for unranked solo matches, any other will say invalid state."
   )
   public static boolean MATCH_ALLOW_LEAVE = true;
   @ConfigValue(
      priority = 21,
      path = "MATCH.RANKED-LIMIT",
      comment = "The minimum amount of won matches required by a player to join Ranked Queue\nSet this to 0, if you don't want any limit"
   )
   public static int MATCH_RANKED_LIMIT = 10;
   @ConfigValue(
      priority = 22,
      path = "MATCH.RANKED-MAX-PING",
      comment = "The maximum amount of ping a player can have while joining Ranked Queue\nSet this to 0, if you don't want any limit"
   )
   public static int MATCH_RANKED_PING = 300;
   @ConfigValue(
      priority = 23,
      path = "MATCH.DUEL-EXPIRE-SECONDS",
      comment = "The amount of time (in seconds) before the duel invite expires."
   )
   public static int MATCH_DUEL_EXPIRE = 60;
   @ConfigValue(
      priority = 10,
      path = "MATCH.PLAY-AGAIN-EXPIRE-SECONDS",
      comment = "The amount of time (in seconds) before the play-again expires."
   )
   public static int PLAY_AGAIN_EXPIRE = 40;
   @ConfigValue(
      priority = 24,
      path = "MATCH.TELEPORT-DELAY",
      comment = "The amount of delay (in seconds) before you get teleported back to spawn"
   )
   public static int MATCH_TELEPORT_DELAY = 4;
   @ConfigValue(
      priority = 24,
      path = "MATCH.SPAWN-PROTECTION",
      comment = "The amount of spawn protection (in seconds) when you respawn in BedFight"
   )
   public static int MATCH_SPAWN_PROTECTION = 3;
   @ConfigValue(
      priority = 24,
      path = "MATCH.BLOCK-REMOVE-TIMER",
      comment = "The amount of delay (in seconds) before BattleRush/PearlFight blocks disappear"
   )
   public static int BLOCK_REMOVE_TIMER = 10;
   @ConfigValue(
      priority = 25,
      path = "MATCH.TIME-LIMIT-ENABLED",
      comment = "This enables a timer on all standard matches, after the timer is up, the match is ended in a tie."
   )
   public static boolean MATCH_TIME_LIMIT_ENABLED = true;
   @ConfigValue(
      priority = 26,
      path = "MATCH.TIME-LIMIT-VALUE",
      comment = "Specify the time limit in minutes for matches"
   )
   public static int MATCH_TIME_LIMIT_VALUE = 20;
   @ConfigValue(
      priority = 27,
      path = "TIMER.PEARL.ENABLED"
   )
   public static boolean PEARL_COOLDOWN_TOGGLE = true;
   @ConfigValue(
      priority = 28,
      path = "TIMER.PEARL.VALUE"
   )
   public static int PEARL_COOLDOWN_VALUE = 15;
   @ConfigValue(
      priority = 28,
      path = "TIMER.BRIDGE-BOW.ENABLED"
   )
   public static boolean BOW_COOLDOWN_TOGGLE = true;
   @ConfigValue(
      priority = 29,
      path = "TIMER.BRIDGE-BOW.VALUE"
   )
   public static int BOW_COOLDOWN_VALUE = 5;
   @ConfigValue(
      priority = 28,
      path = "TIMER.FIRE-BALL.ENABLED"
   )
   public static boolean FIREBALL_COOLDOWN_TOGGLE = true;
   @ConfigValue(
      priority = 29,
      path = "TIMER.FIRE-BALL.VALUE"
   )
   public static int FIREBALL_COOLDOWN_VALUE = 2;
   @ConfigValue(
      priority = 30,
      path = "HOLOGRAMS.DEFAULT-LINES"
   )
   public static List<String> HOLOGRAM_DEFAULT_LINES = Arrays.asList("&c&lPractice Leaderboards", "&7", "&e◉ <type> &e◉", "&7", "<c>&c#1 &f<player1> &7- &c<value1> &7- <division1>", "<c>&c#2 &f<player2> &7- &c<value2> &7- <division2>", "<c>&c#3 &f<player3> &7- &c<value3> &7- <division3>", "<c>&c#4 &f<player4> &7- &c<value4> &7- <division4>", "<c>&c#5 &f<player5> &7- &c<value5> &7- <division5>", "<c>&c#6 &f<player6> &7- &c<value6> &7- <division6>", "<c>&c#7 &f<player7> &7- &c<value7> &7- <division7>", "<c>&c#8 &f<player8> &7- &c<value8> &7- <division8>", "<c>&c#9 &f<player9> &7- &c<value9> &7- <division9>", "<c>&c#10 &f<player10> &7- &c<value10> &7- <division10>", "&7", "&fNext update in &c<update>");
   @ConfigValue(
      priority = 30,
      path = "HOLOGRAMS.KIT-LINES"
   )
   public static List<String> HOLOGRAM_KIT_LINES = Arrays.asList("&c&lPractice Leaderboards", "&7", "&e◉ <kit> &e◉", "&7", "<c>&c#1 &f<player1> &7- &c<value1> &7- <division1>", "<c>&c#2 &f<player2> &7- &c<value2> &7- <division2>", "<c>&c#3 &f<player3> &7- &c<value3> &7- <division3>", "<c>&c#4 &f<player4> &7- &c<value4> &7- <division4>", "<c>&c#5 &f<player5> &7- &c<value5> &7- <division5>", "<c>&c#6 &f<player6> &7- &c<value6> &7- <division6>", "<c>&c#7 &f<player7> &7- &c<value7> &7- <division7>", "<c>&c#8 &f<player8> &7- &c<value8> &7- <division8>", "<c>&c#9 &f<player9> &7- &c<value9> &7- <division9>", "<c>&c#10 &f<player10> &7- &c<value10> &7- <division10>", "&7", "&fNext update in &c<update>");
   @ConfigValue(
      priority = 90,
      path = "NAME-TAGS.ENABLED",
      comment = "Toggle whether Bolt's NameTag system should be enabled or not\nPlease note that this won't disable match or event name-tags, only lobby & queue"
   )
   public static boolean NAME_TAGS_ENABLED = true;
   @ConfigValue(
      priority = 91,
      path = "NAME-TAGS.LOBBY-PREFIX",
      comment = "You can change lobby name-tag here"
   )
   public static String LOBBY_PREFIX = "&a";
   @ConfigValue(
      priority = 92,
      path = "NAME-TAGS.LOBBY-SUFFIX",
      comment = ""
   )
   public static String LOBBY_SUFFIX = "";
   @ConfigValue(
      priority = 93,
      path = "NAME-TAGS.PARTY-PREFIX",
      comment = "You can change party name-tag prefix here"
   )
   public static String PARTY_PREFIX = "&9";
   @ConfigValue(
      priority = 94,
      path = "NAME-TAGS.PARTY-SUFFIX",
      comment = ""
   )
   public static String PARTY_SUFFIX = "";
   @ConfigValue(
      priority = 95,
      path = "NAME-TAGS.QUEUE-PREFIX",
      comment = "You can change queue name-tag prefix here"
   )
   public static String QUEUE_PREFIX = "&e";
   @ConfigValue(
      priority = 96,
      path = "NAME-TAGS.QUEUE-SUFFIX",
      comment = ""
   )
   public static String QUEUE_SUFFIX = "";
   @ConfigValue(
      priority = 97,
      path = "NAME-TAGS.EVENT-PREFIX",
      comment = "You can change event name-tag prefix here"
   )
   public static String EVENT_PREFIX = "&b";
   @ConfigValue(
      priority = 98,
      path = "NAME-TAGS.EVENT-SUFFIX",
      comment = ""
   )
   public static String EVENT_SUFFIX = "";
   @ConfigValue(
      priority = 99,
      path = "NAME-TAGS.BLUE-TEAM-PREFIX",
      comment = "You can change blue team name-tag prefix here"
   )
   public static String BLUE_TEAM_PREFIX = "&9[B] &9";
   @ConfigValue(
      priority = 100,
      path = "NAME-TAGS.RED-TEAM-PREFIX",
      comment = "You can change red team name-tag prefix here"
   )
   public static String RED_TEAM_PREFIX = "&c[R] &c";
   @ConfigValue(
      priority = 101,
      path = "LOGGING.STATS-LOGS"
   )
   public static boolean ALLOW_STATS_LOGS = true;

   public SettingsConfig(JavaPlugin plugin, String name) {
      super(plugin, name, false);
   }

   public void addSeparateComments() {
      this.addComment("SYSTEM", "DO NOT TOUCH!");
      this.addCommentWithBlankLine("LOGGING", "Should we log stats saving in console?");
      this.addCommentWithBlankLine("TIMER", "Configure whether there should be a cool-down for pearls and bows, and what should it be in seconds");
      this.addCommentWithBlankLine("HOLOGRAMS", "Here you can configure the format of the Holograms");
      this.addCommentWithBlankLine("PLACEHOLDER-API", "Configure PlaceholderAPI hook settings here");
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = SettingsConfig.class.getDeclaredFields();
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
      return new String[]{"This is the main configuration file for Bolt.", "As you can see, there's tons to configure. Some options may impact gameplay, so use", "with caution, and make sure you know what each option does before configuring.", "If you need help with the configuration or have any questions related to Bolt, join us in our Discord", null};
   }

   public static void sendDebugMessage(String message) {
      if (DEBUG_MODE) {
         Runnable wrapper = () -> {
            Player player = Bukkit.getPlayer("NotDrizzy");
            if (player != null) {
               player.sendMessage(CC.translate("&e&l[DEBUG] &f" + message));
            }
         };
         if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(Main.getInstance(), wrapper);
         } else {
            wrapper.run();
         }

      }
   }

   public int getMaxNameTagLength() {
      return 16; // Default max length
   }

   public String getDefaultNameTagPrefix() {
      return "&7"; // Default prefix
   }
   
   public List<String> getNameTagPrefixes() {
      return Arrays.asList(
         "&a[VIP]",
         "&b[MVP]", 
         "&6[ELITE]",
         "&c[MASTER]",
         "&d[LEGEND]",
         "&e[CHAMPION]",
         "&f[PRO]",
         "&9[EXPERT]",
         "&5[ADMIN]",
         "&c[OWNER]"
      );
   }
   
   public String getEventPrefix() {
      return "&6[EVENT]";
   }
   
   public String getEventSuffix() {
      return "&6";
   }

   public void save() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
   }

   public long getMaxMatchDuration() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getMaxMatchDuration'");
   }
}
