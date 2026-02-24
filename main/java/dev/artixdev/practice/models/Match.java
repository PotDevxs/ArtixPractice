package dev.artixdev.practice.models;

import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import dev.artixdev.practice.enums.KitType;

import net.citizensnpcs.api.npc.NPC;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * Match Model
 * Represents a practice match
 */
public class Match {
    
    private UUID id;
    private Player player1;
    private Player player2;
    private KitType kitType;
    private long startTime;
    private long endTime;
    private boolean ended;
    private Player winner;
    private Player loser;
    private boolean enderPearlEnabled = true;
    private double distance = 0.0;
    private Kit kit;
    private Location location;
    private EnderPearl enderPearl;
    private NPC bot;
    private Object botState;

    /** Warmup: no damage until this time (ms). 0 = no warmup. */
    private long warmupEndTime;
    /** Match paused (e.g. /pause). */
    private boolean paused;
    /** Time left in match (seconds). -1 = no limit. */
    private int timeLeft = -1;
    /** Current match duration in ticks/seconds for display. */
    private int matchTime;
    private Arena arena;
    private PlayerLocationTracker locationTracker;

    public Match(UUID id, Player player1, Player player2, KitType kitType) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.kitType = kitType;
        this.startTime = System.currentTimeMillis();
        this.ended = false;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Alias for {@link #getId()} for code that expects getMatchId().
     */
    public UUID getMatchId() {
        return id;
    }
    
    public Player getPlayer1() {
        return player1;
    }
    
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
    
    public KitType getKitType() {
        return kitType;
    }
    
    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    public boolean isEnded() {
        return ended;
    }
    
    public void setEnded(boolean ended) {
        this.ended = ended;
    }
    
    public Player getWinner() {
        return winner;
    }
    
    public void setWinner(Player winner) {
        this.winner = winner;
    }
    
    public Player getLoser() {
        return loser;
    }
    
    public void setLoser(Player loser) {
        this.loser = loser;
    }

    public boolean isEnderPearlEnabled() {
        return enderPearlEnabled;
    }

    public void setEnderPearlEnabled(boolean enderPearlEnabled) {
        this.enderPearlEnabled = enderPearlEnabled;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EnderPearl getEnderPearl() {
        return enderPearl;
    }

    public void setEnderPearl(EnderPearl enderPearl) {
        this.enderPearl = enderPearl;
    }

    public NPC getBot() {
        return bot;
    }

    public void setBot(NPC bot) {
        this.bot = bot;
    }

    public Object getBotState() {
        return botState;
    }

    public void setBotState(Object botState) {
        this.botState = botState;
    }

    public long getDuration() {
        if (ended) {
            return endTime - startTime;
        }
        return System.currentTimeMillis() - startTime;
    }
    
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        if (player1 != null) players.add(player1);
        if (player2 != null) players.add(player2);
        return players;
    }

    public long getWarmupEndTime() {
        return warmupEndTime;
    }

    public void setWarmupEndTime(long warmupEndTime) {
        this.warmupEndTime = warmupEndTime;
    }

    public boolean isInWarmup() {
        return warmupEndTime > 0 && System.currentTimeMillis() < warmupEndTime;
    }

    public int getWarmupSecondsRemaining() {
        if (warmupEndTime <= 0) return 0;
        long rem = (warmupEndTime - System.currentTimeMillis()) / 1000;
        return rem <= 0 ? 0 : (int) rem;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void endMatch() {
        this.ended = true;
        this.endTime = System.currentTimeMillis();
    }

    public void decrementTimeLeft() {
        if (timeLeft > 0) timeLeft--;
    }

    public void updateMatch() {
        // Optional: update distance, etc.
    }

    public Player getPlayer() {
        return player1 != null ? player1 : player2;
    }

    public void incrementMatchTime() {
        matchTime++;
    }

    public int getMatchTime() {
        return matchTime;
    }

    public boolean isActive() {
        return !ended;
    }

    public PlayerLocationTracker getLocationTracker() {
        return locationTracker;
    }

    public void setLocationTracker(PlayerLocationTracker locationTracker) {
        this.locationTracker = locationTracker;
    }

    public String getMaxPlayers() {
        return String.valueOf(Math.max(2, getPlayers().size()));
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }
}
