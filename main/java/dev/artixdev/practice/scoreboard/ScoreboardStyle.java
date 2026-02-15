package dev.artixdev.practice.scoreboard;

public enum ScoreboardStyle {
    MODERN(0, true),
    CLASSIC(1, false),
    MINIMAL(2, true);

    private final int id;
    private final boolean modern;

    ScoreboardStyle(int id, boolean modern) {
        this.id = id;
        this.modern = modern;
    }

    public int getId() {
        return id;
    }

    public boolean isModern() {
        return modern;
    }

    public static ScoreboardStyle getById(int id) {
        for (ScoreboardStyle style : values()) {
            if (style.id == id) {
                return style;
            }
        }
        return MODERN;
    }

    public static ScoreboardStyle getByName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MODERN;
        }
    }
}
