package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.Deque;
import java.util.List;
import java.util.Set;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@FunctionalInterface
@ApiStatus.NonExtendable
public interface ComponentIteratorType {
   ComponentIteratorType DEPTH_FIRST = (component, deque, flags) -> {
      List children;
      int i;
      if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
         TranslatableComponent translatable = (TranslatableComponent)component;
         children = translatable.args();

         for(i = children.size() - 1; i >= 0; --i) {
            deque.addFirst((Component)children.get(i));
         }
      }

      HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
         HoverEvent.Action<?> action = hoverEvent.action();
         if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
            deque.addFirst(((HoverEvent.ShowEntity)hoverEvent.value()).name());
         } else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
            deque.addFirst((Component)hoverEvent.value());
         }
      }

      children = component.children();

      for(i = children.size() - 1; i >= 0; --i) {
         deque.addFirst((Component)children.get(i));
      }

   };
   ComponentIteratorType BREADTH_FIRST = (component, deque, flags) -> {
      if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
         deque.addAll(((TranslatableComponent)component).args());
      }

      HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
         HoverEvent.Action<?> action = hoverEvent.action();
         if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
            deque.addLast(((HoverEvent.ShowEntity)hoverEvent.value()).name());
         } else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
            deque.addLast((Component)hoverEvent.value());
         }
      }

      deque.addAll(component.children());
   };

   void populate(@NotNull Component var1, @NotNull Deque<Component> var2, @NotNull Set<ComponentIteratorFlag> var3);
}
