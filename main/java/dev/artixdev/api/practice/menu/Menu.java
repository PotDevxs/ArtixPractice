package dev.artixdev.api.practice.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.bukkit.InternalMenuConfig;
import dev.artixdev.api.practice.menu.util.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public abstract class Menu {
   private Map<Integer, Button> buttons = new HashMap();
   private boolean autoUpdate = false;
   private boolean updateAfterClick = true;
   private boolean closedByMenu = false;
   private boolean placeholder = false;
   private boolean bordered = false;
   private Button placeholderButton;
   private Button borderButton;

   public Menu() {
      this.placeholderButton = Button.placeholder((new ItemBuilder(ItemBuilder.parseFromX(InternalMenuConfig.PLACEHOLDER_BUTTON_MATERIAL, XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE))).durability(InternalMenuConfig.PLACEHOLDER_BUTTON_DURABILITY).name(InternalMenuConfig.PLACEHOLDER_BUTTON_NAME).build());
      this.borderButton = Button.placeholder((new ItemBuilder(ItemBuilder.parseFromX(InternalMenuConfig.BORDER_BUTTON_MATERIAL, XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE))).durability(InternalMenuConfig.BORDER_BUTTON_DURABILITY).name(InternalMenuConfig.BORDER_BUTTON_NAME).build());
   }

   public int size(Map<Integer, Button> buttons) {
      int highest = 0;
      Iterator var3 = buttons.keySet().iterator();

      while(var3.hasNext()) {
         int buttonValue = (Integer)var3.next();
         if (buttonValue > highest) {
            highest = buttonValue;
         }
      }

      return (int)(Math.ceil((double)(highest + 1) / 9.0D) * 9.0D);
   }

   public int getSize() {
      return -1;
   }

   public int getSlot(int x, int y) {
      return 9 * y + x;
   }

   public abstract String getTitle(Player var1);

   public abstract Map<Integer, Button> getButtons(Player var1);

   public void onOpen(Player player) {
   }

   public void onClose(Player player) {
   }

   public Map<Integer, Button> getButtons() {
      return this.buttons;
   }

   public boolean isAutoUpdate() {
      return this.autoUpdate;
   }

   public boolean isUpdateAfterClick() {
      return this.updateAfterClick;
   }

   public boolean isClosedByMenu() {
      return this.closedByMenu;
   }

   public boolean isPlaceholder() {
      return this.placeholder;
   }

   public boolean isBordered() {
      return this.bordered;
   }

   public Button getPlaceholderButton() {
      return this.placeholderButton;
   }

   public Button getBorderButton() {
      return this.borderButton;
   }

   public void setButtons(Map<Integer, Button> buttons) {
      this.buttons = buttons;
   }

   public void setAutoUpdate(boolean autoUpdate) {
      this.autoUpdate = autoUpdate;
   }

   public void setUpdateAfterClick(boolean updateAfterClick) {
      this.updateAfterClick = updateAfterClick;
   }

   public void setClosedByMenu(boolean closedByMenu) {
      this.closedByMenu = closedByMenu;
   }

   public void setPlaceholder(boolean placeholder) {
      this.placeholder = placeholder;
   }

   public void setBordered(boolean bordered) {
      this.bordered = bordered;
   }

   public void setPlaceholderButton(Button placeholderButton) {
      this.placeholderButton = placeholderButton;
   }

   public void setBorderButton(Button borderButton) {
      this.borderButton = borderButton;
   }
}
