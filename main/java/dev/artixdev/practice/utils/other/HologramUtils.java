package dev.artixdev.practice.utils.other;

import java.lang.reflect.Method;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * HologramUtils
 * 
 * Utility class for hologram-related operations.
 * Note: This class uses reflection to work with HolographicDisplays API
 * to avoid direct dependency issues.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class HologramUtils {
    
    /**
     * Create a hologram at a specific location visible to specific players.
     * Uses reflection to work with HolographicDisplays API if available.
     * 
     * @param api the HolographicDisplays API instance (can be any Object)
     * @param location the location to create the hologram
     * @param viewers the list of players who can see the hologram
     * @return the created hologram object, or null if creation failed
     */
    public static Object createHologram(Object api, Location location, List<Player> viewers) {
        if (api == null || location == null) {
            return null;
        }
        
        try {
            // Use reflection to avoid direct dependency
            Class<?> apiClass = api.getClass();
            Method createMethod = apiClass.getMethod("createHologram", Location.class);
            Object hologram = createMethod.invoke(api, location);
            
            if (hologram != null && viewers != null && !viewers.isEmpty()) {
                try {
                    Method getVisibilityMethod = hologram.getClass().getMethod("getVisibilitySettings");
                    Object visibilitySettings = getVisibilityMethod.invoke(hologram);
                    
                    if (visibilitySettings != null) {
                        Method setVisibilityMethod = visibilitySettings.getClass()
                            .getMethod("setIndividualVisibility", Player.class, boolean.class);
                        
                        for (Player viewer : viewers) {
                            if (viewer != null) {
                                setVisibilityMethod.invoke(visibilitySettings, viewer, true);
                            }
                        }
                    }
                } catch (Exception e) {
                    // Visibility settings not available, continue anyway
                }
            }
            
            return hologram;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Create a hologram at a specific location visible to all players.
     * 
     * @param api the HolographicDisplays API instance (can be any Object)
     * @param location the location to create the hologram
     * @return the created hologram object, or null if creation failed
     */
    public static Object createHologram(Object api, Location location) {
        return createHologram(api, location, null);
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private HologramUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
