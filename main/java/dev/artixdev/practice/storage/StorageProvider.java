package dev.artixdev.practice.storage;

import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.models.Bot;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Storage Provider Interface
 * Defines the contract for all storage implementations
 */
public interface StorageProvider {
    
    /**
     * Initialize the storage provider
     */
    void initialize();
    
    /**
     * Check if the storage is connected
     */
    boolean isConnected();
    
    /**
     * Close the storage connection
     */
    void closeConnection();
    
    /**
     * Reconnect to storage
     */
    void reconnect();
    
    // Player Profile Operations
    CompletableFuture<PlayerProfile> loadPlayerProfile(UUID uuid);
    CompletableFuture<Void> savePlayerProfile(PlayerProfile profile);
    CompletableFuture<List<PlayerProfile>> loadAllPlayerProfiles();
    CompletableFuture<Void> deletePlayerProfile(UUID uuid);
    
    // Arena Operations
    CompletableFuture<Arena> loadArena(UUID uuid);
    CompletableFuture<Void> saveArena(Arena arena);
    CompletableFuture<List<Arena>> loadAllArenas();
    CompletableFuture<Void> deleteArena(UUID uuid);
    
    // Match Operations
    CompletableFuture<Match> loadMatch(UUID uuid);
    CompletableFuture<Void> saveMatch(Match match);
    CompletableFuture<List<Match>> loadAllMatches();
    CompletableFuture<Void> deleteMatch(UUID uuid);
    
    // Queue Operations
    CompletableFuture<Queue> loadQueue(UUID uuid);
    CompletableFuture<Void> saveQueue(Queue queue);
    CompletableFuture<List<Queue>> loadAllQueues();
    CompletableFuture<Void> deleteQueue(UUID uuid);
    
    // Bot Operations
    CompletableFuture<Bot> loadBot(UUID uuid);
    CompletableFuture<Void> saveBot(Bot bot);
    CompletableFuture<List<Bot>> loadAllBots();
    CompletableFuture<Void> deleteBot(UUID uuid);
    
    // Statistics Operations
    CompletableFuture<Map<String, Object>> getPlayerStatistics(UUID uuid);
    CompletableFuture<List<PlayerProfile>> getTopPlayers(String category, int limit);
}
