package dev.artixdev.practice.scoreboard;

import java.util.List;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardLine {
    private int position;
    public static final boolean DEBUG = false;
    private Team team;
    private String identifier;
    private final ScoreboardManager scoreboardManager;
    public static final int MAX_POSITION = 15;
    private String text;

    public void send(int position) {
        this.position = position;
        if (team != null) {
            team.setPrefix(text);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setup() {
        if (scoreboardManager.getScoreboard() != null) {
            String teamName = "line_" + position;
            team = scoreboardManager.getScoreboard().getTeam(teamName);
            
            if (team == null) {
                team = scoreboardManager.getScoreboard().registerNewTeam(teamName);
            }
            
            team.addEntry(identifier);
            team.setPrefix(text);
        }
    }

    public void remove() {
        List<String> identifiers = scoreboardManager.getIdentifiers();
        if (team != null) {
            team.unregister();
            team = null;
        }
        
        if (identifier != null && identifiers.contains(identifier)) {
            identifiers.remove(identifier);
        }
    }

    public String getText() {
        return text;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Team getTeam() {
        return team;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ScoreboardLine(ScoreboardManager scoreboardManager, String text) {
        this.scoreboardManager = scoreboardManager;
        this.text = text;
        this.identifier = generateIdentifier();
    }

    private String generateIdentifier() {
        return "§" + position + "§" + System.currentTimeMillis();
    }
}
