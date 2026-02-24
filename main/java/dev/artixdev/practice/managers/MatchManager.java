package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.enums.PlayerState;
import dev.artixdev.practice.utils.Messages;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Match Manager
 * Handles all match-related operations
 */
public class MatchManager {
    
    private final Main plugin;
    private final Map<UUID, Match> matches;
    private final Map<UUID, BukkitTask> matchTasks;
    private final Map<UUID, Match> spectators;

    public MatchManager(Main plugin) {
        this.plugin = plugin;
        this.matches = new ConcurrentHashMap<>();
        this.matchTasks = new ConcurrentHashMap<>();
        this.spectators = new ConcurrentHashMap<>();
    }
    
    public void initialize() {
        plugin.getLogger().info("Initializing MatchManager...");
        
        // Load active matches from storage
        loadActiveMatches();
        
        // Start match cleanup task
        startMatchCleanupTask();
        
        plugin.getLogger().info("MatchManager initialized successfully!");
    }
    
    /**
     * Create a new match between two players
     */
    public Match createMatch(Player player1, Player player2, KitType kitType) {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId, player1, player2, kitType);
        int warmupSeconds = dev.artixdev.practice.configs.SettingsConfig.MATCH_SPAWN_PROTECTION > 0
            ? dev.artixdev.practice.configs.SettingsConfig.MATCH_SPAWN_PROTECTION
            : 5;
        match.setWarmupEndTime(System.currentTimeMillis() + warmupSeconds * 1000L);
        matches.put(matchId, match);
        
        // Save match to storage
        plugin.getStorageManager().saveMatch(match);
        
        // Set player states
        PlayerProfile profile1 = plugin.getPlayerManager().getPlayerProfile(player1);
        PlayerProfile profile2 = plugin.getPlayerManager().getPlayerProfile(player2);
        
        if (profile1 != null) {
            profile1.setState(PlayerState.FIGHTING);
        }
        if (profile2 != null) {
            profile2.setState(PlayerState.FIGHTING);
        }
        
        // Start match task
        startMatchTask(match);
        
        // Send match start messages
        sendMatchStartMessages(match);
        
        plugin.getLogger().info("Created match " + matchId + " between " + player1.getName() + " and " + player2.getName());
        
        return match;
    }
    
    /**
     * End a match
     */
    public void endMatch(UUID matchId, Player winner, Player loser) {
        Match match = matches.get(matchId);
        if (match == null) {
            return;
        }
        
        // Set match end time and winner/loser
        match.setEndTime(System.currentTimeMillis());
        match.setEnded(true);
        match.setWinner(winner);
        match.setLoser(loser);
        
        // Update player profiles
        updatePlayerProfiles(winner, loser);
        
        // Send match end messages
        sendMatchEndMessages(match);
        
        // Teleport players back to spawn
        plugin.getPlayerManager().teleportToSpawn(winner);
        plugin.getPlayerManager().teleportToSpawn(loser);
        
        // Set player states back to lobby
        PlayerProfile profile1 = plugin.getPlayerManager().getPlayerProfile(winner);
        PlayerProfile profile2 = plugin.getPlayerManager().getPlayerProfile(loser);
        
        if (profile1 != null) {
            profile1.setState(PlayerState.LOBBY);
        }
        if (profile2 != null) {
            profile2.setState(PlayerState.LOBBY);
        }
        
        // Cancel match task
        BukkitTask task = matchTasks.remove(matchId);
        if (task != null) {
            task.cancel();
        }
        
        // Save match to storage
        plugin.getStorageManager().saveMatch(match);
        
        // Remove from active matches
        matches.remove(matchId);
        
        plugin.getLogger().info("Ended match " + matchId + " - Winner: " + winner.getName() + ", Loser: " + loser.getName());
    }
    
    /**
     * Get a match by ID
     */
    public Match getMatch(UUID matchId) {
        return matches.get(matchId);
    }
    
    /**
     * Get a match by player
     */
    public Match getMatchByPlayer(Player player) {
        for (Match match : matches.values()) {
            if (match.getPlayer1() != null && match.getPlayer1().equals(player)) {
                return match;
            }
            if (match.getPlayer2() != null && match.getPlayer2().equals(player)) {
                return match;
            }
        }
        return null;
    }

    /**
     * Get a "current" match when no player context is available.
     * Returns the first active (non-ended) match, or null if none.
     * Prefer {@link #getMatchByPlayer(Player)} when a player is available.
     */
    public Match getCurrentMatch() {
        for (Match match : matches.values()) {
            if (match != null && !match.isEnded()) {
                return match;
            }
        }
        return null;
    }
    
    /**
     * Check if a player is in a match
     */
    public boolean isPlayerInMatch(Player player) {
        return getMatchByPlayer(player) != null;
    }
    
    /**
     * Check if a location is within an active match
     * This checks if the location is within an arena that has an active match
     */
    public boolean isLocationInMatch(Location location) {
        if (location == null) {
            return false;
        }
        
        // Check if location is in an arena (matches occur in arenas)
        if (!plugin.getArenaManager().isInArena(location)) {
            return false;
        }
        
        // Check if there are any active matches
        // Since matches occur in arenas, if location is in an arena and there are active matches,
        // we can assume it's likely in a match area
        if (matches.isEmpty()) {
            return false;
        }
        
        // For more accuracy, check if any match player is near this location
        // or if the location is within the arena bounds of an active match
        for (Match match : matches.values()) {
            if (match.isEnded()) {
                continue;
            }
            
            // Check if either player in the match is near this location
            if (match.getPlayer1() != null && match.getPlayer1().isOnline()) {
                Location player1Loc = match.getPlayer1().getLocation();
                if (player1Loc.getWorld().equals(location.getWorld())) {
                    double distance = player1Loc.distance(location);
                    // If within reasonable match distance (e.g., 100 blocks)
                    if (distance < 100) {
                        return true;
                    }
                }
            }
            
            if (match.getPlayer2() != null && match.getPlayer2().isOnline()) {
                Location player2Loc = match.getPlayer2().getLocation();
                if (player2Loc.getWorld().equals(location.getWorld())) {
                    double distance = player2Loc.distance(location);
                    // If within reasonable match distance (e.g., 100 blocks)
                    if (distance < 100) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * End all active matches
     */
    public void endAllMatches() {
        plugin.getLogger().info("Ending all matches...");
        
        List<UUID> matchIds = new ArrayList<>(matches.keySet());
        for (UUID matchId : matchIds) {
            Match match = matches.get(matchId);
            if (match != null) {
                // End match without winner (disconnect)
                match.setEndTime(System.currentTimeMillis());
                match.setEnded(true);
                
                // Set players back to lobby
                if (match.getPlayer1() != null) {
                    PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(match.getPlayer1());
                    if (profile != null) {
                        profile.setState(PlayerState.LOBBY);
                    }
                    plugin.getPlayerManager().teleportToSpawn(match.getPlayer1());
                }
                
                if (match.getPlayer2() != null) {
                    PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(match.getPlayer2());
                    if (profile != null) {
                        profile.setState(PlayerState.LOBBY);
                    }
                    plugin.getPlayerManager().teleportToSpawn(match.getPlayer2());
                }
                
                // Cancel match task
                BukkitTask task = matchTasks.remove(matchId);
                if (task != null) {
                    task.cancel();
                }
                
                // Save match
                plugin.getStorageManager().saveMatch(match);
            }
        }
        
        matches.clear();
        plugin.getLogger().info("All matches ended!");
    }

    /**
     * Handle a player disconnecting from the server.
     * If the player is in an active match, end that match and
     * declare the remaining player as the winner.
     *
     * @param player the disconnecting player
     */
    public void handlePlayerDisconnect(Player player) {
        if (player == null) {
            return;
        }
        spectators.remove(player.getUniqueId());

        Match match = getMatchByPlayer(player);
        if (match == null) {
            return;
        }

        Player winner = match.getPlayer1() != null && match.getPlayer1().equals(player)
                ? match.getPlayer2()
                : match.getPlayer1();

        // If for some reason we don't have an opponent, just end the match without winner/loser logic
        if (winner == null) {
            match.setEndTime(System.currentTimeMillis());
            match.setEnded(true);
            UUID matchId = match.getId();
            if (matchId != null) {
                BukkitTask task = matchTasks.remove(matchId);
                if (task != null) {
                    task.cancel();
                }
                matches.remove(matchId);
            }
            return;
        }

        UUID matchId = match.getId();
        if (matchId != null) {
            endMatch(matchId, winner, player);
        }
    }

    /**
     * Handle a player dying during a match.
     * For now this uses the same logic as a disconnect:
     * the opponent is treated as the winner and the match is ended.
     *
     * @param player the player who died
     */
    public void handlePlayerDeath(Player player) {
        handlePlayerDisconnect(player);
    }
    
    /**
     * Start a match task for monitoring
     */
    private void startMatchTask(Match match) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // Check if match is still active
                if (!matches.containsKey(match.getId())) {
                    this.cancel();
                    return;
                }
                
                // Check if players are still online
                if (match.getPlayer1() == null || !match.getPlayer1().isOnline()) {
                    endMatch(match.getId(), match.getPlayer2(), match.getPlayer1());
                    return;
                }
                
                if (match.getPlayer2() == null || !match.getPlayer2().isOnline()) {
                    endMatch(match.getId(), match.getPlayer1(), match.getPlayer2());
                    return;
                }
                
                // Check match duration
                long duration = System.currentTimeMillis() - match.getStartTime();
                long maxDuration = plugin.getSettingsConfig().getMaxMatchDuration() * 1000L;
                
                if (duration > maxDuration) {
                    // End match due to timeout
                    endMatch(match.getId(), null, null);
                    return;
                }
                
                // Send match progress messages
                sendMatchProgressMessages(match);
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second
        
        matchTasks.put(match.getId(), task);
    }
    
    /**
     * Start match cleanup task
     */
    private void startMatchCleanupTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Clean up old matches
                List<UUID> toRemove = new ArrayList<>();
                
                for (Map.Entry<UUID, Match> entry : matches.entrySet()) {
                    Match match = entry.getValue();
                    long duration = System.currentTimeMillis() - match.getStartTime();
                    
                    // Remove matches older than 1 hour
                    if (duration > 3600000) {
                        toRemove.add(entry.getKey());
                    }
                }
                
                for (UUID matchId : toRemove) {
                    matches.remove(matchId);
                    BukkitTask task = matchTasks.remove(matchId);
                    if (task != null) {
                        task.cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 6000L, 6000L); // Run every 5 minutes
    }
    
    /**
     * Load active matches from storage
     */
    private void loadActiveMatches() {
        plugin.getStorageManager().loadAllMatches().thenAccept(matchesList -> {
            for (Match match : matchesList) {
                if (!match.isEnded()) {
                    matches.put(match.getId(), match);
                    startMatchTask(match);
                }
            }
        });
    }
    
    /**
     * Update player profiles after match
     */
    private void updatePlayerProfiles(Player winner, Player loser) {
        PlayerProfile winnerProfile = plugin.getPlayerManager().getPlayerProfile(winner);
        PlayerProfile loserProfile = plugin.getPlayerManager().getPlayerProfile(loser);
        
        if (winnerProfile != null) {
            winnerProfile.addWin();
            winnerProfile.addKill();
        }
        
        if (loserProfile != null) {
            loserProfile.addLoss();
            loserProfile.addDeath();
        }
    }
    
    /**
     * Send match start messages
     */
    private void sendMatchStartMessages(Match match) {
        String kitName = match.getKitType() != null ? match.getKitType().getDisplayName() : "Unknown";
        if (match.getPlayer1() != null) {
            match.getPlayer1().sendMessage(Messages.get("MATCH.STARTED_TITLE"));
            match.getPlayer1().sendMessage(Messages.get("MATCH.OPPONENT", "opponent", match.getPlayer2().getName()));
            match.getPlayer1().sendMessage(Messages.get("MATCH.KIT", "kit", kitName));
        }
        if (match.getPlayer2() != null) {
            match.getPlayer2().sendMessage(Messages.get("MATCH.STARTED_TITLE"));
            match.getPlayer2().sendMessage(Messages.get("MATCH.OPPONENT", "opponent", match.getPlayer1().getName()));
            match.getPlayer2().sendMessage(Messages.get("MATCH.KIT", "kit", kitName));
        }
    }
    
    /**
     * Send match end messages
     */
    private void sendMatchEndMessages(Match match) {
        if (match.getWinner() != null && match.getLoser() != null) {
            match.getWinner().sendMessage(Messages.get("MATCH.YOU_WON"));
            match.getLoser().sendMessage(Messages.get("MATCH.YOU_LOST"));
        }
    }
    
    /**
     * Send match progress messages
     */
    private void sendMatchProgressMessages(Match match) {
        long duration = System.currentTimeMillis() - match.getStartTime();
        long minutes = duration / 60000;
        long seconds = (duration % 60000) / 1000;
        
        String timeString = String.format("%d:%02d", minutes, seconds);
        
        if (match.getPlayer1() != null) {
            match.getPlayer1().sendMessage(Messages.get("MATCH.TIME", "time", timeString));
        }
        if (match.getPlayer2() != null) {
            match.getPlayer2().sendMessage(Messages.get("MATCH.TIME", "time", timeString));
        }
    }
    
    public Map<UUID, Match> getMatches() {
        return matches;
    }
    
    public int getActiveMatchCount() {
        return matches.size();
    }

    public void addSpectator(Player player, Match match) {
        if (player == null || match == null) return;
        spectators.put(player.getUniqueId(), match);
        if (match.getLocation() != null) {
            player.teleport(match.getLocation());
        }
        String msg = Messages.get("MATCH.SPECTATING");
        if (msg == null) msg = "§7Spectating match.";
        player.sendMessage(Messages.getPrefix() + " " + msg);
    }

    public void stopSpectating(Player player) {
        Match match = spectators.remove(player.getUniqueId());
        if (match != null) {
            plugin.getPlayerManager().teleportToSpawn(player);
            player.sendMessage(Messages.getPrefix() + " §7You are no longer spectating.");
        }
    }

    public boolean isSpectating(Player player) {
        return spectators.containsKey(player.getUniqueId());
    }

    public List<Match> getOngoingMatches() {
        return new ArrayList<>(matches.values());
    }
}
