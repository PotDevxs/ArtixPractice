package dev.artixdev.practice.utils.other;

public enum CallbackPriority {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    private final int priority;

    CallbackPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
