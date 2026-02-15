package dev.artixdev.practice.utils.math;

import org.bukkit.util.Vector;

public enum Vector3DType {
    NORMAL(new Vector(0, 0, 0), new Vector(1, 1, 1), 1.0, 1.0, 1.0),
    INVERTED(new Vector(1, 1, 1), new Vector(0, 0, 0), -1.0, -1.0, -1.0);

    private final Vector minVector;
    private final Vector maxVector;
    private final double xMultiplier;
    private final double yMultiplier;
    private final double zMultiplier;

    Vector3DType(Vector minVector, Vector maxVector, double xMultiplier, double yMultiplier, double zMultiplier) {
        this.minVector = minVector;
        this.maxVector = maxVector;
        this.xMultiplier = xMultiplier;
        this.yMultiplier = yMultiplier;
        this.zMultiplier = zMultiplier;
    }

    public Vector getMinVector() {
        return this.minVector;
    }

    public Vector getMaxVector() {
        return this.maxVector;
    }

    public double getXMultiplier() {
        return this.xMultiplier;
    }

    public double getYMultiplier() {
        return this.yMultiplier;
    }

    public double getZMultiplier() {
        return this.zMultiplier;
    }
}