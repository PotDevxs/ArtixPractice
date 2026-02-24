package dev.artixdev.practice.models;

import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Knockback profile for bots (or players in bot duels).
 * Values can be applied to NMS/knockback handlers.
 */
public class KnockbackProfile {

    @SerializedName("name")
    private String name;

    @SerializedName("horizontal")
    private double horizontal = 0.4;

    @SerializedName("vertical")
    private double vertical = 0.4;

    @SerializedName("verticalLimit")
    private double verticalLimit = 0.4;

    @SerializedName("extraHorizontal")
    private double extraHorizontal = 0.0;

    @SerializedName("extraVertical")
    private double extraVertical = 0.0;

    @SerializedName("friction")
    private double friction = 2.0;

    @SerializedName("delay")
    private int delay = 0;

    public KnockbackProfile() {
        this.name = "default";
    }

    public KnockbackProfile(String name) {
        this.name = name == null || name.isEmpty() ? "default" : name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(double horizontal) {
        this.horizontal = horizontal;
    }

    public double getVertical() {
        return vertical;
    }

    public void setVertical(double vertical) {
        this.vertical = vertical;
    }

    public double getVerticalLimit() {
        return verticalLimit;
    }

    public void setVerticalLimit(double verticalLimit) {
        this.verticalLimit = verticalLimit;
    }

    public double getExtraHorizontal() {
        return extraHorizontal;
    }

    public void setExtraHorizontal(double extraHorizontal) {
        this.extraHorizontal = extraHorizontal;
    }

    public double getExtraVertical() {
        return extraVertical;
    }

    public void setExtraVertical(double extraVertical) {
        this.extraVertical = extraVertical;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public KnockbackProfile copy() {
        KnockbackProfile copy = new KnockbackProfile(this.name + "_copy");
        copy.horizontal = this.horizontal;
        copy.vertical = this.vertical;
        copy.verticalLimit = this.verticalLimit;
        copy.extraHorizontal = this.extraHorizontal;
        copy.extraVertical = this.extraVertical;
        copy.friction = this.friction;
        copy.delay = this.delay;
        return copy;
    }
}
