package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.tokens;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class AliasToken extends Token {
   private final String value;

   public AliasToken(String value, Mark startMark, Mark endMark) {
      super(startMark, endMark);
      if (value == null) {
         throw new NullPointerException("alias is expected");
      } else {
         this.value = value;
      }
   }

   public String getValue() {
      return this.value;
   }

   public Token.ID getTokenId() {
      return Token.ID.Alias;
   }
}
