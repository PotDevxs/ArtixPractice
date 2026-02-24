package dev.artixdev.practice.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Achievement definitions: id, display name, description, criteria and target value.
 */
public enum AchievementType {
    FIRST_KILL("first_kill", "&6First Blood", "&7Get your first kill", Material.IRON_SWORD, "kills", 1),
    KILLS_10("kills_10", "&6Killer", "&7Get 10 kills", Material.GOLD_SWORD, "kills", 10),
    KILLS_100("kills_100", "&6Slayer", "&7Get 100 kills", Material.DIAMOND_SWORD, "kills", 100),
    KILLS_1000("kills_1000", "&c&lAssassin", "&7Get 1000 kills", Material.DIAMOND_SWORD, "kills", 1000),
    WINS_10("wins_10", "&6Winner", "&7Win 10 matches", Material.GOLDEN_APPLE, "wins", 10),
    WINS_100("wins_100", "&6Champion", "&7Win 100 matches", Material.GOLDEN_APPLE, "wins", 100),
    WINSTREAK_5("winstreak_5", "&6On Fire", "&7Reach 5 winstreak", Material.BLAZE_POWDER, "bestWinStreak", 5),
    WINSTREAK_10("winstreak_10", "&6Unstoppable", "&7Reach 10 winstreak", Material.FIREBALL, "bestWinStreak", 10),
    WINSTREAK_20("winstreak_20", "&c&lInvincible", "&7Reach 20 winstreak", Material.NETHER_STAR, "bestWinStreak", 20),
    ELO_1200("elo_1200", "&6Rising", "&7Reach 1200 ELO", Material.EMERALD, "elo", 1200),
    ELO_1500("elo_1500", "&6Diamond", "&7Reach 1500 ELO", Material.DIAMOND, "elo", 1500),
    ELO_2000("elo_2000", "&c&lMaster", "&7Reach 2000 ELO", Material.BEACON, "elo", 2000),
    FIRST_MATCH("first_match", "&6Rookie", "&7Complete your first match", Material.LEATHER_HELMET, "totalMatches", 1),
    MATCHES_50("matches_50", "&6Dedicated", "&7Play 50 matches", Material.IRON_HELMET, "totalMatches", 50);

    private final String id;
    private final String displayName;
    private final String description;
    private final Material icon;
    private final String criteriaKey;
    private final int targetValue;

    AchievementType(String id, String displayName, String description, Material icon, String criteriaKey, int targetValue) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.icon = icon != null ? icon : Material.PAPER;
        this.criteriaKey = criteriaKey;
        this.targetValue = targetValue;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public ItemStack getIcon() {
        return new ItemStack(icon);
    }

    public Material getIconMaterial() {
        return icon;
    }

    public String getCriteriaKey() {
        return criteriaKey;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public static AchievementType fromId(String id) {
        for (AchievementType t : values()) {
            if (t.id.equalsIgnoreCase(id)) return t;
        }
        return null;
    }
}
