package dev.artixdev.practice.models;

import org.bukkit.entity.Player;
import dev.artixdev.practice.enums.KitType;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * Queue Model
 * Represents a practice queue
 */
public class Queue {
    
    private UUID id;
    private KitType kitType;
    private boolean ranked;
    private List<Player> players;
    private long createdTime;
    
    public Queue(UUID id, KitType kitType, boolean ranked) {
        this.id = id;
        this.kitType = kitType;
        this.ranked = ranked;
        this.players = new ArrayList<>();
        this.createdTime = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public KitType getKitType() {
        return kitType;
    }
    
    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }
    
    public boolean isRanked() {
        return ranked;
    }
    
    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    
    public long getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
    
    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }
    
    public void removePlayer(Player player) {
        players.remove(player);
    }
    
    public int getSize() {
        return players.size();
    }
    
    public boolean isEmpty() {
        return players.isEmpty();
    }
}
