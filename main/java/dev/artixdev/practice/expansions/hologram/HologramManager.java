package dev.artixdev.practice.expansions.hologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.HologramInterface;

public class HologramManager extends AbstractHologram {

    private static final double DEFAULT_RENDERING_RANGE = 64.0;

    private final Map<String, HologramInterface> hologramsByName = new HashMap<>();
    private final Map<String, HologramInterface> hologramsByLocationKey = new HashMap<>();
    private double hologramRenderingRange = DEFAULT_RENDERING_RANGE;
    private boolean hologramRenderingEnabled = true;

    public HologramManager(Main plugin) {
        super(plugin);
    }

    @Override
    public void tick() {
        updateHolograms();
    }

    private void updateHolograms() {
        // Implementation for updating holograms
    }

    public HologramInterface getHologramAtLocation(Location location) {
        if (location == null) return null;
        String key = locationKey(location);
        return hologramsByLocationKey.get(key);
    }

    public HologramInterface getHologram(String name) {
        return name != null ? hologramsByName.get(name) : null;
    }

    public List<HologramInterface> getAllHolograms() {
        return new ArrayList<>(hologramsByName.values());
    }

    public int getHologramCount() {
        return hologramsByName.size();
    }

    public void createHologramLine(Player player, Location location, String text) {
        if (player == null || location == null || text == null) return;
        // Stub: NMS/ProtocolLib line creation would go here
    }

    public void removeHologramLine(Player player, Location location) {
        if (player == null || location == null) return;
        // Stub: remove line for player at location
    }

    public void clearHologramLines(Player player) {
        if (player == null) return;
        // Stub: clear all lines for player
    }

    public void clearAllHologramLines() {
        // Stub: clear lines for all players
    }

    public boolean isHologramRendered(Player player, Location location) {
        if (player == null || location == null) return false;
        return false; // Stub
    }

    public int getHologramRenderingCount(Player player) {
        if (player == null) return 0;
        return 0; // Stub
    }

    public int getTotalHologramRenderingCount() {
        return 0; // Stub
    }

    public void setHologramRenderingRange(double range) {
        this.hologramRenderingRange = range;
    }

    public double getHologramRenderingRange() {
        return hologramRenderingRange;
    }

    public void setHologramRenderingEnabled(boolean enabled) {
        this.hologramRenderingEnabled = enabled;
    }

    public boolean isHologramRenderingEnabled() {
        return hologramRenderingEnabled;
    }

    public void showHologram(String name, Player player) {
        if (name == null || player == null) return;
        // Stub: show hologram to player
    }

    public void hideHologram(String name, Player player) {
        if (name == null || player == null) return;
        // Stub: hide hologram from player
    }

    public void updateHologram(String name, Player player) {
        if (name == null || player == null) return;
        // Stub: refresh hologram for player
    }

    public void showHologramToAll(String name) {
        if (name == null) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            showHologram(name, player);
        }
    }

    public void hideHologramFromAll(String name) {
        if (name == null) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            hideHologram(name, player);
        }
    }

    public void updateHologramForAll(String name) {
        if (name == null) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateHologram(name, player);
        }
    }

    public boolean isHologramVisible(String name, Player player) {
        if (name == null || player == null) return false;
        return false; // Stub
    }

    public boolean enableHologram(String name) {
        if (name == null) return false;
        HologramInterface h = hologramsByName.get(name);
        if (h != null) {
            h.setEnabled(true);
            return true;
        }
        return false;
    }

    public boolean disableHologram(String name) {
        if (name == null) return false;
        HologramInterface h = hologramsByName.get(name);
        if (h != null) {
            h.setEnabled(false);
            return true;
        }
        return false;
    }

    /** Register a hologram for lookup by name and location. */
    public void registerHologram(HologramInterface hologram) {
        if (hologram == null) return;
        String name = hologram.getName();
        if (name != null) hologramsByName.put(name, hologram);
        Location loc = hologram.getLocation();
        if (loc != null) hologramsByLocationKey.put(locationKey(loc), hologram);
    }

    private static String locationKey(Location loc) {
        if (loc == null || loc.getWorld() == null) return "";
        return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }
}
