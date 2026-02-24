package dev.artixdev.practice.storage.impl;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.models.Bot;
import dev.artixdev.practice.storage.StorageProvider;
import dev.artixdev.practice.utils.JsonUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Flatfile Storage - armazena dados em arquivos JSON locais.
 * Não requer MongoDB/MySQL; ideal para uso local.
 */
public class FlatFileStorage implements StorageProvider {

    private final Main plugin;
    private final ExecutorService executor;
    private File dataDir;
    private File playersDir;
    private File arenasDir;
    private File matchesDir;
    private File queuesDir;
    private File botsDir;
    private boolean connected;

    public FlatFileStorage(Main plugin) {
        this.plugin = plugin;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void initialize() {
        try {
            String folder = plugin.getConfigManager().getDatabaseConfig().getFlatfileFolder();
            if (folder == null || folder.isEmpty()) folder = "data";
            dataDir = new File(plugin.getDataFolder(), folder);
            playersDir = new File(dataDir, "players");
            arenasDir = new File(dataDir, "arenas");
            matchesDir = new File(dataDir, "matches");
            queuesDir = new File(dataDir, "queues");
            botsDir = new File(dataDir, "bots");

            for (File dir : new File[]{ dataDir, playersDir, arenasDir, matchesDir, queuesDir, botsDir }) {
                if (!dir.exists()) dir.mkdirs();
            }
            connected = true;
            plugin.getLogger().info("Flatfile storage initialized at: " + dataDir.getAbsolutePath());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize Flatfile storage!", e);
            throw new RuntimeException("Flatfile initialization failed", e);
        }
    }

    @Override
    public boolean isConnected() {
        return connected && dataDir != null && dataDir.exists();
    }

    @Override
    public void closeConnection() {
        connected = false;
        if (executor != null) executor.shutdown();
    }

    @Override
    public void reconnect() {
        closeConnection();
        initialize();
    }

    private File playerFile(UUID uuid) {
        return new File(playersDir, uuid.toString() + ".json");
    }

    private File arenaFile(UUID uuid) {
        return new File(arenasDir, uuid.toString() + ".json");
    }

    private File matchFile(UUID uuid) {
        return new File(matchesDir, uuid.toString() + ".json");
    }

    private File queueFile(UUID uuid) {
        return new File(queuesDir, uuid.toString() + ".json");
    }

    private File botFile(UUID uuid) {
        return new File(botsDir, uuid.toString() + ".json");
    }

    private static void writeFile(File file, String content) {
        try {
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFile(File file) {
        if (!file.exists()) return null;
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CompletableFuture<PlayerProfile> loadPlayerProfile(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String json = readFile(playerFile(uuid));
                if (json == null || json.isEmpty()) return null;
                return JsonUtils.fromJson(json, PlayerProfile.class);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load player profile: " + uuid, e);
                return null;
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> savePlayerProfile(PlayerProfile profile) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeFile(playerFile(profile.getUniqueId()), JsonUtils.toJson(profile));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save player profile: " + profile.getUniqueId(), e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<List<PlayerProfile>> loadAllPlayerProfiles() {
        return CompletableFuture.supplyAsync(() -> loadAllInDir(playersDir, PlayerProfile.class), executor);
    }

    @Override
    public CompletableFuture<Void> deletePlayerProfile(UUID uuid) {
        return CompletableFuture.runAsync(() -> deleteFile(playerFile(uuid)), executor);
    }

    @Override
    public CompletableFuture<Arena> loadArena(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String json = readFile(arenaFile(uuid));
            if (json == null || json.isEmpty()) return null;
            try {
                return JsonUtils.fromJson(json, Arena.class);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load arena: " + uuid, e);
                return null;
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> saveArena(Arena arena) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeFile(arenaFile(arena.getId()), JsonUtils.toJson(arena));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save arena: " + arena.getId(), e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<List<Arena>> loadAllArenas() {
        return CompletableFuture.supplyAsync(() -> loadAllInDir(arenasDir, Arena.class), executor);
    }

    @Override
    public CompletableFuture<Void> deleteArena(UUID uuid) {
        return CompletableFuture.runAsync(() -> deleteFile(arenaFile(uuid)), executor);
    }

    @Override
    public CompletableFuture<Match> loadMatch(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String json = readFile(matchFile(uuid));
            if (json == null || json.isEmpty()) return null;
            try {
                return JsonUtils.fromJson(json, Match.class);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load match: " + uuid, e);
                return null;
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> saveMatch(Match match) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeFile(matchFile(match.getId()), JsonUtils.toJson(match));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save match: " + match.getId(), e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<List<Match>> loadAllMatches() {
        return CompletableFuture.supplyAsync(() -> loadAllInDir(matchesDir, Match.class), executor);
    }

    @Override
    public CompletableFuture<Void> deleteMatch(UUID uuid) {
        return CompletableFuture.runAsync(() -> deleteFile(matchFile(uuid)), executor);
    }

    @Override
    public CompletableFuture<Queue> loadQueue(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String json = readFile(queueFile(uuid));
            if (json == null || json.isEmpty()) return null;
            try {
                return JsonUtils.fromJson(json, Queue.class);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load queue: " + uuid, e);
                return null;
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> saveQueue(Queue queue) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeFile(queueFile(queue.getId()), JsonUtils.toJson(queue));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save queue: " + queue.getId(), e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<List<Queue>> loadAllQueues() {
        return CompletableFuture.supplyAsync(() -> loadAllInDir(queuesDir, Queue.class), executor);
    }

    @Override
    public CompletableFuture<Void> deleteQueue(UUID uuid) {
        return CompletableFuture.runAsync(() -> deleteFile(queueFile(uuid)), executor);
    }

    @Override
    public CompletableFuture<Bot> loadBot(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String json = readFile(botFile(uuid));
            if (json == null || json.isEmpty()) return null;
            try {
                return JsonUtils.fromJson(json, Bot.class);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load bot: " + uuid, e);
                return null;
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Void> saveBot(Bot bot) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeFile(botFile(bot.getId()), JsonUtils.toJson(bot));
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save bot: " + bot.getId(), e);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<List<Bot>> loadAllBots() {
        return CompletableFuture.supplyAsync(() -> loadAllInDir(botsDir, Bot.class), executor);
    }

    @Override
    public CompletableFuture<Void> deleteBot(UUID uuid) {
        return CompletableFuture.runAsync(() -> deleteFile(botFile(uuid)), executor);
    }

    @Override
    public CompletableFuture<Map<String, Object>> getPlayerStatistics(UUID uuid) {
        return loadPlayerProfile(uuid).thenApply(profile -> {
            if (profile == null) return null;
            try {
                return JsonUtils.toMap(profile);
            } catch (Exception e) {
                return null;
            }
        });
    }

    @Override
    public CompletableFuture<List<PlayerProfile>> getTopPlayers(String category, int limit) {
        return loadAllPlayerProfiles().thenApply(profiles -> {
            if (profiles == null || profiles.isEmpty()) return new ArrayList<>();
            Comparator<PlayerProfile> cmp = comparatorFor(category);
            if (cmp == null) return profiles.size() > limit ? profiles.subList(0, limit) : profiles;
            return profiles.stream().sorted(cmp).limit(limit).collect(java.util.stream.Collectors.toList());
        });
    }

    private Comparator<PlayerProfile> comparatorFor(String category) {
        if (category == null) return null;
        switch (category.toLowerCase()) {
            case "wins": return Comparator.comparingInt(PlayerProfile::getWins).reversed();
            case "losses": return Comparator.comparingInt(PlayerProfile::getLosses).reversed();
            case "kills": return Comparator.comparingInt(PlayerProfile::getKills).reversed();
            case "deaths": return Comparator.comparingInt(PlayerProfile::getDeaths).reversed();
            case "level": return Comparator.comparingInt(PlayerProfile::getLevel).reversed();
            case "experience": return Comparator.comparingInt(PlayerProfile::getExperience).reversed();
            case "elo": return Comparator.comparingInt(PlayerProfile::getElo).reversed();
            case "winstreak":
            case "win_streak": return Comparator.comparingInt(PlayerProfile::getWinStreak).reversed();
            case "bestwinstreak":
            case "best_win_streak": return Comparator.comparingInt(PlayerProfile::getBestWinStreak).reversed();
            case "playtime":
            case "play_time": return Comparator.comparingLong(PlayerProfile::getPlayTime).reversed();
            default: return Comparator.comparingInt(PlayerProfile::getWins).reversed();
        }
    }

    private <T> List<T> loadAllInDir(File dir, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        File[] files = dir.listFiles((d, name) -> name != null && name.endsWith(".json"));
        if (files == null) return list;
        for (File f : files) {
            String json = readFile(f);
            if (json == null || json.isEmpty()) continue;
            try {
                T obj = JsonUtils.fromJson(json, clazz);
                if (obj != null) list.add(obj);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load " + clazz.getSimpleName() + " from " + f.getName() + ": " + e.getMessage());
            }
        }
        return list;
    }

    private void deleteFile(File file) {
        try {
            if (file != null && file.exists()) Files.delete(file.toPath());
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete file: " + file, e);
        }
    }
}
