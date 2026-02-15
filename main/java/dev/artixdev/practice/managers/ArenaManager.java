package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.LocationUtils;
import dev.artixdev.practice.utils.cuboid.Cuboid;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
 * Arena Manager
 * Handles all arena-related operations
 */
public class ArenaManager {
    
    private final Main plugin;
    private final Map<UUID, Arena> arenas;
    private final Map<KitType, List<Arena>> arenasByKit;
    private BukkitTask arenaTask;
    
    public ArenaManager(Main plugin) {
        this.plugin = plugin;
        this.arenas = new ConcurrentHashMap<>();
        this.arenasByKit = new ConcurrentHashMap<>();
    }
    
    public void initialize() {
        plugin.getLogger().info("Initializing ArenaManager...");
        
        // Load arenas from storage
        loadArenas();
        
        // Start arena task
        startArenaTask();
        
        plugin.getLogger().info("ArenaManager initialized successfully!");
    }
    
    /**
     * Create a new arena
     */
    public Arena createArena(String name, KitType kitType) {
        UUID arenaId = UUID.randomUUID();
        Arena arena = new Arena(arenaId, name);
        arena.setKitType(kitType);
        arena.setEnabled(true);
        arena.setRanked(false);
        
        arenas.put(arenaId, arena);
        
        // Add to kit arenas
        if (!arenasByKit.containsKey(kitType)) {
            arenasByKit.put(kitType, new ArrayList<>());
        }
        arenasByKit.get(kitType).add(arena);
        
        // Save to storage
        plugin.getStorageManager().saveArena(arena);
        
        plugin.getLogger().info("Created arena: " + name + " for kit: " + kitType.getDisplayName());
        
        return arena;
    }
    
    /**
     * Delete an arena
     */
    public boolean deleteArena(UUID arenaId) {
        Arena arena = arenas.get(arenaId);
        if (arena == null) {
            return false;
        }
        
        // Remove from maps
        arenas.remove(arenaId);
        
        if (arenasByKit.containsKey(arena.getKitType())) {
            arenasByKit.get(arena.getKitType()).remove(arena);
        }
        
        // Delete from storage
        plugin.getStorageManager().deleteArena(arenaId);
        
        plugin.getLogger().info("Deleted arena: " + arena.getName());
        
        return true;
    }
    
    /**
     * Get arena by ID
     */
    public Arena getArena(UUID arenaId) {
        return arenas.get(arenaId);
    }
    
    /**
     * Get arena by name
     */
    public Arena getArenaByName(String name) {
        for (Arena arena : arenas.values()) {
            if (arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }
    
    /**
     * Get random arena for kit type
     */
    public Arena getRandomArena(KitType kitType) {
        List<Arena> kitArenas = arenasByKit.get(kitType);
        if (kitArenas == null || kitArenas.isEmpty()) {
            return null;
        }
        
        // Filter enabled arenas
        List<Arena> enabledArenas = new ArrayList<>();
        for (Arena arena : kitArenas) {
            if (arena.isEnabled()) {
                enabledArenas.add(arena);
            }
        }
        
        if (enabledArenas.isEmpty()) {
            return null;
        }
        
        // Return random arena
        Random random = new Random();
        return enabledArenas.get(random.nextInt(enabledArenas.size()));
    }
    
    /**
     * Get all arenas for kit type
     */
    public List<Arena> getArenasForKit(KitType kitType) {
        return arenasByKit.getOrDefault(kitType, new ArrayList<>());
    }
    
    /**
     * Get enabled arenas for kit type
     */
    public List<Arena> getEnabledArenasForKit(KitType kitType) {
        List<Arena> enabledArenas = new ArrayList<>();
        List<Arena> kitArenas = arenasByKit.get(kitType);
        
        if (kitArenas != null) {
            for (Arena arena : kitArenas) {
                if (arena.isEnabled()) {
                    enabledArenas.add(arena);
                }
            }
        }
        
        return enabledArenas;
    }
    
    /**
     * Set arena spawn points
     */
    public boolean setArenaSpawn(Arena arena, int spawnNumber, Location location) {
        if (spawnNumber == 1) {
            arena.setSpawn1(location);
        } else if (spawnNumber == 2) {
            arena.setSpawn2(location);
        } else {
            return false;
        }
        
        // Save to storage
        plugin.getStorageManager().saveArena(arena);
        
        return true;
    }
    
    /**
     * Set arena boundaries
     */
    public boolean setArenaBoundaries(Arena arena, Location min, Location max) {
        arena.setMin(min);
        arena.setMax(max);
        
        // Save to storage
        plugin.getStorageManager().saveArena(arena);
        
        return true;
    }
    
    /**
     * Enable/disable arena
     */
    public boolean setArenaEnabled(UUID arenaId, boolean enabled) {
        Arena arena = arenas.get(arenaId);
        if (arena == null) {
            return false;
        }
        
        arena.setEnabled(enabled);
        
        // Save to storage
        plugin.getStorageManager().saveArena(arena);
        
        return true;
    }
    
    /**
     * Set arena ranked status
     */
    public boolean setArenaRanked(UUID arenaId, boolean ranked) {
        Arena arena = arenas.get(arenaId);
        if (arena == null) {
            return false;
        }
        
        arena.setRanked(ranked);
        
        // Save to storage
        plugin.getStorageManager().saveArena(arena);
        
        return true;
    }
    
    /**
     * Check if arena is valid for matches
     */
    public boolean isArenaValid(Arena arena) {
        if (arena == null) {
            return false;
        }
        
        if (!arena.isEnabled()) {
            return false;
        }
        
        if (arena.getSpawn1() == null || arena.getSpawn2() == null) {
            return false;
        }
        
        if (arena.getMin() == null || arena.getMax() == null) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Get arena statistics
     */
    public Map<String, Object> getArenaStatistics() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        
        int totalArenas = arenas.size();
        int enabledArenas = 0;
        int rankedArenas = 0;
        
        for (Arena arena : arenas.values()) {
            if (arena.isEnabled()) {
                enabledArenas++;
            }
            if (arena.isRanked()) {
                rankedArenas++;
            }
        }
        
        stats.put("total", totalArenas);
        stats.put("enabled", enabledArenas);
        stats.put("disabled", totalArenas - enabledArenas);
        stats.put("ranked", rankedArenas);
        stats.put("unranked", totalArenas - rankedArenas);
        
        return stats;
    }
    
    /**
     * Start arena task for maintenance
     */
    private void startArenaTask() {
        arenaTask = new BukkitRunnable() {
            @Override
            public void run() {
                // Check for invalid arenas
                List<UUID> toRemove = new ArrayList<>();
                
                for (Arena arena : arenas.values()) {
                    if (!isArenaValid(arena)) {
                        plugin.getLogger().warning("Arena " + arena.getName() + " is invalid and will be disabled");
                        arena.setEnabled(false);
                        plugin.getStorageManager().saveArena(arena);
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 6000L, 6000L); // Run every 5 minutes
    }
    
    /**
     * Load arenas from storage
     */
    private void loadArenas() {
        plugin.getStorageManager().loadAllArenas().thenAccept(arenasList -> {
            for (Arena arena : arenasList) {
                arenas.put(arena.getId(), arena);
                
                // Add to kit arenas
                if (!arenasByKit.containsKey(arena.getKitType())) {
                    arenasByKit.put(arena.getKitType(), new ArrayList<>());
                }
                arenasByKit.get(arena.getKitType()).add(arena);
            }
        });
    }
    
    /**
     * Save all arenas
     */
    public void saveAllArenas() {
        plugin.getLogger().info("Saving all arenas...");
        
        for (Arena arena : arenas.values()) {
            plugin.getStorageManager().saveArena(arena);
        }
        
        plugin.getLogger().info("All arenas saved!");
    }
    
    /**
     * Get all arenas
     */
    public Map<UUID, Arena> getArenas() {
        return arenas;
    }
    
    /**
     * Get arenas by kit
     */
    public Map<KitType, List<Arena>> getArenasByKit() {
        return arenasByKit;
    }
    
    /**
     * Get total arena count
     */
    public int getTotalArenaCount() {
        return arenas.size();
    }
    
    /**
     * Get enabled arena count
     */
    public int getEnabledArenaCount() {
        int count = 0;
        for (Arena arena : arenas.values()) {
            if (arena.isEnabled()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check if a location is within any arena bounds
     */
    public boolean isInArena(Location location) {
        if (location == null) {
            return false;
        }
        
        for (Arena arena : arenas.values()) {
            if (!arena.isEnabled()) {
                continue;
            }
            
            Cuboid bounds = arena.getBounds();
            if (bounds != null && bounds.contains(location)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get arena at location
     */
    public Arena getArenaAt(Location location) {
        if (location == null) {
            return null;
        }
        
        for (Arena arena : arenas.values()) {
            if (!arena.isEnabled()) {
                continue;
            }
            
            Cuboid bounds = arena.getBounds();
            if (bounds != null && bounds.contains(location)) {
                return arena;
            }
        }
        
        return null;
    }
    
    /**
     * Shutdown arena manager
     */
    public void shutdown() {
        if (arenaTask != null) {
            arenaTask.cancel();
        }
        
        saveAllArenas();
    }
}
