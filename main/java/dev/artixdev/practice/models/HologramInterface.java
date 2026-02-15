package dev.artixdev.practice.models;

import org.bukkit.Location;

/**
 * Hologram Interface
 * Abstract interface for hologram functionality
 * This allows the system to work without the HolographicDisplays dependency
 */
public interface HologramInterface {
    
    /**
     * Get the hologram ID
     * @return hologram ID
     */
    String getId();
    
    /**
     * Get the hologram location
     * @return location or null
     */
    Location getLocation();
    
    /**
     * Set the hologram location
     * @param location the location
     */
    void setLocation(Location location);
    
    /**
     * Get the hologram name
     * @return name (may default to id)
     */
    String getName();
    
    /**
     * Set the hologram name
     * @param name the name
     */
    void setName(String name);
    
    /**
     * Check if the hologram is enabled
     * @return true if enabled
     */
    boolean isEnabled();
    
    /**
     * Set the hologram enabled state
     * @param enabled the enabled state
     */
    void setEnabled(boolean enabled);
    
    /**
     * Set the hologram ID
     * @param id the hologram ID
     */
    void setId(String id);
    
    /**
     * Get the hologram lines
     * @return hologram lines
     */
    String[] getLines();
    
    /**
     * Set the hologram lines
     * @param lines the hologram lines
     */
    void setLines(String[] lines);
    
    /**
     * Add a line to the hologram
     * @param line the line to add
     */
    void addLine(String line);
    
    /**
     * Remove a line from the hologram
     * @param index the index of the line to remove
     */
    void removeLine(int index);
    
    /**
     * Clear all lines from the hologram
     */
    void clearLines();
    
    /**
     * Check if the hologram is visible
     * @return true if visible
     */
    boolean isVisible();
    
    /**
     * Set the hologram visibility
     * @param visible the visibility
     */
    void setVisible(boolean visible);
    
    /**
     * Delete the hologram
     */
    void delete();
    
    /**
     * Check if the hologram is deleted
     * @return true if deleted
     */
    boolean isDeleted();
}
