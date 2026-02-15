package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.enums.KillEffectType;
import dev.artixdev.practice.menus.KillEffectsMenu;
import dev.artixdev.practice.models.CosmeticSettings;
import dev.artixdev.practice.utils.ItemBuilder;

public class KillEffectButton extends Button {

    private final KillEffectType effectType;
    private final CosmeticSettings cosmeticSettings;

    public KillEffectButton(KillEffectType effectType, CosmeticSettings cosmeticSettings) {
        this.effectType = effectType;
        this.cosmeticSettings = cosmeticSettings;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!effectType.hasPermission(player)) {
            player.sendMessage("§cYou don't have permission to use this kill effect!");
            return;
        }

        // Convert KillEffectType to CosmeticSettings.KillEffect
        CosmeticSettings.KillEffect killEffect = convertToCosmeticKillEffect(effectType);
        cosmeticSettings.setKillEffect(killEffect);
        player.sendMessage("§aKill effect set to: §f" + effectType.getName());
        
        // Update the menu
        KillEffectsMenu killEffectsMenu = new KillEffectsMenu(cosmeticSettings);
        MenuHandler.getInstance().openMenu(killEffectsMenu, player);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        CosmeticSettings.KillEffect currentEffect = cosmeticSettings.getKillEffect();
        CosmeticSettings.KillEffect targetEffect = convertToCosmeticKillEffect(effectType);
        boolean isSelected = currentEffect == targetEffect;
        boolean hasPermission = effectType.hasPermission(player);
        
        XMaterial material = isSelected ? XMaterial.LIME_DYE : 
                           hasPermission ? XMaterial.GRAY_DYE : XMaterial.RED_DYE;
        
        ItemBuilder builder = new ItemBuilder(material)
                .name((isSelected ? "§a" : hasPermission ? "§7" : "§c") + effectType.getName())
                .lore("§7Click to select this kill effect");
        
        if (isSelected) {
            builder.lore("§aCurrently selected!");
        } else if (!hasPermission) {
            builder.lore("§cYou don't have permission!");
        }
        
        return builder.build();
    }
    
    /**
     * Convert KillEffectType to CosmeticSettings.KillEffect
     * @param effectType the KillEffectType
     * @return CosmeticSettings.KillEffect
     */
    private CosmeticSettings.KillEffect convertToCosmeticKillEffect(KillEffectType effectType) {
        if (effectType == null) {
            return CosmeticSettings.KillEffect.NONE;
        }
        
        try {
            // Try to match by name
            return CosmeticSettings.KillEffect.valueOf(effectType.getName().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            // Map common effects
            switch (effectType) {
                case NONE:
                    return CosmeticSettings.KillEffect.NONE;
                case EXPLOSION:
                    return CosmeticSettings.KillEffect.EXPLOSION;
                case LIGHTNING:
                    return CosmeticSettings.KillEffect.LIGHTNING;
                case FIREWORK:
                    return CosmeticSettings.KillEffect.FIREWORK;
                case HEARTS:
                    return CosmeticSettings.KillEffect.HEARTS;
                case VILLAGER_HAPPY:
                    return CosmeticSettings.KillEffect.VILLAGER_HAPPY;
                case VILLAGER_ANGRY:
                    return CosmeticSettings.KillEffect.ANGRY_VILLAGER;
                default:
                    return CosmeticSettings.KillEffect.NONE;
            }
        }
    }
}
