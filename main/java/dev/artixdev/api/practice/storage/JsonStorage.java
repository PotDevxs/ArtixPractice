package dev.artixdev.api.practice.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.libs.com.google.gson.Gson;

public class JsonStorage<T> {
   private static final Logger LOGGER = LogManager.getLogger(JsonStorage.class);
   private final String name;
   private final File file;
   private final Gson gson;

   public JsonStorage(String name, JavaPlugin plugin, Gson gson) {
      this(name, new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data"), gson);
   }

   public JsonStorage(String name, File directory, Gson gson) {
      boolean created;
      if (!directory.exists()) {
         created = directory.mkdir();
         if (!created) {
            LOGGER.info("[Storage] Couldn't create " + name + "'s storage");
         }
      }

      this.file = new File(directory, name + ".json");
      this.gson = gson;
      this.name = name;
      if (!this.file.exists()) {
         try {
            created = this.file.createNewFile();
            if (!created) {
               LOGGER.info("[Storage] Couldn't create " + name + "'s storage");
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }

   public T getData(Type type) {
      try {
         FileReader reader = new FileReader(this.file);
         Throwable var3 = null;

         Object var4;
         try {
            var4 = this.gson.fromJson((Reader)reader, (Type)type);
         } catch (Throwable e) {
            var3 = e;
            throw e;
         } finally {
            if (reader != null) {
               if (var3 != null) {
                  try {
                     reader.close();
                  } catch (Throwable suppressed) {
                     var3.addSuppressed(suppressed);
                  }
               } else {
                  reader.close();
               }
            }

         }

         @SuppressWarnings("unchecked")
         T result = (T) var4;
         return result;
      } catch (IOException e) {
         LOGGER.info("[Storage] Unable to load JSON Storage for " + this.name + ", check for syntax errors!");
         e.printStackTrace();
         return null;
      }
   }

   public void saveAsync(T list) {
      CompletableFuture.runAsync(() -> {
         this.save(list);
      });
   }

   public void save(T list) {
      try {
         FileWriter fileWriter = new FileWriter(this.file);
         Throwable var3 = null;

         try {
            this.gson.toJson((Object)list, (Appendable)fileWriter);
         } catch (Throwable e) {
            var3 = e;
            throw e;
         } finally {
            if (fileWriter != null) {
               if (var3 != null) {
                  try {
                     fileWriter.close();
                  } catch (Throwable suppressed) {
                     var3.addSuppressed(suppressed);
                  }
               } else {
                  fileWriter.close();
               }
            }

         }
      } catch (IOException e) {
         e.printStackTrace();
      }

   }
}
