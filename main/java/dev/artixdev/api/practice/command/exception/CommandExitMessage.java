package dev.artixdev.api.practice.command.exception;

public class CommandExitMessage extends Exception {
   public CommandExitMessage(String message) {
      super(message);
   }
}
