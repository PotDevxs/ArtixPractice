package dev.artixdev.api.practice.storage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;
import dev.artixdev.libs.org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfiguration;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlConfigurationOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.file.YamlFile;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.api.QuoteStyle;

public abstract class YamlStorage {
   private static final Logger LOGGER = LogManager.getLogger(YamlStorage.class);
   private final List<Field> fields;
   private final String name;
   private final YamlFile config;

   public YamlStorage(JavaPlugin plugin, String name, boolean saveResource) {
      File file = new File(plugin.getDataFolder(), name + ".yml");
      this.name = name;
      this.config = new YamlFile(file);
      if (!file.exists()) {
         try {
            if (saveResource) {
               plugin.saveResource(name + ".yml", false);
            } else {
               this.config.createNewFile(false);
            }
         } catch (IOException e) {
            LOGGER.error("[Storage] Could not create/save " + name + ".yml!");
            LOGGER.error("[Storage] Error: " + e.getMessage());
         }
      }

      try {
         if (saveResource) {
            this.config.load(file);
         } else {
            this.config.load();
         }
      } catch (IOException e) {
         LOGGER.error("[Storage] Could not load " + name + ".yml, please correct your syntax errors!");
         LOGGER.error("[Storage] Error: " + e.getMessage());
      }

      this.registerChildStorages();
      this.fields = this.getConfigFields();
      this.config.options().header(String.join("\n", this.getHeader()));
      this.readConfig();
   }

   public void readConfig() {
      for (Field field : this.fields) {
         try {
            ConfigValue configValue = (ConfigValue)field.getAnnotation(ConfigValue.class);
            Object value = field.get((Object)null);
            if (this.config.contains(configValue.path())) {
               field.setAccessible(true);
               field.set((Object)null, this.config.get(configValue.path()));
               field.setAccessible(false);
            } else {
               this.config.set(configValue.path(), value);
            }

            if (configValue.comment().length() > 0) {
               this.config.path(configValue.path()).comment(configValue.comment()).blankLine();
            }
         } catch (IllegalAccessException | IllegalArgumentException e) {
            LOGGER.error("[Storage] Error invoking " + field, e);
         }
      }

      this.addSeparateComments();
      this.setupConfigOptions(this.config.options());
      this.saveConfig();
   }

   public void writeConfig() {
      for (Field field : this.fields) {
         ConfigValue configValue = (ConfigValue) field.getAnnotation(ConfigValue.class);

         try {
            Object value = field.get((Object)null);
            this.config.set(configValue.path(), value);
         } catch (IllegalAccessException | IllegalArgumentException e) {
            LOGGER.error("[Storage] Error invoking " + field, e);
         }
      }

      this.saveConfig();
   }

   public void reloadConfig() {
      try {
         this.config.load();
      } catch (IOException e) {
         LOGGER.error("[Storage] Could not load " + this.name + ".yml, please correct your syntax errors!");
         LOGGER.error("[Storage] Error: " + e.getMessage());
      }

      this.readConfig();
   }

   public void saveConfig() {
      try {
         this.config.save();
      } catch (IOException e) {
         LOGGER.error("[Storage] Unable to save " + this.name + ".yml!");
      }

   }

   public void clearConfig() {
      this.config.getKeys(false).forEach((key) -> {
         this.config.set(key, (Object)null);
      });
      this.saveConfig();
   }

   public void setupConfigOptions(YamlConfigurationOptions options) {
      this.config.setCommentFormat(YamlCommentFormat.PRETTY);
      options.charset(StandardCharsets.UTF_8);
      options.useComments(true);
      options.quoteStyleDefaults().setQuoteStyle(String.class, QuoteStyle.DOUBLE);
      options.quoteStyleDefaults().setQuoteStyle(List.class, QuoteStyle.DOUBLE);
      options.header(String.join("\n", this.getHeader()));
   }

   public abstract void addSeparateComments();

   public abstract List<Field> getConfigFields();

   public abstract String[] getHeader();

   public String getString(String path) {
      return this.config.contains(path) ? ChatColor.translateAlternateColorCodes('&', this.config.getString(path)) : null;
   }

   public boolean contains(String path) {
      return this.config.contains(path);
   }

   public String getStringOrDefault(String path, String or) {
      String toReturn = this.getString(path);
      if (toReturn == null) {
         this.config.set(path, or);
         return or;
      } else {
         return toReturn;
      }
   }

   public int getInteger(String path) {
      return this.config.contains(path) ? this.config.getInt(path) : 0;
   }

   public int getInteger(String path, int or) {
      int toReturn = this.getInteger(path);
      return this.config.contains(path) ? or : toReturn;
   }

   public void set(String path, Object value) {
      this.config.set(path, value);
   }

   public boolean getBoolean(String path) {
      return this.config.contains(path) && this.config.getBoolean(path);
   }

   public double getDouble(String path) {
      return this.config.contains(path) ? this.config.getDouble(path) : 0.0D;
   }

   public void addComment(String path, String comment) {
      this.config.setComment(path, comment);
   }

   public void addCommentWithBlankLine(String path, String comment) {
      this.config.path(path).comment(comment).blankLine();
   }

   public Object get(String path) {
      return this.config.contains(path) ? this.config.get(path) : null;
   }

   public List<String> getStringList(String path) {
      return this.config.contains(path) ? this.config.getStringList(path) : null;
   }

   public ConfigurationSection getConfigurationSection(String path) {
      return this.config.getConfigurationSection(path);
   }

   public ConfigurationSection createSection(String path) {
      return this.config.createSection(path);
   }

   public YamlConfiguration getConfiguration() {
      return this.config;
   }

   public void registerChildStorages() {
   }
}
