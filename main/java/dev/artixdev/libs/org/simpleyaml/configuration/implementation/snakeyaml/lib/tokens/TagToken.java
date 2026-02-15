package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.tokens;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class TagToken extends Token {
   private final TagTuple value;

   public TagToken(TagTuple value, Mark startMark, Mark endMark) {
      super(startMark, endMark);
      this.value = value;
   }

   public TagTuple getValue() {
      return this.value;
   }

   public Token.ID getTokenId() {
      return Token.ID.Tag;
   }
}
