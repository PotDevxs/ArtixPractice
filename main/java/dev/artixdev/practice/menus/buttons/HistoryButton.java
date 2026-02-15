package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.menus.MatchHistoryMenu;
import dev.artixdev.practice.utils.ItemBuilder;

public class HistoryButton extends Button {

    public HistoryButton() {
        super();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        XMaterial material = XMaterial.matchXMaterial(GeneralMenus.PROFILE_MENU_HISTORY_MATERIAL).orElse(XMaterial.PAPER);
        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.name(GeneralMenus.PROFILE_MENU_HISTORY_NAME);
        itemBuilder.lore(GeneralMenus.PROFILE_MENU_HISTORY_LORE);
        itemBuilder.durability((short) GeneralMenus.PROFILE_MENU_HISTORY_DURABILITY);
        
        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        MatchHistoryMenu matchHistoryMenu = new MatchHistoryMenu(null);
        MenuHandler.getInstance().openMenu(matchHistoryMenu, player);
    }
}
