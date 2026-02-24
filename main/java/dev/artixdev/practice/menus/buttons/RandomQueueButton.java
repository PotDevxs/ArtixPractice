package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.QueueMenus;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.models.Queue;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomQueueButton extends Button {
    public static final int BUTTON_VERSION = 1;
    private final Queue queue;
    public static final boolean DEBUG_MODE = false;
    private static final Random RANDOM = new Random();

    public RandomQueueButton(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        List<KitType> types = new ArrayList<>();
        for (KitType k : KitType.values()) {
            if (k != KitType.CUSTOM) types.add(k);
        }
        if (types.isEmpty()) {
            player.sendMessage("§cNo queue available.");
            return;
        }
        KitType randomKit = types.get(RANDOM.nextInt(types.size()));
        if (Main.getInstance().getQueueManager().addPlayerToQueue(player, EventType.UNRANKED, randomKit)) {
            player.sendMessage("§aJoined random queue: §f" + randomKit.getDisplayName());
            player.closeInventory();
        }
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
