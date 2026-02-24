package dev.artixdev.practice.enums;

/**
 * Unlockable titles shown in nametag/chat. Requirement matches achievement or stat.
 */
public enum PlayerTitle {
    NONE("none", "", "none", 0),
    ROOKIE("rookie", "&7[Rookie]", "totalMatches", 1),
    KILLER("killer", "&6[Killer]", "kills", 100),
    ASSASSIN("assassin", "&c[Assassin]", "kills", 1000),
    CHAMPION("champion", "&e[Champion]", "wins", 100),
    INVINCIBLE("invincible", "&d[Invincible]", "bestWinStreak", 20),
    MASTER("master", "&b[Master]", "elo", 2000),
    LEGEND("legend", "&5&l[Legend]", "wins", 500);

    private final String id;
    private final String display;
    private final String requirementKey;
    private final int requirementValue;

    PlayerTitle(String id, String display, String requirementKey, int requirementValue) {
        this.id = id;
        this.display = display;
        this.requirementKey = requirementKey;
        this.requirementValue = requirementValue;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public String getRequirementKey() {
        return requirementKey;
    }

    public int getRequirementValue() {
        return requirementValue;
    }

    public static PlayerTitle fromId(String id) {
        if (id == null || id.isEmpty()) return NONE;
        for (PlayerTitle t : values()) {
            if (t.id.equalsIgnoreCase(id)) return t;
        }
        return NONE;
    }
}
