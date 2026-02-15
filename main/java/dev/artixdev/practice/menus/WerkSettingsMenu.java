package dev.artixdev.practice.menus;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Werk Settings Menu
 * Menu for Werk settings integration using WerkCore base
 */
public class WerkSettingsMenu {
   
   private static final String WERK_MENU_CLASS = "me.artix.core.menu.menu.WerkMenu";
   private static final String WERK_SLOT_CLASS = "me.artix.core.menu.slots.Slot";
   private static final String WERK_ITEM_BUILDER_CLASS = "me.artix.core.utilities.item.ItemBuilder";
   private static final String WERK_AMATERIAL_CLASS = "me.artix.core.enums.AMaterial";
   
   private Object werkMenuInstance;
   
   /**
    * Initialize WerkMenu using reflection
    */
   public WerkSettingsMenu() {
      try {
         Class<?> werkMenuClass = Class.forName(WERK_MENU_CLASS);
         Constructor<?> constructor = werkMenuClass.getDeclaredConstructor();
         constructor.setAccessible(true);
         this.werkMenuInstance = constructor.newInstance();
      } catch (Exception e) {
         // WerkCore not available, will use fallback implementation
         this.werkMenuInstance = null;
      }
   }
   
   /**
    * Get menu name
    * @param player the player
    * @return menu name
    */
   public String getName(Player player) {
      return "&7Werk Settings";
   }
   
   /**
    * Get slots for the menu
    * @param player the player
    * @return list of slots
    */
   public List<Object> getSlots(Player player) {
      List<Object> slots = new ArrayList<>();
      
      try {
         Class<?> slotClass = Class.forName(WERK_SLOT_CLASS);
         Method hasSlotMethod = slotClass.getMethod("hasSlot", List.class, int.class);
         Method getGlassMethod = slotClass.getMethod("getGlass", int.class);
         
         // Add Werk settings slot
         int slotNumber = GeneralMenus.SETTINGS_MENU_PHOENIX_SETTINGS_SLOT;
         slots.add(createWerkSettingsSlot(slotNumber));
         
         // Add glass slots for empty spaces
         for (int i = 0; i < 18; i++) {
            Boolean hasSlot = (Boolean) hasSlotMethod.invoke(null, slots, i);
            if (!hasSlot) {
               Object glassSlot = getGlassMethod.invoke(null, i);
               slots.add(glassSlot);
            }
         }
      } catch (Exception e) {
         // Fallback: create simple slot
         int slotNumber = GeneralMenus.SETTINGS_MENU_PHOENIX_SETTINGS_SLOT;
         slots.add(createFallbackSlot(slotNumber));
      }
      
      return slots;
   }
   
   /**
    * Create WerkSettingsSlot using reflection
    */
   private Object createWerkSettingsSlot(int slotNumber) {
      try {
         Class<?> slotClass = Class.forName(WERK_SLOT_CLASS);
         
         // Create anonymous Slot subclass using Proxy
         return java.lang.reflect.Proxy.newProxyInstance(
            slotClass.getClassLoader(),
            new Class[]{slotClass},
            (proxy, method, args) -> {
               if (method.getName().equals("getItem")) {
                  return createWerkItem();
               } else if (method.getName().equals("getSlot")) {
                  return slotNumber;
               } else if (method.getName().equals("onClick")) {
                  Player player = (Player) args[0];
                  player.sendMessage(ChatUtils.colorize("&aOpening Werk settings..."));
                  return null;
               }
               return null;
            }
         );
      } catch (Exception e) {
         // Fallback: create simple slot wrapper
         return createFallbackSlot(slotNumber);
      }
   }
   
   /**
    * Create Werk item using reflection
    */
   private ItemStack createWerkItem() {
      try {
         Class<?> itemBuilderClass = Class.forName(WERK_ITEM_BUILDER_CLASS);
         Class<?> aMaterialClass = Class.forName(WERK_AMATERIAL_CLASS);
         
         Object bookEnum = aMaterialClass.getField("BOOK").get(null);
         Method parseMaterialMethod = bookEnum.getClass().getMethod("parseMaterial");
         Material material = (Material) parseMaterialMethod.invoke(bookEnum);
         
         Constructor<?> itemBuilderConstructor = itemBuilderClass.getConstructor(Material.class);
         Object itemBuilder = itemBuilderConstructor.newInstance(material);
         
         Method setNameMethod = itemBuilderClass.getMethod("setName", String.class);
         Method addLoreLineMethod = itemBuilderClass.getMethod("addLoreLine", String.class);
         Method toItemStackMethod = itemBuilderClass.getMethod("toItemStack");
         
         setNameMethod.invoke(itemBuilder, "&6Werk Settings");
         addLoreLineMethod.invoke(itemBuilder, " ");
         addLoreLineMethod.invoke(itemBuilder, "&7Click to open Werk settings");
         addLoreLineMethod.invoke(itemBuilder, " ");
         
         return (ItemStack) toItemStackMethod.invoke(itemBuilder);
      } catch (Exception e) {
         // Fallback: create simple item
         return createFallbackItem();
      }
   }
   
   /**
    * Create fallback slot (when WerkCore is not available)
    * Returns a simple object that can be used as a slot placeholder
    */
   private Object createFallbackSlot(int slotNumber) {
      // Return a simple wrapper object
      return new SlotWrapper(slotNumber);
   }
   
   /**
    * Simple slot wrapper for fallback
    */
   private static class SlotWrapper {
      private final int slotNumber;
      
      public SlotWrapper(int slotNumber) {
         this.slotNumber = slotNumber;
      }
      
      public ItemStack getItem(Player player) {
         return createFallbackItemStatic();
      }
      
      public int getSlot() {
         return slotNumber;
      }
      
      public void onClick(Player player, int slot, ClickType clickType) {
         player.sendMessage(ChatUtils.colorize("&aOpening Werk settings..."));
      }
      
      private ItemStack createFallbackItemStatic() {
         ItemStack item = new ItemStack(Material.BOOK);
         ItemMeta meta = item.getItemMeta();
         if (meta != null) {
            meta.setDisplayName(ChatUtils.colorize("&6Werk Settings"));
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add(ChatUtils.colorize("&7Click to open Werk settings"));
            lore.add(" ");
            meta.setLore(lore);
            item.setItemMeta(meta);
         }
         return item;
      }
   }
   
   /**
    * Create fallback item
    */
   private ItemStack createFallbackItem() {
      ItemStack item = new ItemStack(Material.BOOK);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(ChatUtils.colorize("&6Werk Settings"));
         List<String> lore = new ArrayList<>();
         lore.add(" ");
         lore.add(ChatUtils.colorize("&7Click to open Werk settings"));
         lore.add(" ");
         meta.setLore(lore);
         item.setItemMeta(meta);
      }
      return item;
   }
   
   /**
    * Open menu for player
    * @param player the player
    */
   public void open(Player player) {
      try {
         if (werkMenuInstance != null) {
            Method openMethod = werkMenuInstance.getClass().getMethod("open", Player.class);
            openMethod.invoke(werkMenuInstance, player);
         } else {
            // Fallback: open simple inventory
            player.sendMessage(ChatUtils.colorize("&cWerkCore is not available. Please install WerkCore to use this menu."));
         }
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cError opening Werk settings menu: " + e.getMessage()));
      }
   }
}
