package dev.artixdev.practice.models;

/**
 * Kit Stats
 * Model for kit-specific statistics
 * This is an alias/wrapper for KitStatistics to maintain compatibility
 */
public class KitStats {
    
    private int elo;
    private int wins;
    private int losses;
    private int kills;
    private int deaths;
    private int winstreak;
    private int rankedWins;
    private int rankedLosses;
    private int rankedWinstreak;
    private int highestWinstreak;
    private int rankedHighestWinstreak;
    
    /**
     * Constructor
     */
    public KitStats() {
        this.elo = 1000;
        this.wins = 0;
        this.losses = 0;
        this.kills = 0;
        this.deaths = 0;
        this.winstreak = 0;
        this.rankedWins = 0;
        this.rankedLosses = 0;
        this.rankedWinstreak = 0;
        this.highestWinstreak = 0;
        this.rankedHighestWinstreak = 0;
    }
    
    /**
     * Get elo
     * @return elo
     */
    public int getElo() {
        return elo;
    }
    
    /**
     * Set elo
     * @param elo the elo
     */
    public void setElo(int elo) {
        this.elo = elo;
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
     * Get losses
     * @return losses
     */
    public int getLosses() {
        return losses;
    }
    
    /**
     * Set losses
     * @param losses the losses
     */
    public void setLosses(int losses) {
        this.losses = losses;
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
     * Get deaths
     * @return deaths
     */
    public int getDeaths() {
        return deaths;
    }
    
    /**
     * Set deaths
     * @param deaths the deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
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
     * Get ranked wins
     * @return ranked wins
     */
    public int getRankedWins() {
        return rankedWins;
    }
    
    /**
     * Set ranked wins
     * @param rankedWins the ranked wins
     */
    public void setRankedWins(int rankedWins) {
        this.rankedWins = rankedWins;
    }
    
    /**
     * Get ranked losses
     * @return ranked losses
     */
    public int getRankedLosses() {
        return rankedLosses;
    }
    
    /**
     * Set ranked losses
     * @param rankedLosses the ranked losses
     */
    public void setRankedLosses(int rankedLosses) {
        this.rankedLosses = rankedLosses;
    }
    
    /**
     * Get ranked winstreak
     * @return ranked winstreak
     */
    public int getRankedWinstreak() {
        return rankedWinstreak;
    }
    
    /**
     * Set ranked winstreak
     * @param rankedWinstreak the ranked winstreak
     */
    public void setRankedWinstreak(int rankedWinstreak) {
        this.rankedWinstreak = rankedWinstreak;
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
     * Get ranked highest winstreak
     * @return ranked highest winstreak
     */
    public int getRankedHighestWinstreak() {
        return rankedHighestWinstreak;
    }
    
    /**
     * Set ranked highest winstreak
     * @param rankedHighestWinstreak the ranked highest winstreak
     */
    public void setRankedHighestWinstreak(int rankedHighestWinstreak) {
        this.rankedHighestWinstreak = rankedHighestWinstreak;
    }
}
