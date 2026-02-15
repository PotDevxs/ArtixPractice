package dev.artixdev.practice.configs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private final Map<String, ConfigSection> configSections = new ConcurrentHashMap<>();
    private static String currentConfig;

    public void load() {
        // Load configuration from files
    }

    public ConfigSection getConfigSection(String name) {
        return configSections.getOrDefault(name.toLowerCase(), createDefaultSection());
    }

    private ConfigSection createDefaultSection() {
        return new ConfigSection();
    }

    public void addConfigSection(String name, ConfigSection section) {
        configSections.put(name.toLowerCase(), section);
    }

    public void removeConfigSection(String name) {
        configSections.remove(name.toLowerCase());
    }

    public List<ConfigSection> getAllConfigSections() {
        return new ArrayList<>(configSections.values());
    }

    public void reload() {
        configSections.clear();
        load();
    }
}
