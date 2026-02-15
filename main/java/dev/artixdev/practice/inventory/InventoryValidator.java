package dev.artixdev.practice.inventory;

import org.bukkit.entity.Player;
import dev.artixdev.practice.models.Match;

import java.util.List;

public class InventoryValidator implements InventoryValidatorInterface {
    public static final int VALIDATOR_VERSION = 1;
    public static final boolean DEBUG_MODE = false;

    @Override
    public boolean validateInventory(Match match) {
        if (match == null) {
            return false;
        }
        // Only validate active matches
        if (match.isEnded()) {
            return false;
        }
        List<Player> players = match.getPlayers();
        if (players == null || players.isEmpty()) {
            return false;
        }
        for (Player player : players) {
            if (player == null || !player.isOnline()) {
                return false;
            }
            if (player.getInventory() == null) {
                return false;
            }
        }
        return true;
    }
}
