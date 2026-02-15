package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.models.PlayerProfile;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatisticsMenu extends Menu {
    
    private final PlayerProfile playerProfile;
    private final Player targetPlayer;

    public PlayerStatisticsMenu(PlayerProfile playerProfile, Player targetPlayer) {
        this.playerProfile = playerProfile;
        this.targetPlayer = targetPlayer;
        this.setAutoUpdate(true);
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    @Override
    public String getTitle(Player player) {
        return targetPlayer.getName() + "'s Statistics";
    }

    @Override
    public int getSize() {
        return 54; // 6 rows
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        // Add statistics buttons
        // This would include buttons for:
        // - Wins/Losses
        // - Kills/Deaths
        // - ELO Rating
        // - Win Rate
        // - Favorite Kit
        // - Match History
        // etc.
        
        return buttons;
    }
}
