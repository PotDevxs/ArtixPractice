package dev.artixdev.practice.enums;

public enum HologramType {
    LEADERBOARD,
    STATISTICS,
    INFORMATION,
    GLOBAL_LEADERBOARD,
    KIT_LEADERBOARD;

    private static final String[] DISPLAY_NAMES = {
        "Leaderboard",
        "Statistics", 
        "Information",
        "Global Leaderboard",
        "Kit Leaderboard"
    };

    private static final String[] DESCRIPTIONS = {
        "Shows top players",
        "Shows player stats",
        "Shows general info",
        "Shows global leaderboard",
        "Shows kit-specific leaderboard"
    };

    public String getDisplayName() {
        return DISPLAY_NAMES[this.ordinal()];
    }

    public String getDescription() {
        return DESCRIPTIONS[this.ordinal()];
    }

    public static HologramType[] getAllTypes() {
        return new HologramType[]{
            LEADERBOARD,
            STATISTICS,
            INFORMATION,
            GLOBAL_LEADERBOARD,
            KIT_LEADERBOARD
        };
    }
}
