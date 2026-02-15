package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Announcement;

import java.util.ArrayList;
import java.util.List;

/**
 * Announcement Manager
 * Manages announcements for the server
 */
public class AnnouncementManager {
    
    private final Main plugin;
    private final List<Announcement> announcements;
    private int currentIndex;
    
    public AnnouncementManager(Main plugin) {
        this.plugin = plugin;
        this.announcements = new ArrayList<>();
        this.currentIndex = 0;
    }
    
    /**
     * Initialize announcement manager
     */
    public void initialize() {
        // Load announcements from config or storage
        // For now, create default announcements
        loadDefaultAnnouncements();
    }
    
    /**
     * Load default announcements
     */
    private void loadDefaultAnnouncements() {
        // Add default announcements
        List<String> messages1 = new ArrayList<>();
        messages1.add("Welcome to Practice!");
        messages1.add("Join our Discord server!");
        announcements.add(new Announcement("default1", messages1));
        
        List<String> messages2 = new ArrayList<>();
        messages2.add("Follow us on social media!");
        messages2.add("Check out our website!");
        announcements.add(new Announcement("default2", messages2));
    }
    
    /**
     * Get current announcement
     * @return current announcement or null
     */
    public Announcement getCurrentAnnouncement() {
        if (announcements.isEmpty()) {
            return null;
        }
        
        Announcement announcement = announcements.get(currentIndex);
        
        // Move to next announcement for next time
        currentIndex = (currentIndex + 1) % announcements.size();
        
        return announcement;
    }
    
    /**
     * Add announcement
     * @param announcement the announcement to add
     */
    public void addAnnouncement(Announcement announcement) {
        announcements.add(announcement);
    }
    
    /**
     * Get all announcements
     * @return list of all announcements
     */
    public List<Announcement> getAnnouncements() {
        return new ArrayList<>(announcements);
    }
}
