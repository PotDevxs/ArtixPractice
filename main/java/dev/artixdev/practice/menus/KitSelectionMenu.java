package dev.artixdev.practice.menus;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.utils.other.Callback;
import dev.artixdev.practice.utils.ChatUtils;

public class KitSelectionMenu extends PaginatedMenu {
    
    private final boolean showDisabled;
    private final boolean closeOnSelect;
    private final int maxKitsPerPage;
    private final Callback<Kit> kitCallback;
    private final List<String> kitNames;
    private final Predicate<Kit> kitFilter;

    public KitSelectionMenu(Callback<Kit> kitCallback, List<String> kitNames, int maxKitsPerPage, boolean showDisabled) {
        this(kitCallback, kitNames, maxKitsPerPage, showDisabled, false);
    }

    public KitSelectionMenu(Callback<Kit> kitCallback, List<String> kitNames, int maxKitsPerPage) {
        this(kitCallback, kitNames, maxKitsPerPage, false, false);
    }

    public KitSelectionMenu(Callback<Kit> kitCallback, Predicate<Kit> kitFilter, boolean showDisabled) {
        this(kitCallback, null, 1, false, showDisabled, kitFilter);
    }

    public KitSelectionMenu(Callback<Kit> kitCallback, Predicate<Kit> kitFilter) {
        this(kitCallback, null, 1, false, false, kitFilter);
    }

    public KitSelectionMenu(Callback<Kit> kitCallback) {
        this(kitCallback, null, 1, false, false);
    }

    public KitSelectionMenu(Callback<Kit> kitCallback, List<String> kitNames) {
        this(kitCallback, kitNames, 1, false, false);
    }

    public KitSelectionMenu(Callback<Kit> kitCallback, List<String> kitNames, int maxKitsPerPage, boolean showDisabled, boolean closeOnSelect) {
        this(kitCallback, kitNames, maxKitsPerPage, showDisabled, closeOnSelect, null);
    }

    public KitSelectionMenu(Callback<Kit> kitCallback, List<String> kitNames, int maxKitsPerPage, boolean showDisabled, boolean closeOnSelect, Predicate<Kit> kitFilter) {
        this.setBordered(true);
        this.kitCallback = Preconditions.checkNotNull(kitCallback, "Kit callback cannot be null");
        this.kitNames = kitNames;
        this.showDisabled = showDisabled;
        this.maxKitsPerPage = maxKitsPerPage;
        this.closeOnSelect = closeOnSelect;
        this.kitFilter = kitFilter;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatUtils.translate("&8Select Kit");
    }

    @Override
    public int getSize() {
        return GeneralMenus.SELECT_KIT_SIZE;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        // Get kit manager and available kits
        // This would need to be injected or accessed through a service locator
        // For now, using placeholder logic
        List<Kit> availableKits = new ArrayList<>(); // Get from KitManager
        
        int slot = 0;
        for (Kit kit : availableKits) {
            if (kit == null) continue;
            
            // Check permissions
            boolean hasPermission = player.hasPermission("practice.kit." + kit.getName().toLowerCase());
            
            if (!hasPermission && !showDisabled) {
                if (!kit.isEnabled()) {
                    continue;
                }
            }
            
            // Apply filter if present
            if (kitFilter != null && !kitFilter.test(kit)) {
                continue;
            }
            
            buttons.put(slot++, new KitButton(kit, kitCallback, kitNames, maxKitsPerPage));
        }
        
        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        // Add global buttons like back button, etc.
        return buttons;
    }

    @Override
    public void onClose(Player player) {
        if (!closeOnSelect) {
            // Handle close logic
        } else {
            player.sendMessage(ChatUtils.colorize("&cKit selection cancelled."));
        }
    }

    private class KitButton extends Button {
        
        private final Kit kit;
        private final Callback<Kit> kitCallback;
        private final List<String> kitNames;
        private final int maxKitsPerPage;

        public KitButton(Kit kit, Callback<Kit> kitCallback, List<String> kitNames, int maxKitsPerPage) {
            this.kit = kit;
            this.kitCallback = kitCallback;
            this.kitNames = kitNames;
            this.maxKitsPerPage = maxKitsPerPage;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            // Create item stack based on kit
            return kit.getIcon(); // Assuming Kit has getIcon() method
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            List<Kit> availableKits = new ArrayList<>(); // Get from KitManager
            List<Kit> filteredKits = new ArrayList<>();
            
            for (Kit availableKit : availableKits) {
                if (availableKit == null) continue;
                
                boolean hasPermission = player.hasPermission("practice.kit." + availableKit.getName().toLowerCase());
                
                if (!hasPermission && !showDisabled) {
                    if (!availableKit.isEnabled()) {
                        continue;
                    }
                }
                
                if (kitFilter != null && !kitFilter.test(availableKit)) {
                    continue;
                }
                
                filteredKits.add(availableKit);
            }
            
            if (filteredKits.isEmpty()) {
                return;
            }
            
            Kit selectedKit;
            if (filteredKits.size() == 1) {
                selectedKit = filteredKits.get(0);
            } else {
                Random random = new Random();
                int randomIndex = Math.max(1, random.nextInt(filteredKits.size()));
                selectedKit = filteredKits.get(randomIndex);
            }
            
            kitCallback.call(selectedKit);
        }
    }
}
