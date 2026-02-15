package dev.artixdev.practice.scoreboard;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardHandler {
    public static final int MAX_BOARDS = 1000;
    private static final String[] CONFIG_KEYS = new String[0];
    private boolean enabled;
    private JavaPlugin plugin;
    private ScoreboardAdapter adapter;
    private ScoreboardStyle style;
    public static final boolean DEBUG_MODE = false;
    private ScoreboardManager manager;
    private boolean debugMode;
    private ScoreboardThread thread;
    private Map<UUID, ScoreboardManager> boards;
    private long ticks;
    private static final String[] MESSAGES = new String[0];

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public Map<UUID, ScoreboardManager> getBoards() {
        return boards;
    }

    public ScoreboardStyle getAssembleStyle() {
        return style;
    }

    public void setAdapter(ScoreboardAdapter adapter) {
        this.adapter = adapter;
    }

    public void cleanup() {
        if (thread != null) {
            thread.stop();
        }
        
        for (ScoreboardManager board : boards.values()) {
            board.clearLines();
        }
        
        boards.clear();
    }

    public void start() {
        if (enabled) {
            thread = new ScoreboardThread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public void addBoard(UUID playerId) {
        if (adapter != null) {
            ScoreboardManager board = new ScoreboardManager(playerId, adapter);
            boards.put(playerId, board);
        }
    }

    public void removeBoard(UUID playerId) {
        ScoreboardManager board = boards.remove(playerId);
        if (board != null) {
            board.clearLines();
        }
    }

    public ScoreboardManager getBoard(UUID playerId) {
        return boards.get(playerId);
    }

    public void updateBoard(UUID playerId) {
        ScoreboardManager board = boards.get(playerId);
        if (board != null) {
            board.updateLines(null); // Player will be passed by adapter
        }
    }

    public void updateAllBoards() {
        for (ScoreboardManager board : boards.values()) {
            board.updateLines(null);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ScoreboardAdapter getAdapter() {
        return adapter;
    }

    public ScoreboardStyle getStyle() {
        return style;
    }

    public void setStyle(ScoreboardStyle style) {
        this.style = style;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public long getTicks() {
        return ticks;
    }

    public ScoreboardHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.boards = new ConcurrentHashMap<>();
        this.style = ScoreboardStyle.MODERN;
        this.enabled = true;
    }
}
