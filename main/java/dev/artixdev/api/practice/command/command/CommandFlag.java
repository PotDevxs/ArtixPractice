package dev.artixdev.api.practice.command.command;

public class CommandFlag {
   public static final char FLAG_PREFIX = '-';
   private final char character;
   private final String value;

   public CommandFlag(char character) {
      this(character, (String)null);
   }

   public CommandFlag(char character, String value) {
      this.character = character;
      this.value = value;
   }

   public char getCharacter() {
      return this.character;
   }

   public String getValue() {
      return this.value;
   }

   public String flagPrefixToString() {
      return String.valueOf(new char[]{'-', this.character});
   }
}
