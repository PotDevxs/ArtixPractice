package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.AchievementType;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.Messages;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class AchievementsMenu extends Menu {

    private final Main plugin = Main.getInstance();
    private final PlayerProfile profile;

    public AchievementsMenu(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public String getTitle(Player player) {
        return Messages.get("ACHIEVEMENTS.MENU_TITLE", "unlocked", String.valueOf(profile.getUnlockedAchievements().size()), "total", String.valueOf(AchievementType.values().length));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        AchievementType[] values = AchievementType.values();
        for (int i = 0; i < values.length && i < 45; i++) {
            buttons.put(i, new AchievementButton(values[i]));
        }
        return buttons;
    }

    private class AchievementButton extends Button {
        private final AchievementType type;

        AchievementButton(AchievementType type) {
            this.type = type;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            boolean unlocked = plugin.getAchievementsManager() != null && plugin.getAchievementsManager().isUnlocked(profile, type);
            int current = plugin.getAchievementsManager() != null ? plugin.getAchievementsManager().getProgressValue(profile, type) : 0;
            int target = type.getTargetValue();
            XMaterial mat = unlocked ? XMaterial.matchXMaterial(type.getIconMaterial().name()).orElse(XMaterial.PAPER) : XMaterial.GRAY_DYE;
            ItemBuilder builder = new ItemBuilder(mat)
                .name(ChatUtils.colorize((unlocked ? "&a" : "&7") + type.getDisplayName()))
                .lore(
                    ChatUtils.colorize(type.getDescription()),
                    "",
                    Messages.get("ACHIEVEMENTS.LORE_PROGRESS", "current", String.valueOf(current), "target", String.valueOf(target)),
                    unlocked ? Messages.get("ACHIEVEMENTS.LORE_UNLOCKED") : Messages.get("ACHIEVEMENTS.LORE_LOCKED")
                );
            return builder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            // View only; no action required
        }
    }
}
