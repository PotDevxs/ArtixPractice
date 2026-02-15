package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEventSource;
import dev.artixdev.libs.net.kyori.adventure.text.format.MutableStyleSetter;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface ComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> extends AbstractBuilder<C>, ComponentBuilderApplicable, ComponentLike, MutableStyleSetter<B>, Buildable.Builder<C> {
   @Contract("_ -> this")
   @NotNull
   B append(@NotNull Component var1);

   @Contract("_ -> this")
   @NotNull
   default B append(@NotNull ComponentLike component) {
      return this.append(component.asComponent());
   }

   @Contract("_ -> this")
   @NotNull
   default B append(@NotNull ComponentBuilder<?, ?> builder) {
      return this.append((Component)builder.build());
   }

   @Contract("_ -> this")
   @NotNull
   B append(@NotNull Component... var1);

   @Contract("_ -> this")
   @NotNull
   B append(@NotNull ComponentLike... var1);

   @Contract("_ -> this")
   @NotNull
   B append(@NotNull Iterable<? extends ComponentLike> var1);

   @NotNull
   default B appendNewline() {
      return this.append((Component)Component.newline());
   }

   @NotNull
   default B appendSpace() {
      return this.append((Component)Component.space());
   }

   @Contract("_ -> this")
   @NotNull
   @SuppressWarnings("unchecked")
   default B apply(@NotNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
      consumer.accept(this);
      return (B) this;
   }

   @Contract("_ -> this")
   @NotNull
   B applyDeep(@NotNull Consumer<? super ComponentBuilder<?, ?>> var1);

   @Contract("_ -> this")
   @NotNull
   B mapChildren(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> var1);

   @Contract("_ -> this")
   @NotNull
   B mapChildrenDeep(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> var1);

   @NotNull
   List<Component> children();

   @Contract("_ -> this")
   @NotNull
   B style(@NotNull Style var1);

   @Contract("_ -> this")
   @NotNull
   B style(@NotNull Consumer<Style.Builder> var1);

   @Contract("_ -> this")
   @NotNull
   B font(@Nullable Key var1);

   @Contract("_ -> this")
   @NotNull
   B color(@Nullable TextColor var1);

   @Contract("_ -> this")
   @NotNull
   B colorIfAbsent(@Nullable TextColor var1);

   @Contract("_, _ -> this")
   @NotNull
   default B decorations(@NotNull Set<TextDecoration> decorations, boolean flag) {
      return (B) MutableStyleSetter.super.decorations(decorations, flag);
   }

   @Contract("_ -> this")
   @NotNull
   default B decorate(@NotNull TextDecoration decoration) {
      return this.decoration(decoration, TextDecoration.State.TRUE);
   }

   @Contract("_ -> this")
   @NotNull
   default B decorate(@NotNull TextDecoration... decorations) {
      return (B) MutableStyleSetter.super.decorate(decorations);
   }

   @Contract("_, _ -> this")
   @NotNull
   default B decoration(@NotNull TextDecoration decoration, boolean flag) {
      return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
   }

   @Contract("_ -> this")
   @NotNull
   default B decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
      return (B) MutableStyleSetter.super.decorations(decorations);
   }

   @Contract("_, _ -> this")
   @NotNull
   B decoration(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

   @Contract("_, _ -> this")
   @NotNull
   B decorationIfAbsent(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

   @Contract("_ -> this")
   @NotNull
   B clickEvent(@Nullable ClickEvent var1);

   @Contract("_ -> this")
   @NotNull
   B hoverEvent(@Nullable HoverEventSource<?> var1);

   @Contract("_ -> this")
   @NotNull
   B insertion(@Nullable String var1);

   @Contract("_ -> this")
   @NotNull
   default B mergeStyle(@NotNull Component that) {
      return this.mergeStyle(that, Style.Merge.all());
   }

   @Contract("_, _ -> this")
   @NotNull
   default B mergeStyle(@NotNull Component that, @NotNull Style.Merge... merges) {
      return this.mergeStyle(that, Style.Merge.merges(merges));
   }

   @Contract("_, _ -> this")
   @NotNull
   B mergeStyle(@NotNull Component var1, @NotNull Set<Style.Merge> var2);

   @NotNull
   B resetStyle();

   @NotNull
   C build();

   @Contract("_ -> this")
   @NotNull
   @SuppressWarnings("unchecked")
   default B applicableApply(@NotNull ComponentBuilderApplicable applicable) {
      applicable.componentBuilderApply(this);
      return (B) this;
   }

   default void componentBuilderApply(@NotNull ComponentBuilder<?, ?> component) {
      component.append(this);
   }

   @NotNull
   default Component asComponent() {
      return this.build();
   }
}
