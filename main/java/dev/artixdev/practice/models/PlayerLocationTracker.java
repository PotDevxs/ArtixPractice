package dev.artixdev.practice.models;

import java.util.LinkedList;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerLocationTracker {
    
    private final PlayerProfile playerProfile;
    private final Player player;
    private static final int MAX_LOCATIONS = 20;
    private final LinkedList<Pair<Location, Long>> locationHistory;

    public PlayerLocationTracker(PlayerProfile playerProfile, Player player) {
        this.playerProfile = playerProfile;
        this.player = player;
        this.locationHistory = new LinkedList<>();
    }

    public void addLocation(Location location) {
        long timestamp = System.currentTimeMillis();
        Pair<Location, Long> locationPair = Pair.of(location, timestamp);
        this.locationHistory.add(locationPair);
        
        // Keep only the last 20 locations
        if (this.locationHistory.size() > MAX_LOCATIONS) {
            this.locationHistory.removeFirst();
        }
    }

    public Location getLastLocation() {
        if (locationHistory.isEmpty()) {
            return null;
        }
        return locationHistory.getLast().getLeft();
    }

    public Player getPlayer() {
        return this.player;
    }

    public LinkedList<Pair<Location, Long>> getLocationHistory() {
        return new LinkedList<>(this.locationHistory);
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    public void clearHistory() {
        this.locationHistory.clear();
    }

    public int getHistorySize() {
        return this.locationHistory.size();
    }

    public boolean hasLocationHistory() {
        return !this.locationHistory.isEmpty();
    }
}
