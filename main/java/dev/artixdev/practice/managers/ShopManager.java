package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.ShopConfig;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.ShopProduct;
import org.bukkit.entity.Player;

/**
 * Processa compras do shop: desconta coins e aplica o efeito do produto (título, resets).
 */
public class ShopManager {

    private final Main plugin;

    public ShopManager(Main plugin) {
        this.plugin = plugin;
    }

    private ShopConfig getShopConfig() {
        if (plugin.getConfigManager() == null) return null;
        return plugin.getConfigManager().getShopConfig();
    }

    /**
     * Tenta comprar o produto. Retorna true se a compra foi concluída.
     */
    public boolean purchase(Player player, PlayerProfile profile, String productId) {
        ShopConfig config = getShopConfig();
        if (config == null) return false;
        ShopProduct product = config.getProductById(productId);
        if (product == null) return false;
        if (profile.getCoins() < product.getPrice()) return false;

        profile.setCoins(profile.getCoins() - product.getPrice());

        if (product.isTitle()) {
            profile.addUnlockedTitleId(product.getTitleId());
            return true;
        }
        if (product.isResetKdr()) {
            profile.setKills(0);
            profile.setDeaths(0);
            return true;
        }
        if (product.isResetElo()) {
            int defaultElo = config.getDefaultEloOnReset();
            profile.setElo(defaultElo);
            profile.setPreviousElo(defaultElo);
            return true;
        }
        if (product.isResetWinsLosses()) {
            profile.setWins(0);
            profile.setLosses(0);
            profile.setRankedWins(0);
            profile.setRankedLosses(0);
            return true;
        }
        if (product.isResetWinstreak()) {
            profile.setWinStreak(0);
            profile.setBestWinStreak(0);
            return true;
        }
        if (product.isResetStats()) {
            int defaultElo = config.getDefaultEloOnReset();
            profile.setKills(0);
            profile.setDeaths(0);
            profile.setWins(0);
            profile.setLosses(0);
            profile.setRankedWins(0);
            profile.setRankedLosses(0);
            profile.setWinStreak(0);
            profile.setBestWinStreak(0);
            profile.setElo(defaultElo);
            profile.setPreviousElo(defaultElo);
            return true;
        }
        return false;
    }

    public boolean canAfford(PlayerProfile profile, int price) {
        return profile != null && profile.getCoins() >= price;
    }
}
