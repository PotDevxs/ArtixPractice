package dev.artixdev.practice.utils;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.ScoreboardConfig;
import dev.artixdev.practice.models.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Scoreboard Manager
 * Handles scoreboard display for players with full configuration support
 */
public class ScoreboardManager {
    
    private final Main plugin;
    private final Map<UUID, Scoreboard> playerScoreboards;
    private final Map<UUID, String> playerStates;
    private BukkitTask titleAnimationTask;

    /** 1.8 protocol: each of prefix/suffix in scoreboard team packet is max 16 chars. */
    private static final int TEAM_PREFIX_SUFFIX_MAX = 16;

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
        this.playerScoreboards = new HashMap<>();
        this.playerStates = new HashMap<>();
    }

    /**
     * Initialize scoreboard manager and start title animation task.
     */
    public void initialize() {
        startTitleAnimationTask();
        plugin.getLogger().info("ScoreboardManager initialized successfully!");
    }

    /**
     * Start repeating task to update animated scoreboard title for all players.
     */
    private void startTitleAnimationTask() {
        if (titleAnimationTask != null) {
            titleAnimationTask.cancel();
        }
        titleAnimationTask = Bukkit.getScheduler().runTaskTimer(plugin.getPlugin(), () -> {
            ScoreboardConfig.updateAnimationFrame();
            String title = buildTitleForPacket();
            for (Map.Entry<UUID, Scoreboard> entry : new HashMap<>(playerScoreboards).entrySet()) {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p == null || !p.isOnline()) continue;
                Scoreboard sb = entry.getValue();
                Objective obj = sb.getObjective("main");
                if (obj != null) {
                    obj.setDisplayName(title);
                }
            }
        }, 20L, 1L);
    }

    /** Build title string for packet (max 32 chars for 1.8 objective). */
    private String buildTitleForPacket() {
        String title = ScoreboardConfig.isAnimationEnabled()
            ? ScoreboardConfig.getAnimatedTitle()
            : ScoreboardConfig.TITLE;
        title = title.replace("%splitter%", "|");
        title = ChatUtils.colorize(title);
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        return title;
    }
    
    /**
     * Set scoreboard for player
     */
    public void setScoreboard(Player player, String state) {
        if (!isScoreboardEnabled()) {
            return;
        }
        
        Scoreboard scoreboard = createScoreboard(player, state);
        if (scoreboard != null) {
            player.setScoreboard(scoreboard);
            playerScoreboards.put(player.getUniqueId(), scoreboard);
            playerStates.put(player.getUniqueId(), state);
        }
    }
    
    /**
     * Update scoreboard for player
     */
    public void updateScoreboard(Player player) {
        String state = playerStates.get(player.getUniqueId());
        if (state != null) {
            setScoreboard(player, state);
        }
    }
    
    /**
     * Remove scoreboard from player
     */
    public void removeScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        playerScoreboards.remove(player.getUniqueId());
        playerStates.remove(player.getUniqueId());
    }
    
    /**
     * Create scoreboard for player
     */
    private Scoreboard createScoreboard(Player player, String state) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        
        Objective objective = scoreboard.registerNewObjective("main", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        // Set title (1.8 limit: 32 chars); animation is updated by titleAnimationTask
        objective.setDisplayName(buildTitleForPacket());

        // Get scoreboard lines based on state
        List<String> lines = getScoreboardLines(state);
        if (lines == null || lines.isEmpty()) {
            return null;
        }

        // Use Teams: prefix (16) + suffix (16) = up to 32 chars per line (1.8 protocol limit per field = 16)
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            line = replacePlaceholders(line, player, state);
            line = ChatUtils.colorize(line);
            if (line.length() > 40) {
                line = line.substring(0, 37) + "...";
            }
            String prefix = line.length() <= TEAM_PREFIX_SUFFIX_MAX ? line : line.substring(0, TEAM_PREFIX_SUFFIX_MAX);
            String suffix = line.length() <= TEAM_PREFIX_SUFFIX_MAX ? "" : line.substring(TEAM_PREFIX_SUFFIX_MAX, Math.min(line.length(), TEAM_PREFIX_SUFFIX_MAX * 2));
            String entry = uniqueEntry(i);
            Team team = scoreboard.getTeam("line_" + i);
            if (team == null) {
                team = scoreboard.registerNewTeam("line_" + i);
            }
            team.addEntry(entry);
            team.setPrefix(prefix);
            team.setSuffix(suffix);
            objective.getScore(entry).setScore(lines.size() - i);
        }
        return scoreboard;
    }
    
    /** Unique scoreboard entry per line (invisible char) so duplicate text doesn't overwrite. */
    private static String uniqueEntry(int index) {
        return String.valueOf((char) (0xE000 + (index % 0x1000)));
    }

    /**
     * Get scoreboard lines based on state
     */
    private List<String> getScoreboardLines(String state) {
        switch (state.toLowerCase()) {
            case "lobby":
                return ScoreboardConfig.LOBBY_SCOREBOARD;
            case "party":
                return ScoreboardConfig.PARTY_SCOREBOARD;
            case "tournament":
                return ScoreboardConfig.TOURNAMENT_SCOREBOARD;
            case "queue_unranked":
                return ScoreboardConfig.UNRANKED_QUEUE_SCOREBOARD;
            case "queue_ranked":
                return ScoreboardConfig.RANKED_QUEUE_SCOREBOARD;
            case "match_solo":
                return ScoreboardConfig.SOLO_MATCH_SCOREBOARD;
            case "match_solo_best_of":
                return ScoreboardConfig.SOLO_BEST_OF_MATCH_SCOREBOARD;
            case "match_solo_boxing":
                return ScoreboardConfig.SOLO_BOXING_MATCH_SCOREBOARD;
            case "match_solo_bridge":
                return ScoreboardConfig.SOLO_BRIDGE_MATCH_SCOREBOARD;
            case "match_solo_battle_rush":
                return ScoreboardConfig.SOLO_BATTLE_RUSH_MATCH_SCOREBOARD;
            case "match_solo_mlg_rush":
                return ScoreboardConfig.SOLO_MLG_RUSH_MATCH_SCOREBOARD;
            case "match_solo_pearl_fight":
                return ScoreboardConfig.SOLO_PEARL_FIGHT_MATCH_SCOREBOARD;
            case "match_solo_bedwars":
                return ScoreboardConfig.SOLO_BEDWARS_MATCH_SCOREBOARD;
            case "match_solo_top_fight":
                return ScoreboardConfig.SOLO_TOP_FIGHT_MATCH_SCOREBOARD;
            case "match_solo_stick_fight":
                return ScoreboardConfig.SOLO_STICK_FIGHT_MATCH_SCOREBOARD;
            case "match_team":
                return ScoreboardConfig.TEAM_MATCH_SCOREBOARD;
            case "match_team_best_of":
                return ScoreboardConfig.TEAM_BEST_OF_MATCH_SCOREBOARD;
            case "match_team_boxing":
                return ScoreboardConfig.TEAM_BOXING_MATCH_SCOREBOARD;
            case "match_team_bridge":
                return ScoreboardConfig.TEAM_BRIDGE_MATCH_SCOREBOARD;
            case "match_team_bedwars":
                return ScoreboardConfig.TEAM_BEDWARS_MATCH_SCOREBOARD;
            case "match_team_battle_rush":
                return ScoreboardConfig.TEAM_BATTLE_RUSH_MATCH_SCOREBOARD;
            case "match_team_hcf":
                return ScoreboardConfig.TEAM_HCF_MATCH_SCOREBOARD;
            case "match_ffa":
                return ScoreboardConfig.FFA_MATCH_SCOREBOARD;
            case "match_ending":
                return ScoreboardConfig.ENDING_SCOREBOARD;
            case "spectator_match_solo":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_SCOREBOARD;
            case "spectator_match_solo_best_of":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_BEST_OF_SCOREBOARD;
            case "spectator_match_solo_boxing":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_BOXING_SCOREBOARD;
            case "spectator_match_solo_battle_rush":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_BATTLE_RUSH_SCOREBOARD;
            case "spectator_match_solo_bridge":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_BRIDGE_SCOREBOARD;
            case "spectator_match_solo_mlg_rush":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_MLG_RUSH_SCOREBOARD;
            case "spectator_match_solo_stick_fight":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_STICK_FIGHT_SCOREBOARD;
            case "spectator_match_solo_bedwars":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_BEDWARS_SCOREBOARD;
            case "spectator_match_solo_pearl_fight":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_PEARL_FIGHT_SCOREBOARD;
            case "spectator_match_solo_top_fight":
                return ScoreboardConfig.SPECTATOR_MATCH_SOLO_TOP_FIGHT_SCOREBOARD;
            case "spectator_match_team":
                return ScoreboardConfig.SPECTATOR_MATCH_TEAM_SCOREBOARD;
            case "spectator_match_team_best_of":
                return ScoreboardConfig.SPECTATOR_MATCH_TEAM_BEST_OF_SCOREBOARD;
            case "spectator_match_team_boxing":
                return ScoreboardConfig.SPECTATOR_MATCH_TEAM_BOXING_SCOREBOARD;
            case "spectator_match_team_bridge":
                return ScoreboardConfig.SPECTATOR_MATCH_TEAM_BRIDGE_SCOREBOARD;
            case "spectator_match_team_battle_rush":
                return ScoreboardConfig.SPECTATOR_MATCH_TEAM_BATTLE_RUSH_SCOREBOARD;
            case "spectator_match_team_bedwars":
                return ScoreboardConfig.SPECTATOR_MATCH_TEAM_BEDWARS_SCOREBOARD;
            case "spectator_match_team_hcf":
                return ScoreboardConfig.SPECTATOR_MATCH_TEAM_HCF_SCOREBOARD;
            case "spectator_match_ffa":
                return ScoreboardConfig.SPECTATOR_MATCH_FFA_SCOREBOARD;
            case "spectator_match_ending":
                return ScoreboardConfig.SPECTATOR_MATCH_ENDING_SCOREBOARD;
            case "event_solo_waiting":
                return ScoreboardConfig.SOLO_EVENT_WAITING_SCOREBOARD;
            case "event_solo_fighting":
                return ScoreboardConfig.SOLO_EVENT_FIGHTING_SCOREBOARD;
            case "event_team_waiting":
                return ScoreboardConfig.TEAM_EVENT_WAITING_SCOREBOARD;
            case "event_team_fighting":
                return ScoreboardConfig.TEAM_EVENT_FIGHTING_SCOREBOARD;
            default:
                return ScoreboardConfig.LOBBY_SCOREBOARD;
        }
    }
    
    /**
     * Replace placeholders in text
     */
    private String replacePlaceholders(String text, Player player, String state) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        
        // Basic player placeholders
        text = text.replace("{player}", player.getName());
        text = text.replace("{displayname}", player.getDisplayName());
        text = text.replace("{world}", player.getWorld().getName());
        text = text.replace("{ping}", String.valueOf(getPlayerPing(player)));
        
        // Profile placeholders (level, xp, progress bar) - level/bar podem vir de levels.yml
        if (profile != null) {
            int displayLevel = profile.getLevel();
            dev.artixdev.practice.configs.LevelsConfig levelsConfig = plugin.getConfigManager() != null
                ? plugin.getConfigManager().getLevelsConfig() : null;
            if (levelsConfig != null && levelsConfig.isUseXpForLevel()) {
                displayLevel = levelsConfig.getLevelFromXp(profile.getExperience());
            }
            text = text.replace("{level}", String.valueOf(displayLevel));
            text = text.replace("{level_color}", getLevelColor(displayLevel));
            text = text.replace("{xp}", String.valueOf(profile.getExperience()));
            text = text.replace("{progress_bar}", buildLevelProgressBar(profile));
            text = text.replace("{progress_bar_10}", buildLevelProgressBar10(profile));
            text = text.replace("{level_percentage}", formatLevelPercentage(profile));
            text = text.replace("{coins}", String.valueOf(profile.getCoins()));
            text = text.replace("{tokens}", String.valueOf(profile.getEventTokens()));
            text = text.replace("{wins}", String.valueOf(profile.getWins()));
            text = text.replace("{losses}", String.valueOf(profile.getLosses()));
            text = text.replace("{kills}", String.valueOf(profile.getKills()));
            text = text.replace("{deaths}", String.valueOf(profile.getDeaths()));
            text = text.replace("{winrate}", dev.artixdev.practice.utils.StatisticsUtils.formatWinrate(profile.getWins(), profile.getLosses()));
            text = text.replace("{kdr}", dev.artixdev.practice.utils.StatisticsUtils.formatKdr(profile.getKills(), profile.getDeaths()));
        } else {
            text = text.replace("{level}", "0");
            text = text.replace("{level_color}", "&7");
            text = text.replace("{xp}", "0");
            text = text.replace("{progress_bar}", buildLevelProgressBar(null));
            text = text.replace("{progress_bar_10}", buildLevelProgressBar10(null));
            text = text.replace("{level_percentage}", "0.00%");
            text = text.replace("{coins}", "0");
            text = text.replace("{tokens}", "0");
            text = text.replace("{wins}", "0");
            text = text.replace("{losses}", "0");
            text = text.replace("{kills}", "0");
            text = text.replace("{deaths}", "0");
            text = text.replace("{winrate}", "0%");
            text = text.replace("{kdr}", "0.0");
        }
        
        // Server placeholders
        text = text.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        text = text.replace("{max_players}", String.valueOf(Bukkit.getMaxPlayers()));
        text = text.replace("{tps}", String.valueOf(getServerTPS()));
        
        // Queue placeholders
        text = text.replace("{in_queues}", "0");
        text = text.replace("{queuing}", "0");
        text = text.replace("{unranked_queue}", "0");
        text = text.replace("{ranked_queue}", "0");
        
        // Match placeholders (fighting = players in active matches)
        int fightingCount = getFightingCount();
        text = text.replace("{in_fights}", String.valueOf(fightingCount));
        text = text.replace("{fighting}", String.valueOf(fightingCount));
        
        // Party placeholders
        text = text.replace("{party_leader}", "None");
        text = text.replace("{party_size}", "0");
        text = text.replace("{party_limit}", "0");
        
        // Time placeholders
        text = text.replace("{time}", getCurrentTime());
        text = text.replace("{date}", getCurrentDate());
        if (Main.getInstance().getSeasonManager() != null) {
            text = text.replace("{season}", Main.getInstance().getSeasonManager().getCurrentSeasonName());
            int rank = Main.getInstance().getSeasonManager().getSeasonRank(player.getUniqueId());
            text = text.replace("{season_rank}", rank > 0 ? String.valueOf(rank) : "-");
        }
        // State-specific placeholders
        text = replaceStatePlaceholders(text, player, state);
        
        return text;
    }

    /**
     * Build level/XP progress bar for scoreboard (e.g. "&a||||&7||||||").
     * Usa levels.yml para progresso 0-100 dentro do nível atual, se disponível.
     */
    private String buildLevelProgressBar(PlayerProfile profile) {
        final int barLength = 12;
        final int max = 100;
        int current = 0;
        if (profile != null) {
            dev.artixdev.practice.configs.LevelsConfig levelsConfig = plugin.getConfigManager() != null
                ? plugin.getConfigManager().getLevelsConfig() : null;
            if (levelsConfig != null && levelsConfig.isUseXpForLevel()) {
                current = levelsConfig.getXpProgressInLevel(profile.getExperience());
            } else {
                int xp = profile.getExperience();
                current = Math.min(max, xp % max);
                if (current == 0 && xp > 0) current = max;
            }
        }
        return ProgressBar.createProgressBar(current, max, barLength, '|', ChatColor.GREEN, ChatColor.GRAY);
    }

    /**
     * Build 10-block level progress bar for Refine-style LOBBY (█ filled, ░ empty).
     */
    private String buildLevelProgressBar10(PlayerProfile profile) {
        final int barLength = 10;
        final int max = 100;
        int current = 0;
        if (profile != null) {
            dev.artixdev.practice.configs.LevelsConfig levelsConfig = plugin.getConfigManager() != null
                ? plugin.getConfigManager().getLevelsConfig() : null;
            if (levelsConfig != null && levelsConfig.isUseXpForLevel()) {
                current = levelsConfig.getXpProgressInLevel(profile.getExperience());
            } else {
                int xp = profile.getExperience();
                current = Math.min(max, xp % max);
                if (current == 0 && xp > 0) current = max;
            }
        }
        int filled = (current * barLength) / max;
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GREEN);
        for (int i = 0; i < filled; i++) sb.append('\u2588');
        sb.append(ChatColor.GRAY);
        for (int i = filled; i < barLength; i++) sb.append('\u2591');
        return sb.toString();
    }

    /**
     * Format level progress as percentage string (e.g. "89.46%").
     */
    private String formatLevelPercentage(PlayerProfile profile) {
        if (profile == null) return "0.00%";
        dev.artixdev.practice.configs.LevelsConfig levelsConfig = plugin.getConfigManager() != null
            ? plugin.getConfigManager().getLevelsConfig() : null;
        if (levelsConfig == null || !levelsConfig.isUseXpForLevel()) {
            int xp = profile.getExperience();
            int pct = (xp % 100);
            if (xp > 0 && pct == 0) pct = 100;
            return String.format("%.2f%%", (double) pct);
        }
        int totalXp = profile.getExperience();
        int level = levelsConfig.getLevelFromXp(totalXp);
        int xpCurrent = levelsConfig.getXpRequiredForLevel(level);
        int xpNext = levelsConfig.getXpRequiredForLevel(level + 1);
        int range = xpNext - xpCurrent;
        if (range <= 0) return "100.00%";
        double pct = ((totalXp - xpCurrent) * 100.0) / range;
        pct = Math.min(100.0, Math.max(0.0, pct));
        return String.format("%.2f%%", pct);
    }

    private String getLevelColor(int level) {
        if (plugin.getConfigManager() == null || plugin.getConfigManager().getLevelsConfig() == null) {
            return "&7";
        }
        return plugin.getConfigManager().getLevelsConfig().getColorForLevel(level);
    }

    private int getFightingCount() {
        if (plugin.getMatchManager() == null) return 0;
        try {
            return plugin.getMatchManager().getActiveMatchCount() * 2;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Replace state-specific placeholders
     */
    private String replaceStatePlaceholders(String text, Player player, String state) {
        // Match placeholders
        if (state.startsWith("match_")) {
            dev.artixdev.practice.models.Match match = Main.getInstance().getMatchManager().getMatchByPlayer(player);
            int warmupSec = (match != null && match.isInWarmup()) ? match.getWarmupSecondsRemaining() : 0;
            text = text.replace("{warmup_seconds}", String.valueOf(warmupSec));
            text = text.replace("{match_duration}", "00:00");
            text = text.replace("{match_kit}", "Unknown");
            text = text.replace("{match_arena}", "Unknown");
            text = text.replace("{opponent_name}", "Unknown");
            text = text.replace("{opponent_ping}", "0");
            text = text.replace("{your_ping}", String.valueOf(getPlayerPing(player)));
            text = text.replace("{your_kills}", "0");
            text = text.replace("{your_points_formatted}", "0");
            text = text.replace("{opponent_points_formatted}", "0");
            text = text.replace("{your_hits}", "0");
            text = text.replace("{opponent_hits}", "0");
            text = text.replace("{your_combo}", "0");
            text = text.replace("{opponent_combo}", "0");
            text = text.replace("{hit_difference}", "0");
            text = text.replace("{red_points_formatted}", "0");
            text = text.replace("{blue_points_formatted}", "0");
            text = text.replace("{red_lives_formatted}", "0");
            text = text.replace("{blue_lives_formatted}", "0");
            text = text.replace("{bedwars_red_formatted}", "0");
            text = text.replace("{bedwars_blue_formatted}", "0");
            text = text.replace("{their_points_formatted}", "0");
            text = text.replace("{your_team_alive}", "0");
            text = text.replace("{your_team_count}", "0");
            text = text.replace("{opponent_team_alive}", "0");
            text = text.replace("{opponent_team_count}", "0");
            text = text.replace("{alive_count}", "0");
            text = text.replace("{total_count}", "0");
            text = text.replace("{player_count}", "0");
            text = text.replace("{players_alive}", "0");
        }
        
        // Event placeholders
        if (state.startsWith("event_")) {
            text = text.replace("{event_name}", "Event");
            text = text.replace("{event_host_name}", "Unknown");
            text = text.replace("{event_player_count}", "0");
            text = text.replace("{event_max_players}", "0");
            text = text.replace("{event_players_alive}", "0");
            text = text.replace("{event_duration}", "00:00");
            text = text.replace("{event_remaining}", "0");
            text = text.replace("{event_status}", "Waiting...");
            text = text.replace("{event_playerA_name}", "Player A");
            text = text.replace("{event_playerB_name}", "Player B");
            text = text.replace("{event_playerA_ping}", "0");
            text = text.replace("{event_playerB_ping}", "0");
            text = text.replace("{event_teamA_name}", "Team A");
            text = text.replace("{event_teamB_name}", "Team B");
            text = text.replace("{event_teamA_size}", "0");
            text = text.replace("{event_teamB_size}", "0");
        }
        
        // Tournament placeholders
        if (state.equals("tournament")) {
            text = text.replace("{tournament_team_size}", "1");
            text = text.replace("{tournament_kit}", "Unknown");
            text = text.replace("{tournament_round}", "1");
            text = text.replace("{tournament_players}", "0");
            text = text.replace("{tournament_max_players}", "0");
        }
        
        // Queue placeholders
        if (state.startsWith("queue_")) {
            text = text.replace("{queue_type}", state.contains("ranked") ? "Ranked" : "Unranked");
            text = text.replace("{queue_kit}", "Unknown");
            text = text.replace("{queue_duration}", "00:00");
        }
        
        return text;
    }
    
    /**
     * Cancel title animation task and cleanup. Call on plugin disable.
     */
    public void shutdown() {
        if (titleAnimationTask != null) {
            titleAnimationTask.cancel();
            titleAnimationTask = null;
        }
    }

    /**
     * Check if scoreboard is enabled (lobby/global scoreboard)
     */
    private boolean isScoreboardEnabled() {
        return true;
    }
    
    /**
     * Get player ping
     */
    private int getPlayerPing(Player player) {
        try {
            // Use reflection to get ping for older versions
            return 0; // Placeholder
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get server TPS
     */
    private double getServerTPS() {
        // Simple TPS calculation - in real implementation you'd use a proper TPS calculator
        return 20.0;
    }
    
    /**
     * Get current time
     */
    private String getCurrentTime() {
        return java.time.LocalTime.now().toString().substring(0, 5);
    }
    
    /**
     * Get current date
     */
    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }
    
    /**
     * Get player scoreboard
     */
    public Scoreboard getPlayerScoreboard(Player player) {
        return playerScoreboards.get(player.getUniqueId());
    }
    
    /**
     * Get player state
     */
    public String getPlayerState(Player player) {
        return playerStates.get(player.getUniqueId());
    }
    
    /**
     * Check if player has scoreboard
     */
    public boolean hasScoreboard(Player player) {
        return playerScoreboards.containsKey(player.getUniqueId());
    }

    /**
     * Refresh scoreboard for all online players that have one (e.g. after config reload).
     */
    public void refreshAllScoreboards() {
        for (Map.Entry<UUID, Scoreboard> entry : new HashMap<>(playerScoreboards).entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            if (p != null && p.isOnline()) {
                updateScoreboard(p);
            }
        }
    }
}