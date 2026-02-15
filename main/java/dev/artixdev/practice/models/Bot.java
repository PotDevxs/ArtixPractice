package dev.artixdev.practice.models;

import org.bukkit.entity.Player;
import dev.artixdev.practice.enums.KitType;

import java.util.UUID;

/**
 * Bot Model
 * Represents a practice bot
 */
public class Bot {
    
    private UUID id;
    private String name;
    private KitType kitType;
    private int difficulty;
    private Player player;
    private boolean active;
    
    public Bot(UUID id, String name, KitType kitType, int difficulty) {
        this.id = id;
        this.name = name;
        this.kitType = kitType;
        this.difficulty = difficulty;
        this.active = false;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public KitType getKitType() {
        return kitType;
    }
    
    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
