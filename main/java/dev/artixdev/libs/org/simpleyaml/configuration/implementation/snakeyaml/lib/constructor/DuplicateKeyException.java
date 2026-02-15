package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.constructor;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public class DuplicateKeyException extends ConstructorException {
   protected DuplicateKeyException(Mark contextMark, Object key, Mark problemMark) {
      super("while constructing a mapping", contextMark, "found duplicate key " + key, problemMark);
   }
}
