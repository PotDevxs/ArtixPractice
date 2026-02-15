package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.PlayerProfile;

public class PlayerJoinEvent extends BaseEvent {
    private final PlayerProfile playerProfile;

    public PlayerJoinEvent(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }
}
