package dev.artixdev.practice.configs;

import dev.artixdev.practice.models.ShopProduct;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Carrega shop.yml – lista de produtos (títulos, resets) e configurações.
 */
public class ShopConfig {

    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;
    private List<ShopProduct> products;
    private int defaultEloOnReset;
    private String menuTitle;

    public ShopConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "shop.yml");
        this.products = new ArrayList<>();
        this.defaultEloOnReset = 1000;
        this.menuTitle = "&c&lShop &8>> &fCoins";
        load();
    }

    public void load() {
        products.clear();
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            plugin.saveResource("shop.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        defaultEloOnReset = config.getInt("SETTINGS.DEFAULT_ELO_ON_RESET", 1000);
        menuTitle = config.getString("SETTINGS.MENU_TITLE", "&c&lShop &8>> &fCoins");

        ConfigurationSection productsRoot = config.getConfigurationSection("PRODUCTS");
        if (productsRoot != null) {
            for (String key : productsRoot.getKeys(false)) {
                ConfigurationSection item = productsRoot.getConfigurationSection(key);
                if (item != null) {
                    addProductFromSection(item);
                }
            }
        }
    }

    private void addProductFromSection(ConfigurationSection section) {
        String id = section.getString("id", "product_" + products.size());
        String type = section.getString("type", "TITLE");
        String titleId = section.getString("title_id");
        int price = section.getInt("price", 0);
        String displayName = section.getString("display_name", id);
        String material = section.getString("material", "PAPER");
        List<String> description = section.getStringList("description");
        if (description == null) description = new ArrayList<>();
        products.add(new ShopProduct(id, type, titleId, price, displayName, material, description));
    }

    public List<ShopProduct> getProducts() {
        return new ArrayList<>(products);
    }

    public ShopProduct getProductById(String id) {
        if (id == null) return null;
        for (ShopProduct p : products) {
            if (id.equals(p.getId())) return p;
        }
        return null;
    }

    public int getDefaultEloOnReset() {
        return defaultEloOnReset;
    }

    public String getMenuTitle() {
        return menuTitle;
    }
}
