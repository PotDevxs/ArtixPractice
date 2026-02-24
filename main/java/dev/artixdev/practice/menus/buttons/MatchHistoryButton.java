package dev.artixdev.practice.menus.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.models.MatchHistory;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Match History Button
 * Button for displaying match history in the match history menu
 */
public class MatchHistoryButton extends Button {
   
   private final PlayerProfile playerProfile;
   private final MatchHistory matchHistory;
   private final int index;
   
   /**
    * Constructor
    * @param playerProfile the player profile
    * @param matchHistory the match history entry
    * @param index the index of the match
    */
   public MatchHistoryButton(PlayerProfile playerProfile, MatchHistory matchHistory, int index) {
      this.playerProfile = playerProfile;
      this.matchHistory = matchHistory;
      this.index = index;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      if (matchHistory == null) {
         return null;
      }
      
      // Determine if player won or lost based on winner/loser snapshots
      boolean won = false;
      if (matchHistory.getWinnerSnapshot() != null && matchHistory.getLoserSnapshot() != null) {
         // Check if player is the winner by comparing names
         won = playerProfile.getName().equals(matchHistory.getWinnerSnapshot().getUsername());
      } else if (matchHistory.getEloChange() > 0) {
         won = true; // Positive ELO change indicates win
      }
      
      String status = won ? MatchMenus.MATCH_HISTORY_STATUS_WON : MatchMenus.MATCH_HISTORY_STATUS_LOST;
      
      // Get match ID
      int matchId = matchHistory.getMatchId();
      if (matchId == 0) {
         matchId = index + 1; // Use index as fallback
      }
      
      // Get material
      Material material = Material.BOOK;
      if (MatchMenus.MATCH_HISTORY_USE_KIT_DISPLAY_ICON && matchHistory.getKit() != null) {
         try {
            material = XMaterial.matchXMaterial(MatchMenus.MATCH_HISTORY_MATERIAL).get().parseMaterial();
         } catch (Exception e) {
            material = Material.BOOK;
         }
      }
      
      ItemBuilder itemBuilder = new ItemBuilder(material);
      
      // Set name
      String name = MatchMenus.MATCH_HISTORY_NAME
         .replace("<id>", String.valueOf(matchId))
         .replace("<status>", status);
      itemBuilder.name(ChatUtils.colorize(name));
      
      // Build lore
      List<String> lore = new ArrayList<>();
      
      // Format date
      String createdAt = formatDate(matchHistory.getTime());
      
      // Get winner and loser names
      String winnerName = "Unknown";
      String loserName = "Unknown";
      
      if (matchHistory.getWinnerSnapshot() != null) {
         winnerName = matchHistory.getWinnerSnapshot().getUsername();
      } else if (matchHistory.getOpponentName() != null) {
         winnerName = won ? playerProfile.getName() : matchHistory.getOpponentName();
      } else {
         winnerName = won ? playerProfile.getName() : "Unknown";
      }
      
      if (matchHistory.getLoserSnapshot() != null) {
         loserName = matchHistory.getLoserSnapshot().getUsername();
      } else if (matchHistory.getOpponentName() != null) {
         loserName = won ? matchHistory.getOpponentName() : playerProfile.getName();
      } else {
         loserName = won ? "Unknown" : playerProfile.getName();
      }
      
      // Build lore from template
      for (String line : MatchMenus.MATCH_HISTORY_LORE) {
         String processedLine = line
            .replace("<createdAt>", createdAt)
            .replace("<winner>", winnerName)
            .replace("<loser>", loserName)
            .replace("<type>", matchHistory.getMatchType() != null ? matchHistory.getMatchType() : "Unknown")
            .replace("<kit>", matchHistory.getKit() != null ? matchHistory.getKit() : "Unknown")
            .replace("<arena>", matchHistory.getArena() != null ? matchHistory.getArena() : "Unknown");
         
         // Handle ELO change
         if (line.contains("<revertElo>")) {
            if (matchHistory.getEloChange() != 0) {
               String eloChangeText = won ? 
                  MatchMenus.MATCH_HISTORY_WINNER_ELO.replace("<elo>", String.valueOf(Math.abs(matchHistory.getEloChange()))) :
                  MatchMenus.MATCH_HISTORY_LOSER_ELO.replace("<elo>", String.valueOf(Math.abs(matchHistory.getEloChange())));
               processedLine = processedLine.replace("<revertElo>", eloChangeText);
            } else {
               processedLine = processedLine.replace("<revertElo>", "");
            }
         } else {
            processedLine = processedLine.replace("<revertElo>", "");
         }
         
         if (!processedLine.trim().isEmpty()) {
            lore.add(ChatUtils.colorize(processedLine));
         }
      }
      
      itemBuilder.lore(lore);
      
      return itemBuilder.build();
   }
   
   /**
    * Format date from timestamp
    * @param timestamp the timestamp
    * @return formatted date string
    */
   private String formatDate(long timestamp) {
      if (timestamp <= 0) {
         return "Unknown";
      }
      try {
         SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
         return sdf.format(new Date(timestamp));
      } catch (Exception e) {
         return "Unknown";
      }
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      if (clickType == ClickType.MIDDLE && matchHistory.getEloChange() != 0) {
         player.sendMessage(ChatUtils.colorize("&eELO revert: contact an admin or use /practice elo revert (if available)."));
      } else {
         String opp = matchHistory.getOpponentName() != null ? matchHistory.getOpponentName() : "Unknown";
         int elo = matchHistory.getEloChange();
         player.sendMessage(ChatUtils.colorize("&7Match #" + matchHistory.getMatchId() + " vs &f" + opp + " &7(ELO " + (elo >= 0 ? "+" : "") + elo + ")"));
      }
   }
}
