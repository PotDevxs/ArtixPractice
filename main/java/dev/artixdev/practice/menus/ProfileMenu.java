package dev.artixdev.practice.menus;

import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Profile Menu
 * Menu for displaying player profile information
 */
public class ProfileMenu extends Menu {
   
   private final Main plugin;
   
   /**
    * Constructor
    */
   public ProfileMenu() {
      this.plugin = Main.getInstance();
   }
   
   @Override
   public int getSize() {
      return 27;
   }
   
   @Override
   public String getTitle(Player player) {
      return "&bYour Profile";
   }
   
   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new java.util.HashMap<>();
      
      // Get player profile
      PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
      if (profile == null) {
         return buttons;
      }
      
      // Statistics button
      buttons.put(10, new StatisticsButton(profile));
      
      // Achievements button
      buttons.put(12, new AchievementsButton(profile));
      
      // Settings button
      buttons.put(14, new SettingsButton(profile));
      
      // Leaderboard button
      buttons.put(16, new LeaderboardButton(profile));
      
      return buttons;
   }
   
   /**
    * Statistics Button
    */
   private class StatisticsButton extends Button {
      private final PlayerProfile profile;
      
      public StatisticsButton(PlayerProfile profile) {
         this.profile = profile;
      }
      
      @Override
      public ItemStack getButtonItem(Player player) {
         return new ItemBuilder(XMaterial.PAPER)
            .name("&aStatistics")
            .lore(
               "&7Kills: &f" + profile.getKills(),
               "&7Deaths: &f" + profile.getDeaths(),
               "&7Wins: &f" + profile.getWins(),
               "&7Losses: &f" + profile.getLosses(),
               "&7ELO: &f" + profile.getElo(),
               "&7Winstreak: &f" + profile.getWinstreak(),
               "",
               "&eClick to view detailed statistics"
            )
            .build();
      }
      
      @Override
      public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
         // Open detailed statistics menu
         player.sendMessage("&aOpening detailed statistics...");
      }
   }
   
   /**
    * Achievements Button
    */
   private class AchievementsButton extends Button {
      private final PlayerProfile profile;
      
      public AchievementsButton(PlayerProfile profile) {
         this.profile = profile;
      }
      
      @Override
      public ItemStack getButtonItem(Player player) {
         return new ItemBuilder(XMaterial.GOLD_INGOT)
            .name("&6Achievements")
            .lore(
               "&7View your achievements",
               "&7and progress",
               "",
               "&eClick to view achievements"
            )
            .build();
      }
      
      @Override
      public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
         // Open achievements menu
         player.sendMessage("&aOpening achievements...");
      }
   }
   
   /**
    * Settings Button
    */
   private class SettingsButton extends Button {
      private final PlayerProfile profile;
      
      public SettingsButton(PlayerProfile profile) {
         this.profile = profile;
      }
      
      @Override
      public ItemStack getButtonItem(Player player) {
         return new ItemBuilder(XMaterial.REDSTONE)
            .name("&cSettings")
            .lore(
               "&7Configure your preferences",
               "&7and personal settings",
               "",
               "&eClick to open settings"
            )
            .build();
      }
      
      @Override
      public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
         // Open settings menu
         player.sendMessage("&aOpening settings...");
      }
   }
   
   /**
    * Leaderboard Button
    */
   private class LeaderboardButton extends Button {
      private final PlayerProfile profile;
      
      public LeaderboardButton(PlayerProfile profile) {
         this.profile = profile;
      }
      
      @Override
      public ItemStack getButtonItem(Player player) {
         return new ItemBuilder(XMaterial.DIAMOND)
            .name("&bLeaderboard")
            .lore(
               "&7View the top players",
               "&7and your ranking",
               "",
               "&eClick to view leaderboard"
            )
            .build();
      }
      
      @Override
      public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
         // Open leaderboard menu
         player.sendMessage("&aOpening leaderboard...");
      }
   }
}