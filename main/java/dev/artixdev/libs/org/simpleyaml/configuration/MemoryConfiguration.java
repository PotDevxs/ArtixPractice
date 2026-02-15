package dev.artixdev.libs.org.simpleyaml.configuration;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class MemoryConfiguration extends MemorySection implements Configuration {
   protected Configuration defaults;
   protected MemoryConfigurationOptions options;

   public MemoryConfiguration() {
   }

   public MemoryConfiguration(Configuration defaults) {
      this.defaults = defaults;
   }

   public void addDefaults(Map<String, Object> defaults) {
      Validate.notNull(defaults, "Defaults may not be null");
      Iterator<Entry<String, Object>> entryIterator = defaults.entrySet().iterator();

      while (entryIterator.hasNext()) {
         Entry<String, Object> entry = entryIterator.next();
         this.addDefault(entry.getKey(), entry.getValue());
      }

   }

   public void addDefaults(Configuration defaults) {
      Validate.notNull(defaults, "Defaults may not be null");
      Iterator<String> keyIterator = defaults.getKeys(true).iterator();

      while (keyIterator.hasNext()) {
         String key = keyIterator.next();
         if (!defaults.isConfigurationSection(key)) {
            this.addDefault(key, defaults.get(key));
         }
      }

   }

   public Configuration getDefaults() {
      return this.defaults;
   }

   public void setDefaults(Configuration defaults) {
      Validate.notNull(defaults, "Defaults may not be null");
      this.defaults = defaults;
   }

   public MemoryConfigurationOptions options() {
      if (this.options == null) {
         this.options = new MemoryConfigurationOptions(this);
      }

      return this.options;
   }

   public ConfigurationSection getParent() {
      return null;
   }

   public void addDefault(String path, Object value) {
      Validate.notNull(path, "Path may not be null");
      if (this.defaults == null) {
         this.defaults = new MemoryConfiguration();
      }

      this.defaults.set(path, value);
   }
}
