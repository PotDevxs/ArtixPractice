package dev.artixdev.practice.managers;

import org.bukkit.event.Listener;

/**
 * Stub block listener for areas. Extend with real logic if needed.
 */
public class AreaBlockListener implements Listener {
    private final AreaManager areaManager;

    public AreaBlockListener(AreaManager areaManager) {
        this.areaManager = areaManager;
    }
}
