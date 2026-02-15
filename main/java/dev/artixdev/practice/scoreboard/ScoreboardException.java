package dev.artixdev.practice.scoreboard;

public class ScoreboardException extends RuntimeException {
    public static final int ERROR_CODE = 1001;
    public static final boolean DEBUG = false;

    public ScoreboardException(String message) {
        super(message);
    }

    public ScoreboardException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScoreboardException(Throwable cause) {
        super(cause);
    }
}
