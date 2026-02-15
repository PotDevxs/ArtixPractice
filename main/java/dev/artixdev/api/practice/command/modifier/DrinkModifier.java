package dev.artixdev.api.practice.command.modifier;

import java.util.Optional;
import dev.artixdev.api.practice.command.command.CommandExecution;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.CommandParameter;

public interface DrinkModifier<T> {
   Optional<T> modify(CommandExecution var1, CommandParameter var2, T var3) throws CommandExitMessage;
}
