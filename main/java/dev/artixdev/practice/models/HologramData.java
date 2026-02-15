package dev.artixdev.practice.models;

import java.util.UUID;
import org.bukkit.Location;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

public class HologramData {
    
    @SerializedName("_id")
    private final UUID uniqueId;
    private transient HologramInterface hologram;
    private String name;
    private Location location;

    public HologramData(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public HologramInterface getHologram() {
        return this.hologram;
    }

    public void setHologram(HologramInterface hologram) {
        this.hologram = hologram;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Create a default hologram if none exists
     */
    public void createDefaultHologram() {
        if (this.hologram == null) {
            this.hologram = new DefaultHologram(this.uniqueId.toString());
        }
    }
    
    /**
     * Add a line to the hologram
     * @param line the line to add
     */
    public void addHologramLine(String line) {
        createDefaultHologram();
        this.hologram.addLine(line);
    }
    
    /**
     * Set hologram lines
     * @param lines the lines to set
     */
    public void setHologramLines(String[] lines) {
        createDefaultHologram();
        this.hologram.setLines(lines);
    }
    
    /**
     * Clear all hologram lines
     */
    public void clearHologramLines() {
        if (this.hologram != null) {
            this.hologram.clearLines();
        }
    }
    
    /**
     * Set hologram visibility
     * @param visible the visibility
     */
    public void setHologramVisible(boolean visible) {
        if (this.hologram != null) {
            this.hologram.setVisible(visible);
        }
    }
    
    /**
     * Check if hologram is visible
     * @return true if visible
     */
    public boolean isHologramVisible() {
        return this.hologram != null && this.hologram.isVisible();
    }
    
    /**
     * Delete the hologram
     */
    public void deleteHologram() {
        if (this.hologram != null) {
            this.hologram.delete();
        }
    }
    
    /**
     * Check if hologram is deleted
     * @return true if deleted
     */
    public boolean isHologramDeleted() {
        return this.hologram != null && this.hologram.isDeleted();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (!(obj instanceof HologramData)) {
            return false;
        }
        
        HologramData other = (HologramData) obj;
        if (!other.canEqual(this)) {
            return false;
        }
        
        UUID thisId = this.getUniqueId();
        UUID otherId = other.getUniqueId();
        
        if (thisId == null) {
            if (otherId != null) {
                return false;
            }
        } else if (!thisId.equals(otherId)) {
            return false;
        }
        
        Location thisLocation = this.getLocation();
        Location otherLocation = other.getLocation();
        
        if (thisLocation == null) {
            if (otherLocation != null) {
                return false;
            }
        } else if (!thisLocation.equals(otherLocation)) {
            return false;
        }
        
        String thisName = this.getName();
        String otherName = other.getName();
        
        if (thisName == null) {
            if (otherName != null) {
                return false;
            }
        } else if (!thisName.equals(otherName)) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        UUID id = this.getUniqueId();
        result = result * 59 + (id == null ? 43 : id.hashCode());
        
        Location loc = this.getLocation();
        result = result * 59 + (loc == null ? 43 : loc.hashCode());
        
        String name = this.getName();
        result = result * 59 + (name == null ? 43 : name.hashCode());
        
        return result;
    }

    @Override
    public String toString() {
        return "HologramData{" +
                "uniqueId=" + uniqueId +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }

    protected boolean canEqual(Object obj) {
        return obj instanceof HologramData;
    }
}
