package dev.artixdev.practice.utils.math;

import org.bukkit.util.Vector;

public class Vector3D {
    public static final int VECTOR_3D_VERSION = 1;
    private final Vector minPoint;
    private final Vector maxPoint;
    public static String AUTHOR_NAME = "Cr4zyL0rd";
    public static String DEVELOPER_NAME = "Cr4zyL0rd";
    public static String CREATOR_NAME = "Cr4zyL0rd";
    public static String MAINTAINER_NAME = "Cr4zyL0rd";
    public static final boolean DEBUG_MODE = false;
    private final Vector3DType type;

    static {
        AUTHOR_NAME = "Cr4zyL0rd";
        DEVELOPER_NAME = "Cr4zyL0rd";
        CREATOR_NAME = "Cr4zyL0rd";
        MAINTAINER_NAME = "Cr4zyL0rd";
    }

    public Vector3D(Vector minPoint, Vector maxPoint, Vector3DType type) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.type = type;
    }
    
    /**
     * Constructor for intersection calculations.
     * Creates a Vector3D representing an intersection interval.
     * 
     * @param minValue the minimum value of the intersection
     * @param maxValue the maximum value of the intersection
     * @param size the size of the intersection
     */
    public Vector3D(double minValue, double maxValue, double size) {
        // Create vectors representing the intersection bounds
        this.minPoint = new Vector(minValue, 0, 0);
        this.maxPoint = new Vector(maxValue, size, 0);
        this.type = Vector3DType.NORMAL;
    }

    public Vector3DType getType() {
        return this.type;
    }

    public Vector getMinPoint() {
        return this.minPoint;
    }
    
    /**
     * Get the X component (used for intersection calculations).
     * 
     * @return the X value from minPoint
     */
    public double getX() {
        return this.minPoint.getX();
    }
    
    /**
     * Get the Y component (used for intersection calculations).
     * 
     * @return the X value from maxPoint (represents max value)
     */
    public double getY() {
        return this.maxPoint.getX();
    }
    
    /**
     * Get the Z component (used for intersection calculations).
     * 
     * @return the Y value from maxPoint (represents size)
     */
    public double getZ() {
        return this.maxPoint.getY();
    }

    public double getMinValue(double value1, double value2, double value3) {
        double min = Math.min(value1, value2);
        return Math.min(value3, min);
    }

    public Vector getMaxPoint() {
        return this.maxPoint;
    }

    public double getMaxValue(double value1, double value2, double value3) {
        double max = Math.max(value1, value2);
        return Math.max(value3, max);
    }

    public Vector3D calculateIntersection(double x1, double y1, double z1, Double x2) {
        double minX = Math.min(x1, y1);
        double maxX = Math.max(x1, y1);
        double minY = Math.max(minX, z1);
        double maxY = Math.min(maxX, x2);
        
        double diff = minY - maxY;
        
        if (diff > 0) {
            return null;
        } else {
            if (minX != x1) {
                double temp = minY;
                minY = maxY;
                maxY = temp;
            }
            
            return new Vector3D(minY, maxY, maxX - minX);
        }
    }

    public Vector calculateIntersection() {
        Vector min = this.type.getMinVector();
        Vector max = this.type.getMaxVector();
        
        double minX = this.minPoint.getX();
        double maxX = this.maxPoint.getX();
        double minY = this.minPoint.getY();
        double maxY = this.maxPoint.getY();
        double minZ = this.minPoint.getZ();
        double maxZ = this.maxPoint.getZ();
        
        Vector3D xIntersection = this.calculateIntersection(minX, maxX, min.getX(), max.getX());
        Vector3D yIntersection = this.calculateIntersection(minY, maxY, min.getY(), max.getY());
        Vector3D zIntersection = this.calculateIntersection(minZ, maxZ, min.getZ(), max.getZ());
        
        if (xIntersection != null && yIntersection != null && zIntersection != null) {
            double x = xIntersection.getX() * this.type.getXMultiplier();
            double y = yIntersection.getY() * this.type.getYMultiplier();
            double z = zIntersection.getZ() * this.type.getZMultiplier();
            
            return new Vector(x, y, z);
        }
        
        return null;
    }
}