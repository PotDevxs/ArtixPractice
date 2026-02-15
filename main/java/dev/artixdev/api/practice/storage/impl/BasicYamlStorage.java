package dev.artixdev.api.practice.storage.impl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.YamlStorage;

public class BasicYamlStorage extends YamlStorage {
   public BasicYamlStorage(JavaPlugin plugin, String name, boolean saveResource) {
      super(plugin, name, saveResource);
   }

   public void addSeparateComments() {
   }

   public List<Field> getConfigFields() {
      return Collections.emptyList();
   }

   public String[] getHeader() {
      return new String[]{"This configuration file is part of a Refine Development Project. Purchased at https://dsc.gg/refine"};
   }
}
