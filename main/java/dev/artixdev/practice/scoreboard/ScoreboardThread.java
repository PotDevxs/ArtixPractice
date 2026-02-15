package dev.artixdev.practice.scoreboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreboardThread extends Thread {
    private static final String[] THREAD_NAMES = new String[] {"ScoreboardThread", "ScoreboardUpdateThread"};
    private static final Logger LOGGER = LogManager.getLogger(ScoreboardThread.class);
    private volatile boolean running;
    private final ScoreboardHandler scoreboardHandler;
    public static final int THREAD_ID = 1;
    private static final String[] MESSAGES = new String[] {"Scoreboard thread started", "Scoreboard thread stopped"};

    public ScoreboardThread(ScoreboardHandler scoreboardHandler) {
        super(THREAD_NAMES[0]);
        this.scoreboardHandler = scoreboardHandler;
        this.running = true;
    }

    @Override
    public void run() {
        LOGGER.info(MESSAGES[0]);
        
        while (running) {
            try {
                if (scoreboardHandler.isEnabled()) {
                    scoreboardHandler.updateAllBoards();
                }
                
                Thread.sleep(20L); // 1 tick = 50ms, but we use 20ms for smoother updates
            } catch (InterruptedException e) {
                LOGGER.warn("Scoreboard thread interrupted", e);
                break;
            } catch (Exception e) {
                LOGGER.error("Error in scoreboard thread", e);
            }
        }
        
        LOGGER.info(MESSAGES[1]);
    }

    public void shutdown() {
        this.running = false;
        this.interrupt();
    }

    public boolean isRunning() {
        return running;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }
}
