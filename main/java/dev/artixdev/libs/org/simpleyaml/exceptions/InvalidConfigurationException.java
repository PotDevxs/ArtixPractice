package dev.artixdev.libs.org.simpleyaml.exceptions;

import java.io.IOException;

public class InvalidConfigurationException extends IOException {
   public InvalidConfigurationException() {
   }

   public InvalidConfigurationException(String msg) {
      super(msg);
   }

   public InvalidConfigurationException(Throwable cause) {
      super(cause);
   }

   public InvalidConfigurationException(String msg, Throwable cause) {
      super(msg, cause);
   }
}
