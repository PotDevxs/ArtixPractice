package dev.artixdev.practice.enums;

/**
 * LeaderboardType
 * Enum for different types of leaderboards
 */
public enum LeaderboardType {
    ELO,
    WINS,
    KILLS,
    DEATHS,
    WINSTREAK,
    HIGHEST_WINSTREAK;
    
    /**
     * Get display name for leaderboard type
     * @return display name
     */
    public String getDisplayName() {
        switch (this) {
            case ELO:
                return "ELO";
            case WINS:
                return "Wins";
            case KILLS:
                return "Kills";
            case DEATHS:
                return "Deaths";
            case WINSTREAK:
                return "Winstreak";
            case HIGHEST_WINSTREAK:
                return "Highest Winstreak";
            default:
                return name();
        }
    }
}
