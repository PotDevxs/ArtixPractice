package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Team;

public class TeamDisbandEvent extends BaseEvent {
    private final Player player;
    private final Team team;

    public TeamDisbandEvent(Team team, Player player) {
        this.team = team;
        this.player = player;
    }

    public Team getTeam() {
        return this.team;
    }

    public Player getPlayer() {
        return this.player;
    }
}
