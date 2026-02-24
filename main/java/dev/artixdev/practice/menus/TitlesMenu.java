package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.PlayerTitle;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.Messages;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class TitlesMenu extends Menu {

    private final Main plugin = Main.getInstance();
    private final PlayerProfile profile;

    public TitlesMenu(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public int getSize() {
        return 45;
    }

    @Override
    public String getTitle(Player player) {
        return Messages.get("TITLES.MENU_TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerTitle[] values = PlayerTitle.values();
        for (int i = 0; i < values.length && i < 45; i++) {
            buttons.put(i, new TitleButton(values[i]));
        }
        return buttons;
    }

    private int getProfileValue(PlayerTitle title) {
        if (profile == null) return 0;
        switch (title.getRequirementKey()) {
            case "kills": return profile.getKills();
            case "wins": return profile.getWins();
            case "bestWinStreak": return profile.getBestWinStreak();
            case "elo": return profile.getElo();
            case "totalMatches": return profile.getWins() + profile.getLosses();
            default: return 0;
        }
    }

    private class TitleButton extends Button {
        private final PlayerTitle title;

        TitleButton(PlayerTitle title) {
            this.title = title;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            boolean unlocked = title == PlayerTitle.NONE || getProfileValue(title) >= title.getRequirementValue() || profile.hasUnlockedTitleId(title.getId());
            boolean selected = title.getId().equalsIgnoreCase(profile.getSelectedTitleId());
            ItemBuilder builder = new ItemBuilder(selected ? XMaterial.LIME_DYE : (unlocked ? XMaterial.PAPER : XMaterial.GRAY_DYE))
                .name(ChatUtils.colorize((selected ? "&a► " : "") + title.getDisplay() + (title == PlayerTitle.NONE ? Messages.get("TITLES.ITEM_NONE_SUFFIX") : "")))
                .lore(
                    title == PlayerTitle.NONE ? Messages.get("TITLES.LORE_REMOVE_TITLE") : Messages.get("TITLES.LORE_REQUIRES", "key", title.getRequirementKey(), "value", String.valueOf(title.getRequirementValue())),
                    unlocked ? Messages.get("TITLES.LORE_UNLOCKED") : Messages.get("TITLES.LORE_LOCKED"),
                    selected ? Messages.get("TITLES.LORE_SELECTED") : Messages.get("TITLES.LORE_CLICK_SELECT")
                );
            return builder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (getProfileValue(title) < title.getRequirementValue() && title != PlayerTitle.NONE) {
                player.sendMessage(Messages.get("TITLES.NOT_UNLOCKED"));
                return;
            }
            profile.setSelectedTitleId(title.getId());
            player.sendMessage(Messages.get("TITLES.TITLE_SET", "title", ChatUtils.colorize(title.getDisplay())));
            MenuHandler.getInstance().openMenu(new TitlesMenu(profile), player);
        }
    }
}
