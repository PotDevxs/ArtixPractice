package dev.artixdev.practice.configs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Carrega messages.yml – centraliza todas as mensagens do plugin.
 * Placeholders no formato {nome} são substituídos por get(key, "nome", valor, ...).
 */
public class MessagesConfig {

    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;
    private String prefix;

    public MessagesConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "messages.yml");
        this.prefix = "&8[&bPractice&8] &r";
        load();
    }

    public void load() {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        prefix = config.getString("PREFIX", "&8[&bPractice&8] &r");
    }

    /**
     * Obtém mensagem pela chave. Suporta chaves com seção: "GENERAL.WELCOME_LINE1" ou "MATCH.YOU_WON".
     */
    public String get(String key) {
        if (key == null) return "";
        String path = key.contains(".") ? key : key;
        String value = config.getString(path);
        return value != null ? value : "&7[" + key + "]";
    }

    /**
     * Obtém mensagem e substitui placeholders. Par pares: "player", "Steve", "amount", "5" -> {player} e {amount}.
     */
    public String get(String key, String... placeholderPairs) {
        String msg = get(key);
        if (placeholderPairs != null && placeholderPairs.length >= 2) {
            for (int i = 0; i < placeholderPairs.length - 1; i += 2) {
                String k = placeholderPairs[i];
                String v = placeholderPairs[i + 1];
                if (k != null && v != null) {
                    msg = msg.replace("{" + k + "}", v);
                }
            }
        }
        return msg;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void reload() {
        load();
    }
}
