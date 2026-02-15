package dev.artixdev.libs.org.bson;

import java.util.Arrays;
import java.util.stream.Collectors;

final class StringUtils {
   @SafeVarargs
   public static <T> String join(String delimiter, T... values) {
      return (String)Arrays.stream(values).map(String::valueOf).collect(Collectors.joining(delimiter));
   }

   private StringUtils() {
   }
}
