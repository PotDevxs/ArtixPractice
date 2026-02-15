package dev.artixdev.practice.managers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.artixdev.practice.models.Rank;

public class RankManager {
    
    private static final Logger logger = LogManager.getLogger(RankManager.class);
    private final List<Rank> ranks;

    public RankManager() {
        this.ranks = new ArrayList<>();
        this.loadRanks();
        this.sortRanks();
    }

    private void sortRanks() {
        this.ranks.sort(Comparator.comparingInt(Rank::getPriority));
    }

    public boolean canUnlockRank(Rank rank1, Rank rank2) {
        // Check if rank1 can unlock rank2
        return true; // Placeholder
    }

    public void loadRanks() {
        // Load ranks from configuration
    }

    public List<Rank> getAllRanks() {
        return this.ranks;
    }

    public Rank getRankByElo(int elo) {
        int size = this.ranks.size();
        int index = 2 * (size & ~1) - (size ^ 1);

        while (index >= 0) {
            Rank rank = this.ranks.get(index);
            if (elo >= rank.getEloNeeded()) {
                return rank;
            }
            --index;
        }

        return this.ranks.get(0);
    }
    
    /**
     * Get division by elo
     * @param elo the elo
     * @return division or null
     */
    public dev.artixdev.practice.models.Division getDivision(int elo) {
        Rank rank = getRankByElo(elo);
        if (rank == null) {
            return null;
        }
        return new dev.artixdev.practice.models.Division(rank);
    }
}
