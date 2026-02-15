package dev.artixdev.practice.configs;

import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract Configuration
 * Base class for configuration management
 */
public abstract class AbstractConfig {
   
   private final JavaPlugin plugin;
   public static final String CONFIG_EXTENSION = ".yml";
   private final String fileName;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    * @param fileName the file name
    */
   public AbstractConfig(JavaPlugin plugin, String fileName) {
      this.plugin = plugin;
      this.fileName = fileName;
   }
   
   /**
    * Get configuration value
    * @param path the configuration path
    * @return configuration value
    */
   public abstract Object get(String path);
   
   /**
    * Get integer value
    * @param path the configuration path
    * @return integer value
    */
   public abstract int getInteger(String path);
   
   /**
    * Get string value
    * @param path the configuration path
    * @return string value
    */
   public abstract String getString(String path);
   
   /**
    * Get boolean value
    * @param path the configuration path
    * @return boolean value
    */
   public abstract boolean getBoolean(String path);
   
   /**
    * Get double value
    * @param path the configuration path
    * @return double value
    */
   public abstract double getDouble(String path);
   
   /**
    * Get float value
    * @param path the configuration path
    * @return float value
    */
   public abstract float getFloat(String path);
   
   /**
    * Get long value
    * @param path the configuration path
    * @return long value
    */
   public abstract long getLong(String path);
   
   /**
    * Get string list
    * @param path the configuration path
    * @return string list
    */
   public abstract List<String> getStringList(String path);
   
   /**
    * Get integer list
    * @param path the configuration path
    * @return integer list
    */
   public abstract List<Integer> getIntegerList(String path);
   
   /**
    * Set configuration value
    * @param path the configuration path
    * @param value the value
    */
   public abstract void set(String path, Object value);
   
   /**
    * Check if path exists
    * @param path the configuration path
    * @return true if exists
    */
   public abstract boolean contains(String path);
   
   /**
    * Get default value
    * @param path the configuration path
    * @param defaultValue the default value
    * @return configuration value or default
    */
   public abstract Object getOrDefault(String path, Object defaultValue);
   
   /**
    * Get integer with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return integer value or default
    */
   public abstract int getIntOrDefault(String path, int defaultValue);
   
   /**
    * Get string with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return string value or default
    */
   public abstract String getStringOrDefault(String path, String defaultValue);
   
   /**
    * Get boolean with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return boolean value or default
    */
   public abstract boolean getBooleanOrDefault(String path, boolean defaultValue);
   
   /**
    * Get double with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return double value or default
    */
   public abstract double getDoubleOrDefault(String path, double defaultValue);
   
   /**
    * Get float with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return float value or default
    */
   public abstract float getFloatOrDefault(String path, float defaultValue);
   
   /**
    * Get long with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return long value or default
    */
   public abstract long getLongOrDefault(String path, long defaultValue);
   
   /**
    * Get string list with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return string list or default
    */
   public abstract List<String> getStringListOrDefault(String path, List<String> defaultValue);
   
   /**
    * Get integer list with default
    * @param path the configuration path
    * @param defaultValue the default value
    * @return integer list or default
    */
   public abstract List<Integer> getIntegerListOrDefault(String path, List<Integer> defaultValue);
   
   /**
    * Save configuration
    * @return true if saved successfully
    */
   public abstract boolean save();
   
   /**
    * Reload configuration
    * @return true if reloaded successfully
    */
   public abstract boolean reload();
   
   /**
    * Load configuration
    * @return true if loaded successfully
    */
   public abstract boolean load();
   
   /**
    * Check if configuration is loaded
    * @return true if loaded
    */
   public abstract boolean isLoaded();
   
   /**
    * Get configuration file name
    * @return file name
    */
   public String getFileName() {
      return fileName;
   }
   
   /**
    * Get plugin instance
    * @return plugin instance
    */
   public JavaPlugin getPlugin() {
      return plugin;
   }
   
   /**
    * Get configuration file path
    * @return file path
    */
   public String getFilePath() {
      return plugin.getDataFolder().getAbsolutePath() + "/" + fileName;
   }
   
   /**
    * Check if configuration file exists
    * @return true if exists
    */
   public boolean exists() {
      return plugin.getDataFolder().exists() && 
             new java.io.File(plugin.getDataFolder(), fileName).exists();
   }
   
   /**
    * Create configuration file
    * @return true if created successfully
    */
   public boolean create() {
      try {
         if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
         }
         
         java.io.File configFile = new java.io.File(plugin.getDataFolder(), fileName);
         if (!configFile.exists()) {
            configFile.createNewFile();
         }
         
         return true;
      } catch (Exception e) {
         plugin.getLogger().severe("Failed to create configuration file: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Delete configuration file
    * @return true if deleted successfully
    */
   public boolean delete() {
      try {
         java.io.File configFile = new java.io.File(plugin.getDataFolder(), fileName);
         if (configFile.exists()) {
            return configFile.delete();
         }
         return true;
      } catch (Exception e) {
         plugin.getLogger().severe("Failed to delete configuration file: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Get configuration statistics
    * @return configuration statistics
    */
   public String getStatistics() {
      return String.format("Config: %s, Loaded: %s, Exists: %s, Path: %s", 
         fileName, isLoaded(), exists(), getFilePath());
   }
}
