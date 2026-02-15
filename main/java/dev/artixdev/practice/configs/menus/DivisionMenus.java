package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class DivisionMenus extends ChildYamlStorage {
   @ConfigValue(
      priority = 1,
      path = "DIVISION-MENUS.TITLE"
   )
   public static String DIVISION_TITLE = "Divisions";
   @ConfigValue(
      priority = 2,
      path = "DIVISION-MENUS.NAME"
   )
   public static String DIVISION_NAME = "<division> <current>";
   @ConfigValue(
      priority = 3,
      path = "DIVISION-MENUS.CURRENT"
   )
   public static String DIVISION_CURRENT = " &a(Current)";
   @ConfigValue(
      priority = 4,
      path = "DIVISION-MENUS.LORE"
   )
   public static List<String> DIVISION_LORE = Arrays.asList("", "&fRequired Elo: &c<elo>");
   @ConfigValue(
      priority = 5,
      path = "DIVISION-MENUS.LORE-CURRENT"
   )
   public static List<String> DIVISION_LORE_CURRENT = Arrays.asList("", "&fRequired Elo: &c<elo>", "", "&a&oThis is your current division.");

   public DivisionMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = DivisionMenus.class.getFields();
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
