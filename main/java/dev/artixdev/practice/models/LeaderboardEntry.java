package dev.artixdev.practice.models;

import java.util.UUID;

public class LeaderboardEntry {
    public static final int LEADERBOARD_ENTRY_VERSION = 1;
    private final PlayerProfile playerProfile;
    public static final boolean DEBUG_MODE = false;
    private final String playerName;
    private final LeaderboardEntry[] topEntries = new LeaderboardEntry[10];
    public static final int MAX_ENTRIES = 10;

    public LeaderboardEntry(PlayerProfile playerProfile, String playerName) {
        this.playerProfile = playerProfile;
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public LeaderboardEntry[] getTopEntries() {
        return this.topEntries;
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    public void addEntry(LeaderboardEntry entry, int position) {
        if (position >= 0 && position < MAX_ENTRIES) {
            this.topEntries[position] = entry;
        }
    }

    public LeaderboardEntry getEntry(int position) {
        if (position >= 0 && position < MAX_ENTRIES) {
            return this.topEntries[position];
        }
        return null;
    }
}