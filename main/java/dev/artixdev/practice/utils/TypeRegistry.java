package dev.artixdev.practice.utils;

import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2IntMap;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2IntMaps;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Type Registry
 * Registry for managing type mappings and conversions
 */
public class TypeRegistry {
   
   private static final Object2IntMap<Class<?>> typeToIdMap;
   private static final Int2ObjectMap<Class<?>> idToTypeMap;
   
   static {
      typeToIdMap = new Object2IntOpenHashMap<>();
      idToTypeMap = new Int2ObjectOpenHashMap<>();
      
      // Initialize default type mappings
      initializeDefaultTypes();
   }
   
   /**
    * Initialize default type mappings
    */
   private static void initializeDefaultTypes() {
      // Register default types
      registerType(String.class, 1);
      registerType(Integer.class, 2);
      registerType(Double.class, 3);
      registerType(Boolean.class, 4);
      registerType(Long.class, 5);
      registerType(Float.class, 6);
      registerType(Byte.class, 7);
      registerType(Short.class, 8);
      registerType(Character.class, 9);
      registerType(java.util.UUID.class, 10);
   }
   
   /**
    * Register a type
    * @param type the type class
    * @param id the type ID
    */
   public static void registerType(Class<?> type, int id) {
      if (type == null) {
         throw new IllegalArgumentException("Type cannot be null");
      }
      
      if (id < 0) {
         throw new IllegalArgumentException("ID must be non-negative");
      }
      
      typeToIdMap.put(type, id);
      idToTypeMap.put(id, type);
   }
   
   /**
    * Get type ID
    * @param type the type class
    * @return type ID or -1 if not found
    */
   public static int getTypeId(Class<?> type) {
      if (type == null) {
         return -1;
      }
      
      return typeToIdMap.getInt(type);
   }
   
   /**
    * Get type class
    * @param id the type ID
    * @return type class or null if not found
    */
   public static Class<?> getType(int id) {
      return idToTypeMap.get(id);
   }
   
   /**
    * Check if type is registered
    * @param type the type class
    * @return true if registered
    */
   public static boolean isTypeRegistered(Class<?> type) {
      if (type == null) {
         return false;
      }
      
      return typeToIdMap.containsKey(type);
   }
   
   /**
    * Check if ID is registered
    * @param id the type ID
    * @return true if registered
    */
   public static boolean isIdRegistered(int id) {
      return idToTypeMap.containsKey(id);
   }
   
   /**
    * Unregister type
    * @param type the type class
    * @return true if unregistered
    */
   public static boolean unregisterType(Class<?> type) {
      if (type == null) {
         return false;
      }
      
      int id = typeToIdMap.removeInt(type);
      if (id != -1) {
         idToTypeMap.remove(id);
         return true;
      }
      
      return false;
   }
   
   /**
    * Unregister type by ID
    * @param id the type ID
    * @return true if unregistered
    */
   public static boolean unregisterType(int id) {
      Class<?> type = idToTypeMap.remove(id);
      if (type != null) {
         typeToIdMap.removeInt(type);
         return true;
      }
      
      return false;
   }
   
   /**
    * Get all registered types
    * @return set of registered types
    */
   public static java.util.Set<Class<?>> getRegisteredTypes() {
      return typeToIdMap.keySet();
   }
   
   /**
    * Get all registered IDs
    * @return set of registered IDs
    */
   public static java.util.Set<Integer> getRegisteredIds() {
      return idToTypeMap.keySet();
   }
   
   /**
    * Get type count
    * @return number of registered types
    */
   public static int getTypeCount() {
      return typeToIdMap.size();
   }
   
   /**
    * Clear all types
    */
   public static void clearAllTypes() {
      typeToIdMap.clear();
      idToTypeMap.clear();
   }
   
   /**
    * Get type info
    * @param type the type class
    * @return type info string
    */
   public static String getTypeInfo(Class<?> type) {
      if (type == null) {
         return "null";
      }
      
      int id = getTypeId(type);
      return String.format("Type: %s, ID: %d", type.getSimpleName(), id);
   }
   
   /**
    * Get all type info
    * @return list of type info strings
    */
   public static java.util.List<String> getAllTypeInfo() {
      java.util.List<String> info = new java.util.ArrayList<>();
      
      for (Class<?> type : getRegisteredTypes()) {
         info.add(getTypeInfo(type));
      }
      
      return info;
   }
   
   /**
    * Validate type registry
    * @return true if valid
    */
   public static boolean validateRegistry() {
      // Check if all types have valid IDs
      for (Class<?> type : typeToIdMap.keySet()) {
         int id = typeToIdMap.getInt(type);
         if (id < 0) {
            return false;
         }
         
         Class<?> mappedType = idToTypeMap.get(id);
         if (mappedType != type) {
            return false;
         }
      }
      
      // Check if all IDs have valid types
      for (int id : idToTypeMap.keySet()) {
         Class<?> type = idToTypeMap.get(id);
         if (type == null) {
            return false;
         }
         
         int mappedId = typeToIdMap.getInt(type);
         if (mappedId != id) {
            return false;
         }
      }
      
      return true;
   }
   
   /**
    * Get registry statistics
    * @return registry statistics
    */
   public static String getRegistryStatistics() {
      return String.format("Registered Types: %d, Valid: %s", 
         getTypeCount(), validateRegistry());
   }
   
   /**
    * Abstract type handler interface
    */
   public static abstract class TypeHandler {
      
      /**
       * Handle type conversion
       * @param value the value to convert
       * @return converted value
       */
      public abstract Object handle(Object value);
      
      /**
       * Get handler type
       * @return handler type
       */
      public abstract Class<?> getHandlerType();
      
      /**
       * Check if handler can handle type
       * @param type the type to check
       * @return true if can handle
       */
      public boolean canHandle(Class<?> type) {
         return getHandlerType().isAssignableFrom(type);
      }
   }
}
