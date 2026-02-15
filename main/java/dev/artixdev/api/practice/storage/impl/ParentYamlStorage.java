package dev.artixdev.api.practice.storage.impl;

import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.YamlStorage;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;

public abstract class ParentYamlStorage extends YamlStorage {
   private List<ChildYamlStorage> childStorages;

   public ParentYamlStorage(JavaPlugin plugin, String name) {
      super(plugin, name, false);
   }

   public void addChildStorage(ChildYamlStorage storage) {
      Preconditions.checkNotNull(storage, "[StorageAPI] Child Storage can not be null!");
      if (this.childStorages == null) {
         this.childStorages = new ArrayList();
      }

      this.childStorages.add(storage);
   }

   public List<Field> getConfigFields() {
      List<Field> annotatedFields = new ArrayList(this.getParentFields());
      this.childStorages.stream().map(ChildYamlStorage::getConfigFields).forEach(annotatedFields::addAll);
      Preconditions.checkArgument(annotatedFields.stream().allMatch((field) -> {
         return field.isAnnotationPresent(ConfigValue.class);
      }), "[Storage-API] One of your field is missing annotation!");
      annotatedFields.sort(Comparator.comparingInt((field) -> {
         return ((ConfigValue)field.getAnnotation(ConfigValue.class)).priority();
      }));
      return annotatedFields;
   }

   public abstract List<Field> getParentFields();

   public abstract String[] getHeader();

   public abstract void addSeparateComments();

   public abstract void registerChildStorages();
}
