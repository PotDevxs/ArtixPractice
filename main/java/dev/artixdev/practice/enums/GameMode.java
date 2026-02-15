package dev.artixdev.practice.enums;

public enum GameMode {
    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3),
    HARD_CORE(4);

    private final byte value;

    private GameMode(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return this.value;
    }
}
