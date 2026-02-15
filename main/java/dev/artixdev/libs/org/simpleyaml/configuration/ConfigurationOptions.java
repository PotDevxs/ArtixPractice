package dev.artixdev.libs.org.simpleyaml.configuration;

import java.util.Objects;
import dev.artixdev.libs.org.simpleyaml.utils.StringUtils;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class ConfigurationOptions {
   private final Configuration configuration;
   private char pathSeparator = '.';
   private boolean copyDefaults = true;
   private int indent = 2;

   protected ConfigurationOptions(Configuration configuration) {
      this.configuration = configuration;
   }

   public Configuration configuration() {
      return this.configuration;
   }

   public char pathSeparator() {
      return this.pathSeparator;
   }

   public ConfigurationOptions pathSeparator(char value) {
      Validate.isTrue(value != '\\', value + " is used for escaping and cannot be a path separator");
      Validate.isTrue(value != '[' && value != ']', value + " is used for indexing and cannot be a path separator");
      this.pathSeparator = value;
      StringUtils.setSeparator(value);
      return this;
   }

   public boolean copyDefaults() {
      return this.copyDefaults;
   }

   public ConfigurationOptions copyDefaults(boolean value) {
      this.copyDefaults = value;
      return this;
   }

   public int indent() {
      return this.indent;
   }

   public ConfigurationOptions indent(int value) {
      this.indent = value;
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ConfigurationOptions)) {
         return false;
      } else {
         ConfigurationOptions that = (ConfigurationOptions)o;
         return this.indent == that.indent && this.pathSeparator == that.pathSeparator && this.copyDefaults == that.copyDefaults && Objects.equals(this.configuration, that.configuration);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.indent, this.pathSeparator, this.copyDefaults, this.configuration});
   }
}
