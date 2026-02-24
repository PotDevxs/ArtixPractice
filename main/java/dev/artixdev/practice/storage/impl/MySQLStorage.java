package dev.artixdev.practice.storage.impl;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.models.Bot;
import dev.artixdev.practice.storage.StorageProvider;
import dev.artixdev.practice.utils.JsonUtils;
import dev.artixdev.practice.utils.LocationUtils;
import dev.artixdev.practice.utils.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.google.gson.reflect.TypeToken;

import java.sql.*;
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
 * MySQL Storage Implementation
 * Handles all MySQL operations
 */
public class MySQLStorage implements StorageProvider {
    
    private final Main plugin;
    private final ExecutorService executor;
    private Connection connection;
    
    public MySQLStorage(Main plugin) {
        this.plugin = plugin;
        this.executor = Executors.newCachedThreadPool();
    }
    
    @Override
    public void initialize() {
        try {
            String host = plugin.getConfigManager().getDatabaseConfig().getMysqlHost();
            int port = plugin.getConfigManager().getDatabaseConfig().getMysqlPort();
            String database = plugin.getConfigManager().getDatabaseConfig().getMysqlDatabase();
            String username = plugin.getConfigManager().getDatabaseConfig().getMysqlUsername();
            String password = plugin.getConfigManager().getDatabaseConfig().getMysqlPassword();
            boolean ssl = plugin.getConfigManager().getDatabaseConfig().isMysqlSslEnabled();
            
            String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=UTC&autoReconnect=true",
                host, port, database, ssl);
            
            connection = DriverManager.getConnection(url, username, password);
            
            // Create tables
            createTables();
            
            plugin.getLogger().info("MySQL storage initialized successfully!");
            
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize MySQL storage!", e);
            throw new RuntimeException("MySQL initialization failed", e);
        }
    }
    
    private void createTables() throws SQLException {
        // Players table
        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
            "uuid VARCHAR(36) PRIMARY KEY," +
            "name VARCHAR(16) NOT NULL," +
            "state VARCHAR(20) NOT NULL," +
            "selected_kit VARCHAR(50)," +
            "inventory TEXT," +
            "armor TEXT," +
            "level INT DEFAULT 1," +
            "experience INT DEFAULT 0," +
            "wins INT DEFAULT 0," +
            "losses INT DEFAULT 0," +
            "kills INT DEFAULT 0," +
            "deaths INT DEFAULT 0," +
            "win_streak INT DEFAULT 0," +
            "best_win_streak INT DEFAULT 0," +
            "play_time BIGINT DEFAULT 0," +
            "last_seen BIGINT DEFAULT 0," +
            "statistics TEXT," +
            "settings TEXT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "INDEX idx_name (name)," +
            "INDEX idx_wins (wins)," +
            "INDEX idx_level (level)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        // Arenas table
        String createArenasTable = "CREATE TABLE IF NOT EXISTS arenas (" +
            "id VARCHAR(36) PRIMARY KEY," +
            "name VARCHAR(50) NOT NULL," +
            "spawn1 TEXT," +
            "spawn2 TEXT," +
            "min_location TEXT," +
            "max_location TEXT," +
            "kit_type VARCHAR(50)," +
            "enabled BOOLEAN DEFAULT TRUE," +
            "ranked BOOLEAN DEFAULT FALSE," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "INDEX idx_name (name)," +
            "INDEX idx_kit_type (kit_type)," +
            "INDEX idx_enabled (enabled)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        // Matches table
        String createMatchesTable = "CREATE TABLE IF NOT EXISTS matches (" +
            "id VARCHAR(36) PRIMARY KEY," +
            "player1 VARCHAR(36)," +
            "player2 VARCHAR(36)," +
            "kit_type VARCHAR(50)," +
            "start_time BIGINT," +
            "end_time BIGINT," +
            "ended BOOLEAN DEFAULT FALSE," +
            "winner VARCHAR(36)," +
            "loser VARCHAR(36)," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "INDEX idx_player1 (player1)," +
            "INDEX idx_player2 (player2)," +
            "INDEX idx_start_time (start_time)," +
            "INDEX idx_ended (ended)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        // Queues table
        String createQueuesTable = "CREATE TABLE IF NOT EXISTS queues (" +
            "id VARCHAR(36) PRIMARY KEY," +
            "kit_type VARCHAR(50)," +
            "ranked BOOLEAN DEFAULT FALSE," +
            "players TEXT," +
            "created_time BIGINT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "INDEX idx_kit_type (kit_type)," +
            "INDEX idx_ranked (ranked)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        // Bots table
        String createBotsTable = "CREATE TABLE IF NOT EXISTS bots (" +
            "id VARCHAR(36) PRIMARY KEY," +
            "name VARCHAR(50) NOT NULL," +
            "kit_type VARCHAR(50)," +
            "difficulty INT DEFAULT 1," +
            "player_uuid VARCHAR(36)," +
            "active BOOLEAN DEFAULT FALSE," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "INDEX idx_name (name)," +
            "INDEX idx_kit_type (kit_type)," +
            "INDEX idx_active (active)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPlayersTable);
            stmt.execute(createArenasTable);
            stmt.execute(createMatchesTable);
            stmt.execute(createQueuesTable);
            stmt.execute(createBotsTable);
        }
    }
    
    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE,"Failed to close MySQL connection", e);
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
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM players WHERE uuid = ?")) {
                
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return mapResultSetToPlayerProfile(rs);
                }
                return null;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load player profile: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> savePlayerProfile(PlayerProfile profile) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO players (uuid, name, state, selected_kit, inventory, armor, " +
                "level, experience, wins, losses, kills, deaths, win_streak, best_win_streak, " +
                "play_time, last_seen, statistics, settings) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "name = VALUES(name), state = VALUES(state), selected_kit = VALUES(selected_kit), " +
                "inventory = VALUES(inventory), armor = VALUES(armor), level = VALUES(level), " +
                "experience = VALUES(experience), wins = VALUES(wins), losses = VALUES(losses), " +
                "kills = VALUES(kills), deaths = VALUES(deaths), win_streak = VALUES(win_streak), " +
                "best_win_streak = VALUES(best_win_streak), play_time = VALUES(play_time), " +
                "last_seen = VALUES(last_seen), statistics = VALUES(statistics), " +
                "settings = VALUES(settings), updated_at = CURRENT_TIMESTAMP")) {
                
                stmt.setString(1, profile.getUuid().toString());
                stmt.setString(2, profile.getName());
                stmt.setString(3, profile.getState().name());
                stmt.setString(4, profile.getSelectedKit() != null ? profile.getSelectedKit().name() : null);
                stmt.setString(5, JsonUtils.toJson(profile.getInventory()));
                stmt.setString(6, JsonUtils.toJson(profile.getArmor()));
                stmt.setInt(7, profile.getLevel());
                stmt.setInt(8, profile.getExperience());
                stmt.setInt(9, profile.getWins());
                stmt.setInt(10, profile.getLosses());
                stmt.setInt(11, profile.getKills());
                stmt.setInt(12, profile.getDeaths());
                stmt.setInt(13, profile.getWinStreak());
                stmt.setInt(14, profile.getBestWinStreak());
                stmt.setLong(15, profile.getPlayTime());
                stmt.setLong(16, profile.getLastSeen());
                stmt.setString(17, JsonUtils.toJson(profile.getStatistics()));
                stmt.setString(18, JsonUtils.toJson(profile.getSettings()));
                
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save player profile: " + profile.getUuid(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<PlayerProfile>> loadAllPlayerProfiles() {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM players");
                 ResultSet rs = stmt.executeQuery()) {
                
                List<PlayerProfile> profiles = new ArrayList<>();
                while (rs.next()) {
                    profiles.add(mapResultSetToPlayerProfile(rs));
                }
                return profiles;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all player profiles", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deletePlayerProfile(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM players WHERE uuid = ?")) {
                
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete player profile: " + uuid, e);
            }
        }, executor);
    }
    
    // Arena Operations
    @Override
    public CompletableFuture<Arena> loadArena(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM arenas WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return mapResultSetToArena(rs);
                }
                return null;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load arena: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveArena(Arena arena) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO arenas (id, name, spawn1, spawn2, min_location, max_location, " +
                "kit_type, enabled, ranked) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "name = VALUES(name), spawn1 = VALUES(spawn1), spawn2 = VALUES(spawn2), " +
                "min_location = VALUES(min_location), max_location = VALUES(max_location), " +
                "kit_type = VALUES(kit_type), enabled = VALUES(enabled), ranked = VALUES(ranked), " +
                "updated_at = CURRENT_TIMESTAMP")) {
                
                stmt.setString(1, arena.getId().toString());
                stmt.setString(2, arena.getName());
                stmt.setString(3, arena.getSpawn1() != null ? JsonUtils.toJson(arena.getSpawn1()) : null);
                stmt.setString(4, arena.getSpawn2() != null ? JsonUtils.toJson(arena.getSpawn2()) : null);
                Cuboid bounds = arena.getBounds();
                stmt.setString(5, bounds != null && bounds.getLowerCorner() != null ? JsonUtils.toJson(bounds.getLowerCorner()) : null);
                stmt.setString(6, bounds != null && bounds.getUpperCorner() != null ? JsonUtils.toJson(bounds.getUpperCorner()) : null);
                stmt.setString(7, arena.getKitType() != null ? arena.getKitType().name() : null);
                stmt.setBoolean(8, arena.isEnabled());
                stmt.setBoolean(9, arena.isRanked());
                
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save arena: " + arena.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Arena>> loadAllArenas() {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM arenas");
                 ResultSet rs = stmt.executeQuery()) {
                
                List<Arena> arenas = new ArrayList<>();
                while (rs.next()) {
                    arenas.add(mapResultSetToArena(rs));
                }
                return arenas;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all arenas", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteArena(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM arenas WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete arena: " + uuid, e);
            }
        }, executor);
    }
    
    // Match Operations
    @Override
    public CompletableFuture<Match> loadMatch(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM matches WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return mapResultSetToMatch(rs);
                }
                return null;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load match: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveMatch(Match match) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO matches (id, player1, player2, kit_type, start_time, end_time, " +
                "ended, winner, loser) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "player1 = VALUES(player1), player2 = VALUES(player2), kit_type = VALUES(kit_type), " +
                "start_time = VALUES(start_time), end_time = VALUES(end_time), ended = VALUES(ended), " +
                "winner = VALUES(winner), loser = VALUES(loser), updated_at = CURRENT_TIMESTAMP")) {
                
                stmt.setString(1, match.getId().toString());
                stmt.setString(2, match.getPlayer1() != null ? match.getPlayer1().getUniqueId().toString() : null);
                stmt.setString(3, match.getPlayer2() != null ? match.getPlayer2().getUniqueId().toString() : null);
                stmt.setString(4, match.getKitType() != null ? match.getKitType().name() : null);
                stmt.setLong(5, match.getStartTime());
                stmt.setLong(6, match.getEndTime());
                stmt.setBoolean(7, match.isEnded());
                stmt.setString(8, match.getWinner() != null ? match.getWinner().getUniqueId().toString() : null);
                stmt.setString(9, match.getLoser() != null ? match.getLoser().getUniqueId().toString() : null);
                
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save match: " + match.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Match>> loadAllMatches() {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM matches");
                 ResultSet rs = stmt.executeQuery()) {
                
                List<Match> matches = new ArrayList<>();
                while (rs.next()) {
                    matches.add(mapResultSetToMatch(rs));
                }
                return matches;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all matches", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteMatch(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM matches WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete match: " + uuid, e);
            }
        }, executor);
    }
    
    // Queue Operations
    @Override
    public CompletableFuture<Queue> loadQueue(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM queues WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return mapResultSetToQueue(rs);
                }
                return null;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load queue: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveQueue(Queue queue) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO queues (id, kit_type, ranked, players, created_time) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "kit_type = VALUES(kit_type), ranked = VALUES(ranked), " +
                "players = VALUES(players), created_time = VALUES(created_time), " +
                "updated_at = CURRENT_TIMESTAMP")) {
                
                stmt.setString(1, queue.getId().toString());
                stmt.setString(2, queue.getKitType() != null ? queue.getKitType().name() : null);
                stmt.setBoolean(3, queue.isRanked());
                stmt.setString(4, JsonUtils.toJson(queue.getPlayers()));
                stmt.setLong(5, queue.getCreatedTime());
                
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save queue: " + queue.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Queue>> loadAllQueues() {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM queues");
                 ResultSet rs = stmt.executeQuery()) {
                
                List<Queue> queues = new ArrayList<>();
                while (rs.next()) {
                    queues.add(mapResultSetToQueue(rs));
                }
                return queues;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all queues", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteQueue(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM queues WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete queue: " + uuid, e);
            }
        }, executor);
    }
    
    // Bot Operations
    @Override
    public CompletableFuture<Bot> loadBot(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM bots WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return mapResultSetToBot(rs);
                }
                return null;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load bot: " + uuid, e);
                return null;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> saveBot(Bot bot) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO bots (id, name, kit_type, difficulty, player_uuid, active) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "name = VALUES(name), kit_type = VALUES(kit_type), difficulty = VALUES(difficulty), " +
                "player_uuid = VALUES(player_uuid), active = VALUES(active), " +
                "updated_at = CURRENT_TIMESTAMP")) {
                
                stmt.setString(1, bot.getId().toString());
                stmt.setString(2, bot.getName());
                stmt.setString(3, bot.getKitType() != null ? bot.getKitType().name() : null);
                stmt.setInt(4, bot.getDifficulty());
                stmt.setString(5, bot.getPlayer() != null ? bot.getPlayer().getUniqueId().toString() : null);
                stmt.setBoolean(6, bot.isActive());
                
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to save bot: " + bot.getId(), e);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<Bot>> loadAllBots() {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bots");
                 ResultSet rs = stmt.executeQuery()) {
                
                List<Bot> bots = new ArrayList<>();
                while (rs.next()) {
                    bots.add(mapResultSetToBot(rs));
                }
                return bots;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to load all bots", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteBot(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM bots WHERE id = ?")) {
                
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to delete bot: " + uuid, e);
            }
        }, executor);
    }
    
    // Statistics Operations
    @Override
    public CompletableFuture<Map<String, Object>> getPlayerStatistics(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT statistics FROM players WHERE uuid = ?")) {
                
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    String statisticsJson = rs.getString("statistics");
                    if (statisticsJson != null && !statisticsJson.isEmpty()) {
                        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>(){};
                        return JsonUtils.fromJson(statisticsJson, typeToken);
                    }
                }
                return null;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to get player statistics: " + uuid, e);
                return null;
            }
        }, (Executor) executor);
    }
    
    @Override
    public CompletableFuture<List<PlayerProfile>> getTopPlayers(String category, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM players ORDER BY " + category + " DESC LIMIT ?")) {
                
                stmt.setInt(1, limit);
                ResultSet rs = stmt.executeQuery();
                
                List<PlayerProfile> players = new ArrayList<>();
                while (rs.next()) {
                    players.add(mapResultSetToPlayerProfile(rs));
                }
                return players;
                
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE,"Failed to get top players", e);
                return new ArrayList<>();
            }
        }, executor);
    }
    
    // Helper methods to map ResultSet to objects
    private PlayerProfile mapResultSetToPlayerProfile(ResultSet rs) throws SQLException {
        UUID uuid = UUID.fromString(rs.getString("uuid"));
        String name = rs.getString("name");
        PlayerProfile profile = new PlayerProfile(uuid, name != null ? name : "Unknown");
        profile.setState(dev.artixdev.practice.enums.PlayerState.valueOf(rs.getString("state")));
        
        String selectedKit = rs.getString("selected_kit");
        if (selectedKit != null) {
            profile.setSelectedKit(dev.artixdev.practice.enums.KitType.valueOf(selectedKit));
        }
        
        String invJson = rs.getString("inventory");
        String armorJson = rs.getString("armor");
        if (invJson != null && !invJson.isEmpty() || armorJson != null && !armorJson.isEmpty()) {
            // PlayerProfile does not expose setInventory/setArmor; map when API is added
        }
        profile.setLevel(rs.getInt("level"));
        profile.setExperience(rs.getInt("experience"));
        profile.setWins(rs.getInt("wins"));
        profile.setLosses(rs.getInt("losses"));
        profile.setKills(rs.getInt("kills"));
        profile.setDeaths(rs.getInt("deaths"));
        profile.setWinStreak(rs.getInt("win_streak"));
        profile.setBestWinStreak(rs.getInt("best_win_streak"));
        profile.setPlayTime(rs.getLong("play_time"));
        profile.setLastSeen(rs.getLong("last_seen"));
        
        return profile;
    }
    
    private Arena mapResultSetToArena(ResultSet rs) throws SQLException {
        Arena arena = new Arena(UUID.fromString(rs.getString("id")), rs.getString("name"));
        String spawn1Str = rs.getString("spawn1");
        if (spawn1Str != null && !spawn1Str.isEmpty()) {
            Location loc = LocationUtils.deserializeLocation(spawn1Str);
            if (loc != null) arena.setSpawn1(loc);
        }
        String spawn2Str = rs.getString("spawn2");
        if (spawn2Str != null && !spawn2Str.isEmpty()) {
            Location loc = LocationUtils.deserializeLocation(spawn2Str);
            if (loc != null) arena.setSpawn2(loc);
        }
        String minStr = rs.getString("min_location");
        if (minStr != null && !minStr.isEmpty()) {
            Location loc = LocationUtils.deserializeLocation(minStr);
            if (loc != null) arena.setMin(loc);
        }
        String maxStr = rs.getString("max_location");
        if (maxStr != null && !maxStr.isEmpty()) {
            Location loc = LocationUtils.deserializeLocation(maxStr);
            if (loc != null) arena.setMax(loc);
        }
        String kitType = rs.getString("kit_type");
        if (kitType != null) {
            arena.setKitType(dev.artixdev.practice.enums.KitType.valueOf(kitType));
        }
        
        arena.setEnabled(rs.getBoolean("enabled"));
        arena.setRanked(rs.getBoolean("ranked"));
        
        return arena;
    }
    
    private Match mapResultSetToMatch(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String kitTypeStr = rs.getString("kit_type");
        dev.artixdev.practice.enums.KitType kitType = kitTypeStr != null
            ? dev.artixdev.practice.enums.KitType.valueOf(kitTypeStr) : dev.artixdev.practice.enums.KitType.DIAMOND;
        Player p1 = null;
        Player p2 = null;
        String u1 = rs.getString("player1");
        if (u1 != null && !u1.isEmpty()) p1 = Bukkit.getPlayer(UUID.fromString(u1));
        String u2 = rs.getString("player2");
        if (u2 != null && !u2.isEmpty()) p2 = Bukkit.getPlayer(UUID.fromString(u2));
        Match match = new Match(id, p1, p2, kitType);
        match.setStartTime(rs.getLong("start_time"));
        match.setEndTime(rs.getLong("end_time"));
        match.setEnded(rs.getBoolean("ended"));
        return match;
    }

    private Queue mapResultSetToQueue(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String kitTypeStr = rs.getString("kit_type");
        dev.artixdev.practice.enums.KitType kitType = kitTypeStr != null
            ? dev.artixdev.practice.enums.KitType.valueOf(kitTypeStr) : dev.artixdev.practice.enums.KitType.DIAMOND;
        boolean ranked = rs.getBoolean("ranked");
        Queue queue = new Queue(id, kitType, ranked);
        queue.setCreatedTime(rs.getLong("created_time"));
        return queue;
    }

    private Bot mapResultSetToBot(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String name = rs.getString("name");
        String kitTypeStr = rs.getString("kit_type");
        dev.artixdev.practice.enums.KitType kitType = kitTypeStr != null
            ? dev.artixdev.practice.enums.KitType.valueOf(kitTypeStr) : dev.artixdev.practice.enums.KitType.DIAMOND;
        int difficulty = rs.getInt("difficulty");
        Bot bot = new Bot(id, name != null ? name : "Bot", kitType, difficulty);
        bot.setActive(rs.getBoolean("active"));
        return bot;
    }
}
