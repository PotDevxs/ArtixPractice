package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class KitMenus extends ChildYamlStorage {
   @ConfigValue(
      priority = 47,
      path = "KIT-MENUS.KIT-EDITOR.TITLE"
   )
   public static String KIT_EDITOR_TITLE = "&7Editing <inventory_custom_name>";
   @ConfigValue(
      priority = 48,
      path = "KIT-MENUS.KIT-EDITOR.CANCEL-BUTTON.NAME"
   )
   public static String KIT_EDITOR_CANCEL_EDIT_NAME = "&cCancel";
   @ConfigValue(
      priority = 49,
      path = "KIT-MENUS.KIT-EDITOR.CANCEL-BUTTON.MATERIAL"
   )
   public static String KIT_EDITOR_CANCEL_EDIT_MATERIAL = "REDSTONE";
   @ConfigValue(
      priority = 50,
      path = "KIT-MENUS.KIT-EDITOR.CANCEL-BUTTON.DURABILITY"
   )
   public static int KIT_EDITOR_CANCEL_EDIT_DURABILITY = 0;
   @ConfigValue(
      priority = 51,
      path = "KIT-MENUS.KIT-EDITOR.CANCEL-BUTTON.LORE"
   )
   public static List<String> KIT_EDITOR_CANCEL_EDIT_LORE = Arrays.asList("", " &7Click this to abort editing your kit,", " &7and return to the kit menu.", "", " &c[Click to cancel]");
   @ConfigValue(
      priority = 52,
      path = "KIT-MENUS.KIT-EDITOR.CLEAR-INVENTORY-BUTTON.NAME"
   )
   public static String KIT_EDITOR_CLEAR_INVENTORY_NAME = "&cClear Inventory";
   @ConfigValue(
      priority = 53,
      path = "KIT-MENUS.KIT-EDITOR.CLEAR-INVENTORY-BUTTON.MATERIAL"
   )
   public static String KIT_EDITOR_CLEAR_INVENTORY_MATERIAL = "RED_DYE";
   @ConfigValue(
      priority = 53,
      path = "KIT-MENUS.KIT-EDITOR.CLEAR-INVENTORY-BUTTON.DURABILITY"
   )
   public static int KIT_EDITOR_CLEAR_INVENTORY_DURABILITY = 0;
   @ConfigValue(
      priority = 54,
      path = "KIT-MENUS.KIT-EDITOR.CLEAR-INVENTORY-BUTTON.LORE"
   )
   public static List<String> KIT_EDITOR_CLEAR_INVENTORY_LORE = Arrays.asList("", " &7This will clear your inventory", " &7so you can start over.", "", " &c[Click to clear]");
   @ConfigValue(
      priority = 55,
      path = "KIT-MENUS.KIT-EDITOR.RENAME-BUTTON.NAME"
   )
   public static String KIT_EDITOR_RENAME_KIT_NAME = "&cRename";
   @ConfigValue(
      priority = 56,
      path = "KIT-MENUS.KIT-EDITOR.RENAME-BUTTON.MATERIAL"
   )
   public static String KIT_EDITOR_RENAME_KIT_MATERIAL = "NAME_TAG";
   @ConfigValue(
      priority = 57,
      path = "KIT-MENUS.KIT-EDITOR.RENAME-BUTTON.DURABILITY"
   )
   public static int KIT_EDITOR_RENAME_KIT_DURABILITY = 0;
   @ConfigValue(
      priority = 58,
      path = "KIT-MENUS.KIT-EDITOR.RENAME-BUTTON.LORE"
   )
   public static List<String> KIT_EDITOR_RENAME_KIT_LORE = Arrays.asList("", " &7Click here to start a prompt", " &7in chat to rename this kit.", " &7Type &fCANCEL &7to cancel the prompt.", "", " &c[Click to rename]");
   @ConfigValue(
      priority = 59,
      path = "KIT-MENUS.KIT-EDITOR.SAVE-BUTTON.NAME"
   )
   public static String KIT_EDITOR_SAVE_KIT_NAME = "&cSave";
   @ConfigValue(
      priority = 60,
      path = "KIT-MENUS.KIT-EDITOR.SAVE-BUTTON.MATERIAL"
   )
   public static String KIT_EDITOR_SAVE_KIT_MATERIAL = "LIME_DYE";
   @ConfigValue(
      priority = 61,
      path = "KIT-MENUS.KIT-EDITOR.SAVE-BUTTON.DURABILITY"
   )
   public static int KIT_EDITOR_SAVE_KIT_DURABILITY = 0;
   @ConfigValue(
      priority = 62,
      path = "KIT-MENUS.KIT-EDITOR.SAVE-BUTTON.LORE"
   )
   public static List<String> KIT_EDITOR_SAVE_KIT_LORE = Arrays.asList("", " &7Click here to save all your", " &7kit inventories to our database.", "", " &c[Click to save]");
   @ConfigValue(
      priority = 63,
      path = "KIT-MENUS.KIT-EDITOR.LOAD-DEFAULT-BUTTON.NAME"
   )
   public static String KIT_EDITOR_LOAD_DEFAULT_KIT_NAME = "&cLoad default kit";
   @ConfigValue(
      priority = 64,
      path = "KIT-MENUS.KIT-EDITOR.LOAD-DEFAULT-BUTTON.MATERIAL"
   )
   public static String KIT_EDITOR_LOAD_DEFAULT_KIT_MATERIAL = "CYAN_DYE";
   @ConfigValue(
      priority = 65,
      path = "KIT-MENUS.KIT-EDITOR.LOAD-DEFAULT-BUTTON.DURABILITY"
   )
   public static int KIT_EDITOR_LOAD_DEFAULT_KIT_DURABILITY = 0;
   @ConfigValue(
      priority = 66,
      path = "KIT-MENUS.KIT-EDITOR.LOAD-DEFAULT-BUTTON.LORE"
   )
   public static List<String> KIT_EDITOR_LOAD_DEFAULT_KIT_LORE = Arrays.asList("", " &7Click this to load the default kit", " &7into the kit editing menu.", "", " &c[Click to reset]");
   @ConfigValue(
      priority = 67,
      path = "KIT-MENUS.KIT-EDITOR.EFFECTS-BUTTON.NAME"
   )
   public static String KIT_EDITOR_EFFECTS_KIT_NAME = "&cPotion Effects";
   @ConfigValue(
      priority = 68,
      path = "KIT-MENUS.KIT-EDITOR.EFFECTS-BUTTON.MATERIAL"
   )
   public static String KIT_EDITOR_EFFECTS_KIT_MATERIAL = "BREWING_STAND";
   @ConfigValue(
      priority = 69,
      path = "KIT-MENUS.KIT-EDITOR.EFFECTS-BUTTON.DURABILITY"
   )
   public static int KIT_EDITOR_EFFECTS_KIT_DURABILITY = 0;
   @ConfigValue(
      priority = 70,
      path = "KIT-MENUS.KIT-EDITOR.EFFECTS-BUTTON.EFFECTS-FORMAT"
   )
   public static String KIT_EDITOR_EFFECTS_KIT_FORMAT = " &c• &f<effect_name> &7(<effect_duration>)";
   @ConfigValue(
      priority = 71,
      path = "KIT-MENUS.KIT-EDITOR.EFFECTS-BUTTON.EFFECTS-LORE"
   )
   public static List<String> KIT_EDITOR_EFFECTS_KIT_EFFECTS_LORE = Arrays.asList("", "<effects>", "", "&eThese are automatically applied.");
   @ConfigValue(
      priority = 72,
      path = "KIT-MENUS.KIT-EDITOR.EFFECTS-BUTTON.NO-EFFECTS-LORE"
   )
   public static List<String> KIT_EDITOR_EFFECTS_KIT_NO_EFFECTS_LORE = Arrays.asList("", "&7No potion effects.");
   @ConfigValue(
      priority = 73,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.TITLE"
   )
   public static String KIT_EDIT_MANAGE_TITLE = "&cViewing <kit_name> kits";
   @ConfigValue(
      priority = 74,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.CREATE-BUTTON.NAME"
   )
   public static String KIT_EDIT_MANAGE_CREATE_KIT_NAME = "&aCreate an Inventory";
   @ConfigValue(
      priority = 75,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.CREATE-BUTTON.MATERIAL"
   )
   public static String KIT_EDIT_MANAGE_CREATE_KIT_MATERIAL = "BOOK";
   @ConfigValue(
      priority = 76,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.CREATE-BUTTON.DURABILITY"
   )
   public static int KIT_EDIT_MANAGE_CREATE_KIT_DURABILITY = 0;
   @ConfigValue(
      priority = 77,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.CREATE-BUTTON.LORE"
   )
   public static List<String> KIT_EDIT_MANAGE_CREATE_KIT_LORE = Collections.emptyList();
   @ConfigValue(
      priority = 78,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.DELETE-BUTTON.NAME"
   )
   public static String KIT_EDIT_MANAGE_DELETE_KIT_NAME = "&c&lDelete";
   @ConfigValue(
      priority = 79,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.DELETE-BUTTON.MATERIAL"
   )
   public static String KIT_EDIT_MANAGE_DELETE_KIT_MATERIAL = "REDSTONE_BLOCK";
   @ConfigValue(
      priority = 80,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.DELETE-BUTTON.DURABILITY"
   )
   public static int KIT_EDIT_MANAGE_DELETE_KIT_DURABILITY = 0;
   @ConfigValue(
      priority = 81,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.DELETE-BUTTON.LORE"
   )
   public static List<String> KIT_EDIT_MANAGE_DELETE_KIT_LORE = Arrays.asList("", " &7Click here to delete this kit", " &7You will &fNOT &7be able to", " &7recover this inventory.", "", " &c[Click to delete]");
   @ConfigValue(
      priority = 82,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.EDIT-BUTTON.NAME"
   )
   public static String KIT_EDIT_MANAGE_EDIT_KIT_NAME = "&a&lEdit <kit_name> #<inventory_slot>";
   @ConfigValue(
      priority = 83,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.EDIT-BUTTON.MATERIAL"
   )
   public static String KIT_EDIT_MANAGE_EDIT_KIT_MATERIAL = "ENCHANTED_BOOK";
   @ConfigValue(
      priority = 84,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.EDIT-BUTTON.DURABILITY"
   )
   public static int KIT_EDIT_MANAGE_EDIT_KIT_DURABILITY = 0;
   @ConfigValue(
      priority = 85,
      path = "KIT-MENUS.KIT-EDIT-MANAGE.EDIT-BUTTON.LORE"
   )
   public static List<String> KIT_EDIT_MANAGE_EDIT_KIT_LORE = Arrays.asList("", " &eName: &f<custom_name>", " &aHeals: &f<heal_count>", " &cDebuffs: &f<debuff_count>", "", " &c[Click to edit]");

   public KitMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = KitMenus.class.getFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(ConfigValue.class)) {
            annotatedFields.add(field);
         }
      }

      return annotatedFields;
   }
}
