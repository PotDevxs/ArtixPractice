package dev.artixdev.practice.menus;

import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class StatisticsMenu extends Menu {
    private final PlayerProfile playerProfile;
    private final Menu previousMenu;

    public StatisticsMenu(Menu previousMenu, PlayerProfile playerProfile) {
        this.setPlaceholder(true);
        this.playerProfile = playerProfile;
        this.previousMenu = previousMenu;
    }

    @Override
    public String getTitle(Player player) {
        return "Statistics - " + playerProfile.getUsername();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        // Create statistics display buttons
        // This would typically show various player statistics
        return createStatisticsButtons(player);
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public void onClose(Player player) {
        // Save player data when menu closes
        Main plugin = Main.getInstance();
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        
        if (profile != null) {
            plugin.getStorageManager().savePlayerProfile(profile);
        }
    }

    private Map<Integer, Button> createStatisticsButtons(Player player) {
        // Implementation would create buttons for various statistics
        // This is a placeholder - actual implementation would create specific buttons
        return null; // Placeholder
    }
}