package dev.artixdev.practice.utils;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public final class FireworkUtils {

    private FireworkUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void spawnFirework(List<Player> players, Location location, FireworkEffect effect, boolean instant) {
        // Spawn firework effect for players
        for (Player player : players) {
            if (player != null && player.isOnline()) {
                // Create and spawn firework at location
                // Implementation would depend on specific requirements
            }
        }
    }
}
