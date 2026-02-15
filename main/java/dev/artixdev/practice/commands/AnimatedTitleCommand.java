package dev.artixdev.practice.commands;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.ScoreboardConfig;
import dev.artixdev.practice.examples.AnimatedTitleExample;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command: /animatedtitle
 * Manages animated title system for scoreboards
 */
public class AnimatedTitleCommand implements CommandExecutor, TabCompleter {
    
    private final Main plugin;
    private final AnimatedTitleExample example;
    
    public AnimatedTitleCommand(Main plugin) {
        this.plugin = plugin;
        this.example = new AnimatedTitleExample(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bolt.animatedtitle")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "info":
                handleInfo(sender);
                break;
                
            case "type":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /animatedtitle type <RAINBOW|WAVE|GLOW|TYPEWRITER|BLINK|FADE|SLIDE>");
                    return true;
                }
                handleType(sender, args[1]);
                break;
                
            case "speed":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /animatedtitle speed <1-10>");
                    return true;
                }
                handleSpeed(sender, args[1]);
                break;
                
            case "enable":
                handleEnable(sender);
                break;
                
            case "disable":
                handleDisable(sender);
                break;
                
            case "reset":
                handleReset(sender);
                break;
                
            case "test":
                handleTest(sender);
                break;
                
            case "start":
                handleStart(sender);
                break;
                
            case "stop":
                handleStop(sender);
                break;
                
            case "demo":
                handleDemo(sender);
                break;
                
            case "performance":
                handlePerformance(sender);
                break;
                
            case "frames":
                handleFrames(sender);
                break;
                
            case "loop":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /animatedtitle loop <true|false>");
                    return true;
                }
                handleLoop(sender, args[1]);
                break;
                
            case "next":
                handleNextFrame(sender);
                break;
                
            case "prev":
                handlePrevFrame(sender);
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== Animated Title Commands ===");
        sender.sendMessage("§e/animatedtitle info §7- Show current settings");
        sender.sendMessage("§e/animatedtitle type §7- Show current animation frames");
        sender.sendMessage("§e/animatedtitle frames §7- List all frames with current indicator");
        sender.sendMessage("§e/animatedtitle speed <1-20> §7- Change frame duration (ticks)");
        sender.sendMessage("§e/animatedtitle loop <true|false> §7- Enable/disable looping");
        sender.sendMessage("§e/animatedtitle next §7- Advance to next frame");
        sender.sendMessage("§e/animatedtitle prev §7- Go to previous frame");
        sender.sendMessage("§e/animatedtitle enable §7- Enable animations");
        sender.sendMessage("§e/animatedtitle disable §7- Disable animations");
        sender.sendMessage("§e/animatedtitle reset §7- Reset animation to frame 0");
        sender.sendMessage("§e/animatedtitle test §7- Test animation system");
        sender.sendMessage("§e/animatedtitle start §7- Start animation task");
        sender.sendMessage("§e/animatedtitle stop §7- Stop animation task");
        sender.sendMessage("§e/animatedtitle demo §7- Run demo");
        sender.sendMessage("§e/animatedtitle performance §7- Test performance");
    }
    
    private void handleInfo(CommandSender sender) {
        sender.sendMessage("§6=== Animated Title Info ===");
        sender.sendMessage("§eEnabled: §f" + ScoreboardConfig.ANIMATED_TITLE_ENABLED);
        sender.sendMessage("§eFrames: §f" + ScoreboardConfig.ANIMATION_FRAMES.size());
        sender.sendMessage("§eFrame Duration: §f" + ScoreboardConfig.FRAME_DURATION + " ticks");
        sender.sendMessage("§eCurrent Frame: §f" + ScoreboardConfig.getCurrentFrame());
        sender.sendMessage("§eLoop Enabled: §f" + ScoreboardConfig.LOOP_ENABLED);
    }
    
    private void handleType(CommandSender sender, String type) {
        // Note: Frame-based animation doesn't use types, but we can show current frames
        sender.sendMessage("§6=== Current Animation Frames ===");
        for (int i = 0; i < ScoreboardConfig.ANIMATION_FRAMES.size(); i++) {
            sender.sendMessage("§eFrame " + (i + 1) + ": §f" + ScoreboardConfig.ANIMATION_FRAMES.get(i));
        }
        sender.sendMessage("§7Total frames: §f" + ScoreboardConfig.ANIMATION_FRAMES.size());
    }
    
    private void handleSpeed(CommandSender sender, String speedStr) {
        try {
            int speed = Integer.parseInt(speedStr);
            if (speed < 1 || speed > 20) {
                sender.sendMessage("§cFrame duration must be between 1 and 20 ticks!");
                return;
            }
            
            // Update frame duration
            ScoreboardConfig.FRAME_DURATION = speed;
            sender.sendMessage("§aFrame duration changed to: §f" + speed + " ticks");
            sender.sendMessage("§7Note: This updates the current session. Restart server to persist changes.");
            
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid duration! Must be a number between 1 and 20.");
        }
    }
    
    private void handleEnable(CommandSender sender) {
        ScoreboardConfig.ANIMATED_TITLE_ENABLED = true;
        sender.sendMessage("§aAnimated titles enabled!");
        sender.sendMessage("§7Note: This updates the current session. Restart server to persist changes.");
    }
    
    private void handleDisable(CommandSender sender) {
        ScoreboardConfig.ANIMATED_TITLE_ENABLED = false;
        sender.sendMessage("§cAnimated titles disabled!");
        sender.sendMessage("§7Note: This updates the current session. Restart server to persist changes.");
    }
    
    private void handleReset(CommandSender sender) {
        ScoreboardConfig.resetAnimation();
        sender.sendMessage("§aAnimation reset!");
        sender.sendMessage("§7Current frame: §f" + ScoreboardConfig.getCurrentFrame());
        
        // Update all player scoreboards
        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getScoreboardManager().updateScoreboard(player);
        }
    }
    
    private void handleTest(CommandSender sender) {
        sender.sendMessage("§aTesting all animation types...");
        example.testAllAnimationTypes();
    }
    
    private void handleStart(CommandSender sender) {
        example.startAnimationTask();
        sender.sendMessage("§aAnimation task started!");
    }
    
    private void handleStop(CommandSender sender) {
        example.stopAnimationTask();
        sender.sendMessage("§cAnimation task stopped!");
    }
    
    private void handleDemo(CommandSender sender) {
        sender.sendMessage("§aRunning animated title demo...");
        example.demonstrateAnimationEvents();
    }
    
    private void handlePerformance(CommandSender sender) {
        sender.sendMessage("§aTesting animation performance...");
        example.demonstratePerformance();
    }
    
    private void handleFrames(CommandSender sender) {
        sender.sendMessage("§6=== Animation Frames ===");
        for (int i = 0; i < ScoreboardConfig.ANIMATION_FRAMES.size(); i++) {
            String frame = ScoreboardConfig.ANIMATION_FRAMES.get(i);
            String current = (i == ScoreboardConfig.getCurrentFrame()) ? " §a[CURRENT]" : "";
            sender.sendMessage("§e" + (i + 1) + ". §f" + frame + current);
        }
        sender.sendMessage("§7Total: §f" + ScoreboardConfig.ANIMATION_FRAMES.size() + " frames");
    }
    
    private void handleLoop(CommandSender sender, String loopStr) {
        boolean loop = Boolean.parseBoolean(loopStr);
        ScoreboardConfig.LOOP_ENABLED = loop;
        sender.sendMessage("§aLoop " + (loop ? "enabled" : "disabled") + "!");
        sender.sendMessage("§7Note: This updates the current session. Restart server to persist changes.");
    }
    
    private void handleNextFrame(CommandSender sender) {
        ScoreboardConfig.updateAnimationFrame();
        sender.sendMessage("§aAdvanced to next frame: §f" + ScoreboardConfig.getCurrentFrame());
    }
    
    private void handlePrevFrame(CommandSender sender) {
        ScoreboardConfig.setFrame(ScoreboardConfig.getCurrentFrame() - 1);
        sender.sendMessage("§aMoved to previous frame: §f" + ScoreboardConfig.getCurrentFrame());
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList(
                "info", "type", "frames", "speed", "loop", "next", "prev",
                "enable", "disable", "reset", "test", "start", "stop", "demo", "performance"
            );
            
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "type":
                    // Frame-based animation doesn't use types
                    break;
                    
                case "loop":
                    List<String> loopOptions = Arrays.asList("true", "false");
                    for (String option : loopOptions) {
                        if (option.startsWith(args[1].toLowerCase())) {
                            completions.add(option);
                        }
                    }
                    break;
                    
                case "speed":
                    List<String> speeds = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", 
                                                      "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
                    
                    for (String speed : speeds) {
                        if (speed.startsWith(args[1])) {
                            completions.add(speed);
                        }
                    }
                    break;
            }
        }
        
        return completions;
    }
}
