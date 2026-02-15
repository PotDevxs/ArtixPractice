package dev.artixdev.practice.menus;

import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.SpectateMenus;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Spectate Menu
 * Menu for spectating ongoing matches
 */
public class SpectateMenu extends PaginatedMenu {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public SpectateMenu(Main plugin) {
      this.plugin = plugin;
   }
   
   @Override
   public String getPrePaginatedTitle(Player player) {
      return ChatUtils.colorize("&bSpectate Matches");
   }
   
   @Override
   public int getSize() {
      return 54; // Standard chest size (6 rows)
   }
   
   @Override
   public Map<Integer, Button> getAllPagesButtons(Player player) {
      Map<Integer, Button> buttons = new java.util.HashMap<>();
      
      // Get all ongoing matches
      java.util.List<dev.artixdev.practice.models.Match> matches = plugin.getMatchManager().getOngoingMatches();
      
      int slot = 0;
      for (Match match : matches) {
         if (slot >= getMaxItemsPerPage(player)) {
            break;
         }
         
         buttons.put(slot, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
               return new ItemBuilder(XMaterial.DIAMOND_SWORD)
                  .name(ChatUtils.colorize("&cMatch #" + match.getId().toString().substring(0, 8)))
                  .lore(
                     "",
                     ChatUtils.colorize("&7Kit: &f" + match.getKitType().getDisplayName()),
                     ChatUtils.colorize("&7Players: &f" + match.getPlayers().size() + "/" + match.getMaxPlayers()),
                     ChatUtils.colorize("&7Duration: &f" + getMatchDuration(match)),
                     ChatUtils.colorize("&7Arena: &f" + match.getArena().getName()),
                     "",
                     ChatUtils.colorize("&eClick to spectate this match")
                  )
                  .build();
            }
            
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
               // Spectate the match
               plugin.getMatchManager().addSpectator(player, match);
               player.sendMessage(ChatUtils.colorize("&aYou are now spectating Match #" + 
                  match.getId().toString().substring(0, 8) + "!"));
               player.closeInventory();
            }
         });
         
         slot++;
      }
      
      // Add empty slots message if no matches
      if (matches.isEmpty()) {
         buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
               return new ItemBuilder(XMaterial.BARRIER)
                  .name(ChatUtils.colorize("&cNo Matches Available"))
                  .lore(
                     "",
                     ChatUtils.colorize("&7There are no ongoing matches to spectate."),
                     ChatUtils.colorize("&7Check back later!")
                  )
                  .build();
            }
            
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
               // Do nothing
            }
         });
      }
      
      return buttons;
   }
   
   /**
    * Get match duration
    * @param match the match
    * @return duration string
    */
   private String getMatchDuration(Match match) {
      long duration = System.currentTimeMillis() - match.getStartTime();
      long seconds = duration / 1000;
      long minutes = seconds / 60;
      seconds = seconds % 60;
      
      if (minutes > 0) {
         return String.format("%dm %ds", minutes, seconds);
      } else {
         return String.format("%ds", seconds);
      }
   }
   
   @Override
   public Map<Integer, Button> getGlobalButtons(Player player) {
      Map<Integer, Button> buttons = new java.util.HashMap<>();
      
      // Close button
      buttons.put(49, new Button() {
         @Override
         public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.BARRIER)
               .name(ChatUtils.colorize("&cClose"))
               .lore(
                  "",
                  ChatUtils.colorize("&7Click to close this menu")
               )
               .build();
         }
         
         @Override
         public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
         }
      });
      
      // Refresh button
      buttons.put(45, new Button() {
         @Override
         public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.EMERALD)
               .name(ChatUtils.colorize("&aRefresh"))
               .lore(
                  "",
                  ChatUtils.colorize("&7Click to refresh the match list")
               )
               .build();
         }
         
         @Override
         public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            // Refresh the menu
            openMenu(player);
         }
      });
      
      return buttons;
   }

   protected void openMenu(Player player) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'openMenu'");
   }
}
