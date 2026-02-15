package dev.artixdev.practice.models;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

/**
 * Default Hologram Implementation
 * Basic implementation of HologramInterface
 */
public class DefaultHologram implements HologramInterface {
    
    private String id;
    private String name;
    private Location location;
    private List<String> lines;
    private boolean visible;
    private boolean deleted;
    private boolean enabled = true;
    
    public DefaultHologram(String id) {
        this.id = id;
        this.name = id;
        this.lines = new ArrayList<>();
        this.visible = true;
        this.deleted = false;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public Location getLocation() {
        return location;
    }
    
    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
    
    @Override
    public String getName() {
        return name != null ? name : id;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public String[] getLines() {
        return lines.toArray(new String[0]);
    }
    
    @Override
    public void setLines(String[] lines) {
        this.lines.clear();
        for (String line : lines) {
            this.lines.add(line);
        }
    }
    
    @Override
    public void addLine(String line) {
        lines.add(line);
    }
    
    @Override
    public void removeLine(int index) {
        if (index >= 0 && index < lines.size()) {
            lines.remove(index);
        }
    }
    
    @Override
    public void clearLines() {
        lines.clear();
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    @Override
    public void delete() {
        this.deleted = true;
        this.visible = false;
    }
    
    @Override
    public boolean isDeleted() {
        return deleted;
    }
    
    @Override
    public String toString() {
        return "DefaultHologram{" +
                "id='" + id + '\'' +
                ", lines=" + lines.size() +
                ", visible=" + visible +
                ", deleted=" + deleted +
                '}';
    }
}
