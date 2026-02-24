package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.BotsConfig;
import dev.artixdev.practice.models.BotProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bot Manager
 * Handles bot creation, management, and configuration
 */
public class BotManager {
    
    private final Main plugin;
    private final Map<UUID, BotProfile> activeBots;
    private final Map<String, List<String>> botKits;
    private BukkitTask botUpdateTask;
    
    public BotManager(Main plugin) {
        this.plugin = plugin;
        this.activeBots = new ConcurrentHashMap<>();
        this.botKits = new HashMap<>();
    }
    
    /**
     * Initialize bot manager
     */
    public void initialize() {
        if (!BotsConfig.BOTS_ENABLED) {
            plugin.getLogger().info("Bots are disabled in configuration.");
            return;
        }
        
        // Load bot configurations
        loadBotConfigurations();
        
        // Start bot update task
        startBotUpdateTask();
        
        plugin.getLogger().info("BotManager initialized successfully!");
    }
    
    /**
     * Load bot configurations
     */
    private void loadBotConfigurations() {
        // Load default kits
        botKits.put("default", BotsConfig.DEFAULT_KITS);
        
        // Load speed PvP kits
        if (BotsConfig.SPEED_PVP_ENABLED) {
            botKits.put("speed_pvp", BotsConfig.SPEED_KITS);
        }
        
        // Load golden apple kits
        if (BotsConfig.GAPPLE_ENABLED) {
            botKits.put("gapple", BotsConfig.GAPPLE_KITS);
        }
        
        // Load UHC kits
        if (BotsConfig.UHC_ENABLED) {
            botKits.put("uhc", BotsConfig.UHC_KITS);
        }
        
        // Load soup kits
        if (BotsConfig.SOUP_ENABLED) {
            botKits.put("soup", BotsConfig.SOUP_KITS);
        }
    }
    
    /**
     * Create a bot
     */
    public BotProfile createBot(String kit, String mode) {
        if (!BotsConfig.BOTS_ENABLED) {
            return null;
        }
        
        // Get random bot name
        String botName = getRandomBotName();
        
        // Create bot profile
        BotProfile botProfile = new BotProfile(botName, kit, mode);
        
        // Configure bot based on mode
        configureBot(botProfile, mode);
        
        // Add to active bots
        activeBots.put(botProfile.getUuid(), botProfile);
        
        plugin.getLogger().info("Created bot: " + botName + " with kit: " + kit + " and mode: " + mode);
        
        return botProfile;
    }
    
    /**
     * Configure bot based on mode
     */
    private void configureBot(BotProfile botProfile, String mode) {
        switch (mode.toLowerCase()) {
            case "default":
                botProfile.setMovementSpeed(BotsConfig.NORMAL_MOVEMENT_SPEED);
                botProfile.setTryhard(false);
                break;
            case "speed_pvp":
                botProfile.setMovementSpeed(BotsConfig.TRY_HARD_MOVEMENT_SPEED);
                botProfile.setTryhard(true);
                botProfile.setFastPotions(BotsConfig.FAST_POTIONS);
                break;
            case "gapple":
                botProfile.setMovementSpeed(BotsConfig.NORMAL_MOVEMENT_SPEED);
                botProfile.setTryhard(false);
                break;
            case "uhc":
                botProfile.setMovementSpeed(BotsConfig.TRY_HARD_MOVEMENT_SPEED);
                botProfile.setTryhard(true);
                break;
            case "soup":
                botProfile.setMovementSpeed(BotsConfig.NORMAL_MOVEMENT_SPEED);
                botProfile.setTryhard(false);
                break;
            default:
                botProfile.setMovementSpeed(BotsConfig.NORMAL_MOVEMENT_SPEED);
                botProfile.setTryhard(false);
                break;
        }
        
        // Apply global settings
        botProfile.setAlternateHitFactor(BotsConfig.ALTERNATE_HIT_FACTOR);
        botProfile.setReduceRangeOnCombo(BotsConfig.REDUCE_RANGE_ON_COMBO);
        botProfile.setSpigotBasedKB(BotsConfig.SPIGOT_BASED_KB);
    }
    
    /**
     * Remove a bot
     */
    public void removeBot(UUID botUuid) {
        BotProfile botProfile = activeBots.remove(botUuid);
        if (botProfile != null) {
            // Remove bot from world if it exists
            Player botPlayer = Bukkit.getPlayer(botUuid);
            if (botPlayer != null) {
                botPlayer.kickPlayer("Bot removed");
            }
            
            plugin.getLogger().info("Removed bot: " + botProfile.getName());
        }
    }
    
    /**
     * Get random bot name
     */
    private String getRandomBotName() {
        List<String> names = BotsConfig.BOT_NAMES;
        if (names == null || names.isEmpty()) {
            return "Bot" + System.currentTimeMillis();
        }
        
        Random random = new Random();
        return names.get(random.nextInt(names.size()));
    }
    
    /**
     * Get bots for specific kit
     */
    public List<BotProfile> getBotsForKit(String kit) {
        List<BotProfile> bots = new ArrayList<>();
        
        for (BotProfile bot : activeBots.values()) {
            if (bot.getKit().equalsIgnoreCase(kit)) {
                bots.add(bot);
            }
        }
        
        return bots;
    }
    
    /**
     * Get bots for specific mode
     */
    public List<BotProfile> getBotsForMode(String mode) {
        List<BotProfile> bots = new ArrayList<>();
        
        for (BotProfile bot : activeBots.values()) {
            if (bot.getMode().equalsIgnoreCase(mode)) {
                bots.add(bot);
            }
        }
        
        return bots;
    }
    
    /**
     * Get all active bots
     */
    public Collection<BotProfile> getAllBots() {
        return activeBots.values();
    }
    
    /**
     * Get bot by UUID
     */
    public BotProfile getBot(UUID uuid) {
        return activeBots.get(uuid);
    }
    
    /**
     * Get bot by name
     */
    public BotProfile getBot(String name) {
        for (BotProfile bot : activeBots.values()) {
            if (bot.getName().equalsIgnoreCase(name)) {
                return bot;
            }
        }
        return null;
    }
    
    /**
     * Check if bot exists
     */
    public boolean isBot(UUID uuid) {
        return activeBots.containsKey(uuid);
    }
    
    /**
     * Check if bot exists by name
     */
    public boolean isBot(String name) {
        return getBot(name) != null;
    }
    
    /**
     * Get available kits for mode
     */
    public List<String> getAvailableKits(String mode) {
        return botKits.getOrDefault(mode, new ArrayList<>());
    }
    
    /**
     * Get all available modes
     */
    public Set<String> getAvailableModes() {
        return botKits.keySet();
    }
    
    /**
     * Start bot update task
     */
    private void startBotUpdateTask() {
        botUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateBots();
            }
        }.runTaskTimer(plugin, 20L, 20L); // Update every second
    }
    
    /**
     * Update all bots
     */
    private void updateBots() {
        for (BotProfile bot : activeBots.values()) {
            updateBot(bot);
        }
    }
    
    /**
     * Update specific bot
     */
    private void updateBot(BotProfile bot) {
        // Bot logic: movement and combat implemented below (updateBotCombat, findNearestTarget).

        // Update bot position if it's moving
        if (bot.isMoving() && bot.getLocation() != null && bot.getVelocity() != null) {
            Location newLocation = bot.getLocation().add(bot.getVelocity());
            bot.setLocation(newLocation);
        }
        
        // Example: Update bot combat if it's fighting
        if (bot.isFighting()) {
            // Update combat logic
            updateBotCombat(bot);
        }
    }
    
    /**
     * Update bot combat
     */
    private void updateBotCombat(BotProfile bot) {
        // If we don't know where the bot is, we can't process combat
        if (bot.getLocation() == null) {
            bot.setFighting(false);
            bot.setTargetPlayerId(null);
            bot.setVelocity(new Vector(0, 0, 0));
            return;
        }

        // Pick the nearest non-bot player as target within 25 blocks
        Player target = findNearestTarget(bot.getLocation());
        if (target == null) {
            bot.setFighting(false);
            bot.setTargetPlayerId(null);
            bot.setVelocity(new Vector(0, 0, 0));
            bot.setMoving(false);
            return;
        }

        bot.setFighting(true);
        bot.setTargetPlayerId(target.getUniqueId());

        // Move a small step toward the target (simple chase logic)
        Location botLoc = bot.getLocation();
        Location targetLoc = target.getLocation();
        Vector direction = targetLoc.toVector().subtract(botLoc.toVector());
        if (direction.lengthSquared() > 0) {
            Vector velocity = direction.normalize().multiply(bot.getMovementSpeed() * 0.2); // scaled down step
            bot.setVelocity(velocity);
            bot.setMoving(true);
        } else {
            bot.setVelocity(new Vector(0, 0, 0));
            bot.setMoving(false);
        }

        // Simple attack cadence: hit every ~800ms (faster if tryhard)
        long now = System.currentTimeMillis();
        long intervalMs = bot.isTryhard() ? 600L : 800L;
        if (now - bot.getLastAttackTimeMs() >= intervalMs) {
            // Light damage tap; no attacker reference since this is a profile-driven bot
            target.damage(1.0);
            bot.setLastAttackTimeMs(now);
        }
    }

    /**
     * Find the nearest real player to the given location, excluding bots.
     */
    private Player findNearestTarget(Location origin) {
        double maxDistance = 25.0;
        double closest = Double.MAX_VALUE;
        Player nearest = null;

        for (Player candidate : Bukkit.getOnlinePlayers()) {
            if (candidate.getLocation().getWorld() != origin.getWorld()) {
                continue;
            }
            double distance = candidate.getLocation().distance(origin);
            if (distance <= maxDistance && distance < closest && !isBot(candidate.getUniqueId())) {
                closest = distance;
                nearest = candidate;
            }
        }

        return nearest;
    }
    
    /**
     * Shutdown bot manager
     */
    public void shutdown() {
        if (botUpdateTask != null) {
            botUpdateTask.cancel();
        }
        
        // Remove all bots
        for (UUID botUuid : new ArrayList<>(activeBots.keySet())) {
            removeBot(botUuid);
        }
        
        activeBots.clear();
        botKits.clear();
    }
    
    /**
     * Get bot count
     */
    public int getBotCount() {
        return activeBots.size();
    }
    
    /**
     * Get bot count for specific mode
     */
    public int getBotCount(String mode) {
        return getBotsForMode(mode).size();
    }
    
    /**
     * Get bot count for specific kit
     */
    public int getBotCountForKit(String kit) {
        return getBotsForKit(kit).size();
    }
    
    /**
     * Check if bots are enabled
     */
    public boolean areBotsEnabled() {
        return BotsConfig.BOTS_ENABLED;
    }
    
    /**
     * Get bot configuration
     */
    public BotsConfig getBotConfig() {
        return plugin.getConfigManager().getBotsConfig();
    }
}