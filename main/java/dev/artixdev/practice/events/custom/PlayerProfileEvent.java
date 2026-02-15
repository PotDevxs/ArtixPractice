package dev.artixdev.practice.events.custom;

import org.bukkit.entity.Player;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.events.BaseEvent;

public class PlayerProfileEvent extends BaseEvent {
    private final PlayerProfile playerProfile;
    public static final int EVENT_ID = 1;
    private final Player player;
    public static final boolean DEBUG = false;

    public Player getPlayer() {
        return player;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public PlayerProfileEvent(PlayerProfile playerProfile, Player player) {
        this.playerProfile = playerProfile;
        this.player = player;
    }
}

