package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.libs.com.cryptomorin.xseries.SkullUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Button that displays a single piece of match information (participant, kit, arena, status).
 */
public class MatchInfoButton extends Button {

    public enum InfoType { OVERVIEW, PLAYER1, PLAYER2, KIT, ARENA, STATUS }

    private final Match match;
    private final InfoType infoType;

    public MatchInfoButton(Match match, InfoType infoType) {
        this.match = match;
        this.infoType = infoType;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        switch (infoType) {
            case OVERVIEW:
                return new ItemBuilder(XMaterial.PAPER)
                    .name(ChatUtils.translate("&6&lMatch Overview"))
                    .lore(lore(
                        "&7Duration: &f" + (match.getDuration() / 1000) + "s",
                        "&7Status: " + (match.isEnded() ? "&cEnded" : "&aIn progress"),
                        "&7Warmup: " + (match.isInWarmup() ? "&aYes" : "&7No")
                    ))
                    .build();
            case PLAYER1:
                return headItem(match.getPlayer1(), "&aPlayer 1");
            case PLAYER2:
                return headItem(match.getPlayer2(), "&cPlayer 2");
            case KIT:
                Kit kit = match.getKit();
                ItemStack icon = (kit != null && kit.getDisplayIcon() != null) ? kit.getDisplayIcon().clone() : XMaterial.DIAMOND_SWORD.parseItem();
                String kitName = kit != null ? kit.getName() : (match.getKitType() != null ? match.getKitType().getDisplayName() : "Unknown");
                return new ItemBuilder(icon)
                    .name(ChatUtils.translate("&eKit: &f" + kitName))
                    .lore(lore("&7Current kit for this match."))
                    .build();
            case ARENA:
                Arena arena = match.getArena();
                String arenaName = arena != null ? arena.getName() : "Unknown";
                XMaterial arenaMat = (arena != null && arena.getDisplayIcon() != null && arena.getDisplayIcon().getType() != null) ? XMaterial.matchXMaterial(arena.getDisplayIcon().getType()) : XMaterial.GRASS_BLOCK;
                return new ItemBuilder(arenaMat != null ? arenaMat : XMaterial.GRASS_BLOCK)
                    .name(ChatUtils.translate("&bArena: &f" + arenaName))
                    .lore(lore("&7Arena for this match."))
                    .build();
            case STATUS:
                return new ItemBuilder(XMaterial.CLOCK)
                    .name(ChatUtils.translate("&eMatch Status"))
                    .lore(lore(
                        match.isEnded() ? "&cEnded" : "&aIn progress",
                        "&7Time: &f" + (match.getDuration() / 1000) + "s"
                    ))
                    .build();
            default:
                return XMaterial.AIR.parseItem();
        }
    }

    private static ItemStack headItem(Player p, String title) {
        if (p == null) {
            return new ItemBuilder(XMaterial.BARRIER).name(ChatUtils.translate("&7" + title + ": &c—")).build();
        }
        ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtils.translate(title + " &f" + p.getName()));
            SkullUtils.applySkin(meta, p);
            skull.setItemMeta(meta);
        }
        return skull;
    }

    private static List<String> lore(String... lines) {
        List<String> list = new ArrayList<>();
        for (String s : lines) list.add(ChatUtils.translate(s));
        return list;
    }
}
