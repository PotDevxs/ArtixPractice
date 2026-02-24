package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.Messages;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * Daily and weekly login rewards. Uses lastLoginDay (day epoch), loginStreak, lastWeeklyClaim.
 */
public class DailyRewardManager {

    private static final long DAY_MS = TimeUnit.DAYS.toMillis(1);
    private static final long WEEK_MS = TimeUnit.DAYS.toMillis(7);

    private final Main plugin;

    public DailyRewardManager(Main plugin) {
        this.plugin = plugin;
    }

    /** Call on player join. Gives daily reward and updates streak; optionally weekly. */
    public void onPlayerJoin(Player player, PlayerProfile profile) {
        if (player == null || profile == null) return;
        long now = System.currentTimeMillis();
        long today = now / DAY_MS;
        long lastDay = profile.getLastLoginDay();

        if (lastDay == 0) {
            profile.setLastLoginDay(today);
            profile.setLoginStreak(1);
            giveDailyReward(player, profile, 1);
            return;
        }

        long diff = today - lastDay;
        if (diff == 0) {
            return; // already logged in today
        }
        if (diff == 1) {
            int streak = profile.getLoginStreak() + 1;
            profile.setLoginStreak(streak);
            profile.setLastLoginDay(today);
            giveDailyReward(player, profile, streak);
        } else {
            profile.setLoginStreak(1);
            profile.setLastLoginDay(today);
            giveDailyReward(player, profile, 1);
        }

        // Weekly: if 7+ days since last claim, give weekly reward
        long lastWeekly = profile.getLastWeeklyClaim();
        if (lastWeekly == 0 || (now - lastWeekly) >= WEEK_MS) {
            profile.setLastWeeklyClaim(now);
            giveWeeklyReward(player, profile);
        }
    }

    private void giveDailyReward(Player player, PlayerProfile profile, int streak) {
        player.sendMessage(Messages.get("REWARDS.DAILY_STREAK", "streak", String.valueOf(streak)));
        // Optional: give items from config, or just message
        // ItemStack reward = ... from config
        // player.getInventory().addItem(reward);
    }

    private void giveWeeklyReward(Player player, PlayerProfile profile) {
        player.sendMessage(Messages.get("REWARDS.WEEKLY"));
        // Optional: give better reward
    }
}
