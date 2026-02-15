package dev.artixdev.practice.managers;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import dev.artixdev.api.practice.storage.MongoStorage;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.MongoClients;
import dev.artixdev.libs.com.mongodb.client.MongoCollection;
import dev.artixdev.libs.com.mongodb.client.MongoDatabase;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.configs.SettingsConfig;

/**
 * Player Manager
 * Manages player profiles and data
 */
public class PlayerManager {
   
   private static final Logger logger = LogManager.getLogger(PlayerManager.class);
   
   private final Main plugin;
   private final MongoStorage<PlayerProfile> playerStorage;
   private final Map<UUID, PlayerProfile> playerProfiles;
   private final SettingsConfig settings;
   private Location spawnLocation;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public PlayerManager(Main plugin) {
      this.plugin = plugin;
      
      // Get MongoDB collection for players
      MongoCollection<Document> playersCollection = getPlayersCollection();
      Gson gson = Main.GSON;
      
      // Initialize MongoStorage with explicit type parameter
      this.playerStorage = new MongoStorage<PlayerProfile>(playersCollection, gson);
      this.playerProfiles = new ConcurrentHashMap<>();
      this.settings = plugin.getSettingsConfig();
      
      initializePlayerManager();
   }
   
   /**
    * Get MongoDB players collection
    * @return MongoCollection for players
    */
   private MongoCollection<Document> getPlayersCollection() {
      try {
         // Get database and create collection
         MongoDatabase database = getMongoDatabase();
         if (database != null) {
            return database.getCollection("players");
         }
      } catch (Exception e) {
         logger.error("Failed to get MongoDB players collection", e);
      }
      
      // If database is not available, throw exception as playerStorage is required
      throw new IllegalStateException("MongoDB database not available. Cannot initialize PlayerManager.");
   }
   
   /**
    * Get MongoDB database instance
    * @return MongoDatabase or null if not available
    */
   private MongoDatabase getMongoDatabase() {
      try {
         // Get database configuration
         String connectionString = plugin.getConfigManager().getDatabaseConfig().getMongoConnectionString();
         String databaseName = plugin.getConfigManager().getDatabaseConfig().getMongoDatabase();
         
         if (connectionString == null || databaseName == null) {
            logger.warn("MongoDB configuration not available");
            return null;
         }
         
         // Create MongoDB client and get database
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
    * Load spawn location
    */
   private void loadSpawnLocation() {
      try {
         // Load spawn location from config
         String worldName = settings.getStringOrDefault("spawn.world", "world");
         double x = settings.contains("spawn.x") ? settings.getDouble("spawn.x") : 0.0;
         double y = settings.contains("spawn.y") ? settings.getDouble("spawn.y") : 100.0;
         double z = settings.contains("spawn.z") ? settings.getDouble("spawn.z") : 0.0;
         float yaw = (float) (settings.contains("spawn.yaw") ? settings.getDouble("spawn.yaw") : 0.0);
         float pitch = (float) (settings.contains("spawn.pitch") ? settings.getDouble("spawn.pitch") : 0.0);
         
         if (Bukkit.getWorld(worldName) != null) {
            spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
         } else {
            spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
         }
         
         logger.info("Spawn location loaded: " + spawnLocation.toString());
      } catch (Exception e) {
         logger.error("Failed to load spawn location", e);
         spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
      }
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
    * Load player profile
    * @param playerId the player ID
    * @return player profile or null
    */
   public PlayerProfile loadPlayerProfile(UUID playerId) {
      if (playerId == null) {
         return null;
      }
      
      try {
         // Check cache first
         PlayerProfile cached = playerProfiles.get(playerId);
         if (cached != null) {
            return cached;
         }
         
         // Load from storage
         PlayerProfile profile = playerStorage.load(playerId);
         if (profile != null) {
            // Cache the profile
            playerProfiles.put(playerId, profile);
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
    * @param profile the player profile
    * @return true if successful
    */
   public boolean savePlayerProfile(PlayerProfile profile) {
      if (profile == null) {
         return false;
      }
      
      try {
         // Save to storage
         boolean success = playerStorage.save(profile);
         if (success) {
            // Update cache
            playerProfiles.put(profile.getUniqueId(), profile);
         }
         return success;
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
    * @param playerId the player ID
    * @return true if successful
    */
   public boolean deletePlayerProfile(UUID playerId) {
      if (playerId == null) {
         return false;
      }
      
      try {
         // Remove from cache
         playerProfiles.remove(playerId);
         
         // Delete from storage
         return playerStorage.delete(playerId);
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
    * Get spawn location
    * @return spawn location
    */
   public Location getSpawnLocation() {
      return spawnLocation;
   }
   
   /**
    * Set spawn location
    * @param location the spawn location
    */
   public void setSpawnLocation(Location location) {
      if (location != null) {
         this.spawnLocation = location;
         
         // Save to config
         settings.set("spawn.world", location.getWorld().getName());
         settings.set("spawn.x", location.getX());
         settings.set("spawn.y", location.getY());
         settings.set("spawn.z", location.getZ());
         settings.set("spawn.yaw", location.getYaw());
         settings.set("spawn.pitch", location.getPitch());
         settings.save();
      }
   }
   
   /**
    * Teleport player to spawn
    * @param player the player
    * @return true if successful
    */
   public boolean teleportToSpawn(Player player) {
      if (player == null || spawnLocation == null) {
         return false;
      }
      
      try {
         player.teleport(spawnLocation);
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

   public PlayerProfile getPlayerProfile(Player player1) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPlayerProfile'");
   }
}