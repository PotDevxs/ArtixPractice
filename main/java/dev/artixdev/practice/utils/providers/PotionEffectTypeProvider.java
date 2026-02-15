package dev.artixdev.practice.utils.providers;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

/**
 * Provider for PotionEffectType command arguments
 * Handles parsing and tab completion for potion effect types
 */
public class PotionEffectTypeProvider extends DrinkProvider<PotionEffectType> {
    
    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public PotionEffectType provide(CommandArg commandArg, List<? extends Annotation> list) throws CommandExitMessage {
        String name = commandArg.get();
        PotionEffectType potionEffectType = PotionEffectType.getByName(name);
        
        if (potionEffectType == null) {
            throw new CommandExitMessage("Invalid potion effect type!");
        }
        
        return potionEffectType;
    }

    @Override
    public String argumentDescription() {
        return "potionEffectType";
    }

    @Override
    public List<String> getSuggestions(String s) {
        return Arrays.stream(PotionEffectType.values())
                .map(PotionEffectType::getName)
                .filter(Objects::nonNull)
                .filter(string -> s.length() == 0 || string.equals(s) || string.toLowerCase().startsWith(s.toLowerCase()))
                .collect(Collectors.toList());
    }
}
