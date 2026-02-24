package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.KnockbackProfile;
import dev.artixdev.practice.models.BotProfile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Loads/saves knockback profiles to YAML. Applies profile to BotProfile by name.
 */
public class KnockbackProfileManager {

    private final Main plugin;
    private final Map<String, KnockbackProfile> profiles = new HashMap<>();
    private final File file;

    public KnockbackProfileManager(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "knockback_profiles.yml");
        load();
    }

    public void load() {
        profiles.clear();
        if (!file.exists()) {
            createDefault();
            return;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String name : config.getKeys(false)) {
            if (!config.isConfigurationSection(name)) continue;
            KnockbackProfile p = new KnockbackProfile(name);
            p.setHorizontal(config.getDouble(name + ".horizontal", 0.4));
            p.setVertical(config.getDouble(name + ".vertical", 0.4));
            p.setVerticalLimit(config.getDouble(name + ".verticalLimit", 0.4));
            p.setExtraHorizontal(config.getDouble(name + ".extraHorizontal", 0));
            p.setExtraVertical(config.getDouble(name + ".extraVertical", 0));
            p.setFriction(config.getDouble(name + ".friction", 2.0));
            p.setDelay(config.getInt(name + ".delay", 0));
            profiles.put(name.toLowerCase(), p);
        }
    }

    private void createDefault() {
        KnockbackProfile def = new KnockbackProfile("default");
        profiles.put("default", def);
        save();
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        for (KnockbackProfile p : profiles.values()) {
            String path = p.getName();
            config.set(path + ".horizontal", p.getHorizontal());
            config.set(path + ".vertical", p.getVertical());
            config.set(path + ".verticalLimit", p.getVerticalLimit());
            config.set(path + ".extraHorizontal", p.getExtraHorizontal());
            config.set(path + ".extraVertical", p.getExtraVertical());
            config.set(path + ".friction", p.getFriction());
            config.set(path + ".delay", p.getDelay());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save knockback_profiles.yml: " + e.getMessage());
        }
    }

    public KnockbackProfile getProfile(String name) {
        if (name == null || name.isEmpty()) return profiles.get("default");
        return profiles.get(name.toLowerCase());
    }

    public Map<String, KnockbackProfile> getProfiles() {
        return new HashMap<>(profiles);
    }

    public List<String> getProfileNames() {
        return new ArrayList<>(profiles.keySet());
    }

    public boolean createProfile(String name) {
        if (name == null || name.isEmpty()) return false;
        if (profiles.containsKey(name.toLowerCase())) return false;
        profiles.put(name.toLowerCase(), new KnockbackProfile(name));
        save();
        return true;
    }

    public boolean deleteProfile(String name) {
        if (name == null || "default".equalsIgnoreCase(name)) return false;
        if (profiles.remove(name.toLowerCase()) == null) return false;
        save();
        return true;
    }

    public void setProperty(String profileName, String property, String value) {
        KnockbackProfile p = getProfile(profileName);
        if (p == null) return;
        try {
            switch (property.toLowerCase()) {
                case "horizontal": p.setHorizontal(Double.parseDouble(value)); break;
                case "vertical": p.setVertical(Double.parseDouble(value)); break;
                case "verticallimit": p.setVerticalLimit(Double.parseDouble(value)); break;
                case "extrahorizontal": p.setExtraHorizontal(Double.parseDouble(value)); break;
                case "extravertical": p.setExtraVertical(Double.parseDouble(value)); break;
                case "friction": p.setFriction(Double.parseDouble(value)); break;
                case "delay": p.setDelay(Integer.parseInt(value)); break;
                default: return;
            }
            save();
        } catch (NumberFormatException ignored) { }
    }

    public String getProperty(String profileName, String property) {
        KnockbackProfile p = getProfile(profileName);
        if (p == null) return null;
        switch (property.toLowerCase()) {
            case "horizontal": return String.valueOf(p.getHorizontal());
            case "vertical": return String.valueOf(p.getVertical());
            case "verticallimit": return String.valueOf(p.getVerticalLimit());
            case "extrahorizontal": return String.valueOf(p.getExtraHorizontal());
            case "extravertical": return String.valueOf(p.getExtraVertical());
            case "friction": return String.valueOf(p.getFriction());
            case "delay": return String.valueOf(p.getDelay());
            default: return null;
        }
    }

    public boolean applyToBot(BotProfile bot, String profileName) {
        if (bot == null) return false;
        if (profileName != null && getProfile(profileName) != null) {
            bot.setKnockbackProfileName(profileName);
            return true;
        }
        bot.setKnockbackProfileName(null);
        return true;
    }

    public boolean copyProfile(String sourceName, String destName) {
        KnockbackProfile src = getProfile(sourceName);
        if (src == null || destName == null || destName.isEmpty()) return false;
        if (profiles.containsKey(destName.toLowerCase())) return false;
        KnockbackProfile copy = src.copy();
        copy.setName(destName);
        profiles.put(destName.toLowerCase(), copy);
        save();
        return true;
    }

    public boolean renameProfile(String oldName, String newName) {
        KnockbackProfile p = profiles.remove(oldName != null ? oldName.toLowerCase() : null);
        if (p == null || newName == null || newName.isEmpty()) return false;
        if ("default".equalsIgnoreCase(oldName)) return false;
        p.setName(newName);
        profiles.put(newName.toLowerCase(), p);
        save();
        return true;
    }

    public void resetProfile(String name) {
        KnockbackProfile p = getProfile(name);
        if (p == null) return;
        p.setHorizontal(0.4);
        p.setVertical(0.4);
        p.setVerticalLimit(0.4);
        p.setExtraHorizontal(0);
        p.setExtraVertical(0);
        p.setFriction(2.0);
        p.setDelay(0);
        save();
    }
}
