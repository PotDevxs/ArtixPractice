package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.KitMenus;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Kit Edit Manage Menu
 * Menu for managing kit editing
 */
public class KitEditManageMenu extends Menu {
   
   private final Kit kit;
   
   /**
    * Constructor
    * @param kit the kit
    */
   public KitEditManageMenu(Kit kit) {
      this.setPlaceholder(true);
      this.kit = kit;
   }
   
   @Override
   public int getSize() {
      return 27;
   }
   
   @Override
   public String getTitle(Player player) {
      String title = KitMenus.KIT_EDIT_MANAGE_TITLE;
      String placeholder = "{kit}";
      return title.replace(placeholder, kit.getName());
   }
   
   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap<>();
      
      // Add kit management buttons
      for (int i = 0; i < 5; i++) {
         int slot = getSlot(2 * i, 1);
         Button button = createKitButton(player, i);
         
         if (button != null) {
            buttons.put(slot, button);
         } else {
            // Add placeholder button
            Material material = XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial();
            buttons.put(slot, Button.placeholder(material, (byte) 0, " "));
         }
      }
      
      int backSlot = getSlot(8, 2);
      buttons.put(backSlot, new Button() {
         @Override
         public ItemStack getButtonItem(Player p) {
            return new dev.artixdev.practice.utils.ItemBuilder(XMaterial.ARROW).name(ChatUtils.colorize("&cBack")).build();
         }
         @Override
         public void clicked(Player p, ClickType clickType) {
            p.closeInventory();
         }
      });
      return buttons;
   }

   private Button createKitButton(Player player, int index) {
      return null;
   }

   @Override
   public void onClose(Player player) {
      if (isClosedByMenu()) {
         return;
      }
      
      // Handle menu close
      Main plugin = Main.getInstance();
      dev.artixdev.practice.managers.PlayerManager playerManager = plugin.getPlayerManager();
      dev.artixdev.practice.models.PlayerProfile profile = playerManager.getPlayerProfile(player.getUniqueId());
      
      if (profile != null) {
         // Reset player state or perform cleanup
         profile.setState(dev.artixdev.practice.enums.PlayerState.LOBBY);
      }
   }
   
   /**
    * Get kit
    * @return kit
    */
   public Kit getKit() {
      return kit;
   }
}
