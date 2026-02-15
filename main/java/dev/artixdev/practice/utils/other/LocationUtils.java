package dev.artixdev.practice.utils.other;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * LocationUtils
 * 
 * Utility class for location-related operations.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class LocationUtils {
    
    /**
     * Check if two players are within a certain distance of each other.
     * 
     * @param player1 the first player
     * @param player2 the second player
     * @param distance the maximum distance
     * @return true if players are within distance
     */
    public static boolean isWithinDistance(Player player1, Player player2, double distance) {
        if (player1 == null || player2 == null) {
            return false;
        }
        
        return player1.getEyeLocation().distance(player2.getLocation()) <= distance
            || player1.getLocation().distance(player2.getLocation()) <= distance;
    }
    
    /**
     * Check if a location is behind another location based on yaw.
     * 
     * @param location the location to check
     * @param targetLocation the target location
     * @return true if location is behind targetLocation
     */
    public static boolean isBehind(Location location, Location targetLocation) {
        if (location == null || targetLocation == null) {
            return false;
        }
        
        double dx = targetLocation.getX() - location.getX();
        double dz = targetLocation.getZ() - location.getZ();
        
        double angle = Math.acos(dz / Math.sqrt(dx * dx + dz * dz));
        angle = Math.toDegrees(angle);
        
        if (dx > 0.0) {
            angle = -angle;
        }
        
        double yawDiff = Math.abs(location.getYaw() - angle);
        return yawDiff > 90.0;
    }
    
    /**
     * Normalize a vector to a specific magnitude.
     * 
     * @param vector the vector to normalize
     * @param magnitude the desired magnitude
     */
    public static void normalizeVector(Vector vector, double magnitude) {
        if (vector == null) {
            return;
        }
        vector.normalize().multiply(magnitude);
    }
    
    /**
     * Calculate the angle difference between two angles.
     * 
     * @param angle1 the first angle
     * @param angle2 the second angle
     * @return the difference between the angles
     */
    public static double getAngleDifference(double angle1, double angle2) {
        double diff = angle2 - angle1;
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;
        return diff;
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private LocationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
