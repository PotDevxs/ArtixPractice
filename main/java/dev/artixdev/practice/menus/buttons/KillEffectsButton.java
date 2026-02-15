package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.enums.KillEffectType;
import dev.artixdev.practice.menus.KillEffectsMenu;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.CosmeticSettings;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.Arrays;

public class KillEffectsButton extends Button {

    @Override
    public void clicked(Player player, ClickType clickType) {
        Main plugin = Main.getInstance();
        PlayerProfile playerProfile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        
        if (playerProfile == null) {
            return;
        }

        CosmeticSettings cosmeticSettings = playerProfile.getCosmeticSettings();
        
        // Open kill effects menu
        KillEffectsMenu killEffectsMenu = new KillEffectsMenu(cosmeticSettings);
        MenuHandler.getInstance().openMenu(killEffectsMenu, player);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        Main plugin = Main.getInstance();
        PlayerProfile playerProfile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        
        if (playerProfile == null) {
            return new ItemBuilder(XMaterial.BLAZE_ROD).build();
        }

        CosmeticSettings cosmeticSettings = playerProfile.getCosmeticSettings();
        CosmeticSettings.KillEffect currentEffect = cosmeticSettings.getKillEffect();
        
        ItemBuilder builder = new ItemBuilder(XMaterial.BLAZE_ROD)
                .name(GeneralMenus.COSMETICS_MENU_KILL_EFFECTS_NAME)
                .durability((short) GeneralMenus.COSMETICS_MENU_KILL_EFFECTS_DURABILITY);

        int totalEffects = KillEffectType.values().length;
        int availableEffects = player.isOp() ? totalEffects : 
                (int) Arrays.stream(KillEffectType.values())
                        .filter(effect -> effect.hasPermission(player))
                        .count();

        String effectName = currentEffect != null ? currentEffect.getName() : "None";

        for (String loreLine : GeneralMenus.COSMETICS_MENU_KILL_EFFECTS_LORE) {
            String processedLore = loreLine
                    .replace("<available>", String.valueOf(availableEffects))
                    .replace("<total>", String.valueOf(totalEffects))
                    .replace("<effect>", effectName);
            builder.lore(processedLore);
        }

        return builder.build();
    }
}
