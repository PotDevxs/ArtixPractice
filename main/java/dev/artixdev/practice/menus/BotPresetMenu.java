package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.QueueMenus;
import dev.artixdev.practice.models.BotSettings;
import dev.artixdev.practice.enums.Difficulty;

import java.util.HashMap;
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
        
        // Add bot preset buttons based on difficulty
        // This would typically include buttons for different bot presets
        // like EASY, MEDIUM, HARD, etc.
        
        return buttons;
    }
}
