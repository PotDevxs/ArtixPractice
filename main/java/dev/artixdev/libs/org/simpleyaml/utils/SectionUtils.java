package dev.artixdev.libs.org.simpleyaml.utils;

import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;

public final class SectionUtils {
   public static void convertMapsToSections(Map<?, ?> values, ConfigurationSection section) {
      if (values != null) {
         for (Entry<?, ?> entry : values.entrySet()) {
            Object keyObject = entry.getKey();
            String key;
            if (keyObject == null) {
               key = "";
            } else {
               key = keyObject.toString();
            }

            Object value = entry.getValue();
            if (value instanceof Map) {
               convertMapsToSections((Map)value, section.createSection(key));
            } else {
               section.set(key, value);
            }
         }

      }
   }
}
