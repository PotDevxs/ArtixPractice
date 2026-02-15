package dev.artixdev.practice.scoreboard;

import java.util.List;
import org.bukkit.entity.Player;

public interface ScoreboardAdapter {
    boolean ENABLED = false;
    int MAX_LINES = 15;

    String getTitle(Player player);

    List<String> getLines(Player player);
}
