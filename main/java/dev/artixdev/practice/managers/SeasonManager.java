package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Season;
import dev.artixdev.practice.models.PlayerProfile;

import java.io.File;
import java.util.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Current season and end-of-season ELO reset / history.
 */
public class SeasonManager {

    private final Main plugin;
    private Season currentSeason;
    private final Map<String, Integer> seasonRankCache = new ConcurrentHashMap<>();
    private final File seasonFile;

    public SeasonManager(Main plugin) {
        this.plugin = plugin;
        this.seasonFile = new File(plugin.getDataFolder(), "current_season.yml");
        loadCurrentSeason();
    }

    private void loadCurrentSeason() {
        if (!seasonFile.exists()) {
            startNewSeason(1);
            return;
        }
        try {
            org.bukkit.configuration.file.YamlConfiguration config = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(seasonFile);
            String id = config.getString("id", "season_1");
            String name = config.getString("name", "Season 1");
            long start = config.getLong("startTime", System.currentTimeMillis());
            long end = config.getLong("endTime", System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
            int num = config.getInt("number", 1);
            currentSeason = new Season(id, name, start, end, num);
        } catch (Exception e) {
            startNewSeason(1);
        }
    }

    private void saveCurrentSeason() {
        if (currentSeason == null) return;
        org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();
        config.set("id", currentSeason.getId());
        config.set("name", currentSeason.getName());
        config.set("startTime", currentSeason.getStartTime());
        config.set("endTime", currentSeason.getEndTime());
        config.set("number", currentSeason.getNumber());
        try {
            config.save(seasonFile);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not save current_season.yml: " + e.getMessage());
        }
    }

    public void startNewSeason(int number) {
        long now = System.currentTimeMillis();
        long end = now + java.util.concurrent.TimeUnit.DAYS.toMillis(30);
        currentSeason = new Season("season_" + number, "Season " + number, now, end, number);
        saveCurrentSeason();
    }

    public Season getCurrentSeason() {
        if (currentSeason != null && currentSeason.isActive()) return currentSeason;
        return currentSeason;
    }

    public String getCurrentSeasonName() {
        return currentSeason != null ? currentSeason.getName() : "Season 1";
    }

    public int getCurrentSeasonNumber() {
        return currentSeason != null ? currentSeason.getNumber() : 1;
    }

    /**
     * End current season: save ELO to history (optional), reset ELO for all, start new season.
     */
    public void endSeason() {
        if (currentSeason == null) return;
        int next = currentSeason.getNumber() + 1;
        // Optional: save top ELO to history
        startNewSeason(next);
        seasonRankCache.clear();
    }

    /**
     * Get player's rank (1-based) in current season by ELO. Uses storage top players.
     */
    public int getSeasonRank(UUID playerId) {
        if (playerId == null) return -1;
        String key = playerId.toString();
        if (seasonRankCache.containsKey(key)) return seasonRankCache.get(key);
        plugin.getStorageManager().getTopPlayers("elo", 1000).thenAccept(list -> {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUniqueId().equals(playerId)) {
                    seasonRankCache.put(key, i + 1);
                    break;
                }
            }
        });
        return -1;
    }

    public void resetPlayerEloForNewSeason(PlayerProfile profile) {
        if (profile == null) return;
        profile.setElo(dev.artixdev.practice.configs.SettingsConfig.STARTING_ELO);
        profile.setPreviousElo(profile.getElo());
    }

}
