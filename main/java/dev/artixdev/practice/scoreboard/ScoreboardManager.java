package dev.artixdev.practice.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {
    public static final int MAX_LINES = 15;
    private final List<ScoreboardLine> lines = new ArrayList<>();
    private final ScoreboardAdapter adapter;
    public static final boolean DEBUG_MODE = false;
    private Scoreboard scoreboard;
    private static final String[] EMPTY_STRINGS = new String[0];
    private final UUID playerId;
    private static final String[] DEFAULT_LINES = new String[2];
    private final List<String> identifiers = new ArrayList<>();

    public void setupScoreboard() {
        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        
        Objective objective = scoreboard.getObjective("practice");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("practice", "dummy");
            objective.setDisplayName(adapter.getTitle(null));
        }
        
        for (int i = 0; i < lines.size(); i++) {
            ScoreboardLine line = lines.get(i);
            line.setup();
            objective.getScore(line.getText()).setScore(15 - i);
        }
    }

    public Scoreboard getScoreboard() {
        if (scoreboard == null) {
            setupScoreboard();
        }
        return scoreboard;
    }

    public void addLine(String text) {
        ScoreboardLine line = new ScoreboardLine(this, text);
        lines.add(line);
        identifiers.add(line.getIdentifier());
    }

    public void removeLine(int index) {
        if (index >= 0 && index < lines.size()) {
            ScoreboardLine line = lines.get(index);
            line.remove();
            lines.remove(index);
            identifiers.remove(line.getIdentifier());
        }
    }

    public void clearLines() {
        for (ScoreboardLine line : lines) {
            line.remove();
        }
        lines.clear();
        identifiers.clear();
    }

    public void updateLines(Player player) {
        List<String> newLines = adapter.getLines(player);
        clearLines();
        
        for (String line : newLines) {
            addLine(line);
        }
        
        setupScoreboard();
    }

    public List<ScoreboardLine> getLines() {
        return new ArrayList<>(lines);
    }

    public List<String> getIdentifiers() {
        return new ArrayList<>(identifiers);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public ScoreboardAdapter getAdapter() {
        return adapter;
    }

    public ScoreboardManager(UUID playerId, ScoreboardAdapter adapter) {
        this.playerId = playerId;
        this.adapter = adapter;
    }
}
