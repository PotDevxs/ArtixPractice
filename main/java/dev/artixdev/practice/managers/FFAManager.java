package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * FFA zones: PvP allowed, respawn in zone, track FFA kills/deaths.
 */
public class FFAManager {

    private final Main plugin;
    private Location ffaSpawn;
    private String ffaWorldName;
    private final Set<UUID> playersInFFA = new HashSet<>();

    public FFAManager(Main plugin) {
        this.plugin = plugin;
        this.ffaWorldName = plugin.getConfig() != null && plugin.getConfig().contains("ffa-world")
            ? plugin.getConfig().getString("ffa-world", "world")
            : "world";
    }

    public void setFFASpawn(Location loc) {
        this.ffaSpawn = loc;
        if (loc != null) this.ffaWorldName = loc.getWorld() != null ? loc.getWorld().getName() : ffaWorldName;
    }

    public Location getFFASpawn() {
        if (ffaSpawn != null) return ffaSpawn;
        World w = Bukkit.getWorld(ffaWorldName);
        if (w != null) return w.getSpawnLocation();
        return null;
    }

    public String getFFAWorldName() {
        return ffaWorldName;
    }

    public void setFFAWorldName(String name) {
        this.ffaWorldName = name;
    }

    /** Check if location is in FFA world (or configured FFA world name). */
    public boolean isInFFA(Location location) {
        if (location == null || location.getWorld() == null) return false;
        return location.getWorld().getName().equalsIgnoreCase(ffaWorldName);
    }

    public boolean isPlayerInFFA(Player player) {
        return player != null && (playersInFFA.contains(player.getUniqueId()) || isInFFA(player.getLocation()));
    }

    public void setPlayerInFFA(Player player, boolean in) {
        if (player == null) return;
        if (in) playersInFFA.add(player.getUniqueId());
        else playersInFFA.remove(player.getUniqueId());
    }

    public void onFFAKill(Player killer, Player victim) {
        PlayerProfile kProfile = plugin.getPlayerManager().getPlayerProfile(killer.getUniqueId());
        PlayerProfile vProfile = plugin.getPlayerManager().getPlayerProfile(victim.getUniqueId());
        if (kProfile != null) kProfile.addFfaKill();
        if (vProfile != null) vProfile.addFfaDeath();
    }

    public void respawnInFFA(Player player) {
        Location spawn = getFFASpawn();
        if (spawn != null && player != null) {
            player.teleport(spawn);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
        }
    }
}
