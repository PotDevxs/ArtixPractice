package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartySettingsMenu extends Menu {
    public static final int MENU_VERSION = 1;
    public static final boolean DEBUG_MODE = false;

    public PartySettingsMenu() {
        super();
        this.setBordered(true);
    }

    @Override
    public String getTitle(Player player) {
        return ChatUtils.translate(PartyMenus.PARTY_SETTINGS_MENU_TITLE);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Team team = Main.getInstance() != null ? Main.getInstance().getPartyManager().getPlayerParty(player.getUniqueId()) : null;
        if (team == null) {
            return buttons;
        }
        buttons.put(PartyMenus.PARTY_SETTINGS_MENU_DUEL_REQUESTS_SLOT, new PartySettingButton(
            PartyMenus.PARTY_SETTINGS_MENU_DUEL_REQUESTS_NAME,
            PartyMenus.PARTY_SETTINGS_MENU_DUEL_REQUESTS_MATERIAL,
            PartyMenus.PARTY_SETTINGS_MENU_DUEL_REQUESTS_DURABILITY,
            PartyMenus.PARTY_SETTINGS_MENU_DUEL_REQUESTS_ENABLED_LORE,
            PartyMenus.PARTY_SETTINGS_MENU_DUEL_REQUESTS_DISABLED_LORE,
            true));
        buttons.put(PartyMenus.PARTY_SETTINGS_MENU_PARTY_MANAGE_SLOT, new PartyManageButton(team));
        return buttons;
    }

    @Override
    public int getSize() {
        return 45;
    }

    private static class PartySettingButton extends Button {
        private final String name;
        private final String materialName;
        private final short durability;
        private final java.util.List<String> enabledLore;
        private final java.util.List<String> disabledLore;
        private boolean enabled;

        PartySettingButton(String name, String materialName, int durability,
                           java.util.List<String> enabledLore, java.util.List<String> disabledLore, boolean defaultEnabled) {
            this.name = ChatUtils.translate(name);
            this.materialName = materialName;
            this.durability = (short) durability;
            this.enabledLore = new java.util.ArrayList<>();
            if (enabledLore != null) for (String s : enabledLore) this.enabledLore.add(ChatUtils.translate(s));
            this.disabledLore = new java.util.ArrayList<>();
            if (disabledLore != null) for (String s : disabledLore) this.disabledLore.add(ChatUtils.translate(s));
            this.enabled = defaultEnabled;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            XMaterial x = XMaterial.matchXMaterial(materialName).orElse(XMaterial.DIAMOND_SWORD);
            return new ItemBuilder(x).name(name).durability(durability).lore(enabled ? enabledLore : disabledLore).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            enabled = !enabled;
        }
    }

    private static class PartyManageButton extends Button {
        private final Team team;

        PartyManageButton(Team team) {
            this.team = team;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new java.util.ArrayList<>();
            if (PartyMenus.PARTY_SETTINGS_MENU_PARTY_MANAGE_LORE != null) {
                for (String s : PartyMenus.PARTY_SETTINGS_MENU_PARTY_MANAGE_LORE) lore.add(ChatUtils.translate(s));
            }
            lore.add(ChatUtils.translate("&7Members: &f" + (team != null ? team.getMembers().size() : 0)));
            return new ItemBuilder(XMaterial.PLAYER_HEAD)
                .name(ChatUtils.translate(PartyMenus.PARTY_SETTINGS_MENU_PARTY_MANAGE_NAME))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.sendMessage(ChatUtils.translate("&7Open party member list from the party menu."));
        }
    }
}
