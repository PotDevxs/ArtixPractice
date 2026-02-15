package dev.artixdev.practice.interfaces;

import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Configuration Interface
 * Interface for configuration-related operations
 */
public interface ConfigurationInterface {
   
   /**
    * Load configuration from YAML
    * @param yamlConfiguration the YAML configuration
    * @return list of configuration items
    */
   List<dev.artixdev.practice.models.ConfigurationItem> loadConfiguration(YamlConfiguration yamlConfiguration);
   
   /**
    * Save configuration to YAML
    * @param configurationItems the configuration items
    * @return number of items saved
    */
   int saveConfiguration(List<dev.artixdev.practice.models.ConfigurationItem> configurationItems);
   
   /**
    * Get configuration value
    * @param key the configuration key
    * @return configuration value
    */
   Object getConfigurationValue(String key);
   
   /**
    * Set configuration value
    * @param key the configuration key
    * @param value the configuration value
    */
   void setConfigurationValue(String key, Object value);
   
   /**
    * Check if configuration key exists
    * @param key the configuration key
    * @return true if exists
    */
   boolean hasConfigurationKey(String key);
   
   /**
    * Remove configuration key
    * @param key the configuration key
    * @return true if removed
    */
   boolean removeConfigurationKey(String key);
   
   /**
    * Get all configuration keys
    * @return list of configuration keys
    */
   List<String> getAllConfigurationKeys();
   
   /**
    * Clear all configuration
    */
   void clearConfiguration();
   
   /**
    * Reload configuration
    */
   void reloadConfiguration();
   
   /**
    * Save configuration to file
    * @return true if saved successfully
    */
   boolean saveConfigurationToFile();
   
   /**
    * Load configuration from file
    * @return true if loaded successfully
    */
   boolean loadConfigurationFromFile();
   
   /**
    * Get configuration file path
    * @return configuration file path
    */
   String getConfigurationFilePath();
   
   /**
    * Set configuration file path
    * @param filePath the file path
    */
   void setConfigurationFilePath(String filePath);
   
   /**
    * Check if configuration is valid
    * @return true if valid
    */
   boolean isConfigurationValid();
   
   /**
    * Get configuration errors
    * @return list of configuration errors
    */
   List<String> getConfigurationErrors();
   
   /**
    * Validate configuration
    * @return true if valid
    */
   boolean validateConfiguration();
   
   /**
    * Get configuration statistics
    * @return configuration statistics
    */
   String getConfigurationStatistics();
}
