package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.AchievementType;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.Messages;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles achievement unlocking and progress.
 */
public class AchievementsManager {

    private final Main plugin;

    public AchievementsManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean isUnlocked(PlayerProfile profile, AchievementType type) {
        return profile != null && profile.hasAchievement(type.getId());
    }

    public int getProgressValue(PlayerProfile profile, AchievementType type) {
        if (profile == null) return 0;
        switch (type.getCriteriaKey()) {
            case "kills": return profile.getKills();
            case "wins": return profile.getWins();
            case "bestWinStreak": return profile.getBestWinStreak();
            case "elo": return profile.getElo();
            case "totalMatches": return profile.getWins() + profile.getLosses();
            default: return 0;
        }
    }

    public boolean meetsRequirement(PlayerProfile profile, AchievementType type) {
        return getProgressValue(profile, type) >= type.getTargetValue();
    }

    /**
     * Unlock achievement if not already unlocked and requirement is met. Returns true if newly unlocked.
     */
    public boolean unlockIfEligible(PlayerProfile profile, AchievementType type) {
        if (profile == null || isUnlocked(profile, type)) return false;
        if (!meetsRequirement(profile, type)) return false;
        profile.getUnlockedAchievements().add(type.getId());
        return true;
    }

    /**
     * Check all achievements and unlock any newly eligible. Returns set of newly unlocked ids.
     */
    public Set<String> checkAndUnlockAll(PlayerProfile profile) {
        Set<String> newlyUnlocked = new HashSet<>();
        for (AchievementType type : AchievementType.values()) {
            if (unlockIfEligible(profile, type)) {
                newlyUnlocked.add(type.getId());
            }
        }
        return newlyUnlocked;
    }

    public void notifyUnlock(Player player, AchievementType type) {
        if (player == null || type == null) return;
        player.sendMessage(Messages.get("ACHIEVEMENTS.UNLOCKED", "achievement", type.getDisplayName()));
        player.sendMessage(Messages.get("ACHIEVEMENTS.DESCRIPTION", "description", type.getDescription()));
    }
}
