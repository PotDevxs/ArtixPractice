package dev.artixdev.practice.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerLocationTracker;
import dev.artixdev.practice.models.PlayerProfile;

public class LocationTrackingManager {
    
    private final int defaultPing;
    private final Map<UUID, PlayerLocationTracker> playerTrackers = new ConcurrentHashMap<>();
    private final Main plugin;

    public LocationTrackingManager(Main plugin, int defaultPing) {
        this.plugin = plugin;
        this.defaultPing = defaultPing;
    }

    public void startTracking(Player player) {
        UUID playerId = player.getUniqueId();
        // Get player profile from PlayerManager, or create a new one if not found
        PlayerProfile playerProfile = plugin.getPlayerManager().getPlayerProfile(playerId);
        if (playerProfile == null) {
            // Create a new profile if it doesn't exist
            playerProfile = new PlayerProfile(player.getUniqueId(), player.getName());
        }
        PlayerLocationTracker tracker = new PlayerLocationTracker(playerProfile, player);
        this.playerTrackers.put(playerId, tracker);
    }

    public PlayerLocationTracker getTracker(Player player) {
        return this.playerTrackers.get(player.getUniqueId());
    }

    public Map<UUID, PlayerLocationTracker> getAllTrackers() {
        return new ConcurrentHashMap<>(this.playerTrackers);
    }

    public int getDefaultPing() {
        return this.defaultPing;
    }

    public void trackLocation(Player player, Location location) {
        PlayerLocationTracker tracker = this.getTracker(player);
        
        if (tracker == null) {
            this.startTracking(player);
            tracker = this.getTracker(player);
        }
        
        if (tracker != null) {
            tracker.addLocation(location);
        }
    }

    public Location getLastLocation(Player player) {
        PlayerLocationTracker tracker = this.getTracker(player);
        
        if (tracker == null) {
            this.startTracking(player);
            return player.getLocation();
        }
        
        Location lastLocation = tracker.getLastLocation();
        return lastLocation != null ? lastLocation : player.getLocation();
    }

    public void stopTracking(Player player) {
        this.playerTrackers.remove(player.getUniqueId());
    }

    public void clearAllTrackers() {
        this.playerTrackers.clear();
    }

    public boolean isTracking(Player player) {
        return this.playerTrackers.containsKey(player.getUniqueId());
    }

    public int getTrackingCount() {
        return this.playerTrackers.size();
    }
}
