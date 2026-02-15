package dev.artixdev.libs.org.bson.diagnostics;

class NoOpLogger implements Logger {
   private final String name;

   NoOpLogger(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}
