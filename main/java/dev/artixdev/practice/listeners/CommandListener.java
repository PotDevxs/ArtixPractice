package dev.artixdev.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import dev.artixdev.practice.Main;

public class CommandListener implements Listener {
    
    private final Main plugin;

    public CommandListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        // Handle command preprocessing
        // This could include blocking certain commands in matches, etc.
        
        String command = event.getMessage().toLowerCase();
        
        // Block certain commands during matches
        if (command.startsWith("/spawn") || command.startsWith("/hub")) {
            // Check if player is in a match
            // If so, cancel the command
        }
    }

    // PlayerDisguiseEvent was provided by an external dependency (dev.artixdev.phoenix)
    // which is not present in this project. If you re-add that dependency later,
    // you can re-introduce the listener method for it here.
}
