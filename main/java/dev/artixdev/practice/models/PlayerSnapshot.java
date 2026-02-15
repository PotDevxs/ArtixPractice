package dev.artixdev.practice.models;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongArrayList;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public final class PlayerSnapshot {
    
    @SerializedName("points")
    private int points;
    @SerializedName("combo")
    private int combo;
    private final transient List<Long> hitTimes;
    private transient boolean isInCombat;
    @SerializedName("lives")
    private int lives;
    @SerializedName("username")
    private final String username;
    @SerializedName("blockedHits")
    private int blockedHits;
    @SerializedName("deaths")
    private int deaths;
    private final transient AtomicLong lastHitTime;
    private transient boolean isAlive;
    @SerializedName("bot")
    private boolean isBot;
    private transient Location location;
    private transient int ping;
    @SerializedName("longestCombo")
    private int longestCombo;
    @SerializedName("kills")
    private int kills;
    private transient boolean isSpectating;
    @SerializedName("potionsThrown")
    private int potionsThrown;
    @SerializedName("hits")
    private int hits;
    private transient boolean isFrozen;
    private transient Skin skin;
    private transient boolean isDisguised;

    public PlayerSnapshot(Player player) {
        this.username = player.getName();
        this.hitTimes = new LongArrayList();
        this.lastHitTime = new AtomicLong();
        this.isAlive = true;
        this.isInCombat = false;
        this.isSpectating = false;
        this.isFrozen = false;
        this.isDisguised = false;
        this.isBot = false;
        this.points = 0;
        this.combo = 0;
        this.lives = 1;
        this.blockedHits = 0;
        this.deaths = 0;
        this.longestCombo = 0;
        this.kills = 0;
        this.potionsThrown = 0;
        this.hits = 0;
        this.ping = 0;
        this.location = player.getLocation();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public List<Long> getHitTimes() {
        return hitTimes;
    }

    public boolean isInCombat() {
        return isInCombat;
    }

    public void setInCombat(boolean inCombat) {
        this.isInCombat = inCombat;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public String getUsername() {
        return username;
    }

    public int getBlockedHits() {
        return blockedHits;
    }

    public void setBlockedHits(int blockedHits) {
        this.blockedHits = blockedHits;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public AtomicLong getLastHitTime() {
        return lastHitTime;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        this.isBot = bot;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getLongestCombo() {
        return longestCombo;
    }

    public void setLongestCombo(int longestCombo) {
        this.longestCombo = longestCombo;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

    public void setSpectating(boolean spectating) {
        this.isSpectating = spectating;
    }

    public int getPotionsThrown() {
        return potionsThrown;
    }

    public void setPotionsThrown(int potionsThrown) {
        this.potionsThrown = potionsThrown;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        this.isFrozen = frozen;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public boolean isDisguised() {
        return isDisguised;
    }

    public void setDisguised(boolean disguised) {
        this.isDisguised = disguised;
    }
}
