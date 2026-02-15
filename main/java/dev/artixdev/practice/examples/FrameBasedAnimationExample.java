package dev.artixdev.practice.examples;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.ScoreboardConfig;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Example: Frame-Based Animation System
 * Demonstrates how to use the new frame-based animation system
 */
public class FrameBasedAnimationExample {
    
    private final Main plugin;
    
    public FrameBasedAnimationExample(Main plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Example: Basic Frame Animation Usage
     */
    public void demonstrateBasicUsage() {
        plugin.getLogger().info("=== Frame-Based Animation Demo ===");
        
        // Check if animation is enabled
        if (!ScoreboardConfig.isAnimationEnabled()) {
            plugin.getLogger().info("Frame animation is disabled!");
            return;
        }
        
        // Show current settings
        plugin.getLogger().info("Total frames: " + ScoreboardConfig.getTotalFrames());
        plugin.getLogger().info("Frame duration: " + ScoreboardConfig.getFrameDuration() + " ticks");
        plugin.getLogger().info("Loop enabled: " + ScoreboardConfig.isLoopEnabled());
        
        // Show all frames
        plugin.getLogger().info("Available frames:");
        for (int i = 0; i < ScoreboardConfig.getTotalFrames(); i++) {
            String frameText = ScoreboardConfig.getFrameText(i);
            plugin.getLogger().info("Frame " + i + ": " + frameText);
        }
    }
    
    /**
     * Example: Manual Frame Control
     */
    public void demonstrateManualFrameControl() {
        plugin.getLogger().info("=== Manual Frame Control Demo ===");
        
        // Reset to first frame
        ScoreboardConfig.resetAnimation();
        plugin.getLogger().info("Reset to frame: " + ScoreboardConfig.getCurrentFrame());
        
        // Manually set specific frames
        for (int i = 0; i < ScoreboardConfig.getTotalFrames(); i++) {
            ScoreboardConfig.setFrame(i);
            String title = ScoreboardConfig.getAnimatedTitle();
            plugin.getLogger().info("Set frame " + i + ": " + title);
        }
    }
    
    /**
     * Example: Animation Simulation
     */
    public void simulateAnimation() {
        plugin.getLogger().info("=== Animation Simulation ===");
        
        // Reset animation
        ScoreboardConfig.resetAnimation();
        
        // Simulate 20 ticks of animation
        for (int tick = 0; tick < 20; tick++) {
            ScoreboardConfig.updateAnimationFrame();
            String title = ScoreboardConfig.getAnimatedTitle();
            int currentFrame = ScoreboardConfig.getCurrentFrame();
            
            plugin.getLogger().info("Tick " + tick + " - Frame " + currentFrame + ": " + title);
        }
    }
    
    /**
     * Example: Custom Frame Creation
     */
    public void demonstrateCustomFrames() {
        plugin.getLogger().info("=== Custom Frame Creation Demo ===");
        
        // Example of how you could create custom frames programmatically
        String[] customFrames = {
            "&c&lR &6&le &e&lf &a&li &b&ln &9&le",
            "&6&lR &e&le &a&lf &b&li &9&ln &d&le", 
            "&e&lR &a&le &b&lf &9&li &d&ln &c&le",
            "&a&lR &b&le &9&lf &d&li &c&ln &6&le",
            "&b&lR &9&le &d&lf &c&li &6&ln &e&le",
            "&9&lR &d&le &c&lf &6&li &e&ln &a&le",
            "&d&lR &c&le &6&lf &e&li &a&ln &b&le"
        };
        
        plugin.getLogger().info("Custom frame examples:");
        for (int i = 0; i < customFrames.length; i++) {
            plugin.getLogger().info("Custom Frame " + i + ": " + customFrames[i]);
        }
        
        plugin.getLogger().info("To use these, add them to the FRAMES list in scoreboard.yml");
    }
    
    /**
     * Example: Different Animation Speeds
     */
    public void demonstrateAnimationSpeeds() {
        plugin.getLogger().info("=== Animation Speed Demo ===");
        
        // Test different frame durations
        int[] durations = {1, 2, 3, 5, 10, 20};
        
        for (int duration : durations) {
            plugin.getLogger().info("Testing frame duration: " + duration + " ticks");
            
            // Reset animation
            ScoreboardConfig.resetAnimation();
            
            // Simulate a few frame changes
            for (int tick = 0; tick < duration * 3; tick++) {
                ScoreboardConfig.updateAnimationFrame();
                if (tick % duration == 0) {
                    String title = ScoreboardConfig.getAnimatedTitle();
                    plugin.getLogger().info("  Frame change at tick " + tick + ": " + title);
                }
            }
            
            plugin.getLogger().info("---");
        }
    }
    
    /**
     * Example: Loop vs No Loop
     */
    public void demonstrateLooping() {
        plugin.getLogger().info("=== Loop vs No Loop Demo ===");
        
        // Test with loop enabled
        plugin.getLogger().info("With loop enabled:");
        ScoreboardConfig.resetAnimation();
        
        for (int i = 0; i < 10; i++) {
            ScoreboardConfig.updateAnimationFrame();
            int frame = ScoreboardConfig.getCurrentFrame();
            plugin.getLogger().info("  Iteration " + i + " - Frame: " + frame);
        }
        
        plugin.getLogger().info("---");
        
        // Test with loop disabled (simulated)
        plugin.getLogger().info("With loop disabled (simulated):");
        ScoreboardConfig.resetAnimation();
        
        // Manually simulate no loop
        int frame = 0;
        int totalFrames = ScoreboardConfig.getTotalFrames();
        
        for (int i = 0; i < 10; i++) {
            if (frame < totalFrames - 1) {
                frame++;
            }
            plugin.getLogger().info("  Iteration " + i + " - Frame: " + frame);
        }
    }
    
    /**
     * Example: Real-time Animation
     */
    public void startRealTimeAnimation() {
        plugin.getLogger().info("Starting real-time animation...");
        
        // Create a task that updates every tick
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            ScoreboardConfig.updateAnimationFrame();
            
            // Update all player scoreboards
            for (Player player : Bukkit.getOnlinePlayers()) {
                plugin.getScoreboardManager().updateScoreboard(player);
            }
            
            // Log current frame every 20 ticks (1 second)
            if (ScoreboardConfig.getCurrentFrame() == 0) {
                plugin.getLogger().info("Animation looped - Current frame: " + 
                    ScoreboardConfig.getCurrentFrame() + " - Title: " + 
                    ScoreboardConfig.getAnimatedTitle());
            }
            
        }, 0L, 1L); // Run every tick
    }
    
    /**
     * Example: Performance Test
     */
    public void performanceTest() {
        plugin.getLogger().info("=== Performance Test ===");
        
        long startTime = System.currentTimeMillis();
        
        // Test 1000 frame updates
        for (int i = 0; i < 1000; i++) {
            ScoreboardConfig.updateAnimationFrame();
            String title = ScoreboardConfig.getAnimatedTitle();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        plugin.getLogger().info("1000 frame updates completed in " + duration + "ms");
        plugin.getLogger().info("Average time per update: " + (duration / 1000.0) + "ms");
    }
}
