package dev.artixdev.practice.constants;

public class EventConstants {
    public static final int DEFAULT_PRIORITY = 0;
    public static final boolean ENABLED = true;
    
    // Event priorities
    public static final int LOW_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int HIGH_PRIORITY = 3;
    public static final int MONITOR_PRIORITY = 4;
    
    // Event types
    public static final int PLAYER_JOIN_EVENT = 1;
    public static final int PLAYER_LEAVE_EVENT = 2;
    public static final int MATCH_START_EVENT = 3;
    public static final int MATCH_END_EVENT = 4;
}
