package dev.artixdev.practice.models;

import java.util.UUID;
import dev.artixdev.practice.models.PlayerSnapshot;

public class MatchHistory implements Comparable<MatchHistory> {
    public static final int MATCH_HISTORY_VERSION = 1;
    private int matchId;
    private UUID opponentUuid;
    private int eloChange;
    public static final boolean DEBUG_MODE = false;
    private String opponentName;
    private long timestamp;
    
    // New fields for serialization
    private UUID uuid;
    private String matchType;
    private long time;
    private PlayerSnapshot winnerSnapshot;
    private PlayerSnapshot loserSnapshot;
    private String kit;
    private String arena;
    private boolean ranked;

    public MatchHistory(int matchId, UUID opponentUuid, String opponentName, int eloChange, long timestamp) {
        this.matchId = matchId;
        this.opponentUuid = opponentUuid;
        this.opponentName = opponentName;
        this.eloChange = eloChange;
        this.timestamp = timestamp;
    }
    
    // New constructor for serialization
    public MatchHistory(UUID uuid, String matchType, long time, PlayerSnapshot winnerSnapshot, 
                       PlayerSnapshot loserSnapshot, String kit, String arena, boolean ranked) {
        this.uuid = uuid;
        this.matchType = matchType;
        this.time = time;
        this.winnerSnapshot = winnerSnapshot;
        this.loserSnapshot = loserSnapshot;
        this.kit = kit;
        this.arena = arena;
        this.ranked = ranked;
        this.timestamp = time; // Set timestamp for compatibility
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public int getEloChange() {
        return this.eloChange;
    }

    public UUID getOpponentUuid() {
        return this.opponentUuid;
    }

    public int getMatchId() {
        return this.matchId;
    }

    public void setOpponentUuid(UUID opponentUuid) {
        this.opponentUuid = opponentUuid;
    }

    public void setEloChange(int eloChange) {
        this.eloChange = eloChange;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getOpponentName() {
        return this.opponentName;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        this.time = timestamp; // Keep time in sync
    }
    
    // Getters for serialization
    public UUID getUuid() {
        return uuid != null ? uuid : opponentUuid;
    }
    
    public String getMatchType() {
        return matchType;
    }
    
    public long getTime() {
        return time > 0 ? time : timestamp;
    }
    
    public PlayerSnapshot getWinnerSnapshot() {
        return winnerSnapshot;
    }
    
    public PlayerSnapshot getLoserSnapshot() {
        return loserSnapshot;
    }
    
    public String getKit() {
        return kit;
    }
    
    public String getArena() {
        return arena;
    }
    
    public boolean isRanked() {
        return ranked;
    }
    
    // Setters for serialization
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
    
    public void setTime(long time) {
        this.time = time;
        this.timestamp = time; // Keep timestamp in sync
    }
    
    public void setWinnerSnapshot(PlayerSnapshot winnerSnapshot) {
        this.winnerSnapshot = winnerSnapshot;
    }
    
    public void setLoserSnapshot(PlayerSnapshot loserSnapshot) {
        this.loserSnapshot = loserSnapshot;
    }
    
    public void setKit(String kit) {
        this.kit = kit;
    }
    
    public void setArena(String arena) {
        this.arena = arena;
    }
    
    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    @Override
    public int compareTo(MatchHistory other) {
        return Long.compare(this.getTime(), other.getTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MatchHistory that = (MatchHistory) obj;
        return matchId == that.matchId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(matchId);
    }
}