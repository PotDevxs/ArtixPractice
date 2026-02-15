package dev.artixdev.libs.net.kyori.adventure.text.format;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEventSource;
import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.net.kyori.adventure.util.MonkeyBars;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface Style extends StyleGetter, StyleSetter<Style>, Buildable<Style, Style.Builder>, Examinable {
   Key DEFAULT_FONT = Key.key("default");

   @NotNull
   static Style empty() {
      return StyleImpl.EMPTY;
   }

   @NotNull
   static Style.Builder style() {
      return new StyleImpl.BuilderImpl();
   }

   @NotNull
   static Style style(@NotNull Consumer<Style.Builder> consumer) {
      return (Style)AbstractBuilder.configureAndBuild(style(), consumer);
   }

   @NotNull
   static Style style(@Nullable TextColor color) {
      return empty().color(color);
   }

   @NotNull
   static Style style(@NotNull TextDecoration decoration) {
      return style().decoration(decoration, true).build();
   }

   @NotNull
   static Style style(@Nullable TextColor color, @NotNull TextDecoration... decorations) {
      Style.Builder builder = style();
      builder.color(color);
      builder.decorate(decorations);
      return builder.build();
   }

   @NotNull
   static Style style(@Nullable TextColor color, Set<TextDecoration> decorations) {
      Style.Builder builder = style();
      builder.color(color);
      if (!decorations.isEmpty()) {
         Iterator var3 = decorations.iterator();

         while(var3.hasNext()) {
            TextDecoration decoration = (TextDecoration)var3.next();
            builder.decoration(decoration, true);
         }
      }

      return builder.build();
   }

   @NotNull
   static Style style(@NotNull StyleBuilderApplicable... applicables) {
      int length = applicables.length;
      if (length == 0) {
         return empty();
      } else {
         Style.Builder builder = style();

         for(int i = 0; i < length; ++i) {
            StyleBuilderApplicable applicable = applicables[i];
            if (applicable != null) {
               applicable.styleApply(builder);
            }
         }

         return builder.build();
      }
   }

   @NotNull
   static Style style(@NotNull Iterable<? extends StyleBuilderApplicable> applicables) {
      Style.Builder builder = style();
      Iterator var2 = applicables.iterator();

      while(var2.hasNext()) {
         StyleBuilderApplicable applicable = (StyleBuilderApplicable)var2.next();
         applicable.styleApply(builder);
      }

      return builder.build();
   }

   @NotNull
   default Style edit(@NotNull Consumer<Style.Builder> consumer) {
      return this.edit(consumer, Style.Merge.Strategy.ALWAYS);
   }

   @NotNull
   default Style edit(@NotNull Consumer<Style.Builder> consumer, @NotNull Style.Merge.Strategy strategy) {
      return style((style) -> {
         if (strategy == Style.Merge.Strategy.ALWAYS) {
            style.merge(this, strategy);
         }

         consumer.accept(style);
         if (strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET) {
            style.merge(this, strategy);
         }

      });
   }

   @Nullable
   Key font();

   @NotNull
   Style font(@Nullable Key var1);

   @Nullable
   TextColor color();

   @NotNull
   Style color(@Nullable TextColor var1);

   @NotNull
   Style colorIfAbsent(@Nullable TextColor var1);

   default boolean hasDecoration(@NotNull TextDecoration decoration) {
      return StyleGetter.super.hasDecoration(decoration);
   }

   @NotNull
   TextDecoration.State decoration(@NotNull TextDecoration var1);

   @NotNull
   default Style decorate(@NotNull TextDecoration decoration) {
      return (Style)StyleSetter.super.decorate(decoration);
   }

   @NotNull
   default Style decoration(@NotNull TextDecoration decoration, boolean flag) {
      return (Style)StyleSetter.super.decoration(decoration, flag);
   }

   @NotNull
   Style decoration(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

   @NotNull
   Style decorationIfAbsent(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

   @NotNull
   @Unmodifiable
   default Map<TextDecoration, TextDecoration.State> decorations() {
      return StyleGetter.super.decorations();
   }

   @NotNull
   Style decorations(@NotNull Map<TextDecoration, TextDecoration.State> var1);

   @Nullable
   ClickEvent clickEvent();

   @NotNull
   Style clickEvent(@Nullable ClickEvent var1);

   @Nullable
   HoverEvent<?> hoverEvent();

   @NotNull
   Style hoverEvent(@Nullable HoverEventSource<?> var1);

   @Nullable
   String insertion();

   @NotNull
   Style insertion(@Nullable String var1);

   @NotNull
   default Style merge(@NotNull Style that) {
      return this.merge(that, Style.Merge.all());
   }

   @NotNull
   default Style merge(@NotNull Style that, @NotNull Style.Merge.Strategy strategy) {
      return this.merge(that, strategy, Style.Merge.all());
   }

   @NotNull
   default Style merge(@NotNull Style that, @NotNull Style.Merge merge) {
      return this.merge(that, Collections.singleton(merge));
   }

   @NotNull
   default Style merge(@NotNull Style that, @NotNull Style.Merge.Strategy strategy, @NotNull Style.Merge merge) {
      return this.merge(that, strategy, Collections.singleton(merge));
   }

   @NotNull
   default Style merge(@NotNull Style that, @NotNull Style.Merge... merges) {
      return this.merge(that, Style.Merge.merges(merges));
   }

   @NotNull
   default Style merge(@NotNull Style that, @NotNull Style.Merge.Strategy strategy, @NotNull Style.Merge... merges) {
      return this.merge(that, strategy, Style.Merge.merges(merges));
   }

   @NotNull
   default Style merge(@NotNull Style that, @NotNull Set<Style.Merge> merges) {
      return this.merge(that, Style.Merge.Strategy.ALWAYS, merges);
   }

   @NotNull
   Style merge(@NotNull Style var1, @NotNull Style.Merge.Strategy var2, @NotNull Set<Style.Merge> var3);

   @NotNull
   Style unmerge(@NotNull Style var1);

   boolean isEmpty();

   @NotNull
   Style.Builder toBuilder();

   public interface Builder extends AbstractBuilder<Style>, MutableStyleSetter<Style.Builder>, Buildable.Builder<Style> {
      @Contract("_ -> this")
      @NotNull
      Style.Builder font(@Nullable Key var1);

      @Contract("_ -> this")
      @NotNull
      Style.Builder color(@Nullable TextColor var1);

      @Contract("_ -> this")
      @NotNull
      Style.Builder colorIfAbsent(@Nullable TextColor var1);

      @Contract("_ -> this")
      @NotNull
      default Style.Builder decorate(@NotNull TextDecoration decoration) {
         return (Style.Builder)MutableStyleSetter.super.decorate((TextDecoration)decoration);
      }

      @Contract("_ -> this")
      @NotNull
      default Style.Builder decorate(@NotNull TextDecoration... decorations) {
         return (Style.Builder)MutableStyleSetter.super.decorate(decorations);
      }

      @Contract("_, _ -> this")
      @NotNull
      default Style.Builder decoration(@NotNull TextDecoration decoration, boolean flag) {
         return (Style.Builder)MutableStyleSetter.super.decoration(decoration, flag);
      }

      @Contract("_ -> this")
      @NotNull
      default Style.Builder decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
         return (Style.Builder)MutableStyleSetter.super.decorations(decorations);
      }

      @Contract("_, _ -> this")
      @NotNull
      Style.Builder decoration(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

      @Contract("_, _ -> this")
      @NotNull
      Style.Builder decorationIfAbsent(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

      @Contract("_ -> this")
      @NotNull
      Style.Builder clickEvent(@Nullable ClickEvent var1);

      @Contract("_ -> this")
      @NotNull
      Style.Builder hoverEvent(@Nullable HoverEventSource<?> var1);

      @Contract("_ -> this")
      @NotNull
      Style.Builder insertion(@Nullable String var1);

      @Contract("_ -> this")
      @NotNull
      default Style.Builder merge(@NotNull Style that) {
         return this.merge(that, Style.Merge.all());
      }

      @Contract("_, _ -> this")
      @NotNull
      default Style.Builder merge(@NotNull Style that, @NotNull Style.Merge.Strategy strategy) {
         return this.merge(that, strategy, Style.Merge.all());
      }

      @Contract("_, _ -> this")
      @NotNull
      default Style.Builder merge(@NotNull Style that, @NotNull Style.Merge... merges) {
         return merges.length == 0 ? this : this.merge(that, Style.Merge.merges(merges));
      }

      @Contract("_, _, _ -> this")
      @NotNull
      default Style.Builder merge(@NotNull Style that, @NotNull Style.Merge.Strategy strategy, @NotNull Style.Merge... merges) {
         return merges.length == 0 ? this : this.merge(that, strategy, Style.Merge.merges(merges));
      }

      @Contract("_, _ -> this")
      @NotNull
      default Style.Builder merge(@NotNull Style that, @NotNull Set<Style.Merge> merges) {
         return this.merge(that, Style.Merge.Strategy.ALWAYS, merges);
      }

      @Contract("_, _, _ -> this")
      @NotNull
      Style.Builder merge(@NotNull Style var1, @NotNull Style.Merge.Strategy var2, @NotNull Set<Style.Merge> var3);

      @Contract("_ -> this")
      @NotNull
      default Style.Builder apply(@NotNull StyleBuilderApplicable applicable) {
         applicable.styleApply(this);
         return this;
      }

      @NotNull
      Style build();
   }

   public static enum Merge {
      COLOR,
      DECORATIONS,
      EVENTS,
      INSERTION,
      FONT;

      static final Set<Style.Merge> ALL = merges(values());
      static final Set<Style.Merge> COLOR_AND_DECORATIONS = merges(COLOR, DECORATIONS);

      @NotNull
      @Unmodifiable
      public static Set<Style.Merge> all() {
         return ALL;
      }

      @NotNull
      @Unmodifiable
      public static Set<Style.Merge> colorAndDecorations() {
         return COLOR_AND_DECORATIONS;
      }

      @NotNull
      @Unmodifiable
      public static Set<Style.Merge> merges(@NotNull Style.Merge... merges) {
         return MonkeyBars.enumSet(Style.Merge.class, merges);
      }

      /** @deprecated */
      @Deprecated
      @ApiStatus.ScheduledForRemoval(
         inVersion = "5.0.0"
      )
      @NotNull
      @Unmodifiable
      public static Set<Style.Merge> of(@NotNull Style.Merge... merges) {
         return MonkeyBars.enumSet(Style.Merge.class, merges);
      }

      static boolean hasAll(@NotNull Set<Style.Merge> merges) {
         return merges.size() == ALL.size();
      }

      public static enum Strategy {
         ALWAYS,
         NEVER,
         IF_ABSENT_ON_TARGET;
      }
   }
}
