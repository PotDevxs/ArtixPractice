package dev.artixdev.practice.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Stats Profile
 * Model for player statistics profile
 */
public class StatsProfile {
    
    private UUID uuid;
    private String name;
    private int globalElo;
    private int kills;
    private int wins;
    private int winstreak;
    private int highestWinstreak;
    private List<MatchHistory> unrankedMatchHistory;
    private List<MatchHistory> rankedMatchHistory;
    private Map<String, KitStats> kitStats;
    
    /**
     * Constructor
     * @param uuid the player UUID
     */
    public StatsProfile(UUID uuid) {
        this.uuid = uuid;
        this.name = null;
        this.globalElo = 1000;
        this.kills = 0;
        this.wins = 0;
        this.winstreak = 0;
        this.highestWinstreak = 0;
        this.unrankedMatchHistory = new ArrayList<>();
        this.rankedMatchHistory = new ArrayList<>();
        this.kitStats = new HashMap<>();
    }
    
    /**
     * Get UUID
     * @return UUID
     */
    public UUID getUuid() {
        return uuid;
    }
    
    /**
     * Set UUID
     * @param uuid the UUID
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    /**
     * Get name
     * @return name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set name
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get global elo
     * @return global elo
     */
    public int getGlobalElo() {
        return globalElo;
    }
    
    /**
     * Set global elo
     * @param globalElo the global elo
     */
    public void setGlobalElo(int globalElo) {
        this.globalElo = globalElo;
    }
    
    /**
     * Get kills
     * @return kills
     */
    public int getKills() {
        return kills;
    }
    
    /**
     * Set kills
     * @param kills the kills
     */
    public void setKills(int kills) {
        this.kills = kills;
    }
    
    /**
     * Get wins
     * @return wins
     */
    public int getWins() {
        return wins;
    }
    
    /**
     * Set wins
     * @param wins the wins
     */
    public void setWins(int wins) {
        this.wins = wins;
    }
    
    /**
     * Get winstreak
     * @return winstreak
     */
    public int getWinstreak() {
        return winstreak;
    }
    
    /**
     * Set winstreak
     * @param winstreak the winstreak
     */
    public void setWinstreak(int winstreak) {
        this.winstreak = winstreak;
    }
    
    /**
     * Get highest winstreak
     * @return highest winstreak
     */
    public int getHighestWinstreak() {
        return highestWinstreak;
    }
    
    /**
     * Set highest winstreak
     * @param highestWinstreak the highest winstreak
     */
    public void setHighestWinstreak(int highestWinstreak) {
        this.highestWinstreak = highestWinstreak;
    }
    
    /**
     * Get unranked match history
     * @return unranked match history
     */
    public List<MatchHistory> getUnrankedMatchHistory() {
        return unrankedMatchHistory;
    }
    
    /**
     * Set unranked match history
     * @param unrankedMatchHistory the unranked match history
     */
    public void setUnrankedMatchHistory(List<MatchHistory> unrankedMatchHistory) {
        this.unrankedMatchHistory = unrankedMatchHistory;
    }
    
    /**
     * Get ranked match history
     * @return ranked match history
     */
    public List<MatchHistory> getRankedMatchHistory() {
        return rankedMatchHistory;
    }
    
    /**
     * Set ranked match history
     * @param rankedMatchHistory the ranked match history
     */
    public void setRankedMatchHistory(List<MatchHistory> rankedMatchHistory) {
        this.rankedMatchHistory = rankedMatchHistory;
    }
    
    /**
     * Get kit stats
     * @return kit stats
     */
    public Map<String, KitStats> getKitStats() {
        return kitStats;
    }
    
    /**
     * Set kit stats
     * @param kitStats the kit stats
     */
    public void setKitStats(Map<String, KitStats> kitStats) {
        this.kitStats = kitStats;
    }
}
