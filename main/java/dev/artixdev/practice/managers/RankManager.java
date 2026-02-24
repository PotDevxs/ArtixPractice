package dev.artixdev.practice.managers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.models.Rank;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

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
        return true;
    }

    public void loadRanks() {
        if (!this.ranks.isEmpty()) return;
        // Default divisions (can be overridden by config later)
        ItemStack iron = XMaterial.IRON_INGOT.parseItem();
        ItemStack gold = XMaterial.GOLD_INGOT.parseItem();
        ItemStack diamond = XMaterial.DIAMOND.parseItem();
        ItemStack emerald = XMaterial.EMERALD.parseItem();
        this.ranks.add(new Rank("iron_i", "Iron I", ChatColor.GRAY, iron != null ? iron : new ItemStack(org.bukkit.Material.IRON_INGOT), 0, 0, 0));
        this.ranks.add(new Rank("iron_ii", "Iron II", ChatColor.DARK_GRAY, iron != null ? iron : new ItemStack(org.bukkit.Material.IRON_INGOT), 500, 1, 0));
        this.ranks.add(new Rank("gold_i", "Gold I", ChatColor.YELLOW, gold != null ? gold : new ItemStack(org.bukkit.Material.GOLD_INGOT), 1000, 2, 0));
        this.ranks.add(new Rank("gold_ii", "Gold II", ChatColor.GOLD, gold != null ? gold : new ItemStack(org.bukkit.Material.GOLD_INGOT), 1500, 3, 0));
        this.ranks.add(new Rank("diamond_i", "Diamond I", ChatColor.AQUA, diamond != null ? diamond : new ItemStack(org.bukkit.Material.DIAMOND), 2000, 4, 0));
        this.ranks.add(new Rank("diamond_ii", "Diamond II", ChatColor.BLUE, diamond != null ? diamond : new ItemStack(org.bukkit.Material.DIAMOND), 3000, 5, 0));
        this.ranks.add(new Rank("platinum_i", "Platinum I", ChatColor.LIGHT_PURPLE, emerald != null ? emerald : new ItemStack(org.bukkit.Material.EMERALD), 4000, 6, 0));
        this.ranks.add(new Rank("platinum_ii", "Platinum II", ChatColor.DARK_PURPLE, emerald != null ? emerald : new ItemStack(org.bukkit.Material.EMERALD), 5500, 7, 0));
        this.ranks.add(new Rank("master", "Master", ChatColor.RED, XMaterial.NETHER_STAR.parseItem() != null ? XMaterial.NETHER_STAR.parseItem() : new ItemStack(org.bukkit.Material.NETHER_STAR), 7500, 8, 0));
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
