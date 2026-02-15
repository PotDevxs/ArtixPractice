package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.Iterator;

public class PotionButton extends Button {
    private final String potionType;
    private final int amount;

    public PotionButton(String potionType, int amount) {
        this.potionType = potionType;
        this.amount = amount;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(XMaterial.SPLASH_POTION.parseMaterial());
        
        int displayAmount = this.amount != 0 ? this.amount : 1;
        builder.setAmount(displayAmount);
        
        String name = MatchMenus.MATCH_INVENTORY_POTIONS_NAME
                .replace("{amount}", Integer.toString(this.amount));
        
        builder.setName(name);
        builder.setDurability(MatchMenus.MATCH_INVENTORY_POTIONS_DURABILITY);
        
        // Add lore with potion type and amount
        for (String loreLine : MatchMenus.MATCH_INVENTORY_POTIONS_LORE) {
            String processedLore = loreLine
                    .replace("{potion_type}", this.potionType)
                    .replace("{amount}", Integer.toString(this.amount));
            builder.addLore(processedLore);
        }
        
        return builder.build();
    }
}
