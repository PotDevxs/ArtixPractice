package dev.artixdev.practice.utils.other;

import dev.artixdev.api.practice.command.CommandHandler;
import dev.artixdev.practice.Main;

/**
 * CommandRegistrationUtils
 * 
 * Utility class for registering commands in the plugin.
 * This class provides a simple interface to register commands from a specific package.
 * 
 * Commands should be annotated with @Register annotation to be automatically discovered.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class CommandRegistrationUtils {
    
    /**
     * Register commands from a package.
     * 
     * This method will:
     * 1. Get the plugin instance
     * 2. Create a CommandHandler
     * 3. Register all commands from the specified package
     * 4. Register any additional commands
     * 
     * @param packageName the package name containing command classes annotated with @Register
     * @throws IllegalStateException if the plugin instance is not available
     */
    public static void registerCommands(String packageName) {
        Main plugin = Main.getInstance();
        if (plugin == null) {
            throw new IllegalStateException("ArtixPractice instance is not available. Make sure the plugin is initialized.");
        }
        
        CommandHandler commandHandler = new CommandHandler(plugin);
        commandHandler.registerCommands(packageName);
        commandHandler.registerCommands();
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private CommandRegistrationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
