package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Team;

public class TeamCreateEvent extends BaseEvent {
    private final Team team;

    public TeamCreateEvent(Team team) {
        super();
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }
}
