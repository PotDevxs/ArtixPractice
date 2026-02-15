package dev.artixdev.practice.examples;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.ScoreboardConfig;
import dev.artixdev.practice.utils.ScoreboardManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Example: Animated Title System Usage
 * Demonstrates how to use the animated title system for scoreboards
 */
public class AnimatedTitleExample {
    
    private final Main plugin;
    private final ScoreboardManager scoreboardManager;
    private BukkitRunnable animationTask;
    
    public AnimatedTitleExample(Main plugin) {
        this.plugin = plugin;
        this.scoreboardManager = plugin.getScoreboardManager();
    }
    
    /**
     * Example: Basic Animated Title Usage
     */
    public void demonstrateBasicUsage() {
        // Check if animation is enabled
        if (!ScoreboardConfig.isAnimationEnabled()) {
            plugin.getLogger().info("Animated titles are disabled!");
            return;
        }
        
        // Get current animation type
        String animationType = ScoreboardConfig.getAnimationType();
        plugin.getLogger().info("Current animation type: " + animationType);
        
        // Get current animation speed (frame duration in ticks)
        int speed = ScoreboardConfig.getFrameDuration();
        plugin.getLogger().info("Animation speed: " + speed + " ticks");
        
        // Get animated title
        String animatedTitle = ScoreboardConfig.getAnimatedTitle();
        plugin.getLogger().info("Current animated title: " + animatedTitle);
        
        // Reset animation
        ScoreboardConfig.resetAnimation();
        plugin.getLogger().info("Animation reset!");
    }
    
    /**
     * Example: Change Animation Type
     */
    public void changeAnimationType(String newType) {
        // Note: In a real implementation, you'd want to update the config file
        // For this example, we'll just demonstrate the concept
        
        plugin.getLogger().info("Changing animation type to: " + newType);
        
        // Reset animation when changing type
        ScoreboardConfig.resetAnimation();
        
        // Update all player scoreboards
        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.updateScoreboard(player);
        }
    }
    
    /**
     * Example: Start Animation Task
     */
    public void startAnimationTask() {
        if (animationTask != null) {
            animationTask.cancel();
        }
        
        animationTask = new BukkitRunnable() {
            @Override
            public void run() {
                // Update animation frame
                ScoreboardConfig.updateAnimationFrame();
                
                // Update all player scoreboards
                for (Player player : Bukkit.getOnlinePlayers()) {
                    scoreboardManager.updateScoreboard(player);
                }
            }
        };
        
        // Run every tick (20 times per second)
        animationTask.runTaskTimer(plugin, 0L, 1L);
        
        plugin.getLogger().info("Animation task started!");
    }
    
    /**
     * Example: Stop Animation Task
     */
    public void stopAnimationTask() {
        if (animationTask != null) {
            animationTask.cancel();
            animationTask = null;
            plugin.getLogger().info("Animation task stopped!");
        }
    }
    
    /**
     * Example: Test Different Animation Types
     */
    public void testAllAnimationTypes() {
        String[] animationTypes = {
            "RAINBOW", "WAVE", "GLOW", "TYPEWRITER", 
            "BLINK", "FADE", "SLIDE"
        };
        
        plugin.getLogger().info("Testing all animation types...");
        
        for (String type : animationTypes) {
            plugin.getLogger().info("Testing animation type: " + type);
            
            // Reset animation
            ScoreboardConfig.resetAnimation();
            
            // Simulate a few frames
            for (int i = 0; i < 10; i++) {
                ScoreboardConfig.updateAnimationFrame();
                String title = ScoreboardConfig.getAnimatedTitle();
                plugin.getLogger().info("Frame " + i + ": " + title);
            }
            
            plugin.getLogger().info("---");
        }
    }
    
    /**
     * Example: Custom Animation Colors
     */
    public void demonstrateCustomColors() {
        plugin.getLogger().info("Demonstrating custom colors...");
        
        // Show current animation frames (each frame uses different color codes)
        plugin.getLogger().info("Animation frames (with colors): " + ScoreboardConfig.ANIMATION_FRAMES);
    }
    
    /**
     * Example: Animation Performance
     */
    public void demonstratePerformance() {
        plugin.getLogger().info("Testing animation performance...");
        
        long startTime = System.currentTimeMillis();
        
        // Generate 1000 animated titles
        for (int i = 0; i < 1000; i++) {
            ScoreboardConfig.updateAnimationFrame();
            String title = ScoreboardConfig.getAnimatedTitle();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        plugin.getLogger().info("Generated 1000 animated titles in " + duration + "ms");
        plugin.getLogger().info("Average time per title: " + (duration / 1000.0) + "ms");
    }
    
    /**
     * Example: Player-Specific Animation
     */
    public void demonstratePlayerSpecificAnimation(Player player) {
        plugin.getLogger().info("Setting up player-specific animation for " + player.getName());
        
        // Set scoreboard for player
        scoreboardManager.setScoreboard(player, "lobby");
        
        // Update scoreboard every 2 ticks
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }
                
                scoreboardManager.updateScoreboard(player);
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    /**
     * Example: Animation Events
     */
    public void demonstrateAnimationEvents() {
        plugin.getLogger().info("Demonstrating animation events...");
        
        // Start animation task
        startAnimationTask();
        
        // Schedule events
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getLogger().info("5 seconds: Changing to WAVE animation");
            changeAnimationType("WAVE");
        }, 100L); // 5 seconds
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getLogger().info("10 seconds: Changing to TYPEWRITER animation");
            changeAnimationType("TYPEWRITER");
        }, 200L); // 10 seconds
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getLogger().info("15 seconds: Stopping animation");
            stopAnimationTask();
        }, 300L); // 15 seconds
    }
}
