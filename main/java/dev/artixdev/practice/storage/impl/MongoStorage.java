package dev.artixdev.practice.storage.impl;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.models.Bot;
import dev.artixdev.practice.storage.StorageProvider;
import dev.artixdev.practice.utils.JsonUtils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * MongoDB Storage Implementation
 * Handles all MongoDB operations
 */
public class MongoStorage implements StorageProvider {
    
    private final Main plugin;
    private final ExecutorService executor;
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    // Collections
    private MongoCollection<Document> playersCollection;
    private MongoCollection<Document> arenasCollection;
    private MongoCollection<Document> matchesCollection;
    private MongoCollection<Document> queuesCollection;
    private MongoCollection<Document> botsCollection;
    
    public MongoStorage(Main plugin) {
        this.plugin = plugin;
        this.executor = Executors.newCachedThreadPool();
    }
    
    @Override
    public void initialize() {
        try {
            String connectionString = plugin.getConfigManager().getDatabaseConfig().getMongoConnectionString();
            String databaseName = plugin.getConfigManager().getDatabaseConfig().getMongoDatabase();
            
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
            
            // Initialize collections
            playersCollection = database.getCollection("players");
            arenasCollection = database.getCollection("arenas");
            matchesCollection = database.getCollection("matches");
            queuesCollection = database.getCollection("queues");
            botsCollection = database.getCollection("bots");
            
            // Create indexes
            createIndexes();
            
            plugin.getLogger().info("MongoDB storage initialized successfully!");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE,"Failed to initialize MongoDB storage!", e);
            throw new RuntimeException("MongoDB initialization failed", e);
        }
    }
    
    private void createIndexes() {
        try {
            // Player indexes
            playersCollection.createIndex(new Document("uuid", 1));
            playersCollection.createIndex(new Document("name", 1));
            playersCollection.createIndex(new Document("wins", -1));
            playersCollection.createIndex(new Document("level", -1));
            
            // Arena indexes
            arenasCollection.createIndex(new Document("id", 1));
            arenasCollection.createIndex(new Document("name", 1));
            arenasCollection.createIndex(new Document("kitType", 1));
            arenasCollection.createIndex(new Document("enabled", 1));
            
            // Match indexes
            matchesCollection.createIndex(new Document("id", 1));
            matchesCollection.createIndex(new Document("player1", 1));
            matchesCollection.createIndex(new Document("player2", 1));
            matchesCollection.createIndex(new Document("startTime", -1));
            
            // Queue indexes
            queuesCollection.createIndex(new Document("id", 1));
            queuesCollection.createIndex(new Document("kitType", 1));
            queuesCollection.createIndex(new Document("ranked", 1));
            
            // Bot indexes
            botsCollection.createIndex(new Document("id", 1));
            botsCollection.createIndex(new Document("name", 1));
            botsCollection.createIndex(new Document("kitType", 1));
            
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to create MongoDB indexes: " + e.getMessage());
        }
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
        if (mongoClient != null) {
            mongoClient.close();
        }
        if (executor != null) {
            executor.shutdown();
        }
    }
    
    @Override
    public void reconnect() {
        closeConnection();
        initialize();
    }
    
    // Player Profile Operations
    @Override
    public CompletableFuture<PlayerProfile> loadPlayerProfile(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = playersCollection.find(Filters.eq("uuid", uuid.toString())).first();
                if (doc != null) {
                    return JsonUtils.fromDocument(doc, PlayerProfile.class);
                }
                return null;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load player profile: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> savePlayerProfile(PlayerProfile profile) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document doc = JsonUtils.toDocument(profile);
                doc.put("uuid", profile.getUuid().toString());
                
                Bson filter = Filters.eq("uuid", profile.getUuid().toString());
                UpdateResult result = playersCollection.replaceOne(filter, doc);
                
                if (result.getMatchedCount() == 0) {
                    playersCollection.insertOne(doc);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save player profile: " + profile.getUuid(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<PlayerProfile>> loadAllPlayerProfiles() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<PlayerProfile> profiles = new ArrayList<>();
                for (Document doc : playersCollection.find()) {
                    profiles.add(JsonUtils.fromDocument(doc, PlayerProfile.class));
                }
                return profiles;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all player profiles", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deletePlayerProfile(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try {
                playersCollection.deleteOne(Filters.eq("uuid", uuid.toString()));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete player profile: " + uuid, e);
            }
        }, executor);
    }
    
    // Arena Operations
    @Override
    public CompletableFuture<Arena> loadArena(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = arenasCollection.find(Filters.eq("id", uuid.toString())).first();
                if (doc != null) {
                    return JsonUtils.fromDocument(doc, Arena.class);
                }
                return null;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load arena: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveArena(Arena arena) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document doc = JsonUtils.toDocument(arena);
                doc.put("id", arena.getId().toString());
                
                Bson filter = Filters.eq("id", arena.getId().toString());
                UpdateResult result = arenasCollection.replaceOne(filter, doc);
                
                if (result.getMatchedCount() == 0) {
                    arenasCollection.insertOne(doc);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save arena: " + arena.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Arena>> loadAllArenas() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Arena> arenas = new ArrayList<>();
                for (Document doc : arenasCollection.find()) {
                    arenas.add(JsonUtils.fromDocument(doc, Arena.class));
                }
                return arenas;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all arenas", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteArena(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try {
                arenasCollection.deleteOne(Filters.eq("id", uuid.toString()));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete arena: " + uuid, e);
            }
        }, executor);
    }
    
    // Match Operations
    @Override
    public CompletableFuture<Match> loadMatch(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = matchesCollection.find(Filters.eq("id", uuid.toString())).first();
                if (doc != null) {
                    return JsonUtils.fromDocument(doc, Match.class);
                }
                return null;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load match: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveMatch(Match match) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document doc = JsonUtils.toDocument(match);
                doc.put("id", match.getId().toString());
                
                Bson filter = Filters.eq("id", match.getId().toString());
                UpdateResult result = matchesCollection.replaceOne(filter, doc);
                
                if (result.getMatchedCount() == 0) {
                    matchesCollection.insertOne(doc);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save match: " + match.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Match>> loadAllMatches() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Match> matches = new ArrayList<>();
                for (Document doc : matchesCollection.find()) {
                    matches.add(JsonUtils.fromDocument(doc, Match.class));
                }
                return matches;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all matches", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteMatch(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try {
                matchesCollection.deleteOne(Filters.eq("id", uuid.toString()));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete match: " + uuid, e);
            }
        }, executor);
    }
    
    // Queue Operations
    @Override
    public CompletableFuture<Queue> loadQueue(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = queuesCollection.find(Filters.eq("id", uuid.toString())).first();
                if (doc != null) {
                    return JsonUtils.fromDocument(doc, Queue.class);
                }
                return null;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load queue: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveQueue(Queue queue) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document doc = JsonUtils.toDocument(queue);
                doc.put("id", queue.getId().toString());
                
                Bson filter = Filters.eq("id", queue.getId().toString());
                UpdateResult result = queuesCollection.replaceOne(filter, doc);
                
                if (result.getMatchedCount() == 0) {
                    queuesCollection.insertOne(doc);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save queue: " + queue.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Queue>> loadAllQueues() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Queue> queues = new ArrayList<>();
                for (Document doc : queuesCollection.find()) {
                    queues.add(JsonUtils.fromDocument(doc, Queue.class));
                }
                return queues;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all queues", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteQueue(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try {
                queuesCollection.deleteOne(Filters.eq("id", uuid.toString()));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete queue: " + uuid, e);
            }
        }, executor);
    }
    
    // Bot Operations
    @Override
    public CompletableFuture<Bot> loadBot(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = botsCollection.find(Filters.eq("id", uuid.toString())).first();
                if (doc != null) {
                    return JsonUtils.fromDocument(doc, Bot.class);
                }
                return null;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load bot: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveBot(Bot bot) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document doc = JsonUtils.toDocument(bot);
                doc.put("id", bot.getId().toString());
                
                Bson filter = Filters.eq("id", bot.getId().toString());
                UpdateResult result = botsCollection.replaceOne(filter, doc);
                
                if (result.getMatchedCount() == 0) {
                    botsCollection.insertOne(doc);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save bot: " + bot.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Bot>> loadAllBots() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Bot> bots = new ArrayList<>();
                for (Document doc : botsCollection.find()) {
                    bots.add(JsonUtils.fromDocument(doc, Bot.class));
                }
                return bots;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all bots", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteBot(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try {
                botsCollection.deleteOne(Filters.eq("id", uuid.toString()));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete bot: " + uuid, e);
            }
        }, executor);
    }
    
    // Statistics Operations
    @Override
    public CompletableFuture<Map<String, Object>> getPlayerStatistics(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = playersCollection.find(Filters.eq("uuid", uuid.toString())).first();
                if (doc != null) {
                    return doc.get("statistics", Map.class);
                }
                return null;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to get player statistics: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<PlayerProfile>> getTopPlayers(String category, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<PlayerProfile> players = new ArrayList<>();
                Bson sort = Sorts.descending(category);
                
                for (Document doc : playersCollection.find().sort(sort).limit(limit)) {
                    players.add(JsonUtils.fromDocument(doc, PlayerProfile.class));
                }
                return players;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to get top players", e);
                return new ArrayList<>();
            }
        }, executor);
    }
}
