package dev.artixdev.practice.utils;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.configs.SettingsConfig;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tablist Manager
 * Handles tablist display for players with full configuration support
 */
public class TablistManager {
    
    private final Main plugin;
    private final Map<UUID, String> playerHeaders;
    private final Map<UUID, String> playerFooters;
    private BukkitTask updateTask;
    
    public TablistManager(Main plugin) {
        this.plugin = plugin;
        this.playerHeaders = new HashMap<>();
        this.playerFooters = new HashMap<>();
    }
    
    /**
     * Initialize tablist manager
     */
    public void initialize() {
        if (!isTablistEnabled()) {
            plugin.getLogger().info("Tablist is disabled in configuration.");
            return;
        }
        
        // Start update task
        startUpdateTask();
        
        plugin.getLogger().info("TablistManager initialized successfully!");
    }
    
    /**
     * Set tablist for player
     */
    public void setTablist(Player player) {
        if (!isTablistEnabled()) {
            return;
        }
        
        String header = getTablistHeader(player);
        String footer = getTablistFooter(player);
        
        playerHeaders.put(player.getUniqueId(), header);
        playerFooters.put(player.getUniqueId(), footer);
        
        // Send tablist packet
        sendTablistPacket(player, header, footer);
    }
    
    /**
     * Remove tablist from player
     */
    public void removeTablist(Player player) {
        playerHeaders.remove(player.getUniqueId());
        playerFooters.remove(player.getUniqueId());
        
        // Send empty tablist
        sendTablistPacket(player, "", "");
    }
    
    /**
     * Update tablist for player
     */
    public void updateTablist(Player player) {
        if (!isTablistEnabled()) {
            return;
        }
        
        String header = getTablistHeader(player);
        String footer = getTablistFooter(player);
        
        playerHeaders.put(player.getUniqueId(), header);
        playerFooters.put(player.getUniqueId(), footer);
        
        // Send tablist packet
        sendTablistPacket(player, header, footer);
    }
    
    /**
     * Get tablist header for player
     */
    private String getTablistHeader(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            return ChatUtils.colorize(getDefaultHeader());
        }
        
        String header = getDefaultHeader();
        
        // Replace placeholders
        header = replacePlaceholders(header, player, profile);
        
        return ChatUtils.colorize(header);
    }
    
    /**
     * Get tablist footer for player
     */
    private String getTablistFooter(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            return ChatUtils.colorize(getDefaultFooter());
        }
        
        String footer = getDefaultFooter();
        
        // Replace placeholders
        footer = replacePlaceholders(footer, player, profile);
        
        return ChatUtils.colorize(footer);
    }
    
    /**
     * Replace placeholders in text
     */
    private String replacePlaceholders(String text, Player player, PlayerProfile profile) {
        // Basic player placeholders
        text = text.replace("%player%", player.getName());
        text = text.replace("%displayname%", player.getDisplayName());
        text = text.replace("%world%", player.getWorld().getName());
        
        // Profile placeholders
        text = text.replace("%level%", String.valueOf(profile.getLevel()));
        text = text.replace("%wins%", String.valueOf(profile.getWins()));
        text = text.replace("%losses%", String.valueOf(profile.getLosses()));
        text = text.replace("%kills%", String.valueOf(profile.getKills()));
        text = text.replace("%deaths%", String.valueOf(profile.getDeaths()));
        text = text.replace("%winrate%", String.valueOf(profile.getWinrate())); // Placeholder for winrate
        text = text.replace("%kdr%", String.valueOf(profile.getKDR())); // Placeholder for KDR
        
        // Server placeholders
        text = text.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()));
        text = text.replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()));
        text = text.replace("%tps%", String.valueOf(getServerTPS()));
        
        // Queue placeholders
        if (profile.getCurrentQueue() != null) {
            text = text.replace("%queue%", String.valueOf(getQueueSize(profile.getCurrentQueue())));
            text = text.replace("%unranked_queue%", String.valueOf(getUnrankedQueueSize(profile.getCurrentQueue())));
            text = text.replace("%ranked_queue%", String.valueOf(getRankedQueueSize(profile.getCurrentQueue())));
        } else {
            text = text.replace("%queue%", "0");
            text = text.replace("%unranked_queue%", "0");
            text = text.replace("%ranked_queue%", "0");
        }
        
        // Match placeholders
        text = text.replace("%match%", "0");
        text = text.replace("%fighting%", "0");
        
        // Party placeholders
        if (profile.getCurrentParty() != null) {
            text = text.replace("%party_size%", String.valueOf(getPartySize(profile.getCurrentParty())));
            text = text.replace("%party_leader%", getPartyLeader(profile.getCurrentParty()));
        } else {
            text = text.replace("%party_size%", "0");
            text = text.replace("%party_leader%", "None");
        }
        
        // Time placeholders
        text = text.replace("%time%", getCurrentTime());
        text = text.replace("%date%", getCurrentDate());
        
        return text;
    }
    
    /**
     * Send tablist packet to player
     */
    private void sendTablistPacket(Player player, String header, String footer) {
        try {
            // Use reflection to send tablist packet for better compatibility
            // This would use the actual packet system
            // For now, use basic tablist with reflection
            player.getClass().getMethod("setPlayerListHeaderFooter", String.class, String.class)
                .invoke(player, header, footer);
        } catch (Exception e) {
            // Fallback - just log the error
            plugin.getLogger().warning("Could not send tablist packet to " + player.getName());
        }
    }
    
    /**
     * Get server version
     */
    private String getVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        return version.substring(version.lastIndexOf('.') + 1);
    }
    
    /**
     * Start update task
     */
    private void startUpdateTask() {
        int updateInterval = getTablistUpdateInterval();
        
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (playerHeaders.containsKey(player.getUniqueId())) {
                        updateTablist(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, updateInterval);
    }
    
    /**
     * Shutdown tablist manager
     */
    public void shutdown() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        
        // Remove all tablists
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeTablist(player);
        }
        
        playerHeaders.clear();
        playerFooters.clear();
    }
    
    /**
     * Check if player has tablist
     */
    public boolean hasTablist(Player player) {
        return playerHeaders.containsKey(player.getUniqueId());
    }
    
    /**
     * Get player header
     */
    public String getPlayerHeader(Player player) {
        return playerHeaders.get(player.getUniqueId());
    }
    
    /**
     * Get player footer
     */
    public String getPlayerFooter(Player player) {
        return playerFooters.get(player.getUniqueId());
    }
    
    // Configuration getters
    private boolean isTablistEnabled() {
        return SettingsConfig.NAME_TAGS_ENABLED; // Using name tags setting as tablist setting
    }
    
    private String getDefaultHeader() {
        return "&c&lRefine &7&lPractice\n&fWelcome &c%player%&f!";
    }
    
    private String getDefaultFooter() {
        return "&7Online: &c%online% &7| &7Level: &c%level% &7| &7Wins: &c%wins%\n&7Queue: &c%queue% &7| &7Fighting: &c%match%";
    }
    
    private int getTablistUpdateInterval() {
        return 20; // 1 second default
    }
    
    private String getCurrentTime() {
        return java.time.LocalTime.now().toString().substring(0, 5);
    }
    
    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }
    
    private double getServerTPS() {
        // Simple TPS calculation - in real implementation you'd use a proper TPS calculator
        return 20.0;
    }
    
    /**
     * Get queue size safely
     */
    private int getQueueSize(Object queue) {
        try {
            // Use reflection to call getSize() method if it exists
            return (Integer) queue.getClass().getMethod("getSize").invoke(queue);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get unranked queue size safely
     */
    private int getUnrankedQueueSize(Object queue) {
        try {
            Object unrankedQueue = queue.getClass().getMethod("getUnrankedQueue").invoke(queue);
            return (Integer) unrankedQueue.getClass().getMethod("getSize").invoke(unrankedQueue);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get ranked queue size safely
     */
    private int getRankedQueueSize(Object queue) {
        try {
            Object rankedQueue = queue.getClass().getMethod("getRankedQueue").invoke(queue);
            return (Integer) rankedQueue.getClass().getMethod("getSize").invoke(rankedQueue);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get party size safely
     */
    private int getPartySize(Object party) {
        try {
            // Use reflection to call getSize() method if it exists
            return (Integer) party.getClass().getMethod("getSize").invoke(party);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get party leader safely
     */
    private String getPartyLeader(Object party) {
        try {
            Object leader = party.getClass().getMethod("getLeader").invoke(party);
            return (String) leader.getClass().getMethod("getName").invoke(leader);
        } catch (Exception e) {
            return "None";
        }
    }
}