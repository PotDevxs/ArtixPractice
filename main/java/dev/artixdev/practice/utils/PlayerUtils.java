package dev.artixdev.practice.utils;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {
    
    public PlayerUtils() {
        // Utility class
    }
    
    /**
     * Get player name from UUID
     * @param uuid the player UUID
     * @return player name or null if not found
     */
    public static String getName(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        
        // Try to get online player first
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return player.getName();
        }
        
        // Fallback to offline player
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer != null) {
            return offlinePlayer.getName();
        }
        
        return null;
    }
}