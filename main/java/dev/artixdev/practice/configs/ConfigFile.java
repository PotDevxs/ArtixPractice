package dev.artixdev.practice.configs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigFile extends AbstractConfig {
    private static final String[] CONFIG_KEYS = {"L+_"};
    private final YamlConfiguration configuration;
    private final File file;

    public ConfigFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, false);
    }

    public ConfigFile(JavaPlugin plugin, String fileName, boolean createIfNotExists) {
        super(plugin, fileName);
        
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        this.file = configFile;
        
        if (createIfNotExists && !this.file.exists()) {
            StringBuilder resourcePath = new StringBuilder();
            resourcePath.append(fileName);
            resourcePath.append(".yml");
            
            String resourceName = resourcePath.toString();
            plugin.saveResource(resourceName, false);
        }
        
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public int getInteger(String path) {
        if (!this.configuration.contains(path)) {
            return 0;
        } else {
            return this.configuration.getInt(path);
        }
    }

    public int getInteger(String path, int defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            return this.configuration.getInt(path, defaultValue);
        }
    }

    public List<String> getStringListOrDefault(String path, List<String> defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            try {
                return this.configuration.getStringList(path);
            } catch (IllegalAccessError e) {
                return defaultValue;
            }
        }
    }

    public boolean contains(String path) {
        return this.configuration.contains(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.getConfiguration().getConfigurationSection(path);
    }

    public Object get(String path) {
        return this.configuration.get(path);
    }

    public double getDouble(String path) {
        if (!this.configuration.contains(path)) {
            return 0.0D;
        } else {
            return this.configuration.getDouble(path);
        }
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public boolean reload() {
        try {
            this.getConfiguration().load(this.file);
            this.getConfiguration().save(this.file);
            return true;
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getStringList(String path) {
        return this.configuration.getStringList(path);
    }

    public String getStringOrDefault(String path, String defaultValue) {
        String value = this.getString(path);
        
        if (value == null) {
            this.set(path, defaultValue);
            this.save();
            return defaultValue;
        } else {
            return value;
        }
    }

    public boolean getBoolean(String path) {
        if (!this.configuration.contains(path)) {
            return false;
        } else {
            return this.configuration.getBoolean(path);
        }
    }

    public String getString(String path) {
        return this.configuration.getString(path);
    }

    @Override
    public boolean save() {
        try {
            this.configuration.save(this.file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void set(String path, Object value) {
        this.configuration.set(path, value);
    }

    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public float getFloat(String path) {
        if (!this.configuration.contains(path)) {
            return 0.0F;
        } else {
            return (float) this.configuration.getDouble(path);
        }
    }

    @Override
    public long getLong(String path) {
        if (!this.configuration.contains(path)) {
            return 0L;
        } else {
            return this.configuration.getLong(path);
        }
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return this.configuration.getIntegerList(path);
    }

    @Override
    public Object getOrDefault(String path, Object defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            return this.configuration.get(path);
        }
    }

    @Override
    public int getIntOrDefault(String path, int defaultValue) {
        return getInteger(path, defaultValue);
    }

    @Override
    public boolean getBooleanOrDefault(String path, boolean defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            return this.configuration.getBoolean(path, defaultValue);
        }
    }

    @Override
    public double getDoubleOrDefault(String path, double defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            return this.configuration.getDouble(path, defaultValue);
        }
    }

    @Override
    public float getFloatOrDefault(String path, float defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            return (float) this.configuration.getDouble(path, defaultValue);
        }
    }

    @Override
    public long getLongOrDefault(String path, long defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            return this.configuration.getLong(path, defaultValue);
        }
    }

    @Override
    public List<Integer> getIntegerListOrDefault(String path, List<Integer> defaultValue) {
        if (!this.configuration.contains(path)) {
            return defaultValue;
        } else {
            try {
                return this.configuration.getIntegerList(path);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    @Override
    public boolean load() {
        try {
            this.configuration.load(this.file);
            return true;
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isLoaded() {
        return this.configuration != null && this.file.exists();
    }
}
