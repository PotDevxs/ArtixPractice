package dev.artixdev.practice.utils.providers;

import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

/**
 * Generic provider for Enum command arguments
 * Handles parsing and tab completion for any enum type
 */
public class EnumProvider<T extends Enum<T>> extends DrinkProvider<T> {
    
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");
    private final Class<T> enumClass;
    private final String argumentName;

    public EnumProvider(Class<T> enumClass, String name) {
        this.enumClass = enumClass;
        this.argumentName = name;
    }

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public T provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        String simplified = simplify(name);
        
        for (T entry : enumClass.getEnumConstants()) {
            if (simplify(entry.name()).equalsIgnoreCase(simplified)) {
                return entry;
            }
        }
        
        throw new CommandExitMessage("No matching value found for " + this.argumentDescription() + 
                ". Available values: " + StringUtils.join(this.getSuggestions(""), ' '));
    }

    @Override
    public String argumentDescription() {
        return this.argumentName;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = Lists.newArrayList();
        String test = simplify(prefix);
        
        for (T entry : enumClass.getEnumConstants()) {
            String name = simplify(entry.name());
            if (test.length() == 0 || name.startsWith(test) || name.contains(test)) {
                suggestions.add(entry.name().toLowerCase());
            }
        }
        
        return suggestions;
    }

    /**
     * Simplifies a string by removing non-alphanumeric characters and converting to lowercase
     */
    private static String simplify(String input) {
        return NON_ALPHANUMERIC.matcher(input.toLowerCase()).replaceAll("");
    }
}
