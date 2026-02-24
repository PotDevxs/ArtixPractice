package dev.artixdev.practice.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.ShopConfig;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.ShopProduct;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.Messages;
import dev.artixdev.practice.utils.ModernMenuStyle;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Menu do Shop – produtos definidos em shop.yml (títulos, reset KDR, reset ELO, resets).
 */
public class ShopMenu extends Menu {

    private static final int SIZE = 45;
    private final Main plugin = Main.getInstance();

    public ShopMenu() {
        this.setPlaceholder(true);
        this.setBordered(true);
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public String getTitle(Player player) {
        ShopConfig config = plugin.getConfigManager() != null ? plugin.getConfigManager().getShopConfig() : null;
        if (config != null) return ChatUtils.colorize(config.getMenuTitle());
        return Messages.get("SHOP.MENU_TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        ShopConfig config = plugin.getConfigManager() != null ? plugin.getConfigManager().getShopConfig() : null;
        if (config == null) return buttons;

        List<ShopProduct> products = config.getProducts();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33};
        for (int i = 0; i < products.size() && i < slots.length; i++) {
            buttons.put(slots[i], new ProductButton(products.get(i), profile));
        }

        buttons.put(40, new BackButton());
        return buttons;
    }

    private class ProductButton extends Button {
        private final ShopProduct product;
        private final PlayerProfile profile;

        ProductButton(ShopProduct product, PlayerProfile profile) {
            this.product = product;
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            boolean canAfford = profile != null && profile.getCoins() >= product.getPrice();
            List<String> lore = new ArrayList<>(product.getDescription());
            lore.add("");
            lore.add(Messages.get("SHOP.LORE_PRICE", "amount", String.valueOf(product.getPrice())));
            lore.add("");
            if (canAfford) {
                lore.add(Messages.get("SHOP.LORE_CLICK_PURCHASE"));
            } else {
                lore.add(Messages.get("SHOP.LORE_NOT_ENOUGH_COINS"));
            }

            XMaterial mat = XMaterial.matchXMaterial(product.getMaterial()).orElse(XMaterial.PAPER);
            return new ItemBuilder(mat)
                .name(ChatUtils.colorize(product.getDisplayName()))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            PlayerProfile currentProfile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
            if (currentProfile == null) {
                player.sendMessage(Messages.get("SHOP.PROFILE_NOT_LOADED"));
                return;
            }
            if (currentProfile.getCoins() < product.getPrice()) {
                player.sendMessage(Messages.get("SHOP.NOT_ENOUGH_COINS", "amount", String.valueOf(product.getPrice())));
                return;
            }
            if (plugin.getShopManager().purchase(player, currentProfile, product.getId())) {
                player.sendMessage(Messages.get("SHOP.PURCHASED", "item", product.getDisplayName()));
                MenuHandler.getInstance().openMenu(new ShopMenu(), player);
            } else {
                player.sendMessage(Messages.get("SHOP.PURCHASE_FAILED"));
            }
        }
    }

    private static class BackButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.ARROW)
                .name(Messages.get("SHOP.BACK"))
                .lore(ModernMenuStyle.tooltip(null, new String[]{"Return to Profile menu."}, null, "Click to go back."))
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new ProfileMenu(), player);
        }
    }
}
