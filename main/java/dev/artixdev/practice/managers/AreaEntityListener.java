package dev.artixdev.practice.managers;

import org.bukkit.event.Listener;

/**
 * Stub entity listener for areas. Extend with real logic if needed.
 */
public class AreaEntityListener implements Listener {
    private final AreaManager areaManager;

    public AreaEntityListener(AreaManager areaManager) {
        this.areaManager = areaManager;
    }
}
