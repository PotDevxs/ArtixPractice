package dev.artixdev.libs.org.simpleyaml.configuration.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import dev.artixdev.libs.org.simpleyaml.utils.Validate;

public class ConfigurationSerialization {
   public static final String SERIALIZED_TYPE_KEY = "==";
   private static final Map<String, Class<? extends ConfigurationSerializable>> aliases = new HashMap();
   private final Class<? extends ConfigurationSerializable> clazz;

   protected ConfigurationSerialization(Class<? extends ConfigurationSerializable> clazz) {
      this.clazz = clazz;
   }

   public static ConfigurationSerializable deserializeObject(Map<String, ?> args, Class<? extends ConfigurationSerializable> clazz) {
      return (new ConfigurationSerialization(clazz)).deserialize(args);
   }

   public static ConfigurationSerializable deserializeObject(Map<String, ?> args) {
      if (args.containsKey("==")) {
         Class clazz;
         try {
            String alias = (String)args.get("==");
            if (alias == null) {
               throw new IllegalArgumentException("Cannot have null alias");
            }

            clazz = getClassByAlias(alias);
            if (clazz == null) {
               throw new IllegalArgumentException("Specified class does not exist ('" + alias + "')");
            }
         } catch (ClassCastException e) {
            e.fillInStackTrace();
            throw e;
         }

         return (new ConfigurationSerialization(clazz)).deserialize(args);
      } else {
         throw new IllegalArgumentException("Args doesn't contain type key ('==')");
      }
   }

   public static void registerClass(Class<? extends ConfigurationSerializable> clazz) {
      DelegateDeserialization delegate = (DelegateDeserialization)clazz.getAnnotation(DelegateDeserialization.class);
      if (delegate == null) {
         registerClass(clazz, getAlias(clazz));
         registerClass(clazz, clazz.getName());
      }

   }

   public static void registerClass(Class<? extends ConfigurationSerializable> clazz, String alias) {
      aliases.put(alias, clazz);
   }

   public static void unregisterClass(String alias) {
      aliases.remove(alias);
   }

   public static void unregisterClass(Class<? extends ConfigurationSerializable> clazz) {
      while(aliases.values().remove(clazz)) {
      }

   }

   public static Class<? extends ConfigurationSerializable> getClassByAlias(String alias) {
      return (Class)aliases.get(alias);
   }

   public static String getAlias(Class<? extends ConfigurationSerializable> clazz) {
      DelegateDeserialization delegate = (DelegateDeserialization)clazz.getAnnotation(DelegateDeserialization.class);
      if (delegate != null && delegate.value() != clazz) {
         return getAlias(delegate.value());
      } else {
         SerializableAs alias = (SerializableAs)clazz.getAnnotation(SerializableAs.class);
         return alias != null ? alias.value() : clazz.getName();
      }
   }

   public ConfigurationSerializable deserialize(Map<String, ?> args) {
      Validate.notNull(args, "Args must not be null");
      ConfigurationSerializable result = null;
      Method method = this.getMethod("deserialize", true);
      if (method != null) {
         result = this.deserializeViaMethod(method, args);
      }

      if (result == null) {
         method = this.getMethod("valueOf", true);
         if (method != null) {
            result = this.deserializeViaMethod(method, args);
         }
      }

      if (result == null) {
         Constructor<? extends ConfigurationSerializable> constructor = this.getConstructor();
         if (constructor != null) {
            result = this.deserializeViaCtor(constructor, args);
         }
      }

      return result;
   }

   protected Method getMethod(String name, boolean isStatic) {
      try {
         Method method = this.clazz.getDeclaredMethod(name, Map.class);
         if (!ConfigurationSerializable.class.isAssignableFrom(method.getReturnType())) {
            return null;
         } else {
            return Modifier.isStatic(method.getModifiers()) != isStatic ? null : method;
         }
      } catch (SecurityException | NoSuchMethodException e) {
         return null;
      }
   }

   protected Constructor<? extends ConfigurationSerializable> getConstructor() {
      try {
         return this.clazz.getConstructor(Map.class);
      } catch (SecurityException | NoSuchMethodException e) {
         return null;
      }
   }

   protected ConfigurationSerializable deserializeViaMethod(Method method, Map<String, ?> args) {
      try {
         ConfigurationSerializable result = (ConfigurationSerializable)method.invoke((Object)null, args);
         if (result != null) {
            return result;
         }

         Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method + "' of " + this.clazz + " for deserialization: method returned null");
      } catch (Throwable t) {
         Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + this.clazz + " for deserialization", t instanceof InvocationTargetException ? t.getCause() : t);
      }

      return null;
   }

   protected ConfigurationSerializable deserializeViaCtor(Constructor<? extends ConfigurationSerializable> ctor, Map<String, ?> args) {
      try {
         return (ConfigurationSerializable)ctor.newInstance(args);
      } catch (Throwable t) {
         Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call constructor '" + ctor.toString() + "' of " + this.clazz + " for deserialization", t instanceof InvocationTargetException ? t.getCause() : t);
         return null;
      }
   }
}
