package dev.artixdev.practice.storage.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.MongoDatabase;
import dev.artixdev.libs.com.mongodb.client.MongoCollection;
import dev.artixdev.libs.com.mongodb.client.model.Filters;
import dev.artixdev.libs.com.mongodb.client.model.ReplaceOptions;
import dev.artixdev.libs.com.mongodb.client.result.UpdateResult;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.conversions.Bson;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.models.Bot;
import dev.artixdev.practice.storage.StorageProvider;

/**
 * MongoDB Profile Storage
 * Handles player profile storage using MongoDB
 */
public class MongoProfileStorage implements StorageProvider {
   
   private static final Logger logger = LogManager.getLogger(MongoProfileStorage.class);
   private static final String COLLECTION_NAME = "artix-profiles";
   
   private final Main plugin;
   private final MongoClient mongoClient;
   private final MongoDatabase database;
   private final ExecutorService executor;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    * @param mongoClient the MongoDB client
    * @param database the database
    */
   public MongoProfileStorage(Main plugin, MongoClient mongoClient, MongoDatabase database) {
      this.plugin = plugin;
      this.mongoClient = mongoClient;
      this.database = database;
      this.executor = Executors.newCachedThreadPool();
   }
   
   @Override
   public void initialize() {
      // Already initialized via constructor
   }
   
   @Override
   public boolean isConnected() {
      try {
         database.runCommand(new Document("ping", 1));
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   
   @Override
   public void closeConnection() {
      close();
      if (executor != null) {
         executor.shutdown();
      }
   }
   
   @Override
   public void reconnect() {
      closeConnection();
      // Reconnection handled by caller
   }
   
   // Player Profile Operations
   @Override
   public CompletableFuture<PlayerProfile> loadPlayerProfile(UUID uuid) {
      return CompletableFuture.supplyAsync(() -> load(uuid.toString()), executor);
   }
   
   @Override
   public CompletableFuture<Void> savePlayerProfile(PlayerProfile profile) {
      return CompletableFuture.runAsync(() -> save(profile), executor);
   }
   
   @Override
   public CompletableFuture<List<PlayerProfile>> loadAllPlayerProfiles() {
      return CompletableFuture.supplyAsync(() -> loadAll(), executor);
   }
   
   @Override
   public CompletableFuture<Void> deletePlayerProfile(UUID uuid) {
      return CompletableFuture.runAsync(() -> delete(uuid.toString()), executor);
   }
   
   // Arena Operations - Not supported by this storage
   @Override
   public CompletableFuture<Arena> loadArena(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<Void> saveArena(Arena arena) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<List<Arena>> loadAllArenas() {
      return CompletableFuture.completedFuture(new ArrayList<>());
   }
   
   @Override
   public CompletableFuture<Void> deleteArena(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   // Match Operations - Not supported by this storage
   @Override
   public CompletableFuture<Match> loadMatch(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<Void> saveMatch(Match match) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<List<Match>> loadAllMatches() {
      return CompletableFuture.completedFuture(new ArrayList<>());
   }
   
   @Override
   public CompletableFuture<Void> deleteMatch(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   // Queue Operations - Not supported by this storage
   @Override
   public CompletableFuture<Queue> loadQueue(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<Void> saveQueue(Queue queue) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<List<Queue>> loadAllQueues() {
      return CompletableFuture.completedFuture(new ArrayList<>());
   }
   
   @Override
   public CompletableFuture<Void> deleteQueue(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   // Bot Operations - Not supported by this storage
   @Override
   public CompletableFuture<Bot> loadBot(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<Void> saveBot(Bot bot) {
      return CompletableFuture.completedFuture(null);
   }
   
   @Override
   public CompletableFuture<List<Bot>> loadAllBots() {
      return CompletableFuture.completedFuture(new ArrayList<>());
   }
   
   @Override
   public CompletableFuture<Void> deleteBot(UUID uuid) {
      return CompletableFuture.completedFuture(null);
   }
   
   // Statistics Operations
   @Override
   public CompletableFuture<Map<String, Object>> getPlayerStatistics(UUID uuid) {
      return CompletableFuture.supplyAsync(() -> {
         PlayerProfile profile = load(uuid.toString());
         if (profile != null) {
            // Return basic statistics as a map
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("wins", profile.getWins());
            stats.put("losses", profile.getLosses());
            stats.put("kills", profile.getKills());
            stats.put("deaths", profile.getDeaths());
            stats.put("elo", profile.getElo());
            stats.put("level", profile.getLevel());
            return stats;
         }
         return null;
      });
   }
   
   @Override
   public CompletableFuture<List<PlayerProfile>> getTopPlayers(String category, int limit) {
      return CompletableFuture.supplyAsync(() -> {
         // Simple implementation - load all and sort
         List<PlayerProfile> all = loadAll();
         // TODO: Implement proper sorting by category
         return all.size() > limit ? all.subList(0, limit) : all;
      });
   }
   
   // Legacy methods (kept for backward compatibility)
   public void save(PlayerProfile profile) {
      if (profile == null) {
         return;
      }
      
      try {
         Document document = convertProfileToDocument(profile);
         MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
         Bson filter = Filters.eq("_id", profile.getUuid().toString());
         UpdateResult result = collection.replaceOne(filter, document, new ReplaceOptions().upsert(true));
         
         logger.debug("Saved profile for player: " + profile.getName());
      } catch (Exception e) {
         logger.error("Failed to save profile for player: " + profile.getName(), e);
      }
   }
   
   public PlayerProfile load(String identifier) {
      if (identifier == null || identifier.isEmpty()) {
         return null;
      }
      
      try {
         Document document = database.getCollection(COLLECTION_NAME)
            .find(new Document("_id", identifier))
            .first();
            
         if (document == null) {
            return null;
         }
         
         return convertDocumentToProfile(document);
      } catch (Exception e) {
         logger.error("Failed to load profile for identifier: " + identifier, e);
         return null;
      }
   }
   
   public void delete(String identifier) {
      if (identifier == null || identifier.isEmpty()) {
         return;
      }
      
      try {
         database.getCollection(COLLECTION_NAME)
            .deleteOne(new Document("_id", identifier));
            
         logger.debug("Deleted profile for identifier: " + identifier);
      } catch (Exception e) {
         logger.error("Failed to delete profile for identifier: " + identifier, e);
      }
   }
   
   public List<PlayerProfile> loadAll() {
      try {
         return database.getCollection(COLLECTION_NAME)
            .find()
            .map(this::convertDocumentToProfile)
            .into(new java.util.ArrayList<>());
      } catch (Exception e) {
         logger.error("Failed to load all profiles", e);
         return new java.util.ArrayList<>();
      }
   }
   
   public boolean exists(String identifier) {
      if (identifier == null || identifier.isEmpty()) {
         return false;
      }
      
      try {
         return database.getCollection(COLLECTION_NAME)
            .countDocuments(new Document("_id", identifier)) > 0;
      } catch (Exception e) {
         logger.error("Failed to check if profile exists for identifier: " + identifier, e);
         return false;
      }
   }
   
   public long count() {
      try {
         return database.getCollection(COLLECTION_NAME).countDocuments();
      } catch (Exception e) {
         logger.error("Failed to count profiles", e);
         return 0;
      }
   }
   
   public void clear() {
      try {
         database.getCollection(COLLECTION_NAME).drop();
         logger.info("Cleared all profiles from database");
      } catch (Exception e) {
         logger.error("Failed to clear profiles", e);
      }
   }
   
   public void close() {
      try {
         if (mongoClient != null) {
            mongoClient.close();
         }
         logger.info("Closed MongoDB connection");
      } catch (Exception e) {
         logger.error("Failed to close MongoDB connection", e);
      }
   }
   
   /**
    * Convert profile to document
    * @param profile the profile
    * @return document
    */
   private Document convertProfileToDocument(PlayerProfile profile) {
      Document document = new Document();
      document.put("_id", profile.getUuid().toString());
      document.put("name", profile.getName());
      document.put("wins", profile.getWins());
      document.put("losses", profile.getLosses());
      document.put("kills", profile.getKills());
      document.put("deaths", profile.getDeaths());
      document.put("elo", profile.getElo());
      document.put("level", profile.getLevel());
      document.put("experience", profile.getExperience());
      // TODO: coins field/method doesn't exist in PlayerProfile yet
      // document.put("coins", profile.getCoins());
      document.put("lastSeen", profile.getLastSeen());
      // TODO: firstJoin field/method doesn't exist in PlayerProfile yet
      // document.put("firstJoin", profile.getFirstJoin());
      document.put("state", profile.getState().name());
      // TODO: preferences field/method doesn't exist in PlayerProfile yet
      // document.put("preferences", profile.getPreferences());
      return document;
   }
   
   /**
    * Convert document to profile
    * @param document the document
    * @return profile
    */
   private PlayerProfile convertDocumentToProfile(Document document) {
      if (document == null) {
         return null;
      }
      
      try {
         java.util.UUID uuid = java.util.UUID.fromString(document.getString("_id"));
         String name = document.getString("name");
         
         PlayerProfile profile = new PlayerProfile(uuid, name);
         profile.setWins(document.getInteger("wins", 0));
         profile.setLosses(document.getInteger("losses", 0));
         profile.setKills(document.getInteger("kills", 0));
         profile.setDeaths(document.getInteger("deaths", 0));
         profile.setElo(document.getInteger("elo", 1000));
         profile.setLevel(document.getInteger("level", 1));
         Integer experience = document.getInteger("experience");
         profile.setExperience(experience != null ? experience : 0);
         // TODO: coins field/method doesn't exist in PlayerProfile yet
         Long lastSeenValue = document.getLong("lastSeen");
         if (lastSeenValue != null) {
            profile.setLastSeen(lastSeenValue);
         }
         // TODO: firstJoin field/method doesn't exist in PlayerProfile yet
         
         String stateName = document.getString("state");
         if (stateName != null) {
            try {
               profile.setState(dev.artixdev.practice.enums.PlayerState.valueOf(stateName));
            } catch (IllegalArgumentException e) {
               profile.setState(dev.artixdev.practice.enums.PlayerState.LOBBY);
            }
         }
         
         // TODO: preferences field/method doesn't exist in PlayerProfile yet
         
         return profile;
      } catch (Exception e) {
         logger.error("Failed to convert document to profile", e);
         return null;
      }
   }
   
   /**
    * Get collection name
    * @return collection name
    */
   public String getCollectionName() {
      return COLLECTION_NAME;
   }
   
   /**
    * Get database
    * @return database
    */
   public MongoDatabase getDatabase() {
      return database;
   }
   
   /**
    * Get MongoDB client
    * @return MongoDB client
    */
   public MongoClient getMongoClient() {
      return mongoClient;
   }
}
