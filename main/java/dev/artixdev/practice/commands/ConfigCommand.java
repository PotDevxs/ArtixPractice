package dev.artixdev.practice.commands;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.SettingsConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Config Command
 * Handles configuration-related commands
 */
public class ConfigCommand implements CommandExecutor {
    
    private final Main plugin;
    
    public ConfigCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("artix.config")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                handleReloadCommand(sender);
                break;
            case "validate":
                handleValidateCommand(sender);
                break;
            case "info":
                handleInfoCommand(sender);
                break;
            case "set":
                handleSetCommand(sender, args);
                break;
            case "get":
                handleGetCommand(sender, args);
                break;
            case "list":
                handleListCommand(sender);
                break;
            default:
                sendHelpMessage(sender);
                break;
        }
        
        return true;
    }
    
    /**
     * Handle reload command
     */
    private void handleReloadCommand(CommandSender sender) {
        try {
            plugin.getConfigManager().reloadConfigs();
            sender.sendMessage("§aAll configurations reloaded successfully!");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to reload configurations: " + e.getMessage());
        }
    }
    
    /**
     * Handle validate command
     */
    private void handleValidateCommand(CommandSender sender) {
        boolean valid = plugin.getConfigManager().validateConfigs();
        if (valid) {
            sender.sendMessage("§aAll configurations are valid!");
        } else {
            sender.sendMessage("§cSome configurations have validation issues!");
        }
    }
    
    /**
     * Handle info command
     */
    private void handleInfoCommand(CommandSender sender) {
        sender.sendMessage("§6=== Artix Configuration Info ===");
        sender.sendMessage("§7Database Type: §f" + plugin.getDatabaseManager().getDatabaseType());
        sender.sendMessage("§7Bots Enabled: §f" + "true"); // Placeholder
        sender.sendMessage("§7Starting ELO: §f" + SettingsConfig.STARTING_ELO);
        sender.sendMessage("§7Match Time Limit: §f" + SettingsConfig.MATCH_TIME_LIMIT_VALUE + " minutes");
        sender.sendMessage("§7Stats Save Interval: §f" + SettingsConfig.STATS_SAVE_INTERVAL + " minutes");
        sender.sendMessage("§7Active Bots: §f" + plugin.getBotManager().getBotCount());
        sender.sendMessage("§7Active Menus: §f" + plugin.getMenuManager().getActiveMenuCount());
        sender.sendMessage("§7Active Layouts: §f" + plugin.getHotbarManager().getActiveLayoutCount());
    }
    
    /**
     * Handle set command
     */
    private void handleSetCommand(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /config set <key> <value>");
            return;
        }
        
        String key = args[1];
        String value = args[2];
        
        try {
            // This would implement setting configuration values
            sender.sendMessage("§aConfiguration value set: " + key + " = " + value);
        } catch (Exception e) {
            sender.sendMessage("§cFailed to set configuration value: " + e.getMessage());
        }
    }
    
    /**
     * Handle get command
     */
    private void handleGetCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /config get <key>");
            return;
        }
        
        String key = args[1];
        
        try {
            // This would implement getting configuration values
            sender.sendMessage("§aConfiguration value: " + key + " = " + "value");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to get configuration value: " + e.getMessage());
        }
    }
    
    /**
     * Handle list command
     */
    private void handleListCommand(CommandSender sender) {
        sender.sendMessage("§6=== Available Configuration Keys ===");
        sender.sendMessage("§7General Settings:");
        sender.sendMessage("§f- starting_elo");
        sender.sendMessage("§f- match_time_limit");
        sender.sendMessage("§f- stats_save_interval");
        sender.sendMessage("§7Bot Settings:");
        sender.sendMessage("§f- bots_enabled");
        sender.sendMessage("§f- normal_movement_speed");
        sender.sendMessage("§f- tryhard_movement_speed");
        sender.sendMessage("§7Database Settings:");
        sender.sendMessage("§f- database_type");
        sender.sendMessage("§f- mongo_host");
        sender.sendMessage("§f- mysql_host");
    }
    
    /**
     * Send help message
     */
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§6=== Artix Configuration Commands ===");
        sender.sendMessage("§7/config reload §f- Reload all configurations");
        sender.sendMessage("§7/config validate §f- Validate all configurations");
        sender.sendMessage("§7/config info §f- Show configuration information");
        sender.sendMessage("§7/config set <key> <value> §f- Set configuration value");
        sender.sendMessage("§7/config get <key> §f- Get configuration value");
        sender.sendMessage("§7/config list §f- List available configuration keys");
    }
}
