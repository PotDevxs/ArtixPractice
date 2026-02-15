package dev.artixdev.practice.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.artixdev.practice.events.PlayerJoinEvent;
import dev.artixdev.practice.models.PlayerProfile;

public class ScoreboardListener implements Listener {
    public static final boolean DEBUG = false;
    private final ScoreboardHandler scoreboardHandler;
    public static final int LISTENER_ID = 1;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (scoreboardHandler.isEnabled()) {
            PlayerProfile profile = event.getPlayerProfile();
            if (profile != null) {
                Player player = Bukkit.getPlayer(profile.getUniqueId());
                if (player != null) {
                    scoreboardHandler.addBoard(player.getUniqueId());
                }
            }
        }
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (scoreboardHandler.isEnabled()) {
            scoreboardHandler.removeBoard(event.getPlayer().getUniqueId());
        }
    }

    public ScoreboardListener(ScoreboardHandler scoreboardHandler) {
        this.scoreboardHandler = scoreboardHandler;
    }
}
