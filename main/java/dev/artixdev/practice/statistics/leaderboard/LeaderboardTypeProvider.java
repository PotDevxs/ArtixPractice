package dev.artixdev.practice.statistics.leaderboard;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;
import dev.artixdev.practice.enums.LeaderboardType;

/**
 * Leaderboard Type Provider
 * Provides LeaderboardType enum values for command arguments
 */
public class LeaderboardTypeProvider extends DrinkProvider<LeaderboardType> {
   
   private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");
   
   @Override
   public boolean doesConsumeArgument() {
      return true;
   }
   
   @Override
   public boolean isAsync() {
      return false;
   }
   
   @Override
   public LeaderboardType provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String name = arg.get();
      String simplified = simplify(name);
      
      for (LeaderboardType type : LeaderboardType.values()) {
         if (simplify(type.name()).equalsIgnoreCase(simplified)) {
            return type;
         }
      }
      
      throw new CommandExitMessage("No matching value found for " + argumentDescription() + 
         ". Available values: " + String.join(" ", getSuggestions("")));
   }
   
   @Override
   public String argumentDescription() {
      return LeaderboardType.class.getSimpleName();
   }
   
   @Override
   public List<String> getSuggestions(String prefix) {
      String simplifiedPrefix = simplify(prefix);
      
      return Arrays.stream(LeaderboardType.values())
         .map(type -> {
            String simplifiedName = simplify(type.name());
            if (simplifiedPrefix.isEmpty() || simplifiedName.startsWith(simplifiedPrefix)) {
               return type.name().toLowerCase();
            }
            return null;
         })
         .filter(s -> s != null)
         .collect(Collectors.toList());
   }
   
   /**
    * Simplify string by removing non-alphanumeric characters and converting to lowercase
    * @param text the text to simplify
    * @return simplified text
    */
   private static String simplify(String text) {
      return NON_ALPHANUMERIC.matcher(text.toLowerCase()).replaceAll("");
   }
}
