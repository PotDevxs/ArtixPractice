package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.tokens;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class FlowMappingStartToken extends Token {
   public FlowMappingStartToken(Mark startMark, Mark endMark) {
      super(startMark, endMark);
   }

   public Token.ID getTokenId() {
      return Token.ID.FlowMappingStart;
   }
}
