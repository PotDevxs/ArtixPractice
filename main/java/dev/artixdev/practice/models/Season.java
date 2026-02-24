package dev.artixdev.practice.models;

import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Represents a ranked season. At end, ELO can be saved to history and reset.
 */
public class Season {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("startTime")
    private long startTime;

    @SerializedName("endTime")
    private long endTime;

    @SerializedName("number")
    private int number;

    public Season() {
    }

    public Season(String id, String name, long startTime, long endTime, int number) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isActive() {
        long now = System.currentTimeMillis();
        return now >= startTime && now < endTime;
    }
}
