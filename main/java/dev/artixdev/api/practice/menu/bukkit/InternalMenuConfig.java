package dev.artixdev.api.practice.menu.bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class InternalMenuConfig extends ChildYamlStorage {
   @ConfigValue(
      path = "GLOBAL.PLACEHOLDER-BUTTON.NAME",
      priority = 0
   )
   public static String PLACEHOLDER_BUTTON_NAME = "";
   @ConfigValue(
      path = "GLOBAL.PLACEHOLDER-BUTTON.MATERIAL",
      priority = 1
   )
   public static String PLACEHOLDER_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.PLACEHOLDER-BUTTON.DURABILITY",
      priority = 2
   )
   public static int PLACEHOLDER_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.BORDER-BUTTON.NAME",
      priority = 0
   )
   public static String BORDER_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.BORDER-BUTTON.MATERIAL",
      priority = 1
   )
   public static String BORDER_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.BORDER-BUTTON.DURABILITY",
      priority = 2
   )
   public static int BORDER_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.NEXT-PAGE-BUTTON.NAME",
      priority = 3
   )
   public static String NEXT_PAGE_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.NEXT-PAGE-BUTTON.MATERIAL",
      priority = 4
   )
   public static String NEXT_PAGE_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.NEXT-PAGE-BUTTON.DURABILITY",
      priority = 5
   )
   public static int NEXT_PAGE_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.NEXT-PAGE-BUTTON.LORE",
      priority = 5
   )
   public static List<String> NEXT_PAGE_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.PREVIOUS-PAGE-BUTTON.NAME",
      priority = 6
   )
   public static String PREVIOUS_PAGE_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.PREVIOUS-PAGE-BUTTON.MATERIAL",
      priority = 7
   )
   public static String PREVIOUS_PAGE_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.PREVIOUS-PAGE-BUTTON.DURABILITY",
      priority = 8
   )
   public static int PREVIOUS_PAGE_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.PREVIOUS-PAGE-BUTTON.LORE",
      priority = 9
   )
   public static List<String> PREVIOUS_PAGE_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.BACK-BUTTON.NAME",
      priority = 10
   )
   public static String BACK_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.BACK-BUTTON.MATERIAL",
      priority = 11
   )
   public static String BACK_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.BACK-BUTTON.DURABILITY",
      priority = 12
   )
   public static int BACK_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.BACK-BUTTON.LORE",
      priority = 13
   )
   public static List<String> BACK_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.CONFIRMATION-BUTTON.NAME",
      priority = 14
   )
   public static String CONFIRMATION_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.CONFIRMATION-BUTTON.MATERIAL",
      priority = 15
   )
   public static String CONFIRMATION_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.CONFIRMATION-BUTTON.DURABILITY",
      priority = 16
   )
   public static int CONFIRMATION_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.CONFIRMATION-BUTTON.LORE",
      priority = 17
   )
   public static List<String> CONFIRMATION_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.CANCEL-BUTTON.NAME",
      priority = 18
   )
   public static String CANCEL_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.CANCEL-BUTTON.MATERIAL",
      priority = 19
   )
   public static String CANCEL_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.CANCEL-BUTTON.DURABILITY",
      priority = 20
   )
   public static int CANCEL_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.CANCEL-BUTTON.LORE",
      priority = 21
   )
   public static List<String> CANCEL_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.ENABLED.NAME",
      priority = 22
   )
   public static String BOOLEAN_TRAIT_ENABLED_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.ENABLED.MATERIAL",
      priority = 23
   )
   public static String BOOLEAN_TRAIT_ENABLED_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.ENABLED.DURABILITY",
      priority = 24
   )
   public static int BOOLEAN_TRAIT_ENABLED_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.ENABLED.LORE",
      priority = 25
   )
   public static List<String> BOOLEAN_TRAIT_ENABLED_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.DISABLED.NAME",
      priority = 26
   )
   public static String BOOLEAN_TRAIT_DISABLED_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.DISABLED.MATERIAL",
      priority = 27
   )
   public static String BOOLEAN_TRAIT_DISABLED_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.DISABLED.DURABILITY",
      priority = 28
   )
   public static int BOOLEAN_TRAIT_DISABLED_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.BOOLEAN-TRAIT-BUTTON.DISABLED.LORE",
      priority = 29
   )
   public static List<String> BOOLEAN_TRAIT_DISABLED_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.INTEGER-TRAIT-BUTTON.NAME",
      priority = 30
   )
   public static String INTEGER_TRAIT_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.INTEGER-TRAIT-BUTTON.MATERIAL",
      priority = 31
   )
   public static String INTEGER_TRAIT_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.INTEGER-TRAIT-BUTTON.DURABILITY",
      priority = 32
   )
   public static int INTEGER_TRAIT_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.INTEGER-TRAIT-BUTTON.LORE",
      priority = 33
   )
   public static List<String> INTEGER_TRAIT_BUTTON_LORE;
   @ConfigValue(
      path = "GLOBAL.DOUBLE-TRAIT-BUTTON.NAME",
      priority = 34
   )
   public static String DOUBLE_TRAIT_BUTTON_NAME;
   @ConfigValue(
      path = "GLOBAL.DOUBLE-TRAIT-BUTTON.MATERIAL",
      priority = 35
   )
   public static String DOUBLE_TRAIT_BUTTON_MATERIAL;
   @ConfigValue(
      path = "GLOBAL.DOUBLE-TRAIT-BUTTON.DURABILITY",
      priority = 36
   )
   public static int DOUBLE_TRAIT_BUTTON_DURABILITY;
   @ConfigValue(
      path = "GLOBAL.DOUBLE-TRAIT-BUTTON.LORE",
      priority = 37
   )
   public static List<String> DOUBLE_TRAIT_BUTTON_LORE;

   public InternalMenuConfig(ParentYamlStorage storage) {
      super(storage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = InternalMenuConfig.class.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
            ConfigValue configValue = (ConfigValue)field.getAnnotation(ConfigValue.class);
            if (configValue != null) {
               annotatedFields.add(field);
            }
         }
      }

      annotatedFields.sort(Comparator.comparingInt((fieldx) -> {
         return ((ConfigValue)fieldx.getAnnotation(ConfigValue.class)).priority();
      }));
      return annotatedFields;
   }

   static {
      PLACEHOLDER_BUTTON_MATERIAL = XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.name();
      PLACEHOLDER_BUTTON_DURABILITY = 0;
      BORDER_BUTTON_NAME = "";
      BORDER_BUTTON_MATERIAL = XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.name();
      BORDER_BUTTON_DURABILITY = 0;
      NEXT_PAGE_BUTTON_NAME = "&aNext Page &7(<page>/<pages>)";
      NEXT_PAGE_BUTTON_MATERIAL = XMaterial.PLAYER_HEAD.name();
      NEXT_PAGE_BUTTON_DURABILITY = 0;
      NEXT_PAGE_BUTTON_LORE = Arrays.asList("", "&7Right click to view all pages.");
      PREVIOUS_PAGE_BUTTON_NAME = "&cPrevious Page &7(<page>/<pages>)";
      PREVIOUS_PAGE_BUTTON_MATERIAL = XMaterial.PLAYER_HEAD.name();
      PREVIOUS_PAGE_BUTTON_DURABILITY = 0;
      PREVIOUS_PAGE_BUTTON_LORE = Arrays.asList("", "&7Right click to view all pages.");
      BACK_BUTTON_NAME = "&cBack";
      BACK_BUTTON_MATERIAL = "PLAYER_HEAD";
      BACK_BUTTON_DURABILITY = 0;
      BACK_BUTTON_LORE = Arrays.asList("", "&eClick here to return.");
      CONFIRMATION_BUTTON_NAME = "&aConfirm";
      CONFIRMATION_BUTTON_MATERIAL = "LIME_WOOL";
      CONFIRMATION_BUTTON_DURABILITY = 0;
      CONFIRMATION_BUTTON_LORE = Arrays.asList();
      CANCEL_BUTTON_NAME = "&cCancel";
      CANCEL_BUTTON_MATERIAL = "RED_WOOL";
      CANCEL_BUTTON_DURABILITY = 0;
      CANCEL_BUTTON_LORE = Arrays.asList();
      BOOLEAN_TRAIT_ENABLED_BUTTON_NAME = "&c&lEdit <trait>";
      BOOLEAN_TRAIT_ENABLED_BUTTON_MATERIAL = "LIME_WOOL";
      BOOLEAN_TRAIT_ENABLED_BUTTON_DURABILITY = 0;
      BOOLEAN_TRAIT_ENABLED_BUTTON_LORE = Arrays.asList(" &7* &fCurrent: &aOn", "", " &c[Click to toggle]");
      BOOLEAN_TRAIT_DISABLED_BUTTON_NAME = "&c&lEdit <trait>";
      BOOLEAN_TRAIT_DISABLED_BUTTON_MATERIAL = "RED_WOOL";
      BOOLEAN_TRAIT_DISABLED_BUTTON_DURABILITY = 0;
      BOOLEAN_TRAIT_DISABLED_BUTTON_LORE = Arrays.asList(" &7* &fCurrent: &cOff", "", " &c[Click to toggle]");
      INTEGER_TRAIT_BUTTON_NAME = "&c&lEdit <trait>";
      INTEGER_TRAIT_BUTTON_MATERIAL = XMaterial.GHAST_TEAR.name();
      INTEGER_TRAIT_BUTTON_DURABILITY = 0;
      INTEGER_TRAIT_BUTTON_LORE = Arrays.asList(" &7* &fCurrent: &c<value>", "", " &7Left-Click - To increase by 1", " &7Right-Click - To decrease by 1", "", " &7Shit Left-Click - To increase by 10", " &7Shift Right-Click - To decrease by 10", "", " &c[Click to edit]");
      DOUBLE_TRAIT_BUTTON_NAME = "&c&lEdit <trait>";
      DOUBLE_TRAIT_BUTTON_MATERIAL = XMaterial.GHAST_TEAR.name();
      DOUBLE_TRAIT_BUTTON_DURABILITY = 0;
      DOUBLE_TRAIT_BUTTON_LORE = Arrays.asList(" &7* &fCurrent: &c<value>", "", " &7Left-Click - To increase by 0.1", " &7Right-Click - To decrease by 0.1", "", " &7Shit Left-Click - To increase by 0.5", " &7Shift Right-Click - To decrease by 0.5", "", " &c[Click to edit]");
   }
}
