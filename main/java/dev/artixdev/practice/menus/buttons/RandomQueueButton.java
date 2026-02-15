package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.QueueMenus;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.utils.ItemBuilder;

public class RandomQueueButton extends Button {
    public static final int BUTTON_VERSION = 1;
    private final Queue queue;
    public static final boolean DEBUG_MODE = false;

    public RandomQueueButton(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for random queue button click handling
        // Typically used to join a random queue
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(XMaterial.ENDER_EYE);
        itemBuilder.name(QueueMenus.RANDOM_QUEUE_NAME);
        itemBuilder.durability((short) QueueMenus.RANDOM_QUEUE_DURABILITY);
        itemBuilder.lore(QueueMenus.RANDOM_QUEUE_LORE);
        return itemBuilder.build();
    }
}
