package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Leaderboard Manager
 * Handles all leaderboard-related operations
 */
public class LeaderboardManager {
    
    private final Main plugin;
    private final Map<String, List<PlayerProfile>> leaderboards;
    private final Map<String, Long> lastUpdate;
    
    public LeaderboardManager(Main plugin) {
        this.plugin = plugin;
        this.leaderboards = new HashMap<>();
        this.lastUpdate = new HashMap<>();
    }
    
    /**
     * Initialize leaderboard manager
     */
    public void initialize() {
        plugin.getLogger().info("Initializing LeaderboardManager...");
        
        // Initialize leaderboard categories
        initializeCategories();
        
        plugin.getLogger().info("LeaderboardManager initialized successfully!");
    }
    
    /**
     * Initialize leaderboard categories
     */
    private void initializeCategories() {
        String[] categories = {"wins", "losses", "kills", "deaths", "level", "win_streak", "best_win_streak"};
        
        for (String category : categories) {
            leaderboards.put(category, new java.util.ArrayList<>());
            lastUpdate.put(category, 0L);
        }
    }
    
    /**
     * Get leaderboard for category
     */
    public CompletableFuture<List<PlayerProfile>> getLeaderboard(String category, int limit) {
        return plugin.getStorageManager().getTopPlayers(category, limit);
    }
    
    /**
     * Get cached leaderboard
     */
    public List<PlayerProfile> getCachedLeaderboard(String category) {
        return leaderboards.getOrDefault(category, new java.util.ArrayList<>());
    }
    
    /**
     * Update leaderboard
     */
    public CompletableFuture<Void> updateLeaderboard(String category) {
        return getLeaderboard(category, 10).thenAccept(players -> {
            leaderboards.put(category, players);
            lastUpdate.put(category, System.currentTimeMillis());
        });
    }
    
    /**
     * Update all leaderboards
     */
    public CompletableFuture<Void> updateAllLeaderboards() {
        List<CompletableFuture<Void>> futures = new java.util.ArrayList<>();
        
        for (String category : leaderboards.keySet()) {
            futures.add(updateLeaderboard(category));
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
    
    /**
     * Display leaderboard to player
     */
    public void displayLeaderboard(Player player, String category, int limit) {
        getLeaderboard(category, limit).thenAccept(players -> {
            if (players == null || players.isEmpty()) {
                player.sendMessage(ChatUtils.colorize("&cNo data available for " + category + " leaderboard!"));
                return;
            }
            
            // Send header
            player.sendMessage(ChatUtils.colorize("&6&l" + getCategoryDisplayName(category) + " Leaderboard"));
            player.sendMessage(ChatUtils.colorize("&7&m-------------------"));
            
            // Send entries
            for (int i = 0; i < Math.min(players.size(), limit); i++) {
                PlayerProfile profile = players.get(i);
                int position = i + 1;
                String positionColor = getPositionColor(position);
                
                String entry = String.format("&f%d. %s%s &7- &f%d",
                    position,
                    positionColor,
                    profile.getName(),
                    getCategoryValue(profile, category)
                );
                
                player.sendMessage(ChatUtils.colorize(entry));
            }
            
            player.sendMessage(ChatUtils.colorize("&7&m-------------------"));
        });
    }
    
    /**
     * Get category display name
     */
    private String getCategoryDisplayName(String category) {
        switch (category.toLowerCase()) {
            case "wins": return "Wins";
            case "losses": return "Losses";
            case "kills": return "Kills";
            case "deaths": return "Deaths";
            case "level": return "Level";
            case "win_streak": return "Win Streak";
            case "best_win_streak": return "Best Win Streak";
            default: return category;
        }
    }
    
    /**
     * Get category value from profile
     */
    private int getCategoryValue(PlayerProfile profile, String category) {
        switch (category.toLowerCase()) {
            case "wins": return profile.getWins();
            case "losses": return profile.getLosses();
            case "kills": return profile.getKills();
            case "deaths": return profile.getDeaths();
            case "level": return profile.getLevel();
            case "win_streak": return profile.getWinStreak();
            case "best_win_streak": return profile.getBestWinStreak();
            default: return 0;
        }
    }
    
    /**
     * Get position color
     */
    private String getPositionColor(int position) {
        switch (position) {
            case 1: return "&6&l"; // Gold
            case 2: return "&7&l"; // Silver
            case 3: return "&c&l"; // Bronze
            default: return "&f"; // White
        }
    }
    
    /**
     * Get player rank in category
     */
    public CompletableFuture<Integer> getPlayerRank(Player player, String category) {
        return getLeaderboard(category, 1000).thenApply(players -> {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getUuid().equals(player.getUniqueId())) {
                    return i + 1;
                }
            }
            return -1; // Not found
        });
    }
    
    /**
     * Get player position in leaderboard
     */
    public CompletableFuture<String> getPlayerPosition(Player player, String category) {
        return getPlayerRank(player, category).thenApply(rank -> {
            if (rank == -1) {
                return "&cNot ranked";
            }
            
            String positionColor = getPositionColor(rank);
            return positionColor + "#" + rank;
        });
    }
    
    /**
     * Get leaderboard statistics
     */
    public Map<String, Object> getLeaderboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total_categories", leaderboards.size());
        stats.put("cached_leaderboards", leaderboards.size());
        
        int totalPlayers = 0;
        for (List<PlayerProfile> players : leaderboards.values()) {
            totalPlayers += players.size();
        }
        stats.put("total_players", totalPlayers);
        
        return stats;
    }
    
    /**
     * Check if leaderboard needs update
     */
    public boolean needsUpdate(String category) {
        long lastUpdateTime = lastUpdate.getOrDefault(category, 0L);
        long currentTime = System.currentTimeMillis();
        long updateInterval = 300000; // 5 minutes
        
        return (currentTime - lastUpdateTime) > updateInterval;
    }
    
    /**
     * Get last update time
     */
    public long getLastUpdateTime(String category) {
        return lastUpdate.getOrDefault(category, 0L);
    }
    
    /**
     * Clear leaderboard cache
     */
    public void clearCache() {
        leaderboards.clear();
        lastUpdate.clear();
        initializeCategories();
    }
    
    /**
     * Get available categories
     */
    public List<String> getAvailableCategories() {
        return new java.util.ArrayList<>(leaderboards.keySet());
    }
    
    /**
     * Check if category exists
     */
    public boolean hasCategory(String category) {
        return leaderboards.containsKey(category);
    }
    
    /**
     * Get leaderboard size
     */
    public int getLeaderboardSize(String category) {
        return leaderboards.getOrDefault(category, new java.util.ArrayList<>()).size();
    }
    
    /**
     * Shutdown leaderboard manager
     */
    public void shutdown() {
        clearCache();
    }
}
