package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class SpectateMenus extends ChildYamlStorage {
   @ConfigValue(
      priority = 1,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.TITLE"
   )
   public static String SPECTATE_MATCHES_TITLE = "Spectate Matches";
   @ConfigValue(
      priority = 2,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.TITLE"
   )
   public static String SPECTATE_MATCHES_NAME_PARTY = "&a<leaderA>&f's Party &evs. &c<leaderB>&f's Party";
   @ConfigValue(
      priority = 3,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.TITLE"
   )
   public static String SPECTATE_MATCHES_NAME_DUEL = "&a<playerA> &evs. &c<playerB>";
   @ConfigValue(
      priority = 4,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.TITLE"
   )
   public static String SPECTATE_MATCHES_NAME_FFA = "&a<leader>&f's Party";
   @ConfigValue(
      priority = 5,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.PARTY-GAME-LORE"
   )
   public static List<String> SPECTATE_MATCHES_PARTY_GAME_LORE = Arrays.asList("", "&fType: &c<type>", "&fLadder: &c<kit>", "&fArena: &c<arena>", "&fSpectators: &c<spectators>", "", "&cClick to spectate!");
   @ConfigValue(
      priority = 6,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.PARTY-HCT-LORE"
   )
   public static List<String> SPECTATE_MATCHES_PARTY_HCT_LORE = Arrays.asList("", "&fType: &cTeam Fights (HCF)", "&fLadder: &c<kit>", "&fArena: &c<arena>", "&fSpectators: &c<spectators>", "", "&cClick to spectate!");
   @ConfigValue(
      priority = 7,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.PARTY-GAME-LORE"
   )
   public static List<String> SPECTATE_MATCHES_PARTY_FFA_LORE = Arrays.asList("", "&fType: &cFFA", "&fLadder: &c<kit>", "&fArena: &c<arena>", "&fSpectators: &c<spectators>", "", "&cClick to spectate!");
   @ConfigValue(
      priority = 8,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.PARTY-GAME-LORE"
   )
   public static List<String> SPECTATE_MATCHES_SOLO_QUEUE_LORE = Arrays.asList("", "&fType: &cSolo", "&fLadder: &c<kit>", "&fArena: &c<arena>", "&fSpectators: &c<spectators>", "", "&cClick to spectate!");
   @ConfigValue(
      priority = 9,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.PARTY-GAME-LORE"
   )
   public static List<String> SPECTATE_MATCHES_DUO_QUEUE_LORE = Arrays.asList("", "&fType: &cDuos", "&fLadder: &c<kit>", "&fArena: &c<arena>", "&fSpectators: &c<spectators>", "", "&cClick to spectate!");
   @ConfigValue(
      priority = 10,
      path = "SPECTATE-MENUS.SPECTATE-MATCHES.PARTY-GAME-LORE"
   )
   public static List<String> SPECTATE_MATCHES_DUEL_LORE = Arrays.asList("", "&fType: &cDuel", "&fLadder: &c<kit>", "&fArena: &c<arena>", "&fSpectators: &c<spectators>", "", "&cClick to spectate!");
   @ConfigValue(
      priority = 20,
      path = "SPECTATE-MENUS.PLAYERS-IN-MATCH.TITLE"
   )
   public static String PLAYERS_IN_MATCH_TITLE = "Players In Match";
   @ConfigValue(
      priority = 21,
      path = "SPECTATE-MENUS.PLAYERS-IN-MATCH.PLAYER-HEADS"
   )
   public static Boolean PLAYERS_IN_MATCH_PLAYER_HEADS = true;
   @ConfigValue(
      priority = 22,
      path = "SPECTATE-MENUS.PLAYERS-IN-MATCH.ITEM"
   )
   public static String PLAYERS_IN_MATCH_ITEM = "PAPER";
   @ConfigValue(
      priority = 23,
      path = "SPECTATE-MENUS.PLAYERS-IN-MATCH.ITEM-DAMAGE"
   )
   public static Integer PLAYERS_IN_MATCH_ITEM_DAMAGE = 0;
   @ConfigValue(
      priority = 24,
      path = "SPECTATE-MENUS.PLAYERS-IN-MATCH.LORE"
   )
   public static String PLAYERS_IN_MATCH_NAME = "&c<player>";
   @ConfigValue(
      priority = 25,
      path = "SPECTATE-MENUS.PLAYERS-IN-MATCH.LORE-ALIVE"
   )
   public static List<String> PLAYERS_IN_MATCH_LORE = Arrays.asList("", "&fHealth: &c<health> ❤", "&fHunger: &c<hunger>/20", "", "&cLeft click to teleport!");

   public SpectateMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = SpectateMenus.class.getFields();
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
