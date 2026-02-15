package dev.artixdev.practice.enums;

public enum MatchType {
    
    SOLO("Solo"),
    DUO("Duo"),
    TEAM("Team"),
    FFA("Free For All"),
    RANKED("Ranked");

    private final String displayName;

    MatchType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}