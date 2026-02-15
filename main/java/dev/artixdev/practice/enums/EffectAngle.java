package dev.artixdev.practice.enums;

public enum EffectAngle {
    
    ANGLE_60(1.0471975511965976D),
    ANGLE_45(0.7853981633974483D),
    ANGLE_36(0.6283185307179586D),
    ANGLE_30(0.5235987755982988D),
    ANGLE_24(0.41887902047863906D),
    ANGLE_22_5(0.39269908169872414D),
    ANGLE_18(0.3141592653589793D);

    private final double angle;

    EffectAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public double calculateAngle(double baseAngle) {
        return baseAngle * this.angle;
    }
}
