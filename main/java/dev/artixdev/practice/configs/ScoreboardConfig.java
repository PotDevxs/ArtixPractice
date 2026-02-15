package dev.artixdev.practice.configs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.BasicYamlStorage;

public class ScoreboardConfig extends BasicYamlStorage {
   @ConfigValue(
      path = "SCOREBOARD.TITLE",
      priority = 1
   )
   public static String TITLE = "&c&lRefine &7&l%splitter% &fPractice";
   
   // Animated Title System - Frame Based
   @ConfigValue(
      path = "SCOREBOARD.ANIMATED_TITLE.ENABLED",
      priority = 2
   )
   public static boolean ANIMATED_TITLE_ENABLED = true;
   
   @ConfigValue(
      path = "SCOREBOARD.ANIMATED_TITLE.FRAMES",
      priority = 3
   )
   public static List<String> ANIMATION_FRAMES = Arrays.asList(
      "&c&lRefine &7&l%splitter% &fPractice",
      "&6&lRefine &7&l%splitter% &fPractice", 
      "&e&lRefine &7&l%splitter% &fPractice",
      "&a&lRefine &7&l%splitter% &fPractice",
      "&b&lRefine &7&l%splitter% &fPractice",
      "&9&lRefine &7&l%splitter% &fPractice",
      "&d&lRefine &7&l%splitter% &fPractice"
   );
   
   @ConfigValue(
      path = "SCOREBOARD.ANIMATED_TITLE.FRAME_DURATION",
      priority = 4
   )
   public static int FRAME_DURATION = 3; // Ticks per frame
   
   @ConfigValue(
      path = "SCOREBOARD.ANIMATED_TITLE.LOOP_ENABLED",
      priority = 5
   )
   public static boolean LOOP_ENABLED = true;
   @ConfigValue(
      path = "SCOREBOARD.ELO-RANGE-FORMAT",
      priority = 11
   )
   public static String ELO_RANGE_FORMAT = "{min_range} -> {max_range}";
   @ConfigValue(
      path = "SCOREBOARD.ENDING-SCOREBOARD-ENABLED",
      priority = 12
   )
   public static boolean ENDING_SCOREBOARD_ENABLED = true;
   @ConfigValue(
      path = "SCOREBOARD.LOBBY",
      priority = 4
   )
   public static List<String> LOBBY_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lLobby", " &fOnline: &c{online}", " &fQueuing: &c{in_queues}", " &fFighting: &c{in_fights}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.PARTY",
      priority = 5
   )
   public static List<String> PARTY_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lLobby", " &fOnline: &c{online}", " &fQueuing: &c{in_queues}", " &fFighting: &c{in_fights}", "", "&c&lParty", " &fLeader: &c{party_leader}", " &fMembers: &c{party_size}/{party_limit}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.TOURNAMENT",
      priority = 6
   )
   public static List<String> TOURNAMENT_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lLobby", " &fOnline: &c{online}", " &fQueuing: &c{in_queues}", " &fFighting: &c{in_fights}", "", "&c&lTournament &7({tournament_team_size} VS {tournament_team_size})", " &fKit: &c{tournament_kit}", " &fRound: &c{tournament_round}", " &fPlayers: &c{tournament_players}/{tournament_max_players}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.UNRANKED_QUEUE",
      priority = 7
   )
   public static List<String> UNRANKED_QUEUE_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lLobby", " &fOnline: &c{online}", " &fQueuing: &c{in_queues}", " &fFighting: &c{in_fights}", "", "&c&lQueue", " &fType: &c{queue_type}", " &fKit: &c{queue_kit}", " &fDuration: &c{queue_duration}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.RANKED_QUEUE",
      priority = 8
   )
   public static List<String> RANKED_QUEUE_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lLobby", " &fOnline: &c{online}", " &fQueuing: &c{in_queues}", " &fFighting: &c{in_fights}", "", "&c&lQueue", " &fType: &c{queue_type}", " &fKit: &c{queue_kit}", " &fDuration: &c{queue_duration}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO",
      priority = 9
   )
   public static List<String> SOLO_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fDuration: &c{match_duration}", " &fPing: &a{your_ping}ms &7%splitter% &c{opponent_ping}ms", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_BEST_OF",
      priority = 10
   )
   public static List<String> SOLO_BEST_OF_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fDuration: &c{match_duration}", "", "&c&lPoints", " &fYour: &a{your_points_formatted}", " &fTheir: &c{opponent_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_BOXING",
      priority = 11
   )
   public static List<String> SOLO_BOXING_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fDuration: &c{match_duration}", "", "&c&lHits &r{hit_difference}", " &fYour Hits: &a{your_hits} &e{your_combo}", " &fTheir Hits: &c{opponent_hits} &e{opponent_combo}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_BRIDGE",
      priority = 12
   )
   public static List<String> SOLO_BRIDGE_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fKills: &c{your_kills}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_BATTLE_RUSH",
      priority = 13
   )
   public static List<String> SOLO_BATTLE_RUSH_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_MLG_RUSH",
      priority = 14
   )
   public static List<String> SOLO_MLG_RUSH_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fKills: &c{your_kills}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &aYou: &r{your_points_formatted}", " &cThem: &r{their_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_PEARL_FIGHT",
      priority = 15
   )
   public static List<String> SOLO_PEARL_FIGHT_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fDuration: &c{match_duration}", "", "&c&lLives", " &c[R] &r{red_lives_formatted}", " &9[B] &r{blue_lives_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_BEDWARS",
      priority = 16
   )
   public static List<String> SOLO_BEDWARS_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fKills: &c{your_kills}", " &fDuration: &c{match_duration}", "", "&c&lBeds", " &c[R] &fRed: &r{bedwars_red_formatted}", " &9[B] &fBlue: &r{bedwars_blue_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_TOP_FIGHT",
      priority = 17
   )
   public static List<String> SOLO_TOP_FIGHT_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fDuration: &c{match_duration}", "", "&c&lLives", " &c[R] &r{red_lives_formatted}", " &9[B] &r{blue_lives_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.SOLO_STICK_FIGHT",
      priority = 18
   )
   public static List<String> SOLO_STICK_FIGHT_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fRival: &c{opponent_name}", " &fKills: &c{your_kills}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &aYou: &r{your_points_formatted}", " &cThem: &r{their_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.TEAM",
      priority = 19
   )
   public static List<String> TEAM_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fDuration: &c{match_duration}", " &fKit: &c{match_kit}", "", " &fYour Team: &c{your_team_alive}/{your_team_count}", " &fRival Team: &c{opponent_team_alive}/{opponent_team_count}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.TEAM_BEST_OF",
      priority = 20
   )
   public static List<String> TEAM_BEST_OF_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fDuration: &c{match_duration}", " &fKit: &c{match_kit}", "", "&c&lPoints", " &fYour: &a{your_points_formatted}", " &fTheir: &c{opponent_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.TEAM_BOXING",
      priority = 21
   )
   public static List<String> TEAM_BOXING_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fDuration: &c{match_duration}", " &fKit: &c{match_kit}", "", "&c&lHits &r{hit_difference}", " &fYour Team Hits: &a{your_hits}", " &fRival Team Hits: &c{opponent_hits}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.TEAM_BRIDGE",
      priority = 22
   )
   public static List<String> TEAM_BRIDGE_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fDuration: &c{match_duration}", " &fKit: &c{match_kit}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.TEAM_BEDWARS",
      priority = 23
   )
   public static List<String> TEAM_BEDWARS_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fDuration: &c{match_duration}", " &fKills: &c{your_kills}", "", "&c&lBeds", " &c[R] &fRed: &r{bedwars_red_formatted}", " &9[B] &fBlue: &r{bedwars_blue_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.TEAM_BATTLE_RUSH",
      priority = 24
   )
   public static List<String> TEAM_BATTLE_RUSH_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fDuration: &c{match_duration}", " &fKit: &c{match_kit}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.TEAM_HCF",
      priority = 25
   )
   public static List<String> TEAM_HCF_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fDuration: &c{match_duration}", " &fKit: &c{match_kit}", "", " &fYour Team: &c{your_team_alive}/{your_team_count}", " &fRival Team: &c{opponent_team_alive}/{opponent_team_count}", "{bard}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.HCF_BARD_ADDITION",
      priority = 26
   )
   public static List<String> HCF_BARD_ADDITION_SCOREBOARD = Collections.singletonList(" &fBard Energy: &c{your_bard_energy}");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.FFA",
      priority = 27
   )
   public static List<String> FFA_MATCH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &fPlayers: &c{alive_count}/{total_count}", " &fKit: &c{match_kit}", " &fDuration: &c{match_duration}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.MATCH.ENDING",
      priority = 28
   )
   public static List<String> ENDING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&cMatch Status: &fEnded", "", "&7Your match has ended,", "&7teleporting you to spawn.", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO",
      priority = 29
   )
   public static List<String> SPECTATOR_MATCH_SOLO_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", " &c{playerA_name} &7({playerA_ping})", " &7&ovs", " &c{playerB_name} &7({playerB_ping})", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_BEST_OF",
      priority = 30
   )
   public static List<String> SPECTATOR_MATCH_SOLO_BEST_OF_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lPoints", " &fPlayer A: &e{playerA_points_formatted}", " &fPlayer B: &e{playerB_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_BOXING",
      priority = 31
   )
   public static List<String> SPECTATOR_MATCH_SOLO_BOXING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lHits", " &fPlayerA Hits: &a{playerA_hits}", " &fPlayerB Hits: &c{playerB_hits}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_BATTLE_RUSH",
      priority = 32
   )
   public static List<String> SPECTATOR_MATCH_SOLO_BATTLE_RUSH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_BRIDGE",
      priority = 33
   )
   public static List<String> SPECTATOR_MATCH_SOLO_BRIDGE_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_MLG_RUSH",
      priority = 34
   )
   public static List<String> SPECTATOR_MATCH_SOLO_MLG_RUSH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &ePlayerA &r{playerA_points_formatted}", " &ePlayerB &r{playerA_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_STICK_FIGHT",
      priority = 35
   )
   public static List<String> SPECTATOR_MATCH_SOLO_STICK_FIGHT_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &ePlayerA &r{playerA_points_formatted}", " &ePlayerB &r{playerB_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_BEDWARS",
      priority = 36
   )
   public static List<String> SPECTATOR_MATCH_SOLO_BEDWARS_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lBeds", " &c[R] &fRed: &r{bedwars_red_formatted}", " &9[B] &fBlue: &r{bedwars_blue_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_PEARL_FIGHT",
      priority = 37
   )
   public static List<String> SPECTATOR_MATCH_SOLO_PEARL_FIGHT_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lLives", " &c[R] &r{red_lives_formatted}", " &9[B] &r{blue_lives_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.SOLO_TOP_FIGHT",
      priority = 38
   )
   public static List<String> SPECTATOR_MATCH_SOLO_TOP_FIGHT_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Solo)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lLives", " &c[R] &r{red_lives_formatted}", " &9[B] &r{blue_lives_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.TEAM",
      priority = 39
   )
   public static List<String> SPECTATOR_MATCH_TEAM_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Team)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", " &cTeam A &7({teamA_size}&7)", "&7vs", " &cTeam B &7({teamB_size}&7)", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.TEAM_BEST_OF",
      priority = 40
   )
   public static List<String> SPECTATOR_MATCH_TEAM_BEST_OF_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Team)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lPoints", " &fTeam A: &e{teamA_points_formatted}", " &fTeam B: &e{teamB_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.TEAM_BOXING",
      priority = 41
   )
   public static List<String> SPECTATOR_MATCH_TEAM_BOXING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Team)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lHits", " &fTeamA Hits: &a{teamA_hits}", " &fTeamB Hits: &c{teamB_hits}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.TEAM_BRIDGE",
      priority = 42
   )
   public static List<String> SPECTATOR_MATCH_TEAM_BRIDGE_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Team)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.TEAM_BATTLE_RUSH",
      priority = 43
   )
   public static List<String> SPECTATOR_MATCH_TEAM_BATTLE_RUSH_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Team)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lScore", " &c[R] &r{red_points_formatted}", " &9[B] &r{blue_points_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.TEAM_BEDWARS",
      priority = 44
   )
   public static List<String> SPECTATOR_MATCH_TEAM_BEDWARS_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Team)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", "&c&lBeds", " &c[R] &fRed: &r{bedwars_red_formatted}", " &9[B] &fBlue: &r{bedwars_blue_formatted}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.TEAM_HCF",
      priority = 45
   )
   public static List<String> SPECTATOR_MATCH_TEAM_HCF_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (Team)", " &fKit: &c{match_kit}", " &fArena: &c{match_arena}", " &fDuration: &c{match_duration}", "", " &cTeam A &7({teamA_size}&7)", "&7vs", " &cTeam B &7({teamB_size}&7)", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.FFA",
      priority = 46
   )
   public static List<String> SPECTATOR_MATCH_FFA_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight (FFA)", " &fKit: &c{match_kit}", " &fDuration: &c{match_duration}", " &fAlive: &c{players_alive}/{player_count}", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.SPECTATOR.MATCH.ENDING",
      priority = 47
   )
   public static List<String> SPECTATOR_MATCH_ENDING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&lFight", " &cMatch Status: &fEnded", "", " &7The match has ended,", " &7teleporting you to spawn.", "", "&7&otest.refinedev.xyz", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.EVENT.SOLO.STATUS_WAITING",
      priority = 50
   )
   public static String SOLO_EVENT_STATUS_WAITING_SCOREBOARD = " &fWaiting for players...";
   @ConfigValue(
      path = "SCOREBOARD.EVENT.SOLO.STATUS_COUNTING",
      priority = 51
   )
   public static String SOLO_EVENT_STATUS_COUNTING_SCOREBOARD = " &fStarting in &c{event_remaining}&fs";
   @ConfigValue(
      path = "SCOREBOARD.EVENT.SOLO.WAITING",
      priority = 52
   )
   public static List<String> SOLO_EVENT_WAITING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&l{event_name} Event", "", " &fHost: &c{event_host_name}", " &fPlayers: &c{event_player_count}/{event_max_players}", "", "{event_status}", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.EVENT.SOLO.FIGHTING",
      priority = 53
   )
   public static List<String> SOLO_EVENT_FIGHTING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&l{event_name} Event", "", " &fPlayers: &c{event_players_alive}/{event_max_players}", " &fDuration: &c{event_duration}", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.EVENT.SOLO.ROUND_ADDITION",
      priority = 54
   )
   public static List<String> SOLO_EVENT_ROUND_ADDITION_SCOREBOARD = Arrays.asList("", " &c{event_playerA_name} &7(&f{event_playerA_ping}&7)", "&7vs", " &c{event_playerB_name} &7(&f{event_playerB_ping}&7)");
   @ConfigValue(
      path = "SCOREBOARD.EVENT.TEAM.STATUS_WAITING",
      priority = 60
   )
   public static String TEAM_EVENT_STATUS_WAITING_SCOREBOARD = " &fWaiting for players...";
   @ConfigValue(
      path = "SCOREBOARD.EVENT.TEAM.STATUS_COUNTING",
      priority = 61
   )
   public static String TEAM_EVENT_STATUS_COUNTING_SCOREBOARD = " &fStarting in &c{event_remaining}&fs";
   @ConfigValue(
      path = "SCOREBOARD.EVENT.TEAM.WAITING",
      priority = 62
   )
   public static List<String> TEAM_EVENT_WAITING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&l{event_name} Event", "", " &fHost: &c{event_host_name}", " &fPlayers: &c{event_player_count}/{event_max_players}", "", "{event_status}", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.EVENT.TEAM.FIGHTING",
      priority = 63
   )
   public static List<String> TEAM_EVENT_FIGHTING_SCOREBOARD = Arrays.asList("&7&m----------------------", "&c&l{event_name} Event", "", " &fPlayers: &c{event_players_alive}/{event_max_players}", " &fDuration: &c{event_duration}", "&7&m----------------------");
   @ConfigValue(
      path = "SCOREBOARD.EVENT.TEAM.ROUND_ADDITION",
      priority = 64
   )
   public static List<String> TEAM_EVENT_ROUND_ADDITION_SCOREBOARD = Arrays.asList("", " &c{event_teamA_name} &7(&f{event_teamA_size}&7)", "&7vs", " &c{event_teamB_name} &7(&f{event_teamB_size}&7)");

   public ScoreboardConfig(JavaPlugin plugin) {
      super(plugin, "scoreboard", false);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = ScoreboardConfig.class.getDeclaredFields();
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
      return new String[]{"This is the scoreboard config file for Bolt.", "Placeholders and documentation can be viewed at: https://docs.refinedev.xyz/", "You need help with the configuration or have any questions related to Bolt?", "Join us in our Discord: https://dsc.gg/refine", null};
   }
   
   // Animated Title Methods - Frame Based
   private static int currentFrame = 0;
   private static int frameCounter = 0;
   
   /**
    * Get animated title based on current frame
    */
   public static String getAnimatedTitle() {
      if (!ANIMATED_TITLE_ENABLED || ANIMATION_FRAMES.isEmpty()) {
         return TITLE;
      }
      
      return ANIMATION_FRAMES.get(currentFrame);
   }
   
   /**
    * Update animation frame - call this every tick
    */
   public static void updateAnimationFrame() {
      if (!ANIMATED_TITLE_ENABLED || ANIMATION_FRAMES.isEmpty()) {
         return;
      }
      
      frameCounter++;
      
      // Check if it's time to advance to next frame
      if (frameCounter >= FRAME_DURATION) {
         frameCounter = 0;
         currentFrame++;
         
         // Loop back to start if enabled
         if (currentFrame >= ANIMATION_FRAMES.size()) {
            if (LOOP_ENABLED) {
               currentFrame = 0;
            } else {
               currentFrame = ANIMATION_FRAMES.size() - 1; // Stay on last frame
            }
         }
      }
   }
   
   /**
    * Reset animation to initial state
    */
   public static void resetAnimation() {
      currentFrame = 0;
      frameCounter = 0;
   }
   
   /**
    * Get current animation frame index
    */
   public static int getCurrentFrame() {
      return currentFrame;
   }
   
   /**
    * Get total number of frames
    */
   public static int getTotalFrames() {
      return ANIMATION_FRAMES.size();
   }
   
   /**
    * Check if animation is enabled
    */
   public static boolean isAnimationEnabled() {
      return ANIMATED_TITLE_ENABLED;
   }
   
   /**
    * Get frame duration in ticks
    */
   public static int getFrameDuration() {
      return FRAME_DURATION;
   }
   
   /**
    * Check if animation loops
    */
   public static boolean isLoopEnabled() {
      return LOOP_ENABLED;
   }
   
   /**
    * Set specific frame
    */
   public static void setFrame(int frame) {
      if (frame >= 0 && frame < ANIMATION_FRAMES.size()) {
         currentFrame = frame;
         frameCounter = 0;
      }
   }
   
   /**
    * Get specific frame text
    */
   public static String getFrameText(int frame) {
      if (frame >= 0 && frame < ANIMATION_FRAMES.size()) {
         return ANIMATION_FRAMES.get(frame);
      }
      return TITLE;
   }

   public static String getAnimationType() {
      return "FRAME_BASED"; // Default animation type
   }
}
