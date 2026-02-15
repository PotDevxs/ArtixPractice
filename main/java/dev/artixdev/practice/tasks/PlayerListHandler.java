package dev.artixdev.practice.tasks;

import org.bukkit.entity.Player;
import dev.artixdev.practice.models.PlayerProfile;

/**
 * Minimal contract for updating entries in the player list.
 */
public interface PlayerListHandler {
    void updatePlayerEntry(Player player, PlayerProfile profile);
}
