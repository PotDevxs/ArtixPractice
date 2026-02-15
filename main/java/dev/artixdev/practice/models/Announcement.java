package dev.artixdev.practice.models;

import java.util.List;

/**
 * Announcement model
 * Represents an announcement with messages
 */
public class Announcement {
    
    private final List<String> messages;
    private final String id;
    
    public Announcement(String id, List<String> messages) {
        this.id = id;
        this.messages = messages;
    }
    
    /**
     * Get announcement messages
     * @return list of messages
     */
    public List<String> getMessages() {
        return messages;
    }
    
    /**
     * Get announcement ID
     * @return announcement ID
     */
    public String getId() {
        return id;
    }
}
