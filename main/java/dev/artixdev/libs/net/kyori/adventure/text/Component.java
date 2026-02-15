package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEventSource;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import dev.artixdev.libs.net.kyori.adventure.text.format.StyleGetter;
import dev.artixdev.libs.net.kyori.adventure.text.format.StyleSetter;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.adventure.translation.Translatable;
import dev.artixdev.libs.net.kyori.adventure.util.ForwardingIterator;
import dev.artixdev.libs.net.kyori.adventure.util.IntFunction2;
import dev.artixdev.libs.net.kyori.adventure.util.MonkeyBars;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface Component extends ComponentBuilderApplicable, ComponentLike, HoverEventSource<Component>, StyleGetter, StyleSetter<Component>, Examinable {
   BiPredicate<? super Component, ? super Component> EQUALS = Objects::equals;
   BiPredicate<? super Component, ? super Component> EQUALS_IDENTITY = (a, b) -> {
      return a == b;
   };
   Predicate<? super Component> IS_NOT_EMPTY = (component) -> {
      return component != empty();
   };

   @NotNull
   static TextComponent empty() {
      return TextComponentImpl.EMPTY;
   }

   @NotNull
   static TextComponent newline() {
      return TextComponentImpl.NEWLINE;
   }

   @NotNull
   static TextComponent space() {
      return TextComponentImpl.SPACE;
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent join(@NotNull ComponentLike separator, @NotNull ComponentLike... components) {
      return join((ComponentLike)separator, (Iterable)Arrays.asList(components));
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent join(@NotNull ComponentLike separator, Iterable<? extends ComponentLike> components) {
      Component component = join(JoinConfiguration.separator(separator), components);
      return component instanceof TextComponent ? (TextComponent)component : (TextComponent)((TextComponent.Builder)text().append(component)).build();
   }

   @Contract(
      pure = true
   )
   @NotNull
   static Component join(@NotNull JoinConfiguration.Builder configBuilder, @NotNull ComponentLike... components) {
      return join((JoinConfiguration.Builder)configBuilder, (Iterable)Arrays.asList(components));
   }

   @Contract(
      pure = true
   )
   @NotNull
   static Component join(@NotNull JoinConfiguration.Builder configBuilder, @NotNull Iterable<? extends ComponentLike> components) {
      return JoinConfigurationImpl.join((JoinConfiguration)configBuilder.build(), components);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static Component join(@NotNull JoinConfiguration config, @NotNull ComponentLike... components) {
      return join((JoinConfiguration)config, (Iterable)Arrays.asList(components));
   }

   @Contract(
      pure = true
   )
   @NotNull
   static Component join(@NotNull JoinConfiguration config, @NotNull Iterable<? extends ComponentLike> components) {
      return JoinConfigurationImpl.join(config, components);
   }

   @NotNull
   static Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent() {
      return toComponent(empty());
   }

   @NotNull
   static Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent(@NotNull Component separator) {
      return Collector.of(Component::text, (builder, add) -> {
         if (separator != empty() && !builder.children().isEmpty()) {
            builder.append(separator);
         }

         builder.append(add);
      }, (a, b) -> {
         List<Component> aChildren = a.children();
         TextComponent.Builder ret = (TextComponent.Builder)text().append(aChildren);
         if (!aChildren.isEmpty()) {
            ret.append(separator);
         }

         ret.append(b.children());
         return ret;
      }, ComponentBuilder::build);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static BlockNBTComponent.Builder blockNBT() {
      return new BlockNBTComponentImpl.BuilderImpl();
   }

   @Contract("_ -> new")
   @NotNull
   static BlockNBTComponent blockNBT(@NotNull Consumer<? super BlockNBTComponent.Builder> consumer) {
      return (BlockNBTComponent)AbstractBuilder.configureAndBuild(blockNBT(), consumer);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static BlockNBTComponent blockNBT(@NotNull String nbtPath, @NotNull BlockNBTComponent.Pos pos) {
      return blockNBT(nbtPath, false, pos);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static BlockNBTComponent blockNBT(@NotNull String nbtPath, boolean interpret, @NotNull BlockNBTComponent.Pos pos) {
      return blockNBT(nbtPath, interpret, (ComponentLike)null, pos);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static BlockNBTComponent blockNBT(@NotNull String nbtPath, boolean interpret, @Nullable ComponentLike separator, @NotNull BlockNBTComponent.Pos pos) {
      return BlockNBTComponentImpl.create(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, pos);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static EntityNBTComponent.Builder entityNBT() {
      return new EntityNBTComponentImpl.BuilderImpl();
   }

   @Contract("_ -> new")
   @NotNull
   static EntityNBTComponent entityNBT(@NotNull Consumer<? super EntityNBTComponent.Builder> consumer) {
      return (EntityNBTComponent)AbstractBuilder.configureAndBuild(entityNBT(), consumer);
   }

   @Contract("_, _ -> new")
   @NotNull
   static EntityNBTComponent entityNBT(@NotNull String nbtPath, @NotNull String selector) {
      return (EntityNBTComponent)((EntityNBTComponent.Builder)entityNBT().nbtPath(nbtPath)).selector(selector).build();
   }

   @Contract(
      pure = true
   )
   @NotNull
   static KeybindComponent.Builder keybind() {
      return new KeybindComponentImpl.BuilderImpl();
   }

   @Contract("_ -> new")
   @NotNull
   static KeybindComponent keybind(@NotNull Consumer<? super KeybindComponent.Builder> consumer) {
      return (KeybindComponent)AbstractBuilder.configureAndBuild(keybind(), consumer);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull String keybind) {
      return keybind(keybind, Style.empty());
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull KeybindComponent.KeybindLike keybind) {
      return keybind(((KeybindComponent.KeybindLike)Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.empty());
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull String keybind, @NotNull Style style) {
      return KeybindComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), keybind);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull KeybindComponent.KeybindLike keybind, @NotNull Style style) {
      return KeybindComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), ((KeybindComponent.KeybindLike)Objects.requireNonNull(keybind, "keybind")).asKeybind());
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull String keybind, @Nullable TextColor color) {
      return keybind(keybind, Style.style(color));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull KeybindComponent.KeybindLike keybind, @Nullable TextColor color) {
      return keybind(((KeybindComponent.KeybindLike)Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull String keybind, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return keybind(keybind, Style.style(color, decorations));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull KeybindComponent.KeybindLike keybind, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return keybind(((KeybindComponent.KeybindLike)Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color, decorations));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull String keybind, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return keybind(keybind, Style.style(color, decorations));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static KeybindComponent keybind(@NotNull KeybindComponent.KeybindLike keybind, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return keybind(((KeybindComponent.KeybindLike)Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color, decorations));
   }

   @Contract(
      pure = true
   )
   @NotNull
   static ScoreComponent.Builder score() {
      return new ScoreComponentImpl.BuilderImpl();
   }

   @Contract("_ -> new")
   @NotNull
   static ScoreComponent score(@NotNull Consumer<? super ScoreComponent.Builder> consumer) {
      return (ScoreComponent)AbstractBuilder.configureAndBuild(score(), consumer);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static ScoreComponent score(@NotNull String name, @NotNull String objective) {
      return score(name, objective, (String)null);
   }

   /** @deprecated */
   @Deprecated
   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static ScoreComponent score(@NotNull String name, @NotNull String objective, @Nullable String value) {
      return ScoreComponentImpl.create(Collections.emptyList(), Style.empty(), name, objective, value);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static SelectorComponent.Builder selector() {
      return new SelectorComponentImpl.BuilderImpl();
   }

   @Contract("_ -> new")
   @NotNull
   static SelectorComponent selector(@NotNull Consumer<? super SelectorComponent.Builder> consumer) {
      return (SelectorComponent)AbstractBuilder.configureAndBuild(selector(), consumer);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static SelectorComponent selector(@NotNull String pattern) {
      return selector(pattern, (ComponentLike)null);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static SelectorComponent selector(@NotNull String pattern, @Nullable ComponentLike separator) {
      return SelectorComponentImpl.create(Collections.emptyList(), Style.empty(), pattern, separator);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static StorageNBTComponent.Builder storageNBT() {
      return new StorageNBTComponentImpl.BuilderImpl();
   }

   @Contract("_ -> new")
   @NotNull
   static StorageNBTComponent storageNBT(@NotNull Consumer<? super StorageNBTComponent.Builder> consumer) {
      return (StorageNBTComponent)AbstractBuilder.configureAndBuild(storageNBT(), consumer);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static StorageNBTComponent storageNBT(@NotNull String nbtPath, @NotNull Key storage) {
      return storageNBT(nbtPath, false, storage);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static StorageNBTComponent storageNBT(@NotNull String nbtPath, boolean interpret, @NotNull Key storage) {
      return storageNBT(nbtPath, interpret, (ComponentLike)null, storage);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static StorageNBTComponent storageNBT(@NotNull String nbtPath, boolean interpret, @Nullable ComponentLike separator, @NotNull Key storage) {
      return StorageNBTComponentImpl.create(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, storage);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static TextComponent.Builder text() {
      return new TextComponentImpl.BuilderImpl();
   }

   @NotNull
   static TextComponent textOfChildren(@NotNull ComponentLike... components) {
      return components.length == 0 ? empty() : TextComponentImpl.create(Arrays.asList(components), Style.empty(), "");
   }

   @Contract("_ -> new")
   @NotNull
   static TextComponent text(@NotNull Consumer<? super TextComponent.Builder> consumer) {
      return (TextComponent)AbstractBuilder.configureAndBuild(text(), consumer);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(@NotNull String content) {
      return content.isEmpty() ? empty() : text(content, Style.empty());
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(@NotNull String content, @NotNull Style style) {
      return TextComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), content);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(@NotNull String content, @Nullable TextColor color) {
      return text(content, Style.style(color));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(@NotNull String content, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return text(content, Style.style(color, decorations));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(@NotNull String content, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return text(content, Style.style(color, decorations));
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(boolean value) {
      return text(String.valueOf(value));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(boolean value, @NotNull Style style) {
      return text(String.valueOf(value), style);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(boolean value, @Nullable TextColor color) {
      return text(String.valueOf(value), color);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(boolean value, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(boolean value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static TextComponent text(char value) {
      if (value == '\n') {
         return newline();
      } else {
         return value == ' ' ? space() : text(String.valueOf(value));
      }
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(char value, @NotNull Style style) {
      return text(String.valueOf(value), style);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(char value, @Nullable TextColor color) {
      return text(String.valueOf(value), color);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(char value, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(char value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(double value) {
      return text(String.valueOf(value));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(double value, @NotNull Style style) {
      return text(String.valueOf(value), style);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(double value, @Nullable TextColor color) {
      return text(String.valueOf(value), color);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(double value, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(double value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(float value) {
      return text(String.valueOf(value));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(float value, @NotNull Style style) {
      return text(String.valueOf(value), style);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(float value, @Nullable TextColor color) {
      return text(String.valueOf(value), color);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(float value, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(float value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(int value) {
      return text(String.valueOf(value));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(int value, @NotNull Style style) {
      return text(String.valueOf(value), style);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(int value, @Nullable TextColor color) {
      return text(String.valueOf(value), color);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(int value, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(int value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(long value) {
      return text(String.valueOf(value));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(long value, @NotNull Style style) {
      return text(String.valueOf(value), style);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(long value, @Nullable TextColor color) {
      return text(String.valueOf(value), color);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(long value, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TextComponent text(long value, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return text(String.valueOf(value), color, decorations);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static TranslatableComponent.Builder translatable() {
      return new TranslatableComponentImpl.BuilderImpl();
   }

   @Contract("_ -> new")
   @NotNull
   static TranslatableComponent translatable(@NotNull Consumer<? super TranslatableComponent.Builder> consumer) {
      return (TranslatableComponent)AbstractBuilder.configureAndBuild(translatable(), consumer);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key) {
      return translatable(key, Style.empty());
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), Style.empty());
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback) {
      return translatable(key, fallback, Style.empty());
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, Style.empty());
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @NotNull Style style) {
      return TranslatableComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), key, (String)null, (List)Collections.emptyList());
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull Style style) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), style);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull Style style) {
      return TranslatableComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), key, fallback, Collections.emptyList());
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Style style) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, style);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull StyleBuilderApplicable... style) {
      return translatable((String)Objects.requireNonNull(key, "key"), fallback, Style.style(style));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Iterable<StyleBuilderApplicable> style) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, Style.style(style));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull ComponentLike... args) {
      return translatable(key, fallback, Style.empty(), args);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull ComponentLike... args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, args);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull Style style, @NotNull ComponentLike... args) {
      return TranslatableComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), key, fallback, (ComponentLike[])Objects.requireNonNull(args, "args"));
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Style style, @NotNull ComponentLike... args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, style, args);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
      return TranslatableComponentImpl.create(Collections.emptyList(), style, key, fallback, (List)Objects.requireNonNull(args, "args"));
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, style, args);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull Iterable<StyleBuilderApplicable> style) {
      return TranslatableComponentImpl.create(Collections.emptyList(), Style.style(style), key, fallback, (List)Objects.requireNonNull(args, "args"));
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull Iterable<StyleBuilderApplicable> style) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, args, style);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull StyleBuilderApplicable... style) {
      return TranslatableComponentImpl.create(Collections.emptyList(), Style.style(style), key, fallback, (List)Objects.requireNonNull(args, "args"));
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable String fallback, @NotNull List<? extends ComponentLike> args, @NotNull StyleBuilderApplicable... style) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), fallback, args, style);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color) {
      return translatable(key, Style.style(color));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), color);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return translatable(key, Style.style(color, decorations));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return translatable(key, Style.style(color, decorations));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @NotNull ComponentLike... args) {
      return translatable(key, Style.empty(), args);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull ComponentLike... args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), args);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @NotNull Style style, @NotNull ComponentLike... args) {
      return TranslatableComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), key, (String)null, (ComponentLike[])((ComponentLike[])Objects.requireNonNull(args, "args")));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull Style style, @NotNull ComponentLike... args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), style, args);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull ComponentLike... args) {
      return translatable(key, Style.style(color), args);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull ComponentLike... args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), color, args);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull ComponentLike... args) {
      return translatable(key, Style.style(color, decorations), args);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull ComponentLike... args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations, args);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @NotNull List<? extends ComponentLike> args) {
      return TranslatableComponentImpl.create(Collections.emptyList(), Style.empty(), key, (String)null, (List)((List)Objects.requireNonNull(args, "args")));
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull List<? extends ComponentLike> args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), args);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
      return TranslatableComponentImpl.create(Collections.emptyList(), (Style)Objects.requireNonNull(style, "style"), key, (String)null, (List)((List)Objects.requireNonNull(args, "args")));
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @NotNull Style style, @NotNull List<? extends ComponentLike> args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), style, args);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull List<? extends ComponentLike> args) {
      return translatable(key, Style.style(color), args);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull List<? extends ComponentLike> args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), color, args);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull String key, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull List<? extends ComponentLike> args) {
      return translatable(key, Style.style(color, decorations), args);
   }

   @Contract(
      value = "_, _, _, _ -> new",
      pure = true
   )
   @NotNull
   static TranslatableComponent translatable(@NotNull Translatable translatable, @Nullable TextColor color, @NotNull Set<TextDecoration> decorations, @NotNull List<? extends ComponentLike> args) {
      return translatable(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations, args);
   }

   @NotNull
   @Unmodifiable
   List<Component> children();

   @Contract(
      pure = true
   )
   @NotNull
   Component children(@NotNull List<? extends ComponentLike> var1);

   default boolean contains(@NotNull Component that) {
      return this.contains(that, EQUALS_IDENTITY);
   }

   default boolean contains(@NotNull Component that, @NotNull BiPredicate<? super Component, ? super Component> equals) {
      if (equals.test(this, that)) {
         return true;
      } else {
         Iterator var3 = this.children().iterator();

         while(var3.hasNext()) {
            Component child = (Component)var3.next();
            if (child.contains(that, equals)) {
               return true;
            }
         }

         HoverEvent<?> hoverEvent = this.hoverEvent();
         if (hoverEvent != null) {
            Object value = hoverEvent.value();
            Component component = null;
            if (value instanceof Component) {
               component = (Component)hoverEvent.value();
            } else if (value instanceof HoverEvent.ShowEntity) {
               component = ((HoverEvent.ShowEntity)value).name();
            }

            if (component != null) {
               if (equals.test(that, component)) {
                  return true;
               }

               Iterator var6 = component.children().iterator();

               while(var6.hasNext()) {
                  Component child = (Component)var6.next();
                  if (child.contains(that, equals)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default void detectCycle(@NotNull Component that) {
      if (that.contains(this)) {
         throw new IllegalStateException("Component cycle detected between " + this + " and " + that);
      }
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component append(@NotNull Component component) {
      return this.append((ComponentLike)component);
   }

   @NotNull
   default Component append(@NotNull ComponentLike like) {
      Objects.requireNonNull(like, "like");
      Component component = like.asComponent();
      Objects.requireNonNull(component, "component");
      if (component == empty()) {
         return this;
      } else {
         List<Component> oldChildren = this.children();
         return this.children(MonkeyBars.addOne(oldChildren, component));
      }
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component append(@NotNull ComponentBuilder<?, ?> builder) {
      return this.append((Component)builder.build());
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component appendNewline() {
      return this.append((Component)newline());
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component appendSpace() {
      return this.append((Component)space());
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component applyFallbackStyle(@NotNull Style style) {
      Objects.requireNonNull(style, "style");
      return this.style(this.style().merge(style, Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component applyFallbackStyle(@NotNull StyleBuilderApplicable... style) {
      return this.applyFallbackStyle(Style.style(style));
   }

   @NotNull
   Style style();

   @Contract(
      pure = true
   )
   @NotNull
   Component style(@NotNull Style var1);

   @Contract(
      pure = true
   )
   @NotNull
   default Component style(@NotNull Consumer<Style.Builder> consumer) {
      return this.style(this.style().edit(consumer));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component style(@NotNull Consumer<Style.Builder> consumer, @NotNull Style.Merge.Strategy strategy) {
      return this.style(this.style().edit(consumer, strategy));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component style(@NotNull Style.Builder style) {
      return this.style(style.build());
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component mergeStyle(@NotNull Component that) {
      return this.mergeStyle(that, Style.Merge.all());
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component mergeStyle(@NotNull Component that, @NotNull Style.Merge... merges) {
      return this.mergeStyle(that, Style.Merge.merges(merges));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component mergeStyle(@NotNull Component that, @NotNull Set<Style.Merge> merges) {
      return this.style(this.style().merge(that.style(), merges));
   }

   @Nullable
   default Key font() {
      return this.style().font();
   }

   @NotNull
   default Component font(@Nullable Key key) {
      return this.style(this.style().font(key));
   }

   @Nullable
   default TextColor color() {
      return this.style().color();
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component color(@Nullable TextColor color) {
      return this.style(this.style().color(color));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component colorIfAbsent(@Nullable TextColor color) {
      return this.color() == null ? this.color(color) : this;
   }

   default boolean hasDecoration(@NotNull TextDecoration decoration) {
      return StyleGetter.super.hasDecoration(decoration);
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component decorate(@NotNull TextDecoration decoration) {
      return (Component)StyleSetter.super.decorate(decoration);
   }

   @NotNull
   default TextDecoration.State decoration(@NotNull TextDecoration decoration) {
      return this.style().decoration(decoration);
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component decoration(@NotNull TextDecoration decoration, boolean flag) {
      return (Component)StyleSetter.super.decoration(decoration, flag);
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
      return this.style(this.style().decoration(decoration, state));
   }

   @NotNull
   default Component decorationIfAbsent(@NotNull TextDecoration decoration, TextDecoration.State state) {
      Objects.requireNonNull(state, "state");
      TextDecoration.State oldState = this.decoration(decoration);
      return oldState == TextDecoration.State.NOT_SET ? this.style(this.style().decoration(decoration, state)) : this;
   }

   @NotNull
   default Map<TextDecoration, TextDecoration.State> decorations() {
      return this.style().decorations();
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
      return this.style(this.style().decorations(decorations));
   }

   @Nullable
   default ClickEvent clickEvent() {
      return this.style().clickEvent();
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component clickEvent(@Nullable ClickEvent event) {
      return this.style(this.style().clickEvent(event));
   }

   @Nullable
   default HoverEvent<?> hoverEvent() {
      return this.style().hoverEvent();
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component hoverEvent(@Nullable HoverEventSource<?> source) {
      return this.style(this.style().hoverEvent(source));
   }

   @Nullable
   default String insertion() {
      return this.style().insertion();
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component insertion(@Nullable String insertion) {
      return this.style(this.style().insertion(insertion));
   }

   default boolean hasStyling() {
      return !this.style().isEmpty();
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull Consumer<TextReplacementConfig.Builder> configurer) {
      Objects.requireNonNull(configurer, "configurer");
      return this.replaceText((TextReplacementConfig)AbstractBuilder.configureAndBuild(TextReplacementConfig.builder(), configurer));
   }

   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull TextReplacementConfig config) {
      Objects.requireNonNull(config, "replacement");
      if (!(config instanceof TextReplacementConfigImpl)) {
         throw new IllegalArgumentException("Provided replacement was a custom TextReplacementConfig implementation, which is not supported.");
      } else {
         return TextReplacementRenderer.INSTANCE.render(this, ((TextReplacementConfigImpl)config).createState());
      }
   }

   @NotNull
   default Component compact() {
      return ComponentCompaction.compact(this, (Style)null);
   }

   @NotNull
   default Iterable<Component> iterable(@NotNull ComponentIteratorType type, @NotNull @Nullable ComponentIteratorFlag... flags) {
      return this.iterable(type, flags == null ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
   }

   @NotNull
   default Iterable<Component> iterable(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
      Objects.requireNonNull(type, "type");
      Objects.requireNonNull(flags, "flags");
      return new ForwardingIterator(() -> {
         return this.iterator(type, flags);
      }, () -> {
         return this.spliterator(type, flags);
      });
   }

   @NotNull
   default Iterator<Component> iterator(@NotNull ComponentIteratorType type, @NotNull @Nullable ComponentIteratorFlag... flags) {
      return this.iterator(type, flags == null ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
   }

   @NotNull
   default Iterator<Component> iterator(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
      return new ComponentIterator(this, (ComponentIteratorType)Objects.requireNonNull(type, "type"), (Set)Objects.requireNonNull(flags, "flags"));
   }

   @NotNull
   default Spliterator<Component> spliterator(@NotNull ComponentIteratorType type, @NotNull @Nullable ComponentIteratorFlag... flags) {
      return this.spliterator(type, flags == null ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
   }

   @NotNull
   default Spliterator<Component> spliterator(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
      return Spliterators.spliteratorUnknownSize(this.iterator(type, flags), 1296);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull String search, @Nullable ComponentLike replacement) {
      return this.replaceText((b) -> {
         b.matchLiteral(search).replacement(replacement);
      });
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement) {
      return this.replaceText((b) -> {
         b.match(pattern).replacement(replacement);
      });
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceFirstText(@NotNull String search, @Nullable ComponentLike replacement) {
      return this.replaceText((b) -> {
         b.matchLiteral(search).once().replacement(replacement);
      });
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceFirstText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement) {
      return this.replaceText((b) -> {
         b.match(pattern).once().replacement(replacement);
      });
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull String search, @Nullable ComponentLike replacement, int numberOfReplacements) {
      return this.replaceText((b) -> {
         b.matchLiteral(search).times(numberOfReplacements).replacement(replacement);
      });
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement, int numberOfReplacements) {
      return this.replaceText((b) -> {
         b.match(pattern).times(numberOfReplacements).replacement(replacement);
      });
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull String search, @Nullable ComponentLike replacement, @NotNull IntFunction2<PatternReplacementResult> fn) {
      return this.replaceText((b) -> {
         b.matchLiteral(search).replacement(replacement).condition(fn);
      });
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      pure = true
   )
   @NotNull
   default Component replaceText(@NotNull Pattern pattern, @NotNull Function<TextComponent.Builder, ComponentLike> replacement, @NotNull IntFunction2<PatternReplacementResult> fn) {
      return this.replaceText((b) -> {
         b.match(pattern).replacement(replacement).condition(fn);
      });
   }

   default void componentBuilderApply(@NotNull ComponentBuilder<?, ?> component) {
      component.append(this);
   }

   @NotNull
   default Component asComponent() {
      return this;
   }

   @NotNull
   default HoverEvent<Component> asHoverEvent(@NotNull UnaryOperator<Component> op) {
      return HoverEvent.showText((Component)op.apply(this));
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("style", (Object)this.style()), ExaminableProperty.of("children", (Object)this.children()));
   }
}
