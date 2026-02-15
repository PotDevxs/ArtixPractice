package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.enums.KillEffectType;
import dev.artixdev.practice.models.CosmeticSettings;
import dev.artixdev.practice.menus.buttons.KillEffectButton;

import java.util.HashMap;
import java.util.Map;

public class KillEffectsMenu extends Menu {

    private final CosmeticSettings cosmeticSettings;

    public KillEffectsMenu(CosmeticSettings cosmeticSettings) {
        this.cosmeticSettings = cosmeticSettings;
        setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return GeneralMenus.KILL_EFFECTS_TITLE;
    }

    @Override
    public int getSize() {
        return 45;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        int slot = 0;
        for (KillEffectType effectType : KillEffectType.values()) {
            if (slot >= 45) break;
            
            buttons.put(slot, new KillEffectButton(effectType, cosmeticSettings));
            slot++;
        }
        
        return buttons;
    }
}
