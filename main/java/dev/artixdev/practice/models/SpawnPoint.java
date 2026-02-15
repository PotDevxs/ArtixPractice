package dev.artixdev.practice.models;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class SpawnPoint {
    
    private Location primarySpawn;
    private final Map<Integer, Location> spawnPoints;
    private Location spectatorSpawn;

    public SpawnPoint() {
        this.spawnPoints = new HashMap<>();
    }

    public Location getPrimarySpawn() {
        return primarySpawn;
    }

    public void setPrimarySpawn(Location primarySpawn) {
        this.primarySpawn = primarySpawn;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public void setSpectatorSpawn(Location spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    public Map<Integer, Location> getSpawnPoints() {
        return spawnPoints;
    }

    public Location getSpawnPoint(int index) {
        return spawnPoints.get(index);
    }

    public void setSpawnPoint(int index, Location location) {
        spawnPoints.put(index, location);
    }

    public void removeSpawnPoint(int index) {
        spawnPoints.remove(index);
    }
}
