package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.enums.PlayerState;
import dev.artixdev.practice.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Queue Manager
 * Handles all queue-related operations
 */
public class QueueManager {
    
    private final Main plugin;
    private final Map<UUID, Queue> queues;
    private final Map<KitType, Queue> kitQueues;
    private final Map<Player, Queue> playerQueues;
    private BukkitTask matchmakingTask;
    
    public QueueManager(Main plugin) {
        this.plugin = plugin;
        this.queues = new ConcurrentHashMap<>();
        this.kitQueues = new ConcurrentHashMap<>();
        this.playerQueues = new ConcurrentHashMap<>();
    }
    
    public void initialize() {
        plugin.getLogger().info("Initializing QueueManager...");
        
        // Initialize kit queues
        for (KitType kitType : KitType.values()) {
            if (kitType != KitType.CUSTOM) {
                Queue queue = new Queue(UUID.randomUUID(), kitType, false);
                kitQueues.put(kitType, queue);
                queues.put(queue.getId(), queue);
            }
        }
        
        // Start matchmaking task
        startMatchmakingTask();
        
        plugin.getLogger().info("QueueManager initialized successfully!");
    }
    
    /**
     * Add a player to a queue (with EventType and KitType)
     */
    public boolean addPlayerToQueue(Player player, EventType eventType, KitType kitType) {
        // Determine if ranked based on event type
        boolean ranked = eventType == EventType.RANKED;
        return addPlayerToQueue(player, kitType, ranked);
    }
    
    /**
     * Add a player to a queue
     */
    public boolean addPlayerToQueue(Player player, KitType kitType, boolean ranked) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            return false;
        }
        
        // Check if player is already in a queue
        if (playerQueues.containsKey(player)) {
            player.sendMessage(ChatUtils.colorize("&cYou are already in a queue!"));
            return false;
        }
        
        // Check if player is in a match
        if (profile.getState() == PlayerState.FIGHTING) {
            player.sendMessage(ChatUtils.colorize("&cYou are already in a match!"));
            return false;
        }
        
        // Get or create queue
        Queue queue = getOrCreateQueue(kitType, ranked);
        if (queue == null) {
            player.sendMessage(ChatUtils.colorize("&cFailed to join queue!"));
            return false;
        }
        
        // Add player to queue
        queue.addPlayer(player);
        playerQueues.put(player, queue);
        
        // Set player state
        profile.setState(PlayerState.QUEUE);
        
        // Send confirmation message
        player.sendMessage(ChatUtils.colorize("&aJoined " + kitType.getName() + " queue!"));
        player.sendMessage(ChatUtils.colorize("&7Players in queue: &f" + queue.getSize()));
        
        // Save queue to storage
        plugin.getStorageManager().saveQueue(queue);
        
        plugin.getLogger().info("Player " + player.getName() + " joined " + kitType.getName() + " queue");
        
        return true;
    }
    
    /**
     * Remove a player from queue
     */
    public boolean removePlayerFromQueue(Player player) {
        Queue queue = playerQueues.get(player);
        if (queue == null) {
            return false;
        }
        
        // Remove player from queue
        queue.removePlayer(player);
        playerQueues.remove(player);
        
        // Set player state back to lobby
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile != null) {
            profile.setState(PlayerState.LOBBY);
        }
        
        // Send confirmation message
        player.sendMessage(ChatUtils.colorize("&cLeft the queue!"));
        
        // Save queue to storage
        plugin.getStorageManager().saveQueue(queue);
        
        plugin.getLogger().info("Player " + player.getName() + " left the queue");
        
        return true;
    }
    
    /**
     * Get or create a queue for a kit type
     */
    private Queue getOrCreateQueue(KitType kitType, boolean ranked) {
        if (ranked) {
            // For ranked matches, create separate queues
            for (Queue queue : queues.values()) {
                if (queue.getKitType() == kitType && queue.isRanked() == ranked) {
                    return queue;
                }
            }
            
            // Create new ranked queue
            Queue queue = new Queue(UUID.randomUUID(), kitType, ranked);
            queues.put(queue.getId(), queue);
            return queue;
        } else {
            // For unranked matches, use kit queues
            return kitQueues.get(kitType);
        }
    }
    
    /**
     * Start matchmaking task
     */
    private void startMatchmakingTask() {
        matchmakingTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Queue queue : queues.values()) {
                    if (queue.getSize() >= 2) {
                        // Try to match players
                        List<Player> players = new ArrayList<>(queue.getPlayers());
                        if (players.size() >= 2) {
                            // Select two random players
                            Random random = new Random();
                            Player player1 = players.get(random.nextInt(players.size()));
                            players.remove(player1);
                            Player player2 = players.get(random.nextInt(players.size()));
                            
                            // Create match
                            createMatchFromQueue(queue, player1, player2);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second
    }
    
    /**
     * Create a match from queue
     */
    private void createMatchFromQueue(Queue queue, Player player1, Player player2) {
        // Remove players from queue
        queue.removePlayer(player1);
        queue.removePlayer(player2);
        playerQueues.remove(player1);
        playerQueues.remove(player2);
        
        // Create match
        plugin.getMatchManager().createMatch(player1, player2, queue.getKitType());
        
        // Save queue
        plugin.getStorageManager().saveQueue(queue);
        
        plugin.getLogger().info("Created match from queue: " + player1.getName() + " vs " + player2.getName());
    }
    
    /**
     * Get queue by player
     */
    public Queue getQueueByPlayer(Player player) {
        return playerQueues.get(player);
    }
    
    /**
     * Check if player is in queue
     */
    public boolean isPlayerInQueue(Player player) {
        return playerQueues.containsKey(player);
    }
    
    /**
     * Get queue size (with EventType and KitType)
     */
    public int getQueueSize(EventType eventType, KitType kitType) {
        boolean ranked = eventType == EventType.RANKED;
        return getQueueSize(kitType, ranked);
    }
    
    /**
     * Get queue size for a kit type
     */
    public int getQueueSize(KitType kitType, boolean ranked) {
        for (Queue queue : queues.values()) {
            if (queue.getKitType() == kitType && queue.isRanked() == ranked) {
                return queue.getSize();
            }
        }
        return 0;
    }
    
    /**
     * Check if a queue is active for the given event type and kit type
     */
    public boolean isQueueActive(EventType eventType, KitType kitType) {
        boolean ranked = eventType == EventType.RANKED;
        return getQueueSize(kitType, ranked) > 0;
    }
    
    /**
     * Get total players in all queues
     */
    public int getTotalQueueSize() {
        int total = 0;
        for (Queue queue : queues.values()) {
            total += queue.getSize();
        }
        return total;
    }
    
    /**
     * Clear all queues
     */
    public int clearAllQueues() {
        plugin.getLogger().info("Clearing all queues...");
        
        // Count queues before clearing
        int queueCount = queues.size() + kitQueues.size();
        
        // Remove all players from queues
        for (Player player : new ArrayList<>(playerQueues.keySet())) {
            removePlayerFromQueue(player);
        }
        
        // Clear queue maps
        queues.clear();
        kitQueues.clear();
        playerQueues.clear();
        
        // Cancel matchmaking task
        if (matchmakingTask != null) {
            matchmakingTask.cancel();
        }
        
        plugin.getLogger().info("All queues cleared!");
        return queueCount;
    }
    
    /**
     * Get all queues
     */
    public Map<UUID, Queue> getQueues() {
        return queues;
    }
    
    /**
     * Get kit queues
     */
    public Map<KitType, Queue> getKitQueues() {
        return kitQueues;
    }
    
    /**
     * Get player queues
     */
    public Map<Player, Queue> getPlayerQueues() {
        return playerQueues;
    }
    
    /**
     * Shutdown queue manager
     */
    public void shutdown() {
        if (matchmakingTask != null) {
            matchmakingTask.cancel();
        }
        
        clearAllQueues();
    }
}
