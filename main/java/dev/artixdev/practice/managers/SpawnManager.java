package dev.artixdev.practice.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.JsonStorage;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.SpawnPoint;

public class SpawnManager {

    private final JsonStorage<SpawnPoint> spawnStorage;

    public SpawnManager(JavaPlugin plugin) {
        this.spawnStorage = new JsonStorage<>("spawns", plugin, Main.GSON);
        this.initializeSpawns();
    }

    public void initializeSpawns() {
        // Initialize spawn system
    }

    public void teleportToSpawn(Player player) {
        // Teleport player to spawn
    }

    public void teleportToSpectator(Player player) {
        // Teleport player to spectator spawn
    }

    public void saveSpawnData(Player player) {
        // Save spawn data for player
    }
}
