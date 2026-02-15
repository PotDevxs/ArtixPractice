package dev.artixdev.libs.net.kyori.adventure.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public final class MonkeyBars {
   private MonkeyBars() {
   }

   @SafeVarargs
   @NotNull
   public static <E extends Enum<E>> Set<E> enumSet(Class<E> type, @NotNull E... constants) {
      Set<E> set = EnumSet.noneOf(type);
      Collections.addAll(set, constants);
      return Collections.unmodifiableSet(set);
   }

   @NotNull
   public static <T> List<T> addOne(@NotNull List<T> oldList, T newElement) {
      if (oldList.isEmpty()) {
         return Collections.singletonList(newElement);
      } else {
         List<T> newList = new ArrayList(oldList.size() + 1);
         newList.addAll(oldList);
         newList.add(newElement);
         return Collections.unmodifiableList(newList);
      }
   }
}
