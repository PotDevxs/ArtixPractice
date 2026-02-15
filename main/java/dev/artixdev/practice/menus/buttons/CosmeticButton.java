package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.utils.ItemBuilder;

public class CosmeticButton extends Button {

    public CosmeticButton() {
        super();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        XMaterial material = XMaterial.matchXMaterial(GeneralMenus.PROFILE_MENU_COSMETICS_MATERIAL).orElse(XMaterial.FIREWORK_ROCKET);
        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.name(GeneralMenus.PROFILE_MENU_COSMETICS_NAME);
        itemBuilder.lore(GeneralMenus.PROFILE_MENU_COSMETICS_LORE);
        itemBuilder.durability((short) GeneralMenus.PROFILE_MENU_COSMETICS_DURABILITY);
        
        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        // Implementation for cosmetic button click
        // This would typically open a cosmetic menu
    }
}
