package dev.artixdev.practice.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import dev.artixdev.api.practice.storage.MongoStorage;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.MongoClients;
import dev.artixdev.libs.com.mongodb.client.MongoCollection;
import dev.artixdev.libs.com.mongodb.client.MongoDatabase;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

/**
 * Player Manager
 * Manages player profiles and data (MongoDB or StorageManager/FLATFILE)
 */
public class PlayerManager {

   private static final Logger logger = LogManager.getLogger(PlayerManager.class);

   private final Main plugin;
   private final MongoStorage<PlayerProfile> playerStorage;
   private final Map<UUID, PlayerProfile> playerProfiles;
   private Location spawnLocation;
   private final boolean useStorageManager;

   /**
    * Constructor
    */
   public PlayerManager(Main plugin) {
      this.plugin = plugin;
      this.playerProfiles = new ConcurrentHashMap<>();
      String dbType = plugin.getConfigManager().getDatabaseConfig().getDatabaseType().toUpperCase();

      if ("MONGODB".equals(dbType)) {
         MongoCollection<Document> coll = getPlayersCollection();
         this.playerStorage = coll != null ? new MongoStorage<>(coll, Main.GSON) : null;
         this.useStorageManager = (this.playerStorage == null);
      } else {
         this.playerStorage = null;
         this.useStorageManager = true;
      }

      initializePlayerManager();
   }

   private MongoCollection<Document> getPlayersCollection() {
      try {
         MongoDatabase database = getMongoDatabase();
         return database != null ? database.getCollection("players") : null;
      } catch (Exception e) {
         logger.error("Failed to get MongoDB players collection", e);
         return null;
      }
   }

   private MongoDatabase getMongoDatabase() {
      try {
         String connectionString = plugin.getConfigManager().getDatabaseConfig().getMongoConnectionString();
         String databaseName = plugin.getConfigManager().getDatabaseConfig().getMongoDatabase();
         if (connectionString == null || databaseName == null) return null;
         MongoClient mongoClient = MongoClients.create(connectionString);
         return mongoClient.getDatabase(databaseName);
      } catch (Exception e) {
         logger.error("Failed to get MongoDB database", e);
         return null;
      }
   }
   
   /**
    * Initialize player manager
    */
   private void initializePlayerManager() {
      try {
         // Load spawn location
         loadSpawnLocation();
         
         // Start cleanup task
         startCleanupTask();
         
         logger.info("PlayerManager initialized successfully");
      } catch (Exception e) {
         logger.error("Failed to initialize PlayerManager", e);
      }
   }
   
   /**
    * Load spawn location from config.yml (spawn section) or use first world spawn
    */
   private void loadSpawnLocation() {
      try {
         FileConfiguration config = plugin.getConfig();
         if (config == null || config.get("spawn") == null) {
            spawnLocation = getDefaultSpawn();
            logger.info("Spawn: using default world spawn (no spawn in config)");
            return;
         }
         String worldName = config.getString("spawn.world", "world");
         double x = config.getDouble("spawn.x", 0.5);
         double y = config.getDouble("spawn.y", 100.0);
         double z = config.getDouble("spawn.z", 0.5);
         float yaw = (float) config.getDouble("spawn.yaw", 0.0);
         float pitch = (float) config.getDouble("spawn.pitch", 0.0);

         if (Bukkit.getWorld(worldName) != null) {
            spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
         } else {
            spawnLocation = getDefaultSpawn();
         }
         logger.info("Spawn location loaded: " + spawnLocation);
      } catch (Exception e) {
         logger.error("Failed to load spawn location", e);
         spawnLocation = getDefaultSpawn();
      }
   }

   private Location getDefaultSpawn() {
      if (Bukkit.getWorlds().isEmpty()) return null;
      return Bukkit.getWorlds().get(0).getSpawnLocation().clone();
   }
   
   /**
    * Start cleanup task
    */
   private void startCleanupTask() {
      BukkitScheduler scheduler = Bukkit.getScheduler();
      scheduler.runTaskTimerAsynchronously(plugin, () -> {
         try {
            // Clean up offline players
            cleanupOfflinePlayers();
         } catch (Exception e) {
            logger.error("Error in player cleanup task", e);
         }
      }, 20L * 60L, 20L * 60L); // Run every minute
   }
   
   /**
    * Clean up offline players
    */
   private void cleanupOfflinePlayers() {
      try {
         playerProfiles.entrySet().removeIf(entry -> {
            UUID playerId = entry.getKey();
            PlayerProfile profile = entry.getValue();
            
            // Remove if player is offline and not in match
            if (!profile.isOnline() && !profile.isInMatch()) {
               // Save profile before removing
               savePlayerProfile(profile);
               return true;
            }
            
            return false;
         });
      } catch (Exception e) {
         logger.error("Failed to cleanup offline players", e);
      }
   }
   
   /**
    * Load player profile (from cache, MongoDB or StorageManager/FLATFILE)
    */
   public PlayerProfile loadPlayerProfile(UUID playerId) {
      if (playerId == null) return null;
      try {
         PlayerProfile cached = playerProfiles.get(playerId);
         if (cached != null) return cached;

         if (useStorageManager && plugin.getStorageManager() != null) {
            PlayerProfile profile = plugin.getStorageManager().loadPlayerProfile(playerId).join();
            if (profile != null) playerProfiles.put(playerId, profile);
            return profile;
         }
         if (playerStorage != null) {
            PlayerProfile profile = playerStorage.load(playerId);
            if (profile != null) playerProfiles.put(playerId, profile);
            return profile;
         }
         return null;
      } catch (Exception e) {
         logger.error("Failed to load player profile: " + playerId, e);
         return null;
      }
   }

   /**
    * Convenience wrapper used by listeners to ensure a player's profile
    * is present in memory (and storage). If the profile does not exist,
    * it will be created automatically using the current player name.
    *
    * @param playerId the player ID
    * @return loaded or newly created profile, or null on failure
    */
   public PlayerProfile loadPlayer(UUID playerId) {
      if (playerId == null) {
         return null;
      }

      // Check already cached profile
      PlayerProfile profile = getPlayerProfile(playerId);
      if (profile != null) {
         return profile;
      }

      // Try loading from storage
      profile = loadPlayerProfile(playerId);
      if (profile != null) {
         return profile;
      }

      // Create a new profile if none exists
      Player bukkitPlayer = Bukkit.getPlayer(playerId);
      String name = bukkitPlayer != null ? bukkitPlayer.getName() : "Unknown";
      return createPlayerProfile(playerId, name);
   }
   
   /**
    * Save player profile
    */
   public boolean savePlayerProfile(PlayerProfile profile) {
      if (profile == null) return false;
      try {
         if (useStorageManager && plugin.getStorageManager() != null) {
            plugin.getStorageManager().savePlayerProfile(profile).join();
            playerProfiles.put(profile.getUniqueId(), profile);
            return true;
         }
         if (playerStorage != null) {
            boolean ok = playerStorage.save(profile);
            if (ok) playerProfiles.put(profile.getUniqueId(), profile);
            return ok;
         }
         return false;
      } catch (Exception e) {
         logger.error("Failed to save player profile: " + profile.getUniqueId(), e);
         return false;
      }
   }

   /**
    * Convenience wrapper to save a player's profile by UUID.
    * If the profile is not currently cached, this method will
    * attempt to load it before saving.
    *
    * @param playerId the player ID
    * @return true if the profile was saved successfully
    */
   public boolean savePlayer(UUID playerId) {
      if (playerId == null) {
         return false;
      }

      PlayerProfile profile = getPlayerProfile(playerId);
      if (profile == null) {
         profile = loadPlayerProfile(playerId);
      }

      return savePlayerProfile(profile);
   }
   
   /**
    * Create player profile
    * @param playerId the player ID
    * @param playerName the player name
    * @return created player profile
    */
   public PlayerProfile createPlayerProfile(UUID playerId, String playerName) {
      if (playerId == null || playerName == null) {
         return null;
      }
      
      try {
         // Create new profile
         PlayerProfile profile = new PlayerProfile(playerId, playerName);
         
         // Save to storage
         boolean success = savePlayerProfile(profile);
         if (success) {
            return profile;
         }
         
         return null;
      } catch (Exception e) {
         logger.error("Failed to create player profile: " + playerId, e);
         return null;
      }
   }
   
   /**
    * Delete player profile
    */
   public boolean deletePlayerProfile(UUID playerId) {
      if (playerId == null) return false;
      try {
         playerProfiles.remove(playerId);
         if (useStorageManager && plugin.getStorageManager() != null) {
            plugin.getStorageManager().deletePlayerProfile(playerId).join();
            return true;
         }
         return playerStorage != null && playerStorage.delete(playerId);
      } catch (Exception e) {
         logger.error("Failed to delete player profile: " + playerId, e);
         return false;
      }
   }
   
   /**
    * Get player profile
    * @param playerId the player ID
    * @return player profile or null
    */
   public PlayerProfile getPlayerProfile(UUID playerId) {
      if (playerId == null) {
         return null;
      }
      
      return playerProfiles.get(playerId);
   }
   
   /**
    * Get player profile by name
    * @param playerName the player name
    * @return player profile or null
    */
   public PlayerProfile getPlayerProfile(String playerName) {
      if (playerName == null) {
         return null;
      }
      
      for (PlayerProfile profile : playerProfiles.values()) {
         if (profile.getName().equalsIgnoreCase(playerName)) {
            return profile;
         }
      }
      
      return null;
   }
   
   /**
    * Get all player profiles
    * @return map of player profiles
    */
   public Map<UUID, PlayerProfile> getAllPlayerProfiles() {
      return new ConcurrentHashMap<>(playerProfiles);
   }
   
   /**
    * Get online player profiles
    * @return map of online player profiles
    */
   public Map<UUID, PlayerProfile> getOnlinePlayerProfiles() {
      Map<UUID, PlayerProfile> online = new ConcurrentHashMap<>();
      
      for (Map.Entry<UUID, PlayerProfile> entry : playerProfiles.entrySet()) {
         if (entry.getValue().isOnline()) {
            online.put(entry.getKey(), entry.getValue());
         }
      }
      
      return online;
   }
   
   /**
    * Get player count
    * @return player count
    */
   public int getPlayerCount() {
      return playerProfiles.size();
   }
   
   /**
    * Get online player count
    * @return online player count
    */
   public int getOnlinePlayerCount() {
      return getOnlinePlayerProfiles().size();
   }
   
   /**
    * Check if player exists
    * @param playerId the player ID
    * @return true if exists
    */
   public boolean playerExists(UUID playerId) {
      return playerId != null && playerProfiles.containsKey(playerId);
   }
   
   /**
    * Check if player exists
    * @param playerName the player name
    * @return true if exists
    */
   public boolean playerExists(String playerName) {
      return getPlayerProfile(playerName) != null;
   }
   
   /**
    * Add player profile
    * @param profile the player profile
    */
   public void addPlayerProfile(PlayerProfile profile) {
      if (profile != null) {
         playerProfiles.put(profile.getUniqueId(), profile);
      }
   }
   
   /**
    * Remove player profile
    * @param playerId the player ID
    */
   public void removePlayerProfile(UUID playerId) {
      if (playerId != null) {
         playerProfiles.remove(playerId);
      }
   }
   
   /**
    * Get spawn location (never null if any world is loaded)
    */
   public Location getSpawnLocation() {
      if (spawnLocation != null) return spawnLocation;
      spawnLocation = getDefaultSpawn();
      return spawnLocation;
   }
   
   /**
    * Set spawn location and save to config.yml
    */
   public void setSpawnLocation(Location location) {
      if (location == null) return;
      this.spawnLocation = location;
      try {
         plugin.getConfig().set("spawn.world", location.getWorld().getName());
         plugin.getConfig().set("spawn.x", location.getX());
         plugin.getConfig().set("spawn.y", location.getY());
         plugin.getConfig().set("spawn.z", location.getZ());
         plugin.getConfig().set("spawn.yaw", (double) location.getYaw());
         plugin.getConfig().set("spawn.pitch", (double) location.getPitch());
         plugin.saveConfig();
      } catch (Exception e) {
         logger.error("Failed to save spawn to config", e);
      }
   }
   
   /**
    * Teleport player to spawn
    * @param player the player
    * @return true if successful
    */
   public boolean teleportToSpawn(Player player) {
      if (player == null) return false;
      Location loc = getSpawnLocation();
      if (loc == null) return false;
      try {
         player.teleport(loc);
         return true;
      } catch (Exception e) {
         logger.error("Failed to teleport player to spawn: " + player.getName(), e);
         return false;
      }
   }
   
   /**
    * Save all player profiles
    */
   public void saveAllPlayerProfiles() {
      try {
         for (PlayerProfile profile : playerProfiles.values()) {
            savePlayerProfile(profile);
         }
         logger.info("Saved all player profiles");
      } catch (Exception e) {
         logger.error("Failed to save all player profiles", e);
      }
   }
   
   /**
    * Load all player profiles
    */
   public void loadAllPlayerProfiles() {
      try {
         // This would load all profiles from storage
         // Implementation depends on your storage system
         logger.info("Loaded all player profiles");
      } catch (Exception e) {
         logger.error("Failed to load all player profiles", e);
      }
   }
   
   /**
    * Shutdown player manager
    */
   public void shutdown() {
      try {
         // Save all profiles
         saveAllPlayerProfiles();
         
         // Clear cache
         playerProfiles.clear();
         
         logger.info("PlayerManager shutdown successfully");
      } catch (Exception e) {
         logger.error("Failed to shutdown PlayerManager", e);
      }
   }

   public PlayerProfile getPlayerProfile(Player player) {
      return player == null ? null : getPlayerProfile(player.getUniqueId());
   }
}