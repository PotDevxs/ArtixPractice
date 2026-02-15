package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.util;

public class EnumUtils {
   public static <T extends Enum<T>> T findEnumInsensitiveCase(Class<T> enumType, String name) {
      T[] constants = enumType.getEnumConstants();
      if (constants == null) {
         throw new IllegalArgumentException("Not an enum type: " + enumType.getCanonicalName());
      }
      for (int i = 0; i < constants.length; i++) {
         T constant = constants[i];
         if (constant.name().compareToIgnoreCase(name) == 0) {
            return constant;
         }
      }

      throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + "." + name);
   }
}
