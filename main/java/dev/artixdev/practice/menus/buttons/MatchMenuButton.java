package dev.artixdev.practice.menus.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.MatchMenu;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Match Menu Button
 * Button that opens a match menu when clicked
 */
public class MatchMenuButton extends Button {
   
   private final int slot;
   private final Match match;
   private final PlayerProfile playerProfile;
   
   /**
    * Constructor
    * @param match the match
    * @param playerProfile the player profile
    * @param slot the slot
    */
   public MatchMenuButton(Match match, PlayerProfile playerProfile, int slot) {
      this.match = match;
      this.playerProfile = playerProfile;
      this.slot = slot;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      Kit kit = match.getKit();
      ItemStack icon = (kit != null && kit.getDisplayIcon() != null) ? kit.getDisplayIcon().clone() : new ItemStack(Material.DIAMOND_SWORD);
      String p1 = match.getPlayer1() != null ? match.getPlayer1().getName() : "—";
      String p2 = match.getPlayer2() != null ? match.getPlayer2().getName() : "—";
      String kitName = kit != null ? kit.getName() : (match.getKitType() != null ? match.getKitType().getDisplayName() : "?");
      List<String> lore = new ArrayList<>();
      lore.add(ChatUtils.translate("&7" + p1 + " &8vs &7" + p2));
      lore.add(ChatUtils.translate("&7Kit: &f" + kitName));
      lore.add(ChatUtils.translate("&7Click to view match info."));
      return new ItemBuilder(icon).name(ChatUtils.translate("&6Match #" + (slot + 1))).lore(lore).build();
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      try {
         // Create and open match menu
         MatchMenu matchMenu = new MatchMenu(match);
         
         // Open menu using MenuHandler
         MenuHandler.getInstance().openMenu(matchMenu, player);
         
      } catch (Exception e) {
         player.sendMessage("§cAn error occurred while opening the match menu!");
         e.printStackTrace();
      }
   }
   
   /**
    * Get match
    * @return match
    */
   public Match getMatch() {
      return match;
   }
   
   /**
    * Get player profile
    * @return player profile
    */
   public PlayerProfile getPlayerProfile() {
      return playerProfile;
   }
   
   /**
    * Get slot
    * @return slot
    */
   public int getSlot() {
      return slot;
   }
}
