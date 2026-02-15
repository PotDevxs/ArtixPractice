package dev.artixdev.practice.expansions.hologram;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.HologramInterface;

/**
 * Command argument provider that resolves a hologram by name.
 * Use in commands like: delete &lt;hologram&gt;, teleport &lt;hologram&gt;, etc.
 */
public class HologramProvider extends DrinkProvider<HologramInterface> {

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public HologramInterface provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
        HologramManager manager = Main.getInstance().getHologramManager();
        if (manager == null) {
            throw new CommandExitMessage("Hologram system is not available!");
        }
        String name = arg.get();
        HologramInterface hologram = manager.getHologram(name);
        if (hologram == null) {
            throw new CommandExitMessage("A hologram with that name was not found!");
        }
        return hologram;
    }

    @Override
    public String argumentDescription() {
        return "hologram";
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        HologramManager manager = Main.getInstance().getHologramManager();
        if (manager == null) {
            return Collections.emptyList();
        }
        return manager.getAllHolograms().stream()
            .map(HologramInterface::getName)
            .filter(name -> name != null && (prefix.isEmpty()
                || name.equalsIgnoreCase(prefix)
                || name.toLowerCase().startsWith(prefix.toLowerCase())))
            .collect(Collectors.toList());
    }
}
