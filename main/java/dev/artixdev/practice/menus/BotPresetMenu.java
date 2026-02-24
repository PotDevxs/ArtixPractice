package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.QueueMenus;
import dev.artixdev.practice.models.BotSettings;
import dev.artixdev.practice.enums.Difficulty;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotPresetMenu extends Menu {

    private final BotSettings botSettings;
    private final Difficulty difficulty;

    public BotPresetMenu(BotSettings botSettings, Difficulty difficulty) {
        this.botSettings = botSettings;
        this.difficulty = difficulty;
        setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return QueueMenus.BOT_PRESET_TITLE;
    }

    @Override
    public int getSize() {
        return QueueMenus.BOT_PRESET_SIZE;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Difficulty d : Difficulty.values()) {
            buttons.put(slot++, new BotPresetButton(d, difficulty));
        }

        buttons.put(QueueMenus.BOT_PRESET_CUSTOM_SLOT, new CustomPresetButton(botSettings, difficulty));

        return buttons;
    }

    private class BotPresetButton extends Button {
        private final Difficulty preset;
        private final Difficulty current;

        BotPresetButton(Difficulty preset, Difficulty current) {
            this.preset = preset;
            this.current = current;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            String name = QueueMenus.BOT_PRESET_NAME
                .replace("<display_name>", preset.getColor() + preset.getDisplayName());
            int cps = preset.getLevel() * 2 + 4;
            double range = 2.5 + preset.getLevel() * 0.5;
            List<String> lore = new ArrayList<>();
            for (String line : QueueMenus.BOT_PRESET_LORE) {
                lore.add(ChatUtils.colorize(line
                    .replace("<display_name>", preset.getDisplayName())
                    .replace("<cps>", String.valueOf(cps))
                    .replace("<range>", String.format("%.1f", range))
                    .replace("<ping>", "0")
                    .replace("<tryhard>", preset.isAdvanced() ? "Yes" : "No")
                    .replace("<strafe>", preset.getLevel() >= 4 ? "Yes" : "No")
                    .replace("<wtap>", "Yes")
                    .replace("<blockhit>", preset.getLevel() >= 4 ? "Yes" : "No")
                    .replace("<sidepearl>", preset == Difficulty.MASTER ? "Yes" : "No")));
            }
            XMaterial mat = XMaterial.matchXMaterial(preset.getIcon()).orElse(XMaterial.DIAMOND_SWORD);
            return new ItemBuilder(mat).name(ChatUtils.colorize(name)).lore(lore).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.sendMessage(ChatUtils.colorize("&aSelected bot preset: &f" + preset.getDisplayName()
                + (preset == current ? " &7(current)" : "")));
            player.closeInventory();
        }
    }

    private class CustomPresetButton extends Button {
        private final BotSettings settings;
        private final Difficulty currentDifficulty;

        CustomPresetButton(BotSettings settings, Difficulty currentDifficulty) {
            this.settings = settings;
            this.currentDifficulty = currentDifficulty;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            for (String line : QueueMenus.BOT_PRESET_CUSTOM_LORE) {
               lore.add(ChatUtils.colorize(line));
            }
            if (settings != null) {
                lore.add(ChatUtils.colorize("&7Current: " + (currentDifficulty != null ? currentDifficulty.getDisplayName() : settings.getDifficultyName())));
            }
            return new ItemBuilder(XMaterial.NETHER_STAR)
                .name(ChatUtils.colorize(QueueMenus.BOT_PRESET_CUSTOM_NAME))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                player.sendMessage(ChatUtils.colorize("&7Using custom preset."));
            } else if (clickType == ClickType.RIGHT) {
                player.sendMessage(ChatUtils.colorize("&7Edit custom preset (use bot settings)."));
            }
            player.closeInventory();
        }
    }
}
