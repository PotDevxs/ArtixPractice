package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ComponentLike {
   @NotNull
   static List<Component> asComponents(@NotNull List<? extends ComponentLike> likes) {
      return asComponents(likes, (Predicate)null);
   }

   @NotNull
   static List<Component> asComponents(@NotNull List<? extends ComponentLike> likes, @Nullable Predicate<? super Component> filter) {
      Objects.requireNonNull(likes, "likes");
      int size = likes.size();
      if (size == 0) {
         return Collections.emptyList();
      } else {
         ArrayList<Component> components = null;

         for(int i = 0; i < size; ++i) {
            ComponentLike like = (ComponentLike)likes.get(i);
            if (like == null) {
               throw new NullPointerException("likes[" + i + "]");
            }

            Component component = like.asComponent();
            if (filter == null || filter.test(component)) {
               if (components == null) {
                  components = new ArrayList(size);
               }

               components.add(component);
            }
         }

         if (components == null) {
            return Collections.emptyList();
         } else {
            components.trimToSize();
            return Collections.unmodifiableList(components);
         }
      }
   }

   @Nullable
   static Component unbox(@Nullable ComponentLike like) {
      return like != null ? like.asComponent() : null;
   }

   @Contract(
      pure = true
   )
   @NotNull
   Component asComponent();
}
