package dev.artixdev.practice.utils.providers;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

/**
 * Provider for UUID command arguments
 * Handles parsing and tab completion for UUIDs and player names
 */
public class UUIDProvider extends DrinkProvider<UUID> {
    
    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public UUID provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
        String source = arg.get();
        
        // Handle "self" keyword for current player
        if (arg.getSender() instanceof Player && source.contains("self")) {
            return ((Player) arg.getSender()).getUniqueId();
        }
        
        UUID uuid = null;
        
        try {
            // Try to parse as UUID directly
            uuid = UUID.fromString(source);
        } catch (Exception e) {
            // Try to get UUID from player name
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(source);
            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUniqueId();
            }
        }
        
        if (uuid == null) {
            throw new CommandExitMessage("Invalid UUID!");
        }
        
        return uuid;
    }

    @Override
    public String argumentDescription() {
        return "<uuid>";
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Bukkit.getOnlinePlayers().stream()
                .map(OfflinePlayer::getName)
                .filter(s -> prefix.length() == 0 || s.contains(prefix) || s.startsWith(prefix))
                .collect(Collectors.toList());
    }
}
