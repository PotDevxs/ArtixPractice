package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.scanner;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.tokens.Token;

public interface Scanner {
   boolean checkToken(Token.ID... tokenIds);

   Token peekToken();

   Token getToken();
}
