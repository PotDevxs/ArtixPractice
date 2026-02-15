package dev.artixdev.libs.org.simpleyaml.configuration;

import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class ConfigurationWrapper<T extends Configuration> {
   protected final T configuration;
   protected final String path;
   protected final ConfigurationWrapper<T> parent;

   protected ConfigurationWrapper(T configuration, String path, ConfigurationWrapper<T> parent) {
      Validate.notNull(configuration, "configuration cannot be null!");
      this.configuration = configuration;
      this.path = parent == null ? path : parent.childPath(path);
      this.parent = parent;
   }

   public ConfigurationWrapper(T configuration, String path) {
      this(configuration, path, (ConfigurationWrapper)null);
   }

   public ConfigurationWrapper<T> path(String path) {
      return new ConfigurationWrapper(this.configuration, path, this);
   }

   public ConfigurationWrapper<T> set(Object value) {
      return this.set(this.configuration::set, value);
   }

   public ConfigurationWrapper<T> setChild(String child, Object value) {
      return this.setToChild(this.configuration::set, child, value);
   }

   public ConfigurationWrapper<T> addDefault(Object value) {
      return this.set(this.configuration::addDefault, value);
   }

   public ConfigurationWrapper<T> addDefault(String child, Object value) {
      return this.setToChild(this.configuration::addDefault, child, value);
   }

   public ConfigurationWrapper<T> createSection() {
      Configuration config = this.configuration;
      config.getClass();
      return this.apply(config::createSection);
   }

   public ConfigurationWrapper<T> createSection(String child) {
      return this.applyToChild(this.configuration::createSection, child);
   }

   public String getCurrentPath() {
      return this.path;
   }

   public ConfigurationWrapper<T> parent() {
      if (this.parent == null && this.path != null) {
         int lastSectionIndex = this.path.lastIndexOf(this.configuration.options().pathSeparator());
         if (lastSectionIndex >= 0) {
            return new ConfigurationWrapper(this.configuration, this.path.substring(0, lastSectionIndex));
         }
      }

      return this.parent;
   }

   protected final String childPath(String child) {
      return this.path == null ? child : this.path + this.configuration.options().pathSeparator() + child;
   }

   protected ConfigurationWrapper<T> apply(ConfigurationWrapper.ApplyToPath method) {
      method.apply(this.path);
      return this;
   }

   protected ConfigurationWrapper<T> applyToChild(ConfigurationWrapper.ApplyToPath method, String path) {
      method.apply(this.childPath(path));
      return this;
   }

   protected ConfigurationWrapper<T> set(ConfigurationWrapper.SetToPath method, Object value) {
      method.set(this.path, value);
      return this;
   }

   protected ConfigurationWrapper<T> setToChild(ConfigurationWrapper.SetToPath method, String child, Object value) {
      method.set(this.childPath(child), value);
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ConfigurationWrapper<?> that = (ConfigurationWrapper)o;
         return this.configuration == that.configuration && Objects.equals(this.path, that.path) && Objects.equals(this.parent, that.parent);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.configuration, this.path, this.parent});
   }

   @FunctionalInterface
   protected interface SetToPath {
      <T> void set(String path, T value);
   }

   @FunctionalInterface
   protected interface ApplyToPath {
      void apply(String path);
   }
}
