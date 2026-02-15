package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class LeaderboardMenu extends PaginatedMenu {
    private static final String[] MENU_TITLES = {"ù|;ËÄm#ûh-¥"};

    public LeaderboardMenu() {
        this.setAutoUpdate(true);
        this.setBordered(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Leaderboard";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        
        Main plugin = Main.getInstance();
        
        // Get leaderboard by ELO (default category) from storage
        // Note: This is a synchronous call - in production this should be handled asynchronously
        // TODO: Implement async leaderboard loading with CompletableFuture
        List<PlayerProfile> leaderboard = new java.util.ArrayList<>();
        
        try {
            // Try to get top players by ELO
            java.util.concurrent.CompletableFuture<List<PlayerProfile>> future = 
                plugin.getStorageManager().getTopPlayers("elo", 100);
            
            // For now, use empty list if async call is not immediately available
            // In production, this menu should be updated asynchronously
            if (future.isDone()) {
                leaderboard = future.get();
            }
        } catch (Exception e) {
            // If there's an error, use empty list
            leaderboard = new java.util.ArrayList<>();
        }
        
        int position = 1;
        for (PlayerProfile profile : leaderboard) {
            Integer slot = buttons.size();
            LeaderboardButton button = new LeaderboardButton(profile, position++);
            buttons.put(slot, button);
        }
        
        return buttons;
    }
    
    /**
     * Leaderboard Button
     * Button for displaying a player in the leaderboard
     */
    private class LeaderboardButton extends Button {
        private final PlayerProfile profile;
        private final int position;
        
        public LeaderboardButton(PlayerProfile profile, int position) {
            this.profile = profile;
            this.position = position;
        }
        
        @Override
        public ItemStack getButtonItem(Player player) {
            if (profile == null) {
                return null;
            }
            
            ItemBuilder itemBuilder = new ItemBuilder(XMaterial.PLAYER_HEAD);
            
            String name = "&7#" + position + " &f" + profile.getName();
            itemBuilder.name(ChatUtils.colorize(name));
            
            java.util.List<String> lore = new java.util.ArrayList<>();
            lore.add("&7ELO: &f" + profile.getElo());
            lore.add("&7Wins: &f" + profile.getWins());
            lore.add("&7Losses: &f" + profile.getLosses());
            lore.add("&7Kills: &f" + profile.getKills());
            lore.add("&7Deaths: &f" + profile.getDeaths());
            lore.add("&7KDR: &f" + String.format("%.2f", profile.getKDR()));
            lore.add("");
            lore.add("&eClick to view profile");
            
            itemBuilder.lore(lore);
            
            return itemBuilder.build();
        }
        
        @Override
        public void clicked(Player player, ClickType clickType) {
            // Open profile menu for this player
            // TODO: Implement profile menu opening
            player.sendMessage(ChatUtils.colorize("&7Viewing profile of &f" + profile.getName()));
        }
    }
}
