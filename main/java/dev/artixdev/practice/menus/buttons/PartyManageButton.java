package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.PartyManageMenu;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PartyManageButton extends Button {
    public static final int BUTTON_VERSION = 1;
    private static final String[] BUTTON_CONSTANTS = {"PartyManageButton"};
    public static final boolean DEBUG_MODE = false;
    private static final String[] BUTTON_MESSAGES = new String[7];
    private final Team team;

    public PartyManageButton(Team team) {
        super();
        this.team = team;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (team != null) {
            MenuHandler.getInstance().openMenu(new PartyManageMenu(team), player);
        }
    }

    static {
        BUTTON_MESSAGES[0] = "Party Management";
        BUTTON_MESSAGES[1] = "Click to manage party";
        BUTTON_MESSAGES[2] = "Settings";
        BUTTON_MESSAGES[3] = "Members";
        BUTTON_MESSAGES[4] = "Invite";
        BUTTON_MESSAGES[5] = "Disband";
        BUTTON_MESSAGES[6] = "Leave";
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = Arrays.stream(BUTTON_MESSAGES).skip(1).limit(3)
            .map(s -> "&7" + s)
            .map(ChatUtils::colorize)
            .collect(Collectors.toList());
        return new ItemBuilder(XMaterial.BOOK)
            .name(ChatUtils.colorize("&e" + BUTTON_MESSAGES[0]))
            .lore(lore)
            .build();
    }
}
