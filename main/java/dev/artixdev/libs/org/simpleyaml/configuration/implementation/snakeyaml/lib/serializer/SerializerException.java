package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.serializer;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;

public class SerializerException extends YAMLException {
   private static final long serialVersionUID = 2632638197498912433L;

   public SerializerException(String message) {
      super(message);
   }
}
