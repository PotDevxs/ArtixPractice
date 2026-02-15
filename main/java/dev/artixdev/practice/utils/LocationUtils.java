package dev.artixdev.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationUtils {
    
    private static int lastId = -1;

    private LocationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Get the next unique ID
     */
    public static int getNextId() {
        int currentId = lastId;
        lastId = (currentId & ~1) - (~currentId & 1);
        return currentId;
    }

    /**
     * Calculate the difference between two locations
     */
    public static double[] calculateDifference(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return new double[]{0.0, 0.0, 0.0};
        }
        
        double x = location1.getX() - location2.getX();
        double y = location1.getY() - location2.getY();
        double z = location1.getZ() - location2.getZ();
        
        return new double[]{x, y, z};
    }

    /**
     * Clone a location safely
     */
    public static Location cloneLocation(Location location) {
        if (location == null) {
            return null;
        }
        return location.clone();
    }

    /**
     * Check if two locations are equal
     */
    public static boolean isLocationEqual(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return loc1 == loc2;
        }
        
        return loc1.getWorld().equals(loc2.getWorld()) &&
               loc1.getX() == loc2.getX() &&
               loc1.getY() == loc2.getY() &&
               loc1.getZ() == loc2.getZ() &&
               loc1.getYaw() == loc2.getYaw() &&
               loc1.getPitch() == loc2.getPitch();
    }

    /**
     * Calculate distance between two locations
     */
    public static double calculateDistance(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null || !loc1.getWorld().equals(loc2.getWorld())) {
            return Double.MAX_VALUE;
        }
        
        return loc1.distance(loc2);
    }

    /**
     * Check if location is within bounds
     */
    public static boolean isWithinBounds(Location location, Location center, double radius) {
        if (location == null || center == null || !location.getWorld().equals(center.getWorld())) {
            return false;
        }
        
        return calculateDistance(location, center) <= radius;
    }

    /**
     * Get center location between two points
     */
    public static Location getCenterLocation(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null || !loc1.getWorld().equals(loc2.getWorld())) {
            return null;
        }
        
        double x = (loc1.getX() + loc2.getX()) / 2.0;
        double y = (loc1.getY() + loc2.getY()) / 2.0;
        double z = (loc1.getZ() + loc2.getZ()) / 2.0;
        float yaw = (loc1.getYaw() + loc2.getYaw()) / 2.0f;
        float pitch = (loc1.getPitch() + loc2.getPitch()) / 2.0f;
        
        return new Location(loc1.getWorld(), x, y, z, yaw, pitch);
    }

    /**
     * Get location with offset
     */
    public static Location getLocationWithOffset(Location location, double x, double y, double z) {
        if (location == null) {
            return null;
        }
        
        return location.clone().add(x, y, z);
    }

    /**
     * Get location with offset
     */
    public static Location getLocationWithOffset(Location location, double x, double y, double z, float yaw, float pitch) {
        if (location == null) {
            return null;
        }
        
        Location newLocation = location.clone().add(x, y, z);
        newLocation.setYaw(yaw);
        newLocation.setPitch(pitch);
        return newLocation;
    }

    /**
     * Check if location is safe (not in void, not in lava, etc.)
     */
    public static boolean isLocationSafe(Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }
        
        // Check if location is in void
        if (location.getY() < 0 || location.getY() > location.getWorld().getMaxHeight()) {
            return false;
        }
        
        // Check if location is in lava
        if (location.getBlock().isLiquid()) {
            return false;
        }
        
        // Check if location has solid ground below
        Location below = location.clone().subtract(0, 1, 0);
        return below.getBlock().getType().isSolid();
    }

    /**
     * Find safe location near the given location
     */
    public static Location findSafeLocation(Location location, int maxAttempts) {
        if (location == null) {
            return null;
        }
        
        for (int i = 0; i < maxAttempts; i++) {
            Location testLocation = location.clone().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            
            if (isLocationSafe(testLocation)) {
                return testLocation;
            }
        }
        
        return location; // Return original if no safe location found
    }
    
    /**
     * Serialize Location to String
     * Format: world:x:y:z:yaw:pitch
     * @param location the location to serialize
     * @return string representation of the location
     */
    public static String serializeLocation(Location location) {
        if (location == null || location.getWorld() == null) {
            return null;
        }
        return location.getWorld().getName() + ":" + 
               location.getX() + ":" + 
               location.getY() + ":" + 
               location.getZ() + ":" + 
               location.getYaw() + ":" + 
               location.getPitch();
    }
    
    /**
     * Deserialize String to Location
     * Format: world:x:y:z:yaw:pitch
     * @param locationString the string representation of the location
     * @return Location object or null if invalid
     */
    public static Location deserializeLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) {
            return null;
        }
        
        String[] parts = locationString.split(":");
        if (parts.length != 6) {
            return null;
        }
        
        try {
            String worldName = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                return null;
            }
            
            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}