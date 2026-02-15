package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Team;

public class TeamJoinEvent extends BaseEvent {
    private final Player player;
    private final Team team;

    public TeamJoinEvent(Team team, Player player) {
        super();
        this.team = team;
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Team getTeam() {
        return this.team;
    }
}
