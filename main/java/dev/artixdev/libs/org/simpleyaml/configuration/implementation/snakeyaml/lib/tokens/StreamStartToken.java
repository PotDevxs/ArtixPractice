package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.tokens;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class StreamStartToken extends Token {
   public StreamStartToken(Mark startMark, Mark endMark) {
      super(startMark, endMark);
   }

   public Token.ID getTokenId() {
      return Token.ID.StreamStart;
   }
}
