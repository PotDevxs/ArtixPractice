package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.PlayerProfile;

public class PlayerLeaveEvent extends BaseEvent {
    private final PlayerProfile playerProfile;

    public PlayerLeaveEvent(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }
}
