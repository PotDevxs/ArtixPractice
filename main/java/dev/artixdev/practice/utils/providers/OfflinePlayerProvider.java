package dev.artixdev.practice.utils.providers;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

/**
 * Provider for OfflinePlayer command arguments
 * Handles parsing and tab completion for offline players
 */
public class OfflinePlayerProvider extends DrinkProvider<OfflinePlayer> {
    
    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public OfflinePlayer provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        
        if (offlinePlayer == null) {
            throw new CommandExitMessage("A player with that name has never logged on this server!");
        }
        
        return offlinePlayer;
    }

    @Override
    public String argumentDescription() {
        return "offlinePlayer";
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(s -> prefix.length() == 0 || s.startsWith(prefix) || s.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}
