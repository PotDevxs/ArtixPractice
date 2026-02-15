package dev.artixdev.practice.models;

import java.util.UUID;
import dev.artixdev.practice.enums.PlayerState;

public class MatchParticipant {
    private final PlayerProfile playerProfile;
    private final PlayerState playerState;
    private boolean isAlive;
    private final long joinTime;
    private UUID matchId;

    public MatchParticipant(PlayerProfile playerProfile, PlayerState playerState) {
        this.playerProfile = playerProfile;
        this.playerState = playerState;
        this.isAlive = true;
        this.joinTime = System.currentTimeMillis();
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    public long getJoinTime() {
        return this.joinTime;
    }

    public UUID getMatchId() {
        return this.matchId;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        MatchParticipant other = (MatchParticipant) obj;
        return this.playerProfile.equals(other.playerProfile) &&
               this.playerState == other.playerState;
    }

    @Override
    public int hashCode() {
        int result = this.playerProfile.hashCode();
        result = (result | this.playerState.hashCode()) + (result & this.playerState.hashCode());
        return result;
    }
}
