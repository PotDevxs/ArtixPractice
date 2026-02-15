package dev.artixdev.practice.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class VectorUtils {
    
    public static final int MAX_VECTOR_COMPONENT = 4;

    private VectorUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Check if a player is within a certain distance of a location
     */
    public static boolean isWithinDistance(Location location, Player player, double distance) {
        if (location == null || player == null) {
            return false;
        }
        
        return location.distance(player.getLocation()) <= distance;
    }

    /**
     * Create a vector from location and yaw
     */
    public static Vector createVector(Location location, float yaw) {
        if (location == null) {
            return new Vector();
        }
        
        double x = -Math.sin(Math.toRadians(yaw));
        double z = Math.cos(Math.toRadians(yaw));
        
        return new Vector(x, 0, z);
    }

    /**
     * Rotate a vector around a location
     */
    public static Vector rotateVector(Vector vector, Location location, double angle) {
        if (vector == null || location == null) {
            return new Vector();
        }
        
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        
        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;
        
        return new Vector(x, vector.getY(), z);
    }

    /**
     * Set vector components
     */
    public static Vector setComponents(Vector vector, double x, double y, double z) {
        setX(vector, x);
        setY(vector, y);
        setZ(vector, z);
        return vector;
    }

    /**
     * Set X component
     */
    public static Vector setX(Vector vector, double x) {
        if (vector != null) {
            vector.setX(x);
        }
        return vector;
    }

    /**
     * Set Y component
     */
    public static Vector setY(Vector vector, double y) {
        if (vector != null) {
            vector.setY(y);
        }
        return vector;
    }

    /**
     * Set Z component
     */
    public static Vector setZ(Vector vector, double z) {
        if (vector != null) {
            vector.setZ(z);
        }
        return vector;
    }

    /**
     * Calculate distance between two vectors
     */
    public static double calculateDistance(Vector vector1, Vector vector2, double x, double y) {
        if (vector1 == null || vector2 == null) {
            return 0.0;
        }
        
        double dx = vector2.getX() - vector1.getX();
        double dy = vector2.getY() - vector1.getY();
        double dz = vector2.getZ() - vector1.getZ();
        
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Calculate distance between two points
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Generate random vector
     */
    public static Vector generateRandomVector() {
        Vector vector = Vector.getRandom().multiply(2);
        vector.setX(vector.getX() - 1.0);
        vector.setY(vector.getY() - 1.0);
        vector.setZ(vector.getZ() - 1.0);
        return vector;
    }

    /**
     * Generate random double between -1 and 1
     */
    public static double generateRandomDouble() {
        return Math.random() * 2.0 - 1.0;
    }

    /**
     * Rotate vector around Y axis
     */
    public static Vector rotateAroundY(Vector vector, double angle) {
        if (vector == null) {
            return new Vector();
        }
        
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        
        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;
        
        return vector.setX(x).setZ(z);
    }

    /**
     * Validate vector components
     */
    public static void validateVector(Vector vector) throws IllegalArgumentException {
        if (vector == null) {
            throw new IllegalArgumentException("Vector cannot be null");
        }
        
        NumberConversions.checkFinite(vector.getX(), "X component must be finite");
        NumberConversions.checkFinite(vector.getY(), "Y component must be finite");
        NumberConversions.checkFinite(vector.getZ(), "Z component must be finite");
    }

    /**
     * Normalize angle to 0-360 range
     */
    public static float normalizeAngle(float angle) {
        angle = angle % 360.0f;
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    /**
     * Set vector components with validation
     */
    public static Vector setComponents(Vector vector, double x, double y) {
        setX(vector, x);
        setY(vector, y);
        return vector;
    }

    /**
     * Check if vector is within bounds
     */
    public static boolean isWithinBounds(Vector vector) {
        if (vector == null) {
            return false;
        }
        
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        
        return x >= -4.0 && x <= 4.0 && 
               y >= -4.0 && y <= 4.0 && 
               z >= -4.0 && z <= 4.0;
    }

    /**
     * Calculate angle between vectors
     */
    public static double calculateAngle(Vector vector) {
        if (vector == null) {
            return 0.0;
        }
        
        return Math.atan2(vector.getX(), vector.getZ());
    }

    /**
     * Create vector from angle
     */
    public static Vector createVectorFromAngle(double angle) {
        double x = Math.cos(angle);
        double z = Math.sin(angle);
        return new Vector(x, 0, z);
    }

    /**
     * Generate points between two vectors
     */
    public static List<Vector> generatePoints(Vector start, Vector end, double step, int maxPoints) {
        List<Vector> points = new ArrayList<>();
        
        if (start == null || end == null || step <= 0 || maxPoints <= 0) {
            return points;
        }
        
        Vector direction = end.clone().subtract(start);
        double distance = direction.length();
        int steps = Math.min((int) (distance / step), maxPoints);
        
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            Vector point = start.clone().add(direction.clone().multiply(t));
            points.add(point);
        }
        
        return points;
    }
}
