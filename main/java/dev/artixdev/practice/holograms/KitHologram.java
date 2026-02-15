package dev.artixdev.practice.holograms;

import dev.artixdev.practice.enums.LeaderboardType;
import dev.artixdev.practice.models.Kit;

/**
 * KitHologram
 * Hologram for displaying kit-specific leaderboards
 */
public class KitHologram extends AbstractHologram {
    
    private Kit kit;
    
    public KitHologram(LeaderboardType leaderboardType) {
        super(leaderboardType);
        this.setHologramType(dev.artixdev.practice.enums.HologramType.KIT_LEADERBOARD);
    }
    
    public KitHologram(Kit kit, LeaderboardType leaderboardType) {
        super(leaderboardType);
        this.kit = kit;
        this.setHologramType(dev.artixdev.practice.enums.HologramType.KIT_LEADERBOARD);
    }
    
    /**
     * Get kit
     * @return kit
     */
    public Kit getKit() {
        return kit;
    }
    
    /**
     * Set kit
     * @param kit kit
     */
    public void setKit(Kit kit) {
        this.kit = kit;
    }
}
