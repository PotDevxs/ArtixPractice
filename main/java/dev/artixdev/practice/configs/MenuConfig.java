package dev.artixdev.practice.configs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.menu.bukkit.InternalMenuConfig;
import dev.artixdev.api.practice.storage.annotations.ConfigValue;
import dev.artixdev.api.practice.storage.impl.ParentYamlStorage;
import dev.artixdev.practice.configs.menus.DivisionMenus;
import dev.artixdev.practice.configs.menus.EventMenus;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.configs.menus.KitMenus;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.configs.menus.QueueMenus;
import dev.artixdev.practice.configs.menus.SpectateMenus;
import dev.artixdev.practice.configs.menus.StatsMenus;

public class MenuConfig extends ParentYamlStorage {
   public MenuConfig(JavaPlugin plugin, String name) {
      super(plugin, name);
   }

   public void addSeparateComments() {
      this.addComment("GLOBAL", "This is the global menu configuration section.\nThese configurations apply to all menus and are for the buttons that are built-in.");
   }

   public void registerChildStorages() {
      this.addChildStorage(new InternalMenuConfig(this));
      this.addChildStorage(new GeneralMenus(this));
      this.addChildStorage(new QueueMenus(this));
      this.addChildStorage(new PartyMenus(this));
      this.addChildStorage(new KitMenus(this));
      this.addChildStorage(new StatsMenus(this));
      this.addChildStorage(new MatchMenus(this));
      this.addChildStorage(new SpectateMenus(this));
      this.addChildStorage(new DivisionMenus(this));
      this.addChildStorage(new EventMenus(this));
   }

   public List<Field> getParentFields() {
      List<Field> annotatedFields = new ArrayList();
      Field[] var2 = this.getClass().getFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(ConfigValue.class)) {
            annotatedFields.add(field);
         }
      }

      return annotatedFields;
   }

   public String[] getHeader() {
      return new String[]{"This is the main menu configuration file for Bolt.", "The only menus that are configurable are one's accessible by players, not Admins.", "So menus like Arena manage or Kit manage are not configurable, since you won't need to.", "You need help with the configuration or have any questions related to Bolt?", "Join us in our Discord: https://dsc.gg/refine", null, "NOTE: All Material IDs are supported from 1.8x-1.20.x. Recommended: https://minecraftitemids.com/"};
   }
}
