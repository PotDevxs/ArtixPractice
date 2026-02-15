package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ItemBuilder;

public class HealthButton extends Button {
    private final int health;
    private final PlayerProfile playerProfile;

    public HealthButton(PlayerProfile playerProfile, int health) {
        this.playerProfile = playerProfile;
        this.health = health;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial());
        
        String name = MatchMenus.MATCH_INVENTORY_HEALTH_NAME
                .replace("{health}", Integer.toString(this.health));
        
        builder.setName(name);
        builder.setDurability(MatchMenus.MATCH_INVENTORY_HEALTH_DURABILITY);
        builder.setLore(MatchMenus.MATCH_INVENTORY_HEALTH_LORE);
        builder.setGlow(true);
        
        ItemStack item = builder.build();
        
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            
            if (meta instanceof SkullMeta) {
                String username = this.playerProfile.getUsername();
                Skin skin = this.playerProfile.getSkin();
                String skinValue = skin.getValue();
                String skinSignature = skin.getSignature();
                
                item.setItemMeta(ItemBuilder.setSkullMeta(meta, username, skinValue, skinSignature));
            }
        }
        
        return item;
    }
}
