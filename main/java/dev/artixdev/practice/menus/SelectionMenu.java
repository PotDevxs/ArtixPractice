package dev.artixdev.practice.menus;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.other.Callback;

import java.util.HashMap;
import java.util.Map;

public class SelectionMenu extends Menu {
    private final Callback<Integer> callback;

    public SelectionMenu(Callback<Integer> callback) {
        this.callback = callback;
        this.setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Select an option";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        buttons.put(11, new SelectionButton(2));
        buttons.put(13, new SelectionButton(3));
        buttons.put(15, new SelectionButton(4));
        buttons.put(17, new SelectionButton(5));
        
        return buttons;
    }

    @Override
    public int getSize() {
        return 27;
    }

    private class SelectionButton extends Button {
        private final int value;

        public SelectionButton(int value) {
            this.value = value;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder itemBuilder = new ItemBuilder(XMaterial.PAPER);
            itemBuilder.name("Option " + value);
            itemBuilder.lore(ImmutableList.of("", "Click to select this option"));
            
            return itemBuilder.build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            callback.call(value);
            player.closeInventory();
        }
    }
}
