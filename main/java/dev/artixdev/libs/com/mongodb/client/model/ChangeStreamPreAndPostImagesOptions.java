package dev.artixdev.libs.com.mongodb.client.model;

public class ChangeStreamPreAndPostImagesOptions {
   private final boolean enabled;

   public ChangeStreamPreAndPostImagesOptions(boolean enabled) {
      this.enabled = enabled;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public String toString() {
      return "ChangeStreamPreAndPostImagesOptions{enabled=" + this.enabled + '}';
   }
}
