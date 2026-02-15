package dev.artixdev.practice.holograms;

import org.bukkit.Location;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.HologramType;
import dev.artixdev.practice.enums.LeaderboardType;
import dev.artixdev.practice.models.HologramData;

/**
 * AbstractHologram
 * Wrapper class for hologram serialization compatibility
 */
public abstract class AbstractHologram {
    
    protected HologramData data;
    protected HologramType hologramType;
    protected LeaderboardType leaderboardType;
    protected int updateIndex;
    
    public AbstractHologram() {
        this.updateIndex = 0;
    }
    
    public AbstractHologram(LeaderboardType leaderboardType) {
        this();
        this.leaderboardType = leaderboardType;
    }
    
    /**
     * Get hologram data
     * @return hologram data
     */
    public HologramData getData() {
        return data;
    }
    
    /**
     * Set hologram data
     * @param data hologram data
     */
    public void setData(HologramData data) {
        this.data = data;
    }
    
    /**
     * Get hologram type
     * @return hologram type
     */
    public HologramType getHologramType() {
        return hologramType;
    }
    
    /**
     * Set hologram type
     * @param hologramType hologram type
     */
    public void setHologramType(HologramType hologramType) {
        this.hologramType = hologramType;
    }
    
    /**
     * Get leaderboard type
     * @return leaderboard type
     */
    public LeaderboardType getLeaderboardType() {
        return leaderboardType;
    }
    
    /**
     * Set leaderboard type
     * @param leaderboardType leaderboard type
     */
    public void setLeaderboardType(LeaderboardType leaderboardType) {
        this.leaderboardType = leaderboardType;
    }
    
    /**
     * Get update index
     * @return update index
     */
    public int getUpdateIndex() {
        return updateIndex;
    }
    
    /**
     * Set update index
     * @param updateIndex update index
     */
    public void setUpdateIndex(int updateIndex) {
        this.updateIndex = updateIndex;
    }
}
