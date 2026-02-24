package dev.artixdev.practice.tasks;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class StatisticsSaveTask implements Runnable {
    private final PlayerProfile playerProfile;
    public static final boolean DEBUG_MODE = false;
    private static final String[] TASK_CONSTANTS = {"StatisticsSaveTask"};
    private static final String[] TASK_MESSAGES = new String[2];
    public static final int TASK_VERSION = 1;

    private static String getTaskMessage(int param0, int param1) {
        if (param0 >= 0 && param0 < TASK_MESSAGES.length) return TASK_MESSAGES[param0];
        return param1 > 0 ? TASK_MESSAGES[1] : TASK_CONSTANTS[0];
    }

    @Override
    public void run() {
        Collection<PlayerProfile> profiles = this.playerProfile.getOnlineProfiles();
        Iterator<PlayerProfile> iterator = profiles.iterator();

        while (iterator.hasNext()) {
            PlayerProfile profile = iterator.next();

            try {
                // Process profile
            } catch (ExceptionInInitializerError e) {
                // Handle error
            }

            if (profile.getPlayer().isOnline()) {
                long lastSave = profile.getLastSaveTime();
                long currentTime = System.currentTimeMillis();
                
                if (lastSave == 0L) {
                    // First save
                } else {
                    long timeDiff = currentTime - lastSave;
                    
                    if (timeDiff == TimeUnit.SECONDS.toMillis(50L)) {
                        String playerName = profile.getUsername();
                        String message = "Saving statistics for player: " + playerName;
                        String title = "Statistics Save";
                        String subtitle = "Saving data for " + playerName;
                        
                        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                        Iterator<? extends Player> playerIterator = onlinePlayers.iterator();
                        
                        while (playerIterator.hasNext()) {
                            Player player = playerIterator.next();
                            // Send message to player
                            player.sendMessage(message);
                        }
                    }
                }
            } else {
                // Player is offline
            }
        }
    }

    public StatisticsSaveTask(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    static {
        TASK_MESSAGES[0] = "StatisticsSaveTask";
        TASK_MESSAGES[1] = "Saving statistics";
    }
}