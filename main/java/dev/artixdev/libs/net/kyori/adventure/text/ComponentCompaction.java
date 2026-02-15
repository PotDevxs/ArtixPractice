package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.VisibleForTesting;

final class ComponentCompaction {
   @VisibleForTesting
   static final boolean SIMPLIFY_STYLE_FOR_BLANK_COMPONENTS = false;

   private ComponentCompaction() {
   }

   static Component compact(@NotNull Component self, @Nullable Style parentStyle) {
      List<Component> children = self.children();
      Component optimized = self.children(Collections.emptyList());
      if (parentStyle != null) {
         optimized = ((Component)optimized).style(self.style().unmerge(parentStyle));
      }

      int childrenSize = children.size();
      if (childrenSize == 0) {
         if (isBlank((Component)optimized)) {
            optimized = ((Component)optimized).style(simplifyStyleForBlank(((Component)optimized).style(), parentStyle));
         }

         return (Component)optimized;
      } else {
         if (childrenSize == 1 && optimized instanceof TextComponent) {
            TextComponent textComponent = (TextComponent)optimized;
            if (textComponent.content().isEmpty()) {
               Component child = (Component)children.get(0);
               return child.style(child.style().merge(((Component)optimized).style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET)).compact();
            }
         }

         Style childParentStyle = ((Component)optimized).style();
         if (parentStyle != null) {
            childParentStyle = childParentStyle.merge(parentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
         }

         List<Component> childrenToAppend = new ArrayList(children.size());

         int i;
         Component child;
         for(i = 0; i < children.size(); ++i) {
            child = (Component)children.get(i);
            child = compact(child, childParentStyle);
            if (child.children().isEmpty() && child instanceof TextComponent) {
               TextComponent textComponent = (TextComponent)child;
               if (textComponent.content().isEmpty()) {
                  continue;
               }
            }

            childrenToAppend.add(child);
         }

         if (optimized instanceof TextComponent) {
            while(!childrenToAppend.isEmpty()) {
               Component firstChild = (Component)childrenToAppend.get(0);
               Style childStyle = firstChild.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
               if (!(firstChild instanceof TextComponent) || !Objects.equals(childStyle, childParentStyle)) {
                  break;
               }

               optimized = joinText((TextComponent)optimized, (TextComponent)firstChild);
               childrenToAppend.remove(0);
               childrenToAppend.addAll(0, firstChild.children());
            }
         }

         i = 0;

         while(true) {
            while(i + 1 < childrenToAppend.size()) {
               child = (Component)childrenToAppend.get(i);
               Component neighbor = (Component)childrenToAppend.get(i + 1);
               if (child.children().isEmpty() && child instanceof TextComponent && neighbor instanceof TextComponent) {
                  Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                  Style neighborStyle = neighbor.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                  if (childStyle.equals(neighborStyle)) {
                     Component combined = joinText((TextComponent)child, (TextComponent)neighbor);
                     childrenToAppend.set(i, combined);
                     childrenToAppend.remove(i + 1);
                     continue;
                  }
               }

               ++i;
            }

            if (childrenToAppend.isEmpty() && isBlank((Component)optimized)) {
               optimized = ((Component)optimized).style(simplifyStyleForBlank(((Component)optimized).style(), parentStyle));
            }

            return ((Component)optimized).children(childrenToAppend);
         }
      }
   }

   private static boolean isBlank(Component component) {
      if (component instanceof TextComponent) {
         TextComponent textComponent = (TextComponent)component;
         String content = textComponent.content();

         for(int i = 0; i < content.length(); ++i) {
            char c = content.charAt(i);
            if (c != ' ') {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @NotNull
   private static Style simplifyStyleForBlank(@NotNull Style style, @Nullable Style parentStyle) {
      return style;
   }

   private static TextComponent joinText(TextComponent one, TextComponent two) {
      return TextComponentImpl.create(two.children(), one.style(), one.content() + two.content());
   }
}
