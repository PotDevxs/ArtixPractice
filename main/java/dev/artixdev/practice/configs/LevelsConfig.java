package dev.artixdev.practice.configs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Carrega levels.yml e fornece nível e progresso a partir do XP total.
 * LEVELS no YAML: nível (1, 2, 3...) -> XP cumulativo mínimo para esse nível.
 */
public class LevelsConfig {

    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;
    /** Índice 0 = XP para nível 1, índice 1 = XP para nível 2, ... */
    private final List<Integer> xpPerLevel = new ArrayList<>();
    /** Faixas de nível -> cor (ex: [1,20,"&7"], [21,40,"&f"], ...). Ordenado por minLevel. */
    private final List<int[]> colorRangesMinMax = new ArrayList<>();
    private final List<String> colorRangesColor = new ArrayList<>();
    private boolean useXpForLevel = true;
    private int startingLevel = 1;
    private int startingXp = 0;
    private static final Pattern RANGE_PATTERN = Pattern.compile("^(\\d+)-(\\d+)$");

    public LevelsConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "levels.yml");
        load();
    }

    public void load() {
        xpPerLevel.clear();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            plugin.saveResource("levels.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        useXpForLevel = config.getBoolean("SETTINGS.USE_XP_FOR_LEVEL", true);
        startingLevel = config.getInt("SETTINGS.STARTING_LEVEL", 1);
        startingXp = config.getInt("SETTINGS.STARTING_XP", 0);

        ConfigurationSection section = config.getConfigurationSection("LEVELS");
        if (section != null) {
            TreeMap<Integer, Integer> sorted = new TreeMap<>();
            for (String key : section.getKeys(false)) {
                try {
                    int level = Integer.parseInt(key);
                    int xp = section.getInt(key, 0);
                    sorted.put(level, xp);
                } catch (NumberFormatException ignored) { }
            }
            int maxLevel = sorted.isEmpty() ? 1 : sorted.lastKey();
            for (int l = 1; l <= maxLevel; l++) {
                xpPerLevel.add(sorted.getOrDefault(l, l == 1 ? 0 : 100 * l));
            }
        }
        if (xpPerLevel.isEmpty()) {
            xpPerLevel.add(0);
            xpPerLevel.add(100);
        }

        // LEVEL_COLORS: "min-max" -> "&x"
        colorRangesMinMax.clear();
        colorRangesColor.clear();
        ConfigurationSection colorsSection = config.getConfigurationSection("LEVEL_COLORS");
        if (colorsSection != null) {
            TreeMap<Integer, String> byMin = new TreeMap<>();
            for (String key : colorsSection.getKeys(false)) {
                Matcher m = RANGE_PATTERN.matcher(key.trim());
                if (m.matches()) {
                    int min = Integer.parseInt(m.group(1));
                    int max = Integer.parseInt(m.group(2));
                    String color = colorsSection.getString(key, "&7");
                    if (color == null) color = "&7";
                    byMin.put(min, min + "," + max + "," + color);
                }
            }
            for (String value : byMin.values()) {
                String[] parts = value.split(",", 3);
                if (parts.length >= 3) {
                    colorRangesMinMax.add(new int[]{ Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) });
                    colorRangesColor.add(parts[2]);
                }
            }
        }
        if (colorRangesColor.isEmpty()) {
            colorRangesMinMax.add(new int[]{ 1, 200 });
            colorRangesColor.add("&7");
        }
    }

    /** XP cumulativo necessário para estar no nível (nível 1-based). */
    public int getXpRequiredForLevel(int level) {
        if (level <= 0) return 0;
        if (level <= xpPerLevel.size()) return xpPerLevel.get(level - 1);
        int last = xpPerLevel.get(xpPerLevel.size() - 1);
        return last + 500 * (level - xpPerLevel.size());
    }

    /** Nível calculado a partir do XP total (1-based). */
    public int getLevelFromXp(int totalXp) {
        if (totalXp <= 0) return startingLevel;
        for (int i = xpPerLevel.size() - 1; i >= 0; i--) {
            if (totalXp >= xpPerLevel.get(i)) return i + 1;
        }
        return startingLevel;
    }

    /** Progresso dentro do nível atual (0-100). Para barra de progresso. */
    public int getXpProgressInLevel(int totalXp) {
        int level = getLevelFromXp(totalXp);
        int xpForCurrent = getXpRequiredForLevel(level);
        int xpForNext = getXpRequiredForLevel(level + 1);
        int range = xpForNext - xpForCurrent;
        if (range <= 0) return 100;
        int currentInLevel = totalXp - xpForCurrent;
        return Math.min(100, Math.max(0, (currentInLevel * 100) / range));
    }

    /** XP necessário para o próximo nível (quanto falta). */
    public int getXpForNextLevel(int totalXp) {
        return Math.max(0, getXpRequiredForLevel(getLevelFromXp(totalXp) + 1) - totalXp);
    }

    public boolean isUseXpForLevel() {
        return useXpForLevel;
    }

    public int getStartingLevel() {
        return startingLevel;
    }

    public int getStartingXp() {
        return startingXp;
    }

    /**
     * Retorna o código de cor (ex: "&7", "&a") para o nível dado, conforme LEVEL_COLORS no YAML.
     */
    public String getColorForLevel(int level) {
        if (level < 1) return "&7";
        for (int i = 0; i < colorRangesMinMax.size(); i++) {
            int[] range = colorRangesMinMax.get(i);
            if (level >= range[0] && level <= range[1]) {
                return colorRangesColor.get(i);
            }
        }
        return colorRangesColor.isEmpty() ? "&7" : colorRangesColor.get(colorRangesColor.size() - 1);
    }

    public void reload() {
        load();
    }
}
