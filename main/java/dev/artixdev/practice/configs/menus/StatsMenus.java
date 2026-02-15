package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class StatsMenus extends ChildYamlStorage {
   @ConfigValue(
      priority = 47,
      path = "STATS-MENU.PLAYER-STATS.TITLE"
   )
   public static String STATS_MENU_TITLE = "<player_name>'s Statistics";
   @ConfigValue(
      priority = 47,
      path = "STATS-MENU.PLAYER-STATS.GLOBAL-NAME"
   )
   public static String STATS_MENU_GLOBAL_NAME = "&c&lGlobal Statistics";
   @ConfigValue(
      priority = 48,
      path = "STATS-MENU.PLAYER-STATS.GLOBAL-MATERIAL"
   )
   public static String STATS_MENU_GLOBAL_MATERIAL = "NETHER_STAR";
   @ConfigValue(
      priority = 49,
      path = "STATS-MENU.PLAYER-STATS.GLOBAL-DURABILITY"
   )
   public static int STATS_MENU_GLOBAL_DURABILITY = 0;
   @ConfigValue(
      priority = 50,
      path = "STATS-MENU.PLAYER-STATS.GLOBAL-LORE"
   )
   public static List<String> STATS_MENU_GLOBAL_LORE = Arrays.asList("", " &fELO: &c<globalElo>", " &fDivision: &c<division>", " &fWins: &c<wins>", " &fKills: &c<kills>", " &fLosses: &c<losses>", " &fDeaths: &c<deaths>", " &fW/L Ratio: &c<wlr>", " &fK/D Ratio: &c<kdr>", " &fTournament Wins: &c<tournament_wins>");
   @ConfigValue(
      priority = 51,
      path = "STATS-MENU.PLAYER-STATS.KIT-NAME"
   )
   public static String STATS_MENU_KIT_NAME = "&c&l<kit>";
   @ConfigValue(
      priority = 54,
      path = "STATS-MENU.PLAYER-STATS.KIT-RANKED-LORE"
   )
   public static List<String> STATS_MENU_RANKED_KIT_LORE = Arrays.asList("", "&c&lRanked", " &fELO: &c<elo> &8- &f<division>", " &fWins: &c<rankedWins>", " &fLosses: &c<rankedLosses>", " &fWinstreak: &c<rankedWinstreak> &7(<highestRankedWinstreak>)", " &fW/L Ratio: &c<rankedWLRatio>", "", "&c&lUnranked", " &fWins: &c<unrankedWins>", " &fLosses: &c<unrankedLosses>", " &fWinstreak: &c<unrankedWinstreak> &7(<highestUnrankedWinstreak>)", " &fW/L Ratio: &c<unrankedWLRatio>", " &fTournament Wins: &c<tournament_wins>");
   @ConfigValue(
      priority = 53,
      path = "STATS-MENU.PLAYER-STATS.KIT-UNRANKED-LORE"
   )
   public static List<String> STATS_MENU_UNRANKED_KIT_LORE = Arrays.asList("", "&c&lUnranked", " &fWins: &c<unrankedWins>", " &fLosses: &c<unrankedLosses>", " &fWinstreak: &c<unrankedWinstreak> &7(<highestUnrankedWinstreak>)", " &fW/L Ratio: &c<unrankedWLRatio>", " &fTournament Wins: &c<tournament_wins>");
   @ConfigValue(
      priority = 54,
      path = "STATS-MENU.STATS-BUTTON.NAME"
   )
   public static String STATS_BUTTON_NAME = "&a<player>'s Stats";
   @ConfigValue(
      priority = 55,
      path = "STATS-MENU.STATS-BUTTON.MATERIAL"
   )
   public static String STATS_BUTTON_MATERIAL = "PLAYER_HEAD";
   @ConfigValue(
      priority = 56,
      path = "STATS-MENU.STATS-BUTTON.DURABILITY"
   )
   public static int STATS_BUTTON_DURABILITY = 0;
   @ConfigValue(
      priority = 57,
      path = "STATS-MENU.STATS-BUTTON.LORE"
   )
   public static List<String> STATS_BUTTON_LORE = Arrays.asList("", "&7Click to view your stats.");
   @ConfigValue(
      priority = 58,
      path = "LEADERBOARDS-MENU.STATS-BUTTON.SLOT"
   )
   public static int STATS_BUTTON_SLOT = 49;
   @ConfigValue(
      priority = 52,
      path = "LEADERBOARDS-MENU.RANKED-TITLE"
   )
   public static String LEADERBOARD_TITLE = "Ranked Leaderboards";
   @ConfigValue(
      priority = 53,
      path = "LEADERBOARDS-MENU.WIN-STREAK-TITLE"
   )
   public static String WINSTREAK_TITLE = "Win-Streak Leaderboard";
   @ConfigValue(
      priority = 54,
      path = "LEADERBOARDS-MENU.KILLS-TITLE"
   )
   public static String KILLS_TITLE = "Kills Leaderboard";
   @ConfigValue(
      priority = 55,
      path = "LEADERBOARDS-MENU.WINS-TITLE"
   )
   public static String WINS_TITLE = "Wins Leaderboard";
   @ConfigValue(
      priority = 56,
      path = "LEADERBOARDS-MENU.GLOBAL-LEADERBOARD.NAME"
   )
   public static String GLOBAL_LEADERBOARD_NAME = "&c&lGlobal &7┃ &fTop 10";
   @ConfigValue(
      priority = 57,
      path = "LEADERBOARDS-MENU.GLOBAL-LEADERBOARD.MATERIAL"
   )
   public static String GLOBAL_LEADERBOARD_MATERIAL = "NETHER_STAR";
   @ConfigValue(
      priority = 57,
      path = "LEADERBOARDS-MENU.GLOBAL-LEADERBOARD.DURABILITY"
   )
   public static int GLOBAL_LEADERBOARD_DURABILITY = 0;
   @ConfigValue(
      priority = 58,
      path = "LEADERBOARDS-MENU.GLOBAL-LEADERBOARD.LORE"
   )
   public static List<String> GLOBAL_LEADERBOARD_LORE = Arrays.asList("&7Showing top 10 results.", "", "<c>&c#1 &f<player1> &7- &c<value1> &7- <division1>", "<c>&c#2 &f<player2> &7- &c<value2> &7- <division2>", "<c>&c#3 &f<player3> &7- &c<value3> &7- <division3>", "<c>&c#4 &f<player4> &7- &c<value4> &7- <division4>", "<c>&c#5 &f<player5> &7- &c<value5> &7- <division5>", "<c>&c#6 &f<player6> &7- &c<value6> &7- <division6>", "<c>&c#7 &f<player7> &7- &c<value7> &7- <division7>", "<c>&c#8 &f<player8> &7- &c<value8> &7- <division8>", "<c>&c#9 &f<player9> &7- &c<value9> &7- <division9>", "<c>&c#10 &f<player10> &7- &c<value10> &7- <division10>", "", "<bottom_notice>");
   @ConfigValue(
      priority = 59,
      path = "LEADERBOARDS-MENU.KIT-LEADERBOARD.NAME"
   )
   public static String KIT_LEADERBOARD_NAME = "&c&l<kit> &7┃ &fTop 10";
   @ConfigValue(
      priority = 60,
      path = "LEADERBOARDS-MENU.KIT-LEADERBOARD.LORE"
   )
   public static List<String> KIT_LEADERBOARD_LORE = Arrays.asList("&7Showing top 10 results.", "", "<c>&c#1 &f<player1> &7- &c<value1> &7- <division1>", "<c>&c#2 &f<player2> &7- &c<value2> &7- <division2>", "<c>&c#3 &f<player3> &7- &c<value3> &7- <division3>", "<c>&c#4 &f<player4> &7- &c<value4> &7- <division4>", "<c>&c#5 &f<player5> &7- &c<value5> &7- <division5>", "<c>&c#6 &f<player6> &7- &c<value6> &7- <division6>", "<c>&c#7 &f<player7> &7- &c<value7> &7- <division7>", "<c>&c#8 &f<player8> &7- &c<value8> &7- <division8>", "<c>&c#9 &f<player9> &7- &c<value9> &7- <division9>", "<c>&c#10 &f<player10> &7- &c<value10> &7- <division10>", "", "<bottom_notice>");
   @ConfigValue(
      priority = 61,
      path = "LEADERBOARDS-MENU.ELO-LORE-NOTICE"
   )
   public static String LEADERBOARDS_ELO_LORE_NOTICE = "&fYou have a total of &c<elo>&f elo.";
   @ConfigValue(
      priority = 62,
      path = "LEADERBOARDS-MENU.KILLS-LORE-NOTICE"
   )
   public static String LEADERBOARDS_KILLS_LORE_NOTICE = "&fYou have a total of &c<kills>&f kills&7.";
   @ConfigValue(
      priority = 62,
      path = "LEADERBOARDS-MENU.WIN-STREAK-LORE-NOTICE"
   )
   public static String LEADERBOARDS_WINSTREAK_LORE_NOTICE = "&fYou have a total of &c<winstreak>&f winstreak.\n&fYour highest winstreak was &c<highest_winstreak>&7.";
   @ConfigValue(
      priority = 62,
      path = "LEADERBOARDS-MENU.WIN-LORE-NOTICE"
   )
   public static String LEADERBOARDS_WIN_LORE_NOTICE = "&fYou have a total of &c<wins>&f wins&7.";
   @ConfigValue(
      priority = 63,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.SLOT"
   )
   public static int KILLS_LEADERBOARDS_SLOT = 53;
   @ConfigValue(
      priority = 64,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.SELECTED.NAME"
   )
   public static String KILLS_LEADERBOARDS_SELECTED_NAME = "&aKills Leaderboards";
   @ConfigValue(
      priority = 65,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.SELECTED.MATERIAL"
   )
   public static String KILL_LEADERBOARDS_SELECTED_MATERIAL = "LIME_DYE";
   @ConfigValue(
      priority = 66,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.SELECTED.DURABILITY"
   )
   public static int KILL_LEADERBOARDS_SELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 67,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.SELECTED.LORE"
   )
   public static List<String> KILLS_LEADERBOARDS_SELECTED_LORE = Arrays.asList("", "&7You're currently viewing kills leaderboards.");
   @ConfigValue(
      priority = 68,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.UNSELECTED.NAME"
   )
   public static String KILLS_LEADERBOARDS_UNSELECTED_NAME = "&cKills Leaderboards";
   @ConfigValue(
      priority = 69,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.UNSELECTED.MATERIAL"
   )
   public static String KILL_LEADERBOARDS_UNSELECTED_MATERIAL = "GRAY_DYE";
   @ConfigValue(
      priority = 70,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.UNSELECTED.DURABILITY"
   )
   public static int KILL_LEADERBOARDS_UNSELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 71,
      path = "LEADERBOARDS-MENU.KILLS-LEADERBOARDS.UNSELECTED.LORE"
   )
   public static List<String> KILLS_LEADERBOARDS_UNSELECTED_LORE = Arrays.asList("", "&7Click to view kills leaderboards.");
   @ConfigValue(
      priority = 72,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.SLOT"
   )
   public static int RANKED_LEADERBOARDS_SLOT = 45;
   @ConfigValue(
      priority = 73,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.SELECTED.NAME"
   )
   public static String RANKED_LEADERBOARDS_SELECTED = "&aRanked Leaderboards";
   @ConfigValue(
      priority = 74,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.SELECTED.MATERIAL"
   )
   public static String RANKED_LEADERBOARDS_SELECTED_MATERIAL = "LIME_DYE";
   @ConfigValue(
      priority = 75,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.SELECTED.DURABILITY"
   )
   public static int RANKED_LEADERBOARDS_SELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 76,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.SELECTED.LORE"
   )
   public static List<String> RANKED_LEADERBOARDS_SELECTED_LORE = Arrays.asList("", "&7You're currently viewing ranked leaderboards.");
   @ConfigValue(
      priority = 77,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.UNSELECTED.NAME"
   )
   public static String RANKED_LEADERBOARDS_UNSELECTED = "&cRanked Leaderboards";
   @ConfigValue(
      priority = 78,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.UNSELECTED.MATERIAL"
   )
   public static String RANKED_LEADERBOARDS_UNSELECTED_MATERIAL = "GRAY_DYE";
   @ConfigValue(
      priority = 79,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.UNSELECTED.DURABILITY"
   )
   public static int RANKED_LEADERBOARDS_UNSELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 80,
      path = "LEADERBOARDS-MENU.RANKED-LEADERBOARDS.UNSELECTED.LORE"
   )
   public static List<String> RANKED_LEADERBOARDS_UNSELECTED_LORE = Arrays.asList("", "&7Click to view ranked leaderboards.");
   @ConfigValue(
      priority = 81,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.SLOT"
   )
   public static int WINSTREAK_LEADERBOARDS_SLOT = 51;
   @ConfigValue(
      priority = 82,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.SELECTED.NAME"
   )
   public static String WINSTREAK_LEADERBOARD_SELECTED = "&aWin Streak Leaderboards";
   @ConfigValue(
      priority = 83,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.SELECTED.MATERIAL"
   )
   public static String WINSTREAK_LEADERBOARDS_SELECTED_MATERIAL = "LIME_DYE";
   @ConfigValue(
      priority = 84,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.SELECTED.DURABILITY"
   )
   public static int WINSTREAK_LEADERBOARDS_SELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 85,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.SELECTED.LORE"
   )
   public static List<String> WINSTREAK_LEADERBOARD_SELECTED_LORE = Arrays.asList("", "&7You're currently viewing win streak leaderboards.");
   @ConfigValue(
      priority = 86,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.UNSELECTED.NAME"
   )
   public static String WINSTREAK_LEADERBOARD_UNSELECTED = "&cWin Streak Leaderboards";
   @ConfigValue(
      priority = 87,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.UNSELECTED.MATERIAL"
   )
   public static String WINSTREAK_LEADERBOARDS_UNSELECTED_MATERIAL = "GRAY_DYE";
   @ConfigValue(
      priority = 88,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.UNSELECTED.DURABILITY"
   )
   public static int WINSTREAK_LEADERBOARDS_UNSELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 89,
      path = "LEADERBOARDS-MENU.WINSTREAK-LEADERBOARDS.UNSELECTED.LORE"
   )
   public static List<String> WINSTREAK_LEADERBOARD_UNSELECTED_LORE = Arrays.asList("", "&7Click to view win streak leaderboards.");
   @ConfigValue(
      priority = 90,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.SLOT"
   )
   public static int WINS_LEADERBOARDS_SLOT = 47;
   @ConfigValue(
      priority = 91,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.SELECTED.NAME"
   )
   public static String WINS_LEADERBOARD_SELECTED = "&aWins Leaderboards";
   @ConfigValue(
      priority = 92,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.SELECTED.MATERIAL"
   )
   public static String WINS_LEADERBOARDS_SELECTED_MATERIAL = "LIME_DYE";
   @ConfigValue(
      priority = 93,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.SELECTED.DURABILITY"
   )
   public static int WINS_LEADERBOARDS_SELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 94,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.SELECTED.LORE"
   )
   public static List<String> WINS_LEADERBOARD_SELECTED_LORE = Arrays.asList("", "&7You're currently viewing wins leaderboards.");
   @ConfigValue(
      priority = 95,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.UNSELECTED.NAME"
   )
   public static String WINS_LEADERBOARD_UNSELECTED = "&cWins Leaderboards";
   @ConfigValue(
      priority = 96,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.UNSELECTED.MATERIAL"
   )
   public static String WINS_LEADERBOARDS_UNSELECTED_MATERIAL = "GRAY_DYE";
   @ConfigValue(
      priority = 97,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.UNSELECTED.DURABILITY"
   )
   public static int WINS_LEADERBOARDS_UNSELECTED_DURABILITY = 0;
   @ConfigValue(
      priority = 98,
      path = "LEADERBOARDS-MENU.WINS-LEADERBOARDS.UNSELECTED.LORE"
   )
   public static List<String> WINS_LEADERBOARD_UNSELECTED_LORE = Arrays.asList("", "&7Click to view wins leaderboards.");

   public StatsMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = StatsMenus.class.getFields();
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
