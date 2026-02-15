package dev.artixdev.practice.configs.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ChildYamlStorage;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;

public class EventMenus extends ChildYamlStorage {
   public EventMenus(ParentYamlStorage parentStorage) {
      super(parentStorage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = EventMenus.class.getFields();
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
