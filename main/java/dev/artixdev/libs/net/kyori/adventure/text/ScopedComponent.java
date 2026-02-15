package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEventSource;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public interface ScopedComponent<C extends Component> extends Component {
   @NotNull
   C children(@NotNull List<? extends ComponentLike> var1);

   @NotNull
   C style(@NotNull Style var1);

   @NotNull
   default C style(@NotNull Consumer<Style.Builder> style) {
      return (C) Component.super.style(style);
   }

   @NotNull
   default C style(@NotNull Style.Builder style) {
      return (C) Component.super.style(style);
   }

   @NotNull
   default C mergeStyle(@NotNull Component that) {
      return (C) Component.super.mergeStyle(that);
   }

   @NotNull
   default C mergeStyle(@NotNull Component that, @NotNull Style.Merge... merges) {
      return (C) Component.super.mergeStyle(that, merges);
   }

   @NotNull
   default C append(@NotNull Component component) {
      return (C) Component.super.append(component);
   }

   @NotNull
   default C append(@NotNull ComponentLike like) {
      return (C) Component.super.append(like);
   }

   @NotNull
   default C append(@NotNull ComponentBuilder<?, ?> builder) {
      return (C) Component.super.append(builder);
   }

   @NotNull
   default C mergeStyle(@NotNull Component that, @NotNull Set<Style.Merge> merges) {
      return (C) Component.super.mergeStyle(that, merges);
   }

   @NotNull
   default C color(@Nullable TextColor color) {
      return (C) Component.super.color(color);
   }

   @NotNull
   default C colorIfAbsent(@Nullable TextColor color) {
      return (C) Component.super.colorIfAbsent(color);
   }

   @NotNull
   default C decorate(@NotNull TextDecoration decoration) {
      return (C) Component.super.decorate(decoration);
   }

   @NotNull
   default C decoration(@NotNull TextDecoration decoration, boolean flag) {
      return (C) Component.super.decoration(decoration, flag);
   }

   @NotNull
   default C decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
      return (C) Component.super.decoration(decoration, state);
   }

   @NotNull
   default C clickEvent(@Nullable ClickEvent event) {
      return (C) Component.super.clickEvent(event);
   }

   @NotNull
   default C hoverEvent(@Nullable HoverEventSource<?> event) {
      return (C) Component.super.hoverEvent(event);
   }

   @NotNull
   default C insertion(@Nullable String insertion) {
      return (C) Component.super.insertion(insertion);
   }
}
