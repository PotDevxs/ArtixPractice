package dev.artixdev.practice.tasks;

import dev.artixdev.practice.models.PlayerProfile;

public class BotUpdateTask implements Runnable {
    
    private final PlayerProfile playerProfile;

    public BotUpdateTask(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public void run() {
        if (playerProfile == null) return;
        // Bot AI/difficulty updates can be added here (e.g. based on playerProfile.getElo()).
    }
}
