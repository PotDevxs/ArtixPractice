package dev.artixdev.practice.menus;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Practice;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.other.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class ArenaSelectionMenu extends PaginatedMenu {
    
    private final Callback<Arena> callback;
    private final boolean showRandom;
    private final Predicate<Arena> filter;

    public ArenaSelectionMenu(Callback<Arena> callback, boolean showRandom, Predicate<Arena> filter) {
        this.callback = Preconditions.checkNotNull(callback, "Callback cannot be null");
        this.showRandom = showRandom;
        this.filter = filter;
        this.setBordered(true);
    }

    public ArenaSelectionMenu(Callback<Arena> callback) {
        this(callback, true, null);
    }

    public ArenaSelectionMenu(Callback<Arena> callback, Predicate<Arena> filter) {
        this(callback, true, filter);
    }

    public ArenaSelectionMenu(Callback<Arena> callback, Predicate<Arena> filter, boolean showRandom) {
        this(callback, showRandom, filter);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return GeneralMenus.SELECT_ARENA_TITLE;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        List<Arena> arenas = new ArrayList<>();
        Practice.getPlugin().getArenaManager().getArenas().values().forEach(arena -> {
            if (arena.isEnabled()) {
                if (filter == null || filter.test(arena)) {
                    arenas.add(arena);
                }
            }
        });

        int slot = 0;
        for (Arena arena : arenas) {
            buttons.put(slot++, new ArenaButton(arena, callback));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        if (showRandom) {
            buttons.put(4, new RandomArenaButton(callback, filter));
        }
        
        return buttons;
    }

    @Override
    public int getSize() {
        return GeneralMenus.SELECT_ARENA_SIZE;
    }

    private class ArenaButton extends Button {
        private final Arena arena;
        private final Callback<Arena> callback;

        public ArenaButton(Arena arena, Callback<Arena> callback) {
            this.arena = arena;
            this.callback = callback;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder itemBuilder = new ItemBuilder(arena.getDisplayIcon());
            itemBuilder.name(ChatUtils.translate("&a" + arena.getName()));
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatUtils.translate("&7Tipo: &f" + arena.getType().toString()));
            lore.add(ChatUtils.translate("&7Status: " + (arena.isEnabled() ? "&aAtivado" : "&cDesativado")));
            lore.add(ChatUtils.translate("&7Kits: &f" + arena.getKits().size()));
            lore.add("");
            lore.add(ChatUtils.translate("&eClique para selecionar esta arena."));
            
            itemBuilder.lore(lore);
            return itemBuilder.build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            callback.call(arena);
            player.closeInventory();
            playSuccess(player);
        }
    }

    private class RandomArenaButton extends Button {
        private final Callback<Arena> callback;
        private final Predicate<Arena> filter;

        public RandomArenaButton(Callback<Arena> callback, Predicate<Arena> filter) {
            this.callback = callback;
            this.filter = filter;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder itemBuilder = new ItemBuilder(XMaterial.ENDER_EYE.parseMaterial());
            itemBuilder.name(ChatUtils.translate(GeneralMenus.SELECT_ARENA_RANDOM_NAME));
            itemBuilder.lore(GeneralMenus.SELECT_ARENA_RANDOM_LORE);
            itemBuilder.durability((short) GeneralMenus.SELECT_ARENA_RANDOM_DURABILITY);
            return itemBuilder.build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            List<Arena> availableArenas = new ArrayList<>();
            
            Practice.getPlugin().getArenaManager().getArenas().values().forEach(arena -> {
                if (arena.isEnabled()) {
                    if (filter == null || filter.test(arena)) {
                        availableArenas.add(arena);
                    }
                }
            });

            if (availableArenas.isEmpty()) {
                player.sendMessage(ChatUtils.translate("&cNenhuma arena disponível encontrada."));
                return;
            }

            if (availableArenas.size() == 1) {
                callback.call(availableArenas.get(0));
            } else {
                Random random = new Random();
                int randomIndex = Math.max(0, random.nextInt(availableArenas.size()));
                callback.call(availableArenas.get(randomIndex));
            }
            
            player.closeInventory();
            playSuccess(player);
        }
    }
}
