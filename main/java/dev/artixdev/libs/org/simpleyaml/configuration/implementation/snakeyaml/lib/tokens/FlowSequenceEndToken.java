package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.tokens;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class FlowSequenceEndToken extends Token {
   public FlowSequenceEndToken(Mark startMark, Mark endMark) {
      super(startMark, endMark);
   }

   public Token.ID getTokenId() {
      return Token.ID.FlowSequenceEnd;
   }
}
