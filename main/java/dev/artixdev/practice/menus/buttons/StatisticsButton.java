package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.managers.PlayerManager;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.menus.StatisticsMenu;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.UUID;

public class StatisticsButton extends Button {

    public StatisticsButton() {
        super();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        XMaterial material = XMaterial.matchXMaterial(GeneralMenus.PROFILE_MENU_STATISTICS_MATERIAL).orElse(XMaterial.EMERALD);
        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.name(GeneralMenus.PROFILE_MENU_STATISTICS_NAME);
        itemBuilder.lore(GeneralMenus.PROFILE_MENU_STATISTICS_LORE);
        itemBuilder.durability((short) GeneralMenus.PROFILE_MENU_STATISTICS_DURABILITY);
        
        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Main plugin = Main.getInstance();
        PlayerManager playerManager = plugin.getPlayerManager();
        UUID playerId = player.getUniqueId();
        PlayerProfile playerProfile = playerManager.getPlayerProfile(playerId);
        
        // Open statistics menu
        // TODO: Get previous menu if needed
        StatisticsMenu statisticsMenu = new StatisticsMenu(null, playerProfile);
        MenuHandler.getInstance().openMenu(statisticsMenu, player);
    }
}