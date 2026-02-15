package dev.artixdev.practice.storage;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.models.Bot;
import dev.artixdev.practice.storage.impl.MongoStorage;
import dev.artixdev.practice.storage.impl.MySQLStorage;
import dev.artixdev.practice.storage.impl.SQLiteStorage;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Storage Manager
 * Handles all data storage operations
 */
public class StorageManager {
    
    private final Main plugin;
    private StorageProvider storageProvider;
    
    public StorageManager(Main plugin) {
        this.plugin = plugin;
        initializeStorage();
    }
    
    /**
     * Initialize storage provider based on configuration
     */
    private void initializeStorage() {
        String databaseType = plugin.getConfigManager().getDatabaseConfig().getDatabaseType().toUpperCase();
        
        switch (databaseType) {
            case "MONGODB":
                storageProvider = new MongoStorage(plugin);
                break;
            case "MYSQL":
                storageProvider = new MySQLStorage(plugin);
                break;
            case "SQLITE":
                storageProvider = new SQLiteStorage(plugin);
                break;
            default:
                plugin.getLogger().warning("Unknown database type: " + databaseType + ", defaulting to SQLite");
                storageProvider = new SQLiteStorage(plugin);
                break;
        }
        
        storageProvider.initialize();
    }
    
    // Player Profile Operations
    public CompletableFuture<PlayerProfile> loadPlayerProfile(UUID uuid) {
        return storageProvider.loadPlayerProfile(uuid);
    }
    
    public CompletableFuture<Void> savePlayerProfile(PlayerProfile profile) {
        return storageProvider.savePlayerProfile(profile);
    }
    
    public CompletableFuture<List<PlayerProfile>> loadAllPlayerProfiles() {
        return storageProvider.loadAllPlayerProfiles();
    }
    
    public CompletableFuture<Void> deletePlayerProfile(UUID uuid) {
        return storageProvider.deletePlayerProfile(uuid);
    }
    
    // Arena Operations
    public CompletableFuture<Arena> loadArena(UUID uuid) {
        return storageProvider.loadArena(uuid);
    }
    
    public CompletableFuture<Void> saveArena(Arena arena) {
        return storageProvider.saveArena(arena);
    }
    
    public CompletableFuture<List<Arena>> loadAllArenas() {
        return storageProvider.loadAllArenas();
    }
    
    public CompletableFuture<Void> deleteArena(UUID uuid) {
        return storageProvider.deleteArena(uuid);
    }
    
    // Match Operations
    public CompletableFuture<Match> loadMatch(UUID uuid) {
        return storageProvider.loadMatch(uuid);
    }
    
    public CompletableFuture<Void> saveMatch(Match match) {
        return storageProvider.saveMatch(match);
    }
    
    public CompletableFuture<List<Match>> loadAllMatches() {
        return storageProvider.loadAllMatches();
    }
    
    public CompletableFuture<Void> deleteMatch(UUID uuid) {
        return storageProvider.deleteMatch(uuid);
    }
    
    // Queue Operations
    public CompletableFuture<Queue> loadQueue(UUID uuid) {
        return storageProvider.loadQueue(uuid);
    }
    
    public CompletableFuture<Void> saveQueue(Queue queue) {
        return storageProvider.saveQueue(queue);
    }
    
    public CompletableFuture<List<Queue>> loadAllQueues() {
        return storageProvider.loadAllQueues();
    }
    
    public CompletableFuture<Void> deleteQueue(UUID uuid) {
        return storageProvider.deleteQueue(uuid);
    }
    
    // Bot Operations
    public CompletableFuture<Bot> loadBot(UUID uuid) {
        return storageProvider.loadBot(uuid);
    }
    
    public CompletableFuture<Void> saveBot(Bot bot) {
        return storageProvider.saveBot(bot);
    }
    
    public CompletableFuture<List<Bot>> loadAllBots() {
        return storageProvider.loadAllBots();
    }
    
    public CompletableFuture<Void> deleteBot(UUID uuid) {
        return storageProvider.deleteBot(uuid);
    }
    
    // Statistics Operations
    public CompletableFuture<Map<String, Object>> getPlayerStatistics(UUID uuid) {
        return storageProvider.getPlayerStatistics(uuid);
    }
    
    public CompletableFuture<List<PlayerProfile>> getTopPlayers(String category, int limit) {
        return storageProvider.getTopPlayers(category, limit);
    }
    
    // Connection Management
    public boolean isConnected() {
        return storageProvider.isConnected();
    }
    
    public void closeConnection() {
        storageProvider.closeConnection();
    }
    
    public void reconnect() {
        storageProvider.reconnect();
    }
}
