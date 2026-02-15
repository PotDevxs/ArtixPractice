package dev.artixdev.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

public class VelocityListener implements Listener {

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        // Handle player velocity events
        // This would typically involve custom velocity mechanics for practice matches
        // Such as knockback modification, special movement effects, etc.
    }

    @EventHandler
    public void onPlayerVelocitySecondary(PlayerVelocityEvent event) {
        // Secondary velocity event handler
        // This might be used for different types of velocity modifications
    }
}
