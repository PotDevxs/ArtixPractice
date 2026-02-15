package dev.artixdev.practice.models;

/**
 * Minimal match result holder for ELO updates.
 */
public class MatchResult {
    private final int opponentElo;
    private final boolean winner;

    public MatchResult(int opponentElo, boolean winner) {
        this.opponentElo = opponentElo;
        this.winner = winner;
    }

    public int getOpponentElo() {
        return opponentElo;
    }

    public boolean isWinner() {
        return winner;
    }
}
