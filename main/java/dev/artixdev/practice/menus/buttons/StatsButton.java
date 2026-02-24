package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.StatisticsMenu;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Stats Button
 * Button for displaying player statistics in the stats menu
 */
public class StatsButton extends Button {
   
   private final PlayerProfile targetProfile;
   private final PlayerProfile profile;
   
   /**
    * Constructor
    * @param targetProfile the target profile (viewer)
    * @param profile the profile to display
    */
   public StatsButton(PlayerProfile targetProfile, PlayerProfile profile) {
      this.targetProfile = targetProfile;
      this.profile = profile;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      if (profile == null) {
         return null;
      }
      
      ItemBuilder itemBuilder = new ItemBuilder(XMaterial.PLAYER_HEAD);
      
      itemBuilder.name("&7" + profile.getName());
      
      // Add statistics to lore
      java.util.List<String> lore = new java.util.ArrayList<>();
      lore.add("&7Level: &f" + profile.getLevel());
      lore.add("&7Wins: &f" + profile.getWins());
      lore.add("&7Losses: &f" + profile.getLosses());
      lore.add("&7Kills: &f" + profile.getKills());
      lore.add("&7Deaths: &f" + profile.getDeaths());
      lore.add("&7ELO: &f" + profile.getElo());
      
      itemBuilder.lore(lore);
      
      return itemBuilder.build();
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      if (profile == null) return;
      MenuHandler.getInstance().openMenu(new StatisticsMenu(null, profile), player);
   }
}
