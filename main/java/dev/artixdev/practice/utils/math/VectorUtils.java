package dev.artixdev.practice.utils.math;

import org.bukkit.util.Vector;

public final class VectorUtils {
    private static final String[] VECTOR_CONSTANTS = {"VectorUtils"};
    public static final int VECTOR_UTILS_VERSION = 1;
    public static final boolean DEBUG_MODE = false;

    private VectorUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static Vector calculateNormal(Vector point1, Vector point2, Vector point3) {
        Vector v1 = point1.clone();
        Vector v2 = point2.clone();
        Vector v3 = point3.clone();
        
        Vector edge1 = v1.subtract(v2);
        Vector edge2 = v2.subtract(v3);
        
        return edge1.crossProduct(edge2).normalize();
    }

    /**
     * Calculate normal vector from 5 points (average of multiple triangle normals).
     * 
     * @param point1 first point
     * @param point2 second point
     * @param point3 third point
     * @param point4 fourth point
     * @param point5 fifth point
     * @return normalized normal vector
     */
    public static Vector calculateNormal(Vector point1, Vector point2, Vector point3, Vector point4, Vector point5) {
        if (point1 == null || point2 == null || point3 == null || point4 == null || point5 == null) {
            return new Vector(0, 1, 0);
        }
        
        // Calculate normal from first triangle
        Vector normal1 = calculateNormal(point1, point2, point3);
        
        // Calculate normal from second triangle
        Vector normal2 = calculateNormal(point3, point4, point5);
        
        // Average the normals
        return normal1.add(normal2).normalize();
    }
    
    /**
     * Calculate normal vector from two points and a reference normal.
     * Projects the normal onto the plane defined by the two points.
     * 
     * @param point1 first point
     * @param point2 second point
     * @param referenceNormal reference normal vector
     * @return normalized normal vector
     */
    public static Vector calculateNormalFromReference(Vector point1, Vector point2, Vector referenceNormal) {
        if (point1 == null || point2 == null || referenceNormal == null) {
            return new Vector(0, 1, 0);
        }
        
        // Calculate direction vector
        Vector direction = point2.clone().subtract(point1).normalize();
        
        // Project reference normal onto plane perpendicular to direction
        Vector projected = referenceNormal.clone().subtract(
            direction.clone().multiply(referenceNormal.dot(direction))
        );
        
        return projected.length() > 0 ? projected.normalize() : new Vector(0, 1, 0);
    }
}
