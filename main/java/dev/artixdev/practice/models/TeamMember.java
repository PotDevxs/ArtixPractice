package dev.artixdev.practice.models;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

public final class TeamMember {
    
    private int kills;
    @SerializedName("_id")
    private final UUID playerId;
    private boolean isAlive;
    private boolean isSpectator;
    private final String username;

    public TeamMember(Player player) {
        this.playerId = player.getUniqueId();
        this.username = player.getName();
        this.isAlive = true;
        this.isSpectator = false;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public String getUsername() {
        return username;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getDisplayName() {
        Player player = this.getPlayer();
        if (player == null) {
            return this.username;
        } else {
            return player.getDisplayName();
        }
    }

    public void setSpectator(boolean spectator) {
        this.isSpectator = spectator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (!(obj instanceof TeamMember)) {
            return false;
        }
        
        TeamMember other = (TeamMember) obj;
        if (this.playerId == null) {
            return super.equals(obj);
        } else {
            return other.getPlayerId().equals(this.playerId);
        }
    }

    @Override
    public int hashCode() {
        return this.playerId != null ? this.playerId.hashCode() : 0;
    }

    public boolean isSpectator() {
        return this.isSpectator;
    }

    public int getKills() {
        return this.kills;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }
}
